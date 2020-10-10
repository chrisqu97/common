package cn.com.qucl.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2020/9/16 22:20
 * 重复添加
 */
public class DuplicationOfAddException extends BaseException {
    public DuplicationOfAddException(String message) {
        super(message);
        setCode(HttpStatus.BAD_REQUEST.value());
    }

    public DuplicationOfAddException() {
        super("重复添加");
        setCode(HttpStatus.BAD_REQUEST.value());
    }
}
