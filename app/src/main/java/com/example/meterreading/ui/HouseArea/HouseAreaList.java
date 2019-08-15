//error 75  - 79
//https://stackoverflow.com/questions/33896030/how-do-i-use-a-variable-outside-the-onresponse-in-android
//https://mobikul.com/how-to-send-json-post-request-using-volley-rest-api/

package com.example.meterreading.ui.HouseArea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meterreading.R;
import com.example.meterreading.ui.ui.HouseList.HomeFactory;
import com.example.meterreading.ui.ui.HouseList.HomeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;


public class HouseAreaList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    final static String TAG = HouseAreaList.class.getSimpleName();

    private TreeSet<String> myDataDistinct = new TreeSet<>();
    public JSONArray jsonArray;
    HomeModel homeModel;

    List<HomeModel> homeModels = new ArrayList<>();

    // Instantiate the RequestQueue.
    RequestQueue queue;
    String url ="http://192.168.43.173:8080/user/all";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_area_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.house_Area_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getHouseList();
    }

    protected void getHouseList(){
        //clear up factory before refill with data
        HomeFactory.getInstance().clear();
        homeModels.clear();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "onResponse: "+ response);
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                myDataDistinct.add(jsonObject.getString("area"));

                                homeModel = new HomeModel();
                                homeModel.setHouseId(jsonObject.getString("houseID"));
                                homeModel.setArea(jsonObject.getString("area"));
                                homeModel.setAddr(jsonObject.getString("address"));
                                homeModel.setIdentityCard(jsonObject.getString("identityCard"));
                                homeModels.add(homeModel);
                            }

                            HomeFactory.getInstance().setHomeModels(homeModels);


                            String [] myDataset = new String[myDataDistinct.size()];
                            Iterator iterator = myDataDistinct.iterator();

                            Log.d(TAG, "onCreate: " + myDataDistinct.size());

                            int i = 0;
                            while (iterator.hasNext()) {
                                myDataset[i] = iterator.next().toString();
                                i++;
                            }

                            mAdapter = new HouseAreaAdapter(myDataset);

                            recyclerView.setAdapter(mAdapter);


                        }catch (JSONException e){
                            Log.e(TAG, "onResponse: ", e );
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HouseAreaList.this, "no house area set! or check Db server", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
