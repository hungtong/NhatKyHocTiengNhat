package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.ClauseAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.ClauseCardContent;


public class ClauseTabFragment extends Fragment {

    private ArrayList<ClauseCardContent> list;
    private ClauseAdapter adapter;

    public static ClauseTabFragment newInstance() {
        return new ClauseTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clause_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.clause_tab_recycler_view);

        list = initiateList();
        adapter = new ClauseAdapter(getActivity(), list, new OnConcreteListener());

        // spanCount : number of columns or rows,  orientation : vertical or horizontal
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3 ,StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    class OnConcreteListener implements ClauseAdapter.ClauseListener {

        @Override
        public void onDelete(View childPressed, final int position) {
            list.remove(position);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCloserView(View childPressed, final int position) {

        }

    }

    private ArrayList<ClauseCardContent> initiateList() {
        ArrayList<ClauseCardContent> myList = new ArrayList<>();

        int[] headerImage = {
                R.drawable.image2,
                R.drawable.image3,
                R.drawable.image4,
                R.drawable.image5,
                R.drawable.image6
        };

        String[] title = {
                "Fragment",
                "Recycler View",
                "Card View",
                "View Pager",
                "Recycler View Adapter"
                };

        String[] clause = {
                "Static library support version of the framework's Fragment",
                "A flexible view for providing a limited window into a large data set.",
                "A FrameLayout with a rounded corner background and shadow.",
                "Layout manager that allows the user to flip left and right through pages of data.",
                "Adapters provide a binding from an app-specific data set to views that are displayed within a RecyclerView."
        };

        for (int i = 0; i < title.length; i++)
            myList.add(new ClauseCardContent(headerImage[i], title[i], clause[i]));

        return myList;
    }

}
