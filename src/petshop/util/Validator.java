package petshop.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import petshop.exception.DuLieuKhongHopLeException;

/**
 * Kiểm tra dữ liệu nhập từ giao diện. Mọi lỗi đều ném DuLieuKhongHopLeException
 * với thông điệp tiếng Việt để hiển thị trực tiếp.
 */
public final class Validator {

    private static final Pattern SO_DIEN_THOAI = Pattern.compile("^0\\d{9}$");
    private static final DateTimeFormatter NGAY_VIET = DateTimeFormatter.ofPattern("d/M/yyyy");

    private Validator() {
    }

    public static String chuoi(String giaTri, String tenTruong) {
        String s = giaTri == null ? "" : giaTri.trim();
        if (s.isEmpty()) {
            throw new DuLieuKhongHopLeException(tenTruong + " không được để trống");
        }
        if (s.length() > 200) {
            throw new DuLieuKhongHopLeException(tenTruong + " quá dài (tối đa 200 ký tự)");
        }
        return s;
    }

    public static String chuoiTuyChon(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }

    public static int soNguyen(String giaTri, String tenTruong, int nhoNhat, int lonNhat) {
        String s = chuoi(giaTri, tenTruong);
        int giaTriSo;
        try {
            giaTriSo = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new DuLieuKhongHopLeException(tenTruong + " phải là số nguyên");
        }
        if (giaTriSo < nhoNhat || giaTriSo > lonNhat) {
            throw new DuLieuKhongHopLeException(tenTruong + " phải nằm trong khoảng " + nhoNhat + " - " + lonNhat);
        }
        return giaTriSo;
    }

    public static double soTien(String giaTri, String tenTruong) {
        double giaTriSo = Tien.doc(giaTri);
        if (giaTriSo <= 0) {
            throw new DuLieuKhongHopLeException(tenTruong + " phải lớn hơn 0");
        }
        return giaTriSo;
    }

    public static LocalDate ngay(String giaTri, String tenTruong, boolean choPhepRong) {
        String s = giaTri == null ? "" : giaTri.trim();
        if (s.isEmpty()) {
            if (choPhepRong) {
                return null;
            }
            throw new DuLieuKhongHopLeException(tenTruong + " không được để trống");
        }
        LocalDate ngay;
        try {
            ngay = s.contains("/") ? LocalDate.parse(s, NGAY_VIET) : LocalDate.parse(s);
        } catch (DateTimeParseException ex) {
            throw new DuLieuKhongHopLeException(tenTruong + " phải có dạng yyyy-MM-dd hoặc dd/MM/yyyy");
        }
        if (ngay.isAfter(LocalDate.now())) {
            throw new DuLieuKhongHopLeException(tenTruong + " không được ở tương lai");
        }
        return ngay;
    }

    public static String soDienThoai(String giaTri, String tenTruong, boolean choPhepRong) {
        String s = giaTri == null ? "" : giaTri.trim();
        if (s.isEmpty()) {
            if (choPhepRong) {
                return "";
            }
            throw new DuLieuKhongHopLeException(tenTruong + " không được để trống");
        }
        if (!SO_DIEN_THOAI.matcher(s).matches()) {
            throw new DuLieuKhongHopLeException(tenTruong + " phải gồm 10 chữ số và bắt đầu bằng 0");
        }
        return s;
    }
}
