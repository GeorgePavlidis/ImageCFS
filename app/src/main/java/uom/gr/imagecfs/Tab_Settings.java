package uom.gr.imagecfs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import uom.gr.imagecfs.data.ImageEntry;


@SuppressLint("ValidFragment")
    class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private MyArrayAdapter  myArrayAdapter;
        private ListView mListView;
        private static Uri imageUri;


        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber,Uri img) {
            imageUri = img;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Cursor cursor;
            if(getArguments().getInt(ARG_SECTION_NUMBER)==3) {
                cursor = getActivity().getContentResolver().query(ImageEntry.LabelTable.CONTENT_URI, null, ImageEntry.LabelTable.COLUMN_ID + "='" + imageUri.toString() + "'", null, null);
                cursor.moveToFirst();
                myArrayAdapter = new MyArrayAdapter(getActivity(), cursor, 0, ImageEntry.LabelTable.TABLE_NAME);
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER)==2) {
                cursor = getActivity().getContentResolver().query(ImageEntry.LogosTable.CONTENT_URI, null, ImageEntry.LogosTable.COLUMN_ID + "='" + imageUri.toString() + "'", null, null);
                cursor.moveToFirst();
                myArrayAdapter = new MyArrayAdapter(getActivity(), cursor, 0, ImageEntry.LogosTable.TABLE_NAME);
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                 cursor = getActivity().getContentResolver().query(ImageEntry.TextTable.CONTENT_URI, null, ImageEntry.TextTable.COLUMN_ID+"='"+imageUri.toString()+"'", null, null);
                cursor.moveToFirst();
                myArrayAdapter = new MyArrayAdapter(getActivity(),cursor,0, ImageEntry.TextTable.TABLE_NAME);
            }

            View rootView = inflater.inflate(R.layout.fragment_item, container, false);



            mListView = rootView.findViewById(R.id.listview_items);
            mListView.setAdapter(myArrayAdapter);


            return rootView;
        }
    }


    class SectionsPagerAdapter extends FragmentPagerAdapter {


        private  Uri imageUri;
        private int TotalTabs=3;
        public SectionsPagerAdapter(FragmentManager fm, Uri img) {
            super(fm);
            setCount();
            imageUri=img;

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, imageUri);
        }

        @Override
        public int getCount() {

            return TotalTabs;
        }

        private void setCount(){

        }
    }

