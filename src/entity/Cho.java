package entity;

public class Cho extends ThuCung {
    private String giong;
    private double canNang;

    public Cho() {
        super();
    }

    public Cho(String maThuCung, String ten, int tuoiThang, double gia, String gioiTinh, boolean daBan,
               String giong, double canNang) {
        super(maThuCung, ten, tuoiThang, gia, gioiTinh, daBan);
        this.giong = giong;
        this.canNang = canNang;
    }

    public String getGiong() { return giong; }
    public void setGiong(String giong) { this.giong = giong; }
    public double getCanNang() { return canNang; }
    public void setCanNang(double canNang) { this.canNang = canNang; }

    @Override
    public String getLoai() { return "CHO"; }

    @Override
    public String tiengKeu() { return "Gau gau!"; }

    @Override
    public double tinhGiamGia() {
        return tuoiThang >= 12 ? 10 : 5;
    }

    @Override
    protected String thongTinRieng() { return giong + "|" + canNang; }

    @Override
    public String moTa() { return "Giong: " + giong + ", " + canNang + " kg"; }
}
