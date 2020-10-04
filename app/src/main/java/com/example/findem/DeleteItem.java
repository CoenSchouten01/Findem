package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DeleteItem extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);
        listView = (ListView) findViewById(R.id.list_view);

        list = read_from_file();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String itemname = listView.getItemAtPosition((int)id).toString();
//                delete_item(itemname);
                delete_item((int) id);

            }
        });
    }

    public void delete_item(int id) {
        
        File dir = getFilesDir();
        File file = new File(dir, MainActivity.FILE_NAME);
        file.delete();

        list.remove(id);

        write_to_file(list);

        Intent intent = new Intent(this, DeleteItem.class);
        startActivity(intent);
    }

    public ArrayList<String> read_from_file() {
        FileInputStream fis = null;
        ArrayList<String> output = new ArrayList<>();
        try {
            fis = openFileInput(MainActivity.FILE_NAME);
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

    public void write_to_file(ArrayList<String> list) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(MainActivity.FILE_NAME, MODE_APPEND);
            for (String item_name : list) {
                fos.write(item_name.getBytes());
                fos.write("\n".getBytes());
            }
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