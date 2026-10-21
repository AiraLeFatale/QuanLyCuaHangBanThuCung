package petshop.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import petshop.io.TextFileRepository;
import petshop.model.ChiTietHoaDon;
import petshop.model.HoaDon;

/** Nghiệp vụ hóa đơn: doanh thu, thống kê, lọc theo khoảng ngày. */
public class QuanLyHoaDon extends QuanLyCoBan<HoaDon> {

    public QuanLyHoaDon(TextFileRepository<HoaDon> kho) {
        super(kho, "HD");
    }

    public List<HoaDon> moiNhatTruoc() {
        return danhSach.stream()
                .sorted(Comparator.comparing(HoaDon::getNgayLap).reversed())
                .toList();
    }

    public List<HoaDon> trongKhoang(LocalDate tuNgay, LocalDate denNgay) {
        return danhSach.stream()
                .filter(hd -> {
                    LocalDate ngay = hd.getNgayLap().toLocalDate();
                    boolean sauTu = tuNgay == null || !ngay.isBefore(tuNgay);
                    boolean truocDen = denNgay == null || !ngay.isAfter(denNgay);
                    return sauTu && truocDen;
                })
                .toList();
    }

    public double tongDoanhThu() {
        return danhSach.stream().mapToDouble(HoaDon::getTongTien).sum();
    }

    public double tongTienGiam() {
        return danhSach.stream().mapToDouble(HoaDon::getTienGiam).sum();
    }

    public int tongSoThuCungDaBan() {
        return danhSach.stream().mapToInt(HoaDon::getTongSoLuong).sum();
    }

    /** Tên thú cưng bán chạy nhất, sắp xếp giảm dần theo số lượng bán. */
    public List<Map.Entry<String, Integer>> topBanChay(int soLuongCanLay) {
        Map<String, Integer> dem = new LinkedHashMap<>();
        for (HoaDon hd : danhSach) {
            for (ChiTietHoaDon ct : hd.getChiTiet()) {
                dem.merge(ct.getTenThuCung() + " (" + ct.getLoaiThuCung() + ")",
                        ct.getSoLuong(), Integer::sum);
            }
        }
        List<Map.Entry<String, Integer>> ketQua = new ArrayList<>(dem.entrySet());
        ketQua.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        return ketQua.size() > soLuongCanLay ? ketQua.subList(0, soLuongCanLay) : ketQua;
    }

    /** Doanh thu (sau giảm giá) theo từng loại thú cưng. */
    public Map<String, Double> doanhThuTheoLoai() {
        Map<String, Double> ketQua = new LinkedHashMap<>();
        for (HoaDon hd : danhSach) {
            for (ChiTietHoaDon ct : hd.getChiTiet()) {
                ketQua.merge(ct.getLoaiThuCung(), ct.getThanhTien(), Double::sum);
            }
        }
        return ketQua;
    }
}
