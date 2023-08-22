package com.atguigu.security.filter;

import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.utils.ResponseUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * <p>
 * 认证解析token过滤器
 * </p>
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  public TokenAuthenticationFilter() {

  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {
    logger.info("uri:"+request.getRequestURI());
    //如果是登录接口，直接放行
    if("/admin/system/index/login".equals(request.getRequestURI())) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
    if(null != authentication) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    } else {
      ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
    }
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    // token置于header里
    String token = request.getHeader("token");
    logger.info("token:"+token);
    if (!StringUtils.isEmpty(token)) {
      String useruame = JwtHelper.getUsername(token);
      logger.info("useruame:"+useruame);
      if (!StringUtils.isEmpty(useruame)) {
        return new UsernamePasswordAuthenticationToken(useruame, null, Collections.emptyList());
      }
    }
    return null;
  }
}
