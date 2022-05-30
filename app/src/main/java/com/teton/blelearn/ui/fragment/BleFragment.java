package com.teton.blelearn.ui.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teton.blelearn.R;
import com.teton.blelearn.model.BleDevice;
import com.teton.blelearn.ui.activity.BleCentral;
import com.teton.blelearn.ui.helper.adapter.BleDeviceListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BleFragment extends Fragment implements BleDeviceListAdapter.OnItemClickListener {

    private final String TAG = "BLE_TEST";


    private Map<String, BluetoothDevice> mScanResults;
    private Handler mHandler = new Handler();

    ListView bleDeviceListView ;
    List<BleDevice> bleDeviceList ;
    ArrayAdapter<String> bleListAdapter;
    BleDeviceListAdapter mBleListAdapter;


    BluetoothManager bluetoothManager ;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 60000;
    private ScanCallback leScanCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        bleDeviceList =  new ArrayList<>();

        View v = inflater.inflate(R.layout.fragment_ble_list, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.fragment_ble_list);
        mBleListAdapter = new BleDeviceListAdapter(bleDeviceList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mBleListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        leScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                String dName = result.getDevice().getName();
                String dAddress = result.getDevice().getAddress();
                if( null == dName || null == dAddress ){
                    return ;
                }
                for(BleDevice leDevice : bleDeviceList){
                    if (leDevice.getName().contains(dName)){
                        return;
                    }
                }
                BleDevice bleDevice = new BleDevice();
                bleDevice.setName(dName);
                bleDevice.setAddress(dAddress);
                bleDeviceList.add(bleDevice);
                mBleListAdapter.notifyDataSetChanged();
                Log.d(TAG, "Remote device name: " + dName);
                Log.d(TAG, "Remote address: " + dAddress);
                // ByteArrayToString(result.getScanRecord().getBytes());
                //printScanRecord(result.getScanRecord().getBytes());
            }
        };

        scanLeDevice();
    }
    private void scanLeDevice() {
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }
    @Override
    public void onItemClick(String item) {
        Log.d(TAG, "Item Clicked .."+ item);
        Intent intent = new Intent(getActivity(), BleCentral.class);
        intent.putExtra("sBleDevice", item);
        startActivity(intent);
    }
}
