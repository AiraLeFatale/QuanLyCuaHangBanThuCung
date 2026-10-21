package petshop.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import petshop.exception.AppException;
import petshop.model.DichVu;
import petshop.model.DichVuCatTia;
import petshop.model.DichVuTam;
import petshop.model.DichVuTiemPhong;
import petshop.service.CuaHang;
import petshop.util.Tien;

/** Menu quản lý dịch vụ chăm sóc thú cưng trên console. */
public class MenuDichVu {

    private static final String[] COT = {"Mã", "Tên dịch vụ", "Loại", "Giá", "Phút", "Đặc điểm"};
    private static final int[] RONG = {6, 20, 12, 13, 5, 32};

    private final CuaHang cuaHang;

    public MenuDichVu(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public void chay() {
        while (true) {
            NhapXuat.tieuDe("Quản lý dịch vụ");
            System.out.println("  1. Xem danh sách");
            System.out.println("  2. Thêm dịch vụ");
            System.out.println("  3. Cập nhật dịch vụ");
            System.out.println("  4. Xóa dịch vụ");
            System.out.println("  5. Tìm kiếm / lọc theo loại");
            System.out.println("  6. Sắp xếp theo giá");
            System.out.println("  0. Quay lại menu chính");

            int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 6);
            switch (chon) {
                case 1 -> {
                    xemDanhSach(cuaHang.getDichVu().tatCa(), "Danh sách dịch vụ");
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

    private void xemDanhSach(List<DichVu> danhSach, String tieuDe) {
        NhapXuat.muc(tieuDe + " (" + danhSach.size() + " bản ghi)");
        List<String[]> dong = new ArrayList<>();
        for (DichVu dv : danhSach) {
            dong.add(new String[]{
                    dv.getMa(),
                    dv.getTen(),
                    dv.getLoai(),
                    Tien.dinhDang(dv.getGiaBan()),
                    String.valueOf(dv.getThoiGianPhut()),
                    dv.getDacDiem()
            });
        }
        NhapXuat.inBang(COT, dong, RONG);
    }

    private void them() {
        NhapXuat.muc("Thêm dịch vụ");
        String ma = cuaHang.getDichVu().sinhMa();
        System.out.println("Mã tự động: " + ma);
        try {
            DichVu moi = nhapThongTin(ma, null);
            cuaHang.getDichVu().them(moi);
            cuaHang.getDichVu().luu();
            NhapXuat.thongBao("Đã thêm " + moi.getMa() + " - " + moi.getTen());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void capNhat() {
        NhapXuat.muc("Cập nhật dịch vụ");
        String ma = NhapXuat.docBatBuoc("Mã dịch vụ: ", "Mã dịch vụ");
        Optional<DichVu> tim = cuaHang.getDichVu().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy dịch vụ " + ma);
            NhapXuat.tamDung();
            return;
        }
        DichVu cu = tim.get();
        System.out.println("Hiện tại: " + cu.getTen() + " - " + cu.getLoai()
                + " - " + Tien.dinhDang(cu.getGiaBan()));
        System.out.println("(Để trống ở mỗi câu hỏi là giữ nguyên giá trị cũ)");
        try {
            DichVu moi = nhapThongTin(ma, cu);
            cuaHang.getDichVu().capNhat(moi);
            cuaHang.getDichVu().luu();
            NhapXuat.thongBao("Đã cập nhật " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void xoa() {
        NhapXuat.muc("Xóa dịch vụ");
        String ma = NhapXuat.docBatBuoc("Mã dịch vụ: ", "Mã dịch vụ");
        Optional<DichVu> tim = cuaHang.getDichVu().tim(ma);
        if (tim.isEmpty()) {
            NhapXuat.loi("Không tìm thấy dịch vụ " + ma);
            NhapXuat.tamDung();
            return;
        }
        DichVu dv = tim.get();
        System.out.println("Sẽ xóa: " + dv.getMa() + " - " + dv.getTen() + " (" + dv.getLoai() + ")");
        if (!NhapXuat.xacNhan("Xác nhận xóa?")) {
            NhapXuat.thongBao("Đã hủy thao tác");
            NhapXuat.tamDung();
            return;
        }
        try {
            cuaHang.getDichVu().xoa(ma);
            cuaHang.getDichVu().luu();
            NhapXuat.thongBao("Đã xóa " + ma);
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void timKiem() {
        NhapXuat.muc("Tìm kiếm dịch vụ");
        String tuKhoa = NhapXuat.doc("Từ khóa (mã / tên / đặc điểm), bỏ trống để bỏ qua: ");
        System.out.println("  Lọc loại: 0-Tất cả  1-Tắm & sấy  2-Cắt tỉa  3-Tiêm phòng");
        int chon = NhapXuat.docSoNguyen("  Chọn: ", 0, 3);
        String loai = switch (chon) {
            case 1 -> "Tắm & sấy";
            case 2 -> "Cắt tỉa";
            case 3 -> "Tiêm phòng";
            default -> "Tất cả";
        };
        xemDanhSach(cuaHang.getDichVu().timKiem(tuKhoa, loai), "Kết quả tìm kiếm");
        NhapXuat.tamDung();
    }

    private void sapXep() {
        NhapXuat.muc("Sắp xếp dịch vụ theo giá");
        int chon = NhapXuat.docSoNguyen("  1-Tăng dần  2-Giảm dần: ", 1, 2);
        xemDanhSach(cuaHang.getDichVu().sapXepTheoGia(chon == 1), "Danh sách theo giá");
        NhapXuat.tamDung();
    }

    private DichVu nhapThongTin(String ma, DichVu coDuLieu) {
        String ten = HoiNhap.chuoi("  Tên dịch vụ", coDuLieu == null ? null : coDuLieu.getTen());
        double giaBan = HoiNhap.tien("  Giá dịch vụ", coDuLieu == null ? 0 : coDuLieu.getGiaBan());
        int phut = HoiNhap.soNguyen("  Thời gian thực hiện (phút)",
                coDuLieu == null ? 30 : coDuLieu.getThoiGianPhut(), 1, 600);

        String loai = hoiLoai(coDuLieu == null ? null : coDuLieu.getLoai());
        boolean cungLoai = coDuLieu != null && coDuLieu.getLoai().equals(loai);

        return switch (loai) {
            case "Tắm & sấy" -> {
                DichVuTam cu = cungLoai ? (DichVuTam) coDuLieu : null;
                String kichThuoc = HoiNhap.chuoi("  Kích thước thú cưng (Nhỏ/Vừa/Lớn)",
                        cu == null ? null : cu.getKichThuoc());
                yield new DichVuTam(ma, ten, giaBan, phut, kichThuoc);
            }
            case "Cắt tỉa" -> {
                DichVuCatTia cu = cungLoai ? (DichVuCatTia) coDuLieu : null;
                String kieuLong = HoiNhap.chuoi("  Kiểu lông",
                        cu == null ? null : cu.getKieuLong());
                yield new DichVuCatTia(ma, ten, giaBan, phut, kieuLong);
            }
            default -> {
                DichVuTiemPhong cu = cungLoai ? (DichVuTiemPhong) coDuLieu : null;
                String vacXin = HoiNhap.chuoi("  Loại vắc-xin",
                        cu == null ? null : cu.getLoaiVacXin());
                yield new DichVuTiemPhong(ma, ten, giaBan, phut, vacXin);
            }
        };
    }

    private String hoiLoai(String hienTai) {
        System.out.println("  Loại: 1-Tắm & sấy  2-Cắt tỉa  3-Tiêm phòng"
                + (hienTai == null ? "" : "  0-Giữ nguyên (" + hienTai + ")"));
        int chon = NhapXuat.docSoNguyen("  Chọn loại: ", hienTai == null ? 1 : 0, 3);
        return switch (chon) {
            case 0 -> hienTai;
            case 1 -> "Tắm & sấy";
            case 2 -> "Cắt tỉa";
            default -> "Tiêm phòng";
        };
    }
}
