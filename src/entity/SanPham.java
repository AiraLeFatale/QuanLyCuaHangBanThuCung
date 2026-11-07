package entity;

import java.time.LocalDate;
import java.util.Objects;

import inter.MatHang;
import util.AppException;
import util.DocGhiFile;


/**
 * Lớp trừu tượng cho sản phẩm bán kèm: thức ăn, thuốc, phụ kiện.
 * Mỗi lớp con tự mô tả đặc điểm riêng và loại hàng của mình.
 */
public abstract class SanPham implements MatHang {

    /** Thuộc tính static: đếm số đối tượng sản phẩm đã tạo trong phiên chạy. */
    private static int soLuongDaTao = 0;

    private final String ma;
    private String ten;
    private double giaBan;
    private int soLuong;

    protected SanPham(String ma, String ten, double giaBan, int soLuong) {
        this.ma = Objects.requireNonNull(ma, "Mã không được rỗng");
        this.ten = Objects.requireNonNull(ten, "Tên không được rỗng");
        this.giaBan = Math.max(0, giaBan);
        this.soLuong = Math.max(0, soLuong);
        soLuongDaTao++;
    }

    public static int getSoLuongDaTao() {
        return soLuongDaTao;
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = Math.max(0, soLuong);
    }

    @Override
    public int getSoLuongTon() {
        return soLuong;
    }

    @Override
    public boolean conBanDuoc() {
        return soLuong > 0;
    }

    @Override
    public void truKho(int soLuongBan) {
        if (soLuongBan <= 0) {
            throw new AppException.NghiepVu("Số lượng bán phải lớn hơn 0");
        }
        if (soLuongBan > soLuong) {
            throw new AppException.NghiepVu("Không đủ hàng cho " + ten + " (chỉ còn " + soLuong + ")");
        }
        soLuong -= soLuongBan;
    }

    @Override
    public void hoanKho(int soLuongNhap) {
        if (soLuongNhap <= 0) {
            throw new AppException.NghiepVu("Số lượng nhập phải lớn hơn 0");
        }
        soLuong += soLuongNhap;
    }

    @Override
    public abstract String getLoai();

    @Override
    public abstract String getDacDiem();

    @Override
    public boolean quanLyTonKho() {
        return true;
    }

    /** Phần dữ liệu chung; lớp con nối thêm thuộc tính riêng. */
    public String toFileString() {
        return getLoai() + "|" + getMa() + "|" + DocGhiFile.maHoa(getTen()) + "|"
                + Math.round(giaBan) + "|" + soLuong + "|";
    }

    /** Đọc một dòng văn bản thành sản phẩm (Thức ăn, Thuốc hoặc Phụ kiện). */
    public static SanPham fromFileString(String dong) {
        String[] p = DocGhiFile.tach(dong);
        if (p.length < 6) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/6)");
        }
        String loai = p[0];
        String ma = p[1];
        String ten = DocGhiFile.giaiMa(p[2]);
        double gia = Double.parseDouble(p[3]);
        int soLuong = Integer.parseInt(p[4]);

        if (loai.equals("Thức ăn")) {
            double khoiLuong = p.length > 6 ? Double.parseDouble(p[6]) : 0;
            LocalDate hanSuDung = p.length > 7 && !p[7].isEmpty() ? LocalDate.parse(p[7]) : null;
            return new ThucAn(ma, ten, gia, soLuong, DocGhiFile.giaiMa(p[5]), khoiLuong, hanSuDung);
        }
        if (loai.equals("Thuốc")) {
            LocalDate hanSuDung = p.length > 6 && !p[6].isEmpty() ? LocalDate.parse(p[6]) : null;
            boolean canKeDon = p.length > 7 && Boolean.parseBoolean(p[7]);
            return new Thuoc(ma, ten, gia, soLuong, DocGhiFile.giaiMa(p[5]), hanSuDung, canKeDon);
        }
        if (loai.equals("Phụ kiện")) {
            String mauSac = p.length > 6 ? DocGhiFile.giaiMa(p[6]) : "";
            return new PhuKien(ma, ten, gia, soLuong, DocGhiFile.giaiMa(p[5]), mauSac);
        }
        throw new IllegalArgumentException("loại sản phẩm không hợp lệ: " + loai);
    }

    @Override
    public String toString() {
        return ma + " - " + ten + " (" + getLoai() + ")";
    }
}
