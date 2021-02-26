package cn.com.qucl.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2020/12/30 17:02
 * 文件下载异常
 */
public class DownloadException extends BaseException {
    public DownloadException(String message) {
        super(message);
        setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public DownloadException() {
        super("文件下载失败");
        setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
