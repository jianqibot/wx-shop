package com.github.jianqibot.wxshop.entity;

import java.util.List;

public class PageResponse<T> {
    private int pageNum;
    private int pageSize;
    private int totalPage;
    private List<T> data;
    private String message;

    public static <T> PageResponse<T> of(int pageNum, int pageSize, int totalPage, List<T> data, String message) {
        return new PageResponse<>(pageNum, pageSize, totalPage, data, message);
    }

    public PageResponse(int pageNum, int pageSize, int totalPage, List<T> data, String message) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.data = data;
        this.message = message;
    }

    public PageResponse() {
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
