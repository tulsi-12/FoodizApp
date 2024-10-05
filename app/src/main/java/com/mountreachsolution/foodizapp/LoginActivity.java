package com.mountreachsolution.foodizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText etusername, etpassword;
    TextView tvForgetPassword;
    CheckBox cbshowhidepassword;
    Button btnlogin;
    TextView tvnewuser;

    GoogleSignInOptions googleSignInOptions; //shows multiple account options
    GoogleSignInClient googleSignInClient;
    AppCompatButton btnSignInWithGoogle;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etusername = findViewById(R.id.et_loginusername);
        etpassword = findViewById(R.id.et_loginpassword);
        tvForgetPassword=findViewById(R.id.tvLoginForgetPassword);
        cbshowhidepassword = findViewById(R.id.cb_loginshowhide);
        btnlogin = findViewById(R.id.btnlogin);
        tvnewuser = findViewById(R.id.tvloginnewuser);
        btnSignInWithGoogle=findViewById(R.id.acbtnLoginSigninwithgoogle);

        preferences= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor=preferences.edit();

        if (preferences.getBoolean("isLogin",false))
        {
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);

        }

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);

        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }

        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ConfirmMobileNumberActivity.class);
                startActivity(i);
            }
        });



        cbshowhidepassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etusername.getText().toString().isEmpty()) {
                    etusername.setError("Enter Your Username");
                } else if (etpassword.getText().toString().isEmpty()) {
                    etpassword.setError("Enter Your Password");
                } else if (etusername.getText().toString().length() < 8) {
                    etusername.setError("Enter 8 Character Username");
                } else if (etpassword.getText().toString().length() < 8) {
                    etpassword.setError("Enter 8 Charater Password");
                } else if (!etusername.getText().toString().matches(".*[A-Z].*")) {
                    etusername.setError("Use 1 Uppercase Letter");
                } else if (!etusername.getText().toString().matches(".*[a-z].*")) {
                    etusername.setError("Use 1 Lowercase Letter");
                } else if (!etusername.getText().toString().matches(".*[0-9].*")) {
                    etusername.setError("Use 1 Number");
                } else if (!etusername.getText().toString().matches(".*[@,#,$,*,&,!].*")) {
                    etusername.setError("Use 1 Special Symbol");
                } else {
                    Toast.makeText(LoginActivity.this, "Login Successfully Done", Toast.LENGTH_SHORT).show();
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Login User");
                    progressDialog.setMessage("Please Wait ");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    userLogin();
                }
            }
        });

        tvnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
        }

    private void signIn() {
        Intent i = googleSignInClient.getSignInIntent();
        startActivityForResult(i,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==999){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    private void userLogin() {
        AsyncHttpClient client = new AsyncHttpClient(); //manage data over network
        RequestParams params = new RequestParams(); //put or store data

        params.put("username",etusername.getText().toString());
        params.put("password",etpassword.getText().toString());

        client.post("http://192.168.113.99:80/FoodizAPI/userlogin.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    String userrole= response.getString("userrole");
                    if(status.equals("1") && userrole.equals("user")){
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Login Successfully Done ",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        editor.putBoolean("isLogin",true).commit();
                        editor.putString("username",etusername.getText().toString()).commit();
                        startActivity(i);
                    }
                    else if(status.equals("1") && userrole.equals("admin")){
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Login Successfully Done ",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(i);
                    }
                    else {
                     Toast.makeText(LoginActivity.this,"Invalid Username or Password",Toast.LENGTH_SHORT).show();
                     progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,"Server Issue",Toast.LENGTH_SHORT).show();
            }
        });
    }
}