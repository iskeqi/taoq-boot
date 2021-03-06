package tech.taoq.web.mvc.exception;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.taoq.common.exception.TaoqException;
import tech.taoq.common.response.ResultEntity;
import tech.taoq.common.response.ResultEntityBuilder;

import java.util.List;

/**
 * 全局异常处理器
 *
 * @author keqi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DefaultExceptionHandlerAdapter.class);

    @Autowired
    private List<ExceptionHandlerAdapter> exceptionHandlerAdapterList;

    /**
     * TaoqException
     *
     * @param e TaoqException
     * @return r
     */
    @ExceptionHandler(value = TaoqException.class)
    public ResultEntity businessException(TaoqException e) {
        log.error("status={},message={}", e.getStatus(), e.getMessage());
        return ResultEntityBuilder.failure(e.getStatus(), e.getMessage());
    }

    /**
     * 捕获所有异常（这个异常必须要放在最后）
     *
     * @param e Throwable
     * @return r
     */
    @ExceptionHandler(Throwable.class)
    public ResultEntity throwable(Throwable e) {
        for (ExceptionHandlerAdapter handlerAdapter : exceptionHandlerAdapterList) {
            if (handlerAdapter.supports(e)) {
                return handlerAdapter.handle(e);
            }
        }
        return ResultEntityBuilder.failure();
    }

}
