package petshop.console;

import java.time.LocalDate;
import java.util.List;

/**
 * Các câu hỏi nhập liệu dùng chung cho nhiều menu.
 * Mỗi hàm tự kiểm tra dữ liệu và hỏi lại khi nhập sai.
 */
public final class HoiNhap {

    private HoiNhap() {
    }

    /** Hỏi chuỗi; nếu có giá trị cũ thì để trống là giữ nguyên. */
    public static String chuoi(String nhan, String giaTriCu) {
        if (giaTriCu == null || giaTriCu.isBlank()) {
            return NhapXuat.docBatBuoc(nhan + ": ", nhan);
        }
        return NhapXuat.docMacDinh(nhan + " [" + giaTriCu + "]: ", giaTriCu);
    }

    public static String chuoiTuyChon(String nhan, String giaTriCu) {
        String s = NhapXuat.doc(nhan + (giaTriCu == null || giaTriCu.isBlank()
                ? " (có thể bỏ trống): "
                : " [" + giaTriCu + "]: "));
        if (s.isEmpty()) {
            return giaTriCu == null ? "" : giaTriCu;
        }
        return s;
    }

    public static double tien(String nhan, double giaTriCu) {
        while (true) {
            String goiY = giaTriCu > 0
                    ? nhan + " [" + (long) giaTriCu + "]: "
                    : nhan + ": ";
            String s = NhapXuat.doc(goiY);
            if (s.isEmpty()) {
                if (giaTriCu > 0) {
                    return giaTriCu;
                }
                NhapXuat.loi(nhan + " không được để trống");
                continue;
            }
            try {
                return petshop.util.Validator.soTien(s, nhan);
            } catch (petshop.exception.AppException ex) {
                NhapXuat.loi(ex.getMessage());
            }
        }
    }

    public static int soNguyen(String nhan, int giaTriCu, int nhoNhat, int lonNhat) {
        while (true) {
            String goiY = nhan + " [" + giaTriCu + "]: ";
            String s = NhapXuat.doc(goiY);
            if (s.isEmpty()) {
                return giaTriCu;
            }
            try {
                int giaTri = Integer.parseInt(s);
                if (giaTri < nhoNhat || giaTri > lonNhat) {
                    NhapXuat.loi(nhan + " phải trong khoảng " + nhoNhat + " - " + lonNhat);
                    continue;
                }
                return giaTri;
            } catch (NumberFormatException ex) {
                NhapXuat.loi(nhan + " phải là số nguyên");
            }
        }
    }

    public static LocalDate ngay(String nhan, LocalDate giaTriCu) {
        while (true) {
            String goiY = giaTriCu == null
                    ? nhan + " (dd/MM/yyyy, bỏ trống nếu không rõ): "
                    : nhan + " [" + petshop.util.TrangTri.ngay(giaTriCu) + "]: ";
            String s = NhapXuat.doc(goiY);
            if (s.isEmpty()) {
                return giaTriCu;
            }
            try {
                return petshop.util.Validator.ngay(s, nhan, true);
            } catch (petshop.exception.AppException ex) {
                NhapXuat.loi(ex.getMessage());
            }
        }
    }

    public static boolean coKhong(String nhan, boolean giaTriCu) {
        while (true) {
            String s = NhapXuat.doc(nhan + " (c/k) [" + (giaTriCu ? "c" : "k") + "]: ")
                    .toLowerCase(java.util.Locale.ROOT);
            if (s.isEmpty()) {
                return giaTriCu;
            }
            if (s.equals("c") || s.equals("co") || s.equals("có") || s.equals("y")) {
                return true;
            }
            if (s.equals("k") || s.equals("khong") || s.equals("không") || s.equals("n")) {
                return false;
            }
            NhapXuat.loi("Vui lòng nhập c hoặc k");
        }
    }

    /**
     * Chọn một phần tử trong danh sách theo mã.
     * Trả về null nếu người dùng bỏ trống (khi choPhepHuy = true).
     */
    public static String maTrongDanhSach(String goiY, List<String> cacMa, boolean choPhepHuy) {
        while (true) {
            String ma = NhapXuat.doc(goiY);
            if (ma.isEmpty() && choPhepHuy) {
                return null;
            }
            for (String moiMa : cacMa) {
                if (moiMa.equalsIgnoreCase(ma)) {
                    return moiMa;
                }
            }
            NhapXuat.loi("Không tìm thấy mã \"" + ma + "\" trong danh sách");
        }
    }
}
