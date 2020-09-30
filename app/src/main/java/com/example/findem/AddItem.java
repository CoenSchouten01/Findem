package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        EditText define_itemImage_textField = findViewById(R.id.define_itemImage_textField);
//        //Assign values to the strings
        String name = define_itemName_textField.getText().toString();
//        //construct an Item based on the name and image
//        Item new_item = new Item(name);
//        define_itemName_textField.setText(new_item.name + " " + "toegevoegd");
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(MainActivity.FILE_NAME, MODE_PRIVATE);
            fos.write(name.getBytes());
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" +
                            MainActivity.FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FileInputStream fis = null;
        try {
            fis = openFileInput(MainActivity.FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            define_itemImage_textField.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}