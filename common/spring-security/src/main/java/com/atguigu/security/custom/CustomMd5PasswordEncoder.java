package com.atguigu.security.custom;

import com.atguigu.common.utils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码处理，加密器
 * @author 一只鱼zzz
 * @version 1.0
 */
@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {

  @Override
  public String encode(CharSequence rawPassword) {
    return MD5.encrypt(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
  }
}
