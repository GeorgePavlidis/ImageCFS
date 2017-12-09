package uom.gr.imagecfs.data;

import android.content.Context;
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


                // foreign keys
                " FOREIGN KEY (" +  ImageEntry.ImageTable.COLUMN_LABEL_ID + ") REFERENCES " +
                ImageEntry.LabelTable.TABLE_NAME + "(" +ImageEntry.FaceTable.COLUMN_ID +") ," +

                 " FOREIGN KEY (" +  ImageEntry.ImageTable.COLUMN_FACE_ID + ") REFERENCES " +
                ImageEntry.FaceTable.TABLE_NAME + "(" +ImageEntry.FaceTable.COLUMN_ID +") ," +


                " FOREIGN KEY (" +  ImageEntry.ImageTable.COLUMN_TEXT_ID + ") REFERENCES " +
                ImageEntry.TextTable.TABLE_NAME + "(" +ImageEntry.FaceTable.COLUMN_ID +") ," +

                " FOREIGN KEY (" +  ImageEntry.ImageTable.COLUMN_LOGO_ID + ") REFERENCES " +
                ImageEntry.LogosTable.TABLE_NAME + "(" +ImageEntry.FaceTable.COLUMN_ID +") ," +


                " UNIQUE (" + ImageEntry.ImageTable.COLUMN_URI + ") ON CONFLICT REPLACE);";


        final String SQL_CREATE_LABEL_TABEL = "CREATE TABLE " + ImageEntry.LabelTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.LabelTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ImageEntry.LabelTable.COLUMN_SCORE + " REAL , " +

                ImageEntry.LabelTable.COLUMN_DESCRIPTION + " TEXT , ";


        final String SQL_CREATE_LOGO_TABEL = "CREATE TABLE " + ImageEntry.LogosTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.LogosTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ImageEntry.LogosTable.COLUMN_SCORE + " REAL , " +

                ImageEntry.LogosTable.COLUMN_DESCRIPTION + " TEXT , ";


        final String SQL_CREATE_TEXT_TABEL = "CREATE TABLE " + ImageEntry.TextTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.TextTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ImageEntry.TextTable.COLUMN_DESCRIPTION + " TEXT , ";


        final String SQL_CREATE_SAFE_TABEL = "CREATE TABLE " + ImageEntry.SafeTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.SafeTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ImageEntry.SafeTable.COLUMN_ADULT + " TEXT , " +

                ImageEntry.SafeTable.COLUMN_MEDICAL + " TEXT , " +

                ImageEntry.SafeTable.COLUMN_SPOOF + " TEXT , " +

                ImageEntry.SafeTable.COLUMN_VIOLENCE + " TEXT , ";

        final String SQL_CREATE_FACE_TABEL = "CREATE TABLE " + ImageEntry.FaceTable.TABLE_NAME+ " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ImageEntry.LogosTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ImageEntry.FaceTable.COLUMN_ANGER_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_BLURRED_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_JOY_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_SORROW_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_HEADWEAR_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_HEADWEAR_LIKELIHOOD + " TEXT , " +

                ImageEntry.FaceTable.COLUMN_SURPRISE_LIKELIHOOD + " TEXT , " ;



        Log.e("ffffffffffffff",SQL_CREATE_LABEL_TABEL+"\n"+SQL_CREATE_LOGO_TABEL+"\n"+SQL_CREATE_TEXT_TABEL+"\n"+SQL_CREATE_SAFE_TABEL);
        sqLiteDatabase.execSQL(SQL_CREATE_FACE_TABEL);
        sqLiteDatabase.execSQL(SQL_CREATE_LABEL_TABEL);
        sqLiteDatabase.execSQL(SQL_CREATE_LOGO_TABEL);
        sqLiteDatabase.execSQL(SQL_CREATE_TEXT_TABEL);
        sqLiteDatabase.execSQL(SQL_CREATE_SAFE_TABEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.ImageTable.TABLE_NAME_IMAGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.LabelTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.LogosTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.TextTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.SafeTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageEntry.FaceTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}


