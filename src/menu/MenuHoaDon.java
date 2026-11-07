package menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entity.HoaDon;
import entity.NhanVien;
import manager.CuaHang;
import util.AppException;
import util.Tien;
import util.TrangTri;


/** Menu quản lý hóa đơn: xem, tạo (bán hàng), sửa, hủy và tìm kiếm. */
public class MenuHoaDon {

    private static final String[] COT = {"Mã HĐ", "Ngày lập", "Khách hàng", "Nhân viên", "SL", "Tổng tiền"};
    private static final int[] RONG = {7, 16, 20, 18, 4, 14};

    private final CuaHang cuaHang;
    private final MenuBanHang menuBanHang;

    public MenuHoaDon(CuaHang cuaHang, MenuBanHang menuBanHang) {
        this.cuaHang = cuaHang;
        this.menuBanHang = menuBanHang;
    }

    public void chay() {
        while (true) {
            NhapXuat.tieuDe("Quản lý hóa đơn");
            System.out.println("  1. Xem danh sách hóa đơn");
            System.out.println("  2. Xem chi tiết hóa đơn");
            System.out.println("  3. Tạo hóa đơn mới (bán hàng)");
            System.out.println("  4. Cập nhật hóa đơn (đổi nhân viên lập)");
            System.out.println("  5. Hủy hóa đơn (xóa)");
            System.out.println("  6. Tìm kiếm hóa đơn");
            System.out.println("  0. Quay lại menu chính");

            int chon = NhapXuat.docSoNguyen("Chọn: ", 0, 6);
            switch (chon) {
                case 1:
                    xemDanhSach(cuaHang.getHoaDon().moiNhatTruoc(), "Danh sách hóa đơn");
                    NhapXuat.tamDung();
                    break;
                case 2:
                    xemChiTiet();
                    break;
                case 3:
                    menuBanHang.chay();
                    break;
                case 4:
                    capNhat();
                    break;
                case 5:
                    huy();
                    break;
                case 6:
                    timKiem();
                    break;
                default:
                    return;
            }
        }
    }

    private void xemDanhSach(List<HoaDon> danhSach, String tieuDe) {
        NhapXuat.muc(tieuDe + " (" + danhSach.size() + " bản ghi)");
        List<String[]> dong = new ArrayList<>();
        for (HoaDon hd : danhSach) {
            dong.add(new String[]{
                    hd.getMa(),
                    hd.getNgayLap().format(TrangTri.NGAY_GIO),
                    hd.getTenKhachHang(),
                    hd.getTenNhanVien(),
                    String.valueOf(hd.getTongSoLuong()),
                    Tien.dinhDang(hd.getTongTien())
            });
        }
        NhapXuat.inBang(COT, dong, RONG);
    }

    private void xemChiTiet() {
        NhapXuat.muc("Chi tiết hóa đơn");
        HoaDon tim = timTheoMa();
        if (tim == null) {
            NhapXuat.tamDung();
            return;
        }
        System.out.println();
        System.out.print(HoaDonText.tao(tim));
        NhapXuat.tamDung();
    }

    private void capNhat() {
        NhapXuat.muc("Cập nhật hóa đơn");
        HoaDon hd = timTheoMa();
        if (hd == null) {
            NhapXuat.tamDung();
            return;
        }
        System.out.println("Hóa đơn " + hd.getMa() + " - nhân viên hiện tại: "
                + hd.getTenNhanVien() + " (" + hd.getMaNhanVien() + ")");

        List<String[]> dong = new ArrayList<>();
        for (NhanVien nv : cuaHang.getNhanVien().tatCa()) {
            dong.add(new String[]{nv.getMa(), nv.getHoTen(), nv.getChucVu()});
        }
        NhapXuat.inBang(new String[]{"Mã", "Họ tên", "Chức vụ"}, dong, new int[]{6, 22, 14});

        String maNV = NhapXuat.docBatBuoc("Mã nhân viên mới: ", "Mã nhân viên");
        try {
            HoaDon moi = cuaHang.capNhatNhanVien(hd.getMa(), maNV);
            NhapXuat.thongBao("Đã cập nhật hóa đơn " + moi.getMa()
                    + " - nhân viên: " + moi.getTenNhanVien());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void huy() {
        NhapXuat.muc("Hủy hóa đơn");
        HoaDon hd = timTheoMa();
        if (hd == null) {
            NhapXuat.tamDung();
            return;
        }
        System.out.println("Hóa đơn " + hd.getMa() + " - " + Tien.dinhDang(hd.getTongTien())
                + " - khách " + hd.getTenKhachHang());
        System.out.println("Hủy hóa đơn sẽ hoàn hàng vào kho và trừ "
                + hd.getDiemCong() + " điểm tích lũy của khách.");
        if (!NhapXuat.xacNhan("Xác nhận hủy hóa đơn?")) {
            NhapXuat.thongBao("Đã giữ nguyên hóa đơn");
            NhapXuat.tamDung();
            return;
        }
        try {
            cuaHang.huyHoaDon(hd.getMa());
            NhapXuat.thongBao("Đã hủy hóa đơn " + hd.getMa());
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void timKiem() {
        NhapXuat.muc("Tìm kiếm hóa đơn");
        System.out.println("  Có thể nhập: mã hóa đơn, mã khách hàng, tên khách hàng hoặc ngày (dd/MM/yyyy)");
        String tuKhoa = NhapXuat.docBatBuoc("Từ khóa: ", "Từ khóa");
        String khoa = tuKhoa.toLowerCase(Locale.ROOT);
        LocalDate ngay = null;
        try {
            ngay = LocalDate.parse(tuKhoa, TrangTri.NGAY);
        } catch (DateTimeParseException ignored) {
            // không phải ngày thì chỉ tìm theo chuỗi
        }
        List<HoaDon> ketQua = new ArrayList<HoaDon>();
        List<HoaDon> tatCa = cuaHang.getHoaDon().tatCa();
        for (int i = 0; i < tatCa.size(); i++) {
            HoaDon hd = tatCa.get(i);
            boolean khopChuoi = hd.getMa().toLowerCase(Locale.ROOT).contains(khoa)
                    || hd.getMaKhachHang().toLowerCase(Locale.ROOT).contains(khoa)
                    || hd.getTenKhachHang().toLowerCase(Locale.ROOT).contains(khoa);
            boolean khopNgay = ngay != null && hd.getNgayLap().toLocalDate().equals(ngay);
            if (khopChuoi || khopNgay) {
                ketQua.add(hd);
            }
        }
        xemDanhSach(ketQua, "Kết quả tìm kiếm");
        NhapXuat.tamDung();
    }

    /** Trả về hóa đơn theo mã, hoặc null nếu không tìm thấy. */
    private HoaDon timTheoMa() {
        String ma = NhapXuat.docBatBuoc("Mã hóa đơn: ", "Mã hóa đơn");
        HoaDon tim = cuaHang.getHoaDon().tim(ma);
        if (tim == null) {
            NhapXuat.loi("Không tìm thấy hóa đơn " + ma);
        }
        return tim;
    }
}
