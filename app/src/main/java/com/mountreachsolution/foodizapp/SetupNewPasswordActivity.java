package com.mountreachsolution.foodizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SetupNewPasswordActivity extends AppCompatActivity {
    EditText etnewPassword,etConfirmPassword;
    AppCompatButton acbtnSetPassword;
    ProgressDialog progressDialog;
    String strMobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_new_password);

          etnewPassword=findViewById(R.id.etSetUpPasswordNewPassword);
          etConfirmPassword=findViewById(R.id.etSetUpPasswordConfirmNewPassword);
          acbtnSetPassword=findViewById(R.id.btnSetUpPasswordSetPassword);

          strMobileNo=getIntent().getStringExtra("mobileno");
        Toast.makeText(this, strMobileNo, Toast.LENGTH_SHORT).show();

          acbtnSetPassword.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (etnewPassword.getText().toString().isEmpty()){
                      etnewPassword.setError("Please enter a new Password");
                  } else if (etConfirmPassword.getText().toString().isEmpty()) {
                      etConfirmPassword.setError("Please enter confirm password");
                  } else if (etnewPassword.getText().toString().length()<8) {
                      etnewPassword.setError("Password should be greater than 8 characters");
                  } else if (etConfirmPassword.getText().toString().length()<8) {
                      etConfirmPassword.setError("Password should be greater than 8 characters");
                  } else if (!etnewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                      etConfirmPassword.setError("Password does not match");
                  }
                  else {
                      progressDialog=new ProgressDialog(SetupNewPasswordActivity.this);
                      progressDialog.setTitle("Changeing Password");
                      progressDialog.setMessage("Please wait..");
                      progressDialog.setCanceledOnTouchOutside(false);
                      progressDialog.show();

                      forgetpassword();
                  }
              }
          });
    }

    private void forgetpassword() {
        AsyncHttpClient client = new AsyncHttpClient(); //client-server communication
        RequestParams params = new RequestParams();  //put the data

        params.put("mobileno","8208187488");
        params.put("password",etnewPassword.getText().toString());

        client.post("http://192.168.164.99:80/FoodizAPI/changepassword.php",params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Intent i = new Intent(SetupNewPasswordActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SetupNewPasswordActivity.this, "Password not changed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(SetupNewPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}