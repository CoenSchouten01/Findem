package com.example.findem;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Finding_item extends AppCompatActivity {

    String item;
    public static final int REQUEST_ENABLE_BT = 1;
    public BluetoothAdapter bt_adapter;
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private static final UUID MY_UUID = UUID.fromString("2cf6c45d-2106-4004-b91b-17b3939969bd");
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> pairedDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_item);

        item = getIntent().getStringExtra("ITEM_NAME");

        TextView item_name_text = findViewById(R.id.finding_item);
        item_name_text.setText(item);

        bt_adapter = BluetoothAdapter.getDefaultAdapter();
        if (!bt_adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        pairedDevices = bt_adapter.getBondedDevices();

        pairedDev = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceMAC = device.getAddress();
                System.out.println(deviceName + "connected");
                pairedDev.add(device);
            }
            ConnectThread connectThread = new ConnectThread(pairedDev.get(0));
            connectThread.start();
            System.out.println("Paired devices: " + pairedDev.toString());
        }
    }

    public void test_bluetooth(View view) {
        if (bt_adapter.isDiscovering()) {
            bt_adapter.cancelDiscovery();
        }
        bt_adapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

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
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            System.out.println("socket versie 0: " + tmp);
            mmDevice = device;
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
            System.out.println("socket versie 1:" + mmSocket);
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bt_adapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                // Do something for Send/Receive

            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    System.out.println("Socket versie 2: " + mmSocket);
                    //mmSocket.close();
                    Method m = mmDevice.getClass().getMethod("createRfcommSocket",
                            new Class[] { int.class });
                    mmSocket = (BluetoothSocket)m.invoke(mmDevice, Integer.valueOf(1));
                    mmSocket.connect();
                    Log.d("ZeeTest", "++++ Connecting");
                    System.out.println("Regel 96, run! " + connectException);
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            // manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
                System.out.println("Regel 108, cancelled!");
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
}