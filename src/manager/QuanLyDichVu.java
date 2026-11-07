package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import entity.DichVu;
import util.AppException;
import util.DocGhiFile;

/** Quản lý danh mục dịch vụ: xem, thêm, sửa, xóa, tìm kiếm, sắp xếp theo giá. */
public class QuanLyDichVu {

    private final ArrayList<DichVu> danhSach = new ArrayList<DichVu>();
    private final String duongDanTep;

    public QuanLyDichVu(String duongDanTep) {
        this.duongDanTep = duongDanTep;
    }

    public ArrayList<DichVu> tatCa() {
        return danhSach;
    }

    public int soLuong() {
        return danhSach.size();
    }

    /** Tìm theo mã; trả về null nếu không thấy. */
    public DichVu tim(String ma) {
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

    public void them(DichVu moi) {
        if (tim(moi.getMa()) != null) {
            throw new AppException.NghiepVu("Mã dịch vụ đã tồn tại: " + moi.getMa());
        }
        danhSach.add(moi);
    }

    public void capNhat(DichVu moi) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(moi.getMa())) {
                danhSach.set(i, moi);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy dịch vụ " + moi.getMa());
    }

    public void xoa(String ma) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(ma)) {
                danhSach.remove(i);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy dịch vụ " + ma);
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
        return String.format("DV%03d", lonNhat + 1);
    }

    public ArrayList<DichVu> timKiem(String tuKhoa, String loai) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        boolean locTheoLoai = loai != null && !loai.trim().isEmpty()
                && !loai.equalsIgnoreCase("Tất cả");
        ArrayList<DichVu> ketQua = new ArrayList<DichVu>();
        for (int i = 0; i < danhSach.size(); i++) {
            DichVu dv = danhSach.get(i);
            if (locTheoLoai && !dv.getLoai().equalsIgnoreCase(loai)) {
                continue;
            }
            if (khoa.isEmpty()
                    || dv.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                    || dv.getTen().toLowerCase(Locale.ROOT).contains(khoa)
                    || dv.getDacDiem().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua.add(dv);
            }
        }
        return ketQua;
    }

    public ArrayList<DichVu> sapXepTheoGia(boolean tangDan) {
        ArrayList<DichVu> ketQua = new ArrayList<DichVu>(danhSach);
        Collections.sort(ketQua, new Comparator<DichVu>() {
            @Override
            public int compare(DichVu a, DichVu b) {
                return Double.compare(a.getGiaBan(), b.getGiaBan());
            }
        });
        if (!tangDan) {
            Collections.reverse(ketQua);
        }
        return ketQua;
    }

    public void napDuLieu() {
        danhSach.clear();
        ArrayList<String> cacDong = DocGhiFile.doc(duongDanTep);
        for (int i = 0; i < cacDong.size(); i++) {
            try {
                danhSach.add(DichVu.fromFileString(cacDong.get(i)));
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
