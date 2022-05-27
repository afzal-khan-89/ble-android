package com.example.bletest.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bletest.R;

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
    public BleDeviceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bleListView = inflater.inflate(R.layout.ble_divice_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(bleListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BleDeviceListAdapter.ViewHolder holder, int position) {

        String item = bleDeviceList.get(position);

        TextView textView = holder.deviceName;
        textView.setText(item);
//        Button button = holder.connectTo;
//        button.setText(contact.isOnline() ? "Message" : "Offline");
//        button.setEnabled(contact.isOnline());
    }

    @Override
    public int getItemCount() {
        return bleDeviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deviceName;
        public Button connectTo;
        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.device_neme);
            //connectTo = (Button) itemView.findViewById(R.id.connet_device);
        }
//        public void bind(final String item, final OnItemClickListener listener) {
//            name.setText(item.name);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    listener.onItemClick(item);
//                }
//            });
//        }
    }
}