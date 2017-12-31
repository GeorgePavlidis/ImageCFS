package uom.gr.imagecfs;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;

import java.util.ArrayList;

import uom.gr.imagecfs.data.ImageEntry;

public class DetailsActivity extends AppCompatActivity {



    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ShareActionProvider mShareActionProvider=null;
    FloatingActionButton fab_FromGallery;
    FloatingActionButton fab_FromCamera;
    FloatingActionButton fab;

    Animation FabOpen;
    Animation FabClose;
    Animation FabRotateC; //clockwise
    Animation FabRotateA; //anticlockwise
    Boolean isOpen = false;

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

        fab_FromGallery = (FloatingActionButton)findViewById(R.id.fab_gallery);
        fab_FromCamera = ( FloatingActionButton)findViewById(R.id.fab_camera);
        fab = (FloatingActionButton)findViewById((R.id.fabMain));


        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRotateC= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise); //clockwise
        FabRotateA = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise); //anticlockwise



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAction(isOpen);

            }
        });

//        fab_FromGallery.setOnClickListener( new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                openGallery();
//            }
//        });
//
//
//        fab_FromCamera.setOnClickListener( new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                openCamera();
//            }
//        });



        Bitmap bitmap =  FetchResponseTask.bitmap;
        ImageView imageView = findViewById(R.id.imageView_toolbar);
        imageView.setImageBitmap(bitmap);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onShareTargetSelected", "eimai o pio gamatos ston kosmo ");
                Intent lol = new Intent(DetailsActivity.this,ImageFullScreen.class);
                startActivity(lol);
            }
        });



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


    private void fabAction(Boolean isopen) {

        if (isopen) {
            fab_FromCamera.startAnimation(FabClose);
            fab_FromGallery.startAnimation(FabClose);
            fab.startAnimation(FabRotateA);
            fab_FromGallery.setClickable(false);
            fab_FromCamera.setClickable(false);
            isOpen = false;

        } else {
            fab_FromCamera.startAnimation(FabOpen);
            fab_FromGallery.startAnimation(FabOpen);
            fab.startAnimation(FabRotateC);
            fab_FromGallery.setClickable(true);
            fab_FromCamera.setClickable(true);
            isOpen = true;
        }

    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider =(ShareActionProvider)MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(createShareForecastIntent());

        return true;
    }

    // Call to update the share intent
    private Intent createShareForecastIntent() {
        final Intent shareIntent = new  Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "this is ta fucking awesome photo ");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse((String) getIntent().getSerializableExtra("image")));
        shareIntent.setType("*/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //startActivity(Intent.createChooser(shareIntent, "Share Via"));

//        mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
//            @Override
//            public boolean onShareTargetSelected(ShareActionProvider shareActionProvider, Intent intent) {
//                // String urlKey = mImageUrlList.get(mImageGallery.getCurrentItem()) + "\n";
//
//                startActivity(Intent.createChooser(shareIntent, "Share Via"));
//                // mShareActionProvider.setShareIntent(getImageIntent(app.getImageCache().get(urlKey)));
//                Log.e("onShareTargetSelected", "eimai o pio gamatos ston kosmo ");
//                return false;
//            }
//        });

        return shareIntent;
    }

}
