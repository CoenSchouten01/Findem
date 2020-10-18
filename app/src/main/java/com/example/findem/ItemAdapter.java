package com.example.findem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<String> {
    private ArrayList<String> item_names;
    private ArrayList<String> item_images;
    private Context context;

    public ItemAdapter(Context context, ArrayList<String> item_names, ArrayList<String> item_images) {
        super(context, R.layout.activity_find_item, item_names);
        this.item_names = item_names;
        this.item_images = item_images;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item_name = item_names.get(position);
        String item_image = item_images.get(position);

        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
           // LayoutInflater inflater = (LayoutInflater) context.getSystemService();
          //  LayoutInflater inflater = context.getLayoutInflater();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.activity_find_item, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        System.out.println("printje: " + item_image);



//        int targetW = viewHolder.imageview.getMaxWidth();
//        int targetH = viewHolder.imageview.getMaxHeight();

        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;

 //      BitmapFactory.decodeFile(item_image, bmOptions);
//
// //       int photoW = bmOptions.outWidth;
////        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//   //     int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//  //      bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        BitmapFactory.Options bounds = new BitmapFactory.Options();
//
//        BitmapFactory.decodeFile(item_image, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(item_image, opts);
//        ExifInterface exif = null;
//        try {
//            exif = new ExifInterface(item_image);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;
//
//        int rotationAngle = 0;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
//
//        Matrix matrix = new Matrix();
//        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, opts.outWidth, opts.outHeight, matrix, true);

        System.out.println(bitmap);



        viewHolder.imageview.setImageBitmap(bitmap);
        viewHolder.textview.setText(item_name);

        return view;
    }

    class ViewHolder {
        TextView textview;
        ImageView imageview;

        public ViewHolder(View view) {
            textview = (TextView) view.findViewById(R.id.text_view);
            imageview = (ImageView) view.findViewById(R.id.image);
        }
    }
}
