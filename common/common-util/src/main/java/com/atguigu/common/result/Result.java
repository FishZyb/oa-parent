package com.atguigu.common.result;

import lombok.Data;

/**
 * @author 一只鱼zzz
 * @version 1.0
 * 统一结果返回类
 */
@Data
public class Result<T>{

  private Integer code;//状态码

  private String message;//返回信息

  private T data;//具体的数据

  //私有化
  private Result(){}

  //返回数据
  protected static<T> Result<T> build(T data){
    Result<T> result = new Result<>();
    if(data!=null){
      result.setData(data);
    }
    return result;
  }

  public static <T> Result<T> build(T body, Integer code, String message) {
    Result<T> result = build(body);
    result.setCode(code);
    result.setMessage(message);
    return result;
  }

  public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
    Result<T> result = build(body);
    result.setCode(resultCodeEnum.getCode());
    result.setMessage(resultCodeEnum.getMessage());
    return result;
  }

  public static<T> Result<T> ok(){
    return Result.ok(null);
  }

  //操作成功
  public static<T> Result<T> ok(T data){
    Result<T> result = build(data);
    return build(data,ResultCodeEnum.SUCCESS);
  }

  //操作失败
  public static<T> Result<T> fail(T data){
    Result<T> result = build(data);
    return build(data, ResultCodeEnum.FAIL);
  }

  public static<T> Result<T> fail(){
    return Result.fail(null);
  }

  public Result<T> message(String msg){
    this.setMessage(msg);
    return this;
  }

  public Result<T> code(Integer code){
    this.setCode(code);
    return this;
  }


}
