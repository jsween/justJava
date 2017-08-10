package com.jonsweeney.justjava;

/**
 * package com.example.android.justjava;
 *
 */


import java.text.NumberFormat;
import java.text.StringCharacterIterator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.name;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 2;
    int basePrice = 5;
    int whippedCreamPrice = 1;
    int chocolatePrice = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**
     * Increment the order by one
     */
    public void increment(View view) {
        if(quantity == 100) {
            Toast.makeText(this, "Only 100 at a time sir or ma'am", Toast.LENGTH_LONG).show();
            return;
        }
        quantity += 1;
        displayQuantity(quantity);
    }

    /**
     * Decrement the order by one
     */
    public void decrement(View view) {
        if(quantity == 1) {
            Toast.makeText(this, "1 Cup Minimum", Toast.LENGTH_LONG).show();
            return;
        }
        quantity -= 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String name = getUserName();
        boolean addWhippedCream = checkForWhippedCream();
        boolean addChocolate = checkForChocolate();
        int priceOfOrder = calculatePrice(addWhippedCream, addChocolate);
        String priceMessage = createOrderSummary(name, addWhippedCream, addChocolate, priceOfOrder);
        composeEmail(priceMessage);
    }

    /**
     * This generates the string message for the email
     */
    private String createOrderSummary(String name, boolean addWhippedCream, boolean addChocolate, int priceOfOrder) {
        String priceMessage = getString(R.string.name_string, name)+"\n"
                +getString(R.string.whipped_cream_string)+addWhippedCream+"\n"
                +getString(R.string.chocolate_string)+addChocolate+"\n"
                +getString(R.string.quantity_string)+quantity+"\n"
                +"$"+priceOfOrder+"\n"
                +getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method checks if there should be whipped cream on drink
     */
    private boolean checkForWhippedCream() {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        return whippedCreamCheckBox.isChecked();
    }

    /**
     * This method checks if there should be chocolate on drink
     */
    private boolean checkForChocolate() {
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        return chocolateCheckBox.isChecked();
    }

    /**
     * This method gets the name for the user
     */
    private String getUserName() {
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        return nameEditText.getText().toString();
    }

    /**
     * This method calculates the price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int total = basePrice;
        if(addWhippedCream)
            total += whippedCreamPrice;
        if(addChocolate)
            total += chocolatePrice;
        return total*quantity;
    }

    /**
     * Compose Email
     */
    public void composeEmail(String message) {
        String[] address = new String[] {"jon.sweeney@developmentnow.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Test Email from Java");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
