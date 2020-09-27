package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static File items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new File("files/items.txt");
    }

    //This function navigates the user to the AddItem page when this button on the main page is clicked
    public void navigateAddItem(View view) {
        //Do something in response to the button
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }
    //This function navigates the user to the FindItem page when this button on the main page is clicked
    public void navigateFindItem(View view) {
        //Do something in response to the button
        Intent intent = new Intent( this, FindItem.class);
        startActivity(intent);
    }
}