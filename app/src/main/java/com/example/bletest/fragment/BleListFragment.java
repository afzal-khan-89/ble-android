package com.example.bletest.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bletest.R;
import com.example.bletest.ui.adapter.BleDeviceListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BleListFragment extends Fragment {

    private final String TAG = "BLE_TEST";
    private BluetoothAdapter mBluetoothAdapter;

    private boolean mScanning = false;
    private Map<String, BluetoothDevice> mScanResults;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler = new Handler();

    ListView bleDeviceListView ;
    List<String> bleDeviceList ;
    ArrayAdapter<String> bleListAdapter;
    BleDeviceListAdapter mBleListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bleDeviceList =  new ArrayList<>();
        bleDeviceList.add("ble dummy -1");
        bleDeviceList.add("ble dummy -2");

        View v = inflater.inflate(R.layout.fragment_ble_list, container, false);
//        bleDeviceListView = (ListView)v.findViewById(R.id.ble_devices_list_view);
//        bleListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, bleDeviceList);
//        bleDeviceListView.setAdapter(bleListAdapter) ;

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.fragment_ble_list);
        mBleListAdapter = new BleDeviceListAdapter(bleDeviceList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mBleListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            getActivity().finish();
        }
        Log.d(TAG, "APP STARTED ");

        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission fail ...");
        }
        mBluetoothAdapter.getBluetoothLeScanner().startScan(new ScanCallback() {

            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                String s = result.getDevice().getName();
                if( null ==s){

                    return ;
                }
                if(!bleDeviceList.contains(s)){
                    bleDeviceList.add(s);
                    mBleListAdapter.notifyDataSetChanged();
                }


                Log.i(TAG, "Remote device name: " + s);

               // ByteArrayToString(result.getScanRecord().getBytes());
                //printScanRecord(result.getScanRecord().getBytes());
            }
        });
    }
}