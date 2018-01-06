package uom.gr.imagecfs.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by George Pavlidis on 09-Dec-17.
 */

public class ImageProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ImageDbHelper mOpenHelper;


    //idk why i wrote this in my code, but all the others have it so maybe should i too
    static final int IMAGE = 100;
    static final int LABEL = 200;
    static final int FACE = 300;
    static final int LOGOS = 400;
    static final int TEXT = 500;
    static final int SAFE = 600;





    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH); // No_Match== -1
        final String authority = ImageEntry.CONTENT_AUTHORITY;

        matcher.addURI(authority, ImageEntry.ImageTable.TABLE_NAME_IMAGE,IMAGE);
        matcher.addURI(authority, ImageEntry.LabelTable.TABLE_NAME, LABEL);
        matcher.addURI(authority, ImageEntry.FaceTable.TABLE_NAME, FACE);
        matcher.addURI(authority, ImageEntry.LogosTable.TABLE_NAME, LOGOS);
        matcher.addURI(authority, ImageEntry.TextTable.TABLE_NAME, TEXT);
        matcher.addURI(authority, ImageEntry.SafeTable.TABLE_NAME, SAFE);


        return matcher;
    }



    @Override
    public boolean onCreate() {
        mOpenHelper = new ImageDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor = null;
        final int match = sUriMatcher.match(uri);

//       Cursor lol =  mOpenHelper.getReadableDatabase().rawQuery("SELECT "+ ImageEntry.ImageTable.COLUMN_URI+" FROM "+ ImageEntry.ImageTable.TABLE_NAME_IMAGE,null);
//        lol.moveToFirst();
//        for(int i=0;i<lol.getCount();i++) {
//            if (lol.getCount() > 0) {
//                Log.e("aaaxxxaa",lol.getString(lol.getColumnIndex(ImageEntry.ImageTable.COLUMN_URI)));;
//            }
//        }

        switch (match) {
            case IMAGE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ImageEntry.ImageTable.TABLE_NAME_IMAGE,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LABEL:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ImageEntry.LabelTable.TABLE_NAME,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FACE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ImageEntry.FaceTable.TABLE_NAME,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOGOS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ImageEntry.LogosTable.TABLE_NAME,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TEXT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ImageEntry.TextTable.TABLE_NAME,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SAFE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ImageEntry.SafeTable.TABLE_NAME,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case IMAGE:
                return ImageEntry.ImageTable.CONTENT_TYPE;
            case LABEL:
                return ImageEntry.LabelTable.CONTENT_TYPE;
            case FACE:
                return ImageEntry.FaceTable.CONTENT_TYPE;
            case LOGOS:
                return ImageEntry.LogosTable.CONTENT_TYPE;
            case TEXT:
                return ImageEntry.TextTable.CONTENT_TYPE;
            case SAFE:
                return ImageEntry.SafeTable.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db =mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri =null;

        switch (match) {
            case IMAGE: {
                long _id = db.insert(ImageEntry.ImageTable.TABLE_NAME_IMAGE, null, contentValues);
                if (_id > 0)
                    returnUri = ImageEntry.ImageTable.buildTableUri(_id);
                else
                    Log.i("Not insert","Failed to insert row into " + uri +"\n"+ contentValues);
                    //throw new android.database.SQLException("Failed to insert row into " + uri +"\n"+ contentValues);
                break;
            }
            case LABEL: {
                long _id = db.insert(ImageEntry.LabelTable.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = ImageEntry.LabelTable.buildTableUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FACE:{
                long _id = db.insert(ImageEntry.FaceTable.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = ImageEntry.FaceTable.buildTableUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LOGOS:{
                long _id = db.insert(ImageEntry.LogosTable.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = ImageEntry.LogosTable.buildTableUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TEXT:{
                long _id = db.insert(ImageEntry.TextTable.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = ImageEntry.TextTable.buildTableUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SAFE:{
                long _id = db.insert(ImageEntry.SafeTable.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = ImageEntry.SafeTable.buildTableUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        Cursor lol =  mOpenHelper.getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM "+ ImageEntry.ImageTable.TABLE_NAME_IMAGE,null);


        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String image_id, @Nullable String[] strings) {
        Uri aha = ImageEntry.ImageTable.CONTENT_URI;
        if(!uri.equals(ImageEntry.ImageTable.CONTENT_URI)){
            reset();
        }else{
            final SQLiteDatabase sqLiteDatabase =mOpenHelper.getWritableDatabase();
            sqLiteDatabase.delete(ImageEntry.ImageTable.TABLE_NAME_IMAGE,ImageEntry.ImageTable.COLUMN_URI+ "='" +image_id +"';",null);
            sqLiteDatabase.delete(ImageEntry.LabelTable.TABLE_NAME,ImageEntry.LabelTable.COLUMN_ID+ "='" + image_id+"';",null);
            sqLiteDatabase.delete(ImageEntry.FaceTable.TABLE_NAME,ImageEntry.FaceTable.COLUMN_ID+ "='" + image_id+"';",null);
            sqLiteDatabase.delete(ImageEntry.LogosTable.TABLE_NAME,ImageEntry.LogosTable.COLUMN_ID+ "='" + image_id+"';",null);
            sqLiteDatabase.delete(ImageEntry.TextTable.TABLE_NAME,ImageEntry.TextTable.COLUMN_ID+ "='" + image_id+"';",null);
            sqLiteDatabase.delete(ImageEntry.SafeTable.TABLE_NAME,ImageEntry.SafeTable.COLUMN_ID+ "='" + image_id+"';",null);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    public boolean reset(){
        final SQLiteDatabase sqLiteDatabase =mOpenHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + ImageEntry.ImageTable.TABLE_NAME_IMAGE);
        sqLiteDatabase.execSQL("DELETE FROM " + ImageEntry.LabelTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ImageEntry.LogosTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ImageEntry.TextTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ImageEntry.SafeTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + ImageEntry.FaceTable.TABLE_NAME);
        return true;
    }

}
