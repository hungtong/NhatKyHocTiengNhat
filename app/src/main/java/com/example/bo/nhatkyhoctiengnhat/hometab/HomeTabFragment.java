package com.example.bo.nhatkyhoctiengnhat.hometab;

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
import com.example.bo.nhatkyhoctiengnhat.R;
import com.example.bo.nhatkyhoctiengnhat.TaoMoi;

import java.util.ArrayList;

public class HomeTabFragment extends Fragment {

    public static final int RESULT_OK = 928;
    public static final int REQUESTED_CODE = 212;

    public static final String KEY_LAY_TITLE = "KeyLayTitle";
    public static final String KEY_LAY_SO_NGAY_LUYEN_TAP = "KeyLaySoNgayLuyenTap";
    public static final String KEY_LAY_MAU_CAU = "KeyLayMauCau";
    public static final String KEY_LAY_BELL_ICON = "KeyLayBellIcon";

    private ArrayList<RecyclerViewContent> list;
    private RecyclerView recyclerView;
    private CustomRecyclerViewAdapter adapter;
    private FloatingActionButton taoMoiButton;

    /**
     * Since it is highly recommended that every Fragment should not have any constructor other than default
     * constructor, this is actually a design so that we can construct fragment without using constructor
     * nor setter methods.
     *
     * Even more conveniently, this method helps save all arguments, which are data about title, page number, etc
     * so that we can retrieve easily later by Bunlde class. This is actually the communication between Fragments
     *
     * @param position - initial position referring to this fragment
     * @return - a HomeTabFragment in corresponding position
     */
    public static HomeTabFragment newInstance(int position) {
        Bundle bundle = new Bundle();   // Setup an empty Bundle to store data
        bundle.putInt("home_tab_fragment", android.R.drawable.ic_menu_report_image); // put into Bundle data wanted to retrieve

        HomeTabFragment fragment = new HomeTabFragment(); // default constructor and the only constructor that should
                                                            // be in every Fragment class
        fragment.setArguments(bundle);  // Want to retrieve, simply use getArguments().getXXX("key", [default value]);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cau_tao_cua_home_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        list = new ArrayList<RecyclerViewContent>();
        adapter = new CustomRecyclerViewAdapter(((Activity) getContext()),list, new ConcreteOnDeleteListener());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        taoMoiButton = (FloatingActionButton) view.findViewById(R.id.tao_moi);
        taoMoiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((Activity) getContext()), TaoMoi.class);
                startActivityForResult(intent, REQUESTED_CODE);
            }
        });
    }

    /**
     * Specified the interface OnDeleteListener.
     * In Recycler View, selection as well as selected position are not handled
     */
    class ConcreteOnDeleteListener implements CustomRecyclerViewAdapter.OnDeleteListener {
        @Override
        public void onDelete(View childPressed, int position) {
            list.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTED_CODE && resultCode == RESULT_OK && data != null) {
            RecyclerViewContent newModule = new RecyclerViewContent();

            String title = data.getStringExtra(KEY_LAY_TITLE);
            String soNgayLuyenTap = data.getStringExtra(KEY_LAY_SO_NGAY_LUYEN_TAP);
            String mauCau = data.getStringExtra(KEY_LAY_MAU_CAU);
            int bellIcon = data.getIntExtra(KEY_LAY_BELL_ICON, android.R.drawable.ic_lock_idle_alarm);

            newModule.setTitle(title);
            newModule.setSoNgayLuyenTap(soNgayLuyenTap);
            newModule.setMauCau(mauCau);
            newModule.setBellIcon(bellIcon);

            list.add(newModule);
            adapter.notifyDataSetChanged();
        }
    }
}
