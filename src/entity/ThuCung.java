package entity;

import inter.IGiamGia;

/** Lop truu tuong: thu cung. */
public abstract class ThuCung implements IGiamGia {
    // thuoc tinh static: dem so doi tuong thu cung da duoc khoi tao
    private static int soLuongDaTao = 0;

    protected String maThuCung;
    protected String ten;
    protected int tuoiThang;
    protected double gia;
    protected String gioiTinh;
    protected boolean daBan;

    public ThuCung() {
        soLuongDaTao++;
    }

    public ThuCung(String maThuCung, String ten, int tuoiThang, double gia, String gioiTinh, boolean daBan) {
        this.maThuCung = maThuCung;
        this.ten = ten;
        this.tuoiThang = tuoiThang;
        this.gia = gia;
        this.gioiTinh = gioiTinh;
        this.daBan = daBan;
        soLuongDaTao++;
    }

    // phuong thuc static
    public static int getSoLuongDaTao() {
        return soLuongDaTao;
    }

    public String getMaThuCung() { return maThuCung; }
    public void setMaThuCung(String maThuCung) { this.maThuCung = maThuCung; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    public int getTuoiThang() { return tuoiThang; }
    public void setTuoiThang(int tuoiThang) { this.tuoiThang = tuoiThang; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public boolean isDaBan() { return daBan; }
    public void setDaBan(boolean daBan) { this.daBan = daBan; }

    // ===== phuong thuc truu tuong =====
    public abstract String getLoai();          // CHO / MEO / CHIM
    public abstract String tiengKeu();
    public abstract String moTa();             // thong tin rieng de hien thi
    protected abstract String thongTinRieng(); // thong tin rieng de ghi file

    public double giaSauGiam() {
        return gia * (100 - tinhGiamGia()) / 100;
    }

    public String toFileString() {
        return getLoai() + "|" + maThuCung + "|" + ten + "|" + tuoiThang + "|" + (long) gia
                + "|" + gioiTinh + "|" + daBan + "|" + thongTinRieng();
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-4s | %-12s | %3d thang | %-4s | %,12.0f d | %-6s | %s",
                maThuCung, getLoai(), ten, tuoiThang, gioiTinh, gia, daBan ? "DA BAN" : "CON", moTa());
    }
}
