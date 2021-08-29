package terminal.exceptions;

/**
 * Выбрасывается, когда произошла попытка обратиться к заблокированному
 * в результате введения 3-ех неправильных пин-кодов аккаунт.
 * Аккаунт будет разблокирован по истечению 10 минут.
 * Выводится информация об оставшемся времени до снятия блокировки.
 */

public class AccountIsLockedException extends Exception {

    public AccountIsLockedException(String message) {
        super(message);
    }
}
