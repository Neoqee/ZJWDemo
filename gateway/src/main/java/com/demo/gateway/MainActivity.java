package com.demo.gateway;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mastercard.gateway.android.sdk.Gateway;
import com.mastercard.gateway.android.sdk.GatewayCallback;
import com.mastercard.gateway.android.sdk.GatewayMap;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_CARD_INFO = 100;

    // static for demo
    static final String AMOUNT = "1.00";
    static final String CURRENCY = "HKD";

    private final String merchantId="TEST010826188";
    private Gateway gateway;
    private ApiController apiController;
    String sessionId, apiVersion, threeDSecureId, orderId, transactionId;
    //    private final String region="NORTH_AMERICA";
//    private final String testUrl="https://gateway-test-merchant-server.herokuapp.com";
//    https://ap-gateway.mastercard.com/api/rest/version/52/merchant/{merchantId}/session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiController=new ApiController();

        gateway = new Gateway();
        gateway.setMerchantId(merchantId);
        gateway.setRegion(Gateway.Region.ASIA_PACIFIC);

        // random order/txn IDs for example purposes
        orderId = UUID.randomUUID().toString();
        orderId = orderId.substring(0, orderId.indexOf('-'));
        System.out.println("orderId:-------"+orderId);
        transactionId = UUID.randomUUID().toString();
        transactionId = transactionId.substring(0, transactionId.indexOf('-'));
        System.out.println("transactionId:-------"+transactionId);

        createSession();
        Button button=findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CARD_INFO) {

            if (resultCode == Activity.RESULT_OK) {

                assert data != null;
                String cardName = data.getStringExtra(CollectCardInfoActivity.EXTRA_CARD_NAME);
                String cardNumber = data.getStringExtra(CollectCardInfoActivity.EXTRA_CARD_NUMBER);
                String cardExpiryMonth = data.getStringExtra(CollectCardInfoActivity.EXTRA_CARD_EXPIRY_MONTH);
                String cardExpiryYear = data.getStringExtra(CollectCardInfoActivity.EXTRA_CARD_EXPIRY_YEAR);
                String cardCvv = data.getStringExtra(CollectCardInfoActivity.EXTRA_CARD_CVV);

                updateSession(cardName, cardNumber, cardExpiryMonth, cardExpiryYear, cardCvv);

            }

            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createSession(){
        apiController.createSession(new CreateSessionCallback());
    }

    private void updateSession(String cardName, String cardNumber, String cardExpiryMonth, String cardExpiryYear, String cardCvv) {
        GatewayMap request = new GatewayMap()
                .set("sourceOfFunds.provided.card.nameOnCard", cardName)
                .set("sourceOfFunds.provided.card.number", cardNumber)
                .set("sourceOfFunds.provided.card.securityCode", cardCvv)
                .set("sourceOfFunds.provided.card.expiry.month", cardExpiryMonth)
                .set("sourceOfFunds.provided.card.expiry.year", cardExpiryYear);

        gateway.updateSession(sessionId, apiVersion, request,new UpdateSessionCallback());
    }

    private void check3dsEnrollment() {
        // generate a random 3DSecureId for testing
        String threeDSId = UUID.randomUUID().toString();
        threeDSId = threeDSId.substring(0, threeDSId.indexOf('-'));

        apiController.check3DSecureEnrollment(sessionId, AMOUNT, CURRENCY, threeDSId, new Check3DSecureEnrollmentCallback());
    }

    private void processPayment() {
        apiController.completeSession(sessionId, "TEST01001", "1", AMOUNT, CURRENCY, new CompleteSessionCallback());
    }

    @Override
    public void onClick(View v) {
//        if (v.getId()==R.id.button){
//            Intent intent=new Intent(this,CollectCardInfoActivity.class);
////            startActivity(intent);
//            startActivityForResult(intent,REQUEST_CARD_INFO);
//        }

        GatewayMap request = new GatewayMap()
//                .set("sourceOfFunds.provided.card.nameOnCard", "Mike Jor")
                .set("sourceOfFunds.provided.card.number", "5123450000000008")
                .set("sourceOfFunds.provided.card.securityCode", "100")
                .set("sourceOfFunds.provided.card.expiry.month", "05")
                .set("sourceOfFunds.provided.card.expiry.year", "21");

        gateway.updateSession(sessionId, apiVersion, request,new UpdateSessionCallback());

    }


    class CreateSessionCallback implements ApiController.CreateSessionCallback{

        @Override
        public void onSuccess(String sessionId, String apiVersion) {
            MainActivity.this.sessionId=sessionId;
            MainActivity.this.apiVersion=apiVersion;
        }

        @Override
        public void onError(Throwable throwable) {

        }
    }

    class UpdateSessionCallback implements GatewayCallback {
        @Override
        public void onSuccess(GatewayMap response) {
            Log.i(MainActivity.class.getSimpleName(), "Successfully updated session");
//            check3dsEnrollment();
            System.out.println(response);
            processPayment();
        }

        @Override
        public void onError(Throwable throwable) {
            Log.e(MainActivity.class.getSimpleName(), throwable.getMessage(), throwable);
            System.out.println(throwable.getMessage());

        }
    }

    class Check3DSecureEnrollmentCallback implements ApiController.Check3DSecureEnrollmentCallback {
        @Override
        public void onSuccess(GatewayMap response) {
            int apiVersionInt = Integer.valueOf(apiVersion);
            String threeDSecureId = (String) response.get("gatewayResponse.3DSecureID");

            String html = null;
            if (response.containsKey("gatewayResponse.3DSecure.authenticationRedirect.simple.htmlBodyContent")) {
                html = (String) response.get("gatewayResponse.3DSecure.authenticationRedirect.simple.htmlBodyContent");
            }

            // for API versions <= 46, you must use the summary status field to determine next steps for 3DS
            if (apiVersionInt <= 46) {
                String summaryStatus = (String) response.get("gatewayResponse.3DSecure.summaryStatus");

                if ("CARD_ENROLLED".equalsIgnoreCase(summaryStatus)) {
                    Gateway.start3DSecureActivity(MainActivity.this, html);
                    return;
                }

                MainActivity.this.threeDSecureId = null;

                // for these 2 cases, you still provide the 3DSecureId with the pay operation
                if ("CARD_NOT_ENROLLED".equalsIgnoreCase(summaryStatus) || "AUTHENTICATION_NOT_AVAILABLE".equalsIgnoreCase(summaryStatus)) {
                    MainActivity.this.threeDSecureId = threeDSecureId;
                }

                processPayment();
            }

            // for API versions >= 47, you must look to the gateway recommendation and the presence of 3DS info in the payload
            else {
                String gatewayRecommendation = (String) response.get("gatewayResponse.response.gatewayRecommendation");

                // if DO_NOT_PROCEED returned in recommendation, should stop transaction
                if ("DO_NOT_PROCEED".equalsIgnoreCase(gatewayRecommendation)) {
                    return;
                }

                // if PROCEED in recommendation, and we have HTML for 3ds, perform 3DS
                if (html != null) {
                    Gateway.start3DSecureActivity(MainActivity.this, html);
                    return;
                }

                MainActivity.this.threeDSecureId = threeDSecureId;

                processPayment();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            Log.e(MainActivity.class.getSimpleName(), throwable.getMessage(), throwable);
        }
    }

    class CompleteSessionCallback implements ApiController.CompleteSessionCallback {
        @Override
        public void onSuccess(String result) {
            Log.d(MainActivity.class.getSimpleName(), result);
        }

        @Override
        public void onError(Throwable throwable) {
            Log.e(MainActivity.class.getSimpleName(), throwable.getMessage(), throwable);


        }
    }

}
