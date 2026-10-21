package entity;

public class ChiTietHoaDon {
    private String maHang;
    private String tenHang;
    private int soLuong;
    private double donGia;
    private double tiLeGiam; // phan tram

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String maHang, String tenHang, int soLuong, double donGia, double tiLeGiam) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.tiLeGiam = tiLeGiam;
    }

    public String getMaHang() { return maHang; }
    public String getTenHang() { return tenHang; }
    public int getSoLuong() { return soLuong; }
    public double getDonGia() { return donGia; }
    public double getTiLeGiam() { return tiLeGiam; }

    public double thanhTien() {
        return soLuong * donGia * (100 - tiLeGiam) / 100;
    }

    public String toFileString() {
        return maHang + ";" + tenHang + ";" + soLuong + ";" + (long) donGia + ";" + tiLeGiam;
    }

    public static ChiTietHoaDon fromFileString(String s) {
        String[] p = s.split(";", -1);
        return new ChiTietHoaDon(p[0], p[1], Integer.parseInt(p[2]),
                Double.parseDouble(p[3]), Double.parseDouble(p[4]));
    }

    @Override
    public String toString() {
        return String.format("%-6s %-24s x%-3d %,10.0f d  giam %.0f%%  = %,12.0f d",
                maHang, tenHang, soLuong, donGia, tiLeGiam, thanhTien());
    }
}
