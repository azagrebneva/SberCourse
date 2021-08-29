package terminal;

import terminal.exceptions.AccountIsLockedException;
import terminal.exceptions.InvalidPinCodeException;

import java.time.Duration;
import java.time.Instant;

/**
 * Класс проверяет пин-код на правильность.
 * Правильный пин-код состоит из 4 цифр.
 * При вводе 3 неправильных пин-кодов аккаунт блокируется на 10 сек.
 */

public class PinValidator {

    private static final int MAX_NUMBER_OF_ATTEMPTS = 3;
    private static final int MAX_LOCK_TIME_IN_SECONDS = 10;
    private static final Duration MAX_LOCK_DURATION = Duration.ofSeconds(MAX_LOCK_TIME_IN_SECONDS);

    private int numberOfAttempt;
    private Instant beginLockTime;

    public PinValidator() {
        this.numberOfAttempt = 0;
        this.beginLockTime = null;
    }

    /**
     * Проверяет пин-код на правильность.
     * @param pin - проверяемый пин-код;
     * @throws InvalidPinCodeException - выбрасывается, когда пароль введен неверно -
     * не совпадает по длине (4 символа) или содержит не только символы;
     * @throws AccountIsLockedException - выбрасывается при попытке обратиться
     * к заблокированному в результате введения 3-ех неправильных пин-кодов
     * аккаунту. Аккаунт будет разблокирован по истечению 10 минут.
     */
    public void checkPin(String pin) throws InvalidPinCodeException, AccountIsLockedException {

        if (numberOfAttempt >= MAX_NUMBER_OF_ATTEMPTS) {
            if (beginLockTime != null) {
                Duration duration = Duration.between(beginLockTime, Instant.now());
                if (duration.compareTo(MAX_LOCK_DURATION) <= 0) {
                    throw new AccountIsLockedException("Account is locked." +
                            " Wait for " + (MAX_LOCK_TIME_IN_SECONDS - duration.getSeconds()) + " seconds.");
                }
                else {
                    beginLockTime = null;
                    numberOfAttempt = 0;
                }
            }
        }
        if (pin.length() != 4) {
            throwInvalidPinCodeException("Pin's length is not 4 characters. Pin code must be 4 digits.");
        }
        if (!isNumeric(pin)) {
            throwInvalidPinCodeException("Pin is not a numeric. Pin code must be 4 digits.");
        }
    }

    private void throwInvalidPinCodeException(String message) throws InvalidPinCodeException {
        numberOfAttempt += 1;
        if (numberOfAttempt == MAX_NUMBER_OF_ATTEMPTS) {
            if (beginLockTime == null) {
                beginLockTime = Instant.now();
            }
        }
        throw new InvalidPinCodeException(message + " You have " + (MAX_NUMBER_OF_ATTEMPTS - numberOfAttempt) + " attempts.");
    }

    private boolean isNumeric(String str) {
        return str.matches("[0-9]{4}");
    }
}
