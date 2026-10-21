package entity;

public class PhuKien extends SanPham {
    private String chatLieu;
    private String mauSac;

    public PhuKien() {
        super();
    }

    public PhuKien(String maSP, String tenSP, double gia, int soLuong, String chatLieu, String mauSac) {
        super(maSP, tenSP, gia, soLuong);
        this.chatLieu = chatLieu;
        this.mauSac = mauSac;
    }

    public String getChatLieu() { return chatLieu; }
    public void setChatLieu(String chatLieu) { this.chatLieu = chatLieu; }
    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }

    @Override
    public String getLoai() { return "PHUKIEN"; }

    @Override
    public double tinhGiamGia() { return 10; }

    @Override
    protected String thongTinRieng() { return chatLieu + "|" + mauSac; }

    @Override
    public String moTa() { return "Chat lieu " + chatLieu + ", mau " + mauSac; }
}
