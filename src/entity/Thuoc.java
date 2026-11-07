package entity;

import java.time.LocalDate;

import util.DocGhiFile;
import util.TrangTri;


/** Thuốc cho thú cưng: công dụng, hạn sử dụng và có cần kê đơn hay không. */
public class Thuoc extends SanPham {

    private String congDung;
    private LocalDate hanSuDung;
    private boolean canKeDon;

    public Thuoc(String ma, String ten, double giaBan, int soLuong,
                 String congDung, LocalDate hanSuDung, boolean canKeDon) {
        super(ma, ten, giaBan, soLuong);
        this.congDung = congDung == null ? "" : congDung;
        this.hanSuDung = hanSuDung;
        this.canKeDon = canKeDon;
    }

    public String getCongDung() {
        return congDung;
    }

    public void setCongDung(String congDung) {
        this.congDung = congDung == null ? "" : congDung;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public boolean isCanKeDon() {
        return canKeDon;
    }

    public void setCanKeDon(boolean canKeDon) {
        this.canKeDon = canKeDon;
    }

    @Override
    public String getLoai() {
        return "Thuốc";
    }

    @Override
    public String getDacDiem() {
        return congDung + ", HSD " + TrangTri.ngay(hanSuDung)
                + (canKeDon ? ", cần kê đơn" : ", không cần kê đơn");
    }

    @Override
    public String toFileString() {
        return super.toFileString() + DocGhiFile.maHoa(congDung) + "|"
                + (hanSuDung == null ? "" : hanSuDung.toString()) + "|" + canKeDon;
    }
}
