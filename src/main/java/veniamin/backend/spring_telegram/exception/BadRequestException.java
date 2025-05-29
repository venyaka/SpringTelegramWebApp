package veniamin.backend.spring_telegram.exception;

import lombok.Getter;
import veniamin.backend.spring_telegram.exception.error.BadRequestError;

@Getter
public class BadRequestException extends BusinessException {

    private String errorName;

    public BadRequestException(BadRequestError badRequestError) {
        super(badRequestError.getMessage());
        errorName = badRequestError.name();
    }

    public BadRequestException(String message, String errorName) {
        super(message);
        this.errorName = errorName;
    }
}
