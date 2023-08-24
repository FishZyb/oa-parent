package com.atguigu.security.filter;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 认证解析token过滤器
 * </p>
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private RedisTemplate redisTemplate;

  public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
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
        //用过username从redis中获取权限数据
        String authString = (String) redisTemplate.opsForValue().get(useruame);
        //把redis中获取到的字符串权限数据转换为要求的集合类型List<SimpleGrantedAuthority>
        if(!StringUtils.isEmpty(authString)){
          List<Map> mapList = JSON.parseArray(authString, Map.class);
          System.out.println(mapList);
          List<SimpleGrantedAuthority> authList = new ArrayList<>();
          for(Map map:mapList){
            String authority = (String)map.get("authority");
            authList.add(new SimpleGrantedAuthority(authority));
          }
          return new UsernamePasswordAuthenticationToken(useruame, null, authList);
        }else{
          return new UsernamePasswordAuthenticationToken(useruame, null, new ArrayList<>());
        }

      }
    }
    return null;
  }
}
