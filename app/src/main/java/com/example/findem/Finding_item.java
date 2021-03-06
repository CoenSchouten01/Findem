package com.example.findem;
//test
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Finding_item extends AppCompatActivity {

    private String item;
    private String address = null;
    private String photopath;
    public static final int REQUEST_ENABLE_BT = 1;
    public BluetoothAdapter bt_adapter = null;
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private static final UUID MY_UUID = UUID.fromString("2cf6c45d-2106-4004-b91b-17b3939969bd");
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> pairedDev = new ArrayList<>();
    private ConnectedThread connectedThread = null;
    BluetoothSocket mmSocket = null;
    private ProgressDialog progress;
    private boolean isBtConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_item);

        item = getIntent().getStringExtra("ITEM_NAME");
        address = getIntent().getStringExtra("MAC_ADDRESS");
        photopath = getIntent().getStringExtra("PHOTOPATH");

        System.out.println("The received address is: " + address);

        TextView item_name_text = findViewById(R.id.finding_item);
        item_name_text.setText("Currently looking for: " + item + "\n" + "With address: " + address);

        ImageView imageView = findViewById(R.id.image_view);
        System.out.println("Photopath pre if: " + photopath);
        if (!photopath.equals("....")) {
            System.out.println("Photopath: " + photopath);
            try {
                setPic(imageView, photopath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Imageview W: " + imageView.getMaxWidth());
        System.out.println("Imageview H: " + imageView.getMaxHeight());

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
        new ConnectedThread().execute();
    }


    public void connect_the_item(View view) {
        activate_tracer();
    }

    private void setPic(ImageView imageView, String currentPhotoPath) throws IOException {
        // Get the dimensions of the View
        int targetW = imageView.getMaxWidth();
        int targetH = imageView.getMaxHeight();

        System.out.println("targetW: " + targetW + " TargetH: " + targetH);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;


        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, opts);
        ExifInterface exif = new ExifInterface(currentPhotoPath);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        imageView.setImageBitmap(bitmap);
    }

    public void found_the_item(View view) {
        //print 0 to tracer
        kill_tracer();
    }

    public void kill_tracer() {
        try {
            if (mmSocket != null) {
                mmSocket.getOutputStream().write("0".toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void start_tracer() {
        activate_tracer();
    }

    public void activate_tracer() {
        try {
            if (mmSocket != null) {
                mmSocket.getOutputStream().write("1".toString().getBytes());
                System.out.println("Wrote a 1 to outputt stream");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ConnectedThread extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Finding_item.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (mmSocket == null || !isBtConnected) {
                    System.out.println("Regel 332");
                    BluetoothDevice dispositivo = null;//connects to the device's address and checks if it's available
                    for(BluetoothDevice device: pairedDev){
                        if(device.getAddress().equals(address)){
                            dispositivo = device;
                            System.out.println("Found a device with a correct address.");
                            break;
                        }
                    }
                    mmSocket = dispositivo.createRfcommSocketToServiceRecord(MY_UUID);//create a RFCOMM (SPP) connection
                    bt_adapter.cancelDiscovery();
                    try {
                        mmSocket.connect();//start connection
                    } catch (IOException e) {
                        try {
                            mmSocket =(BluetoothSocket) dispositivo.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(dispositivo,1);
                        } catch (IllegalAccessException illegalAccessException) {
                            illegalAccessException.printStackTrace();
                        } catch (InvocationTargetException invocationTargetException) {
                            invocationTargetException.printStackTrace();
                        }
                        mmSocket.connect();
                    }
                }
            } catch (IOException | NoSuchMethodException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                e.printStackTrace();
                System.out.println("Regel 340");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                finish();
            } else {
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}

