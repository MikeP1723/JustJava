package com.example.android.justjava;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    // Global constants
    private static final String COST_PER_CUP_PROP = "COST_PER_CUP";
    private static final String SALES_TAX_PROP = "SALES_TAX";
    private static final String PRICE_PER_CUP_PROP = "PRICE_PER_CUP";
    private static String COST_PER_CUP;
    private static double SALES_TAX;
    private static double PRICE_PER_CUP;
    private static Properties props = new Properties();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context=this;
        initializeProperties(props);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initializeProperties(Properties props) {

        try {

            AssetManager assetManager = context.getAssets();
            InputStream input = assetManager.open("prices.properties");
            props.load(input);

            // Initialize global variables
            COST_PER_CUP = " " + props.getProperty(COST_PER_CUP_PROP);
            SALES_TAX = Double.parseDouble(props.getProperty(SALES_TAX_PROP));
            PRICE_PER_CUP = Double.parseDouble(props.getProperty(PRICE_PER_CUP_PROP));


        } catch (Exception e) {
            Log.e("Property init failed.", e.getMessage());
        }

    }

    /**
     * Displays the final total with sales tax.
     * @param view
     */
    public void submitOrder(View view) {

        TextView cupsOrdered = (TextView) findViewById(R.id.quantity_text_view);

        int quantity = Integer.parseInt(cupsOrdered.getText().toString().substring(0, cupsOrdered.getText().toString().indexOf(" ")));

        displayTotal((quantity * PRICE_PER_CUP) + (((quantity * PRICE_PER_CUP) * SALES_TAX)));
    }

    /**
     * Increments the number of cups and calculates tax and subtotal.
     * @param view
     */
    public void incrementQty(View view) {

        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);

        int qty = Integer.parseInt(quantityTextView.getText().toString().substring(0, quantityTextView.getText().toString().indexOf(" ")));
        qty++;

        quantityTextView.setText("" + qty + COST_PER_CUP);
        double subTotal = qty * PRICE_PER_CUP;

        display(qty);
        displayPrice(subTotal);
        displayTax(subTotal);
    }

    /**
     * Decrements the number of cups and calculates the tax and subtotal.
     * @param view
     */
    public void decrementQty(View view) {

        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);

        int qty = Integer.parseInt(quantityTextView.getText().toString().substring(0, quantityTextView.getText().toString().indexOf(" ")));

        if (qty == 0)
            return;

        qty--;

        quantityTextView.setText("" + qty + COST_PER_CUP);
        double subTotal = qty * PRICE_PER_CUP;

        display(qty);
        displayPrice(subTotal);
        displayTax(subTotal);
    }

    private void display(int number) {

        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number + COST_PER_CUP);
    }

    private void displayPrice(double number) {

        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    private void displayTax(double subTotal) {

        String salesTaxPercent = String.valueOf(SALES_TAX * 100) + " percent";

        double subTotalPlusTax = subTotal * SALES_TAX;

        TextView quantityTextView = (TextView) findViewById(R.id.tax_cost_text_view);
        quantityTextView.setText("" + NumberFormat.getCurrencyInstance().format(subTotal)
                + " * " + salesTaxPercent + " = "
                + NumberFormat.getCurrencyInstance().format(subTotalPlusTax));

    }

    private void displayTotal(double number) {

        TextView priceTextView = (TextView) findViewById(R.id.total_cost_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number) + "\nThank You!");
    }

}
