package com.atguigu.security.filter;

import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.utils.ResponseUtil;
import com.atguigu.security.custom.CustomUser;
import com.atguigu.vo.system.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录过滤器，继承UsernamePasswordAuthenticationFilter，对用户名和密码进行登录校验
 * @author 一只鱼zzz
 * @version 1.0
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

  //构造方法
  public TokenLoginFilter(AuthenticationManager authenticationManager) {
    this.setAuthenticationManager(authenticationManager);
    this.setPostOnly(false);
    //指定登录接口及提交方式，可以指定任意路径
    this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
  }

  /**
   * 登录认证，获取输入的用户名和密码，调用方法认证
   * @param req
   * @param res
   * @return
   * @throws AuthenticationException
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
    throws AuthenticationException {
    try {
      LoginVo loginVo = new ObjectMapper().readValue(req.getInputStream(), LoginVo.class);

      Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
      return this.getAuthenticationManager().authenticate(authenticationToken);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * 登录成功调用方法
   * @param request
   * @param response
   * @param chain
   * @param auth
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication auth) throws IOException, ServletException {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());

    Map<String, Object> map = new HashMap<>();
    map.put("token", token);
    ResponseUtil.out(response, Result.ok(map));
  }

  /**
   * 登录失败调用方法
   * @param request
   * @param response
   * @param e
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException e) throws IOException, ServletException {

    if(e.getCause() instanceof RuntimeException) {
      ResponseUtil.out(response, Result.build(null, 204, e.getMessage()));
    } else {
      ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
    }
  }
}
