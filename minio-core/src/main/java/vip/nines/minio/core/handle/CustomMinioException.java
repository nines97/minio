package vip.nines.minio.core.handle;

/**
 * 自定义异常
 *
 * @author ruoyi
 */
public class CustomMinioException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private final String message;

    public CustomMinioException(String message) {
        this.message = message;
    }

    public CustomMinioException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public CustomMinioException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
