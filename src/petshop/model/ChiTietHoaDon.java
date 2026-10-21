package petshop.model;

import java.util.Objects;

/**
 * Một dòng trong hóa đơn. Thông tin thú cưng được "chụp lại" tại thời điểm bán
 * (mã, tên, loại, đơn giá) để hóa đơn cũ không bị thay đổi khi dữ liệu gốc đổi.
 */
public class ChiTietHoaDon {

    private final String maThuCung;
    private final String tenThuCung;
    private final String loaiThuCung;
    private final int soLuong;
    private final double donGia;

    public ChiTietHoaDon(String maThuCung, String tenThuCung, String loaiThuCung,
                         int soLuong, double donGia) {
        this.maThuCung = Objects.requireNonNull(maThuCung, "Mã thú cưng không được rỗng");
        this.tenThuCung = tenThuCung == null ? "" : tenThuCung;
        this.loaiThuCung = loaiThuCung == null ? "" : loaiThuCung;
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        this.soLuong = soLuong;
        this.donGia = Math.max(0, donGia);
    }

    /** Tạo dòng hóa đơn từ thú cưng đang có trong kho. */
    public static ChiTietHoaDon tuThuCung(ThuCung thuCung, int soLuong) {
        return new ChiTietHoaDon(thuCung.getMa(), thuCung.getTen(),
                thuCung.getLoai(), soLuong, thuCung.getGiaBan());
    }

    public String getMaThuCung() {
        return maThuCung;
    }

    public String getTenThuCung() {
        return tenThuCung;
    }

    public String getLoaiThuCung() {
        return loaiThuCung;
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

    /** Bản sao với số lượng mới (dùng khi cộng dồn cùng một thú cưng trong giỏ). */
    public ChiTietHoaDon voiSoLuong(int soLuongMoi) {
        return new ChiTietHoaDon(maThuCung, tenThuCung, loaiThuCung, soLuongMoi, donGia);
    }
}
