package com.atguigu.common.result;

import lombok.Getter;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */

@Getter
public enum ResultCodeEnum {

  SUCCESS(200,"成功"),
  FAIL(201, "失败")
  ;

  private Integer code;

  private String message;

  private ResultCodeEnum(Integer code,String message){
    this.code = code;
    this.message = message;
  }

}
