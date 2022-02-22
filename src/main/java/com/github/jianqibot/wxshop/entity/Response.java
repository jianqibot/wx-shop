package com.github.jianqibot.wxshop.entity;

public class Response<T> {
    private String message;
    private T data;

    public static <T> Response<T> of(T data) {
        return new Response<>(null, data);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(message, null);
    }

    private Response(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public Response() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
