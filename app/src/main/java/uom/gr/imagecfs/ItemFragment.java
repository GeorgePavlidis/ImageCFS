package uom.gr.imagecfs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {

    private ArrayAdapter<ProgressBar> ListAdapter;
    private FragmentActivity activity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<ProgressBar> dummyList = new ArrayList<>();

        ListAdapter =
                new ArrayAdapter<ProgressBar>(
                        activity,
                        R.layout.fragment_item_list,
                        R.id.item_bar,
                        dummyList
                );
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);

        ListView forecastListView = rootView.findViewById(R.id.listview_items);
        forecastListView.setAdapter(ListAdapter);
        return rootView;

    }

    public View onCreate(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState, FragmentActivity activity) {
        this.activity = activity;
        return onCreateView(inflater,container,savedInstanceState);
    }
}
