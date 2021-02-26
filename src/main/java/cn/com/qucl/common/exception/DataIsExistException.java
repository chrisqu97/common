package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2018/11/27 11:46
 * 数据已存在
 */
public class DataIsExistException extends BaseException {
    public DataIsExistException(String message) {
        super(message);
        setCode(ResponseStatusEnum.BAD_REQUEST.code());
    }
}
