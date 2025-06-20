/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.util.regex.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Francesc Perez
 * @version 1.1.0
 */
public class DataValidation {

  public static boolean isNumber(char c) {
    return (48 <= c && c <= 57);
  }

  public static boolean isLetter(char c) {
    //The name can contain uppercase and lowercase letters, whitespace, 
    //hyphens and code control
//        return (97 <= c && c <= 122) || (65 <= c && c <= 90) || (c == 32) || (c == 45);
    return Character.isLetter(c) || c == 32 || c == 45;
  }

  public static String calculateNifLetter(String nifNoLetter) {
    String[] letter = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B",
      "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};
    return nifNoLetter + letter[Integer.parseInt(nifNoLetter) % 23];
  }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\+?[0-9]{1,4}?[-.\\s]?(\\d{1,3})?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static Boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
  
    public static Boolean validatePostalCode(String postalCode) {
        String postalCodeRegex = "^(\\d{5})(?:[-\\s]?\\d{4})?$";
        Pattern pattern = Pattern.compile(postalCodeRegex);
        Matcher matcher = pattern.matcher(postalCode);
        return matcher.matches();
    }
}
