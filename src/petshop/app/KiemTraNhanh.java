package petshop.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import petshop.exception.AppException;
import petshop.model.ChiTietHoaDon;
import petshop.model.HoaDon;
import petshop.model.KhachHang;
import petshop.model.NhanVien;
import petshop.model.ThuCung;
import petshop.service.CuaHang;
import petshop.util.Tien;

/**
 * Kiểm tra nhanh phần nghiệp vụ và đọc/ghi tệp mà không mở giao diện.
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
                    + " khách hàng=" + cuaHang.getKhachHang().soLuong()
                    + " nhân viên=" + cuaHang.getNhanVien().soLuong());

            KhachHang khach = cuaHang.getKhachHang().tatCa().get(0);
            NhanVien nhanVien = cuaHang.getNhanVien().tatCa().get(0);
            ThuCung thuCung = cuaHang.getThuCung().conBanDuoc().get(0);

            int tonTruoc = thuCung.getSoLuong();
            int diemTruoc = khach.getDiemTichLuy();

            HoaDon hoaDon = cuaHang.thanhToan(khach.getMa(), nhanVien.getMa(),
                    List.of(ChiTietHoaDon.tuThuCung(thuCung, 1)));
            System.out.println("[2] Hóa đơn " + hoaDon.getMa()
                    + " | tạm tính=" + Tien.dinhDang(hoaDon.getTamTinh())
                    + " | giảm=" + Tien.dinhDang(hoaDon.getTienGiam())
                    + " | tổng=" + Tien.dinhDang(hoaDon.getTongTien()));

            ThuCung sauKhiBan = cuaHang.getThuCung().tim(thuCung.getMa()).orElseThrow();
            System.out.println("[3] Tồn kho: " + tonTruoc + " -> " + sauKhiBan.getSoLuong()
                    + " | điểm khách: " + diemTruoc + " -> "
                    + cuaHang.getKhachHang().tim(khach.getMa()).orElseThrow().getDiemTichLuy());

            try {
                cuaHang.thanhToan(khach.getMa(), nhanVien.getMa(),
                        List.of(ChiTietHoaDon.tuThuCung(thuCung, 999)));
                System.out.println("[4] LỖI: đáng lẽ phải báo không đủ hàng");
            } catch (AppException ex) {
                System.out.println("[4] Chặn bán vượt tồn kho đúng: " + ex.getMessage());
            }

            CuaHang docLai = new CuaHang(thuMuc);
            System.out.println("[5] Đọc lại từ tệp: hóa đơn=" + docLai.getHoaDon().soLuong()
                    + " doanh thu=" + Tien.dinhDang(docLai.getHoaDon().tongDoanhThu())
                    + " tồn kho " + thuCung.getMa() + "="
                    + docLai.getThuCung().tim(thuCung.getMa()).orElseThrow().getSoLuong());

            System.out.println("KIỂM TRA THÀNH CÔNG");
        } catch (Exception ex) {
            System.out.println("KIỂM TRA THẤT BẠI: " + ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
