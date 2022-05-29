package com.teton.blelearn.ui.helper.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teton.blelearn.R;

import java.util.List;

public class BleDeviceListAdapter extends  RecyclerView.Adapter<BleDeviceListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String item);
    }


    private List<String> bleDeviceList;
    private final OnItemClickListener listener;
    public BleDeviceListAdapter(List<String> deviceList, OnItemClickListener listener) {
        this.bleDeviceList = deviceList;
        this.listener = listener ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bleListView = inflater.inflate(R.layout.ble_divice_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(bleListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = bleDeviceList.get(position);
        TextView textView = holder.deviceName;
        textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return bleDeviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView deviceName;
        public Button connectTo;
        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.device_neme);
            connectTo = (Button) itemView.findViewById(R.id.connect_to);
            connectTo.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            listener.onItemClick(deviceName.getText().toString());
        }
    }
}