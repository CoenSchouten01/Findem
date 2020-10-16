package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public BluetoothAdapter bt_adapter;
    public static final String FILE_NAME = "items.txt";
    public static final String FILE_NAME_ADDRESS = "bluetooth_address.txt";
    public static final String FILE_NAME_IMAGE = "images.txt";
    public static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_adapter = BluetoothAdapter.getDefaultAdapter();
        //check if bluetooth is enabled, if not, ask the user to enable it
        if (!bt_adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }



    //This function navigates the user to the AddItem page when this button on the main page is clicked
    public void navigateAddItem(View view) {
        //start the activity that corresponds to the button being clicked
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }
    //This function navigates the user to the FindItem page when this button on the main page is clicked
    public void navigateFindItem(View view) {
        //start the activity that corresponds to the button being clicked
        Intent intent = new Intent( this, FindItem.class);
        startActivity(intent);
    }
    //This function navigates the user to the Finding_item page when this button on the main page is clicked
    public void navigateDeleteItem(View view) {
        //start the activity that corresponds to the button being clicked
        Intent intent = new Intent( this, DeleteItem.class);
        startActivity(intent);
    }
}