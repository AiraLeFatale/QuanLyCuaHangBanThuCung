package menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entity.GioiTinh;
import entity.KhachHang;
import manager.CuaHang;
import util.AppException;
import util.TrangTri;
import util.Validator;


/** Menu quản lý khách hàng trên console. */
public class MenuKhachHang {

    private static final String[] COT = {"Mã", "Họ tên", "Giới tính", "SĐT", "Điểm", "Hạng", "Ưu đãi"};
    private static final int[] RONG = {6, 20, 9, 11, 6, 9, 7};

    private final CuaHang cuaHang;

    public MenuKhachHang(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public void chay() {
        while (true) {
            NhapXuat.tieuDe("Quản lý khách hàng");
            System.out.println("  1. Xem danh sách");
            System.out.println("  2. Thêm khách hàng");
            System.out.println("  3. Cập nhật khách hàng");
            System.out.println("  4. Xóa khách hàng");
            System.out.println("  5. Tìm kiếm");
            System.out.println("  6. Sắp xếp theo điểm tích lũy");
            System.out.println("  0. Quay lại menu chính");

            int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 6);
            switch (chon) {
                case 1 -> {
                    xemDanhSach(cuaHang.getKhachHang().tatCa(), "Danh sách khách hàng");
                    NhapXuat.tamDung();
                }
                case 2 -> them();
                case 3 -> capNhat();
                case 4 -> xoa();
                case 5 -> timKiem();
                case 6 -> {
                    xemDanhSach(cuaHang.getKhachHang().sapXepTheoDiem(), "Khách hàng theo điểm tích lũy");
                    NhapXuat.tamDung();
                }
                default -> {
                    return;
                }
            }
        }
    }

    private void xemDanhSach(List<KhachHang> danhSach, String tieuDe) {
        NhapXuat.muc(tieuDe + " (" + danhSach.size() + " bản ghi)");
        List<String[]> dong = new ArrayList<>();
        for (KhachHang kh : danhSach) {
            dong.add(new String[]{
                    kh.getMa(),
                    kh.getHoTen(),
                    kh.getGioiTinh().getNhan(),
                    kh.getSoDienThoai(),
                    String.valueOf(kh.getDiemTichLuy()),
                    kh.getHang().getNhan(),
                    TrangTri.phanTram(kh.getTiLeChietKhau())
            });
        }
        NhapXuat.inBang(COT, dong, RONG);
    }

    private void them() {
        NhapXuat.muc("Thêm khách hàng");
        String ma = cuaHang.getKhachHang().sinhMa();
        System.out.println("Mã tự động: " + ma);
        try {
            KhachHang moi = new KhachHang(
                    ma,
                    NhapXuat.chuoi("  Họ tên", null),
                    hoiGioiTinh(GioiTinh.KHAC),
                    NhapXuat.ngay("  Ngày sinh", null),
                    hoiDienThoai(null),
                    NhapXuat.chuoiTuyChon("  Địa chỉ", ""),
                    NhapXuat.soNguyen("  Điểm tích lũy", 0, 0, 1_000_000));
            kiemTraTrungDienThoai(moi);
            cuaHang.getKhachHang().them(moi);
            cuaHang.getKhachHang().luu();
            NhapXuat.thongBao("Đã thêm khách hàng " + moi.getMa() + " - " + moi.getHoTen()
                    + " (hạng " + moi.getHang().getNhan() + ")");
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void capNhat() {
        NhapXuat.muc("Cập nhật khách hàng");
        String ma = NhapXuat.docBatBuoc("Mã khách hàng: ", "Mã khách hàng");
        Optional<KhachHang> tim = cuaHang.getKhachHang().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy khách hàng " + ma);
            NhapXuat.tamDung();
            return;
        }
        KhachHang cu = tim.get();
        System.out.println("Hiện tại: " + cu.getHoTen() + " - " + cu.getHang().getNhan()
                + " - " + cu.getDiemTichLuy() + " điểm");
        System.out.println("(Để trống ở mỗi câu hỏi là giữ nguyên giá trị cũ)");
        try {
            KhachHang moi = new KhachHang(
                    ma,
                    NhapXuat.chuoi("  Họ tên", cu.getHoTen()),
                    hoiGioiTinh(cu.getGioiTinh()),
                    NhapXuat.ngay("  Ngày sinh", cu.getNgaySinh()),
                    hoiDienThoai(cu.getSoDienThoai()),
                    NhapXuat.chuoiTuyChon("  Địa chỉ", cu.getDiaChi()),
                    NhapXuat.soNguyen("  Điểm tích lũy", cu.getDiemTichLuy(), 0, 1_000_000));
            kiemTraTrungDienThoai(moi);
            cuaHang.getKhachHang().capNhat(moi);
            cuaHang.getKhachHang().luu();
            NhapXuat.thongBao("Đã cập nhật khách hàng " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void xoa() {
        NhapXuat.muc("Xóa khách hàng");
        String ma = NhapXuat.docBatBuoc("Mã khách hàng: ", "Mã khách hàng");
        Optional<KhachHang> tim = cuaHang.getKhachHang().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy khách hàng " + ma);
            NhapXuat.tamDung();
            return;
        }
        KhachHang kh = tim.get();
        System.out.println("Sẽ xóa: " + kh.getMa() + " - " + kh.getHoTen());
        if (!NhapXuat.xacNhan("Xác nhận xóa?")) {
            NhapXuat.thongBao("Đã hủy thao tác");
            NhapXuat.tamDung();
            return;
        }
        try {
            cuaHang.getKhachHang().xoa(ma);
            cuaHang.getKhachHang().luu();
            NhapXuat.thongBao("Đã xóa " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void timKiem() {
        NhapXuat.muc("Tìm kiếm khách hàng");
        String tuKhoa = NhapXuat.doc("Nhập mã / họ tên / số điện thoại: ");
        xemDanhSach(cuaHang.getKhachHang().timKiem(tuKhoa), "Kết quả tìm kiếm");
        NhapXuat.tamDung();
    }

    private GioiTinh hoiGioiTinh(GioiTinh hienTai) {
        System.out.println("  Giới tính: 1-Nam  2-Nữ  3-Khác  (hiện tại: " + hienTai.getNhan() + ")");
        int chon = NhapXuat.docSoNguyen("  Chọn: ", 1, 3);
        return switch (chon) {
            case 1 -> GioiTinh.NAM;
            case 2 -> GioiTinh.NU;
            default -> GioiTinh.KHAC;
        };
    }

    private String hoiDienThoai(String hienTai) {
        while (true) {
            String s = NhapXuat.chuoi("  Số điện thoại", hienTai == null || hienTai.isBlank() ? null : hienTai);
            try {
                return Validator.soDienThoai(s, "Số điện thoại", false);
            } catch (AppException ex) {
                NhapXuat.loi(ex.getMessage());
            }
        }
    }

    private void kiemTraTrungDienThoai(KhachHang kh) {
        Optional<KhachHang> trung = cuaHang.getKhachHang()
                .timTheoDienThoai(kh.getSoDienThoai())
                .filter(co -> !co.getMa().equalsIgnoreCase(kh.getMa()));
        if (trung.isPresent()) {
            throw new AppException.NghiepVu("Số điện thoại đã thuộc về khách hàng "
                    + trung.get().getMa() + " - " + trung.get().getHoTen());
        }
    }
}
