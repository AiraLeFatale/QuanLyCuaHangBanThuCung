package manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import entity.SanPham;
import util.DocGhiFile;


/** Nghiệp vụ danh mục sản phẩm: lọc theo loại, tìm kiếm, sắp xếp, giá trị tồn kho. */
public class QuanLySanPham extends QuanLyCoBan<SanPham> {

    public QuanLySanPham(DocGhiFile.Kho<SanPham> kho) {
        super(kho, "SP");
    }

    public List<SanPham> conBanDuoc() {
        return danhSach.stream().filter(SanPham::conBanDuoc).toList();
    }

    public List<SanPham> timKiem(String tuKhoa, String loai) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        boolean locTheoLoai = loai != null && !loai.isBlank() && !loai.equalsIgnoreCase("Tất cả");
        return danhSach.stream()
                .filter(sp -> !locTheoLoai || sp.getLoai().equalsIgnoreCase(loai))
                .filter(sp -> khoa.isEmpty()
                        || sp.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                        || sp.getTen().toLowerCase(Locale.ROOT).contains(khoa)
                        || sp.getDacDiem().toLowerCase(Locale.ROOT).contains(khoa))
                .toList();
    }

    public List<SanPham> sapXepTheoGia(boolean tangDan) {
        Comparator<SanPham> soSanh = Comparator.comparingDouble(SanPham::getGiaBan);
        List<SanPham> ketQua = new ArrayList<>(danhSach);
        ketQua.sort(tangDan ? soSanh : soSanh.reversed());
        return ketQua;
    }

    public double tongGiaTriTonKho() {
        return danhSach.stream()
                .mapToDouble(sp -> sp.getGiaBan() * sp.getSoLuong())
                .sum();
    }
}
