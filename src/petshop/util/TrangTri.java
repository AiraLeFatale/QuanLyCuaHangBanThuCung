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

    /** Số gọn: 1.5 -> "1.5", 2.0 -> "2". */
    public static String so(double giaTri) {
        if (giaTri == Math.rint(giaTri)) {
            return String.valueOf((long) giaTri);
        }
        return String.format(java.util.Locale.ROOT, "%.1f", giaTri);
    }
}
