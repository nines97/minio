package vip.nines.minio.core.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vip.nines.minio.api.entity.R;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * minio业务异常
     */
    @ExceptionHandler(CustomMinioException.class)
    public R<?> minioException(CustomMinioException e) {
        log.error("minio调用服务异常" + e);
        return R.fail(e.getMessage());
    }

}
