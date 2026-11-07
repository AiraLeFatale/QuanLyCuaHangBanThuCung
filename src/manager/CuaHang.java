package manager;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import entity.ChiTietHoaDon;
import entity.DichVu;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;
import entity.ThuCung;
import inter.MatHang;
import util.AppException;


/**
 * Lớp điều phối toàn bộ cửa hàng: gom 6 danh mục và xử lý nghiệp vụ bán hàng
 * (kiểm tra tồn kho, tính tiền, cộng điểm, hủy hóa đơn, lưu dữ liệu).
 */
public class CuaHang {

    private final QuanLyThuCung thuCung;
    private final QuanLySanPham sanPham;
    private final QuanLyDichVu dichVu;
    private final QuanLyKhachHang khachHang;
    private final QuanLyNhanVien nhanVien;
    private final QuanLyHoaDon hoaDon;
    private final String thuMucDuLieu;

    public CuaHang(String thuMucDuLieu) {
        this.thuMucDuLieu = thuMucDuLieu;
        this.thuCung = new QuanLyThuCung(duongDan("thucung.txt"));
        this.sanPham = new QuanLySanPham(duongDan("sanpham.txt"));
        this.dichVu = new QuanLyDichVu(duongDan("dichvu.txt"));
        this.khachHang = new QuanLyKhachHang(duongDan("khachhang.txt"));
        this.nhanVien = new QuanLyNhanVien(duongDan("nhanvien.txt"));
        this.hoaDon = new QuanLyHoaDon(duongDan("hoadon.txt"));
        napTatCa();
    }

    private String duongDan(String tenTep) {
        return Paths.get(thuMucDuLieu, tenTep).toString();
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

    public String getThuMucDuLieu() {
        return thuMucDuLieu;
    }

    /**
     * Tìm mặt hàng theo mã trong cả 3 danh mục (thú cưng, sản phẩm, dịch vụ).
     * Trả về null nếu không tìm thấy.
     */
    public MatHang timMatHang(String ma) {
        if (ma == null || ma.trim().isEmpty()) {
            return null;
        }
        ThuCung tc = thuCung.tim(ma);
        if (tc != null) {
            return tc;
        }
        SanPham sp = sanPham.tim(ma);
        if (sp != null) {
            return sp;
        }
        return dichVu.tim(ma);
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
        KhachHang kh = khachHang.tim(maKhachHang);
        if (kh == null) {
            throw new AppException.KhongTimThay("Không tìm thấy khách hàng " + maKhachHang);
        }
        NhanVien nv = nhanVien.tim(maNhanVien);
        if (nv == null) {
            throw new AppException.KhongTimThay("Không tìm thấy nhân viên " + maNhanVien);
        }

        // Gộp số lượng theo từng mã: giỏ có thể có nhiều dòng cùng mặt hàng,
        // phải kiểm tra/trừ kho trên tổng để không bị trừ dở dang.
        Map<String, Integer> soLuongTheoMa = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < gioHang.size(); i++) {
            ChiTietHoaDon ct = gioHang.get(i);
            Integer cu = soLuongTheoMa.get(ct.getMaHang());
            soLuongTheoMa.put(ct.getMaHang(), cu == null ? ct.getSoLuong() : cu + ct.getSoLuong());
        }

        // Kiểm tra toàn bộ giỏ trước khi trừ kho để tránh trừ dở dang.
        for (Map.Entry<String, Integer> dong : soLuongTheoMa.entrySet()) {
            MatHang matHang = batBuocTimMatHang(dong.getKey());
            if (!matHang.conBanDuoc()) {
                throw new AppException.NghiepVu("Mặt hàng " + matHang.getTen() + " không còn bán được");
            }
            if (matHang.quanLyTonKho() && dong.getValue() > matHang.getSoLuongTon()) {
                throw new AppException.NghiepVu("Không đủ hàng cho " + matHang.getTen()
                        + " (chỉ còn " + matHang.getSoLuongTon() + ")");
            }
        }

        HoaDon hoaDonMoi = new HoaDon(hoaDon.sinhMa(), LocalDateTime.now(),
                kh.getMa(), kh.getHoTen(), kh.getHang(),
                nv.getMa(), nv.getHoTen(), new ArrayList<ChiTietHoaDon>(gioHang));

        for (Map.Entry<String, Integer> dong : soLuongTheoMa.entrySet()) {
            batBuocTimMatHang(dong.getKey()).truKho(dong.getValue());
        }
        kh.congDiem(hoaDonMoi.getDiemCong());
        hoaDon.them(hoaDonMoi);

        luuTatCa();
        return hoaDonMoi;
    }

    /** Sửa hóa đơn: đổi nhân viên lập (các thông tin khác giữ nguyên). */
    public HoaDon capNhatNhanVien(String maHoaDon, String maNhanVien) {
        HoaDon cu = hoaDon.tim(maHoaDon);
        if (cu == null) {
            throw new AppException.KhongTimThay("Không tìm thấy hóa đơn " + maHoaDon);
        }
        NhanVien nv = nhanVien.tim(maNhanVien);
        if (nv == null) {
            throw new AppException.KhongTimThay("Không tìm thấy nhân viên " + maNhanVien);
        }
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
        HoaDon hd = hoaDon.tim(maHoaDon);
        if (hd == null) {
            throw new AppException.KhongTimThay("Không tìm thấy hóa đơn " + maHoaDon);
        }
        for (int i = 0; i < hd.getChiTiet().size(); i++) {
            ChiTietHoaDon ct = hd.getChiTiet().get(i);
            MatHang matHang = timMatHang(ct.getMaHang());
            if (matHang != null) {
                matHang.hoanKho(ct.getSoLuong());
            }
        }
        KhachHang kh = khachHang.tim(hd.getMaKhachHang());
        if (kh != null) {
            kh.truDiem(hd.getDiemCong());
        }
        hoaDon.xoa(maHoaDon);
        luuTatCa();
    }

    private MatHang batBuocTimMatHang(String ma) {
        MatHang matHang = timMatHang(ma);
        if (matHang == null) {
            throw new AppException.KhongTimThay("Không tìm thấy mặt hàng " + ma);
        }
        return matHang;
    }
}
