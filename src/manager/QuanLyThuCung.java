package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import entity.ThuCung;
import util.AppException;
import util.DocGhiFile;

/**
 * Quản lý danh mục thú cưng bằng ArrayList.
 * Gồm các thao tác chung (thêm, sửa, xóa, tìm, lưu) và nghiệp vụ riêng (lọc, sắp xếp, tồn kho).
 */
public class QuanLyThuCung {

    private final ArrayList<ThuCung> danhSach = new ArrayList<ThuCung>();
    private final String duongDanTep;

    public QuanLyThuCung(String duongDanTep) {
        this.duongDanTep = duongDanTep;
    }

    public ArrayList<ThuCung> tatCa() {
        return danhSach;
    }

    public int soLuong() {
        return danhSach.size();
    }

    /** Tìm theo mã; trả về null nếu không thấy. */
    public ThuCung tim(String ma) {
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

    public void them(ThuCung moi) {
        if (tim(moi.getMa()) != null) {
            throw new AppException.NghiepVu("Mã thú cưng đã tồn tại: " + moi.getMa());
        }
        danhSach.add(moi);
    }

    public void capNhat(ThuCung moi) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(moi.getMa())) {
                danhSach.set(i, moi);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy thú cưng " + moi.getMa());
    }

    public void xoa(String ma) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(ma)) {
                danhSach.remove(i);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy thú cưng " + ma);
    }

    /** Sinh mã kế tiếp, ví dụ TC001 -> TC002. */
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
        return String.format("TC%03d", lonNhat + 1);
    }

    public ArrayList<ThuCung> conBanDuoc() {
        ArrayList<ThuCung> ketQua = new ArrayList<ThuCung>();
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).conBanDuoc()) {
                ketQua.add(danhSach.get(i));
            }
        }
        return ketQua;
    }

    public ArrayList<ThuCung> timKiem(String tuKhoa, String loai) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        boolean locTheoLoai = loai != null && !loai.trim().isEmpty()
                && !loai.equalsIgnoreCase("Tất cả");
        ArrayList<ThuCung> ketQua = new ArrayList<ThuCung>();
        for (int i = 0; i < danhSach.size(); i++) {
            ThuCung t = danhSach.get(i);
            if (locTheoLoai && !t.getLoai().equalsIgnoreCase(loai)) {
                continue;
            }
            if (khoa.isEmpty()
                    || t.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                    || t.getTen().toLowerCase(Locale.ROOT).contains(khoa)
                    || t.getDacDiem().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua.add(t);
            }
        }
        return ketQua;
    }

    public ArrayList<ThuCung> sapXepTheoGia(boolean tangDan) {
        ArrayList<ThuCung> ketQua = new ArrayList<ThuCung>(danhSach);
        Collections.sort(ketQua, new Comparator<ThuCung>() {
            @Override
            public int compare(ThuCung a, ThuCung b) {
                return Double.compare(a.getGiaBan(), b.getGiaBan());
            }
        });
        if (!tangDan) {
            Collections.reverse(ketQua);
        }
        return ketQua;
    }

    /** Tổng giá trị hàng còn trong kho. */
    public double tongGiaTriTonKho() {
        double tong = 0;
        for (int i = 0; i < danhSach.size(); i++) {
            ThuCung t = danhSach.get(i);
            tong += t.getGiaBan() * t.getSoLuong();
        }
        return tong;
    }

    /** Trừ kho khi bán; ném ngoại lệ nếu không đủ hàng. */
    public void truKho(String ma, int soLuongBan) {
        ThuCung t = tim(ma);
        if (t == null) {
            throw new AppException.KhongTimThay("Không tìm thấy thú cưng " + ma);
        }
        t.truKho(soLuongBan);
    }

    /** Đọc dữ liệu từ tệp vào danh sách. */
    public void napDuLieu() {
        danhSach.clear();
        ArrayList<String> cacDong = DocGhiFile.doc(duongDanTep);
        for (int i = 0; i < cacDong.size(); i++) {
            try {
                danhSach.add(ThuCung.fromFileString(cacDong.get(i)));
            } catch (RuntimeException ex) {
                throw new AppException("Dữ liệu không hợp lệ trong " + duongDanTep
                        + ", dòng " + (i + 1) + ": " + ex.getMessage(), ex);
            }
        }
    }

    /** Ghi toàn bộ danh sách xuống tệp. */
    public void luu() {
        ArrayList<String> cacDong = new ArrayList<String>();
        for (int i = 0; i < danhSach.size(); i++) {
            cacDong.add(danhSach.get(i).toFileString());
        }
        DocGhiFile.ghi(duongDanTep, cacDong);
    }
}
