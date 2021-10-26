package com.smoc.cloud.admin.security.provider;

import com.smoc.cloud.admin.security.remote.service.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户自有权限提供者，加载用户数据库权限
 * <p>
 * Description:用户自有权限提供者，加载用户数据库权限，生成UsernamePasswordAuthenticationToken
 * </p>
 */
@Slf4j
public class MpmAuthenticationProvider implements AuthenticationProvider {


	private UserDetailsService userDetailsService;

	public MpmAuthenticationProvider(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

		String username = token.getName();
		// 从数据库找到的用户
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		if (username != null) {
			userDetails = userDetailsService.loadUserByUsername(username);
		}

		// 授权
		return new UsernamePasswordAuthenticationToken(userDetails,username, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// 返回true后才会执行上面的authenticate方法,这步能确保authentication能正确转换类型
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
}
