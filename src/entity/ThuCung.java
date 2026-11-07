package entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import inter.MatHang;
import util.AppException;
import util.DocGhiFile;


/**
 * Lớp trừu tượng cho thú cưng.
 * Các lớp con (Cho, Meo, Chim) tự quyết định đặc điểm riêng, chi phí chăm sóc...
 */
public abstract class ThuCung implements MatHang {

    /** Thuộc tính static: đếm số đối tượng thú cưng đã được tạo trong phiên chạy. */
    private static int soLuongDaTao = 0;

    private final String ma;
    private String ten;
    private LocalDate ngaySinh;
    private double giaBan;
    private int soLuong;
    private TrangThaiThuCung trangThai;

    protected ThuCung(String ma, String ten, LocalDate ngaySinh, double giaBan,
                      int soLuong, TrangThaiThuCung trangThai) {
        this.ma = Objects.requireNonNull(ma, "Mã không được rỗng");
        this.ten = Objects.requireNonNull(ten, "Tên không được rỗng");
        this.ngaySinh = ngaySinh;
        this.giaBan = Math.max(0, giaBan);
        this.soLuong = Math.max(0, soLuong);
        this.trangThai = trangThai == null ? TrangThaiThuCung.CON_BAN : trangThai;
        soLuongDaTao++;
    }

    /** Phương thức static: số đối tượng thú cưng đã tạo (kể cả nạp từ tệp). */
    public static int getSoLuongDaTao() {
        return soLuongDaTao;
    }

    @Override
    public String getMa() {
        return ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = Objects.requireNonNull(ten, "Tên không được rỗng");
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = Math.max(0, giaBan);
    }

    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public int getSoLuongTon() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = Math.max(0, soLuong);
        if (this.soLuong == 0 && this.trangThai == TrangThaiThuCung.CON_BAN) {
            this.trangThai = TrangThaiThuCung.DA_BAN;
        }
    }

    public TrangThaiThuCung getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiThuCung trangThai) {
        this.trangThai = trangThai == null ? TrangThaiThuCung.CON_BAN : trangThai;
    }

    /** "Chó", "Mèo" hoặc "Chim". */
    public abstract String getLoai();

    /** Mô tả các thuộc tính riêng của từng loài. */
    public abstract String getDacDiem();

    /** Chi phí chăm sóc ước tính mỗi tháng (đồng). */
    public abstract double getChiPhiChamSoc();

    @Override
    public boolean quanLyTonKho() {
        return true;
    }

    /**
     * Chuyển thú cưng thành một dòng văn bản.
     * Lớp con gọi phương thức này rồi nối thêm các thuộc tính riêng.
     */
    public String toFileString() {
        return getLoai() + "|" + getMa() + "|" + DocGhiFile.maHoa(getTen()) + "|"
                + (ngaySinh == null ? "" : ngaySinh.toString()) + "|"
                + Math.round(giaBan) + "|" + soLuong + "|" + trangThai.name() + "|";
    }

    /** Đọc một dòng văn bản thành đối tượng thú cưng (Chó, Mèo hoặc Chim). */
    public static ThuCung fromFileString(String dong) {
        String[] p = DocGhiFile.tach(dong);
        if (p.length < 10) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/10)");
        }
        String loai = p[0];
        String ma = p[1];
        String ten = DocGhiFile.giaiMa(p[2]);
        LocalDate ngaySinh = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
        double giaBan = Double.parseDouble(p[4]);
        int soLuong = Integer.parseInt(p[5]);
        TrangThaiThuCung trangThai = TrangThaiThuCung.valueOf(p[6]);

        if (loai.equals("Chó")) {
            return new Cho(ma, ten, ngaySinh, giaBan, soLuong, trangThai,
                    DocGhiFile.giaiMa(p[7]), DocGhiFile.giaiMa(p[8]), Boolean.parseBoolean(p[9]));
        }
        if (loai.equals("Mèo")) {
            return new Meo(ma, ten, ngaySinh, giaBan, soLuong, trangThai,
                    DocGhiFile.giaiMa(p[7]), Boolean.parseBoolean(p[8]), Boolean.parseBoolean(p[9]));
        }
        if (loai.equals("Chim")) {
            return new Chim(ma, ten, ngaySinh, giaBan, soLuong, trangThai,
                    DocGhiFile.giaiMa(p[7]), Boolean.parseBoolean(p[8]), DocGhiFile.giaiMa(p[9]));
        }
        throw new IllegalArgumentException("loại thú cưng không hợp lệ: " + loai);
    }

    public int getTuoiThang() {
        return ngaySinh == null ? 0 : (int) ChronoUnit.MONTHS.between(ngaySinh, LocalDate.now());
    }

    public boolean conBanDuoc() {
        return trangThai == TrangThaiThuCung.CON_BAN && soLuong > 0;
    }

    /**
     * Trừ số lượng trong kho khi bán. Ném ngoại lệ nếu không đủ hàng
     * hoặc thú cưng không ở trạng thái bán được.
     */
    public void truKho(int soLuongBan) {
        if (soLuongBan <= 0) {
            throw new AppException.NghiepVu("Số lượng bán phải lớn hơn 0");
        }
        if (trangThai != TrangThaiThuCung.CON_BAN) {
            throw new AppException.NghiepVu("Thú cưng " + ten + " đang ở trạng thái \"" + trangThai.getNhan() + "\"");
        }
        if (soLuongBan > soLuong) {
            throw new AppException.NghiepVu("Không đủ hàng cho " + ten + " (chỉ còn " + soLuong + ")");
        }
        soLuong -= soLuongBan;
        if (soLuong == 0) {
            trangThai = TrangThaiThuCung.DA_BAN;
        }
    }

    /** Nhập lại hàng vào kho (dùng khi hủy hóa đơn hoặc kiểm kê). */
    public void hoanKho(int soLuongNhap) {
        if (soLuongNhap <= 0) {
            throw new AppException.NghiepVu("Số lượng nhập phải lớn hơn 0");
        }
        soLuong += soLuongNhap;
        if (trangThai == TrangThaiThuCung.DA_BAN) {
            trangThai = TrangThaiThuCung.CON_BAN;
        }
    }

    @Override
    public String toString() {
        return ma + " - " + ten + " (" + getLoai() + ")";
    }
}
