package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2020/11/27 16:48
 * 方法不支持异常
 */
public class MethodNotSupportException extends BaseException {
    public MethodNotSupportException(String message) {
        super(message);
        setCode(ResponseStatusEnum.INTERNAL_SERVER_ERROR.code());
    }
}
