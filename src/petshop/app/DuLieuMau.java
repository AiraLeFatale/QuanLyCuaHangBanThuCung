package petshop.app;

import java.time.LocalDate;

import petshop.model.Cho;
import petshop.model.Chim;
import petshop.model.GioiTinh;
import petshop.model.KhachHang;
import petshop.model.Meo;
import petshop.model.NhanVien;
import petshop.model.TrangThaiThuCung;
import petshop.service.CuaHang;

/**
 * Dữ liệu mẫu, chỉ nạp khi cả 4 tệp dữ liệu đang trống
 * (lần chạy đầu tiên) để tiện demo và chấm bài.
 */
public final class DuLieuMau {

    private DuLieuMau() {
    }

    public static boolean napNeuTrong(CuaHang cuaHang) {
        boolean trong = cuaHang.getThuCung().soLuong() == 0
                && cuaHang.getKhachHang().soLuong() == 0
                && cuaHang.getNhanVien().soLuong() == 0
                && cuaHang.getHoaDon().soLuong() == 0;
        if (!trong) {
            return false;
        }
        napThuCung(cuaHang);
        napKhachHang(cuaHang);
        napNhanVien(cuaHang);
        cuaHang.luuTatCa();
        return true;
    }

    private static void napThuCung(CuaHang ch) {
        var ds = ch.getThuCung();
        ds.them(new Cho("TC001", "Milu", LocalDate.of(2025, 3, 12), 6_500_000, 2,
                TrangThaiThuCung.CON_BAN, "Poodle", "Nhỏ", true));
        ds.them(new Cho("TC002", "Bông", LocalDate.of(2024, 11, 2), 12_000_000, 1,
                TrangThaiThuCung.CON_BAN, "Golden Retriever", "Lớn", true));
        ds.them(new Cho("TC003", "Lu", LocalDate.of(2025, 7, 20), 15_000_000, 1,
                TrangThaiThuCung.CON_BAN, "Corgi", "Vừa", false));
        ds.them(new Meo("TC004", "Miu", LocalDate.of(2025, 1, 8), 8_000_000, 3,
                TrangThaiThuCung.CON_BAN, "Anh lông ngắn", false, true));
        ds.them(new Meo("TC005", "Mun", LocalDate.of(2025, 5, 15), 1_200_000, 4,
                TrangThaiThuCung.CON_BAN, "Mèo ta", false, false));
        ds.them(new Meo("TC006", "Bối", LocalDate.of(2024, 9, 30), 9_500_000, 1,
                TrangThaiThuCung.CON_BAN, "Ba Tư", true, true));
        ds.them(new Chim("TC007", "Két", LocalDate.of(2025, 2, 20), 1_800_000, 2,
                TrangThaiThuCung.CON_BAN, "Vẹt Úc", true, "Xanh lá"));
        ds.them(new Chim("TC008", "Chào", LocalDate.of(2025, 6, 1), 700_000, 3,
                TrangThaiThuCung.CON_BAN, "Chào mào", false, "Nâu đỏ"));
        ds.them(new Chim("TC009", "Vàng Anh", LocalDate.of(2025, 8, 5), 450_000, 0,
                TrangThaiThuCung.DA_BAN, "Vàng anh", false, "Vàng"));
        ds.them(new Meo("TC010", "Sushi", LocalDate.of(2025, 4, 18), 4_500_000, 1,
                TrangThaiThuCung.CACH_LY, "Munchkin", false, true));
    }

    private static void napKhachHang(CuaHang ch) {
        var ds = ch.getKhachHang();
        ds.them(new KhachHang("KH001", "Nguyễn Văn An", GioiTinh.NAM, LocalDate.of(1995, 4, 12),
                "0901234567", "12 Lê Lợi, Quận 1, TP.HCM", 520));
        ds.them(new KhachHang("KH002", "Trần Thị Bình", GioiTinh.NU, LocalDate.of(1998, 9, 3),
                "0912345678", "45 Nguyễn Huệ, Quận 1, TP.HCM", 310));
        ds.them(new KhachHang("KH003", "Lê Minh Cường", GioiTinh.NAM, LocalDate.of(2000, 1, 25),
                "0987654321", "88 Cách Mạng Tháng 8, Quận 3, TP.HCM", 80));
        ds.them(new KhachHang("KH004", "Phạm Thu Dung", GioiTinh.NU, LocalDate.of(2002, 6, 17),
                "0938111222", "20 Võ Văn Ngân, Thủ Đức, TP.HCM", 15));
    }

    private static void napNhanVien(CuaHang ch) {
        var ds = ch.getNhanVien();
        ds.them(new NhanVien("NV001", "Trần Quốc Bảo", GioiTinh.NAM, LocalDate.of(1990, 2, 8),
                "0903111222", "5 Pasteur, Quận 3, TP.HCM", "Quản lý", 15_000_000, LocalDate.of(2020, 5, 2)));
        ds.them(new NhanVien("NV002", "Nguyễn Thị Hoa", GioiTinh.NU, LocalDate.of(1997, 7, 19),
                "0909333444", "31 Điện Biên Phủ, Quận 3, TP.HCM", "Bán hàng", 8_000_000, LocalDate.of(2022, 8, 15)));
        ds.them(new NhanVien("NV003", "Đỗ Văn Khánh", GioiTinh.NAM, LocalDate.of(1999, 12, 1),
                "0914555666", "90 Lý Thường Kiệt, Quận 10, TP.HCM", "Chăm sóc", 7_000_000, LocalDate.of(2023, 3, 20)));
    }
}
