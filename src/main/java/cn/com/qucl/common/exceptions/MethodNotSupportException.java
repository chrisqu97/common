package cn.com.qucl.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2020/11/27 16:48
 * 方法不支持异常
 */
public class MethodNotSupportException extends BaseException {
    public MethodNotSupportException(String message) {
        super(message);
        setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
