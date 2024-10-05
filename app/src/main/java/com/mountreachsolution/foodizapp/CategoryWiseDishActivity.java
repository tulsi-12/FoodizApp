package com.mountreachsolution.foodizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryWiseDishActivity extends AppCompatActivity {
    SearchView searchCategoryWiseDish;
    ListView lvMultipleDishes;
    TextView tvNoDishAvailable;
    String strCategoryName;
    List<POJOCategoryWiseDish> pojoCategoryWiseDishes;
    AdapterCategoryWiseDish adapterCategoryWiseDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_dish);

        searchCategoryWiseDish =findViewById(R.id.svCategorywiseDishCategorySearch);
        lvMultipleDishes=findViewById(R.id.lvCategorywiseDishMultipleDishes);
        tvNoDishAvailable = findViewById(R.id.tvCategorywiseDishNoDishAvailable);

        pojoCategoryWiseDishes = new ArrayList<>();

        searchCategoryWiseDish.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDishByCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchDishByCategory(query);
                return false;
            }
        });

        strCategoryName=getIntent().getStringExtra("categoryname");
        categoryWiseDishes();
    }

    private void searchDishByCategory(String query) {
        List<POJOCategoryWiseDish> tempList = new ArrayList<>();
        tempList.clear();
        for (POJOCategoryWiseDish obj:pojoCategoryWiseDishes) {
            if (obj.getRestname().toUpperCase().contains(query.toUpperCase()) ||
            obj.getDishname().toUpperCase().contains(query.toUpperCase()) ||
            obj.getDishprice().toUpperCase().contains(query.toUpperCase()) ||
            obj.getDishcategory().toUpperCase().contains(query.toUpperCase()))
            {
                tempList.add(obj);
            }

            adapterCategoryWiseDish=new AdapterCategoryWiseDish(tempList,CategoryWiseDishActivity.this);
            lvMultipleDishes.setAdapter(adapterCategoryWiseDish);
        }
    }

    private void categoryWiseDishes() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("categoryname",strCategoryName);

        client.post("http://192.168.113.99:80/FoodizAPI/categorywisedish.php",params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("categorywisedish");
                    if (jsonArray.isNull(0)){
                        tvNoDishAvailable.setVisibility(View.VISIBLE);
                        lvMultipleDishes.setVisibility(View.GONE);
                    }
                    for (int i=0;i< jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("id");
                        String categoryname=jsonObject.getString("categoryname");
                        String restname=jsonObject.getString("restname");
                        String dishcategory=jsonObject.getString("dishcategory");
                        String dishimg=jsonObject.getString("dishimg");
                        String dishname=jsonObject.getString("dishname");
                        String dishprice=jsonObject.getString("dishprice");
                        String dishoffer=jsonObject.getString("dishoffer");
                        String dishrating=jsonObject.getString("dishrating");
                        String dishdescription=jsonObject.getString("dishdescription");

                        pojoCategoryWiseDishes.add(new POJOCategoryWiseDish(id,categoryname,restname,dishcategory,dishimg,dishname,
                                dishprice,dishoffer,dishrating,dishdescription));
                    }
                    adapterCategoryWiseDish = new AdapterCategoryWiseDish(pojoCategoryWiseDishes,CategoryWiseDishActivity.this);
                    lvMultipleDishes.setAdapter(adapterCategoryWiseDish);
                } catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(CategoryWiseDishActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}