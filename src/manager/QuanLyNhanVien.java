package manager;

import java.util.List;
import java.util.Locale;

import entity.NhanVien;
import util.DocGhiFile;


/** Nghiệp vụ danh mục nhân viên. */
public class QuanLyNhanVien extends QuanLyCoBan<NhanVien> {

    public QuanLyNhanVien(DocGhiFile.Kho<NhanVien> kho) {
        super(kho, "NV");
    }

    public List<NhanVien> timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        if (khoa.isEmpty()) {
            return tatCa();
        }
        return danhSach.stream()
                .filter(nv -> nv.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                        || nv.getHoTen().toLowerCase(Locale.ROOT).contains(khoa)
                        || nv.getChucVu().toLowerCase(Locale.ROOT).contains(khoa))
                .toList();
    }

    public double tongLuong() {
        return danhSach.stream().mapToDouble(NhanVien::getLuong).sum();
    }
}
