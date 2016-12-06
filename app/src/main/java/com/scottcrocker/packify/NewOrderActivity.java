package com.scottcrocker.packify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.scottcrocker.packify.helper.ValidationHelper;
import com.scottcrocker.packify.model.Order;

import java.util.ArrayList;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.currentUserId;

public class NewOrderActivity extends AppCompatActivity {

    EditText orderNr;
    EditText customerName;
    EditText customerNr;
    EditText orderSum;
    EditText adress;
    EditText postAdress;

    String orderNo;

    ValidationHelper validationHelper = new ValidationHelper();
    List<Boolean> isValidInput = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        orderNr = (EditText) findViewById(R.id.input_new_order_number);
        customerName = (EditText) findViewById(R.id.input_new_customer_name);
        customerNr = (EditText) findViewById(R.id.input_new_customer_id);
        orderSum = (EditText) findViewById(R.id.input_new_order_sum);
        adress = (EditText) findViewById(R.id.input_new_order_address);
        postAdress = (EditText) findViewById(R.id.input_new_order_post_address);

    }

    public void addOrder(View view) {



//TODO : fixa valideringen.
/*        isValidInput.add(validationHelper.validateInputNumber(customerId, "Kundnummer", this));

        isValidInput.add(validationHelper.validateInputText(customerName, "Namn", this));

        isValidInput.add(validationHelper.validateInputNumber(orderSum, "Ordersumma", this));


        isValidInput.add(validationHelper.validateInputText(address, "Address", this));

        isValidInput.add(validationHelper.validateInputText(postAddress, "Postadress", this));*/

        int newOrderNr = Integer.parseInt(String.valueOf(orderNr.getText()));
        isValidInput.add(validationHelper.validateInputNumber(String.valueOf(newOrderNr), "Ordernummer", this));

        String newCustomerName = String.valueOf(customerName.getText());
        int newCustomerNr = Integer.parseInt(String.valueOf(customerNr.getText()));
        int newOrderSum = Integer.parseInt(String.valueOf(orderSum.getText()));
        String newAdress = String.valueOf(adress.getText());
        String newPostAdress = String.valueOf(postAdress.getText());


        if (validationHelper.isAllTrue(isValidInput) && !validationHelper.orderExist(this, orderNo)) {
            Order order = new Order(newOrderNr, newCustomerNr,
                    newCustomerName, newAdress, newPostAdress, newOrderSum, "---",
                    false, MainActivity.db.getUser(currentUserId).getId(), MainActivity.gps.getLongitude(newAdress + ", "+ newPostAdress),
                    MainActivity.gps.getLatitude(newAdress + ", "+ newPostAdress), null);

            MainActivity.db.addOrder(order);
            Toast.makeText(getApplicationContext(), "Order sparad", Toast.LENGTH_SHORT).show();
        } else if (MainActivity.db.doesFieldExist("Orders", "orderNo", orderNo)) {
            Toast.makeText(getApplicationContext(), "Ordernumret finns redan!", Toast.LENGTH_LONG).show();
        }
        isValidInput.clear();

    }

    public void cancelBtn(View view) {
        finish();
    }
}
