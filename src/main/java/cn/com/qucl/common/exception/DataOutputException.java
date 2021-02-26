package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2019/1/11 15:07
 * 数据导出异常
 */
public class DataOutputException extends BaseException{
    public DataOutputException(String message) {
        super(message);
        setCode(ResponseStatusEnum.INTERNAL_SERVER_ERROR.code());
    }
}
