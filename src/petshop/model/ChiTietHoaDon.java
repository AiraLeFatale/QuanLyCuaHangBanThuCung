package petshop.model;

import java.util.Objects;

/**
 * Một dòng trong hóa đơn. Thông tin mặt hàng được "chụp lại" tại thời điểm bán
 * (mã, tên, loại, đơn giá) để hóa đơn cũ không bị thay đổi khi dữ liệu gốc đổi.
 */
public class ChiTietHoaDon {

    private final String maHang;
    private final String tenHang;
    private final String loaiHang;
    private final int soLuong;
    private final double donGia;

    public ChiTietHoaDon(String maHang, String tenHang, String loaiHang,
                         int soLuong, double donGia) {
        this.maHang = Objects.requireNonNull(maHang, "Mã hàng không được rỗng");
        this.tenHang = tenHang == null ? "" : tenHang;
        this.loaiHang = loaiHang == null ? "" : loaiHang;
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        this.soLuong = soLuong;
        this.donGia = Math.max(0, donGia);
    }

    /**
     * Tạo dòng hóa đơn từ bất kỳ mặt hàng nào (thú cưng, sản phẩm, dịch vụ).
     * Tham số là interface nên dùng được đa hình.
     */
    public static ChiTietHoaDon tuMatHang(MatHang matHang, int soLuong) {
        return new ChiTietHoaDon(matHang.getMa(), matHang.getTen(),
                matHang.getLoai(), soLuong, matHang.getGiaBan());
    }

    public String getMaHang() {
        return maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public String getLoaiHang() {
        return loaiHang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public double getThanhTien() {
        return donGia * soLuong;
    }

    /** Bản sao với số lượng mới (dùng khi cộng dồn cùng một mặt hàng trong giỏ). */
    public ChiTietHoaDon voiSoLuong(int soLuongMoi) {
        return new ChiTietHoaDon(maHang, tenHang, loaiHang, soLuongMoi, donGia);
    }
}
