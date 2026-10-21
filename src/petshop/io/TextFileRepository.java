package petshop.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import petshop.exception.AppException;

/**
 * Kho dữ liệu dùng tệp văn bản UTF-8, mỗi dòng là một đối tượng.
 * Dùng lại được cho mọi kiểu T nhờ TextCodec (generic + interface).
 */
public class TextFileRepository<T> {

    private final Path tep;
    private final TextCodec<T> codec;

    public TextFileRepository(Path tep, TextCodec<T> codec) {
        this.tep = tep;
        this.codec = codec;
    }

    public Path getTep() {
        return tep;
    }

    public List<T> doc() {
        List<T> ketQua = new ArrayList<>();
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
                try {
                    ketQua.add(codec.fromLine(dong));
                } catch (RuntimeException ex) {
                    throw new AppException("Dữ liệu không hợp lệ trong " + tep.getFileName()
                            + ", dòng " + (i + 1) + ": " + ex.getMessage(), ex);
                }
            }
        } catch (IOException ex) {
            throw new AppException("Không đọc được tệp " + tep + ": " + ex.getMessage(), ex);
        }
        return ketQua;
    }

    /** Ghi đè toàn bộ tệp (ghi ra tệp tạm rồi đổi tên để tránh mất dữ liệu). */
    public void ghi(List<T> danhSach) {
        try {
            Path thuMuc = tep.toAbsolutePath().getParent();
            if (thuMuc != null) {
                Files.createDirectories(thuMuc);
            }
            List<String> cacDong = new ArrayList<>();
            cacDong.add("# " + tep.getFileName() + " - dữ liệu cửa hàng thú cưng (UTF-8)");
            for (T doiTuong : danhSach) {
                cacDong.add(codec.toLine(doiTuong));
            }
            Path tepTam = tep.resolveSibling(tep.getFileName() + ".tmp");
            Files.write(tepTam, cacDong, StandardCharsets.UTF_8);
            Files.move(tepTam, tep, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new AppException("Không ghi được tệp " + tep + ": " + ex.getMessage(), ex);
        }
    }
}
