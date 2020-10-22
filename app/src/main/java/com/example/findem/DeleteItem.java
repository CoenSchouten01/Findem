package com.example.findem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    ArrayList<String> items_list;
    ArrayList<String> addresses_list;
    ArrayList<String> photopaths_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);
        listView = (ListView) findViewById(R.id.list_view);

        items_list = read_from_file(MainActivity.FILE_NAME);
        addresses_list = read_from_file(MainActivity.FILE_NAME_ADDRESS);
        photopaths_list = read_from_file(MainActivity.FILE_NAME_IMAGE);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_layout, items_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                String itemname = listView.getItemAtPosition((int)id).toString();
                AlertDialog.Builder alert = new AlertDialog.Builder(DeleteItem.this);

                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete " + itemname + "?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_item((int) id);
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();




            }
        });
    }

    public void delete_item(int id) {
        File dir = getFilesDir();
        File item_file = new File(dir, MainActivity.FILE_NAME);
        File address_file = new File(dir, MainActivity.FILE_NAME_ADDRESS);
        File photopaths_file = new File(dir, MainActivity.FILE_NAME_IMAGE);
        item_file.delete();
        address_file.delete();
        photopaths_file.delete();

        items_list.remove(id);
        addresses_list.remove(id);
        photopaths_list.remove(id);

        write_to_file(items_list, MainActivity.FILE_NAME);
        write_to_file(addresses_list, MainActivity.FILE_NAME_ADDRESS);
        write_to_file(photopaths_list, MainActivity.FILE_NAME_IMAGE);


        Intent intent = new Intent(this, DeleteItem.class);
        startActivity(intent);
    }

    public ArrayList<String> read_from_file(String filename) {
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

    public void write_to_file(ArrayList<String> list, String filename) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, MODE_APPEND);
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