package com.smoc.cloud.admin.security.remote.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 重写UserDetailsService，为了防止在Autowired 注入的时候和spring security 时冲突
 * 因为要重写UserDetailsService的实现类
 */
public interface IUserDetailsService extends UserDetailsService {

}
