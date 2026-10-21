package petshop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Hóa đơn bán hàng (bất biến sau khi tạo): tổng tiền, tiền giảm giá
 * và các dòng chi tiết đều được tính từ dữ liệu bên trong.
 */
public class HoaDon implements CoMaKhoa {

    private final String ma;
    private final LocalDateTime ngayLap;
    private final String maKhachHang;
    private final String tenKhachHang;
    private final HangKhachHang hangKhachHang;
    private final String maNhanVien;
    private final String tenNhanVien;
    private final double tiLeGiam;
    private final List<ChiTietHoaDon> chiTiet;

    public HoaDon(String ma, LocalDateTime ngayLap,
                  String maKhachHang, String tenKhachHang, HangKhachHang hangKhachHang,
                  String maNhanVien, String tenNhanVien,
                  List<ChiTietHoaDon> chiTiet) {
        this.ma = Objects.requireNonNull(ma, "Mã hóa đơn không được rỗng");
        this.ngayLap = ngayLap == null ? LocalDateTime.now() : ngayLap;
        this.maKhachHang = maKhachHang == null ? "" : maKhachHang;
        this.tenKhachHang = tenKhachHang == null ? "" : tenKhachHang;
        this.hangKhachHang = hangKhachHang == null ? HangKhachHang.THUONG : hangKhachHang;
        this.maNhanVien = maNhanVien == null ? "" : maNhanVien;
        this.tenNhanVien = tenNhanVien == null ? "" : tenNhanVien;
        this.tiLeGiam = this.hangKhachHang.getTiLeChietKhau();
        this.chiTiet = chiTiet == null
                ? List.of()
                : Collections.unmodifiableList(new ArrayList<>(chiTiet));
    }

    @Override
    public String getMa() {
        return ma;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public HangKhachHang getHangKhachHang() {
        return hangKhachHang;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public double getTiLeGiam() {
        return tiLeGiam;
    }

    public List<ChiTietHoaDon> getChiTiet() {
        return chiTiet;
    }

    public double getTamTinh() {
        return chiTiet.stream().mapToDouble(ChiTietHoaDon::getThanhTien).sum();
    }

    public double getTienGiam() {
        return Math.round(getTamTinh() * tiLeGiam);
    }

    public double getTongTien() {
        return getTamTinh() - getTienGiam();
    }

    public int getTongSoLuong() {
        return chiTiet.stream().mapToInt(ChiTietHoaDon::getSoLuong).sum();
    }

    @Override
    public String toString() {
        return ma + " - " + tenKhachHang;
    }
}
