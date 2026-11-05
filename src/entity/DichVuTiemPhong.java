package entity;

/** Dịch vụ tiêm phòng cho thú cưng. */
public class DichVuTiemPhong extends DichVu {

    private String loaiVacXin;

    public DichVuTiemPhong(String ma, String ten, double giaBan, int thoiGianPhut, String loaiVacXin) {
        super(ma, ten, giaBan, thoiGianPhut);
        this.loaiVacXin = loaiVacXin == null ? "" : loaiVacXin;
    }

    public String getLoaiVacXin() {
        return loaiVacXin;
    }

    public void setLoaiVacXin(String loaiVacXin) {
        this.loaiVacXin = loaiVacXin == null ? "" : loaiVacXin;
    }

    @Override
    public String getLoai() {
        return "Tiêm phòng";
    }

    @Override
    public String getDacDiem() {
        return "Vắc-xin " + loaiVacXin + ", khoảng " + getThoiGianPhut() + " phút";
    }
}
