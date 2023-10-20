package co.inventorsoft.academy.spring.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends AbstractCommonException {

    public static final Detail USER_ID_NOT_FOUND =
        new Detail(404, "user_not_found", "User with this id doesn't exist");


    public static final Detail USER_EMAIL_NOT_FOUND =
        new Detail(404, "user_not_found", "User with this email doesn't exist");

    public NotFoundException(Detail detail) {
        super(detail);
    }
}
