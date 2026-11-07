package entity;

import util.DocGhiFile;

/** Dịch vụ tắm - sấy cho thú cưng, giá khác nhau theo kích cỡ. */
public class DichVuTam extends DichVu {

    private String kichThuoc;

    public DichVuTam(String ma, String ten, double giaBan, int thoiGianPhut, String kichThuoc) {
        super(ma, ten, giaBan, thoiGianPhut);
        this.kichThuoc = kichThuoc == null ? "" : kichThuoc;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc == null ? "" : kichThuoc;
    }

    @Override
    public String getLoai() {
        return "Tắm & sấy";
    }

    @Override
    public String getDacDiem() {
        return "Dành cho thú cưng cỡ " + kichThuoc + ", khoảng " + getThoiGianPhut() + " phút";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + DocGhiFile.maHoa(kichThuoc);
    }
}
