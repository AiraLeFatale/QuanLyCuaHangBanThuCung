package entity;

import inter.IGiamGia;

/** Lop truu tuong: san pham (thuc an, phu kien, thuoc). */
public abstract class SanPham implements IGiamGia {
    protected String maSP;
    protected String tenSP;
    protected double gia;
    protected int soLuong;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, double gia, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }
    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public abstract String getLoai();           // THUCAN / PHUKIEN / THUOC
    protected abstract String thongTinRieng();  // de ghi file
    public abstract String moTa();              // de hien thi

    public String toFileString() {
        return getLoai() + "|" + maSP + "|" + tenSP + "|" + (long) gia + "|" + soLuong + "|" + thongTinRieng();
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-8s | %-24s | %,10.0f d | SL: %4d | %s",
                maSP, getLoai(), tenSP, gia, soLuong, moTa());
    }
}
