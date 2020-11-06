package cn.com.qucl.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2018/11/27 11:46
 * 数据未找到
 */
public class DataNotFoundException extends BaseException {
    public DataNotFoundException(String message) {
        super(message);
        setCode(HttpStatus.NOT_FOUND.value());
    }
}
