package co.inventorsoft.academy.spring.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends AbstractCommonException {

    // User related validation
    public static final Detail USER_EMAIL_EXISTS =
        new Detail(400, "invalid_email", "User with this email already exists");

    public static final Detail USER_ID_DOES_NOT_EXIST =
        new Detail(400, "invalid_email", "User with this id does not exist");

    public static final Detail USER_ID_EXISTS =
        new Detail(400, "invalid_id", "User with this id already exists");

    public static final Detail USER_MUST_NOT_HAVE_NULL_FIELDS =
        new Detail(400, "invalid_values", "User with this id already exists");

    public ValidationException(Detail detail) {
        super(detail);
    }

    public ValidationException(int statusCode, String errorCode, String errorMessage) {
        super(statusCode, errorCode, errorMessage);
    }
}
