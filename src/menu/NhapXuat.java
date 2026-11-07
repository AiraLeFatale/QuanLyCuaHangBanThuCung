package menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import util.AppException;
import util.TrangTri;
import util.Validator;

/**
 * Nhập dữ liệu từ bàn phím (có kiểm tra, hỏi lại khi sai) và in bảng ra màn hình console.
 */
public final class NhapXuat {

    private NhapXuat() {
    }

    private static final Scanner SCANNER = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);

    /** Lặp một chuỗi n lần (thay cho String.repeat của Java 11). */
    private static String lap(String chuoi, int soLan) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < soLan; i++) {
            sb.append(chuoi);
        }
        return sb.toString();
    }

    public static void dongKe() {
        System.out.println(lap("-", 96));
    }

    public static void tieuDe(String tieuDe) {
        System.out.println();
        System.out.println(lap("=", 96));
        System.out.println("  " + tieuDe.toUpperCase(Locale.ROOT));
        System.out.println(lap("=", 96));
    }

    public static void muc(String tieuDe) {
        System.out.println();
        System.out.println("--- " + tieuDe + " ---");
    }

    public static void thongBao(String noiDung) {
        System.out.println("[OK]  " + noiDung);
    }

    public static void loi(String noiDung) {
        System.out.println("[LỖI] " + noiDung);
    }

    /** Đọc một dòng, trả về chuỗi đã cắt khoảng trắng. */
    public static String doc(String goiY) {
        System.out.print(goiY);
        if (!SCANNER.hasNextLine()) {
            throw new NhapXuat.KetThucNhapException();
        }
        return SCANNER.nextLine().trim();
    }

    public static String docBatBuoc(String goiY, String tenTruong) {
        while (true) {
            String s = doc(goiY);
            if (!s.isEmpty()) {
                return s;
            }
            loi(tenTruong + " không được để trống");
        }
    }

    /** Để trống thì giữ giá trị cũ. */
    public static String docMacDinh(String goiY, String giaTriCu) {
        String s = doc(goiY);
        return s.isEmpty() ? giaTriCu : s;
    }

    public static int docSoNguyen(String goiY, int nhoNhat, int lonNhat) {
        while (true) {
            String s = docBatBuoc(goiY, "Lựa chọn");
            try {
                int giaTri = Integer.parseInt(s);
                if (giaTri < nhoNhat || giaTri > lonNhat) {
                    loi("Vui lòng nhập số trong khoảng " + nhoNhat + " - " + lonNhat);
                    continue;
                }
                return giaTri;
            } catch (NumberFormatException ex) {
                loi("\"" + s + "\" không phải số nguyên");
            }
        }
    }

    public static double docSoTien(String goiY) {
        while (true) {
            String s = docBatBuoc(goiY, "Số tiền");
            try {
                return Validator.soTien(s, "Số tiền");
            } catch (AppException ex) {
                loi(ex.getMessage());
            }
        }
    }

    public static LocalDate docNgay(String goiY, boolean choPhepRong) {
        while (true) {
            String s = doc(goiY);
            try {
                return Validator.ngay(s, "Ngày", choPhepRong);
            } catch (AppException ex) {
                loi(ex.getMessage());
            }
        }
    }

    public static boolean xacNhan(String cauHoi) {
        while (true) {
            String s = doc(cauHoi + " (c/k): ").toLowerCase(Locale.ROOT);
            if (s.equals("c") || s.equals("co") || s.equals("có") || s.equals("y")) {
                return true;
            }
            if (s.equals("k") || s.equals("khong") || s.equals("không") || s.equals("n")) {
                return false;
            }
            loi("Vui lòng nhập c (có) hoặc k (không)");
        }
    }

    public static void tamDung() {
        doc("\nNhấn Enter để tiếp tục...");
    }

    /** In bảng văn bản với độ rộng cột cho trước. */
    public static void inBang(String[] tieuDe, List<String[]> cacDong, int[] doRong) {
        dongKe();
        inMotDong(tieuDe, doRong);
        dongKe();
        if (cacDong.isEmpty()) {
            System.out.println("  (chưa có dữ liệu)");
        } else {
            for (String[] dong : cacDong) {
                inMotDong(dong, doRong);
            }
        }
        dongKe();
    }

    private static void inMotDong(String[] o, int[] doRong) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < o.length && i < doRong.length; i++) {
            String giaTri = o[i] == null ? "" : o[i];
            if (giaTri.length() > doRong[i]) {
                giaTri = doRong[i] > 1 ? giaTri.substring(0, doRong[i] - 1) + "…" : "";
            }
            sb.append(' ').append(giaTri);
            for (int j = giaTri.length(); j < doRong[i]; j++) {
                sb.append(' ');
            }
            sb.append(" |");
        }
        System.out.println(sb);
    }

    // ---- Các câu hỏi nhập liệu dùng chung cho nhiều menu ----

    /** Hỏi chuỗi; nếu có giá trị cũ thì để trống là giữ nguyên. */
    public static String chuoi(String nhan, String giaTriCu) {
        if (giaTriCu == null || giaTriCu.trim().isEmpty()) {
            return NhapXuat.docBatBuoc(nhan + ": ", nhan);
        }
        return NhapXuat.docMacDinh(nhan + " [" + giaTriCu + "]: ", giaTriCu);
    }

    public static String chuoiTuyChon(String nhan, String giaTriCu) {
        String s = NhapXuat.doc(nhan + (giaTriCu == null || giaTriCu.trim().isEmpty()
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
                return util.Validator.soTien(s, nhan);
            } catch (util.AppException ex) {
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
        return hoiNgay(nhan, giaTriCu, false);
    }

    /** Như {@link #ngay(String, LocalDate)} nhưng cho phép ngày ở tương lai (hạn sử dụng...). */
    public static LocalDate ngayTuongLai(String nhan, LocalDate giaTriCu) {
        return hoiNgay(nhan, giaTriCu, true);
    }

    private static LocalDate hoiNgay(String nhan, LocalDate giaTriCu, boolean choPhepTuongLai) {
        while (true) {
            String goiY = giaTriCu == null
                    ? nhan + " (dd/MM/yyyy, bỏ trống nếu không rõ): "
                    : nhan + " [" + util.TrangTri.ngay(giaTriCu) + "]: ";
            String s = NhapXuat.doc(goiY);
            if (s.isEmpty()) {
                return giaTriCu;
            }
            try {
                return util.Validator.ngay(s, nhan, true, choPhepTuongLai);
            } catch (util.AppException ex) {
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

    /** Ném ra khi người dùng kết thúc nhập (Ctrl+Z / Ctrl+D) để thoát chương trình an toàn. */
    public static class KetThucNhapException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public KetThucNhapException() {
            super("Kết thúc nhập");
        }
    }
}
