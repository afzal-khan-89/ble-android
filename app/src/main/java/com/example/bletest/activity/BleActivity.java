package com.example.bletest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bletest.R;
import com.example.bletest.fragment.BleListFragment;

public class BleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        getSupportFragmentManager().beginTransaction().replace(R.id.ble_fragment, BleListFragment.class, null).commit();
    }
}