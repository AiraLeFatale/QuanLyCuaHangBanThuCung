package entity;

/** Dịch vụ cắt tỉa lông, tạo kiểu cho thú cưng. */
public class DichVuCatTia extends DichVu {

    private String kieuLong;

    public DichVuCatTia(String ma, String ten, double giaBan, int thoiGianPhut, String kieuLong) {
        super(ma, ten, giaBan, thoiGianPhut);
        this.kieuLong = kieuLong == null ? "" : kieuLong;
    }

    public String getKieuLong() {
        return kieuLong;
    }

    public void setKieuLong(String kieuLong) {
        this.kieuLong = kieuLong == null ? "" : kieuLong;
    }

    @Override
    public String getLoai() {
        return "Cắt tỉa";
    }

    @Override
    public String getDacDiem() {
        return "Kiểu " + kieuLong + ", khoảng " + getThoiGianPhut() + " phút";
    }
}
