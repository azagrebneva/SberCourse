package terminal.exceptions;

/**
 * Выбрасывается, когда у клиента на счете недостаточно средств
 * для проведения операции
 */

public class InsufficientFundsOnAccountException extends Exception {

    public InsufficientFundsOnAccountException(String message) {
        super(message);
    }
}
