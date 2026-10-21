package petshop.exception;

/** Vi phạm quy tắc nghiệp vụ (hết hàng, trùng mã, ...). */
public class NghiepVuException extends AppException {

    private static final long serialVersionUID = 1L;

    public NghiepVuException(String thongDiep) {
        super(thongDiep);
    }
}
