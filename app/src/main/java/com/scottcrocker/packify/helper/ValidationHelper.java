package com.scottcrocker.packify.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * ValidationHelper, validates various fields to make sure input is correct.
 */
public class ValidationHelper {

    /**
     * This method checks all booleans in an ArrayList and returns a boolean.
     *
     * @param isAllValid requires a list of Booleans.
     * @return true only if all the other booleans are true.
     */
    public static boolean isAllTrue(List<Boolean> isAllValid) {
        for (boolean isValid : isAllValid) {
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method validates input for phone numbers. It makes a simple RegEx check to see if
     * the provided input contains numbers from 0-9 & is either 9 or 10 digits & that it is not null.
     * The method displays a different toast depending on the result.
     *
     * @param input     the input provided to check.
     * @param fieldName this param is for the toast to write out where it is being shown.
     * @param context   needs a context to know where to show the toast.
     * @return a boolean that is true or false depending on the result of the check.
     */
    public boolean validateInputPhoneNr(String input, String fieldName, Context context) {
        boolean validInput = true;
        if (input.matches("[0-9]{9,10}")) {
            Log.d(TAG, "Input for phone nr is valid");

        } else if (input.equals("")) {
            Toast.makeText(context, fieldName + " är tom", Toast.LENGTH_SHORT).show();
            validInput = false;
        } else {
            Toast.makeText(context, fieldName + " måste bestå av 9-10 siffror", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        return validInput;
    }

    /**
     * This method validates input for numbers. It makes a simple RegEx check to see if
     * the provided input contains numbers & that it is not null.
     * The method displays a different toast depending on the result.
     *
     * @param input     the input provided to check.
     * @param fieldName this param is for the toast to write out where it is being shown.
     * @param context   needs a context to know where to show the toast.
     * @return a boolean that is true or false depending on the result of the check.
     */
    public boolean validateInputNumber(String input, String fieldName, Context context) {
        boolean isValidInput = true;
        if (input.matches("^\\d{1,9}$")) {
            Log.d(TAG, "Input for " + fieldName + " is valid");
        } else if (input.length() > 10) {
            isValidInput = false;
            Toast.makeText(context, fieldName + " är för långt (1-9 siffror)", Toast.LENGTH_SHORT).show();
        } else if (input.equals("")) {
            isValidInput = false;
            Toast.makeText(context, fieldName + " är tom", Toast.LENGTH_SHORT).show();
        } else {
            isValidInput = false;
            Toast.makeText(context, fieldName + " måste bestå av siffror", Toast.LENGTH_SHORT).show();
        }
        return isValidInput;
    }

    /**
     * This method validates input for text it makes a check to see if
     * the provided input is not null.
     * The method displays a different toast depending on the result.
     *
     * @param input     the input provided to check.
     * @param fieldName this param is for the toast to write out where it is being shown.
     * @param context   needs a context to know where to show the toast.
     * @return a boolean that is true or false depending on the result of the check.
     */
    public boolean validateInputText(String input, String fieldName, Context context) {
        boolean validInput = true;
        if (input.equals("")) {
            Toast.makeText(context, fieldName + " är tom", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        return validInput;
    }
}
