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

public class UpdateProfileActivity extends AppCompatActivity {

    String strName, strMobileno, strEmailid, strUsername;
    
    EditText etName, etMobileno, etEmailid, etUsername;
    AppCompatButton btnUpdateProfile;
    
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        
        strName = getIntent().getStringExtra("name");
        strMobileno = getIntent().getStringExtra("mobileno");
        strEmailid = getIntent().getStringExtra("emailid");
        strUsername = getIntent().getStringExtra("username");
        
        etName = findViewById(R.id.etUpdateProfileName);
        etMobileno = findViewById(R.id.etUpdateProfileMobileno);
        etEmailid = findViewById(R.id.etUpdateProfileEmailid);
        etUsername = findViewById(R.id.etUpdateProfileUsername);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        
        etName.setText(strName);
        etMobileno.setText(strMobileno);
        etEmailid.setText(strEmailid);
        etUsername.setText(strUsername);
        
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(UpdateProfileActivity.this);
                progressDialog.setTitle("Updating Profile");
                progressDialog.setMessage("Please Wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                
                updateProfile();
            }
        });
    }

    private void updateProfile() {

        AsyncHttpClient client = new AsyncHttpClient();  //client-server communication establishment
        RequestParams params = new RequestParams();  //put the data in client

        params.put("name",etName.getText().toString());
        params.put("emailid",etEmailid.getText().toString());
        params.put("username",etUsername.getText().toString());
        params.put("mobileno",etMobileno.getText().toString());

        client.post("http://192.168.113.99:80/FoodizAPI/updateProfileDetails.php",params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateProfileActivity.this,MyProfileActivity.class);
                        startActivity(i);
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Profile not Updated", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
        
    }
}