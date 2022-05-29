package com.teton.blelearn.ui.activity;

import android.nfc.Tag;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.teton.blelearn.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity{

    private final String TAG = "SplashActivity";
    private final int REQUEST_ENABLE_BT = 111;
    private BluetoothAdapter mBluetoothAdapter;

    private boolean mScanning = false;
    private Map<String, BluetoothDevice> mScanResults;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler = new Handler();

    ListView bleDeviceListView ;
    List<String> bleDeviceList ;
    ArrayAdapter<String> bleListAdapter;
    ActivityResultLauncher<Intent> mActivityReasultLauncher ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityResultLauncher<Intent> mActivityReasultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.d(TAG, "BLE ACTIVATED ... ");
                            startActivity(new Intent(SplashActivity.this, BleActivity.class));
                            SplashActivity.this.finish();
                        }
                    }
                });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 2);

        }

        BluetoothManager bluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null){
                //Handle this issue. Report to the user that the device does not support BLE
        } else {
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if(mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivityReasultLauncher.launch(intent);
            }else{
                System.out.println("BLE on!");
                startActivity(new Intent(SplashActivity.this, BleActivity.class));
                this.finish();
            }
        }



  //      startActivity(new Intent(this, BleActivity.class));
    }

    @Override
    protected void onResume() {

        super.onResume();
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            finish();
//        }
//        Log.d(TAG, "APP STARTED ");
//
//        BluetoothManager bluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothAdapter = bluetoothManager.getAdapter();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//            Log.i(TAG, "Permission fail ...");
//        }
//        mBluetoothAdapter.getBluetoothLeScanner().startScan(new ScanCallback() {
//
//
//            @Override
//            public void onScanResult(int callbackType, ScanResult result) {
//                super.onScanResult(callbackType, result);
//                String s = result.getDevice().getName();
//                if( null ==s){
//
//                    return ;
//                }
//                if(!bleDeviceList.contains(s)){
//                    bleDeviceList.add(s);
//                    bleListAdapter.notifyDataSetChanged();
//                }
//
//
//                Log.i(TAG, "Remote device name: " + s);
//
//                ByteArrayToString(result.getScanRecord().getBytes());
//                //printScanRecord(result.getScanRecord().getBytes());
//            }
//        });
    }

//    public void printScanRecord (byte[] scanRecord) {
//
//        // Simply print all raw bytes
//        try {
//            String decodedRecord = new String(scanRecord,"UTF-8");
//            Log.d("DEBUG","decoded String : " + ByteArrayToString(scanRecord));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        // Parse data bytes into individual records
//        List<AdRecord> records = AdRecord.parseScanRecord(scanRecord);
//
//
//        // Print individual records
//        if (records.size() == 0) {
//            Log.i("DEBUG", "Scan Record Empty");
//        } else {
//            Log.i("DEBUG", "Scan Record: " + TextUtils.join(",", records));
//        }
//
//    }

    public  String ByteArrayToString(byte[] ba)
    {
//        String c = new String(ba, StandardCharsets.UTF_8);
        StringBuilder hex = new StringBuilder(ba.length * 2);

        for (byte b : ba)
            Log.i(TAG, "data: " + (char)b);
//            hex.append(c + " ");
        //       Log.i(TAG, "data: " +c);
        return hex.toString();
    }

//
//    public static class AdRecord {
//
//        public AdRecord(int length, int type, byte[] data) {
//            String decodedRecord = "";
//            try {
//                decodedRecord = new String(data,"UTF-8");
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            Log.d("DEBUG", "Length: " + length + " Type : " + type + " Data : " + ByteArrayToString(data));
//        }
//
//        public static List<AdRecord> parseScanRecord(byte[] scanRecord) {
//            List<AdRecord> records = new ArrayList<AdRecord>();
//
//            int index = 0;
//            while (index < scanRecord.length) {
//                int length = scanRecord[index++];
//                //Done once we run out of records
//                if (length == 0) break;
//
//                int type = scanRecord[index];
//                //Done if our record isn't a valid type
//                if (type == 0) break;
//
//                byte[] data = Arrays.copyOfRange(scanRecord, index+1, index+length);
//
//                records.add(new AdRecord(length, type, data));
//                //Advance
//                index += length;
//            }
//            return records;
//        }
//    }
}