package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private static final String COST_PER_CUP_PROP = "COST_PER_CUP";
    private static final String SALES_TAX_PROP = "SALES_TAX";
    private static final String PRICE_PER_CUP_PROP = "PRICE_PER_CUP";
    private static final String WHIP_CREAM_COST_PROP = "WHIP_CREAM_COST";
    private static final String CHOC_COST_PROP = "CHOC_COST";

    private static String COST_PER_CUP;
    private static double SALES_TAX;
    private static double PRICE_PER_CUP;
    private static double WHIP_CREAM_COST;
    private static double CHOC_COST;
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
            WHIP_CREAM_COST = Double.parseDouble(props.getProperty(WHIP_CREAM_COST_PROP));
            CHOC_COST = Double.parseDouble(props.getProperty(CHOC_COST_PROP));


        } catch (Exception e) {
            Log.e("Property init failed.", e.getMessage());
        }

    }

    /**
     * Displays the final total with sales tax.
     *
     */
    public void submitOrder(View view) {

        context=this;
        intializeProperties(props);

        TextView cupsOrdered = (TextView) findViewById(R.id.quantity_text_view);

        int quantity = 0;

        if (null != cupsOrdered)
            quantity = Integer.parseInt(cupsOrdered.getText().toString().substring(0, cupsOrdered.getText().toString().indexOf(" ")));

        TextView orderSummary = (TextView) findViewById(R.id.order_summary_message);

        CheckBox whippedCream = (CheckBox) findViewById(R.id.whipped_cream);

        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate);

        TextView nameText = (TextView) findViewById(R.id.name_text);

        if (null != whippedCream && whippedCream.isChecked())
            PRICE_PER_CUP = PRICE_PER_CUP + WHIP_CREAM_COST;
        if (null != chocolate && chocolate.isChecked())
            PRICE_PER_CUP = PRICE_PER_CUP + CHOC_COST;

        double subTotal = quantity * PRICE_PER_CUP;
        double taxTotal = subTotal * SALES_TAX;
        double total = subTotal + taxTotal;

        String summaryText = "";

        if (null != nameText && null != whippedCream && null != chocolate)
            summaryText = "Name: " + nameText.getText() + "\n"
                + "Add whipped cream? " + whippedCream.isChecked() + "\n"
                + "Add chocolate? " + chocolate.isChecked() + "\n"
                + "Quantity: " + quantity +  "\n"
                + "Total: " + NumberFormat.getCurrencyInstance().format(subTotal)
                + " Plus Tax (@ " + (SALES_TAX * 100) + "%): "
                + NumberFormat.getCurrencyInstance().format(taxTotal) +  " = "
                + NumberFormat.getCurrencyInstance().format(total) + "\n"
                + "Thank You!";

        // Send Email
        String subject = "";
        if (null != nameText)
            subject = "JustJava order summary for " + nameText.getText();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, summaryText);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

//        if (null != orderSummary)
//            orderSummary.setText(summaryText);


    }

    /**
     * Increments the number of cups and calculates tax and subtotal.
     *
     */
    public void incrementQty(View view) {

        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);

        int qty = 0;

        if (null != quantityTextView)
            qty = Integer.parseInt(quantityTextView.getText().toString().substring(0, quantityTextView.getText().toString().indexOf(" ")));

        qty++;

        String text = "" + qty + COST_PER_CUP;

        if (null != quantityTextView)
            quantityTextView.setText(text);

        display(qty);
    }

    /**
     * Decrements the number of cups and calculates the tax and subtotal.
     *
     */
    public void decrementQty(View view) {

        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);

        int qty = 0;

        if (null != quantityTextView)
            qty = Integer.parseInt(quantityTextView.getText().toString().substring(0, quantityTextView.getText().toString().indexOf(" ")));

        if (qty == 0)
            return;

        qty--;

        String text = "" + qty + COST_PER_CUP;

        quantityTextView.setText(text);

        display(qty);
    }

    private void display(int number) {

        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);

        String text = "" + number + COST_PER_CUP;

        if (null != quantityTextView)
            quantityTextView.setText(text);
    }


}
