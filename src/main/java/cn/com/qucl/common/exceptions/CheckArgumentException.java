package cn.com.qucl.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2018/11/20 10:50
 * 参数检查异常
 */
public class CheckArgumentException extends BaseException {
    public CheckArgumentException(String message) {
        super(message);
        setCode(HttpStatus.BAD_REQUEST.value());
    }
}
