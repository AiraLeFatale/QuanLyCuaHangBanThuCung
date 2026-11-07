package menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Chim;
import entity.Cho;
import entity.Meo;
import entity.ThuCung;
import entity.TrangThaiThuCung;
import manager.CuaHang;
import util.AppException;
import util.Tien;
import util.TrangTri;


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
                case 1:
                    xemDanhSach(cuaHang.getThuCung().tatCa(), "Danh sách thú cưng");
                    NhapXuat.tamDung();
                    break;
                case 2:
                    them();
                    break;
                case 3:
                    capNhat();
                    break;
                case 4:
                    xoa();
                    break;
                case 5:
                    timKiem();
                    break;
                case 6:
                    sapXep();
                    break;
                default:
                    return;
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
        ThuCung cu = cuaHang.getThuCung().tim(ma);
        if (cu == null) {
            NhapXuat.loi("Không tìm thấy thú cưng " + ma);
            NhapXuat.tamDung();
            return;
        }
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
        ThuCung t = cuaHang.getThuCung().tim(ma);
        if (t == null) {
            NhapXuat.loi("Không tìm thấy thú cưng " + ma);
            NhapXuat.tamDung();
            return;
        }
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
        String loai;
        switch (chon) {
            case 1:
                loai = "Chó";
                break;
            case 2:
                loai = "Mèo";
                break;
            case 3:
                loai = "Chim";
                break;
            default:
                loai = "Tất cả";
                break;
        }
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
        String ten = NhapXuat.chuoi("  Tên thú cưng", coDuLieu == null ? null : coDuLieu.getTen());
        LocalDate ngaySinh = NhapXuat.ngay("  Ngày sinh", coDuLieu == null ? null : coDuLieu.getNgaySinh());
        double giaBan = NhapXuat.tien("  Giá bán", coDuLieu == null ? 0 : coDuLieu.getGiaBan());
        int soLuong = NhapXuat.soNguyen("  Số lượng", coDuLieu == null ? 1 : coDuLieu.getSoLuong(), 0, 9999);
        TrangThaiThuCung trangThai = hoiTrangThai(coDuLieu == null ? TrangThaiThuCung.CON_BAN : coDuLieu.getTrangThai());

        String loai = hoiLoai(coDuLieu == null ? null : coDuLieu.getLoai());
        boolean cungLoai = coDuLieu != null && coDuLieu.getLoai().equals(loai);

        if (loai.equals("Chó")) {
            Cho cu = cungLoai ? (Cho) coDuLieu : null;
            String giong = NhapXuat.chuoi("  Giống chó", cu == null ? null : cu.getGiong());
            String kichThuoc = hoiKichThuoc(cu == null ? "Vừa" : cu.getKichThuoc());
            boolean tiemPhong = NhapXuat.coKhong("  Đã tiêm phòng", cu != null && cu.isDaTiemPhong());
            return new Cho(ma, ten, ngaySinh, giaBan, soLuong, trangThai, giong, kichThuoc, tiemPhong);
        }
        if (loai.equals("Mèo")) {
            Meo cu = cungLoai ? (Meo) coDuLieu : null;
            String giong = NhapXuat.chuoi("  Giống mèo", cu == null ? null : cu.getGiong());
            boolean daiLong = NhapXuat.coKhong("  Lông dài", cu != null && cu.isDaiLong());
            boolean trietSan = NhapXuat.coKhong("  Đã triệt sản", cu != null && cu.isDaTrietSan());
            return new Meo(ma, ten, ngaySinh, giaBan, soLuong, trangThai, giong, daiLong, trietSan);
        }
        Chim cu = cungLoai ? (Chim) coDuLieu : null;
        String loaiChim = NhapXuat.chuoi("  Loài chim", cu == null ? null : cu.getLoaiChim());
        boolean bietNoi = NhapXuat.coKhong("  Biết nói", cu != null && cu.isBietNoi());
        String mauLong = NhapXuat.chuoi("  Màu lông", cu == null ? null : cu.getMauLong());
        return new Chim(ma, ten, ngaySinh, giaBan, soLuong, trangThai, loaiChim, bietNoi, mauLong);
    }

    private String hoiLoai(String hienTai) {
        System.out.println("  Loại: 1-Chó  2-Mèo  3-Chim"
                + (hienTai == null ? "" : "  0-Giữ nguyên (" + hienTai + ")"));
        int chon = NhapXuat.docSoNguyen("  Chọn loại: ", hienTai == null ? 1 : 0, 3);
        switch (chon) {
            case 0:
                return hienTai;
            case 1:
                return "Chó";
            case 2:
                return "Mèo";
            default:
                return "Chim";
        }
    }

    private String hoiKichThuoc(String hienTai) {
        System.out.println("  Kích cỡ: 1-Nhỏ  2-Vừa  3-Lớn  (hiện tại: " + hienTai + ")");
        int chon = NhapXuat.docSoNguyen("  Chọn: ", 1, 3);
        switch (chon) {
            case 1:
                return "Nhỏ";
            case 3:
                return "Lớn";
            default:
                return "Vừa";
        }
    }

    private TrangThaiThuCung hoiTrangThai(TrangThaiThuCung hienTai) {
        System.out.println("  Trạng thái: 1-Còn bán  2-Đã bán  3-Cách ly  (hiện tại: " + hienTai.getNhan() + ")");
        int chon = NhapXuat.docSoNguyen("  Chọn: ", 1, 3);
        switch (chon) {
            case 2:
                return TrangThaiThuCung.DA_BAN;
            case 3:
                return TrangThaiThuCung.CACH_LY;
            default:
                return TrangThaiThuCung.CON_BAN;
        }
    }
}
