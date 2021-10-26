package com.smoc.cloud.admin.sso.provider;


import com.smoc.cloud.admin.sso.token.PhoneAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by fp295 on 2018/11/25.4
 * 手机验证码登陆
 */
@Slf4j
public class PhoneAuthenticationProvider extends MpmAbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {

    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        PhoneAuthenticationToken result = new PhoneAuthenticationToken(principal, authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    protected UserDetails retrieveUser(String phone, Authentication authentication) throws AuthenticationException {
        UserDetails loadedUser;

        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(phone);
        } catch (UsernameNotFoundException var6) {
            throw var6;
        } catch (Exception var7) {
            throw new InternalAuthenticationServiceException(var7.getMessage(), var7);
        }

        if(loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        } else {
            return loadedUser;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PhoneAuthenticationToken.class.isAssignableFrom(authentication);
    }


    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
