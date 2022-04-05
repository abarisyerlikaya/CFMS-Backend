package tr.edu.yildiz.cfms.core.response_types;

import org.springframework.http.HttpStatus;

public class SuccessResponse extends Response {
    public SuccessResponse(String message) {
        super(true, HttpStatus.OK);
        this.message = message;
    }

    public SuccessResponse() {
        super(true, HttpStatus.OK);
    }
}
