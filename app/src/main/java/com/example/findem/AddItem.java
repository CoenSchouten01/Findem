package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class AddItem extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bt_adapter;
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private static final UUID MY_UUID = UUID.fromString("2cf6c45d-2106-4004-b91b-17b3939969bd");
    private BluetoothDevice btdevice;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        bt_adapter = BluetoothAdapter.getDefaultAdapter();
        //check if bluetooth is enabled on the device, if not, ask to enable it
        if (!bt_adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);


    }

    //This function gets executed when the add_item_button gets clicked
    public void add_new_item(View view) {
        // Make objects out of the textfields
        EditText item_name_textField = findViewById(R.id.define_itemName_textField);
        EditText MACAdress_textField = findViewById(R.id.define_MACAdress_textField);
        // Read info from text to corresponding files
        String item_name = item_name_textField.getText().toString();
        String MAC = MACAdress_textField.getText().toString();
        //if the item name and the mac address aren't empty, execute this code block
        if(item_name.length() != 0 && MAC.length() != 0) {
            //write name and address to file
            write_to_file(item_name, MainActivity.FILE_NAME);
            write_to_file(MAC, MainActivity.FILE_NAME_ADDRESS);
            address = MAC;
            //initial pairing to the tracker
            connect_the_item(view);
        } else {
            //Give some error message
            Toast.makeText(this, "Illegal item name or MAC address.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void connect_the_item(View view){
        //discover nearby bluetooth devices
        if (bt_adapter.isDiscovering()) {
            bt_adapter.cancelDiscovery();
        }
        bt_adapter.startDiscovery();
        // Make a connection with the found device

        ConnectThread connectThread = new ConnectThread(btdevice);
        connectThread.start();
//        if (pairedDev.size() > 0) {
//            Toast.makeText(this, "Found item: " + pairedDev.get(0).getAddress(),
//                    Toast.LENGTH_LONG).show();
//            for(BluetoothDevice device : pairedDev) {
//                if(device.getAddress() == address) {
//                    Finding_item.ConnectThread connectThread = new Finding_item.ConnectThread(device);
//                    connectThread.start();
//                }
//            }
//        } else {
//            Toast.makeText(this, "Could not find any items",
//                    Toast.LENGTH_LONG).show();
//        }
    }

//    public void test_bluetooth(View view) {
//        if (bt_adapter.isDiscovering()) {
//            bt_adapter.cancelDiscovery();
//        }
//        bt_adapter.startDiscovery();
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(receiver, filter);
//    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                System.out.println("Found bluetooth device: " + deviceName + " " + deviceHardwareAddress);
                if (deviceHardwareAddress.equals(address)) {
                    btdevice = device;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        try {
            unregisterReceiver(receiver);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    // Add text from define_itemName_textField to items.txt
    public void write_to_file(String item_name, String filename) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, MODE_APPEND);
            fos.write(item_name.getBytes());
            fos.write("\n".getBytes());
            Toast.makeText(this, "Added " + item_name + " to items",
                    Toast.LENGTH_LONG).show();
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

//    public void write_address_to_file(String address){
//        FileOutputStream fos = null;
//        try {
//            fos = openFileOutput(MainActivity.FILE_NAME_ADDRESS, MODE_APPEND);
//            fos.write(address.getBytes());
//            fos.write("\n".getBytes());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            mmSocket = null;
            mmDevice = device;
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            System.out.println("socket versie 1:" + mmSocket);
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bt_adapter.cancelDiscovery();
            if(mmSocket.isConnected()){
                return;
            }
            try {
                Method m = mmDevice.getClass().getMethod("createRfcommSocket",
                        new Class[] { int.class });
                mmSocket = (BluetoothSocket) m.invoke(mmDevice, Integer.valueOf(1));
                mmSocket.connect();
            } catch (IOException closeException) {
                Log.e(TAG, "hier moeten we eigenlijk niet naar kijken", closeException);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            //manage_connected_socket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                if(mmSocket.isConnected()) {
                    mmSocket.close();
                }
                System.out.println("Regel 108, cancelled!");
            } catch (IOException e) {
                Log.e(TAG, "REgel 177", e);
            }
        }
    }
}