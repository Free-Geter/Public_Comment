package com.chenliang.public_comment.entity;

public class ResponseObject<T> {

    private String state;
    private T datas;

    public ResponseObject() {
    }

    public ResponseObject(String state, T datas) {
        this.state = state;
        this.datas = datas;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }
}
