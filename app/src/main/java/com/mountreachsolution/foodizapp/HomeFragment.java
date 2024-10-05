package com.mountreachsolution.foodizapp;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class HomeFragment extends Fragment {

    // class_name object_name;
ListView lvMultipleCategory;
TextView tvCategoryNotAvailable;
List<POJOGetAllCategories> pojoGetAllCategories;
GetAllCategoryAdapter getAllCategoryAdapter;
SearchView searchCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // object_name=findViewById(R.id.id_name);

        lvMultipleCategory = view.findViewById(R.id.lvHomeFragmentMultipleCategories);
        tvCategoryNotAvailable = view.findViewById(R.id.tvHomeFragmentNoCategoryFound);
        searchCategory=view.findViewById(R.id.svHomeFragmentCategorySearch);

        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchCategory(query);
                return false;
            }
        });

        pojoGetAllCategories= new ArrayList<>();

        getAllCategoryDetails();

        return view;
    }

    private void searchCategory(String query) {
        List<POJOGetAllCategories> temporaryCategory = new ArrayList<>();
        temporaryCategory.clear();
        for (POJOGetAllCategories obj:pojoGetAllCategories){
            if (obj.getCategoryName().toUpperCase().contains(query.toUpperCase()))
            {
                temporaryCategory.add(obj);

            }

            getAllCategoryAdapter = new GetAllCategoryAdapter(temporaryCategory,getActivity());
            lvMultipleCategory.setAdapter(getAllCategoryAdapter);
        }
    }

    private void getAllCategoryDetails() {

// class_name object_name = new_keyword Constructor_name();
        AsyncHttpClient client = new AsyncHttpClient(); //client-server communication
        RequestParams params = new RequestParams(); //put the data

        client.post("http://192.168.113.99:80/FoodizAPI/getAllCategoryDetails.php",params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getAllCategory");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String categoryimage = jsonObject.getString("categoryimage");
                        String categoryname = jsonObject.getString("categoryname");

                        pojoGetAllCategories.add(new POJOGetAllCategories(id,categoryimage,categoryname));

                    }

                    getAllCategoryAdapter = new GetAllCategoryAdapter(pojoGetAllCategories,getActivity());
                    lvMultipleCategory.setAdapter(getAllCategoryAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(),"Server Not Found",Toast.LENGTH_SHORT).show();
            }
        });
    }
}