package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
    }

    //This function gets executed when the add_item_button gets clicked
    public void add_new_item(View view) {
        // Make objects out of the textfields
        EditText item_name_textField = findViewById(R.id.define_itemName_textField);
        write_to_file(item_name_textField.getText().toString());
    }

    // Add text from define_itemName_textField to items.txt
    public void write_to_file(String item_name) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(MainActivity.FILE_NAME, MODE_APPEND);
            fos.write(item_name.getBytes());
            fos.write("\n".getBytes());
            Toast.makeText(this, "Added " + item_name + " to items",
                    Toast.LENGTH_LONG).show();
//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" +
//                    MainActivity.FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}