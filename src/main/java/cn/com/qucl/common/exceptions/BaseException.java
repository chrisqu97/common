package cn.com.qucl.common.exceptions;

/**
 * @author qucl
 * @date 2020/9/16 22:19
 */
public class BaseException extends RuntimeException {
    private Integer code;

    public BaseException(String message) {
        super(message);
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
