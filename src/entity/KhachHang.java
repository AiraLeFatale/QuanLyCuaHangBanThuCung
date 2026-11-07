package entity;

import java.time.LocalDate;

import util.DocGhiFile;

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

    /** Trừ điểm khi hủy hóa đơn (không để âm). */
    public void truDiem(int diem) {
        if (diem > 0) {
            this.diemTichLuy = Math.max(0, this.diemTichLuy - diem);
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

    /** Định dạng: ma|hoTen|gioiTinh|ngaySinh|soDienThoai|diaChi|diemTichLuy */
    public String toFileString() {
        return getMa() + "|" + DocGhiFile.maHoa(getHoTen()) + "|" + getGioiTinh().name() + "|"
                + (getNgaySinh() == null ? "" : getNgaySinh().toString()) + "|"
                + DocGhiFile.maHoa(getSoDienThoai()) + "|" + DocGhiFile.maHoa(getDiaChi()) + "|"
                + diemTichLuy;
    }

    public static KhachHang fromFileString(String dong) {
        String[] p = DocGhiFile.tach(dong);
        if (p.length < 7) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/7)");
        }
        LocalDate ngaySinh = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
        return new KhachHang(p[0], DocGhiFile.giaiMa(p[1]), GioiTinh.tuChuoi(p[2]), ngaySinh,
                DocGhiFile.giaiMa(p[4]), DocGhiFile.giaiMa(p[5]), Integer.parseInt(p[6]));
    }

    @Override
    public String toString() {
        return getMa() + " - " + getHoTen() + (getSoDienThoai().isEmpty() ? "" : " (" + getSoDienThoai() + ")");
    }
}
