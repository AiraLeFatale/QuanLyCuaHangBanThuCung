package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;


/**
 * Kiểm tra dữ liệu nhập từ giao diện. Mọi lỗi đều ném AppException.DuLieuKhongHopLe
 * với thông điệp tiếng Việt để hiển thị trực tiếp.
 */
public final class Validator {

    private static final Pattern SO_DIEN_THOAI = Pattern.compile("^0\\d{9}$");
    private static final DateTimeFormatter NGAY_VIET = DateTimeFormatter
            .ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private Validator() {
    }

    public static String chuoi(String giaTri, String tenTruong) {
        String s = giaTri == null ? "" : giaTri.trim();
        if (s.isEmpty()) {
            throw new AppException.DuLieuKhongHopLe(tenTruong + " không được để trống");
        }
        if (s.length() > 200) {
            throw new AppException.DuLieuKhongHopLe(tenTruong + " quá dài (tối đa 200 ký tự)");
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
            throw new AppException.DuLieuKhongHopLe(tenTruong + " phải là số nguyên");
        }
        if (giaTriSo < nhoNhat || giaTriSo > lonNhat) {
            throw new AppException.DuLieuKhongHopLe(tenTruong + " phải nằm trong khoảng " + nhoNhat + " - " + lonNhat);
        }
        return giaTriSo;
    }

    public static double soTien(String giaTri, String tenTruong) {
        double giaTriSo = Tien.doc(giaTri);
        if (giaTriSo <= 0) {
            throw new AppException.DuLieuKhongHopLe(tenTruong + " phải lớn hơn 0");
        }
        return giaTriSo;
    }

    public static LocalDate ngay(String giaTri, String tenTruong, boolean choPhepRong) {
        return ngay(giaTri, tenTruong, choPhepRong, false);
    }

    /**
     * Kiểm tra ngày; choPhepTuongLai = true dùng cho hạn sử dụng, ngày dự kiến...
     * Các trường như ngày sinh, ngày vào làm vẫn dùng bản mặc định để chặn ngày tương lai.
     */
    public static LocalDate ngay(String giaTri, String tenTruong, boolean choPhepRong,
                                 boolean choPhepTuongLai) {
        String s = giaTri == null ? "" : giaTri.trim();
        if (s.isEmpty()) {
            if (choPhepRong) {
                return null;
            }
            throw new AppException.DuLieuKhongHopLe(tenTruong + " không được để trống");
        }
        LocalDate ngay;
        try {
            ngay = s.contains("/") ? LocalDate.parse(s, NGAY_VIET) : LocalDate.parse(s);
        } catch (DateTimeParseException ex) {
            throw new AppException.DuLieuKhongHopLe(tenTruong + " phải có dạng yyyy-MM-dd hoặc dd/MM/yyyy");
        }
        if (!choPhepTuongLai && ngay.isAfter(LocalDate.now())) {
            throw new AppException.DuLieuKhongHopLe(tenTruong + " không được ở tương lai");
        }
        return ngay;
    }

    public static String soDienThoai(String giaTri, String tenTruong, boolean choPhepRong) {
        String s = giaTri == null ? "" : giaTri.trim();
        if (s.isEmpty()) {
            if (choPhepRong) {
                return "";
            }
            throw new AppException.DuLieuKhongHopLe(tenTruong + " không được để trống");
        }
        if (!SO_DIEN_THOAI.matcher(s).matches()) {
            throw new AppException.DuLieuKhongHopLe(tenTruong + " phải gồm 10 chữ số và bắt đầu bằng 0");
        }
        return s;
    }
}
