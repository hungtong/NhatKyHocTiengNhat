package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.data.EntryContent;



/**
 * Adapter cho RecyclerView, RecyclerView.Adapter<E> là một generic type
 */
public class EntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity context;
    private final ArrayList<EntryContent> list;

    private static RecyclerViewListener listener;

    public static final int DATE_INDICATOR = 97000;
    public static final int CONTENT = 79120;

    public interface RecyclerViewListener {
        void onDelete(View childPressed, final int position);
        void onModify(View childPressed, final int position);
    }

    public EntryAdapter(Activity context, ArrayList<EntryContent> list,
                        RecyclerViewListener concreteListener) {
        this.context = context;
        this.list = list;
        listener = concreteListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getItemViewType() == DATE_INDICATOR)
            return DATE_INDICATOR;
        else return CONTENT;
    }

    /**
     *
     * Thay vì trực tiếp tương tác lên các components của container, ta sẽ tạo một
     * ViewHolder để chứa các components đó và tương tác với chúng qua ViewHolder.
     *
     * ViewHolder thực chất là một static class programmer tự tạo.
     *
     * Trong khi initialize các components, ta phải liên tục gọi findViewById() làm cho
     * quá trình tạo các items chậm hơn.
     *
     * This ViewHolder is typically for content of each section
     */
    public static class ContentViewHolder extends RecyclerView.ViewHolder
                                        implements OnLongClickListener, View.OnClickListener {
        // Không cần Encapsulation
        public final TextView title, mauCau, soNgayLuyenTap;
        public final ImageView bellIcon;

        /**
         * ========================================================================================
         * Trong constructor này, ta sẻ định hướng để tìm các components luôn
         * Lưu ý ở giai đoạn này, ta chỉ định hướng chứ không specify, specification được
         * thực hiện ở onBindViewHolder(...) ---> tại sao ư? Trên android.developer nói vậy
         * ========================================================================================
         * @param view - Một container tượng trưng cho layout chứa các components con cần customize
         */
        public ContentViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            mauCau = (TextView) view.findViewById(R.id.mau_cau);
            soNgayLuyenTap = (TextView) view.findViewById(R.id.so_ngay_luyen_tap);
            bellIcon = (ImageView) view.findViewById(R.id.bell_icon);

            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onDelete(view, getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
            listener.onModify(view, getAdapterPosition());
        }
    }

    /**
     * This ViewHolder is typically for  Indicator
     */
    public static class DateIndicatorViewHolder extends RecyclerView.ViewHolder {
        public final TextView dateIndicator;

        public DateIndicatorViewHolder(View view) {
            super(view);
            dateIndicator = (TextView) view.findViewById(R.id.date_indicator);
        }

    }


    /**
     * ========================================================================================
     * Trả về một ViewHolder với đầy đủ các components cần thiết và đã được định hướng sẵn.
     * ViewHolder này sẽ được bind vào Recycler View bằng onBindViewHolder
     *
     * Lưu ý cần phải lấy context của ViewGroup (container)
     *
     * Since we have multiple different items, we need to specify individually
     *========================================================================================
     * @param container  View chứa RecyclerView, trong trường hợp này là một layout
     * @param viewType  In case we need to specify different items, this parameter will indicate
     *                 corresponding actions for a certain item.
     * @return Một ViewHolder có đủ các component ở trên đó, qua constructor, ta đã định hướng
     *          được các components này
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        //   View view = context.getLayoutInflater().inflate(R.layout...., container, false);
        //  View view = LayoutInflater.from(container.getContext()).inflate(R.layout...., container, false);
        //  or View view = ((Activity) container.getContext()).getLayoutInflater().inflate(...)
        //  Chỉ có Activity, subclass của Context, có getLayoutInflater, downcast nó xuống

        View view;
        switch (viewType) {
            case (DATE_INDICATOR):
                view = context.getLayoutInflater().inflate(R.layout.date_indicator_recycler_view, container, false);
                return new DateIndicatorViewHolder(view);
            case (CONTENT):
                view = context.getLayoutInflater().inflate(R.layout.content_recycler_view, container, false);
                return new ContentViewHolder(view);
            default:  // to use switch to RETURN, default has to be presented
                view = context.getLayoutInflater().inflate(R.layout.content_recycler_view, container, false);
                return new ContentViewHolder(view);
        }
    }

    /**
     *  Hiểu đơn giản là gắn ViewHolder với RecyclerView
     *  Ở bước này ta sẽ specify cho các components luôn
     * @param viewHolder - Cái ViewHolder lấy từ onCreateViewHolder(...)
     * @param position - vị trí của item trên RecyclerView
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case DATE_INDICATOR :
                String today = context.getString(R.string.today);
                ((DateIndicatorViewHolder) viewHolder).dateIndicator.setText(today);
                break;
            case CONTENT :
                ContentViewHolder contentViewHolder = (ContentViewHolder) viewHolder;
                EntryContent entryContent = list.get(position);

                String title = entryContent.getTitle();
                String soNgayLuyenTap = entryContent.getSoNgayLuyenTap();
                String mauCau = entryContent.getMauCau();
                boolean bellIcon = entryContent.getBellIcon();

                contentViewHolder.title.setText(title);

                contentViewHolder.bellIcon.setImageResource(android.R.drawable.ic_lock_idle_alarm);
                if (bellIcon)
                    contentViewHolder.bellIcon.setColorFilter(ContextCompat.getColor(context, R.color.bell_on));
                else
                    contentViewHolder.bellIcon.setColorFilter(ContextCompat.getColor(context, R.color.bell_off));

                String format_so_ngay_luyen_tap = context.getResources().getString(R.string.format_so_ngay_luyen_tap);
                String format_mau_cau = context.getResources().getString(R.string.format_mau_cau);
                contentViewHolder.soNgayLuyenTap.setText(String.format(format_so_ngay_luyen_tap, soNgayLuyenTap));
                contentViewHolder.mauCau.setText(String.format(format_mau_cau, mauCau));

                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
