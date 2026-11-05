package inter;

import java.util.List;
import java.util.Optional;

/**
 * Hợp đồng chung cho mọi danh mục quản lý (thú cưng, khách hàng, nhân viên, hóa đơn).
 * Nhờ interface này, các lớp quản lý dùng lại được cùng một khung xử lý.
 */
public interface QuanLy<T> {

    List<T> tatCa();

    Optional<T> tim(String ma);

    boolean chuaCo(String ma);

    void them(T doiTuong);

    void capNhat(T doiTuong);

    void xoa(String ma);

    /** Sinh mã kế tiếp, ví dụ TC001 -> TC002. */
    String sinhMa();

    /** Ghi danh sách hiện tại xuống tệp. */
    void luu();

    int soLuong();
}
