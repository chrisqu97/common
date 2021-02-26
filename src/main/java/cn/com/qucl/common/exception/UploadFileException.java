package cn.com.qucl.common.exception;

import cn.com.qucl.common.enums.ResponseStatusEnum;

/**
 * @author qucl
 * @date 2020/12/30 16:42
 * 文件上传异常
 */
public class UploadFileException extends BaseException {
    public UploadFileException(String message) {
        super(message);
        setCode(ResponseStatusEnum.INTERNAL_SERVER_ERROR.code());
    }

    public UploadFileException() {
        super("文件上传异常");
        setCode(ResponseStatusEnum.INTERNAL_SERVER_ERROR.code());
    }
}
