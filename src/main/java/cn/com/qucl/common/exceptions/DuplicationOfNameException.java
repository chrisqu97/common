package cn.com.qucl.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2020/9/16 22:20
 * 名称重复
 */
public class DuplicationOfNameException extends BaseException {
    public DuplicationOfNameException(String message) {
        super(message);
        setCode(HttpStatus.BAD_REQUEST.value());
    }

    public DuplicationOfNameException() {
        super("名称重复");
        setCode(HttpStatus.BAD_REQUEST.value());
    }
}
