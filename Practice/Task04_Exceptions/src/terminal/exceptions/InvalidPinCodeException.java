package terminal.exceptions;

/**
 * Выбрасывается при вводе неправильного пин-кода
 * Пин код должен состоять из 4 цифр.
 * Классы выбрасывающие исключение - TerminalServer
 */

public class InvalidPinCodeException extends Exception {

    public InvalidPinCodeException(String message) {
        super(message);
    }
}
