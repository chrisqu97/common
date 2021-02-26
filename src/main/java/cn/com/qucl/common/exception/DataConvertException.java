package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2018/11/28 18:02
 * 数据转换异常
 */
public class DataConvertException extends BaseException {
    public DataConvertException(String message) {
        super(message);
        setCode(ResponseStatusEnum.INTERNAL_SERVER_ERROR.code());
    }
}
