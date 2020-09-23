package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Get the intent that started this activity
        Intent intent = getIntent();
    }

    public void add_new_item(View view){
        EditText define_itemName_textField = (EditText)  findViewById(R.id.define_itemName_textField);

        String name = define_itemName_textField.getText().toString();
        Item(name, image)
    }
}