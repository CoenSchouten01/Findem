package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //This function navigates the user to the AddItem page when this button on the main page is clicked
    public void navigateAddItem(View view) {
        //Do something in response to the button
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }
    //create an addItem object to get the items ArrayList.
    AddItem addItem = new AddItem();
    addItem.items;
}