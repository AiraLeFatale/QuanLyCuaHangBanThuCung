package entity;

public class ThucAn extends SanPham {
    private String loaiThuCung;
    private double trongLuong;
    private String hanSuDung;

    public ThucAn() {
        super();
    }

    public ThucAn(String maSP, String tenSP, double gia, int soLuong,
                  String loaiThuCung, double trongLuong, String hanSuDung) {
        super(maSP, tenSP, gia, soLuong);
        this.loaiThuCung = loaiThuCung;
        this.trongLuong = trongLuong;
        this.hanSuDung = hanSuDung;
    }

    public String getLoaiThuCung() { return loaiThuCung; }
    public void setLoaiThuCung(String loaiThuCung) { this.loaiThuCung = loaiThuCung; }
    public double getTrongLuong() { return trongLuong; }
    public void setTrongLuong(double trongLuong) { this.trongLuong = trongLuong; }
    public String getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(String hanSuDung) { this.hanSuDung = hanSuDung; }

    @Override
    public String getLoai() { return "THUCAN"; }

    @Override
    public double tinhGiamGia() { return 5; }

    @Override
    protected String thongTinRieng() { return loaiThuCung + "|" + trongLuong + "|" + hanSuDung; }

    @Override
    public String moTa() { return "Danh cho " + loaiThuCung + ", " + trongLuong + " kg, HSD " + hanSuDung; }
}
