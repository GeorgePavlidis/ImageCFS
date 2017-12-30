package uom.gr.imagecfs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;


import java.util.ArrayList;

import uom.gr.imagecfs.data.ImageEntry;


@SuppressLint("ValidFragment")
    class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private MyArrayAdapter  myArrayAdapter;
        private ListView mListView;
        private static Uri imageUri;
        String position;

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment getInstance(String sectionNumber,Uri img) {
            imageUri = img;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString("pos", sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            position = getArguments().getString("pos");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_item, container, false);

            return rootView;
        }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Cursor cursor;
        if(position==ImageEntry.LabelTable.TABLE_NAME) {
            cursor = getActivity().getContentResolver().query(ImageEntry.LabelTable.CONTENT_URI, null, ImageEntry.LabelTable.COLUMN_ID + "='" + imageUri.toString() + "'", null, ImageEntry.LabelTable.COLUMN_SCORE+" DESC");
            cursor.moveToFirst();
            myArrayAdapter = new MyArrayAdapter(getActivity(), cursor, 0, ImageEntry.LabelTable.TABLE_NAME);
        }
        if(position==ImageEntry.FaceTable.TABLE_NAME) {
            cursor = getActivity().getContentResolver().query(ImageEntry.FaceTable.CONTENT_URI, null, ImageEntry.FaceTable.COLUMN_ID+"='"+imageUri.toString()+"'", null, null);
            cursor.moveToFirst();
            myArrayAdapter = new MyArrayAdapter(getActivity(),cursor,0, ImageEntry.FaceTable.TABLE_NAME);
        }
        if(position==ImageEntry.LogosTable.TABLE_NAME) {
            cursor = getActivity().getContentResolver().query(ImageEntry.LogosTable.CONTENT_URI, null, ImageEntry.LogosTable.COLUMN_ID + "='" + imageUri.toString() + "'", null, null);
            cursor.moveToFirst();
            myArrayAdapter = new MyArrayAdapter(getActivity(), cursor, 0, ImageEntry.LogosTable.TABLE_NAME);
        }
        if(position==ImageEntry.TextTable.TABLE_NAME) {
            cursor = getActivity().getContentResolver().query(ImageEntry.TextTable.CONTENT_URI, null, ImageEntry.TextTable.COLUMN_ID+"='"+imageUri.toString()+"'", null, null);
            cursor.moveToFirst();
            myArrayAdapter = new MyArrayAdapter(getActivity(),cursor,0, ImageEntry.TextTable.TABLE_NAME);
        }
        if(position==ImageEntry.SafeTable.TABLE_NAME) {
            cursor = getActivity().getContentResolver().query(ImageEntry.SafeTable.CONTENT_URI, null, ImageEntry.SafeTable.COLUMN_ID+"='"+imageUri.toString()+"'", null, null);
            cursor.moveToFirst();
            myArrayAdapter = new MyArrayAdapter(getActivity(),cursor,0, ImageEntry.SafeTable.TABLE_NAME);
        }


        mListView =  view.findViewById(R.id.listview_items);
        mListView.setAdapter(myArrayAdapter);
    }

    }


    class SectionsPagerAdapter extends FragmentPagerAdapter {


        private  Uri imageUri;
        private int TotalTabs=3;
        private   ArrayList<String> title;
        public SectionsPagerAdapter(FragmentManager fm, Uri img, ArrayList<String> tit) {
            super(fm);
            this.title=tit;
            TotalTabs=title.size();
            imageUri=img;

        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.getInstance(title.get((position)), imageUri);
        }

        @Override
        public int getCount() {

            return TotalTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }


    }

