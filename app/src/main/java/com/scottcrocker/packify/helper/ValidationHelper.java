package com.scottcrocker.packify.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by mavve on 2016-11-28.
 */

public class ValidationHelper {

    boolean[] validationArray = new boolean[4];

    public boolean validatedNewUserFields(){

            for(boolean b : validationArray)
                if(!b){
                    return false;
                }
                    return true;
    }

    public void validateInputName(String newUsername, Context context) {
        if (newUsername.equals("")) {
            Toast.makeText(context,"namn är tom", Toast.LENGTH_SHORT).show();
            validationArray[0] = false;
        } else {
            //intended to accept numbers as user input. Maybe a name can be "Kenny212".
            Log.d(TAG, "Input for name is valid");
            validationArray[0] = true;
        }
    }

    public void validateInputPassWord(String newUserPass, Context context) {
        if (newUserPass.equals("") || newUserPass == null) {
            Toast.makeText(context,"lösenord är tom", Toast.LENGTH_SHORT).show();
            validationArray[1] = false;
        } else {
            validationArray[1] = true;
        }
    }

    public void validateInputPhoneNr(String userPhoneNr, Context context) {

        if (userPhoneNr.matches("[0-9]{9,10}")) {
            Log.d(TAG, "Input for phone nr is valid");
            validationArray[2] = true;

        } else if (userPhoneNr.equals("")) {
            Toast.makeText(context, "telefon nr är tom", Toast.LENGTH_SHORT).show();
            validationArray[2] = false;
        } else {
            Toast.makeText(context,"telefon nr måste bestå 9-10 av siffror!", Toast.LENGTH_SHORT).show();
            validationArray[2] = false;
        }
    }

    public void validateInputUserId(String userId, Context context) {
        if (userId.matches("^\\d{1,9}$")) {
            Log.d(TAG, "Input for user id is valid");
            validationArray[3] = true;
        } else if (userId.equals(null)) {
            Toast.makeText(context, "användar id är tomt", Toast.LENGTH_SHORT).show();
            validationArray[3] = false;
        } else {
            Toast.makeText(context,"användar id måste bestå av siffror!", Toast.LENGTH_SHORT).show();
            validationArray[3] = false;
        }
    }
}
