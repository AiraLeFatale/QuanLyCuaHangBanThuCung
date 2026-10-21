package petshop.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import petshop.io.TextFileRepository;
import petshop.model.DichVu;

/** Nghiệp vụ danh mục dịch vụ: lọc theo loại, tìm kiếm, sắp xếp theo giá. */
public class QuanLyDichVu extends QuanLyCoBan<DichVu> {

    public QuanLyDichVu(TextFileRepository<DichVu> kho) {
        super(kho, "DV");
    }

    public List<DichVu> timKiem(String tuKhoa, String loai) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        boolean locTheoLoai = loai != null && !loai.isBlank() && !loai.equalsIgnoreCase("Tất cả");
        return danhSach.stream()
                .filter(dv -> !locTheoLoai || dv.getLoai().equalsIgnoreCase(loai))
                .filter(dv -> khoa.isEmpty()
                        || dv.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                        || dv.getTen().toLowerCase(Locale.ROOT).contains(khoa)
                        || dv.getDacDiem().toLowerCase(Locale.ROOT).contains(khoa))
                .toList();
    }

    public List<DichVu> sapXepTheoGia(boolean tangDan) {
        Comparator<DichVu> soSanh = Comparator.comparingDouble(DichVu::getGiaBan);
        List<DichVu> ketQua = new ArrayList<>(danhSach);
        ketQua.sort(tangDan ? soSanh : soSanh.reversed());
        return ketQua;
    }
}
