package com.atguigu.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */
@Service
public interface UserDetailsService {
  /**
   * 根据用户名获取用户对象（获取不到直接抛异常）
   */
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
