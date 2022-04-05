package tr.edu.yildiz.cfms.core.response_types;

import org.springframework.http.HttpStatus;

public class SuccessDataResponse<T> extends DataResponse<T> {

    public SuccessDataResponse(String message, T data) {
        super(true, HttpStatus.OK, message, data);
    }

    public SuccessDataResponse(T data) {
        super(true, HttpStatus.OK, data);
    }
}
