package uom.gr.imagecfs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =123;

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique


        }



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



    private ArrayList<String> getImages(){
        ArrayList<String> imagesUri = new ArrayList<String>();
        // which image properties are we querying
        String[] projection = new String[] {
                MediaStore.Images.Media._ID
        };

// content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

// Make the query.
        Cursor cur = managedQuery(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.Images.Media.DATE_TAKEN+" DESC"       // Ordering
        );

        if (cur.moveToFirst()) {

            String dataUri;


            do {
                // Get the field values
                dataUri ="content://media/external/images/media/"+ cur.getString(cur.getColumnIndex(
                        MediaStore.Images.Media._ID));
                imagesUri.add(dataUri);
                // Do something with the values.
                Log.i("ListingImages",
                         "  date_taken=" + dataUri);
            } while (cur.moveToNext());

        }
        return imagesUri;
    }
}
