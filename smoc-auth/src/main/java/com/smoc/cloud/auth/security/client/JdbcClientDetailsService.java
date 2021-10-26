package com.smoc.cloud.auth.security.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoc.cloud.auth.common.utils.MpmEncryptPasswordEncoder;
import com.smoc.cloud.common.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.util.DefaultJdbcListFactory;
import org.springframework.security.oauth2.common.util.JdbcListFactory;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 扩展JdbcClientDetailsService  支持Redis查询
 * 2019/5/30 13:49
 **/
@Slf4j
public class JdbcClientDetailsService implements ClientDetailsService, ClientRegistrationService {

    @Resource
    private RedisTemplate<String, ClientDetails> redisTemplate;

    private JdbcClientDetailsService.JsonMapper mapper = createJsonMapper();
    private static final String CLIENT_FIELDS_FOR_UPDATE = "resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove";
    private static final String CLIENT_FIELDS = "client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove";
    private static final String BASE_FIND_STATEMENT = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details";
    private static final String DEFAULT_FIND_STATEMENT = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details order by client_id";
    private static final String DEFAULT_SELECT_STATEMENT = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?";
    private static final String DEFAULT_INSERT_STATEMENT = "insert into oauth_client_details (client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, client_id) values (?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DEFAULT_UPDATE_STATEMENT = "update oauth_client_details set " + "resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove".replaceAll(", ", "=?, ") + "=? where client_id = ?";
    private static final String DEFAULT_UPDATE_SECRET_STATEMENT = "update oauth_client_details set client_secret = ? where client_id = ?";
    private static final String DEFAULT_DELETE_STATEMENT = "delete from oauth_client_details where client_id = ?";
    private RowMapper<ClientDetails> rowMapper = new JdbcClientDetailsService.ClientDetailsRowMapper();
    private String deleteClientDetailsSql = "delete from oauth_client_details where client_id = ?";
    private String findClientDetailsSql = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details order by client_id";
    private String updateClientDetailsSql;
    private String updateClientSecretSql;
    private String insertClientDetailsSql;
    private String selectClientDetailsSql;
    private PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private JdbcListFactory listFactory;

    public JdbcClientDetailsService(DataSource dataSource) {
        this.updateClientDetailsSql = DEFAULT_UPDATE_STATEMENT;
        this.updateClientSecretSql = "update oauth_client_details set client_secret = ? where client_id = ?";
        this.insertClientDetailsSql = "insert into oauth_client_details (client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, client_id) values (?,?,?,?,?,?,?,?,?,?,?)";
        this.selectClientDetailsSql = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?";
        this.passwordEncoder = MpmEncryptPasswordEncoder.getPasswordEncoder();
        Assert.notNull(dataSource, "DataSource required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.listFactory = new DefaultJdbcListFactory(new NamedParameterJdbcTemplate(this.jdbcTemplate));
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        try {
            ClientDetails details = null;
            //判断是否存在key
            boolean hasKey = redisTemplate.hasKey(RedisConstant.AUTH_USER_CLIENT + ":" + clientId);
            if (hasKey) {
                //redis查询
                details = redisTemplate.opsForValue().get(RedisConstant.AUTH_USER_CLIENT + ":" + clientId);
            } else {
                //数据库查询
                details = this.jdbcTemplate.queryForObject(this.selectClientDetailsSql, new ClientDetailsRowMapper(), new Object[]{clientId});

                if (null != details) {
                    //把client信息放到redis中
                    redisTemplate.opsForValue().set(RedisConstant.AUTH_USER_CLIENT + ":" + clientId, details);
                }
            }
            return details;
        } catch (EmptyResultDataAccessException var4) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        try {
            this.jdbcTemplate.update(this.insertClientDetailsSql, this.getFields(clientDetails));
        } catch (DuplicateKeyException var3) {
            throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId(), var3);
        }
    }

    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        int count = this.jdbcTemplate.update(this.updateClientDetailsSql, this.getFieldsForUpdate(clientDetails));
        if (count != 1) {
            throw new NoSuchClientException("No client found with id = " + clientDetails.getClientId());
        }
    }

    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        int count = this.jdbcTemplate.update(this.updateClientSecretSql, this.passwordEncoder.encode(secret), clientId);
        if (count != 1) {
            throw new NoSuchClientException("No client found with id = " + clientId);
        }
    }

    public void removeClientDetails(String clientId) throws NoSuchClientException {
        int count = this.jdbcTemplate.update(this.deleteClientDetailsSql, clientId);
        if (count != 1) {
            throw new NoSuchClientException("No client found with id = " + clientId);
        }
    }

    public List<ClientDetails> listClientDetails() {
        return this.listFactory.getList(this.findClientDetailsSql, Collections.emptyMap(), this.rowMapper);
    }

    private Object[] getFields(ClientDetails clientDetails) {
        Object[] fieldsForUpdate = this.getFieldsForUpdate(clientDetails);
        Object[] fields = new Object[fieldsForUpdate.length + 1];
        System.arraycopy(fieldsForUpdate, 0, fields, 1, fieldsForUpdate.length);
        fields[0] = clientDetails.getClientSecret() != null ? this.passwordEncoder.encode(clientDetails.getClientSecret()) : null;
        return fields;
    }

    private Object[] getFieldsForUpdate(ClientDetails clientDetails) {
        String json = null;

        try {
            json = this.mapper.write(clientDetails.getAdditionalInformation());
        } catch (Exception var4) {
            log.warn("Could not serialize additional information: " + clientDetails, var4);
        }

        return new Object[]{clientDetails.getResourceIds() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getResourceIds()) : null, clientDetails.getScope() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getScope()) : null, clientDetails.getAuthorizedGrantTypes() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorizedGrantTypes()) : null, clientDetails.getRegisteredRedirectUri() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getRegisteredRedirectUri()) : null, clientDetails.getAuthorities() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorities()) : null, clientDetails.getAccessTokenValiditySeconds(), clientDetails.getRefreshTokenValiditySeconds(), json, this.getAutoApproveScopes(clientDetails), clientDetails.getClientId()};
    }

    private String getAutoApproveScopes(ClientDetails clientDetails) {
        if (clientDetails.isAutoApprove("true")) {
            return "true";
        } else {
            Set<String> scopes = new HashSet();
            Iterator var3 = clientDetails.getScope().iterator();

            while (var3.hasNext()) {
                String scope = (String) var3.next();
                if (clientDetails.isAutoApprove(scope)) {
                    scopes.add(scope);
                }
            }

            return StringUtils.collectionToCommaDelimitedString(scopes);
        }
    }

    public void setSelectClientDetailsSql(String selectClientDetailsSql) {
        this.selectClientDetailsSql = selectClientDetailsSql;
    }

    public void setDeleteClientDetailsSql(String deleteClientDetailsSql) {
        this.deleteClientDetailsSql = deleteClientDetailsSql;
    }

    public void setUpdateClientDetailsSql(String updateClientDetailsSql) {
        this.updateClientDetailsSql = updateClientDetailsSql;
    }

    public void setUpdateClientSecretSql(String updateClientSecretSql) {
        this.updateClientSecretSql = updateClientSecretSql;
    }

    public void setInsertClientDetailsSql(String insertClientDetailsSql) {
        this.insertClientDetailsSql = insertClientDetailsSql;
    }

    public void setFindClientDetailsSql(String findClientDetailsSql) {
        this.findClientDetailsSql = findClientDetailsSql;
    }

    public void setListFactory(JdbcListFactory listFactory) {
        this.listFactory = listFactory;
    }

    public void setRowMapper(RowMapper<ClientDetails> rowMapper) {
        this.rowMapper = rowMapper;
    }

    private static JdbcClientDetailsService.JsonMapper createJsonMapper() {
        if (ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", null)) {
            return new JdbcClientDetailsService.JacksonMapper();
        } else {
            return (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null) ? new Jackson2Mapper() : new NotSupportedJsonMapper());
        }
    }

    private static class NotSupportedJsonMapper implements JdbcClientDetailsService.JsonMapper {
        private NotSupportedJsonMapper() {
        }

        public String write(Object input) throws Exception {
            throw new UnsupportedOperationException("Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
        }

        public <T> T read(String input, Class<T> type) throws Exception {
            throw new UnsupportedOperationException("Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
        }
    }

    private static class Jackson2Mapper implements JdbcClientDetailsService.JsonMapper {
        private ObjectMapper mapper;

        private Jackson2Mapper() {
            this.mapper = new ObjectMapper();
        }

        public String write(Object input) throws Exception {
            return this.mapper.writeValueAsString(input);
        }

        public <T> T read(String input, Class<T> type) throws Exception {
            return this.mapper.readValue(input, type);
        }
    }

    private static class JacksonMapper implements JdbcClientDetailsService.JsonMapper {
        private org.codehaus.jackson.map.ObjectMapper mapper;

        private JacksonMapper() {
            this.mapper = new org.codehaus.jackson.map.ObjectMapper();
        }

        public String write(Object input) throws Exception {
            return this.mapper.writeValueAsString(input);
        }

        public <T> T read(String input, Class<T> type) throws Exception {
            return this.mapper.readValue(input, type);
        }
    }

    interface JsonMapper {
        String write(Object var1) throws Exception;

        <T> T read(String var1, Class<T> var2) throws Exception;
    }

    private static class ClientDetailsRowMapper implements RowMapper<ClientDetails> {
        private JdbcClientDetailsService.JsonMapper mapper;

        private ClientDetailsRowMapper() {
            this.mapper = JdbcClientDetailsService.createJsonMapper();
        }

        public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            BaseClientDetails details = new BaseClientDetails(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(7), rs.getString(6));
            details.setClientSecret(rs.getString(2));
            if (rs.getObject(8) != null) {
                details.setAccessTokenValiditySeconds(rs.getInt(8));
            }

            if (rs.getObject(9) != null) {
                details.setRefreshTokenValiditySeconds(rs.getInt(9));
            }

            String json = rs.getString(10);
            if (json != null) {
                try {
                    Map<String, Object> additionalInformation = (Map) this.mapper.read(json, Map.class);
                    details.setAdditionalInformation(additionalInformation);
                } catch (Exception var6) {
                    JdbcClientDetailsService.log.warn("Could not decode JSON for additional information: " + details, var6);
                }
            }

            String scopes = rs.getString(11);
            if (scopes != null) {
                details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes));
            }

            return details;
        }
    }
}
