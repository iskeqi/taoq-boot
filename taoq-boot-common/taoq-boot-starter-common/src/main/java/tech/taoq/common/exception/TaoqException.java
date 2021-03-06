package tech.taoq.common.exception;

/**
 * TaoqException
 *
 * @author keqi
 */
public abstract class TaoqException extends RuntimeException {

    private static final long serialVersionUID = 3267680643599580436L;

    /**
     * 返回状态码
     */
    private String status;

    public TaoqException(String status, String message) {
        super(message);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
