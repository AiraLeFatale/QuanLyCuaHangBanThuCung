package manager;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import entity.KhachHang;
import util.DocGhiFile;


/** Nghiệp vụ danh mục khách hàng. */
public class QuanLyKhachHang extends QuanLyCoBan<KhachHang> {

    public QuanLyKhachHang(DocGhiFile.Kho<KhachHang> kho) {
        super(kho, "KH");
    }

    public List<KhachHang> timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        if (khoa.isEmpty()) {
            return tatCa();
        }
        return danhSach.stream()
                .filter(kh -> kh.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                        || kh.getHoTen().toLowerCase(Locale.ROOT).contains(khoa)
                        || kh.getSoDienThoai().contains(khoa))
                .toList();
    }

    public Optional<KhachHang> timTheoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isBlank()) {
            return Optional.empty();
        }
        return danhSach.stream()
                .filter(kh -> kh.getSoDienThoai().equals(soDienThoai.trim()))
                .findFirst();
    }

    public List<KhachHang> sapXepTheoDiem() {
        return danhSach.stream()
                .sorted(Comparator.comparingInt(KhachHang::getDiemTichLuy).reversed())
                .toList();
    }
}
