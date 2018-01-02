package uom.gr.imagecfs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import static uom.gr.imagecfs.StartActivity.scaleBitmapDown;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mThumbIds = new ArrayList<String>();

    public ImageAdapter(Context c) {

         mContext = c;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public String getItem(int position) {
        return mThumbIds.get(position);
    }

    void add(String uri){
        mThumbIds.add(uri);
    }

    void addArray(ArrayList<String> list){
        mThumbIds=list;
    }


    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(380, 380));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap bitmap=null;
        //decodeSampledBitmapFromUri( mThumbIds.get(position), 220, 220 );;
        try {
             bitmap = scaleBitmapDown (MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.parse(mThumbIds.get(position))), 400);
        } catch (IOException e) {
            e.printStackTrace();
        }




        imageView.setImageBitmap(bitmap);
        //imageView.setImageURI(Uri.parse(mThumbIds.get(position)));
        return imageView;
    }


    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {


        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }



}