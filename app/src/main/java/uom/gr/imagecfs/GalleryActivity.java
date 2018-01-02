package uom.gr.imagecfs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import uom.gr.imagecfs.data.ImageEntry;

public class GalleryActivity extends AppCompatActivity {
    ImageAdapter myImageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        //Log.e("test select",mThumbIds.get(position));
        GridView gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        //myImageAdapter.addArray(getImages());

        Cursor cursor = getContentResolver().query(ImageEntry.ImageTable.CONTENT_URI, new String[]{ImageEntry.ImageTable.COLUMN_URI}, null, null, null);
        cursor.moveToFirst();

        for(int i=0;i<cursor.getCount();i++) {
            if (cursor.getCount() > 0) {
                Log.e("aaaxxxaa",Uri.parse(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_URI)))+" getCount "+cursor.getCount() );
                myImageAdapter.add(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_URI)));
                cursor.moveToNext();
            }
        }
        cursor.close();




        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent lol = new Intent( GalleryActivity.this, DetailsActivity.class);
                lol.putExtra("image",myImageAdapter.getItem(position));
             //   lol.putExtra("bitmap",bitmap);
               startActivity(lol);
                Toast.makeText(GalleryActivity.this, "" + myImageAdapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



}
