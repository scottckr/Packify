package com.scottcrocker.packify.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.scottcrocker.packify.MainActivity;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by mavve on 2016-11-28.
 */

public class ValidationHelper {

    //Denna kommer säkert inte behövas när orderhandler får en popup...
    public static boolean orderExist(Context context, String orderNo){
        boolean validAction = true;


        if (!MainActivity.db.doesFieldExist("Orders", "orderNo", orderNo)){
            Toast.makeText(context, "Order " + orderNo + " does not exist", Toast.LENGTH_SHORT).show();
            validAction = false;
        }
        return validAction;
    }

    public static boolean isAllTrue(List<Boolean> isAllValid){
        for(boolean isValid : isAllValid){
            if(!isValid){
                return false;
            }
        }
        return true;
    }

    public boolean validateInputPhoneNr(String input, String fieldName ,Context context) {
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

    public boolean validateInputNumber(String input, String fieldName, Context context){
        boolean isValidInput = true;
        if (input.matches("^\\d{1,9}$")){
            Log.d(TAG, "Input for "+fieldName+" is valid");
        }else if(input.equals(null)){
            isValidInput = false;
            Toast.makeText(context, fieldName+" är tom", Toast.LENGTH_SHORT).show();
        }else{
            isValidInput = false;
            Toast.makeText(context, fieldName+" måste bestå av siffror!", Toast.LENGTH_SHORT).show();
        }
        return isValidInput;
    }

    public boolean validateInputText(String input, String fieldName, Context context) {
        boolean validInput = true;
        if (input.equals("") || input == null) {
            Toast.makeText(context, fieldName + " är tom", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        return validInput;
    }

}
