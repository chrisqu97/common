package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2020/9/16 22:20
 * 权限异常
 */
public class PermissionException extends BaseException {
    public PermissionException(String message) {
        super(message);
        setCode(ResponseStatusEnum.UNAUTHORIZED.code());
    }

    public PermissionException() {
        super("权限不足");
        setCode(ResponseStatusEnum.UNAUTHORIZED.code());
    }
}
