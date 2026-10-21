package petshop.console;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import petshop.exception.AppException;
import petshop.util.Validator;

/** Nhập dữ liệu từ bàn phím và in bảng ra màn hình console. */
public final class NhapXuat {

    private static final Scanner SCANNER = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);

    private NhapXuat() {
    }

    public static void dongKe() {
        System.out.println("-".repeat(96));
    }

    public static void tieuDe(String tieuDe) {
        System.out.println();
        System.out.println("=".repeat(96));
        System.out.println("  " + tieuDe.toUpperCase(Locale.ROOT));
        System.out.println("=".repeat(96));
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
            throw new KetThucNhapException();
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
}
