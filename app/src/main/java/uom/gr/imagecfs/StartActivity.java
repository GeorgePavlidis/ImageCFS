package uom.gr.imagecfs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.ArrayList;

import uom.gr.imagecfs.data.ImageEntry;


public class StartActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;


    FloatingActionButton fab_FromGallery;
    FloatingActionButton fab_FromCamera;
    FloatingActionButton fab;

    Animation FabOpen;
    Animation FabClose;
    Animation FabRotateC; //clockwise
    Animation FabRotateA; //anticlockwise
    AnimationDrawable loading_anim;

    ImageView imageView;
    ImageView background;
    TextView text;
    Boolean flag=false;
    Boolean isOpen = false;

    MaterialSearchView searchView;
    Toolbar toolbar;

    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imageView = (ImageView)findViewById(R.id.imageView);
        fab_FromGallery = (FloatingActionButton)findViewById(R.id.fab_gallery);
        fab_FromCamera = ( FloatingActionButton)findViewById(R.id.fab_camera);
        fab = (FloatingActionButton)findViewById((R.id.fabMain));


        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRotateC= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise); //clockwise
        FabRotateA = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise); //anticlockwise


        text = (TextView)findViewById(R.id.textlol);
        text.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAction(isOpen);

            }
        });

        fab_FromGallery.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        fab_FromCamera.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openCamera();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setEllipsize(true);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);

        searchView.setSuggestions(getAllLabels());
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.startFragment), "Query: " + query, Snackbar.LENGTH_LONG)
                        .show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }


    public void loadingAction(){
        if(imageView==null) throw  new AssertionError();
        imageView.setBackgroundResource(R.drawable.loading_animatrion);

        loading_anim = (AnimationDrawable) imageView.getBackground();
        loading_anim.start();
    }

    private void fabAction(Boolean isopen){

        if(isopen){
            fab_FromCamera.startAnimation(FabClose);
            fab_FromGallery.startAnimation(FabClose);
            fab.startAnimation(FabRotateA);
            fab_FromGallery.setClickable(false );
            fab_FromCamera.setClickable(false);
            isOpen=false;

        }
        else
        {
            fab_FromCamera.startAnimation(FabOpen);
            fab_FromGallery.startAnimation(FabOpen);
            fab.startAnimation(FabRotateC);
            fab_FromGallery.setClickable(true);
            fab_FromCamera.setClickable(true);
            isOpen=true;
        }
    }

    public void openCamera() {
        fabAction(true);// close the buttons
        flag=false;
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, PICK_IMAGE);

    }

    private void openGallery() {
        fabAction(true);// close the buttons
        flag=true;
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);


        if (resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {

                  if(flag){
                     Bitmap bitmap = scaleBitmapDown (MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri), 1200);
                      callCloudVision(bitmap);
                  }else{
                    Bitmap bitmap = scaleBitmapDown((Bitmap)data.getExtras().get("data"),1200);
                      callCloudVision(bitmap);
                  }

            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        fab.setClickable(false);
        fab.startAnimation(FabClose);
        text.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        loadingAction();
        FetchResponseTask imageTask =new FetchResponseTask(StartActivity.this, imageUri,false);
        imageTask.execute(bitmap);

    }


    @Override
    protected void onPause()
    {

        text.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        if(loading_anim!=null && loading_anim.isRunning())    loading_anim.stop();

        super.onPause();



    }

    @Override
    protected void onResume()
    {

        if (!(loading_anim!=null && loading_anim.isRunning())) {
           fab.setClickable(true);
           fab.startAnimation(FabOpen);
       }
        super.onResume();



    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void scan(final ArrayList<String> list) {
        final boolean[] run = {true};
       final ArrayList<String> saved = getSavedImages();

       final int max = 6;//list.size();



        final NotificationManager mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture scan")
                .setContentText("scan in progress")
                .setSmallIcon(R.drawable.scan);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("scan the gallery..");
        dialog.setMax(6);
        dialog.setProgress(0);
        dialog.setCancelable(false);






        final Thread t = new Thread() {
            @Override
            public void run() {
                mBuilder.setProgress(max, 0, false);
                mNotifyManager.notify(0, mBuilder.build());
                for (int i=0;i<max;i++) {
                    String uri = list.get(i);
                    Bitmap bitmap = null;
                    if(saved.contains(uri)){
                        saved.remove(uri);
                        mBuilder.setProgress(max, i, false);
                        dialog.setProgress(i);
                        continue;
                    }
                    try {
                        bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri)), 1200);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FetchResponseTask imageTask = new FetchResponseTask(StartActivity.this, Uri.parse(uri), true);

                    AsyncTask<Bitmap, Void, String> hm = imageTask.execute(bitmap);
                    while (true) {
                        Log.i("Scan ", String.valueOf(hm.getStatus())+" "+i);

                        if(hm.getStatus() == AsyncTask.Status.FINISHED  ||  !run[0]){
                            mBuilder.setProgress(max, i+1, false);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(0, mBuilder.build());
                            dialog.setProgress(i+1);
                            break;
                        }
                    }
                    if(!run[0]){
                        break;
                    }
                }

                if(saved.size()>0){
                    for(String uri: saved){
                        getContentResolver().delete(ImageEntry.ImageTable.CONTENT_URI, uri,null);
                    }
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                // When the loop is finished, updates the notification
                mBuilder.setContentText("Scan complete")
                        // Removes the progress bar
                        .setProgress(0,0,false);
                mNotifyManager.notify(0, mBuilder.build());
            }
        };
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // When the loop is finished, updates the notification
                mBuilder.setContentText("Scan stoped")
                        // Removes the progress bar
                        .setProgress(0,0,false);
                mNotifyManager.notify(0, mBuilder.build());
                dialog.dismiss();
                run[0] =false;

            }
        });
        dialog.show();

        t.start();


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
//                    Log.i("ListingImages",
//                            "  date_taken=" + dataUri);
                } while (cur.moveToNext());

            }
            return imagesUri;
        }

    public  ArrayList<String> getSavedImages (){
        ArrayList<String> myImages =new ArrayList<>();
        Cursor cursor = getContentResolver().query(ImageEntry.ImageTable.CONTENT_URI, new String[]{ImageEntry.ImageTable.COLUMN_URI}, null, null, null);
        cursor.moveToFirst();

        for(int i=0;i<cursor.getCount();i++) {
            if (cursor.getCount() > 0) {
                myImages.add(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_URI)));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return myImages;
    }


    public String[] getAllLabels(){
        Cursor cursor = getContentResolver().query(ImageEntry.LabelTable.CONTENT_URI, new String[]{"DISTINCT "+ImageEntry.LabelTable.COLUMN_DESCRIPTION}, null, null, null);
        cursor.moveToFirst();

        String[] labels = new String[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++) {
            if (cursor.getCount() > 0) {
                labels[i] =cursor.getString(cursor.getColumnIndex(ImageEntry.LabelTable.COLUMN_DESCRIPTION));
                cursor.moveToNext();
            }
        }
        cursor.close();


      Log.i("Scan ", String.valueOf(labels));

        return labels;
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

        MenuItem item = menu.findItem(R.id.search2);
        searchView.setMenuItem(item);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent lol = new Intent(StartActivity.this,SettingsActivity.class);
                startActivity(lol);
                return true;
            case R.id.scan:
                new AlertDialog.Builder(this)
                        .setTitle("Are you using wifi ?")
                        .setMessage("make sure you are using wifi, scanning may cost several megabytes")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                scan(getImages());
                                Toast.makeText(StartActivity.this, "DONE!!!! XD" ,
                                        Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(R.string.no, null).show();

                return true;
            case R.id.search2:
//                Intent sca = new Intent(StartActivity.this,GalleryActivity.class);
//
//                startActivity(sca);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


        public static Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
