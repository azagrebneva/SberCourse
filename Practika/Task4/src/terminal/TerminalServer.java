package terminal;

import terminal.exceptions.InsufficientFundsOnAccountException;

import java.util.HashMap;
import java.util.Map;

/**
 * В классе хранятся пары пин-код (ключ) и
 * количество средств на счете (значение, целое кратное 100).
 * Класс производит все операции со счетом.
 * Класс имеет единственный экземпляр (паттерн Singleton)
 */

public class TerminalServer {

    private static volatile TerminalServer singletonInstance = null;

    private Map<String, Integer> accounts;

    private TerminalServer() {
        this.accounts = new HashMap<>( Map.of(
                "1234", 100,
                "1111", 20_000,
                "2222",3_000));
    }

    public static TerminalServer getInstance(){
        if(singletonInstance==null){
            synchronized (TerminalServer.class) {
                if(singletonInstance ==null){
                    singletonInstance = new TerminalServer();
                }
            }
        }
        return singletonInstance;
    }

    public boolean isAccountExists(String pin){
        Integer account = accounts.get(pin);
        if (account == null) {
            return false;
        }
        return true;
    }

    public void createNewAccount(String pin){
        Integer account = accounts.get(pin);
        if (account == null) {
            accounts.put(pin, 0);
        }
    }

    public int checkAccount(String pin){
        return accounts.get(pin);
    }

    public void putMoney(String pin, int money) {
        Integer account = accounts.get(pin);
        if (account != null) {
            accounts.replace(pin, account + money);
        }
    }

    public void getMoney(String pin, int money) throws InsufficientFundsOnAccountException {
        Integer account = accounts.get(pin);
        if (account != null) {
            if ((account - money) < 0) {
                throw new InsufficientFundsOnAccountException("There are insufficient funds in your account.");
            }
            accounts.replace(pin, account - money);
        }
    }
}
