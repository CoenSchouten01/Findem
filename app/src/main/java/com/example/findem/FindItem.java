package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

        ArrayList<String> list = read_from_file();
//        list.add("Potje");
//        list.add("Civ");
//        list.add("?");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

    }

    public ArrayList<String> read_from_file() {
        FileInputStream fis = null;
        ArrayList<String> output = new ArrayList<>();
        try {
            fis = openFileInput(MainActivity.FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text = br.readLine();
            while (text != null) {
                output.add(text);
                text = br.readLine();
//                sb.append(text).append("\n");
            }
//            output = sb.toString();
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
        System.out.println(output.toString());
        return output;
    }
}