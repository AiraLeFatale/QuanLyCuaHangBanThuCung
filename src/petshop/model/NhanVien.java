package petshop.model;

import java.time.LocalDate;

/** Nhân viên của cửa hàng. */
public class NhanVien extends Nguoi {

    private String chucVu;
    private double luong;
    private LocalDate ngayVaoLam;

    public NhanVien(String ma, String hoTen, GioiTinh gioiTinh, LocalDate ngaySinh,
                    String soDienThoai, String diaChi, String chucVu, double luong, LocalDate ngayVaoLam) {
        super(ma, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi);
        this.chucVu = chucVu == null ? "" : chucVu;
        this.luong = Math.max(0, luong);
        this.ngayVaoLam = ngayVaoLam;
    }

    public NhanVien(String ma, String hoTen, String chucVu, double luong) {
        this(ma, hoTen, GioiTinh.KHAC, null, "", "", chucVu, luong, null);
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu == null ? "" : chucVu;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = Math.max(0, luong);
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    @Override
    public String getLoaiNguoi() {
        return "Nhân viên";
    }

    @Override
    public String toString() {
        return getMa() + " - " + getHoTen() + (chucVu.isEmpty() ? "" : " (" + chucVu + ")");
    }
}
