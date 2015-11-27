package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bo.nhatkyhoctiengnhat.R;

import app.learning.fantaster.nhatkyhoctiengnhat.util.slidingtab.SlidingTabLayout;

/**
 * HomeFragment trả về một SlidingTabLayout (được viết bởi Google)-> SlidingTabLayout cần được đặt một ViewPager
 * để sử dụng -> ViewPager cần một adapter inherited từ PagerAdapter
 */
public class HomeFragment extends Fragment {

    /**
     * Ở method này, ta chỉ cần trả về view mà fragment này dùng mà chưa cần phải assign cụ thể content
     * hoặc listener cho các thành phần của view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
        // Đây là LinearLayout chứa SlidingTabLayout và ViewPager khi chưa được downcast.
    }

    /**
     * Đây là method để cụ thể hóa các thành phần của LinearLayout chưa downcast ở trên.
     * Ta cần cụ thể hóa SlidingTabLayout và PagerAdapter.
     * Vì SlidingTabLayout chỉ có một thành phần trong đó là ViewPager, nên ta chỉ cần setAdapter cho nó
     * Thành phần trong Adapter sẽ được cụ thể trong một class nơi ta custom PagerAdapter
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

        mSlidingTabLayout.setDistributeEvenly(true); // code của Google hổng có cái method này

       // Set color for tab-indicator, cái thanh dài dài chạy qua chạy lại trên tab >_<
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getActivity(), R.color.colorTitle);
            }

            @Override
            public int getDividerColor(int position) {
                return ContextCompat.getColor(getActivity(), R.color.colorPrimary);
            }

        });

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomPagerAdapter(getFragmentManager()));
        mSlidingTabLayout.setViewPager(viewPager);

    }

    /**
     * Ta sẽ custom PagerAdapter ở đây qua CustomPagerAdapter
     */
    class CustomPagerAdapter extends FragmentPagerAdapter {

        private final int SO_LUONG_TAB = 3;

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * This method will setup Fragment as well as its content in respect desired position.
         * To customize the tab in divider, indicator, title, etc, recall method populateTabStrip() and
         * class TabClickListener
         * @param position - desired position. In other words, what kind of Fragments needed to create
         *                 at this position
         * @return - desired Fragment at this position
         */
        @Override
        public Fragment getItem(int position) {
            return HomeTabFragment.newInstance();
        }

        @Override
        public int getCount() {
            return SO_LUONG_TAB;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = "";
            switch (position) {
                case 0 :
                    title = "HOME";
                    break;
                case 1 :
                    title = getString(R.string.mau_cau);
                    break;
                case 2 :
                    title = getString(R.string.thuc_hanh);
                    break;
            }
            return title;
        }

        /**
         * Đây là method quan trọng nhất, trong ViewPager sẽ có nhiều page với những thành phần khác nhau
         * Dựa vào vị trí position, ta sẽ cụ thể hóa các thành phần theo ý muốn
         *
         * Method này instantiate nội dung trong tab, để instantiate title hay những đặc tính của tab (cái
         * bấm qua bấm lại), tùy chỉnh trong SlidingTabLayout 2 methods createDefaultTabView(...),
         * populateTabStrip() và 1 class TabClickListener để tùy chỉnh event
         */
    /*    @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Trước hết inflate một layout đã có đủ hết các thành phần và chưa được cụ thể hóa (hết)
            // getActivity() = Context mà view này được chứa trong
           View view = getActivity().getLayoutInflater().inflate(R.layout.layout_home_tab, container, false);

            container.addView(view); // Cái này rất quan trọng

            return view;
        }*/

    }
}
