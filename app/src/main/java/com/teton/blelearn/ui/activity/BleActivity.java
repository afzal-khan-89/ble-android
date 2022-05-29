package com.teton.blelearn.ui.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.teton.blelearn.R;
import com.teton.blelearn.ui.fragment.BleFragment;

public class BleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        getSupportFragmentManager().beginTransaction().replace(R.id.ble_fragment, BleFragment.class, null).commit();
    }
}