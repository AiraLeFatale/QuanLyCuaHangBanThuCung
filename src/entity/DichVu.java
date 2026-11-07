package entity;

import java.util.Objects;

import inter.MatHang;
import util.AppException;
import util.DocGhiFile;


/**
 * Lớp trừu tượng cho dịch vụ chăm sóc thú cưng.
 * Dịch vụ không quản lý tồn kho: bán bao nhiêu lần cũng được.
 */
public abstract class DichVu implements MatHang {

    private final String ma;
    private String ten;
    private double giaBan;
    private int thoiGianPhut;

    protected DichVu(String ma, String ten, double giaBan, int thoiGianPhut) {
        this.ma = Objects.requireNonNull(ma, "Mã không được rỗng");
        this.ten = Objects.requireNonNull(ten, "Tên không được rỗng");
        this.giaBan = Math.max(0, giaBan);
        this.thoiGianPhut = Math.max(0, thoiGianPhut);
    }

    @Override
    public String getMa() {
        return ma;
    }

    @Override
    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = Objects.requireNonNull(ten, "Tên không được rỗng");
    }

    @Override
    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = Math.max(0, giaBan);
    }

    public int getThoiGianPhut() {
        return thoiGianPhut;
    }

    public void setThoiGianPhut(int thoiGianPhut) {
        this.thoiGianPhut = Math.max(0, thoiGianPhut);
    }

    /** Dịch vụ không có tồn kho. */
    @Override
    public int getSoLuongTon() {
        return -1;
    }

    @Override
    public boolean conBanDuoc() {
        return true;
    }

    @Override
    public void truKho(int soLuong) {
        if (soLuong <= 0) {
            throw new AppException.NghiepVu("Số lượng phải lớn hơn 0");
        }
    }

    @Override
    public void hoanKho(int soLuong) {
        if (soLuong <= 0) {
            throw new AppException.NghiepVu("Số lượng phải lớn hơn 0");
        }
    }

    @Override
    public abstract String getLoai();

    @Override
    public abstract String getDacDiem();

    @Override
    public boolean quanLyTonKho() {
        return false;
    }

    /** Phần dữ liệu chung; lớp con nối thêm thuộc tính riêng. */
    public String toFileString() {
        return getLoai() + "|" + getMa() + "|" + DocGhiFile.maHoa(getTen()) + "|"
                + Math.round(giaBan) + "|" + thoiGianPhut + "|";
    }

    /** Đọc một dòng văn bản thành dịch vụ (Tắm & sấy, Cắt tỉa hoặc Tiêm phòng). */
    public static DichVu fromFileString(String dong) {
        String[] p = DocGhiFile.tach(dong);
        if (p.length < 6) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/6)");
        }
        String loai = p[0];
        String ma = p[1];
        String ten = DocGhiFile.giaiMa(p[2]);
        double gia = Double.parseDouble(p[3]);
        int thoiGian = Integer.parseInt(p[4]);
        String rieng = DocGhiFile.giaiMa(p[5]);

        if (loai.equals("Tắm & sấy")) {
            return new DichVuTam(ma, ten, gia, thoiGian, rieng);
        }
        if (loai.equals("Cắt tỉa")) {
            return new DichVuCatTia(ma, ten, gia, thoiGian, rieng);
        }
        if (loai.equals("Tiêm phòng")) {
            return new DichVuTiemPhong(ma, ten, gia, thoiGian, rieng);
        }
        throw new IllegalArgumentException("loại dịch vụ không hợp lệ: " + loai);
    }

    @Override
    public String toString() {
        return ma + " - " + ten + " (" + getLoai() + ")";
    }
}
