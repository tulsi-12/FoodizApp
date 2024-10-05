package com.mountreachsolution.foodizapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TextView tvName,tvEmail;
    Button btnSignOut;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvName=findViewById(R.id.tvName);
        tvEmail=findViewById(R.id.tvEmail);
        btnSignOut=findViewById(R.id.btnSignOut);

        bottomNavigationView=findViewById(R.id.homeBottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(HomeActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNavMenuHome);

        preferences= PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        editor= preferences.edit();

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient= GoogleSignIn.getClient(HomeActivity.this,googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(HomeActivity.this);

        if (googleSignInAccount!=null){
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();

            tvName.setText(name);
            tvEmail.setText(email);

            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(HomeActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            if (item.getItemId()==R.id.offersHomeMenu){
                Intent i = new Intent(HomeActivity.this,OffersActivity.class);
                startActivity(i);
            } else if (item.getItemId()==R.id.myCartHomeMenu) {
                Intent i = new Intent(HomeActivity.this,MyCartActivity.class);
                startActivity(i);
            } else if (item.getItemId()==R.id.myProfileHomeMenu) {
                Intent i = new Intent(HomeActivity.this,MyProfileActivity.class);
                startActivity(i);
            } else if (item.getItemId()==R.id.logoutHomeMenu) {

                logout();
            }
        return true;
    }

    private void logout() {
        android.app.AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("FoodizApp");
        ad.setMessage("Are You Sure to Logout");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                editor.putBoolean("isLogin", false).commit();
                startActivity(intent);
            }
        }).create().show();
    }

    HomeFragment homeFragment = new HomeFragment();
    CategoryFragment categoryFragment= new CategoryFragment();
    NearbyStoreFragment nearbyStoreFragment= new NearbyStoreFragment();
    OrderHistoryFragment orderHistoryFragment= new OrderHistoryFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       if (item.getItemId()==R.id.homeBottomNavMenuHome){
           getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,homeFragment).commit();
       } else if (item.getItemId()==R.id.categoryBottomNavMenuHome) {
           getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,categoryFragment).commit();
       } else if (item.getItemId()==R.id.nearbyStoresBottomNavMenuHome) {
           getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,nearbyStoreFragment).commit();
       }else if(item.getItemId()==R.id.orderHistoryBottomNavMenuHome) {
           getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,orderHistoryFragment).commit();
       }

        return true;
    }
}