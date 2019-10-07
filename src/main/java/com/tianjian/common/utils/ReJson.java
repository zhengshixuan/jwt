package com.tianjian.common.utils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 返回数据
 */
public class ReJson extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    public ReJson() {
        put("code", 0);
        put("msg", "success");
    }

    /**
     * 返回错误代码和信息
     *
     * @param code 错误代码
     * @param msg  错误消息
     * @return
     */
    public static ReJson error(int code, String msg) {
        ReJson r = new ReJson();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    /**
     * 通用异常
     *
     * @return
     */
    public static ReJson error(String msg) {
        return error(1, msg);
    }

    /**
     * 返回正常代码和消息
     *
     * @param msg 消息
     * @return
     */
    public static ReJson ok(String msg) {
        ReJson r = new ReJson();
        r.put("msg", msg);
        return r;
    }

    /**
     * 添加其他数据
     *
     * @param key   key
     * @param value value
     * @return
     */
    public ReJson put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
