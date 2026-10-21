package petshop.util;

import java.time.format.DateTimeFormatter;

/** Định dạng ngày giờ và phần trăm dùng chung cho giao diện và báo cáo. */
public final class TrangTri {

    public static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter NGAY_GIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private TrangTri() {
    }

    public static String ngay(java.time.LocalDate ngay) {
        return ngay == null ? "" : NGAY.format(ngay);
    }

    public static String phanTram(double tiLe) {
        return Math.round(tiLe * 100) + "%";
    }
}
