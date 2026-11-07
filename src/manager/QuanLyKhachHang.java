package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import entity.KhachHang;
import util.AppException;
import util.DocGhiFile;

/** Quản lý danh mục khách hàng. */
public class QuanLyKhachHang {

    private final ArrayList<KhachHang> danhSach = new ArrayList<KhachHang>();
    private final String duongDanTep;

    public QuanLyKhachHang(String duongDanTep) {
        this.duongDanTep = duongDanTep;
    }

    public ArrayList<KhachHang> tatCa() {
        return danhSach;
    }

    public int soLuong() {
        return danhSach.size();
    }

    /** Tìm theo mã; trả về null nếu không thấy. */
    public KhachHang tim(String ma) {
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

    public void them(KhachHang moi) {
        if (tim(moi.getMa()) != null) {
            throw new AppException.NghiepVu("Mã khách hàng đã tồn tại: " + moi.getMa());
        }
        danhSach.add(moi);
    }

    public void capNhat(KhachHang moi) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(moi.getMa())) {
                danhSach.set(i, moi);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy khách hàng " + moi.getMa());
    }

    public void xoa(String ma) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(ma)) {
                danhSach.remove(i);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy khách hàng " + ma);
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
        return String.format("KH%03d", lonNhat + 1);
    }

    public ArrayList<KhachHang> timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        if (khoa.isEmpty()) {
            return tatCa();
        }
        ArrayList<KhachHang> ketQua = new ArrayList<KhachHang>();
        for (int i = 0; i < danhSach.size(); i++) {
            KhachHang kh = danhSach.get(i);
            if (kh.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                    || kh.getHoTen().toLowerCase(Locale.ROOT).contains(khoa)
                    || kh.getSoDienThoai().contains(khoa)) {
                ketQua.add(kh);
            }
        }
        return ketQua;
    }

    /** Tìm khách hàng theo số điện thoại; trả về null nếu không có. */
    public KhachHang timTheoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            return null;
        }
        String sdt = soDienThoai.trim();
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getSoDienThoai().equals(sdt)) {
                return danhSach.get(i);
            }
        }
        return null;
    }

    public ArrayList<KhachHang> sapXepTheoDiem() {
        ArrayList<KhachHang> ketQua = new ArrayList<KhachHang>(danhSach);
        Collections.sort(ketQua, new Comparator<KhachHang>() {
            @Override
            public int compare(KhachHang a, KhachHang b) {
                return Integer.compare(b.getDiemTichLuy(), a.getDiemTichLuy());
            }
        });
        return ketQua;
    }

    public void napDuLieu() {
        danhSach.clear();
        ArrayList<String> cacDong = DocGhiFile.doc(duongDanTep);
        for (int i = 0; i < cacDong.size(); i++) {
            try {
                danhSach.add(KhachHang.fromFileString(cacDong.get(i)));
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
