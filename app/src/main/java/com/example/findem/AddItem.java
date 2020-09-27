package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

public class AddItem extends AppCompatActivity {

//    public AddItem(){
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Get the intent that started this activity
//        Intent intent = getIntent();
    }
    //This function gets executed when the add_item_button gets clicked
    public void add_new_item(View view){
        //
        EditText define_itemName_textField = findViewById(R.id.define_itemName_textField);

        //Assign values to the strings
        String name = define_itemName_textField.getText().toString();
        //construct an Item based on the name and image
        Item new_item = new Item(name);
        define_itemName_textField.setText(new_item.name + " " + "toegevoegd");
    }
}