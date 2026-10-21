package petshop.service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import petshop.exception.KhongTimThayException;
import petshop.exception.NghiepVuException;
import petshop.io.HoaDonCodec;
import petshop.io.KhachHangCodec;
import petshop.io.NhanVienCodec;
import petshop.io.TextFileRepository;
import petshop.io.ThuCungCodec;
import petshop.model.ChiTietHoaDon;
import petshop.model.HoaDon;
import petshop.model.KhachHang;
import petshop.model.NhanVien;
import petshop.model.ThuCung;

/**
 * Lớp điều phối toàn bộ cửa hàng: gom 4 danh mục và xử lý nghiệp vụ bán hàng
 * (kiểm tra tồn kho, tính tiền, cộng điểm, lưu dữ liệu) trong một chỗ.
 */
public class CuaHang {

    private static final int TIEN_MOI_DIEM = 100_000;

    private final QuanLyThuCung thuCung;
    private final QuanLyKhachHang khachHang;
    private final QuanLyNhanVien nhanVien;
    private final QuanLyHoaDon hoaDon;
    private final Path thuMucDuLieu;

    public CuaHang(Path thuMucDuLieu) {
        this.thuMucDuLieu = thuMucDuLieu;
        this.thuCung = new QuanLyThuCung(
                new TextFileRepository<>(thuMucDuLieu.resolve("thucung.txt"), new ThuCungCodec()));
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
        khachHang.napDuLieu();
        nhanVien.napDuLieu();
        hoaDon.napDuLieu();
    }

    public void luuTatCa() {
        thuCung.luu();
        khachHang.luu();
        nhanVien.luu();
        hoaDon.luu();
    }

    public QuanLyThuCung getThuCung() {
        return thuCung;
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
            ThuCung tc = thuCung.tim(ct.getMaThuCung())
                    .orElseThrow(() -> new KhongTimThayException("Không tìm thấy thú cưng " + ct.getMaThuCung()));
            if (!tc.conBanDuoc()) {
                throw new NghiepVuException("Thú cưng " + tc.getTen() + " không còn bán được");
            }
            if (ct.getSoLuong() > tc.getSoLuong()) {
                throw new NghiepVuException("Không đủ hàng cho " + tc.getTen()
                        + " (chỉ còn " + tc.getSoLuong() + ")");
            }
        }

        HoaDon hoaDonMoi = new HoaDon(hoaDon.sinhMa(), LocalDateTime.now(),
                kh.getMa(), kh.getHoTen(), kh.getHang(),
                nv.getMa(), nv.getHoTen(), new ArrayList<>(gioHang));

        for (ChiTietHoaDon ct : gioHang) {
            thuCung.truKho(ct.getMaThuCung(), ct.getSoLuong());
        }
        kh.congDiem((int) (hoaDonMoi.getTongTien() / TIEN_MOI_DIEM));
        hoaDon.them(hoaDonMoi);

        luuTatCa();
        return hoaDonMoi;
    }
}
