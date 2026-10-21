package entity;

public class NhanVien extends Nguoi {
    private String chucVu;
    private double luong;

    public NhanVien() {
        super();
    }

    public NhanVien(String ma, String hoTen, String sdt, String diaChi, String chucVu, double luong) {
        super(ma, hoTen, sdt, diaChi);
        this.chucVu = chucVu;
        this.luong = luong;
    }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    @Override
    public String getVaiTro() { return "Nhan vien"; }

    @Override
    protected String thongTinRieng() { return chucVu + "|" + (long) luong; }

    @Override
    protected String moTaRieng() { return String.format("%s, luong %,.0f d", chucVu, luong); }
}
