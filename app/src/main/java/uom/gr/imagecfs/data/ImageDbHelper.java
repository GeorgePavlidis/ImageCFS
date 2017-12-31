package uom.gr.imagecfs.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ImageDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "images.db";

    public ImageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE " + ImageEntry.ImageTable.TABLE_NAME_IMAGE + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.ImageTable.COLUMN_URI + " TEXT PRIMARY KEY ," +
                // AUTOINCREMENT


                ImageEntry.ImageTable.COLUMN_DATE + " TEXT , " +
                ImageEntry.ImageTable.COLUMN_LABEL_ID + " INTEGER , " +
                ImageEntry.ImageTable.COLUMN_TEXT_ID + " INTEGER , " +
                ImageEntry.ImageTable.COLUMN_TYPE_ID + " INTEGER , " +
                ImageEntry.ImageTable.COLUMN_FACE_ID + " INTEGER , " +
                ImageEntry.ImageTable.COLUMN_WEB_ID + " INTEGER , " +
                ImageEntry.ImageTable.COLUMN_LANDMARK_ID + " INTEGER , " +
                ImageEntry.ImageTable.COLUMN_LOGO_ID + " INTEGER , " +
                ImageEntry.ImageTable.COLUMN_SAFE_ID + " INTEGER , " +


//                // foreign keys
//                " FOREIGN KEY (" +  ImageEntry.ImageTable.COLUMN_LABEL_ID + ") REFERENCES " +
//                ImageEntry.LabelTable.TABLE_NAME + "(" +ImageEntry.FaceTable.COLUMN_ID +") ," +
//
//                 " FOREIGN KEY (" +  ImageEntry.ImageTable.COLUMN_FACE_ID + ") REFERENCES " +
//                ImageEntry.FaceTable.TABLE_NAME + "(" +ImageEntry.FaceTable.COLUMN_ID +") ," +
//
//
//                " FOREIGN KEY (" +  ImageEntry.ImageTable.COLUMN_TEXT_ID + ") REFERENCES " +
//                ImageEntry.TextTable.TABLE_NAME + "(" +ImageEntry.FaceTable.COLUMN_ID +") ," +
//
//                " FOREIGN KEY (" +  ImageEntry.ImageTable.COLUMN_LOGO_ID + ") REFERENCES " +
//                ImageEntry.LogosTable.TABLE_NAME + "(" +ImageEntry.FaceTable.COLUMN_ID +") ," +


                " UNIQUE (" + ImageEntry.ImageTable.COLUMN_URI + ") ON CONFLICT ABORT);";


        final String SOL_CREATE_LABEL_TABLE = "CREATE TABLE " + ImageEntry.LabelTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.LabelTable._ID + " INT PRIMARY KEY, " +

                ImageEntry.LabelTable.COLUMN_ID + " TEXT ," +
                //AUTOINCREMENT
                ImageEntry.LabelTable.COLUMN_SCORE + " REAL , " +

                ImageEntry.LabelTable.COLUMN_DESCRIPTION + " TEXT  );";


        final String SOL_CREATE_LOGO_TABLE = "CREATE TABLE " + ImageEntry.LogosTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.LogosTable._ID + " INT PRIMARY KEY, " +

                ImageEntry.LogosTable.COLUMN_ID + " TEXT ," +
                //AUTOINCREMENT
                ImageEntry.LogosTable.COLUMN_SCORE + " REAL , " +

                ImageEntry.LogosTable.COLUMN_DESCRIPTION + " TEXT ); ";


        final String SOL_CREATE_TEXT_TABLE = "CREATE TABLE " + ImageEntry.TextTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.LabelTable._ID + " INT PRIMARY KEY, " +

                ImageEntry.TextTable.COLUMN_ID + " TEXT  , " +
                //AUTOINCREMENT
                ImageEntry.TextTable.COLUMN_DESCRIPTION + " TEXT  );";


        final String SOL_CREATE_SAFE_TABLE = "CREATE TABLE " + ImageEntry.SafeTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.SafeTable._ID + " INT PRIMARY KEY, " +

                ImageEntry.SafeTable.COLUMN_ID + " TEXT ," +
                //AUTOINCREMENT
                ImageEntry.SafeTable.COLUMN_ADULT + " TEXT , " +

                ImageEntry.SafeTable.COLUMN_MEDICAL + " TEXT , " +

                ImageEntry.SafeTable.COLUMN_SPOOF + " TEXT , " +

                ImageEntry.SafeTable.COLUMN_VIOLENCE + " TEXT );";

        final String SOL_CREATE_FACE_TABLE = "CREATE TABLE " + ImageEntry.FaceTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.FaceTable._ID + " INT PRIMARY KEY, " +

                ImageEntry.LogosTable.COLUMN_ID + " TEXT ," +
                //AUTOINCREMENT
                ImageEntry.FaceTable.COLUMN_ANGER_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_BLURRED_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_JOY_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_SORROW_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_SURPRISE_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_HEADWEAR_LIKELIHOOD + " TEXT ); ";



        Log.e("ffffffffffffff",SQL_CREATE_IMAGE_TABLE+"\n"+SOL_CREATE_LABEL_TABLE+"\n"+SOL_CREATE_LOGO_TABLE+"\n"+SOL_CREATE_TEXT_TABLE+"\n"+SOL_CREATE_SAFE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_IMAGE_TABLE);
        sqLiteDatabase.execSQL(SOL_CREATE_FACE_TABLE);
        sqLiteDatabase.execSQL(SOL_CREATE_LABEL_TABLE);
        sqLiteDatabase.execSQL(SOL_CREATE_LOGO_TABLE);
        sqLiteDatabase.execSQL(SOL_CREATE_TEXT_TABLE);
        sqLiteDatabase.execSQL(SOL_CREATE_SAFE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.e("onUpg","Failed to insert row into ");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.ImageTable.TABLE_NAME_IMAGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.LabelTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.LogosTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.TextTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.SafeTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.FaceTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }





}

