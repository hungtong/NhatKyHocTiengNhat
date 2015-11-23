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

/**
 * Created by Bo on 11/18/2015.
 */
public class HomeTabFragment extends Fragment {

    public static final String KEY_LAY_TITLE = "KeyLayTitle";
    public static final String KEY_LAY_SO_NGAY_LUYEN_TAP = "KeyLaySoNgayLuyenTap";
    public static final String KEY_LAY_MAU_CAU = "KeyLayMauCau";
    public static final String KEY_LAY_BELL_ICON = "KeyLayBellIcon";

    private ArrayList<RecyclerViewContent> list;
    private RecyclerView recyclerView;
    private CustomRecyclerViewAdapter adapter;
    private FloatingActionButton taoMoiButton;

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
                startActivity(intent);
                createNewModule();
            }
        });
    }

    class ConcreteOnDeleteListener implements CustomRecyclerViewAdapter.OnDeleteListener {
        @Override
        public void onDelete(View childPressed, int position) {
            list.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    private void createNewModule() {
        Bundle bundle = new Bundle();
        RecyclerViewContent newModule = new RecyclerViewContent();

        String title = bundle.getString(KEY_LAY_TITLE);
        String soNgayLuyenTap = bundle.getString(KEY_LAY_SO_NGAY_LUYEN_TAP);
        String mauCau = bundle.getString(KEY_LAY_MAU_CAU);
        int bellIcon = bundle.getInt(KEY_LAY_BELL_ICON);

        newModule.setTitle(title);
        newModule.setSoNgayLuyenTap(soNgayLuyenTap);
        newModule.setMauCau(mauCau);
        newModule.setBellIcon(bellIcon);
    }

}
