package com.antonk.urantestapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anton on 07-Nov-15.
 */
public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.CustomViewHolder> {

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView time;

        public CustomViewHolder(View view) {
            super(view);
            this.time = (TextView) view.findViewById(android.R.id.text1);
        }
    }
    private List<String> data;

    public TimesAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        String item = data.get(i);
        customViewHolder.time.setText(item);
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public void add(String item){
        data.add(0, item);
        notifyItemInserted(0);
    }
}