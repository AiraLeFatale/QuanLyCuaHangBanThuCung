package entity;

import java.time.LocalDate;

/** Mèo. Đặc điểm riêng: giống, lông dài hay ngắn, đã triệt sản chưa. */
public class Meo extends ThuCung {

    private String giong;
    private boolean daiLong;
    private boolean daTrietSan;

    public Meo(String ma, String ten, LocalDate ngaySinh, double giaBan, int soLuong,
               TrangThaiThuCung trangThai, String giong, boolean daiLong, boolean daTrietSan) {
        super(ma, ten, ngaySinh, giaBan, soLuong, trangThai);
        this.giong = giong == null ? "" : giong;
        this.daiLong = daiLong;
        this.daTrietSan = daTrietSan;
    }

    public String getGiong() {
        return giong;
    }

    public void setGiong(String giong) {
        this.giong = giong == null ? "" : giong;
    }

    public boolean isDaiLong() {
        return daiLong;
    }

    public void setDaiLong(boolean daiLong) {
        this.daiLong = daiLong;
    }

    public boolean isDaTrietSan() {
        return daTrietSan;
    }

    public void setDaTrietSan(boolean daTrietSan) {
        this.daTrietSan = daTrietSan;
    }

    @Override
    public String getLoai() {
        return "Mèo";
    }

    @Override
    public String getDacDiem() {
        return "Giống " + giong + ", lông " + (daiLong ? "dài" : "ngắn")
                + (daTrietSan ? ", đã triệt sản" : ", chưa triệt sản");
    }

    @Override
    public double getChiPhiChamSoc() {
        return daiLong ? 700_000 : 600_000;
    }
}
