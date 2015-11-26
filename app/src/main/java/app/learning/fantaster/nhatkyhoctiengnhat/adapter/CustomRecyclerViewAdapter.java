package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bo.nhatkyhoctiengnhat.R;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.data.RecyclerViewContent;

/**
 * Adapter cho RecyclerView, RecyclerView.Adapter<E> là một generic type
 */
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    private final Activity context;
    private final ArrayList<RecyclerViewContent> list;

    private static OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(View childPressed, int position);
    }

    public CustomRecyclerViewAdapter(Activity context, ArrayList<RecyclerViewContent> list, OnDeleteListener concreteListener) {
        this.context = context;
        this.list = list;
        listener = concreteListener;
    }

    /**
     * Thay vì trục tiếp tương tác lên các components của container, ta sẽ tạo một
     * ViewHolder để chứa các components đó và tương tác với chúng qua ViewHolder.
     *
     * ViewHolder thực chất là một static class programmer tự tạo.
     *
     * Trong khi initialize các components, ta phải liên tục gọi findViewById() làm cho
     * quá trình tạo các items chậm hơn.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements OnLongClickListener {
        private final TextView title, mauCau, soNgayLuyenTap;
        private final ImageView bellIcon;

        /**
         * Trong constructor này, ta sẻ định hướng để tìm các components luôn
         * Lưu ý ở giai đoạn này, ta chỉ định hướng chứ không specify, specification được
         * thực hiện ở onBindViewHolder(...) ---> tại sao ư? Trên android.developer nói vậy
         * @param view - Một container tượng trưng cho layout chứa các components con cần customize
         */
        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            mauCau = (TextView) view.findViewById(R.id.mau_cau);
            soNgayLuyenTap = (TextView) view.findViewById(R.id.so_ngay_luyen_tap);
            bellIcon = (ImageView) view.findViewById(R.id.bell_icon);

            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onDelete(view, getAdapterPosition());
            return false;
        }


        // ViewHolder.title = ... ??? Tại sao ko, bởi vì Encapsulation
        public TextView getTitle() {
            return title;
        }

        public TextView getMauCau() {
            return mauCau;
        }

        public TextView getSoNgayLuyenTap() {
            return soNgayLuyenTap;
        }

        public ImageView getBellIcon() {
            return bellIcon;
        }

    }

    /**
     * Trả về một ViewHolder với đầy đủ các components cần thiết và đã được định hướng sẵn.
     * ViewHolder này sẽ được bind vào Recycler View bằng onBindViewHolder
     *
     * Lưu ý cần phải lấy context của ViewGroup (container)
     *
     * @param container - View chứa RecyclerView, trong trường hợp này là một layout
     * @param position - Vị trí trên RecyclerView
     * @return Một ViewHolder có đủ các component ở trên đó, qua constructor, ta đã định hướng
     *          được các components này
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int position) {
       View view = context.getLayoutInflater().inflate(R.layout.custom_recycler_view, container, false);
    //  View view = LayoutInflater.from(container.getContext()).inflate(R.layout.custom_recycler_view, container, false);
    //  or View view = ((Activity) container.getContext()).getLayoutInflater().inflate(...)
    //  Chỉ có Activity, subclass của Context, có getLayoutInflater, downcast nó xuống

        return new ViewHolder(view);

    }

    /**
     *  Hiểu đơn giản là gắn ViewHolder với RecyclerView
     *  Ở bước này ta sẽ specify cho các components luôn
     * @param viewHolder - Cái ViewHolder lấy từ onCreateViewHolder(...)
     * @param position - vị trí của item trên RecyclerView
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RecyclerViewContent recyclerViewContent = list.get(position);

        String title = recyclerViewContent.getTitle();
        String soNgayLuyenTap = recyclerViewContent.getSoNgayLuyenTap();
        String mauCau = recyclerViewContent.getMauCau();
        int bellIcon = recyclerViewContent.getBellIcon();

        viewHolder.getTitle().setText(title);
        viewHolder.getBellIcon().setBackgroundResource(bellIcon);

        String format_so_ngay_luyen_tap = context.getResources().getString(R.string.format_so_ngay_luyen_tap);
        String format_mau_cau = context.getResources().getString(R.string.format_mau_cau);
        viewHolder.getSoNgayLuyenTap().setText(String.format(format_so_ngay_luyen_tap, soNgayLuyenTap));
        viewHolder.getMauCau().setText(String.format(format_mau_cau, mauCau));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
