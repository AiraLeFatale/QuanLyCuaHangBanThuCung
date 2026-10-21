package entity;

public class Chim extends ThuCung {
    private String mauSac;
    private boolean bietNoi;

    public Chim() {
        super();
    }

    public Chim(String maThuCung, String ten, int tuoiThang, double gia, String gioiTinh, boolean daBan,
                String mauSac, boolean bietNoi) {
        super(maThuCung, ten, tuoiThang, gia, gioiTinh, daBan);
        this.mauSac = mauSac;
        this.bietNoi = bietNoi;
    }

    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }
    public boolean isBietNoi() { return bietNoi; }
    public void setBietNoi(boolean bietNoi) { this.bietNoi = bietNoi; }

    @Override
    public String getLoai() { return "CHIM"; }

    @Override
    public String tiengKeu() { return bietNoi ? "Xin chao! Xin chao!" : "Chiu chiu!"; }

    @Override
    public double tinhGiamGia() { return 2; }

    @Override
    protected String thongTinRieng() { return mauSac + "|" + bietNoi; }

    @Override
    public String moTa() { return "Mau: " + mauSac + ", " + (bietNoi ? "biet noi" : "khong biet noi"); }
}
