package entity;

import java.util.Objects;

import inter.MatHang;
import util.DocGhiFile;

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

    /** Định dạng: maHang~tenHang~loaiHang~soLuong~donGia */
    public String toFileString() {
        return DocGhiFile.maHoa(maHang) + "~" + DocGhiFile.maHoa(tenHang) + "~"
                + DocGhiFile.maHoa(loaiHang) + "~" + soLuong + "~" + Math.round(donGia);
    }

    public static ChiTietHoaDon fromFileString(String dong) {
        String[] p = dong.split("~", -1);
        if (p.length < 5) {
            throw new IllegalArgumentException("chi tiết hóa đơn không hợp lệ: " + dong);
        }
        return new ChiTietHoaDon(DocGhiFile.giaiMa(p[0]), DocGhiFile.giaiMa(p[1]),
                DocGhiFile.giaiMa(p[2]), Integer.parseInt(p[3]), Double.parseDouble(p[4]));
    }
}
