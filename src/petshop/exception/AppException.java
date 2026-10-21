package petshop.exception;

/**
 * Lỗi chung của ứng dụng. Thông điệp của lớp này (và các lớp con)
 * luôn hiển thị được trực tiếp cho người dùng.
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AppException(String thongDiep) {
        super(thongDiep);
    }

    public AppException(String thongDiep, Throwable nguyenNhan) {
        super(thongDiep, nguyenNhan);
    }
}
