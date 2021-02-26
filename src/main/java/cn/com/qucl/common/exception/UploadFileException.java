package cn.com.qucl.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2020/12/30 16:42
 * 文件上传异常
 */
public class UploadFileException extends BaseException {
    public UploadFileException(String message) {
        super(message);
        setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public UploadFileException() {
        super("文件上传异常");
        setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
