package tech.taoq.modbus;

/**
 * ModbusTCPException
 *
 * @author keqi
 */
public class ModbusTCPException extends RuntimeException {

    private static final long serialVersionUID = -3042686055658055555L;

    public ModbusTCPException(String message, Throwable e) {
        super(message, e);
    }
}
