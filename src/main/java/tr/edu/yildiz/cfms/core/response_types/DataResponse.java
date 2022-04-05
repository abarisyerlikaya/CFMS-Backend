package tr.edu.yildiz.cfms.core.response_types;

import org.springframework.http.HttpStatus;

public class DataResponse<T> extends Response {
    protected T data;

    public DataResponse(boolean success, HttpStatus httpStatus, String message, T data) {
        super(success, httpStatus, message);
        this.data = data;
    }

    public DataResponse(boolean success, HttpStatus httpStatus, T data) {
        super(success, httpStatus);
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
