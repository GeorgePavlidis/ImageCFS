package uom.gr.imagecfs;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import uom.gr.imagecfs.data.ImageEntry;


public class MyArrayAdapter extends CursorAdapter {
    private String match;
    private ListView mListView;
    private  MyArrayAdapter myArrayAdapter;
    static final String LABEL = ImageEntry.LabelTable.TABLE_NAME;
    static final String FACE = ImageEntry.FaceTable.TABLE_NAME;
    static final String LOGOS = ImageEntry.LogosTable.TABLE_NAME;
    static final String TEXT = ImageEntry.TextTable.TABLE_NAME;
    static final String SAFE = ImageEntry.SafeTable.TABLE_NAME;

    public static class ViewHolder{
        public static  TextView score_txt;
        public static ProgressBar progressBar;
        public static  TextView description_txt;

        public ViewHolder(View view){
            score_txt = view.findViewById(R.id.score_textview);
            description_txt = view.findViewById(R.id.description_textview);
            progressBar = view.findViewById(R.id.item_bar);
        }
    }




    public MyArrayAdapter(Context context, Cursor c,  int flags, String name) {
        super(context, c, flags);
        match = name;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // Choose the layout type
      //  int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
//
//        if(match== ImageEntry.FaceTable.TABLE_NAME || match== ImageEntry.SafeTable.TABLE_NAME || false){
//
//
//            layoutId= R.layout.fragment_item;
//
//            myArrayAdapter = new MyArrayAdapter(context,cursor,0, ImageEntry.SafeTable.TABLE_NAME);
//            View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
//
//
//        }else{
//            layoutId = R.layout.fragment_item_list;
//            View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
//            ViewHolder viewHolder =new ViewHolder(view);
//            view.setTag(viewHolder);
//        }


        layoutId = R.layout.fragment_item_list;

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        ViewHolder viewHolder =new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }




    @Override
    public void bindView(View view, Context context, Cursor cursor) {
       ViewHolder viewholder = (ViewHolder) view.getTag();
        switch (match) {
            case LABEL: {
                label_list(viewholder,cursor);
                break;
            }
            case FACE: {
                face_list(viewholder, cursor);
                break;
            }
            case LOGOS: {
                logos_list(viewholder, cursor);
                break;
            }
            case TEXT: {
                text_list(viewholder, cursor);
                break;
            }
            case SAFE: {
                safe_list(viewholder, cursor);
                break;
            }
        default:
            break;
        }


    }


    private void label_list(ViewHolder viewHolder ,Cursor cursor){
        // Read score from cursor
        Double scr = cursor.getDouble(cursor.getColumnIndex(ImageEntry.LabelTable.COLUMN_SCORE))*100;
        int score = scr.intValue();

        // set the score text
        viewHolder.score_txt.setText(  String.valueOf(score+"%"));

        //set the bar progress
        viewHolder.progressBar.setProgress(score);


        // Read Description from cursor
        String description = cursor.getString(cursor.getColumnIndex(ImageEntry.LabelTable.COLUMN_DESCRIPTION));

        // Find TextView and set formatted date on it
        viewHolder.description_txt.setText(description);
    }

    private void face_list(ViewHolder viewHolder , Cursor cursor){


        //set the bar progress

        // Read Description from cursor
        String description = cursor.getString(cursor.getColumnIndex("value"));

        int scr;
        if(description.equals("VERY_UNLIKELY") ) {
            scr=0;
        }else if(description.equals("UNLIKELY")) {
            scr=25;

        }else if(description.equals("POSSIBLE") ){
           scr=50;

        }else if(description.equals("LIKELY")) {
           scr=75;

        }else if(description.equals("VERY_LIKELY")) {
           scr=100;

        }else {
            scr=81;
        }
        viewHolder.progressBar.setProgress(scr);
        viewHolder.score_txt.setText(  String.valueOf(scr+"%"));

        // Find TextView and set formatted date on it
        viewHolder.description_txt.setText(description);
    }

    private void logos_list(ViewHolder viewHolder , Cursor cursor){  // Read score from cursor
        Double scr = cursor.getDouble(cursor.getColumnIndex(ImageEntry.LogosTable.COLUMN_SCORE))*100;
        int score = scr.intValue();

        // set the score text
        viewHolder.score_txt.setText(  String.valueOf(score+"%"));

        //set the bar progress
        viewHolder.progressBar.setProgress(score);

        // Read Description from cursor
        String description = cursor.getString(cursor.getColumnIndex(ImageEntry.LogosTable.COLUMN_DESCRIPTION));

        // Find TextView and set formatted date on it
        viewHolder.description_txt.setText(description);
    }

    private void text_list(ViewHolder viewHolder , Cursor cursor){

        // Read Description from cursor
        String description = cursor.getString(cursor.getColumnIndex(ImageEntry.LabelTable.COLUMN_DESCRIPTION));
        Log.e("text.COLUMN_DESCRIPTION", description);
        // Find TextView and set formatted date on it
        viewHolder.description_txt.setText(description);
    }

    private void safe_list(ViewHolder viewHolder , Cursor cursor){
        // set the score text
        viewHolder.score_txt.setText( cursor.getString(cursor.getColumnIndex("name")));
        //set the bar progress

        // Read Description from cursor
        String description = cursor.getString(cursor.getColumnIndex("value"));


        if(description.equals("VERY_UNLIKELY") ) {
            viewHolder.progressBar.setProgress(0);

        }else if(description.equals("UNLIKELY")) {
            viewHolder.progressBar.setProgress(25);

        }else if(description.equals("POSSIBLE") ){
            viewHolder.progressBar.setProgress(50);

        }else if(description.equals("LIKELY")) {
            viewHolder.progressBar.setProgress(75);

        }else if(description.equals("VERY_LIKELY")) {
            viewHolder.progressBar.setProgress(100);

        }else {
            viewHolder.progressBar.setProgress(81);

        }
        // Find TextView and set formatted date on it
        viewHolder.description_txt.setText(description);
    }


    public static Cursor getSafeCursor(Cursor cursor){
        MatrixCursor c =  new MatrixCursor(new String[] {"_id","name", "value",});
        c.addRow(new Object[]{0,ImageEntry.SafeTable.COLUMN_ADULT ,cursor.getString(cursor.getColumnIndex(ImageEntry.SafeTable.COLUMN_ADULT ))});
        c.addRow(new Object[]{1,ImageEntry.SafeTable.COLUMN_VIOLENCE ,cursor.getString(cursor.getColumnIndex(ImageEntry.SafeTable.COLUMN_VIOLENCE))});
        c.addRow(new Object[]{2,ImageEntry.SafeTable.COLUMN_MEDICAL ,cursor.getString(cursor.getColumnIndex(ImageEntry.SafeTable.COLUMN_MEDICAL))});
        c.addRow(new Object[]{3,ImageEntry.SafeTable.COLUMN_SPOOF ,cursor.getString(cursor.getColumnIndex(ImageEntry.SafeTable.COLUMN_SPOOF))});

        return c;
    }


    public static Cursor getFaceCursor(Cursor cursor){
        MatrixCursor c =  new MatrixCursor(new String[] {"_id","name", "value",});
        c.addRow(new Object[]{0, "ANGER" , cursor.getString(cursor.getColumnIndex(ImageEntry.FaceTable.COLUMN_ANGER_LIKELIHOOD))});
        c.addRow(new Object[]{1, "BLURRED"  , cursor.getString(cursor.getColumnIndex(ImageEntry.FaceTable.COLUMN_BLURRED_LIKELIHOOD))});
        c.addRow(new Object[]{2, "JOY" ,cursor.getString(cursor.getColumnIndex(ImageEntry.FaceTable.COLUMN_JOY_LIKELIHOOD))});
        c.addRow(new Object[]{3,"SORROW" , cursor.getString(cursor.getColumnIndex(ImageEntry.FaceTable.COLUMN_SORROW_LIKELIHOOD))});
        c.addRow(new Object[]{4,"SURPRISE", cursor.getString(cursor.getColumnIndex(ImageEntry.FaceTable.COLUMN_SURPRISE_LIKELIHOOD))});
        c.addRow(new Object[]{5,"HEADWEAR"  , cursor.getString(cursor.getColumnIndex(ImageEntry.FaceTable.COLUMN_HEADWEAR_LIKELIHOOD))});



        return c;
    }



}