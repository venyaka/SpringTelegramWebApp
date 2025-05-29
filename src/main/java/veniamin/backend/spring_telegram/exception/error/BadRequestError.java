package veniamin.backend.spring_telegram.exception.error;

public enum BadRequestError {

    NOT_CORRECT_PASSWORD("Неверный пароль");

    private String message;

    BadRequestError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
