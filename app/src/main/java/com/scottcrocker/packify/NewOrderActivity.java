package com.scottcrocker.packify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.Order;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.db;
import static com.scottcrocker.packify.MainActivity.gps;

public class NewOrderActivity extends AppCompatActivity {

    EditText orderNr;
    EditText customerName;
    EditText customerNr;
    EditText orderSum;
    EditText address;
    EditText postAddress;
    Switch isDelivered;

    String orderNo;

    List<Boolean> isValidInput = new ArrayList<>();
    ValidationHelper validationHelper = new ValidationHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        orderNr = (EditText) findViewById(R.id.input_new_order_number);
        customerName = (EditText) findViewById(R.id.input_new_customer_name);
        customerNr = (EditText) findViewById(R.id.input_new_customer_id);
        orderSum = (EditText) findViewById(R.id.input_new_order_sum);
        address = (EditText) findViewById(R.id.input_new_order_address);
        postAddress = (EditText) findViewById(R.id.input_new_order_post_address);
        isDelivered = (Switch) findViewById(R.id.is_new_order_delivered);


    }

    public void addNewOrder(View view) {

            String newOrderNr = String.valueOf(orderNr.getText());
            isValidInput.add(validationHelper.validateInputNumber(String.valueOf(newOrderNr), "Ordernummer", this));

            String newCustomerName = String.valueOf(customerName.getText());
            isValidInput.add(validationHelper.validateInputText(newCustomerName, "Namn", this));

            String newCustomerNr = String.valueOf(customerNr.getText());
            isValidInput.add(validationHelper.validateInputNumber(String.valueOf(newCustomerNr), "Kundnummer", this));

            String newOrderSum = String.valueOf(orderSum.getText());
            isValidInput.add(validationHelper.validateInputNumber(String.valueOf(newOrderSum), "Ordersumma", this));

            String newAdress = String.valueOf(address.getText());
            isValidInput.add(validationHelper.validateInputText(newAdress, "Address", this));

            String newPostAddress = String.valueOf(postAddress.getText());
            isValidInput.add(validationHelper.validateInputText(newPostAddress, "Postadress", this));

            if (validationHelper.isAllTrue(isValidInput) && !db.doesFieldExist("Orders", "orderNo", newOrderNr)) {
                Order order = new Order(Integer.parseInt(newOrderNr), Integer.parseInt(newCustomerNr),
                        newCustomerName, newAdress, newPostAddress, Integer.parseInt(newOrderSum), "---",
                        isDelivered.isChecked(), "---", gps.getLongitude(newAdress + ", " + newPostAddress),
                        gps.getLatitude(newAdress + ", " + newPostAddress), null);

                db.addOrder(order);
                finish();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_neworder_saved), Toast.LENGTH_SHORT).show();
            } else if (db.doesFieldExist("Orders", "orderNo", newOrderNr)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_neworder_orderno), Toast.LENGTH_LONG).show();
            }
            isValidInput.clear();
        }

    public void cancelBtn(View view) {
        finish();
    }
}
