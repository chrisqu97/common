package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2020/12/30 17:02
 * 文件下载异常
 */
public class DownloadException extends BaseException {
    public DownloadException(String message) {
        super(message);
        setCode(ResponseStatusEnum.INTERNAL_SERVER_ERROR.code());
    }

    public DownloadException() {
        super("文件下载失败");
        setCode(ResponseStatusEnum.INTERNAL_SERVER_ERROR.code());
    }
}
