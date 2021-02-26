package cn.com.qucl.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2020/9/16 22:20
 * 权限异常
 */
public class PermissionException extends BaseException {
    public PermissionException(String message) {
        super(message);
        setCode(HttpStatus.UNAUTHORIZED.value());
    }

    public PermissionException() {
        super("权限不足");
        setCode(HttpStatus.UNAUTHORIZED.value());
    }
}
