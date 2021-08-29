package terminal;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Класс отвечающий за интерфейс.
 */

public class TerminalInterfaceConsole implements TerminalInterface {

    @Override
    public String getPin(){
        Scanner scanner = new Scanner(System.in); // System.in закрывать не нужно
        System.out.print("Type pin: ");
        String password = scanner.next();
        return password;
    }

    @Override
    public void showMessage(String message){
        System.out.println(message);
    }

    @Override
    public void showErrorMessage(String message) {
        showMessage(message);
    }

    @Override
    //create new account
    public boolean getAnswerToGeneralQuestion(String message) {
        message = message + "\nEnter \"yes\" to answer yes, and any other characters to answer no.";
        System.out.println(message);

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        if (answer.equals("yes")) return true;
        return false;
    }

    @Override
    public TerminalOperations chooseOperation() {

        System.out.println("Possible operations: ");
        Arrays.stream(TerminalOperations.values()).forEach(
                s->{System.out.println(s.getStringNotation()+" - " + s);});

        Scanner scanner = new Scanner(System.in); // System.in закрывать не нужно
        System.out.print("Type action: ");
        String operation = scanner.next();

        if (operation.equals(TerminalOperations.CHECK_ACCOUNT.getStringNotation())) {
            return TerminalOperations.CHECK_ACCOUNT;
        }
        if (operation.equals(TerminalOperations.PUT_MONEY.getStringNotation())) {
            return TerminalOperations.PUT_MONEY;
        }
        if (operation.equals(TerminalOperations.GET_MONEY.getStringNotation())) {
            return TerminalOperations.GET_MONEY;
        }
        return null;
    }

    @Override
    public int getMoney() {
        Scanner scanner = new Scanner(System.in);
        int money = 0;
        while (true)
        {
            System.out.print("Type sum: ");
            String line = scanner.nextLine();
            try
            {
                money = Integer.parseInt(line);
                if (money % 100 != 0)
                    System.out.println("The sum must be a multiple of 100.");
                else if (money < 0){
                    System.out.println("The sum must be a positive.");
                } else {
                    return money;
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("This is not a number");
            }
            System.out.println("Try to enter the sum again.");
        }
    }
}
