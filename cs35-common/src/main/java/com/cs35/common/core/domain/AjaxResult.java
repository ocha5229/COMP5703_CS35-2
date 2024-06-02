package com.cs35.common.core.domain;

import com.cs35.common.constant.HttpStatus;
import com.cs35.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * 
 *
 * @author cs35
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final String CODE_TAG = "code";

    /**
     * 
     */
    public static final String MSG_TAG = "msg";

    /**
     * 
     */
    public static final String DATA_TAG = "data";

    /**
     *  AjaxResult ，。
     */
    public AjaxResult() {
    }

    /**
     *  AjaxResult 
     *
     * @param code 
     * @param msg  
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     *  AjaxResult 
     *
     * @param code 
     * @param msg  
     * @param data 
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 
     *
     * @return 
     */
    public static AjaxResult success() {
        return AjaxResult.success("Operation successful");
    }

    /**
     * 
     *
     * @return 
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("Operation successful", data);
    }

    /**
     * 
     *
     * @param msg 
     * @return 
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 
     *
     * @param msg  
     * @param data 
     * @return 
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 
     *
     * @param msg 
     * @return 
     */
    public static AjaxResult warn(String msg) {
        return AjaxResult.warn(msg, null);
    }

    /**
     * 
     *
     * @param msg  
     * @param data 
     * @return 
     */
    public static AjaxResult warn(String msg, Object data) {
        return new AjaxResult(HttpStatus.WARN, msg, data);
    }

    /**
     * 
     *
     * @return 
     */
    public static AjaxResult error() {
        return AjaxResult.error("operation failed");
    }

    /**
     * 
     *
     * @param msg 
     * @return 
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 
     *
     * @param msg  
     * @param data 
     * @return 
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 
     *
     * @param code 
     * @param msg  
     * @return 
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }

    /**
     * 
     *
     * @return 
     */
    public boolean isSuccess() {
        return Objects.equals(HttpStatus.SUCCESS, this.get(CODE_TAG));
    }

    /**
     * 
     *
     * @return 
     */
    public boolean isWarn() {
        return Objects.equals(HttpStatus.WARN, this.get(CODE_TAG));
    }

    /**
     * 
     *
     * @return 
     */
    public boolean isError() {
        return Objects.equals(HttpStatus.ERROR, this.get(CODE_TAG));
    }

    /**
     * 
     *
     * @param key   
     * @param value 
     * @return 
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
