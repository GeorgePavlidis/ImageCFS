package uom.gr.imagecfs;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MyArrayAdapter extends CursorAdapter {


    private TextView  score_txt;
    private ProgressBar progressBar;
    private  TextView description_txt;

    public MyArrayAdapter(Context context, Cursor c) {
        super(context, c);
        score_txt = findViewById(R.id.score_textview);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}