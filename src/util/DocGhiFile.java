package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc / ghi dữ liệu ra tệp văn bản .txt (UTF-8): mỗi dòng là một bản ghi.
 *
 * Lớp này chỉ làm 4 việc đơn giản:
 *  - doc(duongDan)          : đọc tệp thành danh sách dòng
 *  - ghi(duongDan, cacDong) : ghi đè cả tệp
 *  - maHoa / giaiMa         : mã hóa ký tự phân cách '|', ';', '~'
 */
public final class DocGhiFile {

    private DocGhiFile() {
    }

    /** Đọc tệp, bỏ qua dòng trống và dòng chú thích bắt đầu bằng '#'. */
    public static ArrayList<String> doc(String duongDan) {
        ArrayList<String> ketQua = new ArrayList<String>();
        Path tep = Paths.get(duongDan);
        if (!Files.exists(tep)) {
            return ketQua;
        }
        try {
            List<String> cacDong = Files.readAllLines(tep, StandardCharsets.UTF_8);
            for (int i = 0; i < cacDong.size(); i++) {
                String dong = cacDong.get(i).trim();
                if (dong.isEmpty() || dong.startsWith("#")) {
                    continue;
                }
                ketQua.add(dong);
            }
        } catch (IOException ex) {
            throw new AppException("Không đọc được tệp " + duongDan + ": " + ex.getMessage(), ex);
        }
        return ketQua;
    }

    /** Ghi đè toàn bộ tệp (ghi ra tệp tạm rồi đổi tên để tránh mất dữ liệu). */
    public static void ghi(String duongDan, ArrayList<String> cacDong) {
        Path tep = Paths.get(duongDan);
        try {
            Path thuMuc = tep.toAbsolutePath().getParent();
            if (thuMuc != null) {
                Files.createDirectories(thuMuc);
            }
            ArrayList<String> noiDung = new ArrayList<String>();
            noiDung.add("# " + tep.getFileName() + " - dữ liệu cửa hàng thú cưng (UTF-8)");
            for (int i = 0; i < cacDong.size(); i++) {
                noiDung.add(cacDong.get(i));
            }
            Path tepTam = tep.resolveSibling(tep.getFileName() + ".tmp");
            Files.write(tepTam, noiDung, StandardCharsets.UTF_8);
            Files.move(tepTam, tep, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new AppException("Không ghi được tệp " + duongDan + ": " + ex.getMessage(), ex);
        }
    }

    /** Tách một dòng thành các trường theo dấu '|' (giữ cả trường rỗng ở cuối). */
    public static String[] tach(String dong) {
        return dong.split("\\|", -1);
    }

    /** Mã hóa các ký tự đặc biệt để không lẫn với dấu phân cách trong tệp. */
    public static String maHoa(String chuoi) {
        if (chuoi == null || chuoi.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(chuoi.length() + 8);
        for (int i = 0; i < chuoi.length(); i++) {
            char c = chuoi.charAt(i);
            if (c == '\\') {
                sb.append("\\\\");
            } else if (c == '|') {
                sb.append("\\p");
            } else if (c == ';') {
                sb.append("\\s");
            } else if (c == '~') {
                sb.append("\\t");
            } else if (c == '\n') {
                sb.append("\\n");
            } else if (c == '\r') {
                sb.append("\\r");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /** Giải mã chuỗi đã được mã hóa bằng {@link #maHoa(String)}. */
    public static String giaiMa(String chuoi) {
        if (chuoi == null || chuoi.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(chuoi.length());
        for (int i = 0; i < chuoi.length(); i++) {
            char c = chuoi.charAt(i);
            if (c != '\\' || i == chuoi.length() - 1) {
                sb.append(c);
                continue;
            }
            char tiepTheo = chuoi.charAt(++i);
            if (tiepTheo == '\\') {
                sb.append('\\');
            } else if (tiepTheo == 'p') {
                sb.append('|');
            } else if (tiepTheo == 's') {
                sb.append(';');
            } else if (tiepTheo == 't') {
                sb.append('~');
            } else if (tiepTheo == 'n') {
                sb.append('\n');
            } else if (tiepTheo == 'r') {
                sb.append('\r');
            } else {
                sb.append(tiepTheo);
            }
        }
        return sb.toString();
    }
}
