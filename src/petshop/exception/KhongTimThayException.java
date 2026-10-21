package petshop.exception;

/** Không tìm thấy đối tượng theo mã. */
public class KhongTimThayException extends AppException {

    private static final long serialVersionUID = 1L;

    public KhongTimThayException(String thongDiep) {
        super(thongDiep);
    }
}
