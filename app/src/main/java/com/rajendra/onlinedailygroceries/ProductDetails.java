package com.rajendra.onlinedailygroceries;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class ProductDetails extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 9999;
    ImageView img, back;
    TextView proName, proPrice, proDesc, proQty, proUnit;

    String name, price, desc, qty, unit;
    int image;

    static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);
    String total, comment;

    private Button buyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent i = getIntent();

         name = i.getStringExtra("name");
         image = i.getIntExtra("image", R.drawable.b1);
         price = i.getStringExtra("price");
         desc = i.getStringExtra("desc");
         qty = i.getStringExtra("qty");
         unit = i.getStringExtra("unit");

         proName = findViewById(R.id.productName);
         proDesc = findViewById(R.id.prodDesc);
         proPrice = findViewById(R.id.prodPrice);
         img = findViewById(R.id.big_image);
         back = findViewById(R.id.back2);
         proQty = findViewById(R.id.qty);
         proUnit = findViewById(R.id.unit);

         proName.setText(name);
         proPrice.setText(price);
         proDesc.setText(desc);
         proQty.setText(qty);
         proUnit.setText(unit);

        buyBtn = findViewById(R.id.buyBtn);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total = proPrice.getText().toString()
                .replace("MYR", "")
                .replace(" ", "");

                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(total), "MYR", "Groceries App", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                startActivityForResult(intent, PAYPAL_REQUEST_CODE);
                //System.out.println("hello: " + total);
            }
        });

        img.setImageResource(image);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProductDetails.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(Integer.parseInt("4"));
                        JSONObject jsonObject = new JSONObject(paymentDetail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // this tutorial has been completed
    // see you in the next.
}


























