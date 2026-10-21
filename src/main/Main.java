package main;

import entity.*;
import inter.IGiamGia;
import java.util.*;
import manager.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    private static final String F_THUCUNG = "data/thucung.txt";
    private static final String F_SANPHAM = "data/sanpham.txt";
    private static final String F_KHACHHANG = "data/khachhang.txt";
    private static final String F_NHANVIEN = "data/nhanvien.txt";
    private static final String F_HOADON = "data/hoadon.txt";

    private static final QuanLyThuCung qlTC = new QuanLyThuCung();
    private static final QuanLySanPham qlSP = new QuanLySanPham();
    private static final QuanLyKhachHang qlKH = new QuanLyKhachHang();
    private static final QuanLyNhanVien qlNV = new QuanLyNhanVien();
    private static final QuanLyHoaDon qlHD = new QuanLyHoaDon();

    public static void main(String[] args) {
        qlTC.docFile(F_THUCUNG);
        qlSP.docFile(F_SANPHAM);
        qlKH.docFile(F_KHACHHANG);
        qlNV.docFile(F_NHANVIEN);
        qlHD.docFile(F_HOADON);

        int chon;
        do {
            System.out.println("\n========== QUAN LY CUA HANG THU CUNG ==========");
            System.out.println("1. Quan ly thu cung");
            System.out.println("2. Quan ly san pham");
            System.out.println("3. Quan ly khach hang");
            System.out.println("4. Quan ly nhan vien");
            System.out.println("5. Quan ly hoa don / ban hang");
            System.out.println("0. Thoat");
            chon = nhapInt("Chon: ");
            switch (chon) {
                case 1: menuThuCung(); break;
                case 2: menuSanPham(); break;
                case 3: menuKhachHang(); break;
                case 4: menuNhanVien(); break;
                case 5: menuHoaDon(); break;
                case 0: System.out.println("Tam biet!"); break;
                default: System.out.println("Lua chon khong hop le!");
            }
        } while (chon != 0);
    }

    // ================= TIEN ICH NHAP LIEU (static) =================
    public static String nhapChuoi(String nhan) {
        System.out.print(nhan);
        String s = sc.nextLine().trim();
        // tranh ky tu phan cach cua file
        return s.replace("|", " ").replace(";", " ").replace("#", " ");
    }

    public static String nhapChuoiKhongRong(String nhan) {
        String s;
        do {
            s = nhapChuoi(nhan);
            if (s.isEmpty()) System.out.println("Khong duoc de trong!");
        } while (s.isEmpty());
        return s;
    }

    public static int nhapInt(String nhan) {
        while (true) {
            System.out.print(nhan);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so nguyen!");
            }
        }
    }

    public static double nhapDouble(String nhan) {
        while (true) {
            System.out.print(nhan);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so!");
            }
        }
    }

    private static void inDanhSach(List<?> ds) {
        if (ds.isEmpty()) {
            System.out.println("Khong co ket qua.");
            return;
        }
        for (Object o : ds) System.out.println(o);
    }

    /** Da hinh qua interface: tong giam gia cua mat hang + khach hang (toi da 30%). */
    private static double tinhGiam(IGiamGia hang, IGiamGia khach) {
        return Math.min(hang.tinhGiamGia() + khach.tinhGiamGia(), 30);
    }

    // ================= THU CUNG =================
    private static void menuThuCung() {
        int c;
        do {
            System.out.println("\n===== QUAN LY THU CUNG =====");
            System.out.println("(So thu cung da khoi tao - bien static: " + ThuCung.getSoLuongDaTao() + ")");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem theo ten");
            System.out.println("6. Nghe tieng keu (da hinh)");
            System.out.println("0. Quay lai");
            c = nhapInt("Chon: ");
            switch (c) {
                case 1: qlTC.hienThiTatCa(); break;
                case 2: themThuCung(); break;
                case 3: suaThuCung(); break;
                case 4: xoaThuCung(); break;
                case 5: inDanhSach(qlTC.timTheoTen(nhapChuoi("Tu khoa ten: "))); break;
                case 6: qlTC.choTatCaKeu(); break;
                case 0: break;
                default: System.out.println("Lua chon khong hop le!");
            }
        } while (c != 0);
    }

    private static void themThuCung() {
        String ma = nhapChuoiKhongRong("Ma thu cung: ");
        if (qlTC.timTheoMa(ma) != null) {
            System.out.println("Ma da ton tai!");
            return;
        }
        System.out.println("Loai: 1. Cho   2. Meo   3. Chim");
        int loai = nhapInt("Chon loai: ");
        if (loai < 1 || loai > 3) {
            System.out.println("Loai khong hop le!");
            return;
        }
        String ten = nhapChuoiKhongRong("Ten: ");
        int tuoi = nhapInt("Tuoi (thang): ");
        double gia = nhapDouble("Gia: ");
        String gt = nhapChuoiKhongRong("Gioi tinh (Duc/Cai): ");
        ThuCung t;
        if (loai == 1) {
            String giong = nhapChuoiKhongRong("Giong: ");
            double canNang = nhapDouble("Can nang (kg): ");
            t = new Cho(ma, ten, tuoi, gia, gt, false, giong, canNang);
        } else if (loai == 2) {
            String mau = nhapChuoiKhongRong("Mau long: ");
            boolean dai = nhapInt("Long dai? (1 = co, 0 = khong): ") == 1;
            t = new Meo(ma, ten, tuoi, gia, gt, false, mau, dai);
        } else {
            String mau = nhapChuoiKhongRong("Mau sac: ");
            boolean noi = nhapInt("Biet noi? (1 = co, 0 = khong): ") == 1;
            t = new Chim(ma, ten, tuoi, gia, gt, false, mau, noi);
        }
        qlTC.them(t);
        qlTC.ghiFile(F_THUCUNG);
        System.out.println("Da them thu cung.");
    }

    private static void suaThuCung() {
        String ma = nhapChuoiKhongRong("Ma thu cung can sua: ");
        if (qlTC.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay ma " + ma);
            return;
        }
        String ten = nhapChuoiKhongRong("Ten moi: ");
        int tuoi = nhapInt("Tuoi moi (thang): ");
        double gia = nhapDouble("Gia moi: ");
        String gt = nhapChuoiKhongRong("Gioi tinh moi: ");
        qlTC.sua(ma, ten, tuoi, gia, gt);
        qlTC.ghiFile(F_THUCUNG);
        System.out.println("Da cap nhat.");
    }

    private static void xoaThuCung() {
        String ma = nhapChuoiKhongRong("Ma thu cung can xoa: ");
        if (qlTC.xoa(ma)) {
            qlTC.ghiFile(F_THUCUNG);
            System.out.println("Da xoa.");
        } else {
            System.out.println("Khong tim thay ma " + ma);
        }
    }

    // ================= SAN PHAM =================
    private static void menuSanPham() {
        int c;
        do {
            System.out.println("\n===== QUAN LY SAN PHAM =====");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem theo ten");
            System.out.println("0. Quay lai");
            c = nhapInt("Chon: ");
            switch (c) {
                case 1: qlSP.hienThiTatCa(); break;
                case 2: themSanPham(); break;
                case 3: suaSanPham(); break;
                case 4: xoaSanPham(); break;
                case 5: inDanhSach(qlSP.timTheoTen(nhapChuoi("Tu khoa ten: "))); break;
                case 0: break;
                default: System.out.println("Lua chon khong hop le!");
            }
        } while (c != 0);
    }

    private static void themSanPham() {
        String ma = nhapChuoiKhongRong("Ma san pham: ");
        if (qlSP.timTheoMa(ma) != null) {
            System.out.println("Ma da ton tai!");
            return;
        }
        System.out.println("Loai: 1. Thuc an   2. Thuoc   3. Phu kien");
        int loai = nhapInt("Chon loai: ");
        if (loai < 1 || loai > 3) {
            System.out.println("Loai khong hop le!");
            return;
        }
        String ten = nhapChuoiKhongRong("Ten san pham: ");
        double gia = nhapDouble("Gia: ");
        int sl = nhapInt("So luong: ");
        SanPham sp;
        if (loai == 1) {
            String loaiTC = nhapChuoiKhongRong("Danh cho (Cho/Meo/Chim): ");
            double kg = nhapDouble("Trong luong (kg): ");
            String hsd = nhapChuoiKhongRong("Han su dung (dd/MM/yyyy): ");
            sp = new ThucAn(ma, ten, gia, sl, loaiTC, kg, hsd);
        } else if (loai == 2) {
            String congDung = nhapChuoiKhongRong("Cong dung: ");
            String hsd = nhapChuoiKhongRong("Han su dung (dd/MM/yyyy): ");
            sp = new Thuoc(ma, ten, gia, sl, congDung, hsd);
        } else {
            String chatLieu = nhapChuoiKhongRong("Chat lieu: ");
            String mau = nhapChuoiKhongRong("Mau sac: ");
            sp = new PhuKien(ma, ten, gia, sl, chatLieu, mau);
        }
        qlSP.them(sp);
        qlSP.ghiFile(F_SANPHAM);
        System.out.println("Da them san pham.");
    }

    private static void suaSanPham() {
        String ma = nhapChuoiKhongRong("Ma san pham can sua: ");
        if (qlSP.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay ma " + ma);
            return;
        }
        String ten = nhapChuoiKhongRong("Ten moi: ");
        double gia = nhapDouble("Gia moi: ");
        int sl = nhapInt("So luong moi: ");
        qlSP.sua(ma, ten, gia, sl);
        qlSP.ghiFile(F_SANPHAM);
        System.out.println("Da cap nhat.");
    }

    private static void xoaSanPham() {
        String ma = nhapChuoiKhongRong("Ma san pham can xoa: ");
        if (qlSP.xoa(ma)) {
            qlSP.ghiFile(F_SANPHAM);
            System.out.println("Da xoa.");
        } else {
            System.out.println("Khong tim thay ma " + ma);
        }
    }

    // ================= KHACH HANG =================
    private static void menuKhachHang() {
        int c;
        do {
            System.out.println("\n===== QUAN LY KHACH HANG =====");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem theo ten");
            System.out.println("0. Quay lai");
            c = nhapInt("Chon: ");
            switch (c) {
                case 1: qlKH.hienThiTatCa(); break;
                case 2: themKhachHang(); break;
                case 3: suaKhachHang(); break;
                case 4: xoaKhachHang(); break;
                case 5: inDanhSach(qlKH.timTheoTen(nhapChuoi("Tu khoa ten: "))); break;
                case 0: break;
                default: System.out.println("Lua chon khong hop le!");
            }
        } while (c != 0);
    }

    private static void themKhachHang() {
        String ma = nhapChuoiKhongRong("Ma khach hang: ");
        if (qlKH.timTheoMa(ma) != null) {
            System.out.println("Ma da ton tai!");
            return;
        }
        String ten = nhapChuoiKhongRong("Ho ten: ");
        String sdt = nhapChuoiKhongRong("So dien thoai: ");
        String dc = nhapChuoiKhongRong("Dia chi: ");
        qlKH.them(new KhachHang(ma, ten, sdt, dc, 0));
        qlKH.ghiFile(F_KHACHHANG);
        System.out.println("Da them khach hang.");
    }

    private static void suaKhachHang() {
        String ma = nhapChuoiKhongRong("Ma khach hang can sua: ");
        if (qlKH.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay ma " + ma);
            return;
        }
        String ten = nhapChuoiKhongRong("Ho ten moi: ");
        String sdt = nhapChuoiKhongRong("SDT moi: ");
        String dc = nhapChuoiKhongRong("Dia chi moi: ");
        qlKH.sua(ma, ten, sdt, dc);
        qlKH.ghiFile(F_KHACHHANG);
        System.out.println("Da cap nhat.");
    }

    private static void xoaKhachHang() {
        String ma = nhapChuoiKhongRong("Ma khach hang can xoa: ");
        if (qlKH.xoa(ma)) {
            qlKH.ghiFile(F_KHACHHANG);
            System.out.println("Da xoa.");
        } else {
            System.out.println("Khong tim thay ma " + ma);
        }
    }

    // ================= NHAN VIEN =================
    private static void menuNhanVien() {
        int c;
        do {
            System.out.println("\n===== QUAN LY NHAN VIEN =====");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem theo ten");
            System.out.println("0. Quay lai");
            c = nhapInt("Chon: ");
            switch (c) {
                case 1: qlNV.hienThiTatCa(); break;
                case 2: themNhanVien(); break;
                case 3: suaNhanVien(); break;
                case 4: xoaNhanVien(); break;
                case 5: inDanhSach(qlNV.timTheoTen(nhapChuoi("Tu khoa ten: "))); break;
                case 0: break;
                default: System.out.println("Lua chon khong hop le!");
            }
        } while (c != 0);
    }

    private static void themNhanVien() {
        String ma = nhapChuoiKhongRong("Ma nhan vien: ");
        if (qlNV.timTheoMa(ma) != null) {
            System.out.println("Ma da ton tai!");
            return;
        }
        String ten = nhapChuoiKhongRong("Ho ten: ");
        String sdt = nhapChuoiKhongRong("So dien thoai: ");
        String dc = nhapChuoiKhongRong("Dia chi: ");
        String cv = nhapChuoiKhongRong("Chuc vu: ");
        double luong = nhapDouble("Luong: ");
        qlNV.them(new NhanVien(ma, ten, sdt, dc, cv, luong));
        qlNV.ghiFile(F_NHANVIEN);
        System.out.println("Da them nhan vien.");
    }

    private static void suaNhanVien() {
        String ma = nhapChuoiKhongRong("Ma nhan vien can sua: ");
        if (qlNV.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay ma " + ma);
            return;
        }
        String ten = nhapChuoiKhongRong("Ho ten moi: ");
        String sdt = nhapChuoiKhongRong("SDT moi: ");
        String dc = nhapChuoiKhongRong("Dia chi moi: ");
        String cv = nhapChuoiKhongRong("Chuc vu moi: ");
        double luong = nhapDouble("Luong moi: ");
        qlNV.sua(ma, ten, sdt, dc, cv, luong);
        qlNV.ghiFile(F_NHANVIEN);
        System.out.println("Da cap nhat.");
    }

    private static void xoaNhanVien() {
        String ma = nhapChuoiKhongRong("Ma nhan vien can xoa: ");
        if (qlNV.xoa(ma)) {
            qlNV.ghiFile(F_NHANVIEN);
            System.out.println("Da xoa.");
        } else {
            System.out.println("Khong tim thay ma " + ma);
        }
    }

    // ================= HOA DON =================
    private static void menuHoaDon() {
        int c;
        do {
            System.out.println("\n===== QUAN LY HOA DON / BAN HANG =====");
            System.out.println("1. Xem danh sach hoa don");
            System.out.println("2. Lap hoa don moi (ban hang)");
            System.out.println("3. Sua hoa don (nhan vien, ngay lap)");
            System.out.println("4. Xoa hoa don");
            System.out.println("5. Tim hoa don theo ma khach hang");
            System.out.println("6. Thong ke doanh thu");
            System.out.println("0. Quay lai");
            c = nhapInt("Chon: ");
            switch (c) {
                case 1: qlHD.hienThiTatCa(); break;
                case 2: lapHoaDon(); break;
                case 3: suaHoaDon(); break;
                case 4: xoaHoaDon(); break;
                case 5:
                    List<HoaDon> kq = qlHD.timTheoKhachHang(nhapChuoiKhongRong("Ma khach hang: "));
                    if (kq.isEmpty()) System.out.println("Khong co hoa don.");
                    for (HoaDon hd : kq) hd.inChiTiet();
                    break;
                case 6:
                    System.out.printf("Tong so hoa don: %d | Tong doanh thu: %,.0f d%n",
                            qlHD.getDs().size(), qlHD.tongDoanhThu());
                    break;
                case 0: break;
                default: System.out.println("Lua chon khong hop le!");
            }
        } while (c != 0);
    }

    private static void lapHoaDon() {
        KhachHang kh = qlKH.timTheoMa(nhapChuoiKhongRong("Ma khach hang: "));
        if (kh == null) {
            System.out.println("Khong tim thay khach hang!");
            return;
        }
        NhanVien nv = qlNV.timTheoMa(nhapChuoiKhongRong("Ma nhan vien lap hoa don: "));
        if (nv == null) {
            System.out.println("Khong tim thay nhan vien!");
            return;
        }
        HoaDon hd = new HoaDon(qlHD.taoMaMoi(), kh.getMa(), nv.getMa(), HoaDon.ngayHienTai());
        System.out.println("Khach: " + kh.getHoTen() + " (giam " + kh.tinhGiamGia() + "%)");

        while (true) {
            System.out.println("\n1. Them thu cung   2. Them san pham   0. Ket thuc hoa don");
            int loai = nhapInt("Chon: ");
            if (loai == 0) break;
            if (loai == 1) {
                ThuCung t = qlTC.timTheoMa(nhapChuoiKhongRong("Ma thu cung: "));
                if (t == null) {
                    System.out.println("Khong tim thay thu cung!");
                } else if (t.isDaBan()) {
                    System.out.println("Thu cung nay da ban!");
                } else {
                    double giam = tinhGiam(t, kh);
                    hd.themChiTiet(new ChiTietHoaDon(t.getMaThuCung(), t.getTen(), 1, t.getGia(), giam));
                    t.setDaBan(true);
                    System.out.println("Da them " + t.getTen() + " (giam " + giam + "%)");
                }
            } else if (loai == 2) {
                SanPham sp = qlSP.timTheoMa(nhapChuoiKhongRong("Ma san pham: "));
                if (sp == null) {
                    System.out.println("Khong tim thay san pham!");
                } else {
                    int sl = nhapInt("So luong (con " + sp.getSoLuong() + "): ");
                    if (sl <= 0 || sl > sp.getSoLuong()) {
                        System.out.println("So luong khong hop le!");
                    } else {
                        double giam = tinhGiam(sp, kh);
                        hd.themChiTiet(new ChiTietHoaDon(sp.getMaSP(), sp.getTenSP(), sl, sp.getGia(), giam));
                        sp.setSoLuong(sp.getSoLuong() - sl);
                        System.out.println("Da them " + sp.getTenSP() + " (giam " + giam + "%)");
                    }
                }
            } else {
                System.out.println("Lua chon khong hop le!");
            }
        }

        if (hd.getChiTiet().isEmpty()) {
            System.out.println("Hoa don trong, khong luu.");
            return;
        }
        kh.congDiem(hd.tongTien());
        qlHD.them(hd);
        qlTC.ghiFile(F_THUCUNG);
        qlSP.ghiFile(F_SANPHAM);
        qlKH.ghiFile(F_KHACHHANG);
        qlHD.ghiFile(F_HOADON);
        System.out.println("\n=== DA LAP HOA DON ===");
        hd.inChiTiet();
    }

    private static void suaHoaDon() {
        String ma = nhapChuoiKhongRong("Ma hoa don can sua: ");
        if (qlHD.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay ma " + ma);
            return;
        }
        String maNV = nhapChuoiKhongRong("Ma nhan vien moi: ");
        if (qlNV.timTheoMa(maNV) == null) {
            System.out.println("Khong tim thay nhan vien!");
            return;
        }
        String ngay = nhapChuoiKhongRong("Ngay lap moi (dd/MM/yyyy): ");
        qlHD.sua(ma, maNV, ngay);
        qlHD.ghiFile(F_HOADON);
        System.out.println("Da cap nhat.");
    }

    private static void xoaHoaDon() {
        String ma = nhapChuoiKhongRong("Ma hoa don can xoa: ");
        if (qlHD.xoa(ma)) {
            qlHD.ghiFile(F_HOADON);
            System.out.println("Da xoa.");
        } else {
            System.out.println("Khong tim thay ma " + ma);
        }
    }
}
