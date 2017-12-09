package uom.gr.imagecfs;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uom.gr.imagecfs.data.ImageDbHelper;
import uom.gr.imagecfs.data.ImageEntry;


public class StartActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    public static final String FILE_NAME = "temp.jpg";
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCRZqKBmSkyl_sGNQh2eM6uLX1ISVmyov0";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String TAG = MainActivity.class.getSimpleName();


    FloatingActionButton fab_FromGallery;
    FloatingActionButton fab_FromCamera;
    FloatingActionButton fab;

    Animation FabOpen;
    Animation FabClose;
    Animation FabRotateC; //clockwise
    Animation FabRotateA; //anticlockwise

    ImageView imageView;
    TextView text;
    Boolean flag;
    Boolean isOpen = false;

    private Uri imageUri;
    private  Uri oldUri;

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lol = new Intent(StartActivity.this, DetailsActivity.class);
                startActivity(lol);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAction(isOpen);
                Uri lol = ImageEntry.ImageTable.buildTableUri(5);
                Uri lol1 = ImageEntry.LabelTable.buildTableUri(5);
                Uri lol2 = ImageEntry.TextTable.buildTableUri(5);
                Log.e("Uri test",lol.toString()+lol1.toString()+lol2.toString());
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
             //imageView.setImageURI(imageUri);
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
        Log.e(TAG, "Wait.... ");
        text.setText("Wait, loading...");


        // Do the real work in an async task, because we need to use the network anyway
         new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {
                            {
                                Feature feature = new Feature();
                                feature.setType("LABEL_DETECTION");
                                feature.setMaxResults(10);
                                add(feature);
                            }

                            {
                                Feature feature = new Feature();
                                feature.setType("FACE_DETECTION");
                                feature.setMaxResults(10);
                                add(feature);
                            }

                            {
                                Feature feature = new Feature();
                                feature.setType("LOGO_DETECTION");
                                feature.setMaxResults(10);
                                add(feature);
                            }

                            {
                                Feature feature = new Feature();
                                feature.setType("TEXT_DETECTION");
                                add(feature);
                            }

                            {
                                Feature feature = new Feature();
                                feature.setType("SAFE_SEARCH_DETECTION");
                                feature.setMaxResults(10);
                                add(feature);
                            }

                            {
                                Feature feature = new Feature();
                                feature.setType("WEB_DETECTION");
                                feature.setMaxResults(10);
                                add(feature);
                            }

                        });

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});
                    Log.e(TAG, batchAnnotateImagesRequest.toString());
                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);

                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return insertResponseToDb(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }


                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                Snackbar.make(imageView, "Done...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                text.setText(result);
                Log.e(TAG, "The Result is " + result);

            }
        }.execute();
    }



    private String insertResponseToDb(BatchAnnotateImagesResponse response) {
        String message = "I found these things:\n\n";
        ContentValues Values = new ContentValues();
        Values.put(ImageEntry.ImageTable.COLUMN_URI, imageUri.toString());
        Values.put(ImageEntry.ImageTable.COLUMN_DATE, Calendar.getInstance().getTime().toString());



        Log.e("dddddd", (String) Values.get(ImageEntry.ImageTable.COLUMN_DATE));
        oldUri = imageUri;
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message += String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription());
                message += "\n";
            }
        } else {
            message += "nothing";
        }
         ImageDbHelper lol =new ImageDbHelper(this);
        return message;
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

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
