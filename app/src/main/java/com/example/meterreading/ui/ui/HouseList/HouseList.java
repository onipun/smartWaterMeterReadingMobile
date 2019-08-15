package com.example.meterreading.ui.ui.HouseList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.meterreading.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HouseList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private final static String TAG = HouseList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        String houseArea = intent.getStringExtra("houseArea");

        recyclerView = (RecyclerView) findViewById(R.id.house_list_re_recycleview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List <String> homeFilter = new ArrayList<>();
        List <String> homeFilterIdentityCard = new ArrayList<>();

        List<HomeModel> homeModels = HomeFactory.getInstance().getHomes();

        try {
            for (HomeModel homeModel : homeModels) {
                if ((homeModel.getArea()).equals(houseArea)) {
                    homeFilter.add(homeModel.getHouseId());
                    homeFilterIdentityCard.add(homeModel.getIdentityCard());
                    Log.d(TAG, "identity card: "+homeModel.getIdentityCard());

                }
            }
        }catch (Exception e){
            Log.d(TAG, "Home Factory: " + e);
        }

        String[] myDataset = new String[homeFilter.size()];
        String[] identityCard = new String[homeFilter.size()];

        int i = 0;
        for (String houseID: homeFilter) {

          myDataset[i] = houseID;
            i++;
        }

        i = 0;
        for (String identity: homeFilterIdentityCard) {

            identityCard[i] = identity;
            i++;
        }


        try {
            mAdapter = new HouseListAdapter(myDataset, identityCard);
        }catch (Exception e){
            Log.d(TAG, "onCreate: " + e);
        }


        recyclerView.setAdapter(mAdapter);


    }
}
