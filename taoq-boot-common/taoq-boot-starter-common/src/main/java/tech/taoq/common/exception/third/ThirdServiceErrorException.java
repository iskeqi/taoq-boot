package tech.taoq.common.exception.third;

import tech.taoq.common.exception.TaoqException;
import tech.taoq.common.response.ResultStatusEnum;

/**
 * 第三方服务错误时，抛出此异常
 *
 * @author keqi
 */
public class ThirdServiceErrorException extends TaoqException {

    private static final long serialVersionUID = -1177625451890500114L;

    /**
     * 无更细致错误码时使用
     *
     * @param message message
     */
    public ThirdServiceErrorException(String message) {
        super(ResultStatusEnum.THIRD_SERVICE_ERROR.getCode(), message);
    }

    public ThirdServiceErrorException(String status, String message) {
        super(status, message);
    }
}
