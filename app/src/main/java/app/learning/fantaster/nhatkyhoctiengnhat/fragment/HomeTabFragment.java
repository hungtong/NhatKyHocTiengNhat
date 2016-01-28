package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.learning.fantaster.nhatkyhoctiengnhat.R;

public class HomeTabFragment extends Fragment {

    public static final int RESULT_OK = 928;
    public static final int REQUESTED_CODE = 212;

    private static HomeTabFragment instanceHomeTabFragment;

//    private ArrayList<EntryContent> list;
//    private EntryAdapter adapter;

    /**
     * Since it is highly recommended that every Fragment should not have any constructor other than default
     * constructor, this is actually a design so that we can construct fragment without using constructor
     * nor setter methods.
     *
     * Even more conveniently, this method helps save all arguments, which are data about title, page number, etc
     * so that we can retrieve easily later by Bunlde class. This is actually the communication between Fragments
     *
     * @return - a HomeTabFragment in corresponding position
     */
    public static HomeTabFragment getInstance() {
        if (instanceHomeTabFragment == null) {
            instanceHomeTabFragment = new HomeTabFragment();
        }
        return  instanceHomeTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.home_tab_recycler_view);

    }

    /**
     * Specified the interface OnDeleteListener.
     * In Recycler View, selection as well as selected position are not handled
     */
/*    class ConcreteOnDeleteListener implements EntryAdapter.EntryListener {

        @Override
        public void onDelete(View childPressed, final int position) {

        }

        @Override
        public void onCloserView(View childPressed, final int position) {
            /// New Intent Upcoming
        }
    }
*/
}
