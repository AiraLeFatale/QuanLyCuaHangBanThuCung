package entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import inter.CoMaKhoa;

/**
 * Lớp trừu tượng cho mọi con người trong hệ thống (khách hàng, nhân viên).
 */
public abstract class Nguoi implements CoMaKhoa {

    private final String ma;
    private String hoTen;
    private GioiTinh gioiTinh;
    private LocalDate ngaySinh;
    private String soDienThoai;
    private String diaChi;

    protected Nguoi(String ma, String hoTen, GioiTinh gioiTinh, LocalDate ngaySinh,
                    String soDienThoai, String diaChi) {
        this.ma = Objects.requireNonNull(ma, "Mã không được rỗng");
        this.hoTen = Objects.requireNonNull(hoTen, "Họ tên không được rỗng");
        this.gioiTinh = gioiTinh == null ? GioiTinh.KHAC : gioiTinh;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai == null ? "" : soDienThoai;
        this.diaChi = diaChi == null ? "" : diaChi;
    }

    @Override
    public String getMa() {
        return ma;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = Objects.requireNonNull(hoTen, "Họ tên không được rỗng");
    }

    public GioiTinh getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(GioiTinh gioiTinh) {
        this.gioiTinh = gioiTinh == null ? GioiTinh.KHAC : gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai == null ? "" : soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi == null ? "" : diaChi;
    }

    public int getTuoi() {
        return ngaySinh == null ? 0 : (int) ChronoUnit.YEARS.between(ngaySinh, LocalDate.now());
    }

    /** Loại người dùng, dùng cho giao diện và tệp dữ liệu. */
    public abstract String getLoaiNguoi();

    @Override
    public String toString() {
        return ma + " - " + hoTen;
    }
}
