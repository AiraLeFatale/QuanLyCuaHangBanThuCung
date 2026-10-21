package petshop.io;

/**
 * Mã hóa/giải mã các ký tự đặc biệt dùng làm dấu phân cách trong tệp .txt.
 * Nhờ vậy tên thú cưng hay địa chỉ có chứa "|" hoặc ";" vẫn lưu được an toàn.
 */
public final class Esc {

    private Esc() {
    }

    public static String ma(String chuoi) {
        if (chuoi == null || chuoi.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(chuoi.length() + 8);
        for (int i = 0; i < chuoi.length(); i++) {
            char c = chuoi.charAt(i);
            switch (c) {
                case '\\' -> sb.append("\\\\");
                case '|' -> sb.append("\\p");
                case ';' -> sb.append("\\s");
                case '~' -> sb.append("\\t");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

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
            switch (tiepTheo) {
                case '\\' -> sb.append('\\');
                case 'p' -> sb.append('|');
                case 's' -> sb.append(';');
                case 't' -> sb.append('~');
                case 'n' -> sb.append('\n');
                case 'r' -> sb.append('\r');
                default -> sb.append(tiepTheo);
            }
        }
        return sb.toString();
    }
}
