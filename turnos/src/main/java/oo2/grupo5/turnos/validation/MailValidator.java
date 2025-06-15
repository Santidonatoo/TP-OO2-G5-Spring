package oo2.grupo5.turnos.validation;

import java.util.regex.Pattern;

public class MailValidator {

	private static final Pattern EMAIL_PATTERN = 
	        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	    public static boolean isValid(String email) {
	        return email != null && EMAIL_PATTERN.matcher(email).matches();
	    }
}
