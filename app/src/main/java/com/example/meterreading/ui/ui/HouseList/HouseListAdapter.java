package com.example.meterreading.ui.ui.HouseList;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meterreading.R;

import java.util.ArrayList;
import java.util.List;

public class HouseListAdapter extends RecyclerView.Adapter<HouseListAdapter.MyViewHolder> {
    String[] mDataset, identityCard;
    HomeFactory homeFactory = HomeFactory.getInstance();
    HomeModel homeModel;
    List<HomeModel> homeModels = new ArrayList<>();
    private static final String TAG = HouseListAdapter.class.getSimpleName();



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        LinearLayout layout;
        TextView txtHomeListName;
        public MyViewHolder(View v) {
            super(v);
            view = v;
            txtHomeListName = view.findViewById(R.id.houseListName);
            layout = view.findViewById(R.id.layout_list_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HouseListAdapter(String[] myDataset, String[] identityCard) {

        this.mDataset = myDataset;
        this.identityCard = identityCard;

        for (int i = 0; i < myDataset.length;i++){
            HomeModel home = new HomeModel();
            home.setAddr("tamanRia");
            home.setIndex(String.valueOf(i));
            home.setHouseId(myDataset[i]);
            home.setIdentityCard(identityCard[i]);

            homeModels.add(home);
        }
        homeFactory.setHomeModels(homeModels);

    }

    // Create new views (invoked by the layout manager)
    @Override
    public HouseListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.house_list_name, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtHomeListName.setText(mDataset[position]);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String homeID, addr, identityCard;
                homeModel = HomeFactory.getInstance().getHome(String.valueOf(position));
                homeID = homeModel.getHouseId();
                addr = homeModel.getAddr();
                identityCard = homeModel.getIdentityCard();
                Log.d(TAG, "identity card: "+identityCard);

                Intent intent = new Intent(v.getContext(),Home.class);
                intent.putExtra("houseID", homeID);
                intent.putExtra("addr", addr);
                intent.putExtra("identityCard", identityCard);
                v.getContext().startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
