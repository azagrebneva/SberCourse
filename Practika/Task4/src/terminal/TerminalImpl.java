package terminal;

import terminal.exceptions.AccountIsLockedException;
import terminal.exceptions.InsufficientFundsOnAccountException;
import terminal.exceptions.InvalidPinCodeException;

public class TerminalImpl implements Terminal {

    private final TerminalServer server;
    private final PinValidator pinValidator;
    private final TerminalInterface terminalInterface;

    public TerminalImpl() {
        this.terminalInterface = new TerminalInterfaceConsole();
        this.server = TerminalServer.getInstance();
        this.pinValidator = new PinValidator();
    }

    public TerminalInterface getTerminalInterface() {
        return terminalInterface;
    }

    /**
     * Авторизация пользователя в банковском терминале
     * @return - пин код пользователя, в случае если пользователь
     * не захотел создавать аккаунт возвращается null значение
     */
    @Override
    public String logIn() {
        do {
            String pin = terminalInterface.getPin();

            try {
                // проверяем пин-код и выбрасываем исключения в случае ошибки
                pinValidator.checkPin(pin);
                if (server.isAccountExists(pin)) return pin;

                // создаем новый аккаунт по запросу и если пользователя нет в базе
                String message = "Pin was not found. " +
                        "Do you want to create new account?";
                if (terminalInterface.getAnswerToGeneralQuestion(message)){
                    server.createNewAccount(pin);
                    return pin;
                }
                // пользователь не авторизовался
                return null;
            } catch (InvalidPinCodeException | AccountIsLockedException e) {
                terminalInterface.showErrorMessage(e.getMessage());
            }
            terminalInterface.showMessage("Try to log in again.");
        } while (true);
    }

    /**
     * Совершение авторизованным пользователем банковской операции
     * @param pin - пин-код клиента
     */
    @Override
    public void doOperation(String pin) {
        TerminalOperations action = terminalInterface.chooseOperation();

        if (action == null) { return;}

        switch (action) {
            case CHECK_ACCOUNT -> {
                int money = server.checkAccount(pin);
                terminalInterface.showMessage(money + " rubles left on the account.");
            }
            case PUT_MONEY -> {
                int money = terminalInterface.getMoney();
                server.putMoney(pin, money);
            }
            case GET_MONEY -> {
                int money = terminalInterface.getMoney();
                try {
                    server.getMoney(pin, money);
                } catch (InsufficientFundsOnAccountException e) {
                    terminalInterface.showMessage(e.getMessage());
                }
            }
        }
    }
}
