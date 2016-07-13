package com.example.android.justjava;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

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
        intializeProperties(props);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void intializeProperties(Properties props) {

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

        TextView orderSummary = (TextView) findViewById(R.id.order_summary_message);

        CheckBox checkBox = (CheckBox) findViewById(R.id.whipped_cream);

        orderSummary.setText("Name: Michael Peterson\n"
                + "Add whipped cream? " + checkBox.isChecked() + "\n"
                + "Quantity: " + quantity +  "\n"
                + "Total: " + (quantity * PRICE_PER_CUP) + ((quantity * PRICE_PER_CUP) * SALES_TAX) + "\n"
                + "Thank You!");
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
    }

    public void addTopping(View view) {



    }

    private void display(int number) {

        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number + COST_PER_CUP);
    }


}
