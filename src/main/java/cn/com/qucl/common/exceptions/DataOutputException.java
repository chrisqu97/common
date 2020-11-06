package cn.com.qucl.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2019/1/11 15:07
 * 数据导出异常
 */
public class DataOutputException extends BaseException{
    public DataOutputException(String message) {
        super(message);
        setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
