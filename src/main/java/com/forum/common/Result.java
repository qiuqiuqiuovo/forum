package com.forum.common;

import lombok.Data;

@Data
public class Result {
    private Integer code;   // 200 成功 / 400 业务失败 / 500 服务器炸了
    private String msg;     // 人话说明
    private Object data;    // 真正的数据（可以没有）

    public static Result success() {
        Result r = new Result();
        r.code = 200;
        r.msg = "成功";
        return r;
    }

    public static Result success(Object data) {
        Result r = success();
        r.data = data;
        return r;
    }

    public static Result error(Integer code, String msg) {
        Result r = new Result();
        r.code = code;
        r.msg = msg;
        return r;
    }
}