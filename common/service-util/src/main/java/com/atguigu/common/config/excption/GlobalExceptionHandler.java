package com.atguigu.common.config.excption;

import com.atguigu.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */

@ControllerAdvice
public class GlobalExceptionHandler {

  //全局异常处理，执行的方法
  @ResponseBody
  @ExceptionHandler(Exception.class)
  public Result error(Exception e){
    e.printStackTrace();
    return Result.fail().message("执行了全局异常处理...");
  }
}
