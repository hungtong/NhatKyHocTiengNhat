package app.learning.fantaster.nhatkyhoctiengnhat.data;

/**
 * Data for RecyclerView's components
 */
public class RecyclerViewContent {
    private String title, mauCau, soNgayLuyenTap;
    private int bellIcon;

    public String getTitle() {
        return title;
    }

    public String getMauCau() {
        return mauCau;
    }

    public String getSoNgayLuyenTap() {
        return soNgayLuyenTap;
    }

    public int getBellIcon() {
        return bellIcon;
    }

    public void setTitle(String title) {
         this.title = title;
    }

    public void setMauCau(String mauCau) {
        this.mauCau = mauCau;
    }

    public void setSoNgayLuyenTap(String soNgayLuyenTap) {
        this.soNgayLuyenTap = soNgayLuyenTap;
    }

    public void setBellIcon(int bellIcon) {
        this.bellIcon = bellIcon;
    }

}
