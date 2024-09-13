package com.mountreachsolution.foodizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmMobileNumberActivity extends AppCompatActivity {

    EditText etConfirmRegisterMobileNumber;
    AppCompatButton btnMobielNumberVerify;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_mobile_number);

        etConfirmRegisterMobileNumber=findViewById(R.id.etConfirmMobileNumberRegisteredMobileNumber);
        btnMobielNumberVerify=findViewById(R.id.btnConfirmMobileNumberVerify);

        btnMobielNumberVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etConfirmRegisterMobileNumber.getText().toString().isEmpty()){
                    etConfirmRegisterMobileNumber.setError("Please enter mobile number");
                } else if (etConfirmRegisterMobileNumber.getText().toString().length()!=10) {
                    etConfirmRegisterMobileNumber.setError("Please enter valid mobile number");
                }else {
                    progressDialog=new ProgressDialog(ConfirmMobileNumberActivity.this);
                    progressDialog.setTitle("Verifying Mobile Number");
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + etConfirmRegisterMobileNumber.getText().toString(),
                            60, TimeUnit.SECONDS,
                            ConfirmMobileNumberActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmMobileNumberActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmMobileNumberActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent i = new Intent(ConfirmMobileNumberActivity.this,ForgetPasswordOTPVerificationActivity.class);
                                    i.putExtra("verificationcode",verificationCode);
                                    i.putExtra("mobileno",etConfirmRegisterMobileNumber.getText().toString());
                                    startActivity(i);

                                }
                            });
                }
            }
        });

    }
}