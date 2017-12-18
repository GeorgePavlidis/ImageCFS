package uom.gr.imagecfs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import uom.gr.imagecfs.data.ImageEntry;


public class StartFragment extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;


    FloatingActionButton fab_FromGallery;
    FloatingActionButton fab_FromCamera;
    FloatingActionButton fab;

    Animation FabOpen;
    Animation FabClose;
    Animation FabRotateC; //clockwise
    Animation FabRotateA; //anticlockwise

    ImageView imageView;
    TextView text;
    Boolean flag=false;
    Boolean isOpen = false;

    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imageView = (ImageView)findViewById(R.id.imageView);
        fab_FromGallery = (FloatingActionButton)findViewById(R.id.fab_gallery);
        fab_FromCamera = ( FloatingActionButton)findViewById(R.id.fab_camera);
        fab = (FloatingActionButton)findViewById((R.id.fabMain));

        text = (TextView)findViewById(R.id.textlol);
        text.setText("einai kuriaki kai ka8omai spt");

        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRotateC= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise); //clockwise
        FabRotateA = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise); //anticlockwise


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

    private void openCamera() {
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
                      imageView.setImageBitmap(bitmap);
                  }else{
                    Bitmap bitmap = scaleBitmapDown((Bitmap)data.getExtras().get("data"),1200);
                      callCloudVision(bitmap);
                      imageView.setImageBitmap(bitmap);
                  }

            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        text.setText("Wait, loading...");
        FetchResponseTask imageTask =new FetchResponseTask(StartFragment.this, imageUri);
        imageTask.execute(bitmap);
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
