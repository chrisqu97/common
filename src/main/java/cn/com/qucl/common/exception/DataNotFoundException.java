package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2018/11/27 11:46
 * 数据未找到
 */
public class DataNotFoundException extends BaseException {
    public DataNotFoundException(String message) {
        super(message);
        setCode(ResponseStatusEnum.NOT_FOUND.code());
    }
}
