package petshop.app;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import petshop.console.MenuChinh;
import petshop.exception.AppException;
import petshop.service.CuaHang;

/** Điểm khởi động của chương trình console. */
public class Main {

    public static void main(String[] args) {
        // Bảo đảm tiếng Việt hiển thị đúng trên console Windows (cần chcp 65001).
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        } catch (RuntimeException ignored) {
            // giữ System.out mặc định nếu không thay được
        }

        if (Arrays.asList(args).contains("--check")) {
            KiemTraNhanh.chay();
            return;
        }

        Path thuMucDuLieu = Paths.get("data");
        try {
            CuaHang cuaHang = new CuaHang(thuMucDuLieu);
            if (DuLieuMau.napNeuTrong(cuaHang)) {
                System.out.println("Lần chạy đầu tiên: đã nạp dữ liệu mẫu vào " + thuMucDuLieu.toAbsolutePath());
            }
            new MenuChinh(cuaHang).chay();
        } catch (AppException ex) {
            System.out.println("Không khởi động được chương trình: " + ex.getMessage());
            System.exit(1);
        }
    }
}
