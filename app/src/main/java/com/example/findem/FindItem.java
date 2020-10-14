package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FindItem extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_item);
        listView = (ListView) findViewById(R.id.list_view);

        ArrayList<String> items_list = read_from_file(MainActivity.FILE_NAME);
        final ArrayList<String> addresses_list = read_from_file(MainActivity.FILE_NAME_ADDRESS);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FindItem.this, Finding_item.class);
                String itemname = listView.getItemAtPosition((int)id).toString();
                String address = addresses_list.get((int)id);
                intent.putExtra("ITEM_NAME", itemname);
                intent.putExtra("MAC_ADDRESS", address);
                startActivity(intent);
            }
        });
    }

    private ArrayList<String> read_from_file(String filename) {
        FileInputStream fis = null;
        ArrayList<String> output = new ArrayList<>();
        try {
            fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text = br.readLine();
            while (text != null) {
                output.add(text);
                text = br.readLine();
            }
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
        return output;
    }

//    public ArrayList<String> read_items_from_file() {
//        FileInputStream fis = null;
//        ArrayList<String> output = new ArrayList<>();
//        try {
//            fis = openFileInput(MainActivity.FILE_NAME);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            String text = br.readLine();
//            while (text != null) {
//                output.add(text);
//                text = br.readLine();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return output;
//    }
}