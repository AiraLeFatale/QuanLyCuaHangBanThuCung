package entity;

import java.time.LocalDate;

import util.TrangTri;


/** Thức ăn cho thú cưng: có loại thú cưng phù hợp, khối lượng túi và hạn sử dụng. */
public class ThucAn extends SanPham {

    private String loaiThuCung;
    private double khoiLuongKg;
    private LocalDate hanSuDung;

    public ThucAn(String ma, String ten, double giaBan, int soLuong,
                  String loaiThuCung, double khoiLuongKg, LocalDate hanSuDung) {
        super(ma, ten, giaBan, soLuong);
        this.loaiThuCung = loaiThuCung == null ? "" : loaiThuCung;
        this.khoiLuongKg = Math.max(0, khoiLuongKg);
        this.hanSuDung = hanSuDung;
    }

    public String getLoaiThuCung() {
        return loaiThuCung;
    }

    public void setLoaiThuCung(String loaiThuCung) {
        this.loaiThuCung = loaiThuCung == null ? "" : loaiThuCung;
    }

    public double getKhoiLuongKg() {
        return khoiLuongKg;
    }

    public void setKhoiLuongKg(double khoiLuongKg) {
        this.khoiLuongKg = Math.max(0, khoiLuongKg);
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    @Override
    public String getLoai() {
        return "Thức ăn";
    }

    @Override
    public String getDacDiem() {
        return "Cho " + loaiThuCung + ", túi " + TrangTri.so(khoiLuongKg) + " kg, HSD "
                + TrangTri.ngay(hanSuDung);
    }
}
