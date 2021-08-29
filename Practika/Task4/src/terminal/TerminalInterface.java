package terminal;

public interface TerminalInterface {

    public String getPin();

    public void showMessage(String message);

    public void showErrorMessage(String message);

    //create new account
    abstract boolean getAnswerToGeneralQuestion(String message);

    public TerminalOperations chooseOperation();

    public int getMoney();
}
