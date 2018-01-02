package uom.gr.imagecfs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;


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




//        MaterialSearchView searchView = (MaterialSearchView) findViewById(R.id.se_gamaw);
//        toolbar = (Toolbar)findViewById(R.id.toolbar_start);
//        setSupportActionBar(toolbar);
//        searchView.setCursorDrawable(R.drawable.color_cursor_white);
//       // searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //Do some magic
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //Do some magic
//                return false;
//            }
//        });
//
//        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//                //Do some magic
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//                //Do some magic
//            }
//        });

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
        Log.e("WeatherJsonParser", String.valueOf(resultCode == RESULT_OK));


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
        FetchResponseTask imageTask =new FetchResponseTask(StartActivity.this, imageUri);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);

        MenuItem item = menu.findItem(R.id.search2);
       // searchView.setMenuItem(item);
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
                Intent sca = new Intent(StartActivity.this,GalleryActivity.class);
                startActivity(sca);
                return true;
            case R.id.search2:
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
