package cn.com.qucl.common.pojo;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2018/11/2 16:12
 * 接口默认返回格式
 */
public class Result<T> {
    /**
     * 成功
     */
    private Boolean success;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 消息
     */
    private String message;
    /**
     * 内容
     */
    private T data;

    private static <T> Result<T> newInstance(Boolean success, Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.success = success;
        result.message = message;
        result.data = data;
        result.timestamp = System.currentTimeMillis();
        return result;
    }

    /**
     * 返回成功
     */
    public static Result<?> success() {
        return newInstance(Boolean.TRUE, ResponseStatusEnum.OK.code(), ResponseStatusEnum.OK.message(), null);
    }

    /**
     * 返回成功
     *
     * @param message 消息
     */
    public static Result<?> success(String message) {
        return newInstance(Boolean.TRUE, ResponseStatusEnum.OK.code(), message, null);
    }


    /**
     * 返回成功
     *
     * @param message 消息
     * @param data    数据
     */
    public static <T> Result<T> success(String message, T data) {
        return newInstance(Boolean.TRUE, ResponseStatusEnum.OK.code(), message, data);
    }

    /**
     * 失败
     *
     * @param message 消息
     */
    public static Result<?> error(String message) {
        return newInstance(Boolean.FALSE, ResponseStatusEnum.INTERNAL_SERVER_ERROR.code(), message, null);
    }

    /**
     * 失败
     *
     * @param code    错误码
     * @param message 消息
     */
    public static Result<?> error(Integer code, String message) {
        return newInstance(Boolean.FALSE, code, message, null);
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
