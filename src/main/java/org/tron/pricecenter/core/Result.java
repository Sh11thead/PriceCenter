package org.tron.pricecenter.core;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应结果封装
 */

@NoArgsConstructor
public class Result<T> {

  @Data
  @NoArgsConstructor
  public static class Status {

    private int error_code = 0;
    private String error_message;
  }

  private Status status = new Status();
  private T data;

  public Result setCode(ResultCode resultCode) {
    this.status.error_code = resultCode.code();
    return this;
  }

  public Result setMessage(String message) {
    this.status.error_message = message;
    return this;
  }

  public Result(T data) {
    this.data = data;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public T getData() {
    return data;
  }

  public Result setData(T data) {
    this.data = data;
    return this;
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
