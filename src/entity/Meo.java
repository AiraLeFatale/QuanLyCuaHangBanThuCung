package entity;

public class Meo extends ThuCung {
    private String mauLong;
    private boolean longDai;

    public Meo() {
        super();
    }

    public Meo(String maThuCung, String ten, int tuoiThang, double gia, String gioiTinh, boolean daBan,
               String mauLong, boolean longDai) {
        super(maThuCung, ten, tuoiThang, gia, gioiTinh, daBan);
        this.mauLong = mauLong;
        this.longDai = longDai;
    }

    public String getMauLong() { return mauLong; }
    public void setMauLong(String mauLong) { this.mauLong = mauLong; }
    public boolean isLongDai() { return longDai; }
    public void setLongDai(boolean longDai) { this.longDai = longDai; }

    @Override
    public String getLoai() { return "MEO"; }

    @Override
    public String tiengKeu() { return "Meo meo!"; }

    @Override
    public double tinhGiamGia() {
        return tuoiThang >= 12 ? 10 : 3;
    }

    @Override
    protected String thongTinRieng() { return mauLong + "|" + longDai; }

    @Override
    public String moTa() { return "Mau long: " + mauLong + ", " + (longDai ? "long dai" : "long ngan"); }
}
