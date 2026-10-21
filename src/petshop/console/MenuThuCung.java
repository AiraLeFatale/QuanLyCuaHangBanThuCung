package petshop.console;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import petshop.exception.AppException;
import petshop.model.Cho;
import petshop.model.Chim;
import petshop.model.Meo;
import petshop.model.ThuCung;
import petshop.model.TrangThaiThuCung;
import petshop.service.CuaHang;
import petshop.util.Tien;
import petshop.util.TrangTri;

/** Menu quản lý thú cưng trên console. */
public class MenuThuCung {

    private static final String[] COT = {"Mã", "Tên", "Loại", "Ngày sinh", "Giá bán", "SL", "Trạng thái", "Đặc điểm"};
    private static final int[] RONG = {6, 12, 5, 10, 12, 3, 10, 34};

    private final CuaHang cuaHang;

    public MenuThuCung(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public void chay() {
        while (true) {
            NhapXuat.tieuDe("Quản lý thú cưng");
            System.out.println("  (Thuộc tính static) Số đối tượng thú cưng đã tạo: "
                    + ThuCung.getSoLuongDaTao());
            System.out.println("  1. Xem danh sách");
            System.out.println("  2. Thêm thú cưng");
            System.out.println("  3. Cập nhật thú cưng");
            System.out.println("  4. Xóa thú cưng");
            System.out.println("  5. Tìm kiếm / lọc theo loại");
            System.out.println("  6. Sắp xếp theo giá");
            System.out.println("  0. Quay lại menu chính");

            int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 6);
            switch (chon) {
                case 1 -> {
                    xemDanhSach(cuaHang.getThuCung().tatCa(), "Danh sách thú cưng");
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

    private void xemDanhSach(List<ThuCung> danhSach, String tieuDe) {
        NhapXuat.muc(tieuDe + " (" + danhSach.size() + " bản ghi)");
        List<String[]> dong = new ArrayList<>();
        for (ThuCung t : danhSach) {
            dong.add(new String[]{
                    t.getMa(),
                    t.getTen(),
                    t.getLoai(),
                    TrangTri.ngay(t.getNgaySinh()),
                    Tien.dinhDang(t.getGiaBan()),
                    String.valueOf(t.getSoLuong()),
                    t.getTrangThai().getNhan(),
                    t.getDacDiem()
            });
        }
        NhapXuat.inBang(COT, dong, RONG);
    }

    private void them() {
        NhapXuat.muc("Thêm thú cưng mới");
        String ma = cuaHang.getThuCung().sinhMa();
        System.out.println("Mã tự động: " + ma);
        try {
            ThuCung moi = nhapThongTin(ma, null);
            cuaHang.getThuCung().them(moi);
            cuaHang.getThuCung().luu();
            NhapXuat.thongBao("Đã thêm " + moi.getMa() + " - " + moi.getTen());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void capNhat() {
        NhapXuat.muc("Cập nhật thú cưng");
        String ma = NhapXuat.docBatBuoc("Mã thú cưng: ", "Mã thú cưng");
        Optional<ThuCung> tim = cuaHang.getThuCung().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy thú cưng " + ma);
            NhapXuat.tamDung();
            return;
        }
        ThuCung cu = tim.get();
        System.out.println("Hiện tại: " + cu.getTen() + " - " + cu.getLoai()
                + " - " + Tien.dinhDang(cu.getGiaBan()) + " - SL " + cu.getSoLuong());
        System.out.println("(Để trống ở mỗi câu hỏi là giữ nguyên giá trị cũ)");
        try {
            ThuCung moi = nhapThongTin(ma, cu);
            cuaHang.getThuCung().capNhat(moi);
            cuaHang.getThuCung().luu();
            NhapXuat.thongBao("Đã cập nhật " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void xoa() {
        NhapXuat.muc("Xóa thú cưng");
        String ma = NhapXuat.docBatBuoc("Mã thú cưng: ", "Mã thú cưng");
        Optional<ThuCung> tim = cuaHang.getThuCung().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy thú cưng " + ma);
            NhapXuat.tamDung();
            return;
        }
        ThuCung t = tim.get();
        System.out.println("Sẽ xóa: " + t.getMa() + " - " + t.getTen() + " (" + t.getLoai() + ")");
        if (!NhapXuat.xacNhan("Xác nhận xóa?")) {
            NhapXuat.thongBao("Đã hủy thao tác");
            NhapXuat.tamDung();
            return;
        }
        try {
            cuaHang.getThuCung().xoa(ma);
            cuaHang.getThuCung().luu();
            NhapXuat.thongBao("Đã xóa " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void timKiem() {
        NhapXuat.muc("Tìm kiếm thú cưng");
        String tuKhoa = NhapXuat.doc("Từ khóa (mã / tên / đặc điểm), bỏ trống để bỏ qua: ");
        System.out.println("  Lọc loại: 0-Tất cả  1-Chó  2-Mèo  3-Chim");
        int chon = NhapXuat.docSoNguyen("  Chọn: ", 0, 3);
        String loai = switch (chon) {
            case 1 -> "Chó";
            case 2 -> "Mèo";
            case 3 -> "Chim";
            default -> "Tất cả";
        };
        List<ThuCung> ketQua = cuaHang.getThuCung().timKiem(tuKhoa, loai);
        xemDanhSach(ketQua, "Kết quả tìm kiếm");
        NhapXuat.tamDung();
    }

    private void sapXep() {
        NhapXuat.muc("Sắp xếp theo giá bán");
        int chon = NhapXuat.docSoNguyen("  1-Tăng dần  2-Giảm dần: ", 1, 2);
        xemDanhSach(cuaHang.getThuCung().sapXepTheoGia(chon == 1), "Danh sách theo giá");
        NhapXuat.tamDung();
    }

    /** Nhập thông tin; nếu coDuLieu khác null thì các câu hỏi có giá trị mặc định. */
    private ThuCung nhapThongTin(String ma, ThuCung coDuLieu) {
        String ten = HoiNhap.chuoi("  Tên thú cưng", coDuLieu == null ? null : coDuLieu.getTen());
        LocalDate ngaySinh = HoiNhap.ngay("  Ngày sinh", coDuLieu == null ? null : coDuLieu.getNgaySinh());
        double giaBan = HoiNhap.tien("  Giá bán", coDuLieu == null ? 0 : coDuLieu.getGiaBan());
        int soLuong = HoiNhap.soNguyen("  Số lượng", coDuLieu == null ? 1 : coDuLieu.getSoLuong(), 0, 9999);
        TrangThaiThuCung trangThai = hoiTrangThai(coDuLieu == null ? TrangThaiThuCung.CON_BAN : coDuLieu.getTrangThai());

        String loai = hoiLoai(coDuLieu == null ? null : coDuLieu.getLoai());
        boolean cungLoai = coDuLieu != null && coDuLieu.getLoai().equals(loai);

        return switch (loai) {
            case "Chó" -> {
                Cho cu = cungLoai ? (Cho) coDuLieu : null;
                String giong = HoiNhap.chuoi("  Giống chó", cu == null ? null : cu.getGiong());
                String kichThuoc = hoiKichThuoc(cu == null ? "Vừa" : cu.getKichThuoc());
                boolean tiemPhong = HoiNhap.coKhong("  Đã tiêm phòng", cu != null && cu.isDaTiemPhong());
                yield new Cho(ma, ten, ngaySinh, giaBan, soLuong, trangThai, giong, kichThuoc, tiemPhong);
            }
            case "Mèo" -> {
                Meo cu = cungLoai ? (Meo) coDuLieu : null;
                String giong = HoiNhap.chuoi("  Giống mèo", cu == null ? null : cu.getGiong());
                boolean daiLong = HoiNhap.coKhong("  Lông dài", cu != null && cu.isDaiLong());
                boolean trietSan = HoiNhap.coKhong("  Đã triệt sản", cu != null && cu.isDaTrietSan());
                yield new Meo(ma, ten, ngaySinh, giaBan, soLuong, trangThai, giong, daiLong, trietSan);
            }
            default -> {
                Chim cu = cungLoai ? (Chim) coDuLieu : null;
                String loaiChim = HoiNhap.chuoi("  Loài chim", cu == null ? null : cu.getLoaiChim());
                boolean bietNoi = HoiNhap.coKhong("  Biết nói", cu != null && cu.isBietNoi());
                String mauLong = HoiNhap.chuoi("  Màu lông", cu == null ? null : cu.getMauLong());
                yield new Chim(ma, ten, ngaySinh, giaBan, soLuong, trangThai, loaiChim, bietNoi, mauLong);
            }
        };
    }

    private String hoiLoai(String hienTai) {
        System.out.println("  Loại: 1-Chó  2-Mèo  3-Chim"
                + (hienTai == null ? "" : "  0-Giữ nguyên (" + hienTai + ")"));
        int chon = NhapXuat.docSoNguyen("  Chọn loại: ", hienTai == null ? 1 : 0, 3);
        return switch (chon) {
            case 0 -> hienTai;
            case 1 -> "Chó";
            case 2 -> "Mèo";
            default -> "Chim";
        };
    }

    private String hoiKichThuoc(String hienTai) {
        System.out.println("  Kích cỡ: 1-Nhỏ  2-Vừa  3-Lớn  (hiện tại: " + hienTai + ")");
        int chon = NhapXuat.docSoNguyen("  Chọn: ", 1, 3);
        return switch (chon) {
            case 1 -> "Nhỏ";
            case 3 -> "Lớn";
            default -> "Vừa";
        };
    }

    private TrangThaiThuCung hoiTrangThai(TrangThaiThuCung hienTai) {
        System.out.println("  Trạng thái: 1-Còn bán  2-Đã bán  3-Cách ly  (hiện tại: " + hienTai.getNhan() + ")");
        int chon = NhapXuat.docSoNguyen("  Chọn: ", 1, 3);
        return switch (chon) {
            case 2 -> TrangThaiThuCung.DA_BAN;
            case 3 -> TrangThaiThuCung.CACH_LY;
            default -> TrangThaiThuCung.CON_BAN;
        };
    }
}
