package tr.edu.yildiz.cfms.core.response_types;

import org.springframework.http.HttpStatus;

public class Response {
    protected boolean success;
    protected HttpStatus httpStatus;
    protected String message;

    public Response(boolean success, HttpStatus httpStatus, String message) {
        this.success = success;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public Response(boolean success, HttpStatus httpStatus) {
        this.success = success;
        this.httpStatus = httpStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
