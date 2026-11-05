package util;

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

    /** Dữ liệu người dùng nhập vào không đúng định dạng hoặc thiếu thông tin. */
    public static class DuLieuKhongHopLe extends AppException {

        private static final long serialVersionUID = 1L;

        public DuLieuKhongHopLe(String thongDiep) {
            super(thongDiep);
        }
    }

    /** Không tìm thấy đối tượng theo mã. */
    public static class KhongTimThay extends AppException {

        private static final long serialVersionUID = 1L;

        public KhongTimThay(String thongDiep) {
            super(thongDiep);
        }
    }

    /** Vi phạm quy tắc nghiệp vụ (hết hàng, trùng mã, ...). */
    public static class NghiepVu extends AppException {

        private static final long serialVersionUID = 1L;

        public NghiepVu(String thongDiep) {
            super(thongDiep);
        }
    }
}
