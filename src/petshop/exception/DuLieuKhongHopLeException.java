package petshop.exception;

/** Dữ liệu người dùng nhập vào không đúng định dạng hoặc thiếu thông tin. */
public class DuLieuKhongHopLeException extends AppException {

    private static final long serialVersionUID = 1L;

    public DuLieuKhongHopLeException(String thongDiep) {
        super(thongDiep);
    }
}
