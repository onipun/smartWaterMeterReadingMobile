package com.example.meterreading.ui.HouseArea;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meterreading.R;
import com.example.meterreading.ui.ui.HouseList.HouseList;


public class HouseAreaAdapter extends RecyclerView.Adapter<HouseAreaAdapter.MyViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        TextView txtHomeAreaName;
        ConstraintLayout layout;
        public MyViewHolder(View v) {
            super(v);
            view = v;
            txtHomeAreaName = view.findViewById(R.id.houseAreaName);
            layout = view.findViewById(R.id.layout_area_name);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HouseAreaAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HouseAreaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.house_area_name, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtHomeAreaName.setText(mDataset[position]);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //call "HomeRepository"'s method to handle instantiate of "Homemodel"
                Intent intent = new Intent(v.getContext(), HouseList.class);
                intent.putExtra("houseArea" , mDataset[position]);
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

