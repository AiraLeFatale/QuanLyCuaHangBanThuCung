package entity;

/** Lop truu tuong: nguoi (khach hang, nhan vien). */
public abstract class Nguoi {
    protected String ma;
    protected String hoTen;
    protected String sdt;
    protected String diaChi;

    public Nguoi() {
    }

    public Nguoi(String ma, String hoTen, String sdt, String diaChi) {
        this.ma = ma;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
    }

    public String getMa() { return ma; }
    public void setMa(String ma) { this.ma = ma; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public abstract String getVaiTro();
    protected abstract String thongTinRieng();   // de ghi file
    protected abstract String moTaRieng();       // de hien thi

    public String toFileString() {
        return ma + "|" + hoTen + "|" + sdt + "|" + diaChi + "|" + thongTinRieng();
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-10s | %-20s | %-11s | %-22s | %s",
                ma, getVaiTro(), hoTen, sdt, diaChi, moTaRieng());
    }
}
