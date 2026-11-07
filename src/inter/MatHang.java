package inter;

/**
 * Mặt hàng bán được trong cửa hàng: thú cưng, sản phẩm (thức ăn, thuốc, phụ kiện)
 * hoặc dịch vụ (tắm, cắt tỉa, tiêm phòng).
 *
 * Nhờ interface này, giỏ hàng và hóa đơn chỉ làm việc với "mặt hàng" chung,
 * không cần biết cụ thể là loại nào — đây là đa hình ở tầng nghiệp vụ.
 */
public interface MatHang extends CoMaKhoa {

    String getTen();

    /** Tên loại hiển thị: "Chó", "Mèo", "Thức ăn", "Tắm & cắt tỉa"... */
    String getLoai();

    double getGiaBan();

    /**
     * Số lượng còn bán được. Trả về -1 với mặt hàng không quản lý tồn kho (dịch vụ).
     */
    int getSoLuongTon();

    boolean conBanDuoc();

    void truKho(int soLuong);

    void hoanKho(int soLuong);

    /** Mô tả các thuộc tính riêng để hiển thị trong bảng. */
    String getDacDiem();

    /** true nếu mặt hàng có tồn kho (thú cưng, sản phẩm); false với dịch vụ. */
    boolean quanLyTonKho();
}
