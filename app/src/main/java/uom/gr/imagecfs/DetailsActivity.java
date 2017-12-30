package uom.gr.imagecfs;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import uom.gr.imagecfs.data.ImageEntry;

public class DetailsActivity extends AppCompatActivity {



    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public DetailsActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(mViewPager, "Done...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });



        Bitmap bitmap =  FetchResponseTask.bitmap;
        ImageView imageView = findViewById(R.id.imageView_toolbar);
        imageView.setImageBitmap(bitmap);



        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                                                             Uri.parse((String) getIntent().getSerializableExtra("image")),getTitles());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));




    }


    protected ArrayList<String> getTitles(){
        Cursor cursor = getContentResolver().query(ImageEntry.ImageTable.CONTENT_URI, null, ImageEntry.ImageTable.COLUMN_URI + "='" +  getIntent().getSerializableExtra("image").toString() + "'", null, null);
        cursor.moveToFirst();
        ArrayList<String> title =new ArrayList<String>();
        if (cursor.getCount() > 0) {
            //LABEL
            if(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_LABEL_ID))!=null){
                title.add(ImageEntry.ImageTable.COLUMN_LABEL_ID);
            }
            //FACE
            if(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_FACE_ID))!=null){
                title.add(ImageEntry.ImageTable.COLUMN_FACE_ID);
            }
            //Landmark
            if(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_LANDMARK_ID))!=null){
                title.add(ImageEntry.ImageTable.COLUMN_LANDMARK_ID);
            }
            //LOGO
            if(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_LOGO_ID))!=null){
                title.add(ImageEntry.ImageTable.COLUMN_LOGO_ID);
            }
            //TYPE
            if(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_TYPE_ID))!=null){
                title.add(ImageEntry.ImageTable.COLUMN_TYPE_ID);
            }
            //TEXT
            if(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_TEXT_ID))!=null){
                title.add(ImageEntry.ImageTable.COLUMN_TEXT_ID);
            }
            //SAFE
            if(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_SAFE_ID))!=null){
                title.add(ImageEntry.ImageTable.COLUMN_SAFE_ID);
            }
            //WEB
            if(cursor.getString(cursor.getColumnIndex(ImageEntry.ImageTable.COLUMN_WEB_ID))!=null){
                title.add(ImageEntry.ImageTable.COLUMN_WEB_ID);
            }
        }
        cursor.close();
        Log.e("title", title.toString());
        Log.e("title", ImageEntry.SafeTable.TABLE_NAME);

        return title;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
