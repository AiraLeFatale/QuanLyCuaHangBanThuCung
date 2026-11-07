package menu;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import entity.HoaDon;
import manager.CuaHang;
import manager.QuanLyHoaDon;
import util.AppException;
import util.Tien;
import util.TrangTri;


/** Menu thống kê - báo cáo trên console. */
public class MenuThongKe {

    private final CuaHang cuaHang;

    public MenuThongKe(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public void chay() {
        while (true) {
            NhapXuat.tieuDe("Thống kê - báo cáo");
            System.out.println("  1. Tổng quan cửa hàng");
            System.out.println("  2. Hàng bán chạy");
            System.out.println("  3. Doanh thu theo loại");
            System.out.println("  4. Hóa đơn gần đây");
            System.out.println("  5. Xuất báo cáo ra tệp .txt");
            System.out.println("  0. Quay lại menu chính");

            int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 5);
            switch (chon) {
                case 1:
                    tongQuan();
                    break;
                case 2:
                    banChay();
                    break;
                case 3:
                    doanhThuTheoLoai();
                    break;
                case 4:
                    hoaDonGanDay();
                    break;
                case 5:
                    xuatBaoCao();
                    break;
                default:
                    return;
            }
        }
    }

    private void tongQuan() {
        QuanLyHoaDon hoaDon = cuaHang.getHoaDon();
        NhapXuat.muc("Tổng quan cửa hàng");
        System.out.println("  Số hóa đơn       : " + hoaDon.soLuong());
        System.out.println("  Doanh thu        : " + Tien.dinhDang(hoaDon.tongDoanhThu()));
        System.out.println("  Tiền giảm giá    : " + Tien.dinhDang(hoaDon.tongTienGiam()));
        System.out.println("  Mặt hàng đã bán  : " + hoaDon.tongSoMatHangDaBan());
        System.out.println("  Khách hàng       : " + cuaHang.getKhachHang().soLuong());
        System.out.println("  Nhân viên        : " + cuaHang.getNhanVien().soLuong());
        System.out.println("  Thú cưng / SP/DV : " + cuaHang.getThuCung().soLuong()
                + " / " + cuaHang.getSanPham().soLuong()
                + " / " + cuaHang.getDichVu().soLuong());
        System.out.println("  Giá trị tồn kho  : " + Tien.dinhDang(cuaHang.tongGiaTriTonKho()));
        NhapXuat.tamDung();
    }

    private void banChay() {
        NhapXuat.muc("Hàng bán chạy");
        java.util.List<Map.Entry<String, Integer>> ds = cuaHang.getHoaDon().topBanChay(10);
        if (ds.isEmpty()) {
            System.out.println("  (chưa có hóa đơn nào)");
        } else {
            java.util.List<String[]> dong = new java.util.ArrayList<>();
            int thuTu = 1;
            for (Map.Entry<String, Integer> e : ds) {
                dong.add(new String[]{String.valueOf(thuTu++), e.getKey(), String.valueOf(e.getValue())});
            }
            NhapXuat.inBang(new String[]{"#", "Mặt hàng", "Số lượng bán"}, dong, new int[]{3, 34, 12});
        }
        NhapXuat.tamDung();
    }

    private void doanhThuTheoLoai() {
        NhapXuat.muc("Doanh thu theo loại hàng");
        Map<String, Double> ds = cuaHang.getHoaDon().doanhThuTheoLoai();
        if (ds.isEmpty()) {
            System.out.println("  (chưa có hóa đơn nào)");
        } else {
            java.util.List<String[]> dong = new java.util.ArrayList<>();
            for (Map.Entry<String, Double> e : ds.entrySet()) {
                dong.add(new String[]{e.getKey(), Tien.dinhDang(e.getValue())});
            }
            NhapXuat.inBang(new String[]{"Loại", "Doanh thu"}, dong, new int[]{8, 16});
        }
        NhapXuat.tamDung();
    }

    private void hoaDonGanDay() {
        NhapXuat.muc("Hóa đơn gần đây");
        java.util.List<String[]> dong = new java.util.ArrayList<>();
        int dem = 0;
        for (HoaDon hd : cuaHang.getHoaDon().moiNhatTruoc()) {
            if (dem++ >= 20) {
                break;
            }
            dong.add(new String[]{
                    hd.getMa(),
                    hd.getNgayLap().format(TrangTri.NGAY_GIO),
                    hd.getTenKhachHang(),
                    hd.getTenNhanVien(),
                    String.valueOf(hd.getTongSoLuong()),
                    Tien.dinhDang(hd.getTongTien())
            });
        }
        NhapXuat.inBang(new String[]{"Mã HĐ", "Ngày lập", "Khách hàng", "Nhân viên", "SL", "Tổng tiền"},
                dong, new int[]{7, 16, 20, 18, 3, 14});
        if (!dong.isEmpty()) {
            System.out.println("  Gợi ý: mở tệp data/hoadon.txt để xem chi tiết từng dòng hàng.");
        }
        NhapXuat.tamDung();
    }

    private void xuatBaoCao() {
        NhapXuat.muc("Xuất báo cáo");
        QuanLyHoaDon hoaDon = cuaHang.getHoaDon();
        try {
            Path thuMuc = Paths.get("reports");
            Files.createDirectories(thuMuc);
            String tenTep = "bao-cao-"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                    + ".txt";
            Path tep = thuMuc.resolve(tenTep);

            String noiDung = HoaDonText.taoBaoCao(
                    "Cửa hàng thú cưng Petshop",
                    hoaDon.soLuong(),
                    hoaDon.tongDoanhThu(),
                    hoaDon.tongTienGiam(),
                    hoaDon.tongSoMatHangDaBan(),
                    cuaHang.tongGiaTriTonKho(),
                    hoaDon.doanhThuTheoLoai(),
                    hoaDon.topBanChay(10));
            Files.write(tep, noiDung.getBytes(StandardCharsets.UTF_8));
            NhapXuat.thongBao("Đã xuất báo cáo: " + tep.toAbsolutePath());
        } catch (IOException ex) {
            NhapXuat.loi("Không ghi được báo cáo: " + ex.getMessage());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }
}
