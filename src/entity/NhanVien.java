package entity;

import java.time.LocalDate;

import util.DocGhiFile;

/** Nhân viên của cửa hàng. */
public class NhanVien extends Nguoi {

    private String chucVu;
    private double luong;
    private LocalDate ngayVaoLam;

    public NhanVien(String ma, String hoTen, GioiTinh gioiTinh, LocalDate ngaySinh,
                    String soDienThoai, String diaChi, String chucVu, double luong, LocalDate ngayVaoLam) {
        super(ma, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi);
        this.chucVu = chucVu == null ? "" : chucVu;
        this.luong = Math.max(0, luong);
        this.ngayVaoLam = ngayVaoLam;
    }

    public NhanVien(String ma, String hoTen, String chucVu, double luong) {
        this(ma, hoTen, GioiTinh.KHAC, null, "", "", chucVu, luong, null);
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu == null ? "" : chucVu;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = Math.max(0, luong);
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    @Override
    public String getLoaiNguoi() {
        return "Nhân viên";
    }

    /** Định dạng: ma|hoTen|gioiTinh|ngaySinh|soDienThoai|diaChi|chucVu|luong|ngayVaoLam */
    public String toFileString() {
        return getMa() + "|" + DocGhiFile.maHoa(getHoTen()) + "|" + getGioiTinh().name() + "|"
                + (getNgaySinh() == null ? "" : getNgaySinh().toString()) + "|"
                + DocGhiFile.maHoa(getSoDienThoai()) + "|" + DocGhiFile.maHoa(getDiaChi()) + "|"
                + DocGhiFile.maHoa(chucVu) + "|" + Math.round(luong) + "|"
                + (ngayVaoLam == null ? "" : ngayVaoLam.toString());
    }

    public static NhanVien fromFileString(String dong) {
        String[] p = DocGhiFile.tach(dong);
        if (p.length < 9) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/9)");
        }
        LocalDate ngaySinh = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
        LocalDate ngayVaoLam = p[8].isEmpty() ? null : LocalDate.parse(p[8]);
        return new NhanVien(p[0], DocGhiFile.giaiMa(p[1]), GioiTinh.tuChuoi(p[2]), ngaySinh,
                DocGhiFile.giaiMa(p[4]), DocGhiFile.giaiMa(p[5]), DocGhiFile.giaiMa(p[6]),
                Double.parseDouble(p[7]), ngayVaoLam);
    }

    @Override
    public String toString() {
        return getMa() + " - " + getHoTen() + (chucVu.isEmpty() ? "" : " (" + chucVu + ")");
    }
}
