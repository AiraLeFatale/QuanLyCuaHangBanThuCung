package util;

import java.util.Map;

import entity.ChiTietHoaDon;
import entity.HoaDon;


/** Tạo nội dung hóa đơn dạng văn bản (dùng cho hộp thoại và xuất báo cáo). */
public final class HoaDonText {

    private HoaDonText() {
    }

    public static String tao(HoaDon hd) {
        StringBuilder sb = new StringBuilder();
        sb.append("CỬA HÀNG THÚ CƯNG PETSHOP\n");
        sb.append("HÓA ĐƠN BÁN HÀNG\n");
        sb.append("------------------------------------------\n");
        sb.append("Mã hóa đơn : ").append(hd.getMa()).append('\n');
        sb.append("Ngày lập   : ").append(hd.getNgayLap().format(TrangTri.NGAY_GIO)).append('\n');
        sb.append("Khách hàng : ").append(hd.getTenKhachHang())
                .append(" (").append(hd.getMaKhachHang()).append(")\n");
        sb.append("Hạng       : ").append(hd.getHangKhachHang().getNhan())
                .append(" - giảm ").append(TrangTri.phanTram(hd.getTiLeGiam())).append('\n');
        sb.append("Nhân viên  : ").append(hd.getTenNhanVien())
                .append(" (").append(hd.getMaNhanVien()).append(")\n");
        sb.append("------------------------------------------\n");
        sb.append(String.format("%-32s %5s %14s%n", "Mặt hàng", "SL", "Thành tiền"));
        for (ChiTietHoaDon ct : hd.getChiTiet()) {
            sb.append(String.format("%-32s %5d %14s%n",
                    cat(ct.getTenHang() + " (" + ct.getLoaiHang() + ")", 32),
                    ct.getSoLuong(),
                    Tien.dinhDang(ct.getThanhTien())));
        }
        sb.append("------------------------------------------\n");
        sb.append(String.format("%-32s %14s%n", "Tạm tính", Tien.dinhDang(hd.getTamTinh())));
        sb.append(String.format("%-32s %14s%n", "Giảm giá", "-" + Tien.dinhDang(hd.getTienGiam())));
        sb.append(String.format("%-32s %14s%n", "TỔNG CỘNG", Tien.dinhDang(hd.getTongTien())));
        sb.append("------------------------------------------\n");
        sb.append("Cảm ơn quý khách đã mua hàng!\n");
        return sb.toString();
    }

    /** Báo cáo thống kê tổng hợp. */
    public static String taoBaoCao(String tenCuaHang, int soHoaDon, double doanhThu,
                                   double tienGiam, int soThuCung, double giaTriTonKho,
                                   Map<String, Double> doanhThuTheoLoai,
                                   java.util.List<Map.Entry<String, Integer>> banChay) {
        StringBuilder sb = new StringBuilder();
        sb.append("BÁO CÁO THỐNG KÊ - ").append(tenCuaHang).append('\n');
        sb.append("Lập lúc: ").append(java.time.LocalDateTime.now().format(TrangTri.NGAY_GIO)).append('\n');
        sb.append("==========================================\n");
        sb.append("Số hóa đơn        : ").append(soHoaDon).append('\n');
        sb.append("Doanh thu         : ").append(Tien.dinhDang(doanhThu)).append('\n');
        sb.append("Tiền giảm giá     : ").append(Tien.dinhDang(tienGiam)).append('\n');
        sb.append("Mặt hàng đã bán  : ").append(soThuCung).append('\n');
        sb.append("Giá trị tồn kho   : ").append(Tien.dinhDang(giaTriTonKho)).append('\n');
        sb.append("==========================================\n");
        sb.append("Doanh thu theo loại:\n");
        for (Map.Entry<String, Double> e : doanhThuTheoLoai.entrySet()) {
            sb.append("  - ").append(e.getKey()).append(": ").append(Tien.dinhDang(e.getValue())).append('\n');
        }
        sb.append("------------------------------------------\n");
        sb.append("Hàng bán chạy:\n");
        if (banChay.isEmpty()) {
            sb.append("  (chưa có dữ liệu)\n");
        } else {
            int thuTu = 1;
            for (Map.Entry<String, Integer> e : banChay) {
                sb.append("  ").append(thuTu++).append(". ").append(e.getKey())
                        .append(": ").append(e.getValue()).append(" lượt\n");
            }
        }
        return sb.toString();
    }

    private static String cat(String chuoi, int doDai) {
        return chuoi.length() <= doDai ? chuoi : chuoi.substring(0, doDai - 1) + "…";
    }
}
