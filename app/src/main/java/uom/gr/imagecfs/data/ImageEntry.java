package uom.gr.imagecfs.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ImageEntry {

    public static final String CONTENT_AUTHORITY = "uom.gr.imagecfs";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



    public static final class ImageTable implements BaseColumns {

        public static final String TABLE_NAME_IMAGE = "Image";



        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME_IMAGE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME_IMAGE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME_IMAGE;

        // the Uri and the key of image.
        public static final String COLUMN_URI = "Uri";

        public static final String COLUMN_DATE = "Date";

        public static final String COLUMN_LABEL_ID = "Label";

        public static final String COLUMN_FACE_ID = "Face";

        public static final String COLUMN_LANDMARK_ID = "Landmark";

        public static final String COLUMN_LOGO_ID = "Logo";

        public static final String COLUMN_TYPE_ID = "Type";

        public static final String COLUMN_TEXT_ID = "Text";

        public static final String COLUMN_SAFE_ID = "Safe";

        public static final String COLUMN_WEB_ID = "WEB";


        public static Uri buildTableUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }



    public static final class LabelTable implements BaseColumns {

        public static final String TABLE_NAME= "Label";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String COLUMN_ID = "ID";

        public static final String COLUMN_SCORE = "Score";

        public static final String COLUMN_DESCRIPTION = "Description";


        public static Uri buildTableUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }


    public static final class FaceTable implements BaseColumns {

        public static final String TABLE_NAME= "Face";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


        public static final String COLUMN_ID = "ID";

        public static final String COLUMN_ANGER_LIKELIHOOD = "AngerLikelihood";

        public static final String COLUMN_BLURRED_LIKELIHOOD = "BlurredLikelihood";

        public static final String COLUMN_JOY_LIKELIHOOD = "JoyLikelihood";

        public static final String COLUMN_SORROW_LIKELIHOOD = "SorrowLikelihood";

        public static final String COLUMN_SURPRISE_LIKELIHOOD = "SurpriseLikelihood";

        public static final String COLUMN_HEADWEAR_LIKELIHOOD = "HeadwearLikelihood";



        public static Uri buildTableUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


    public static final class LogosTable implements BaseColumns {

        public static final String TABLE_NAME= "Logo";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


        public static final String COLUMN_ID = "ID";

        public static final String COLUMN_SCORE = "Score";

        public static final String COLUMN_DESCRIPTION = "Description";


        public static Uri buildTableUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }


    public static final class TextTable implements BaseColumns {

        public static final String TABLE_NAME= "Text";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


        public static final String COLUMN_ID = "ID";

        public static final String COLUMN_DESCRIPTION = "Description";

        public static Uri buildTableUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class SafeTable implements BaseColumns {

        public static final String TABLE_NAME= "Safe";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String COLUMN_ID = "ID";

        public static final String COLUMN_ADULT = "Adult";

        public static final String COLUMN_MEDICAL= "Medical";

        public static final String COLUMN_SPOOF = "Spoof";

        public static final String COLUMN_VIOLENCE= "Violence";


        public static Uri buildTableUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}