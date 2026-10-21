package petshop.service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import petshop.exception.KhongTimThayException;
import petshop.exception.NghiepVuException;
import petshop.io.DichVuCodec;
import petshop.io.HoaDonCodec;
import petshop.io.KhachHangCodec;
import petshop.io.NhanVienCodec;
import petshop.io.SanPhamCodec;
import petshop.io.TextFileRepository;
import petshop.io.ThuCungCodec;
import petshop.model.ChiTietHoaDon;
import petshop.model.DichVu;
import petshop.model.HoaDon;
import petshop.model.KhachHang;
import petshop.model.MatHang;
import petshop.model.NhanVien;
import petshop.model.SanPham;
import petshop.model.ThuCung;

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
                new TextFileRepository<>(thuMucDuLieu.resolve("thucung.txt"), new ThuCungCodec()));
        this.sanPham = new QuanLySanPham(
                new TextFileRepository<>(thuMucDuLieu.resolve("sanpham.txt"), new SanPhamCodec()));
        this.dichVu = new QuanLyDichVu(
                new TextFileRepository<>(thuMucDuLieu.resolve("dichvu.txt"), new DichVuCodec()));
        this.khachHang = new QuanLyKhachHang(
                new TextFileRepository<>(thuMucDuLieu.resolve("khachhang.txt"), new KhachHangCodec()));
        this.nhanVien = new QuanLyNhanVien(
                new TextFileRepository<>(thuMucDuLieu.resolve("nhanvien.txt"), new NhanVienCodec()));
        this.hoaDon = new QuanLyHoaDon(
                new TextFileRepository<>(thuMucDuLieu.resolve("hoadon.txt"), new HoaDonCodec()));
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
            throw new NghiepVuException("Giỏ hàng đang trống, chưa thể thanh toán");
        }
        KhachHang kh = khachHang.tim(maKhachHang)
                .orElseThrow(() -> new KhongTimThayException("Không tìm thấy khách hàng " + maKhachHang));
        NhanVien nv = nhanVien.tim(maNhanVien)
                .orElseThrow(() -> new KhongTimThayException("Không tìm thấy nhân viên " + maNhanVien));

        // Kiểm tra toàn bộ giỏ trước khi trừ kho để tránh trừ dở dang.
        for (ChiTietHoaDon ct : gioHang) {
            MatHang matHang = batBuocTimMatHang(ct.getMaHang());
            if (!matHang.conBanDuoc()) {
                throw new NghiepVuException("Mặt hàng " + matHang.getTen() + " không còn bán được");
            }
            if (matHang.quanLyTonKho() && ct.getSoLuong() > matHang.getSoLuongTon()) {
                throw new NghiepVuException("Không đủ hàng cho " + matHang.getTen()
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
                .orElseThrow(() -> new KhongTimThayException("Không tìm thấy hóa đơn " + maHoaDon));
        NhanVien nv = nhanVien.tim(maNhanVien)
                .orElseThrow(() -> new KhongTimThayException("Không tìm thấy nhân viên " + maNhanVien));
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
                .orElseThrow(() -> new KhongTimThayException("Không tìm thấy hóa đơn " + maHoaDon));
        for (ChiTietHoaDon ct : hd.getChiTiet()) {
            timMatHang(ct.getMaHang()).ifPresent(matHang -> matHang.hoanKho(ct.getSoLuong()));
        }
        khachHang.tim(hd.getMaKhachHang()).ifPresent(kh -> kh.truDiem(hd.getDiemCong()));
        hoaDon.xoa(maHoaDon);
        luuTatCa();
    }

    private MatHang batBuocTimMatHang(String ma) {
        return timMatHang(ma)
                .orElseThrow(() -> new KhongTimThayException("Không tìm thấy mặt hàng " + ma));
    }
}
