package com.scottcrocker.packify.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.scottcrocker.packify.MainActivity;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * ValidationHelper, validates various fields to make sure input is correct.
 */

public class ValidationHelper {

    /**
     * Returns a boolean to check if an order exists in database
     * if the order does not exist it sets validAction to false and displays a Toast messsage for the user
     *
     * @param context needs a context to know where to show the toast
     * @param orderNo needs an order number to check
     * @return a boolean that is either true or false depending on if the order does exist.
     */
    public static boolean orderExist(Context context, String orderNo) {
        boolean validAction = true;

        if (!MainActivity.db.doesFieldExist("Orders", "orderNo", orderNo)) {
            Toast.makeText(context, "Order " + orderNo + " finns inte!", Toast.LENGTH_SHORT).show();
            validAction = false;
        }
        return validAction;
    }

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
     * This method is validating input for phone numbers it makes a simple RegEx check to see if
     * the provided input is only containing numbers from 0-9 & is either 9 or 10 digits & that it is not null.
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

        } else if (input.equals(null)) {
            Toast.makeText(context, fieldName + " är tom", Toast.LENGTH_SHORT).show();
            validInput = false;
        } else {
            Toast.makeText(context, fieldName + " måste bestå 9-10 av siffror!", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        return validInput;
    }

    /**
     * This method is validating input for numbers it makes a simple RegEx check to see if
     * the provided input is only containing numbers & that it is not null.
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
            Toast.makeText(context, fieldName + " är för långt(1-9 siffror)", Toast.LENGTH_SHORT).show();
        } else if (input.equals(null) || input.equals("")) {
            isValidInput = false;
            Toast.makeText(context, fieldName + " är tom", Toast.LENGTH_SHORT).show();
        } else {
            isValidInput = false;
            Toast.makeText(context, fieldName + " måste bestå av siffror!", Toast.LENGTH_SHORT).show();
        }
        return isValidInput;
    }

    /**
     * This method is validating input for text it makes a check to see if
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
        if (input.equals("") || input == null) {
            Toast.makeText(context, fieldName + " är tom", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        return validInput;
    }
}
