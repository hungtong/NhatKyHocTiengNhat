package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.MainActivity;
import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.TaoMoi;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.EntryAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.EntryContent;
import app.learning.fantaster.nhatkyhoctiengnhat.util.listener.EntryListener;

public class HomeTabFragment extends Fragment {

    public static final int RESULT_OK = 928;
    public static final int REQUESTED_CODE = 212;

    public static final String KEY_LAY_TITLE = "KeyLayTitle";
    public static final String KEY_LAY_SO_NGAY_LUYEN_TAP = "KeyLaySoNgayLuyenTap";
    public static final String KEY_LAY_MAU_CAU = "KeyLayMauCau";
    public static final String KEY_LAY_BELL_ICON = "KeyLayBellIcon";

    private ArrayList<EntryContent> list;
    private EntryAdapter adapter;

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
    public static HomeTabFragment newInstance() {
        Bundle bundle = new Bundle();   // Setup an empty Bundle to store data
        bundle.putInt("home_tab_fragment", R.drawable.ic_home_white_24dp); // put into Bundle data wanted to retrieve

        HomeTabFragment fragment = new HomeTabFragment(); // default constructor and the only constructor that should
                                                            // be in every Fragment class
        fragment.setArguments(bundle);  // Want to retrieve, simply use getArguments().getXXX("key", [default value]);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.home_tab_recycler_view);
        list = new ArrayList<>();
        adapter = new EntryAdapter(((Activity) getContext()),list, new ConcreteOnDeleteListener());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        FloatingActionButton taoMoiButton = (FloatingActionButton) view.findViewById(R.id.tao_entry_moi);
        taoMoiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaoMoi.class);
                startActivityForResult(intent, REQUESTED_CODE);
            }
        });
    }

    /**
     * Specified the interface OnDeleteListener.
     * In Recycler View, selection as well as selected position are not handled
     */
    class ConcreteOnDeleteListener implements EntryListener {

        @Override
        public void onDelete(View childPressed, final int position) {
            list.remove(position);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCloserView(View childPressed, final int position) {
            /// New Intent Upcoming
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTED_CODE && resultCode == RESULT_OK && data != null) {

            // If there is a signal requiring to update recycler view, it means every previous date
            // indicator was already updated, now we simply need to add a "Today Date Indicator" right
            // after when we created a new section.
            if (MainActivity.signalToUpdateRecyclerView) {
                MainActivity.signalToUpdateRecyclerView = false;
                EntryContent dateModule = new EntryContent();
                dateModule.setItemViewType(EntryAdapter.DATE_INDICATOR);
                list.add(dateModule);
                adapter.notifyDataSetChanged();
            }

            EntryContent contentModule = new EntryContent();

            String title = data.getStringExtra(KEY_LAY_TITLE);
            String soNgayLuyenTap = data.getStringExtra(KEY_LAY_SO_NGAY_LUYEN_TAP);
            String mauCau = data.getStringExtra(KEY_LAY_MAU_CAU);
            boolean bellIcon = data.getBooleanExtra(KEY_LAY_BELL_ICON, false);

            contentModule.setTitle(title);
            contentModule.setSoNgayLuyenTap(soNgayLuyenTap);
            contentModule.setMauCau(mauCau);
            contentModule.setBellIcon(bellIcon);
            contentModule.setItemViewType(EntryAdapter.CONTENT);

            list.add(contentModule);
            adapter.notifyDataSetChanged();
        }
    }

    private void updateRecyclerView() {

    }
}
