package veniamin.backend.spring_telegram.exception.error;

public enum AuthorizedError {

    NOT_AUTHENTICATED("Пользователь не авторизован"),

    TOKEN_WAS_EXPIRED("Срок действия токена истек"),

    BAD_CREDENTIALS("Неверные данные для авторизации");


    private String message;

    AuthorizedError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
