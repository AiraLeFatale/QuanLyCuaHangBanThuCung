package manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import entity.ThuCung;
import util.DocGhiFile;


/** Nghiệp vụ riêng của danh mục thú cưng: lọc, tìm, sắp xếp, trừ kho. */
public class QuanLyThuCung extends QuanLyCoBan<ThuCung> {

    public QuanLyThuCung(DocGhiFile.Kho<ThuCung> kho) {
        super(kho, "TC");
    }

    public List<ThuCung> conBanDuoc() {
        return danhSach.stream().filter(ThuCung::conBanDuoc).toList();
    }

    public List<ThuCung> timKiem(String tuKhoa, String loai) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        boolean locTheoLoai = loai != null && !loai.isBlank() && !loai.equalsIgnoreCase("Tất cả");
        return danhSach.stream()
                .filter(t -> !locTheoLoai || t.getLoai().equalsIgnoreCase(loai))
                .filter(t -> khoa.isEmpty()
                        || t.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                        || t.getTen().toLowerCase(Locale.ROOT).contains(khoa)
                        || t.getDacDiem().toLowerCase(Locale.ROOT).contains(khoa))
                .toList();
    }

    public List<ThuCung> sapXepTheoGia(boolean tangDan) {
        Comparator<ThuCung> soSanh = Comparator.comparingDouble(ThuCung::getGiaBan);
        List<ThuCung> ketQua = new ArrayList<>(danhSach);
        ketQua.sort(tangDan ? soSanh : soSanh.reversed());
        return ketQua;
    }

    /** Tổng giá trị hàng còn trong kho. */
    public double tongGiaTriTonKho() {
        return danhSach.stream()
                .mapToDouble(t -> t.getGiaBan() * t.getSoLuong())
                .sum();
    }

    /** Trừ kho khi bán, tự ném ngoại lệ nếu không đủ hàng. */
    public void truKho(String ma, int soLuongBan) {
        batBuocTim(ma).truKho(soLuongBan);
    }

    /** Số lượng thú cưng theo từng loài (đang bán được). */
    public Map<String, Integer> thongKeTheoLoai() {
        Map<String, Integer> ketQua = new TreeMap<>();
        for (ThuCung t : danhSach) {
            ketQua.merge(t.getLoai(), t.getSoLuong(), Integer::sum);
        }
        return ketQua;
    }
}
