package manager;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entity.ChiTietHoaDon;
import entity.DichVu;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;
import entity.ThuCung;
import inter.MatHang;
import util.AppException;
import util.DocGhiFile;


/**
 * Lớp điều phối toàn bộ cửa hàng: gom 6 danh mục và xử lý nghiệp vụ bán hàng
 * (kiểm tra tồn kho, tính tiền, cộng điểm, hủy hóa đơn, lưu dữ liệu) trong một chỗ.
 */
public class CuaHang {

    private final QuanLyThuCung thuCung;
    private final QuanLySanPham sanPham;
    private final QuanLyDichVu dichVu;
    private final QuanLyKhachHang khachHang;
    private final QuanLyNhanVien nhanVien;
    private final QuanLyHoaDon hoaDon;
    private final Path thuMucDuLieu;

    public CuaHang(Path thuMucDuLieu) {
        this.thuMucDuLieu = thuMucDuLieu;
        this.thuCung = new QuanLyThuCung(
                new DocGhiFile.Kho<>(thuMucDuLieu.resolve("thucung.txt"), new DocGhiFile.ThuCungCodec()));
        this.sanPham = new QuanLySanPham(
                new DocGhiFile.Kho<>(thuMucDuLieu.resolve("sanpham.txt"), new DocGhiFile.SanPhamCodec()));
        this.dichVu = new QuanLyDichVu(
                new DocGhiFile.Kho<>(thuMucDuLieu.resolve("dichvu.txt"), new DocGhiFile.DichVuCodec()));
        this.khachHang = new QuanLyKhachHang(
                new DocGhiFile.Kho<>(thuMucDuLieu.resolve("khachhang.txt"), new DocGhiFile.KhachHangCodec()));
        this.nhanVien = new QuanLyNhanVien(
                new DocGhiFile.Kho<>(thuMucDuLieu.resolve("nhanvien.txt"), new DocGhiFile.NhanVienCodec()));
        this.hoaDon = new QuanLyHoaDon(
                new DocGhiFile.Kho<>(thuMucDuLieu.resolve("hoadon.txt"), new DocGhiFile.HoaDonCodec()));
        napTatCa();
    }

    public final void napTatCa() {
        thuCung.napDuLieu();
        sanPham.napDuLieu();
        dichVu.napDuLieu();
        khachHang.napDuLieu();
        nhanVien.napDuLieu();
        hoaDon.napDuLieu();
    }

    public void luuTatCa() {
        thuCung.luu();
        sanPham.luu();
        dichVu.luu();
        khachHang.luu();
        nhanVien.luu();
        hoaDon.luu();
    }

    public QuanLyThuCung getThuCung() {
        return thuCung;
    }

    public QuanLySanPham getSanPham() {
        return sanPham;
    }

    public QuanLyDichVu getDichVu() {
        return dichVu;
    }

    public QuanLyKhachHang getKhachHang() {
        return khachHang;
    }

    public QuanLyNhanVien getNhanVien() {
        return nhanVien;
    }

    public QuanLyHoaDon getHoaDon() {
        return hoaDon;
    }

    public Path getThuMucDuLieu() {
        return thuMucDuLieu;
    }

    /**
     * Tìm mặt hàng theo mã trong cả 3 danh mục (thú cưng, sản phẩm, dịch vụ).
     * Trả về kiểu MatHang nên nơi gọi dùng được đa hình.
     */
    public Optional<MatHang> timMatHang(String ma) {
        if (ma == null || ma.isBlank()) {
            return Optional.empty();
        }
        Optional<ThuCung> tc = thuCung.tim(ma);
        if (tc.isPresent()) {
            return Optional.of(tc.get());
        }
        Optional<SanPham> sp = sanPham.tim(ma);
        if (sp.isPresent()) {
            return Optional.of(sp.get());
        }
        Optional<DichVu> dv = dichVu.tim(ma);
        return dv.map(d -> d);
    }

    /** Tổng giá trị hàng hóa còn trong kho (thú cưng + sản phẩm). */
    public double tongGiaTriTonKho() {
        return thuCung.tongGiaTriTonKho() + sanPham.tongGiaTriTonKho();
    }

    /**
     * Lập hóa đơn cho giỏ hàng: kiểm tra tồn kho, trừ kho, cộng điểm cho khách,
     * lưu hóa đơn và ghi dữ liệu xuống tệp.
     */
    public HoaDon thanhToan(String maKhachHang, String maNhanVien, List<ChiTietHoaDon> gioHang) {
        if (gioHang == null || gioHang.isEmpty()) {
            throw new AppException.NghiepVu("Giỏ hàng đang trống, chưa thể thanh toán");
        }
        KhachHang kh = khachHang.tim(maKhachHang)
                .orElseThrow(() -> new AppException.KhongTimThay("Không tìm thấy khách hàng " + maKhachHang));
        NhanVien nv = nhanVien.tim(maNhanVien)
                .orElseThrow(() -> new AppException.KhongTimThay("Không tìm thấy nhân viên " + maNhanVien));

        // Kiểm tra toàn bộ giỏ trước khi trừ kho để tránh trừ dở dang.
        for (ChiTietHoaDon ct : gioHang) {
            MatHang matHang = batBuocTimMatHang(ct.getMaHang());
            if (!matHang.conBanDuoc()) {
                throw new AppException.NghiepVu("Mặt hàng " + matHang.getTen() + " không còn bán được");
            }
            if (matHang.quanLyTonKho() && ct.getSoLuong() > matHang.getSoLuongTon()) {
                throw new AppException.NghiepVu("Không đủ hàng cho " + matHang.getTen()
                        + " (chỉ còn " + matHang.getSoLuongTon() + ")");
            }
        }

        HoaDon hoaDonMoi = new HoaDon(hoaDon.sinhMa(), LocalDateTime.now(),
                kh.getMa(), kh.getHoTen(), kh.getHang(),
                nv.getMa(), nv.getHoTen(), new ArrayList<>(gioHang));

        for (ChiTietHoaDon ct : gioHang) {
            batBuocTimMatHang(ct.getMaHang()).truKho(ct.getSoLuong());
        }
        kh.congDiem(hoaDonMoi.getDiemCong());
        hoaDon.them(hoaDonMoi);

        luuTatCa();
        return hoaDonMoi;
    }

    /** Sửa hóa đơn: đổi nhân viên lập (các thông tin khác giữ nguyên). */
    public HoaDon capNhatNhanVien(String maHoaDon, String maNhanVien) {
        HoaDon cu = hoaDon.tim(maHoaDon)
                .orElseThrow(() -> new AppException.KhongTimThay("Không tìm thấy hóa đơn " + maHoaDon));
        NhanVien nv = nhanVien.tim(maNhanVien)
                .orElseThrow(() -> new AppException.KhongTimThay("Không tìm thấy nhân viên " + maNhanVien));
        HoaDon moi = new HoaDon(cu.getMa(), cu.getNgayLap(),
                cu.getMaKhachHang(), cu.getTenKhachHang(), cu.getHangKhachHang(),
                nv.getMa(), nv.getHoTen(), cu.getChiTiet());
        hoaDon.capNhat(moi);
        luuTatCa();
        return moi;
    }

    /**
     * Hủy hóa đơn: hoàn lại hàng vào kho, trừ điểm đã cộng cho khách,
     * xóa hóa đơn khỏi danh sách và ghi tệp.
     */
    public void huyHoaDon(String maHoaDon) {
        HoaDon hd = hoaDon.tim(maHoaDon)
                .orElseThrow(() -> new AppException.KhongTimThay("Không tìm thấy hóa đơn " + maHoaDon));
        for (ChiTietHoaDon ct : hd.getChiTiet()) {
            timMatHang(ct.getMaHang()).ifPresent(matHang -> matHang.hoanKho(ct.getSoLuong()));
        }
        khachHang.tim(hd.getMaKhachHang()).ifPresent(kh -> kh.truDiem(hd.getDiemCong()));
        hoaDon.xoa(maHoaDon);
        luuTatCa();
    }

    private MatHang batBuocTimMatHang(String ma) {
        return timMatHang(ma)
                .orElseThrow(() -> new AppException.KhongTimThay("Không tìm thấy mặt hàng " + ma));
    }
}
