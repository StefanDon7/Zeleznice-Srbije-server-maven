/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.stefanlezaic.zeleznice.srbije.server.email.sender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Stefan
 */
public class EmailVerification {

    private Pattern emailPat;
    private final String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public boolean validate(String email) {
        emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);
        return matcher.find();
    }
}
