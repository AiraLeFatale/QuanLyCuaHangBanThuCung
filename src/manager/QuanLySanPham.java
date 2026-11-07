package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import entity.SanPham;
import util.AppException;
import util.DocGhiFile;

/** Quản lý danh mục sản phẩm: xem, thêm, sửa, xóa, tìm kiếm, sắp xếp, tồn kho. */
public class QuanLySanPham {

    private final ArrayList<SanPham> danhSach = new ArrayList<SanPham>();
    private final String duongDanTep;

    public QuanLySanPham(String duongDanTep) {
        this.duongDanTep = duongDanTep;
    }

    public ArrayList<SanPham> tatCa() {
        return danhSach;
    }

    public int soLuong() {
        return danhSach.size();
    }

    /** Tìm theo mã; trả về null nếu không thấy. */
    public SanPham tim(String ma) {
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

    public void them(SanPham moi) {
        if (tim(moi.getMa()) != null) {
            throw new AppException.NghiepVu("Mã sản phẩm đã tồn tại: " + moi.getMa());
        }
        danhSach.add(moi);
    }

    public void capNhat(SanPham moi) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(moi.getMa())) {
                danhSach.set(i, moi);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy sản phẩm " + moi.getMa());
    }

    public void xoa(String ma) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(ma)) {
                danhSach.remove(i);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy sản phẩm " + ma);
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
        return String.format("SP%03d", lonNhat + 1);
    }

    public ArrayList<SanPham> conBanDuoc() {
        ArrayList<SanPham> ketQua = new ArrayList<SanPham>();
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).conBanDuoc()) {
                ketQua.add(danhSach.get(i));
            }
        }
        return ketQua;
    }

    public ArrayList<SanPham> timKiem(String tuKhoa, String loai) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        boolean locTheoLoai = loai != null && !loai.trim().isEmpty()
                && !loai.equalsIgnoreCase("Tất cả");
        ArrayList<SanPham> ketQua = new ArrayList<SanPham>();
        for (int i = 0; i < danhSach.size(); i++) {
            SanPham sp = danhSach.get(i);
            if (locTheoLoai && !sp.getLoai().equalsIgnoreCase(loai)) {
                continue;
            }
            if (khoa.isEmpty()
                    || sp.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                    || sp.getTen().toLowerCase(Locale.ROOT).contains(khoa)
                    || sp.getDacDiem().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua.add(sp);
            }
        }
        return ketQua;
    }

    public ArrayList<SanPham> sapXepTheoGia(boolean tangDan) {
        ArrayList<SanPham> ketQua = new ArrayList<SanPham>(danhSach);
        Collections.sort(ketQua, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham a, SanPham b) {
                return Double.compare(a.getGiaBan(), b.getGiaBan());
            }
        });
        if (!tangDan) {
            Collections.reverse(ketQua);
        }
        return ketQua;
    }

    public double tongGiaTriTonKho() {
        double tong = 0;
        for (int i = 0; i < danhSach.size(); i++) {
            SanPham sp = danhSach.get(i);
            tong += sp.getGiaBan() * sp.getSoLuong();
        }
        return tong;
    }

    public void napDuLieu() {
        danhSach.clear();
        ArrayList<String> cacDong = DocGhiFile.doc(duongDanTep);
        for (int i = 0; i < cacDong.size(); i++) {
            try {
                danhSach.add(SanPham.fromFileString(cacDong.get(i)));
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
