package codingchallenge.constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Simple Email Validation for user input
 */
public class EmailValidator {

    private static final String EMAIL_REGEX =  "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private static Pattern pattern;
    private Matcher matcher;

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
