package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2018/11/20 10:50
 * 参数检查异常
 */
public class CheckArgumentException extends BaseException {
    public CheckArgumentException(String message) {
        super(message);
        setCode(ResponseStatusEnum.BAD_REQUEST.code());
    }
}
