package uom.gr.imagecfs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import uom.gr.imagecfs.data.ImageEntry;


public class MyArrayAdapter extends CursorAdapter {


    public MyArrayAdapter(Context context, Cursor c,  int flags) {
        super(context, c, flags);
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
        return LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Read score from cursor
        Double score = cursor.getDouble(cursor.getColumnIndex(ImageEntry.LabelTable.COLUMN_SCORE));
        // set the score text
        TextView score_txt = (TextView) view.findViewById(R.id.score_textview);
        score_txt.setText(  String.valueOf(score ));

        //set the bar progress
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.item_bar);
        progressBar.setProgress(score.intValue());


        // Read Description from cursor
        String description = cursor.getString(cursor.getColumnIndex(ImageEntry.LabelTable.COLUMN_DESCRIPTION));

        // Find TextView and set formatted date on it
        TextView description_txt = (TextView) view.findViewById(R.id.description_textview);
        description_txt.setText(description);


    }

}