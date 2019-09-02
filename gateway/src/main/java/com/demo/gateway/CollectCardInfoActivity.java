package com.demo.gateway;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CollectCardInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_PREFIX = "com.mastercard.gateway.sample.EXTRA_";

    // request
    public static final String EXTRA_GOOGLE_PAY_TXN_AMOUNT = EXTRA_PREFIX + "GOOGLE_PAY_TXN_AMOUNT";
    public static final String EXTRA_GOOGLE_PAY_TXN_CURRENCY = EXTRA_PREFIX + "GOOGLE_PAY_TXN_CURRENCY";

    // response
    public static final String EXTRA_CARD_DESCRIPTION = EXTRA_PREFIX + "CARD_DESCRIPTION";
    public static final String EXTRA_CARD_NAME = EXTRA_PREFIX + "CARD_NAME";
    public static final String EXTRA_CARD_NUMBER = EXTRA_PREFIX + "CARD_NUMBER";
    public static final String EXTRA_CARD_EXPIRY_MONTH = EXTRA_PREFIX + "CARD_EXPIRY_MONTH";
    public static final String EXTRA_CARD_EXPIRY_YEAR = EXTRA_PREFIX + "CARD_EXPIRY_YEAR";
    public static final String EXTRA_CARD_CVV = EXTRA_PREFIX + "CARD_CVC";
    public static final String EXTRA_PAYMENT_TOKEN = EXTRA_PREFIX + "PAYMENT_TOKEN";

    private TextInputEditText nameOnCard;
    private TextInputEditText cardNumber;
    private TextInputEditText expiryMonth;
    private TextInputEditText expiryYear;
    private TextInputEditText cardCvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_card_info);

        nameOnCard = findViewById(R.id.nameOnCard);
        cardNumber = findViewById(R.id.cardnumber);
        expiryMonth = findViewById(R.id.expiry_month);
        expiryYear = findViewById(R.id.expiry_year);
        cardCvv = findViewById(R.id.cvv);

        Button continueButton=findViewById(R.id.submit_button);
        continueButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submit_button){
            String cardname = nameOnCard.getText().toString();
            String cardnumber = cardNumber.getText().toString();
            String expirymonth = expiryMonth.getText().toString();
            String expiryyear = expiryYear.getText().toString();
            String cardcvv = cardCvv.getText().toString();

            Intent intent=new Intent();
            intent.putExtra(EXTRA_CARD_NAME,cardname);
            intent.putExtra(EXTRA_CARD_NUMBER,cardnumber);
            intent.putExtra(EXTRA_CARD_EXPIRY_MONTH,expirymonth);
            intent.putExtra(EXTRA_CARD_EXPIRY_YEAR,expiryyear);
            intent.putExtra(EXTRA_CARD_CVV,cardcvv);

            setResult(Activity.RESULT_OK,intent);
            finish();

        }

    }
}
