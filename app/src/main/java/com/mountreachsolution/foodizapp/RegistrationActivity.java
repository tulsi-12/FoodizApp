package com.mountreachsolution.foodizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {
    ImageView ivLogo;
    EditText etName,etMobileNo,etEmailId,etUsername,etPassword;
    Button btnRegister;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ivLogo=findViewById(R.id.ivRegistrationLogo);
        etName=findViewById(R.id.etregisterName);
        etMobileNo=findViewById(R.id.etregisterMobileNo);
        etEmailId=findViewById(R.id.etregisterEmail);
        etUsername=findViewById(R.id.etregisterUserName);
        etPassword=findViewById(R.id.etregisterPassword);
        btnRegister=findViewById(R.id.btnregisterregister);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()){
                    etName.setError("Please enter your name");
                } else if (etMobileNo.getText().toString().isEmpty()) {
                    etMobileNo.setError("Please enter your mobile number");
                } else if (etMobileNo.getText().toString().length()!=10) {
                    etMobileNo.setError("Enter 10 digit valid mobile number");
                } else if (etEmailId.getText().toString().isEmpty()) {
                    etEmailId.setError("Please enter your email id");
                } else if (!etEmailId.getText().toString().contains("@") && !etEmailId.getText().toString().contains(".com")) {
                    etEmailId.setError("Please enter valid email id");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please enter your Username");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please enter your password");
                }
                else{
                    progressDialog=new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Registering....");
                    progressDialog.setMessage("Please wait..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + etMobileNo.getText().toString(),
                            60,
                            TimeUnit.SECONDS, RegistrationActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken
                                        forceResendingToken) {
                                    progressDialog.dismiss();
                                    Intent i = new Intent(RegistrationActivity.this,OTPVerificationActivity.class);
                                    i.putExtra("name",etName.getText().toString());
                                    i.putExtra("mobileno",etMobileNo.getText().toString());
                                    i.putExtra("email",etEmailId.getText().toString());
                                    i.putExtra("username",etUsername.getText().toString());
                                    i.putExtra("password",etPassword.getText().toString());
                                    i.putExtra("verificationcode",verificationCode);
                                    startActivity(i);
                                }
                            });
//                    userRegister();
                }
            }
            public void userRegister() {
                AsyncHttpClient client = new AsyncHttpClient(); //manages the operation over the network
                RequestParams params = new RequestParams();  //put the data

                params.put("name",etName.getText().toString());
                params.put("mobileno",etMobileNo.getText().toString());
                params.put("emailid",etEmailId.getText().toString());
                params.put("username",etUsername.getText().toString());
                params.put("password",etPassword.getText().toString());

                client.post("http:// 192.168.43.102:80/FoodizAPI/userregister.php",params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String status = response.getString("success");
                            if (status.equals("1")){
                                Toast.makeText(RegistrationActivity.this,"Registration sucessfully done",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                                startActivity(i);
                                progressDialog.dismiss();
                            }else{
                                Toast.makeText(RegistrationActivity.this,"Already data exists",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(RegistrationActivity.this,"Could not connect",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
}