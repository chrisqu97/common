package cn.com.qucl.common.pojo.vo;

import org.springframework.http.HttpStatus;

/**
 * @author qucl
 * @date 2018/11/2 16:12
 * 接口默认返回格式
 */
public class ResultVo {
    /**
     * 成功
     */
    private Boolean success;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 消息
     */
    private String message;
    /**
     * 内容
     */
    private Object data;

    /**
     * 返回成功
     */
    public ResultVo success() {
        this.success = true;
        this.timestamp = System.currentTimeMillis();
        this.code = HttpStatus.OK.value();
        this.message = HttpStatus.OK.name();
        return this;
    }

    /**
     * 返回成功
     *
     * @param data 数据
     */
    public ResultVo success(Object data) {
        this.success = true;
        this.code = HttpStatus.OK.value();
        this.message = HttpStatus.OK.name();
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    /**
     * 失败
     *
     * @param message 消息
     */
    public ResultVo error(String message) {
        this.success = false;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public ResultVo error(Integer code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
