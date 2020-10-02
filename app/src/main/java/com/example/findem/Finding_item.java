package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Finding_item extends AppCompatActivity {

    String item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_item);

        item = getIntent().getStringExtra("ITEM_NAME");
        System.out.println("Oncreate method of Finding_Item got item name: " + item);
    }
}