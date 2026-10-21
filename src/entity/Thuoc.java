package entity;

public class Thuoc extends SanPham {
    private String congDung;
    private String hanSuDung;

    public Thuoc() {
        super();
    }

    public Thuoc(String maSP, String tenSP, double gia, int soLuong, String congDung, String hanSuDung) {
        super(maSP, tenSP, gia, soLuong);
        this.congDung = congDung;
        this.hanSuDung = hanSuDung;
    }

    public String getCongDung() { return congDung; }
    public void setCongDung(String congDung) { this.congDung = congDung; }
    public String getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(String hanSuDung) { this.hanSuDung = hanSuDung; }

    @Override
    public String getLoai() { return "THUOC"; }

    @Override
    public double tinhGiamGia() { return 0; } // thuoc khong giam gia

    @Override
    protected String thongTinRieng() { return congDung + "|" + hanSuDung; }

    @Override
    public String moTa() { return "Cong dung: " + congDung + ", HSD " + hanSuDung; }
}
