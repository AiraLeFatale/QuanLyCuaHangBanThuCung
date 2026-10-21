package petshop.console;

import java.time.LocalDate;

import petshop.exception.AppException;
import petshop.service.CuaHang;
import petshop.util.TrangTri;

/** Menu chính của chương trình quản lý cửa hàng thú cưng. */
public class MenuChinh {

    private final CuaHang cuaHang;
    private final MenuThuCung menuThuCung;
    private final MenuKhachHang menuKhachHang;
    private final MenuNhanVien menuNhanVien;
    private final MenuBanHang menuBanHang;
    private final MenuThongKe menuThongKe;

    public MenuChinh(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
        this.menuThuCung = new MenuThuCung(cuaHang);
        this.menuKhachHang = new MenuKhachHang(cuaHang);
        this.menuNhanVien = new MenuNhanVien(cuaHang);
        this.menuBanHang = new MenuBanHang(cuaHang);
        this.menuThongKe = new MenuThongKe(cuaHang);
    }

    public void chay() {
        boolean thoat = false;
        try {
            while (!thoat) {
                hienThiMenu();
                int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 6);
                try {
                    switch (chon) {
                        case 1 -> menuThuCung.chay();
                        case 2 -> menuKhachHang.chay();
                        case 3 -> menuNhanVien.chay();
                        case 4 -> menuBanHang.chay();
                        case 5 -> menuThongKe.chay();
                        case 6 -> luuDuLieu();
                        default -> thoat = true;
                    }
                } catch (AppException ex) {
                    NhapXuat.loi(ex.getMessage());
                    NhapXuat.tamDung();
                }
            }
        } catch (KetThucNhapException ex) {
            System.out.println("\nĐã kết thúc nhập từ bàn phím.");
        } finally {
            try {
                cuaHang.luuTatCa();
                System.out.println("Đã lưu dữ liệu vào " + cuaHang.getThuMucDuLieu().toAbsolutePath());
            } catch (AppException ex) {
                NhapXuat.loi("Không lưu được dữ liệu: " + ex.getMessage());
            }
            System.out.println("Tạm biệt!");
        }
    }

    private void hienThiMenu() {
        NhapXuat.tieuDe("Cửa hàng thú cưng Petshop");
        System.out.println("  Hôm nay: " + TrangTri.ngay(LocalDate.now())
                + "   |   Thú cưng: " + cuaHang.getThuCung().soLuong()
                + "   Khách hàng: " + cuaHang.getKhachHang().soLuong()
                + "   Nhân viên: " + cuaHang.getNhanVien().soLuong()
                + "   Hóa đơn: " + cuaHang.getHoaDon().soLuong());
        System.out.println();
        System.out.println("  MENU CHÍNH");
        System.out.println("    1. Quản lý thú cưng");
        System.out.println("    2. Quản lý khách hàng");
        System.out.println("    3. Quản lý nhân viên");
        System.out.println("    4. Bán hàng");
        System.out.println("    5. Thống kê - báo cáo");
        System.out.println("    6. Lưu dữ liệu");
        System.out.println("    0. Thoát");
    }

    private void luuDuLieu() {
        try {
            cuaHang.luuTatCa();
            NhapXuat.thongBao("Đã lưu toàn bộ dữ liệu vào " + cuaHang.getThuMucDuLieu().toAbsolutePath());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
    }
}
