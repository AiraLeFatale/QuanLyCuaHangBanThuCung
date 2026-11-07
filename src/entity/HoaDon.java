package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import inter.CoMaKhoa;
import util.DocGhiFile;

/**
 * Hóa đơn bán hàng (bất biến sau khi tạo): tổng tiền, tiền giảm giá
 * và các dòng chi tiết đều được tính từ dữ liệu bên trong.
 */
public class HoaDon implements CoMaKhoa {

    /** Quy tắc tích điểm: cứ 100.000 đ được 1 điểm. */
    public static final int TIEN_MOI_DIEM = 100_000;

    private final String ma;
    private final LocalDateTime ngayLap;
    private final String maKhachHang;
    private final String tenKhachHang;
    private final HangKhachHang hangKhachHang;
    private final String maNhanVien;
    private final String tenNhanVien;
    private final double tiLeGiam;
    private final List<ChiTietHoaDon> chiTiet;

    public HoaDon(String ma, LocalDateTime ngayLap,
                  String maKhachHang, String tenKhachHang, HangKhachHang hangKhachHang,
                  String maNhanVien, String tenNhanVien,
                  List<ChiTietHoaDon> chiTiet) {
        this.ma = Objects.requireNonNull(ma, "Mã hóa đơn không được rỗng");
        this.ngayLap = ngayLap == null ? LocalDateTime.now() : ngayLap;
        this.maKhachHang = maKhachHang == null ? "" : maKhachHang;
        this.tenKhachHang = tenKhachHang == null ? "" : tenKhachHang;
        this.hangKhachHang = hangKhachHang == null ? HangKhachHang.THUONG : hangKhachHang;
        this.maNhanVien = maNhanVien == null ? "" : maNhanVien;
        this.tenNhanVien = tenNhanVien == null ? "" : tenNhanVien;
        this.tiLeGiam = this.hangKhachHang.getTiLeChietKhau();
        if (chiTiet == null) {
            this.chiTiet = Collections.unmodifiableList(new ArrayList<ChiTietHoaDon>());
        } else {
            this.chiTiet = Collections.unmodifiableList(new ArrayList<ChiTietHoaDon>(chiTiet));
        }
    }

    @Override
    public String getMa() {
        return ma;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public HangKhachHang getHangKhachHang() {
        return hangKhachHang;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public double getTiLeGiam() {
        return tiLeGiam;
    }

    public List<ChiTietHoaDon> getChiTiet() {
        return chiTiet;
    }

    public double getTamTinh() {
        double tong = 0;
        for (int i = 0; i < chiTiet.size(); i++) {
            tong += chiTiet.get(i).getThanhTien();
        }
        return tong;
    }

    public double getTienGiam() {
        return Math.round(getTamTinh() * tiLeGiam);
    }

    public double getTongTien() {
        return getTamTinh() - getTienGiam();
    }

    public int getTongSoLuong() {
        int tong = 0;
        for (int i = 0; i < chiTiet.size(); i++) {
            tong += chiTiet.get(i).getSoLuong();
        }
        return tong;
    }

    /** Số điểm đã cộng cho khách khi lập hóa đơn (dùng khi hủy hóa đơn). */
    public int getDiemCong() {
        return (int) (getTongTien() / TIEN_MOI_DIEM);
    }

    /**
     * Định dạng:
     * ma|ngayLap|maKhachHang|tenKhachHang|hang|maNhanVien|tenNhanVien|danhSachChiTiet
     * trong đó danhSachChiTiet là các dòng chi tiết ngăn bởi ';'.
     */
    public String toFileString() {
        StringBuilder dsChiTiet = new StringBuilder();
        for (int i = 0; i < chiTiet.size(); i++) {
            if (dsChiTiet.length() > 0) {
                dsChiTiet.append(';');
            }
            dsChiTiet.append(chiTiet.get(i).toFileString());
        }
        return ma + "|" + ngayLap.toString() + "|" + DocGhiFile.maHoa(maKhachHang) + "|"
                + DocGhiFile.maHoa(tenKhachHang) + "|" + hangKhachHang.name() + "|"
                + DocGhiFile.maHoa(maNhanVien) + "|" + DocGhiFile.maHoa(tenNhanVien) + "|"
                + dsChiTiet.toString();
    }

    public static HoaDon fromFileString(String dong) {
        String[] p = DocGhiFile.tach(dong);
        if (p.length < 8) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/8)");
        }
        List<ChiTietHoaDon> chiTiet = new ArrayList<ChiTietHoaDon>();
        if (!p[7].isEmpty()) {
            String[] cacDong = p[7].split(";", -1);
            for (int i = 0; i < cacDong.length; i++) {
                chiTiet.add(ChiTietHoaDon.fromFileString(cacDong[i]));
            }
        }
        return new HoaDon(p[0], LocalDateTime.parse(p[1]), DocGhiFile.giaiMa(p[2]),
                DocGhiFile.giaiMa(p[3]), HangKhachHang.valueOf(p[4]), DocGhiFile.giaiMa(p[5]),
                DocGhiFile.giaiMa(p[6]), chiTiet);
    }

    @Override
    public String toString() {
        return ma + " - " + tenKhachHang;
    }
}
