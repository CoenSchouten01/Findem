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
    public void navigateAddItem(View view) {
        //Do something in response to the button
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }
}