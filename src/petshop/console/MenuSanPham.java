package petshop.console;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import petshop.exception.AppException;
import petshop.model.PhuKien;
import petshop.model.SanPham;
import petshop.model.ThucAn;
import petshop.model.Thuoc;
import petshop.service.CuaHang;
import petshop.util.Tien;

/** Menu quản lý sản phẩm (thức ăn, thuốc, phụ kiện) trên console. */
public class MenuSanPham {

    private static final String[] COT = {"Mã", "Tên", "Loại", "Giá bán", "SL", "Đặc điểm"};
    private static final int[] RONG = {6, 20, 10, 13, 4, 38};

    private final CuaHang cuaHang;

    public MenuSanPham(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public void chay() {
        while (true) {
            NhapXuat.tieuDe("Quản lý sản phẩm");
            System.out.println("  1. Xem danh sách");
            System.out.println("  2. Thêm sản phẩm");
            System.out.println("  3. Cập nhật sản phẩm");
            System.out.println("  4. Xóa sản phẩm");
            System.out.println("  5. Tìm kiếm / lọc theo loại");
            System.out.println("  6. Sắp xếp theo giá");
            System.out.println("  0. Quay lại menu chính");

            int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 6);
            switch (chon) {
                case 1 -> {
                    xemDanhSach(cuaHang.getSanPham().tatCa(), "Danh sách sản phẩm");
                    NhapXuat.tamDung();
                }
                case 2 -> them();
                case 3 -> capNhat();
                case 4 -> xoa();
                case 5 -> timKiem();
                case 6 -> sapXep();
                default -> {
                    return;
                }
            }
        }
    }

    private void xemDanhSach(List<SanPham> danhSach, String tieuDe) {
        NhapXuat.muc(tieuDe + " (" + danhSach.size() + " bản ghi)");
        List<String[]> dong = new ArrayList<>();
        for (SanPham sp : danhSach) {
            dong.add(new String[]{
                    sp.getMa(),
                    sp.getTen(),
                    sp.getLoai(),
                    Tien.dinhDang(sp.getGiaBan()),
                    String.valueOf(sp.getSoLuong()),
                    sp.getDacDiem()
            });
        }
        NhapXuat.inBang(COT, dong, RONG);
    }

    private void them() {
        NhapXuat.muc("Thêm sản phẩm");
        String ma = cuaHang.getSanPham().sinhMa();
        System.out.println("Mã tự động: " + ma);
        try {
            SanPham moi = nhapThongTin(ma, null);
            cuaHang.getSanPham().them(moi);
            cuaHang.getSanPham().luu();
            NhapXuat.thongBao("Đã thêm " + moi.getMa() + " - " + moi.getTen());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void capNhat() {
        NhapXuat.muc("Cập nhật sản phẩm");
        String ma = NhapXuat.docBatBuoc("Mã sản phẩm: ", "Mã sản phẩm");
        Optional<SanPham> tim = cuaHang.getSanPham().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy sản phẩm " + ma);
            NhapXuat.tamDung();
            return;
        }
        SanPham cu = tim.get();
        System.out.println("Hiện tại: " + cu.getTen() + " - " + cu.getLoai()
                + " - " + Tien.dinhDang(cu.getGiaBan()) + " - SL " + cu.getSoLuong());
        System.out.println("(Để trống ở mỗi câu hỏi là giữ nguyên giá trị cũ)");
        try {
            SanPham moi = nhapThongTin(ma, cu);
            cuaHang.getSanPham().capNhat(moi);
            cuaHang.getSanPham().luu();
            NhapXuat.thongBao("Đã cập nhật " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void xoa() {
        NhapXuat.muc("Xóa sản phẩm");
        String ma = NhapXuat.docBatBuoc("Mã sản phẩm: ", "Mã sản phẩm");
        Optional<SanPham> tim = cuaHang.getSanPham().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy sản phẩm " + ma);
            NhapXuat.tamDung();
            return;
        }
        SanPham sp = tim.get();
        System.out.println("Sẽ xóa: " + sp.getMa() + " - " + sp.getTen() + " (" + sp.getLoai() + ")");
        if (!NhapXuat.xacNhan("Xác nhận xóa?")) {
            NhapXuat.thongBao("Đã hủy thao tác");
            NhapXuat.tamDung();
            return;
        }
        try {
            cuaHang.getSanPham().xoa(ma);
            cuaHang.getSanPham().luu();
            NhapXuat.thongBao("Đã xóa " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void timKiem() {
        NhapXuat.muc("Tìm kiếm sản phẩm");
        String tuKhoa = NhapXuat.doc("Từ khóa (mã / tên / đặc điểm), bỏ trống để bỏ qua: ");
        System.out.println("  Lọc loại: 0-Tất cả  1-Thức ăn  2-Thuốc  3-Phụ kiện");
        int chon = NhapXuat.docSoNguyen("  Chọn: ", 0, 3);
        String loai = switch (chon) {
            case 1 -> "Thức ăn";
            case 2 -> "Thuốc";
            case 3 -> "Phụ kiện";
            default -> "Tất cả";
        };
        xemDanhSach(cuaHang.getSanPham().timKiem(tuKhoa, loai), "Kết quả tìm kiếm");
        NhapXuat.tamDung();
    }

    private void sapXep() {
        NhapXuat.muc("Sắp xếp sản phẩm theo giá");
        int chon = NhapXuat.docSoNguyen("  1-Tăng dần  2-Giảm dần: ", 1, 2);
        xemDanhSach(cuaHang.getSanPham().sapXepTheoGia(chon == 1), "Danh sách theo giá");
        NhapXuat.tamDung();
    }

    private SanPham nhapThongTin(String ma, SanPham coDuLieu) {
        String ten = HoiNhap.chuoi("  Tên sản phẩm", coDuLieu == null ? null : coDuLieu.getTen());
        double giaBan = HoiNhap.tien("  Giá bán", coDuLieu == null ? 0 : coDuLieu.getGiaBan());
        int soLuong = HoiNhap.soNguyen("  Số lượng", coDuLieu == null ? 1 : coDuLieu.getSoLuong(), 0, 9999);

        String loai = hoiLoai(coDuLieu == null ? null : coDuLieu.getLoai());
        boolean cungLoai = coDuLieu != null && coDuLieu.getLoai().equals(loai);

        return switch (loai) {
            case "Thức ăn" -> {
                ThucAn cu = cungLoai ? (ThucAn) coDuLieu : null;
                String loaiThuCung = HoiNhap.chuoi("  Dành cho (Chó/Mèo/Chim)",
                        cu == null ? null : cu.getLoaiThuCung());
                double kg = hoiSoThuc("  Khối lượng (kg)", cu == null ? 1 : cu.getKhoiLuongKg(), 0.1, 100);
                LocalDate hsd = HoiNhap.ngay("  Hạn sử dụng", cu == null ? null : cu.getHanSuDung());
                yield new ThucAn(ma, ten, giaBan, soLuong, loaiThuCung, kg, hsd);
            }
            case "Thuốc" -> {
                Thuoc cu = cungLoai ? (Thuoc) coDuLieu : null;
                String congDung = HoiNhap.chuoi("  Công dụng", cu == null ? null : cu.getCongDung());
                LocalDate hsd = HoiNhap.ngay("  Hạn sử dụng", cu == null ? null : cu.getHanSuDung());
                boolean keDon = HoiNhap.coKhong("  Cần kê đơn", cu != null && cu.isCanKeDon());
                yield new Thuoc(ma, ten, giaBan, soLuong, congDung, hsd, keDon);
            }
            default -> {
                PhuKien cu = cungLoai ? (PhuKien) coDuLieu : null;
                String chatLieu = HoiNhap.chuoi("  Chất liệu", cu == null ? null : cu.getChatLieu());
                String mauSac = HoiNhap.chuoi("  Màu sắc", cu == null ? null : cu.getMauSac());
                yield new PhuKien(ma, ten, giaBan, soLuong, chatLieu, mauSac);
            }
        };
    }

    private String hoiLoai(String hienTai) {
        System.out.println("  Loại: 1-Thức ăn  2-Thuốc  3-Phụ kiện"
                + (hienTai == null ? "" : "  0-Giữ nguyên (" + hienTai + ")"));
        int chon = NhapXuat.docSoNguyen("  Chọn loại: ", hienTai == null ? 1 : 0, 3);
        return switch (chon) {
            case 0 -> hienTai;
            case 1 -> "Thức ăn";
            case 2 -> "Thuốc";
            default -> "Phụ kiện";
        };
    }

    private double hoiSoThuc(String nhan, double hienTai, double nhoNhat, double lonNhat) {
        while (true) {
            String s = NhapXuat.doc(nhan + " [" + TrangTriSo.hienThi(hienTai) + "]: ");
            if (s.isEmpty()) {
                return hienTai;
            }
            try {
                double giaTri = Double.parseDouble(s.replace(',', '.'));
                if (giaTri < nhoNhat || giaTri > lonNhat) {
                    NhapXuat.loi(nhan + " phải trong khoảng " + nhoNhat + " - " + lonNhat);
                    continue;
                }
                return giaTri;
            } catch (NumberFormatException ex) {
                NhapXuat.loi(nhan + " phải là số");
            }
        }
    }

    /** Hiển thị số gọn cho câu hỏi nhập liệu. */
    private static final class TrangTriSo {
        private TrangTriSo() {
        }

        static String hienThi(double giaTri) {
            if (giaTri == Math.rint(giaTri)) {
                return String.valueOf((long) giaTri);
            }
            return String.format(java.util.Locale.ROOT, "%.2f", giaTri);
        }
    }
}
