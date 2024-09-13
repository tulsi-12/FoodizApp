package com.mountreachsolution.foodizapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    AppCompatButton acbtnEditProfile;
    TextView tvName,tvMobileNo,tvEmailId,tvUsername;

    ProgressDialog progressDialog;
    SharedPreferences preferences;
    String strUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        ivProfilePhoto = findViewById(R.id.ivMyProfileProfilePhoto);
        acbtnEditProfile = findViewById(R.id.acbtnMyProfileeditprofiele);
        tvName=findViewById(R.id.tvMyProfileName);
        tvMobileNo = findViewById(R.id.tvMyProfileMobileNo);
        tvEmailId=findViewById(R.id.tvMyProfileEmailId);
        tvUsername=findViewById(R.id.tvMyProfileUsername);

        preferences= PreferenceManager.getDefaultSharedPreferences(MyProfileActivity.this);
        strUsername=preferences.getString("username","");
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog=new ProgressDialog(MyProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        getMyDetails();
    }

    private void getMyDetails() {
        AsyncHttpClient client=new AsyncHttpClient();   //client-server communication, sends data over the network
        RequestParams params = new RequestParams();  //put the data

        params.put("username",strUsername);

        client.post("http://192.168.43.102:80/FoodizAPI/mydetails.php",params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetails");
                    for (int i=0;i< jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String image = jsonObject.getString("image");
                        String name = jsonObject.getString("name");
                        String mobileno = jsonObject.getString("mobileno");
                        String emailid = jsonObject.getString("emailid");
                        String username = jsonObject.getString("username");

                        tvName.setText(name);
                        tvMobileNo.setText(mobileno);
                        tvEmailId.setText(emailid);
                        tvUsername.setText(username);
                        progressDialog.dismiss();

                        Glide.with(MyProfileActivity.this)
                                .load("http://192.168.43.102:80/FoodizAPI/images/"+image)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)  // Disable disk caching
                                .skipMemoryCache(true)  // Skip memory caching
                                .error(R.drawable.icon_menu_category)
                                .placeholder(R.drawable.icon_menu_home)
                                .into(ivProfilePhoto);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(MyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}