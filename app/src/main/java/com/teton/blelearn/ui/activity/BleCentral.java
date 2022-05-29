package com.teton.blelearn.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.teton.blelearn.R;
import com.teton.blelearn.service.BluetoothLeService;



public class BleCentral extends AppCompatActivity {
    private final String TAG = "BleCentral";
    private String bleDevicAddressToConnect = "";
    private BluetoothLeService bluetoothService;

    boolean connected = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bluetoothService = ((BluetoothLeService.LocalBinder) service).getService();
            if (bluetoothService != null) {
                if (!bluetoothService.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth");
                    finish();
                }
                // perform device connection
                //final boolean result = bluetoothService.connect(bleDevicAddressToConnect);
                if(bluetoothService.connect(bleDevicAddressToConnect)){
                    Log.d(TAG, "onServiceConnected CONNECTED ..");
                }else{
                    Log.d(TAG, "onServiceConnected CONNECTION FAIL  ..");
                }
                // bluetoothService.connect(bleDevicAddressToConnect);
            }else{
                Log.d(TAG, "bluetoothService NULL");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bluetoothService = null;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gatt_services_characteristics);
        Intent intent = getIntent();
        bleDevicAddressToConnect = intent.getStringExtra("sBleDevice");
        Log.d(TAG, "TO CONNECT :: "+bleDevicAddressToConnect );

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (bluetoothService != null) {
//            final boolean result = bluetoothService.connect(bleDevicAddressToConnect);
//            Log.d(TAG, "Connect request result=" + result);
//        }else{
//            Log.d(TAG, "onResume :: bluetoothService NULL");
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(gattUpdateReceiver);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        return intentFilter;
    }
    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                connected = true;
                Log.d(TAG, "CONNDCTED TO BLE DEVICE .. " + bleDevicAddressToConnect);
                //updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG, "FAIL TO CONNECT  DEVICE "+ bleDevicAddressToConnect);
                connected = false;
                //updateConnectionState(R.string.disconnected);
            }
        }
    };
}