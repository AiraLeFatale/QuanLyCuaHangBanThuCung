package petshop.util;

import java.text.NumberFormat;
import java.util.Locale;

import petshop.exception.DuLieuKhongHopLeException;

/** Tiện ích định dạng và đọc số tiền theo kiểu Việt Nam. */
public final class Tien {

    private static final NumberFormat DINH_DANG = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));

    static {
        DINH_DANG.setMinimumFractionDigits(0);
        DINH_DANG.setMaximumFractionDigits(0);
    }

    private Tien() {
    }

    /** 1250000 -> "1.250.000 đ" */
    public static String dinhDang(double soTien) {
        return DINH_DANG.format(Math.round(soTien)) + " đ";
    }

    public static double lamTron(double soTien) {
        return Math.round(soTien);
    }

    /**
     * Đọc số tiền người dùng nhập: chấp nhận "1500000", "1.500.000", "1,500,000", "1.500.000 đ".
     */
    public static double doc(String chuoi) {
        if (chuoi == null) {
            throw new DuLieuKhongHopLeException("Số tiền không được để trống");
        }
        String s = chuoi.trim().toLowerCase(Locale.ROOT)
                .replace("đ", "")
                .replace("vnd", "")
                .replace(".", "")
                .replace(",", "")
                .replace(" ", "");
        if (s.isEmpty()) {
            throw new DuLieuKhongHopLeException("Số tiền không được để trống");
        }
        try {
            double giaTri = Double.parseDouble(s);
            if (giaTri < 0) {
                throw new DuLieuKhongHopLeException("Số tiền không được âm");
            }
            return giaTri;
        } catch (NumberFormatException ex) {
            throw new DuLieuKhongHopLeException("\"" + chuoi.trim() + "\" không phải là số tiền hợp lệ");
        }
    }
}
