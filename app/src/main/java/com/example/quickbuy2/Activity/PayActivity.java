package com.example.quickbuy2.Activity;

import static com.example.quickbuy2.Constants.BUSINESS_SHORT_CODE;
import static com.example.quickbuy2.Constants.CALLBACKURL;
import static com.example.quickbuy2.Constants.PARTYB;
import static com.example.quickbuy2.Constants.PASSKEY;
import static com.example.quickbuy2.Constants.TRANSACTION_TYPE;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quickbuy2.Model.AccessToken;
import com.example.quickbuy2.Model.STKPush;
import com.example.quickbuy2.R;
import com.example.quickbuy2.Services.DarajaApiClient;
import com.example.quickbuy2.Utils;
import com.example.quickbuy2.databinding.ActivityPayBinding;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;
    private ActivityPayBinding binding;


   /* @BindView(R.id.etAmount)
    EditText mAmount;

    @BindView(R.id.etPhone)
    EditText mPhone;

    @BindView(R.id.btnPay)
    Button mPay;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);

        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mProgressDialog = new ProgressDialog(this);

        try {
            mApiClient = new DarajaApiClient();
        Log.d("mpesa auth", "onCreate: mApiClient"+mApiClient);
        } catch (IOException e) {
            Log.d("mpesa auth", "onCreate: mApiClient exception thrown"+e);
            throw new RuntimeException(e);

        }
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        //mPay.setOnClickListener(this);
        binding.btnPay.setOnClickListener(this);

        getAccessToken();

    }

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);

                }else {
                    // Handle access token retrieval error
                    Timber.e("Failed to get access token: %d", response.code());
                    }
            }

            public void onFailure(Call<AccessToken> call, Throwable t) {
                Timber.e("Network error: %s", t.getMessage());
            }




        });
    }


    /*@Override
    public void onClick(View view) {
        if (view== mPay){
            String phone_number = mPhone.getText().toString();
            String amount = mAmount.getText().toString();
            performSTKPush(phone_number,amount);
        }
    }*/
    @Override
    public void onClick(View view) {
        if (view == binding.btnPay) {
            String phone_number = binding.etPhone.getText().toString().trim(); // Trim whitespace
            String amount = binding.etAmount.getText().toString().trim(); // Trim whitespace

            // Basic input validation (consider more thorough validation)
            if (phone_number.isEmpty()) {
                // Show error message for empty phone number
                binding.etPhone.setError("Please enter a phone number");
                return;
            }
            if (amount.isEmpty()) {
                // Show error message for empty amount
                binding.etAmount.setError("Please enter an amount");
                return;
            }

            performSTKPush(phone_number, amount);
        }
    }


    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "QuickBuy2", //Account reference
                "QuickBuy STK PUSH"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        //Timber.d("post submitted to API. %s", response.body());
                        Timber.d("STK Push Request: %s", stkPush.toString()); // Log request body
                        Timber.d("STK Push Response: %s", response.body().toString()); // Log response body
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t,"STK Push Failed");
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
