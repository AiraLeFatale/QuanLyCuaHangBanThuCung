package petshop.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import petshop.exception.AppException;
import petshop.model.ChiTietHoaDon;
import petshop.model.DichVu;
import petshop.model.HoaDon;
import petshop.model.KhachHang;
import petshop.model.NhanVien;
import petshop.model.SanPham;
import petshop.model.ThuCung;
import petshop.service.CuaHang;
import petshop.util.Tien;

/**
 * Kiểm tra nhanh phần nghiệp vụ và đọc/ghi tệp mà không cần nhập liệu.
 * Chạy:  run.bat --check
 */
final class KiemTraNhanh {

    private KiemTraNhanh() {
    }

    static void chay() {
        try {
            Path thuMuc = Files.createTempDirectory("petshop-check");
            System.out.println("Thư mục kiểm tra: " + thuMuc);

            CuaHang cuaHang = new CuaHang(thuMuc);
            boolean coDuLieuMau = DuLieuMau.napNeuTrong(cuaHang);
            System.out.println("[1] Dữ liệu mẫu: " + coDuLieuMau
                    + " | thú cưng=" + cuaHang.getThuCung().soLuong()
                    + " sản phẩm=" + cuaHang.getSanPham().soLuong()
                    + " dịch vụ=" + cuaHang.getDichVu().soLuong()
                    + " khách hàng=" + cuaHang.getKhachHang().soLuong()
                    + " nhân viên=" + cuaHang.getNhanVien().soLuong());

            KhachHang khach = cuaHang.getKhachHang().tatCa().get(0);
            NhanVien nhanVien = cuaHang.getNhanVien().tatCa().get(0);
            ThuCung thuCung = cuaHang.getThuCung().conBanDuoc().get(0);
            SanPham sanPham = cuaHang.getSanPham().conBanDuoc().get(0);
            DichVu dichVu = cuaHang.getDichVu().tatCa().get(0);

            int tonThuCungTruoc = thuCung.getSoLuong();
            int tonSanPhamTruoc = sanPham.getSoLuong();
            int diemTruoc = khach.getDiemTichLuy();

            HoaDon hoaDon = cuaHang.thanhToan(khach.getMa(), nhanVien.getMa(), List.of(
                    ChiTietHoaDon.tuMatHang(thuCung, 1),
                    ChiTietHoaDon.tuMatHang(sanPham, 2),
                    ChiTietHoaDon.tuMatHang(dichVu, 1)));
            System.out.println("[2] Hóa đơn " + hoaDon.getMa()
                    + " | " + hoaDon.getTongSoLuong() + " mặt hàng"
                    + " | tạm tính=" + Tien.dinhDang(hoaDon.getTamTinh())
                    + " | giảm=" + Tien.dinhDang(hoaDon.getTienGiam())
                    + " | tổng=" + Tien.dinhDang(hoaDon.getTongTien()));

            ThuCung thuCungSauBan = cuaHang.getThuCung().tim(thuCung.getMa()).orElseThrow();
            SanPham sanPhamSauBan = cuaHang.getSanPham().tim(sanPham.getMa()).orElseThrow();
            System.out.println("[3] Tồn kho: thú cưng " + tonThuCungTruoc + " -> " + thuCungSauBan.getSoLuong()
                    + ", sản phẩm " + tonSanPhamTruoc + " -> " + sanPhamSauBan.getSoLuong()
                    + " | điểm khách: " + diemTruoc + " -> "
                    + cuaHang.getKhachHang().tim(khach.getMa()).orElseThrow().getDiemTichLuy());

            try {
                cuaHang.thanhToan(khach.getMa(), nhanVien.getMa(),
                        List.of(ChiTietHoaDon.tuMatHang(thuCung, 999)));
                System.out.println("[4] LỖI: đáng lẽ phải báo không đủ hàng");
            } catch (AppException ex) {
                System.out.println("[4] Chặn bán vượt tồn kho đúng: " + ex.getMessage());
            }

            cuaHang.huyHoaDon(hoaDon.getMa());
            System.out.println("[5] Hủy hóa đơn " + hoaDon.getMa()
                    + " | tồn kho phục hồi: thú cưng="
                    + cuaHang.getThuCung().tim(thuCung.getMa()).orElseThrow().getSoLuong()
                    + ", sản phẩm=" + cuaHang.getSanPham().tim(sanPham.getMa()).orElseThrow().getSoLuong()
                    + " | điểm khách=" + cuaHang.getKhachHang().tim(khach.getMa()).orElseThrow().getDiemTichLuy()
                    + " | số hóa đơn=" + cuaHang.getHoaDon().soLuong());

            CuaHang docLai = new CuaHang(thuMuc);
            System.out.println("[6] Đọc lại từ tệp: hóa đơn=" + docLai.getHoaDon().soLuong()
                    + " doanh thu=" + Tien.dinhDang(docLai.getHoaDon().tongDoanhThu())
                    + " sản phẩm=" + docLai.getSanPham().soLuong()
                    + " dịch vụ=" + docLai.getDichVu().soLuong()
                    + " tồn kho " + thuCung.getMa() + "="
                    + docLai.getThuCung().tim(thuCung.getMa()).orElseThrow().getSoLuong());

            System.out.println("[7] Thuộc tính static: số đối tượng thú cưng đã tạo = "
                    + ThuCung.getSoLuongDaTao() + ", sản phẩm = " + SanPham.getSoLuongDaTao());

            System.out.println("KIỂM TRA THÀNH CÔNG");
        } catch (Exception ex) {
            System.out.println("KIỂM TRA THẤT BẠI: " + ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
