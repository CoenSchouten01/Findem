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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Finding_item extends AppCompatActivity {

    private String item;
    private String address;
    public static final int REQUEST_ENABLE_BT = 1;
    public BluetoothAdapter bt_adapter;
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private static final UUID MY_UUID = UUID.fromString("2cf6c45d-2106-4004-b91b-17b3939969bd");
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> pairedDev = new ArrayList<>();
    private Handler handler; // handler that gets info from Bluetooth service

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_item);



        item = getIntent().getStringExtra("ITEM_NAME");
        address = getIntent().getStringExtra("MAC_ADDRESS");

        System.out.println("The received address is: " + address);

        TextView item_name_text = findViewById(R.id.finding_item);
        item_name_text.setText(item);

        bt_adapter = BluetoothAdapter.getDefaultAdapter();
        if (!bt_adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        pairedDevices = bt_adapter.getBondedDevices();
        pairedDev = new ArrayList<>();
        pairedDev.addAll(pairedDevices);

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

//    public void find_the_item(View view){
//        //enable bluetooth if it is not yet enabled on the device
//        if (!bt_adapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
//        start_discovery();
//    }

    public void connect_the_item(View view){
        // Make a connection with the found device
        start_discovery();
        if (pairedDev.size() > 0) {
            Toast.makeText(this, "Found item: " + pairedDev.get(0).getAddress(),
                    Toast.LENGTH_LONG).show();
            for(BluetoothDevice device : pairedDev) {
                if(device.getAddress() == address) {
                    ConnectThread connectThread = new ConnectThread(device);
                    connectThread.start();
                }
            }
        } else {
            Toast.makeText(this, "Could not find any items",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void found_the_item(View view){
        //print 0 to tracer
        kill_tracer(mmSocket);
    }

    public void start_discovery() {
        if (bt_adapter.isDiscovering()) {
            bt_adapter.cancelDiscovery();
        }
        bt_adapter.startDiscovery();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                pairedDev.add(device);
                //pairedDevices.add(device);
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
        try {
            unregisterReceiver(receiver);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

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
            activate_tracer(mmSocket);
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

    public void activate_tracer(BluetoothSocket mmSocket){
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
        try {
            mmSocket.getOutputStream().write("1".toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void kill_tracer(BluetoothSocket mmSocket){
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
        try {
            mmSocket.getOutputStream().write("0".toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(
                            Finding_item.MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(
                        Finding_item.MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(Finding_item.MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}