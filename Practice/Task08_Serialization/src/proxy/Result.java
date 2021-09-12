package proxy;

import java.io.Serializable;

public class Result implements Serializable {

    static final long serialVersionUID = 123456789L;

    private final Object result;

    public Result(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}
