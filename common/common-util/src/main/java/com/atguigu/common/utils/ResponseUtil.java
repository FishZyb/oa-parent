package com.atguigu.common.utils;

import com.atguigu.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */
public class ResponseUtil {
  public static void out(HttpServletResponse response, Result r) {
    ObjectMapper mapper = new ObjectMapper();
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    try {
      mapper.writeValue(response.getWriter(), r);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
