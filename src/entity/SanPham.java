package entity;

import java.util.Objects;

import inter.MatHang;
import util.AppException;


/**
 * Lớp trừu tượng cho sản phẩm bán kèm: thức ăn, thuốc, phụ kiện.
 * Mỗi lớp con tự mô tả đặc điểm riêng và loại hàng của mình.
 */
public abstract class SanPham implements MatHang {

    /** Thuộc tính static: đếm số đối tượng sản phẩm đã tạo trong phiên chạy. */
    private static int soLuongDaTao = 0;

    private final String ma;
    private String ten;
    private double giaBan;
    private int soLuong;

    protected SanPham(String ma, String ten, double giaBan, int soLuong) {
        this.ma = Objects.requireNonNull(ma, "Mã không được rỗng");
        this.ten = Objects.requireNonNull(ten, "Tên không được rỗng");
        this.giaBan = Math.max(0, giaBan);
        this.soLuong = Math.max(0, soLuong);
        soLuongDaTao++;
    }

    public static int getSoLuongDaTao() {
        return soLuongDaTao;
    }

    @Override
    public String getMa() {
        return ma;
    }

    @Override
    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = Objects.requireNonNull(ten, "Tên không được rỗng");
    }

    @Override
    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = Math.max(0, giaBan);
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = Math.max(0, soLuong);
    }

    @Override
    public int getSoLuongTon() {
        return soLuong;
    }

    @Override
    public boolean conBanDuoc() {
        return soLuong > 0;
    }

    @Override
    public void truKho(int soLuongBan) {
        if (soLuongBan <= 0) {
            throw new AppException.NghiepVu("Số lượng bán phải lớn hơn 0");
        }
        if (soLuongBan > soLuong) {
            throw new AppException.NghiepVu("Không đủ hàng cho " + ten + " (chỉ còn " + soLuong + ")");
        }
        soLuong -= soLuongBan;
    }

    @Override
    public void hoanKho(int soLuongNhap) {
        if (soLuongNhap <= 0) {
            throw new AppException.NghiepVu("Số lượng nhập phải lớn hơn 0");
        }
        soLuong += soLuongNhap;
    }

    @Override
    public abstract String getLoai();

    @Override
    public abstract String getDacDiem();

    @Override
    public String toString() {
        return ma + " - " + ten + " (" + getLoai() + ")";
    }
}
