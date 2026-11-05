package entity;

/**
 * Hạng khách hàng được suy ra từ điểm tích lũy, kèm tỉ lệ chiết khấu tương ứng.
 */
public enum HangKhachHang {

    THUONG("Thường", 0.00, 0),
    BAC("Bạc", 0.03, 50),
    VANG("Vàng", 0.07, 200),
    KIM_CUONG("Kim cương", 0.10, 500);

    private final String nhan;
    private final double tiLeChietKhau;
    private final int diemToiThieu;

    HangKhachHang(String nhan, double tiLeChietKhau, int diemToiThieu) {
        this.nhan = nhan;
        this.tiLeChietKhau = tiLeChietKhau;
        this.diemToiThieu = diemToiThieu;
    }

    public String getNhan() {
        return nhan;
    }

    public double getTiLeChietKhau() {
        return tiLeChietKhau;
    }

    public int getDiemToiThieu() {
        return diemToiThieu;
    }

    /** Hạng cao nhất mà số điểm hiện tại đạt được. */
    public static HangKhachHang tuDiem(int diem) {
        HangKhachHang ketQua = THUONG;
        for (HangKhachHang hang : values()) {
            if (diem >= hang.diemToiThieu) {
                ketQua = hang;
            }
        }
        return ketQua;
    }

    @Override
    public String toString() {
        return nhan;
    }
}
