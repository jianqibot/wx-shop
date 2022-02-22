package com.github.jianqibot.wxshop.entity;

public class HttpException extends RuntimeException{
    private int statusCode;
    private String message;

    public static HttpException notAuthorized(String message){
        return new HttpException(message, 403);
    }

    public static HttpException notFound(String message){
        return new HttpException(message, 404);
    }

    private HttpException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
