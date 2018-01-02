package uom.gr.imagecfs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;

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
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.SafeSearchAnnotation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import uom.gr.imagecfs.data.ImageEntry;


class FetchResponseTask extends AsyncTask<Bitmap, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private final Context mContext;
    private final Uri imageUri;
    public static Bitmap bitmap;
    private final boolean onlyScan;
    private static final int PICK_IMAGE = 100;
    public static final String FILE_NAME = "temp.jpg";
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCRZqKBmSkyl_sGNQh2eM6uLX1ISVmyov0";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String TAG = FetchResponseTask.class.getSimpleName();

    public FetchResponseTask(Context context, Uri imageUri, boolean onlyScan) {
        this.onlyScan=onlyScan;
        mContext = context;
        this.imageUri = imageUri;
    }

            @Override
            protected String doInBackground(final Bitmap...params) {
                bitmap=params[0];
                Log.i("Scan Gallery", imageUri.toString());
                Cursor image = mContext.getContentResolver().query(ImageEntry.ImageTable.CONTENT_URI, null, ImageEntry.ImageTable.COLUMN_URI + "= '" + imageUri.toString() + "'", null, null);
                image.moveToFirst();
                Log.i("image",imageUri.toString());

                try {
                    if (image.getString(image.getColumnIndex(ImageEntry.ImageTable.COLUMN_URI)).equals(imageUri.toString())){
                        Log.e("test check","cool");
                        return "empty";
                    }

                }catch (Exception e){
                    Log.e("test check-try","cool");
                }

                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {


                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = mContext.getApplicationContext().getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(mContext.getApplicationContext().getPackageManager(), packageName);

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
                                feature.setMaxResults(50);
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

    @Override
    protected void onPostExecute( String result) {
        Cursor cursor = mContext.getContentResolver().query(ImageEntry.ImageTable.CONTENT_URI, null, ImageEntry.ImageTable.COLUMN_URI + "='" + imageUri.toString() + "'", null, null);
        cursor.moveToFirst();

        for(int i=0;i<cursor.getCount();i++) {
            if (cursor.getCount() > 0) {
                Log.e("test select",
                        String.valueOf(
                                " LABEL " +
                                cursor.getDouble(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_LABEL_ID))
                                + " FACE " +
                                cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_FACE_ID))
                                + " LOGO " +
                                cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_LOGO_ID))
                                + " TEXT " +
                                cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_TEXT_ID))
                                + " SAFE " +
                                cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_SAFE_ID))
                        ));
            }
        }
            cursor.moveToNext();
        if(!onlyScan){
            Intent lol = new Intent( mContext, DetailsActivity.class);
            lol.putExtra("image",imageUri.toString());
            //lol.putExtra("bitmap",bitmap);
            mContext.startActivity(lol);
            Log.e(TAG, "The Result is " + result);
        }


    }







    @Nullable
    private String insertResponseToDb(BatchAnnotateImagesResponse response) {
        String message = "I found these things:\n\n";


        ContentValues imageValues = new ContentValues();
        imageValues.put(ImageEntry.ImageTable.COLUMN_URI, imageUri.toString());
        imageValues.put(ImageEntry.ImageTable.COLUMN_DATE, Calendar.getInstance().getTime().toString());





        // convert the labels to string
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();

        if (labels != null) {

            imageValues.put(ImageEntry.ImageTable.COLUMN_LABEL_ID, 1);

            for (EntityAnnotation label : labels) {
                ContentValues values = new ContentValues();
                values.put(ImageEntry.LabelTable.COLUMN_SCORE, label.getScore());
                values.put(ImageEntry.LabelTable.COLUMN_DESCRIPTION, label.getDescription());
                values.put(ImageEntry.LabelTable.COLUMN_ID, imageUri.toString());


                mContext.getContentResolver().insert(ImageEntry.LabelTable.CONTENT_URI,values);
            }
        }


        // convert the face to string
        List<FaceAnnotation> face = response.getResponses().get(0).getFaceAnnotations();
        if (face != null) {
            imageValues.put(ImageEntry.ImageTable.COLUMN_FACE_ID, 1);
            for (FaceAnnotation label : face) {
                ContentValues values = new ContentValues();
                values.put(ImageEntry.FaceTable.COLUMN_ID,imageUri.toString());
                values.put(ImageEntry.FaceTable.COLUMN_ANGER_LIKELIHOOD,label.getAngerLikelihood());
                values.put(ImageEntry.FaceTable.COLUMN_BLURRED_LIKELIHOOD,label.getBlurredLikelihood());
                values.put(ImageEntry.FaceTable.COLUMN_JOY_LIKELIHOOD,label.getJoyLikelihood());
                values.put(ImageEntry.FaceTable.COLUMN_SORROW_LIKELIHOOD,label.getSorrowLikelihood());
                values.put(ImageEntry.FaceTable.COLUMN_SURPRISE_LIKELIHOOD,label.getSurpriseLikelihood());
                values.put(ImageEntry.FaceTable.COLUMN_HEADWEAR_LIKELIHOOD,label.getHeadwearLikelihood());


                mContext.getContentResolver().insert(ImageEntry.FaceTable.CONTENT_URI,values);


            }
        }


        // convert the logo to string
        List<EntityAnnotation> logos = response.getResponses().get(0).getLogoAnnotations();
        if (logos != null) {
            imageValues.put(ImageEntry.ImageTable.COLUMN_LOGO_ID, 1);


            ContentValues values = new ContentValues();

            values.put(ImageEntry.LogosTable.COLUMN_ID,imageUri.toString());
            values.put(ImageEntry.LogosTable.COLUMN_SCORE,logos.get(0).getScore());
            values.put(ImageEntry.LogosTable.COLUMN_DESCRIPTION,logos.get(0).getDescription());

            mContext.getContentResolver().insert(ImageEntry.LogosTable.CONTENT_URI,values);

        }



        // convert the text to string
        List<EntityAnnotation> text = response.getResponses().get(0).getTextAnnotations();
        if (text != null) {
            imageValues.put(ImageEntry.ImageTable.COLUMN_TEXT_ID, 1);

            for (EntityAnnotation label : text) {
                ContentValues values = new ContentValues();

                values.put(ImageEntry.TextTable.COLUMN_ID,imageUri.toString());
                values.put(ImageEntry.TextTable.COLUMN_DESCRIPTION,label.getDescription());

                mContext.getContentResolver().insert(ImageEntry.TextTable.CONTENT_URI,values);

            }
        }


        // convert the safe to string
        SafeSearchAnnotation safe = response.getResponses().get(0).getSafeSearchAnnotation();
        if (safe != null) {
            imageValues.put(ImageEntry.ImageTable.COLUMN_SAFE_ID, 1);

            ContentValues values = new ContentValues();

            values.put(ImageEntry.SafeTable.COLUMN_ID,imageUri.toString());
            values.put(ImageEntry.SafeTable.COLUMN_ADULT,safe.getAdult());
            values.put(ImageEntry.SafeTable.COLUMN_MEDICAL,safe.getMedical());
            values.put(ImageEntry.SafeTable.COLUMN_SPOOF,safe.getSpoof());
            values.put(ImageEntry.SafeTable.COLUMN_VIOLENCE,safe.getViolence());

            mContext.getContentResolver().insert(ImageEntry.SafeTable.CONTENT_URI,values);

        }

        Uri m = mContext.getContentResolver().insert(ImageEntry.ImageTable.CONTENT_URI, imageValues);
        Cursor cursor = mContext.getContentResolver().query(ImageEntry.LabelTable.CONTENT_URI, null, ImageEntry.LabelTable.COLUMN_ID+"= '"+imageUri.toString()+"'", null, null);


//        cursor.moveToFirst();
//        for(int i=0;i<cursor.getCount();i++) {
//            if (cursor.getCount() > 0) {
//                Log.e("test lololo",
//                        String.valueOf(cursor.getString(cursor.getColumnIndex(ImageEntry.SafeTable.COLUMN_ADULT))
//                                + " " +
//                                cursor.getString(cursor.getColumnIndex(ImageEntry.SafeTable.COLUMN_MEDICAL))
//                                + " " +
//                                cursor.getString(cursor.getColumnIndex(ImageEntry.SafeTable.COLUMN_SPOOF))
//                                + " " +
//                                cursor.getString(cursor.getColumnIndex(ImageEntry.SafeTable.COLUMN_VIOLENCE))));
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
        try {
            Log.e("test insert",m.getLastPathSegment());
        }catch (Exception e){
            Log.e("test insert", String.valueOf(m));
        }


        return message;
    }
        }