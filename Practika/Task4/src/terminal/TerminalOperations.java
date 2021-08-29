package terminal;

public enum TerminalOperations {
    CHECK_ACCOUNT("1"),
    PUT_MONEY("2"),
    GET_MONEY("3");

    private String value;

    TerminalOperations(String value) {
        this.value = value;
    }

    public String getStringNotation() {
        return this.value;
    }

    @Override
    public String toString() {
        switch (this) {
            case CHECK_ACCOUNT:
                return "check out account status.";
            case PUT_MONEY:
                return "put money to the account.";
            case GET_MONEY:
                return "get money from the account.";
        }
        return "";
    }
};
