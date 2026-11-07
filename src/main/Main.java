package main;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.Arrays;

import manager.CuaHang;
import menu.MenuChinh;
import util.AppException;


/** Điểm khởi động của chương trình console. */
public class Main {

    public static void main(String[] args) {
        // Bảo đảm tiếng Việt hiển thị đúng trên console Windows (cần chcp 65001).
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
            // giữ System.out mặc định nếu không thay được
        }

        if (Arrays.asList(args).contains("--check")) {
            KiemTraNhanh.chay();
            return;
        }

        String thuMucDuLieu = "data";
        try {
            CuaHang cuaHang = new CuaHang(thuMucDuLieu);
            if (DuLieuMau.napNeuTrong(cuaHang)) {
                System.out.println("Lần chạy đầu tiên: đã nạp dữ liệu mẫu vào "
                        + Paths.get(thuMucDuLieu).toAbsolutePath());
            }
            new MenuChinh(cuaHang).chay();
        } catch (AppException ex) {
            System.out.println("Không khởi động được chương trình: " + ex.getMessage());
            System.exit(1);
        }
    }
}
