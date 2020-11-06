package cn.com.qucl.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2018/11/28 18:02
 * 数据转换异常
 */
public class DataConvertException extends BaseException {
    public DataConvertException(String message) {
        super(message);
        setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
