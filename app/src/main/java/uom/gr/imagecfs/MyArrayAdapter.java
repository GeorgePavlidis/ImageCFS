package uom.gr.imagecfs;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import uom.gr.imagecfs.data.ImageEntry;


public class MyArrayAdapter extends CursorAdapter {
    private String match;
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
//        switch (viewType) {
//            case VIEW_TYPE_TODAY: {
//                layoutId = R.layout.list_item_forecast_today;
//                break;
//            }
//            case VIEW_TYPE_FUTURE_DAY: {
//                layoutId = R.layout.list_item_forecast;
//                break;
//            }
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

    }


}