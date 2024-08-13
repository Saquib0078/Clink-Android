package com.nirmiteepublic.clink.functions.helpers;

import android.app.Activity;

import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;

import java.util.regex.Pattern;

/**
 * Created by Sahil The Geek
 * Date : 24-02-2023.
 *
 * <p>This {@link CredentialsValidator} Class is Used to Validate user Credentials such as email, phone, password </p>
 */
public class CredentialsValidator {

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 30;
    public static final int NUMBER_LENGTH = 10;
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_FIRST_NAME_LENGTH = 20;
    public static final int MAX_LAST_NAME_LENGTH = 20;
    public static final int MAX_EMAIL_LENGTH = 70;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@gmail.com";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    Activity activity;

    public CredentialsValidator(Activity activity) {
        this.activity = activity;
    }

    public boolean validateEmail(String email, boolean showToast) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "Email can Contain Maximum " + MAX_EMAIL_LENGTH + " Characters");
            }
            return false;
        }

        boolean b = pattern.matcher(email).matches();

        if (!b && showToast) {
            PegaAnimationUtils.showToast(activity, "Invalid Email Address");
        }

        return b;
    }

    public boolean validatePassword(String password, boolean showToast) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "Password must have minimum " + MIN_PASSWORD_LENGTH + " characters");
            }
            return false;
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "Password can Contain Maximum " + MAX_PASSWORD_LENGTH + " Characters");
            }
            return false;
        }
        if (password.contains(" ")) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "Password cannot contain space");
            }
            return false;
        }
        return true;
    }

    public boolean validateNumber(String number, boolean showToast) {
        if (number.startsWith("+91")) {
            number = number.replace("+91", "");
        }
        if (number.length() != NUMBER_LENGTH) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "Please Enter a Valid Number");
            }
            return false;
        }

        return true;
    }

    public boolean validateFirstName(String name, boolean showToast) {
        if (name.length() < MIN_NAME_LENGTH) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "Please Enter a Valid First Name");
            }
            return false;
        }
        if (name.length() > MAX_FIRST_NAME_LENGTH) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "First Name can Contain Maximum " + MAX_FIRST_NAME_LENGTH + " Characters");
            }
            return false;
        }
        return true;
    }

    public boolean validateLastName(String name, boolean showToast) {
        if (name.length() < MIN_NAME_LENGTH) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "Please Enter a Valid Last Name");
            }
            return false;
        }
        if (name.length() > MAX_LAST_NAME_LENGTH) {
            if (showToast) {
                PegaAnimationUtils.showToast(activity, "Last Name can Contain Maximum " + MAX_LAST_NAME_LENGTH + " Characters");
            }
            return false;
        }
        return true;
    }

}
