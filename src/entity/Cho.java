package entity;

import java.time.LocalDate;

import util.DocGhiFile;

/** Chó. Đặc điểm riêng: giống, kích cỡ, tình trạng tiêm phòng. */
public class Cho extends ThuCung {

    private String giong;
    private String kichThuoc;
    private boolean daTiemPhong;

    public Cho(String ma, String ten, LocalDate ngaySinh, double giaBan, int soLuong,
               TrangThaiThuCung trangThai, String giong, String kichThuoc, boolean daTiemPhong) {
        super(ma, ten, ngaySinh, giaBan, soLuong, trangThai);
        this.giong = giong == null ? "" : giong;
        this.kichThuoc = kichThuoc == null || kichThuoc.isBlank() ? "Vừa" : kichThuoc;
        this.daTiemPhong = daTiemPhong;
    }

    public String getGiong() {
        return giong;
    }

    public void setGiong(String giong) {
        this.giong = giong == null ? "" : giong;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc == null || kichThuoc.isBlank() ? "Vừa" : kichThuoc;
    }

    public boolean isDaTiemPhong() {
        return daTiemPhong;
    }

    public void setDaTiemPhong(boolean daTiemPhong) {
        this.daTiemPhong = daTiemPhong;
    }

    @Override
    public String getLoai() {
        return "Chó";
    }

    @Override
    public String getDacDiem() {
        return "Giống " + giong + ", cỡ " + kichThuoc.toLowerCase()
                + ", " + (daTiemPhong ? "đã tiêm phòng" : "chưa tiêm phòng");
    }

    @Override
    public String toFileString() {
        return super.toFileString() + DocGhiFile.maHoa(giong) + "|"
                + DocGhiFile.maHoa(kichThuoc) + "|" + daTiemPhong;
    }

    @Override
    public double getChiPhiChamSoc() {
        if (kichThuoc.equalsIgnoreCase("Nhỏ")) {
            return 700_000;
        }
        if (kichThuoc.equalsIgnoreCase("Lớn")) {
            return 1_400_000;
        }
        return 1_000_000;
    }
}
