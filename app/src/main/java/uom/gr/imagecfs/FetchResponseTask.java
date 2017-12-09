package uom.gr.imagecfs;

import android.content.ContentValues;
import android.content.Context;
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

import uom.gr.imagecfs.data.ImageEntry;


class FetchResponseTask extends AsyncTask<Bitmap, Void, String> {

    private final Context mContext;
    private final Uri imageUri;
    private static final int PICK_IMAGE = 100;
    public static final String FILE_NAME = "temp.jpg";
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCRZqKBmSkyl_sGNQh2eM6uLX1ISVmyov0";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String TAG = MainActivity.class.getSimpleName();

    public FetchResponseTask(Context context, Uri imageUri) {
        mContext = context;
        this.imageUri = imageUri;
    }

            @Override
            protected String doInBackground(final Bitmap...params) {
                final Bitmap bitmap=params[0];

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







    @Nullable
    private String insertResponseToDb(BatchAnnotateImagesResponse response) {
        String message = "I found these things:\n\n";


        ContentValues Values = new ContentValues();
        Values.put(ImageEntry.ImageTable.COLUMN_URI, imageUri.toString());
        Values.put(ImageEntry.ImageTable.COLUMN_DATE, Calendar.getInstance().getTime().toString());
        Values.put(ImageEntry.ImageTable.COLUMN_LABEL_ID, (byte[]) null);
        Values.put(ImageEntry.ImageTable.COLUMN_FACE_ID, (byte[]) null);
        Values.put(ImageEntry.ImageTable.COLUMN_LANDMARK_ID, (byte[]) null);
        Values.put(ImageEntry.ImageTable.COLUMN_LOGO_ID, (byte[]) null);
        Values.put(ImageEntry.ImageTable.COLUMN_TYPE_ID, (byte[]) null);
        Values.put(ImageEntry.ImageTable.COLUMN_TEXT_ID, (byte[]) null);
        Values.put(ImageEntry.ImageTable.COLUMN_SAFE_ID, (byte[]) null);
        Values.put(ImageEntry.ImageTable.COLUMN_WEB_ID, (byte[]) null);

        Log.e("test insert",mContext.getContentResolver().insert(ImageEntry.ImageTable.CONTENT_URI,Values).getLastPathSegment());

//        Cursor cursor = this.getContentResolver().query(ImageEntry.ImageTable.CONTENT_URI, null, null, null, null);
//        String [] names = {""};
//        for(int i = 0; i < cursor.getCount(); i ++){
//            names[i] = cursor.getString(i);
//        }
//        Log.e("test select", names.toString());
        // convert the labels to string
//        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
//        if (labels != null) {
//            String id = this.getContentResolver().insert(ImageEntry.ImageTable.CONTENT_URI, Values).getLastPathSegment();
//            Values.put(ImageEntry.ImageTable.COLUMN_LABEL_ID, Integer.parseInt(id));
//
//            for (EntityAnnotation label : labels) {
//                message.get(0).get(0).add(String.valueOf(label.getScore()));
//                message.get(0).get(1).add(label.getDescription());
//            }
//        } else {
//            message.get(0).get(0).add("nothing");
//        }
//
//
//        // convert the face to string
//        List<FaceAnnotation> face = response.getResponses().get(0).getFaceAnnotations();
//        if (face != null) {
//            for (FaceAnnotation label : face) {
//                message.get(1).get(0).add(String.valueOf(label.getAngerLikelihood()));
//                message.get(1).get(1).add(String.valueOf(label.getBlurredLikelihood()));
//                message.get(1).get(2).add(String.valueOf(label.getJoyLikelihood()));
//                message.get(1).get(3).add(String.valueOf(label.getSorrowLikelihood()));
//                message.get(1).get(4).add(String.valueOf(label.getSurpriseLikelihood()));
//                message.get(1).get(5).add(String.valueOf(label.getHeadwearLikelihood()));
//
//
//
//            }
//        } else {
//            message.get(1).get(0).add("nothing");
//        }
//
//
//        // convert the logo to string
//        List<EntityAnnotation> logos = response.getResponses().get(0).getLogoAnnotations();
//        if (logos != null) {
//            for (EntityAnnotation label : logos) {
//                message.get(2).get(0).add(String.valueOf(label.getScore()));
//                message.get(2).get(0).add(String.valueOf(label.getDescription()));
//            }
//        } else {
//            message.get(2).get(0).add("nothing");
//        }
//
//
//
//        // convert the text to string
//        List<EntityAnnotation> text = response.getResponses().get(0).getTextAnnotations();
//        if (text != null) {
//            for (EntityAnnotation label : text) {
//                message.get(3).get(0).add(String.valueOf(label.getDescription()));
//            }
//        } else {
//            message.get(3).get(0).add("nothing");
//        }
//
//
//        // convert the safe to string
//        SafeSearchAnnotation safe = response.getResponses().get(0).getSafeSearchAnnotation();
//        if (safe != null) {
//            message.get(4).get(0).add(String.valueOf(safe.getAdult()));
//            message.get(4).get(1).add(String.valueOf(safe.getMedical()));
//            message.get(4).get(2).add(String.valueOf(safe.getSpoof()));
//            message.get(4).get(3).add(String.valueOf(safe.getViolence()));
//
//
//
//        } else {
//            message.get(4).get(0).add("nothing");
//        }
//        return message;
        return null;
    }
        }