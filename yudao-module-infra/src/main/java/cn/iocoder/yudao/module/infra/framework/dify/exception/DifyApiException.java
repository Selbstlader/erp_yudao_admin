package cn.iocoder.yudao.module.infra.framework.dify.exception;

import lombok.Getter;

/**
 * Dify API异常类
 */
@Getter
public class DifyApiException extends RuntimeException {

    /**
     * HTTP状态码
     */
    private final Integer statusCode;

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public DifyApiException(String message) {
        super(message);
        this.statusCode = null;
        this.errorCode = null;
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause 异常原因
     */
    public DifyApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.errorCode = null;
    }

    /**
     * 构造函数
     *
     * @param statusCode HTTP状态码
     * @param errorCode 错误码
     * @param message 错误消息
     */
    public DifyApiException(Integer statusCode, String errorCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        if (statusCode != null && errorCode != null) {
            return String.format("DifyApiException[statusCode=%s, errorCode=%s, message=%s]",
                    statusCode, errorCode, getMessage());
        }
        return String.format("DifyApiException[message=%s]", getMessage());
    }
} 