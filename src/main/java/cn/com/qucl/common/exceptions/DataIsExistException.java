package cn.com.qucl.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2018/11/27 11:46
 * 数据已存在
 */
public class DataIsExistException extends BaseException {
    public DataIsExistException(String message) {
        super(message);
        setCode(HttpStatus.BAD_REQUEST.value());
    }
}
