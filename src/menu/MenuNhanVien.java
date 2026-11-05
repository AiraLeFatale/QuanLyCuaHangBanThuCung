package menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entity.GioiTinh;
import entity.NhanVien;
import manager.CuaHang;
import util.AppException;
import util.Tien;
import util.TrangTri;
import util.Validator;


/** Menu quản lý nhân viên trên console. */
public class MenuNhanVien {

    private static final String[] COT = {"Mã", "Họ tên", "Giới tính", "SĐT", "Chức vụ", "Lương", "Vào làm"};
    private static final int[] RONG = {6, 20, 9, 11, 12, 13, 11};

    private final CuaHang cuaHang;

    public MenuNhanVien(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public void chay() {
        while (true) {
            NhapXuat.tieuDe("Quản lý nhân viên");
            System.out.println("  1. Xem danh sách");
            System.out.println("  2. Thêm nhân viên");
            System.out.println("  3. Cập nhật nhân viên");
            System.out.println("  4. Xóa nhân viên");
            System.out.println("  5. Tìm kiếm");
            System.out.println("  6. Tổng quỹ lương");
            System.out.println("  0. Quay lại menu chính");

            int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 6);
            switch (chon) {
                case 1 -> {
                    xemDanhSach(cuaHang.getNhanVien().tatCa(), "Danh sách nhân viên");
                    NhapXuat.tamDung();
                }
                case 2 -> them();
                case 3 -> capNhat();
                case 4 -> xoa();
                case 5 -> timKiem();
                case 6 -> {
                    NhapXuat.muc("Tổng quỹ lương");
                    System.out.println("  Số nhân viên: " + cuaHang.getNhanVien().soLuong());
                    System.out.println("  Tổng lương  : " + Tien.dinhDang(cuaHang.getNhanVien().tongLuong()));
                    NhapXuat.tamDung();
                }
                default -> {
                    return;
                }
            }
        }
    }

    private void xemDanhSach(List<NhanVien> danhSach, String tieuDe) {
        NhapXuat.muc(tieuDe + " (" + danhSach.size() + " bản ghi)");
        List<String[]> dong = new ArrayList<>();
        for (NhanVien nv : danhSach) {
            dong.add(new String[]{
                    nv.getMa(),
                    nv.getHoTen(),
                    nv.getGioiTinh().getNhan(),
                    nv.getSoDienThoai(),
                    nv.getChucVu(),
                    Tien.dinhDang(nv.getLuong()),
                    TrangTri.ngay(nv.getNgayVaoLam())
            });
        }
        NhapXuat.inBang(COT, dong, RONG);
    }

    private void them() {
        NhapXuat.muc("Thêm nhân viên");
        String ma = cuaHang.getNhanVien().sinhMa();
        System.out.println("Mã tự động: " + ma);
        try {
            NhanVien moi = new NhanVien(
                    ma,
                    NhapXuat.chuoi("  Họ tên", null),
                    hoiGioiTinh(GioiTinh.KHAC),
                    NhapXuat.ngay("  Ngày sinh", null),
                    hoiDienThoai(null),
                    NhapXuat.chuoiTuyChon("  Địa chỉ", ""),
                    NhapXuat.chuoi("  Chức vụ", null),
                    NhapXuat.tien("  Lương", 0),
                    NhapXuat.ngay("  Ngày vào làm", null));
            cuaHang.getNhanVien().them(moi);
            cuaHang.getNhanVien().luu();
            NhapXuat.thongBao("Đã thêm nhân viên " + moi.getMa() + " - " + moi.getHoTen());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void capNhat() {
        NhapXuat.muc("Cập nhật nhân viên");
        String ma = NhapXuat.docBatBuoc("Mã nhân viên: ", "Mã nhân viên");
        Optional<NhanVien> tim = cuaHang.getNhanVien().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy nhân viên " + ma);
            NhapXuat.tamDung();
            return;
        }
        NhanVien cu = tim.get();
        System.out.println("Hiện tại: " + cu.getHoTen() + " - " + cu.getChucVu()
                + " - " + Tien.dinhDang(cu.getLuong()));
        System.out.println("(Để trống ở mỗi câu hỏi là giữ nguyên giá trị cũ)");
        try {
            NhanVien moi = new NhanVien(
                    ma,
                    NhapXuat.chuoi("  Họ tên", cu.getHoTen()),
                    hoiGioiTinh(cu.getGioiTinh()),
                    NhapXuat.ngay("  Ngày sinh", cu.getNgaySinh()),
                    hoiDienThoai(cu.getSoDienThoai()),
                    NhapXuat.chuoiTuyChon("  Địa chỉ", cu.getDiaChi()),
                    NhapXuat.chuoi("  Chức vụ", cu.getChucVu()),
                    NhapXuat.tien("  Lương", cu.getLuong()),
                    NhapXuat.ngay("  Ngày vào làm", cu.getNgayVaoLam()));
            cuaHang.getNhanVien().capNhat(moi);
            cuaHang.getNhanVien().luu();
            NhapXuat.thongBao("Đã cập nhật nhân viên " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void xoa() {
        NhapXuat.muc("Xóa nhân viên");
        String ma = NhapXuat.docBatBuoc("Mã nhân viên: ", "Mã nhân viên");
        Optional<NhanVien> tim = cuaHang.getNhanVien().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy nhân viên " + ma);
            NhapXuat.tamDung();
            return;
        }
        NhanVien nv = tim.get();
        System.out.println("Sẽ xóa: " + nv.getMa() + " - " + nv.getHoTen());
        if (!NhapXuat.xacNhan("Xác nhận xóa?")) {
            NhapXuat.thongBao("Đã hủy thao tác");
            NhapXuat.tamDung();
            return;
        }
        try {
            cuaHang.getNhanVien().xoa(ma);
            cuaHang.getNhanVien().luu();
            NhapXuat.thongBao("Đã xóa " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void timKiem() {
        NhapXuat.muc("Tìm kiếm nhân viên");
        String tuKhoa = NhapXuat.doc("Nhập mã / họ tên / chức vụ: ");
        xemDanhSach(cuaHang.getNhanVien().timKiem(tuKhoa), "Kết quả tìm kiếm");
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
            String s = NhapXuat.chuoiTuyChon("  Số điện thoại", hienTai);
            try {
                return Validator.soDienThoai(s, "Số điện thoại", true);
            } catch (AppException ex) {
                NhapXuat.loi(ex.getMessage());
            }
        }
    }
}
