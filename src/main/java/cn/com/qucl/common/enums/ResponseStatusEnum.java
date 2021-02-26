package cn.com.qucl.common.enums;

/**
 * @author qucl
 * @date 2021/2/26 15:11
 * 返回状态
 */
public enum ResponseStatusEnum {

    /**
     * 成功
     */
    OK(200, "OK"),
    /**
     * 请求异常
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 验证失败
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * 未找到
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * 系统内部异常
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 消息
     */
    private final String message;

    ResponseStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
