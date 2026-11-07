package manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import util.AppException;
import util.DocGhiFile;

/** Quản lý hóa đơn và các thống kê doanh thu. */
public class QuanLyHoaDon {

    private final ArrayList<HoaDon> danhSach = new ArrayList<HoaDon>();
    private final String duongDanTep;

    public QuanLyHoaDon(String duongDanTep) {
        this.duongDanTep = duongDanTep;
    }

    public ArrayList<HoaDon> tatCa() {
        return danhSach;
    }

    public int soLuong() {
        return danhSach.size();
    }

    /** Tìm theo mã; trả về null nếu không thấy. */
    public HoaDon tim(String ma) {
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

    public void them(HoaDon moi) {
        if (tim(moi.getMa()) != null) {
            throw new AppException.NghiepVu("Mã hóa đơn đã tồn tại: " + moi.getMa());
        }
        danhSach.add(moi);
    }

    public void capNhat(HoaDon moi) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(moi.getMa())) {
                danhSach.set(i, moi);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy hóa đơn " + moi.getMa());
    }

    public void xoa(String ma) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa().equalsIgnoreCase(ma)) {
                danhSach.remove(i);
                return;
            }
        }
        throw new AppException.KhongTimThay("Không tìm thấy hóa đơn " + ma);
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
        return String.format("HD%03d", lonNhat + 1);
    }

    public ArrayList<HoaDon> moiNhatTruoc() {
        ArrayList<HoaDon> ketQua = new ArrayList<HoaDon>(danhSach);
        Collections.sort(ketQua, new Comparator<HoaDon>() {
            @Override
            public int compare(HoaDon a, HoaDon b) {
                return b.getNgayLap().compareTo(a.getNgayLap());
            }
        });
        return ketQua;
    }

    public ArrayList<HoaDon> trongKhoang(LocalDate tuNgay, LocalDate denNgay) {
        ArrayList<HoaDon> ketQua = new ArrayList<HoaDon>();
        for (int i = 0; i < danhSach.size(); i++) {
            HoaDon hd = danhSach.get(i);
            LocalDate ngay = hd.getNgayLap().toLocalDate();
            boolean sauTu = tuNgay == null || !ngay.isBefore(tuNgay);
            boolean truocDen = denNgay == null || !ngay.isAfter(denNgay);
            if (sauTu && truocDen) {
                ketQua.add(hd);
            }
        }
        return ketQua;
    }

    public double tongDoanhThu() {
        double tong = 0;
        for (int i = 0; i < danhSach.size(); i++) {
            tong += danhSach.get(i).getTongTien();
        }
        return tong;
    }

    public double tongTienGiam() {
        double tong = 0;
        for (int i = 0; i < danhSach.size(); i++) {
            tong += danhSach.get(i).getTienGiam();
        }
        return tong;
    }

    public int tongSoMatHangDaBan() {
        int tong = 0;
        for (int i = 0; i < danhSach.size(); i++) {
            tong += danhSach.get(i).getTongSoLuong();
        }
        return tong;
    }

    /** Tên mặt hàng bán chạy, sắp xếp giảm dần theo số lượng bán. */
    public List<Map.Entry<String, Integer>> topBanChay(int soLuongCanLay) {
        Map<String, Integer> dem = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < danhSach.size(); i++) {
            HoaDon hd = danhSach.get(i);
            for (int j = 0; j < hd.getChiTiet().size(); j++) {
                ChiTietHoaDon ct = hd.getChiTiet().get(j);
                String khoa = ct.getTenHang() + " (" + ct.getLoaiHang() + ")";
                Integer cu = dem.get(khoa);
                dem.put(khoa, cu == null ? ct.getSoLuong() : cu + ct.getSoLuong());
            }
        }
        ArrayList<Map.Entry<String, Integer>> ketQua =
                new ArrayList<Map.Entry<String, Integer>>(dem.entrySet());
        Collections.sort(ketQua, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return Integer.compare(b.getValue(), a.getValue());
            }
        });
        if (ketQua.size() > soLuongCanLay) {
            return ketQua.subList(0, soLuongCanLay);
        }
        return ketQua;
    }

    /** Doanh thu (sau giảm giá) theo từng loại hàng. */
    public Map<String, Double> doanhThuTheoLoai() {
        Map<String, Double> ketQua = new LinkedHashMap<String, Double>();
        for (int i = 0; i < danhSach.size(); i++) {
            HoaDon hd = danhSach.get(i);
            double tamTinh = hd.getTamTinh();
            double tiLeSauGiam = tamTinh == 0 ? 1 : hd.getTongTien() / tamTinh;
            for (int j = 0; j < hd.getChiTiet().size(); j++) {
                ChiTietHoaDon ct = hd.getChiTiet().get(j);
                Double cu = ketQua.get(ct.getLoaiHang());
                double giaTri = ct.getThanhTien() * tiLeSauGiam;
                ketQua.put(ct.getLoaiHang(), cu == null ? giaTri : cu + giaTri);
            }
        }
        return ketQua;
    }

    public void napDuLieu() {
        danhSach.clear();
        ArrayList<String> cacDong = DocGhiFile.doc(duongDanTep);
        for (int i = 0; i < cacDong.size(); i++) {
            try {
                danhSach.add(HoaDon.fromFileString(cacDong.get(i)));
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
