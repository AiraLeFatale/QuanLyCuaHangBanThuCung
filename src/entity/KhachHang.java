package entity;

import inter.IGiamGia;

public class KhachHang extends Nguoi implements IGiamGia {
    private int diemTichLuy;

    public KhachHang() {
        super();
    }

    public KhachHang(String ma, String hoTen, String sdt, String diaChi, int diemTichLuy) {
        super(ma, hoTen, sdt, diaChi);
        this.diemTichLuy = diemTichLuy;
    }

    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    /** Cu 10.000d mua hang duoc 1 diem. */
    public void congDiem(double tongTien) {
        diemTichLuy += (int) (tongTien / 10000);
    }

    @Override
    public double tinhGiamGia() {
        if (diemTichLuy >= 500) return 10;
        if (diemTichLuy >= 100) return 5;
        return 0;
    }

    @Override
    public String getVaiTro() { return "Khach hang"; }

    @Override
    protected String thongTinRieng() { return String.valueOf(diemTichLuy); }

    @Override
    protected String moTaRieng() { return "Diem: " + diemTichLuy + " (giam " + tinhGiamGia() + "%)"; }
}
