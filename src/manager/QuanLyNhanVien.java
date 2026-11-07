package manager;

import java.util.ArrayList;
import java.util.Locale;

import entity.NhanVien;
import util.AppException;
import util.DocGhiFile;

/** Quản lý danh mục nhân viên. */
public class QuanLyNhanVien {

    private final ArrayList<NhanVien> danhSach = new ArrayList<NhanVien>();
    private final String duongDanTep;

    public QuanLyNhanVien(String duongDanTep) {
        this.duongDanTep = duongDanTep;
    }

    public ArrayList<NhanVien> tatCa() {
        return danhSach;
    }

    public int soLuong() {
        return danhSach.size();
    }

    /** Tìm theo mã; trả về null nếu không thấy. */
    public NhanVien tim(String ma) {
        if (ma == null) {
            return null;
        }
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(ma.trim())) {
                return danhSach.get(i);
            }
        }
        return null;
    }

    public void them(NhanVien moi) {
        if (tim(moi.getMa()) != null) {
            throw new AppException.NghiepVu("Mã nhân viên đã tồn tại: " + moi.getMa());
        }
        danhSach.add(moi);
    }

    public void capNhat(NhanVien moi) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(moi.getMa())) {
                danhSach.set(i, moi);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy nhân viên " + moi.getMa());
    }

    public void xoa(String ma) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(ma)) {
                danhSach.remove(i);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy nhân viên " + ma);
    }

    public String sinhMa() {
        int lonNhat = 0;
        for (int i = 0; i < danhSach.size(); i++) {
            String ma = danhSach.get(i).getMa();
            if (ma.length() > 2) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > lonNhat) {
                        lonNhat = so;
                    }
                } catch (NumberFormatException ignored) {
                    // bỏ qua mã không đúng dạng số
                }
            }
        }
        return String.format("NV%03d", lonNhat + 1);
    }

    public ArrayList<NhanVien> timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        if (khoa.isEmpty()) {
            return tatCa();
        }
        ArrayList<NhanVien> ketQua = new ArrayList<NhanVien>();
        for (int i = 0; i < danhSach.size(); i++) {
            NhanVien nv = danhSach.get(i);
            if (nv.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                    || nv.getHoTen().toLowerCase(Locale.ROOT).contains(khoa)
                    || nv.getChucVu().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }

    public double tongLuong() {
        double tong = 0;
        for (int i = 0; i < danhSach.size(); i++) {
            tong += danhSach.get(i).getLuong();
        }
        return tong;
    }

    public void napDuLieu() {
        danhSach.clear();
        ArrayList<String> cacDong = DocGhiFile.doc(duongDanTep);
        for (int i = 0; i < cacDong.size(); i++) {
            try {
                danhSach.add(NhanVien.fromFileString(cacDong.get(i)));
            } catch (RuntimeException ex) {
                throw new AppException("Dữ liệu không hợp lệ trong " + duongDanTep
                        + ", dòng " + (i + 1) + ": " + ex.getMessage(), ex);
            }
        }
    }

    public void luu() {
        ArrayList<String> cacDong = new ArrayList<String>();
        for (int i = 0; i < danhSach.size(); i++) {
            cacDong.add(danhSach.get(i).toFileString());
        }
        DocGhiFile.ghi(duongDanTep, cacDong);
    }
}
