package menu;

import java.time.LocalDate;

import manager.CuaHang;
import util.AppException;
import util.TrangTri;


/** Menu chính của chương trình quản lý cửa hàng thú cưng. */
public class MenuChinh {

    private final CuaHang cuaHang;
    private final MenuThuCung menuThuCung;
    private final MenuSanPham menuSanPham;
    private final MenuDichVu menuDichVu;
    private final MenuKhachHang menuKhachHang;
    private final MenuNhanVien menuNhanVien;
    private final MenuBanHang menuBanHang;
    private final MenuHoaDon menuHoaDon;
    private final MenuThongKe menuThongKe;

    public MenuChinh(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
        this.menuThuCung = new MenuThuCung(cuaHang);
        this.menuSanPham = new MenuSanPham(cuaHang);
        this.menuDichVu = new MenuDichVu(cuaHang);
        this.menuKhachHang = new MenuKhachHang(cuaHang);
        this.menuNhanVien = new MenuNhanVien(cuaHang);
        this.menuBanHang = new MenuBanHang(cuaHang);
        this.menuHoaDon = new MenuHoaDon(cuaHang, menuBanHang);
        this.menuThongKe = new MenuThongKe(cuaHang);
    }

    public void chay() {
        boolean thoat = false;
        try {
            while (!thoat) {
                hienThiMenu();
                int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 9);
                try {
                    switch (chon) {
                        case 1:
                            menuThuCung.chay();
                            break;
                        case 2:
                            menuSanPham.chay();
                            break;
                        case 3:
                            menuDichVu.chay();
                            break;
                        case 4:
                            menuKhachHang.chay();
                            break;
                        case 5:
                            menuNhanVien.chay();
                            break;
                        case 6:
                            menuBanHang.chay();
                            break;
                        case 7:
                            menuHoaDon.chay();
                            break;
                        case 8:
                            menuThongKe.chay();
                            break;
                        case 9:
                            luuDuLieu();
                            break;
                        default:
                            thoat = true;
                            break;
                    }
                } catch (AppException ex) {
                    NhapXuat.loi(ex.getMessage());
                    NhapXuat.tamDung();
                }
            }
        } catch (NhapXuat.KetThucNhapException ex) {
            System.out.println("\nĐã kết thúc nhập từ bàn phím.");
        } finally {
            try {
                cuaHang.luuTatCa();
                System.out.println("Đã lưu dữ liệu vào "
                        + java.nio.file.Paths.get(cuaHang.getThuMucDuLieu()).toAbsolutePath());
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
                + "   Sản phẩm: " + cuaHang.getSanPham().soLuong()
                + "   Dịch vụ: " + cuaHang.getDichVu().soLuong());
        System.out.println("  Khách hàng: " + cuaHang.getKhachHang().soLuong()
                + "   Nhân viên: " + cuaHang.getNhanVien().soLuong()
                + "   Hóa đơn: " + cuaHang.getHoaDon().soLuong());
        System.out.println();
        System.out.println("  MENU CHÍNH");
        System.out.println("    1. Quản lý thú cưng");
        System.out.println("    2. Quản lý sản phẩm");
        System.out.println("    3. Quản lý dịch vụ");
        System.out.println("    4. Quản lý khách hàng");
        System.out.println("    5. Quản lý nhân viên");
        System.out.println("    6. Bán hàng");
        System.out.println("    7. Quản lý hóa đơn");
        System.out.println("    8. Thống kê - báo cáo");
        System.out.println("    9. Lưu dữ liệu");
        System.out.println("    0. Thoát");
    }

    private void luuDuLieu() {
        try {
            cuaHang.luuTatCa();
            NhapXuat.thongBao("Đã lưu toàn bộ dữ liệu vào "
                    + java.nio.file.Paths.get(cuaHang.getThuMucDuLieu()).toAbsolutePath());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
    }
}
