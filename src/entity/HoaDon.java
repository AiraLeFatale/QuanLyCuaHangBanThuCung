package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HoaDon {
    private String maHD;
    private String maKH;
    private String maNV;
    private String ngayLap;
    private ArrayList<ChiTietHoaDon> chiTiet = new ArrayList<>();

    public HoaDon() {
    }

    public HoaDon(String maHD, String maKH, String maNV, String ngayLap) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayLap = ngayLap;
    }

    // phuong thuc static
    public static String ngayHienTai() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getNgayLap() { return ngayLap; }
    public void setNgayLap(String ngayLap) { this.ngayLap = ngayLap; }
    public ArrayList<ChiTietHoaDon> getChiTiet() { return chiTiet; }

    public void themChiTiet(ChiTietHoaDon ct) {
        chiTiet.add(ct);
    }

    public double tongTien() {
        double tong = 0;
        for (ChiTietHoaDon ct : chiTiet) {
            tong += ct.thanhTien();
        }
        return tong;
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chiTiet.size(); i++) {
            if (i > 0) sb.append("#");
            sb.append(chiTiet.get(i).toFileString());
        }
        return maHD + "|" + maKH + "|" + maNV + "|" + ngayLap + "|" + sb;
    }

    public void inChiTiet() {
        System.out.println(this);
        for (ChiTietHoaDon ct : chiTiet) {
            System.out.println("      - " + ct);
        }
    }

    @Override
    public String toString() {
        return String.format("%-6s | Ngay: %-10s | KH: %-6s | NV: %-6s | %d dong | TONG: %,.0f d",
                maHD, ngayLap, maKH, maNV, chiTiet.size(), tongTien());
    }
}
