package petshop.model;

import java.time.LocalDate;

/**
 * Khách hàng của cửa hàng. Hạng và chiết khấu không nhập tay mà suy ra từ điểm tích lũy.
 */
public class KhachHang extends Nguoi {

    private int diemTichLuy;

    public KhachHang(String ma, String hoTen, GioiTinh gioiTinh, LocalDate ngaySinh,
                     String soDienThoai, String diaChi, int diemTichLuy) {
        super(ma, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi);
        this.diemTichLuy = Math.max(0, diemTichLuy);
    }

    public KhachHang(String ma, String hoTen, String soDienThoai) {
        this(ma, hoTen, GioiTinh.KHAC, null, soDienThoai, "", 0);
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = Math.max(0, diemTichLuy);
    }

    public void congDiem(int diem) {
        if (diem > 0) {
            this.diemTichLuy += diem;
        }
    }

    public HangKhachHang getHang() {
        return HangKhachHang.tuDiem(diemTichLuy);
    }

    public double getTiLeChietKhau() {
        return getHang().getTiLeChietKhau();
    }

    @Override
    public String getLoaiNguoi() {
        return "Khách hàng";
    }

    @Override
    public String toString() {
        return getMa() + " - " + getHoTen() + (getSoDienThoai().isEmpty() ? "" : " (" + getSoDienThoai() + ")");
    }
}
