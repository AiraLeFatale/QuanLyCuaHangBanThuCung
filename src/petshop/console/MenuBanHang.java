package petshop.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import petshop.exception.AppException;
import petshop.model.ChiTietHoaDon;
import petshop.model.HoaDon;
import petshop.model.KhachHang;
import petshop.model.NhanVien;
import petshop.model.ThuCung;
import petshop.service.CuaHang;
import petshop.util.HoaDonText;
import petshop.util.Tien;
import petshop.util.TrangTri;

/** Menu bán hàng trên console: chọn khách, nhân viên, thêm thú cưng vào giỏ rồi thanh toán. */
public class MenuBanHang {

    private static final String[] COT_GIO = {"Mã", "Tên thú cưng", "Loại", "Đơn giá", "SL", "Thành tiền"};
    private static final int[] RONG_GIO = {6, 20, 6, 13, 4, 14};

    private final CuaHang cuaHang;

    public MenuBanHang(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public void chay() {
        NhapXuat.tieuDe("Bán hàng");

        if (cuaHang.getKhachHang().soLuong() == 0 || cuaHang.getNhanVien().soLuong() == 0) {
            NhapXuat.loi("Cần có ít nhất một khách hàng và một nhân viên trước khi bán hàng");
            NhapXuat.tamDung();
            return;
        }
        if (cuaHang.getThuCung().conBanDuoc().isEmpty()) {
            NhapXuat.loi("Hiện không có thú cưng nào đang bán");
            NhapXuat.tamDung();
            return;
        }

        xemKhachHang();
        String maKhach = NhapXuat.docBatBuoc("Mã khách hàng: ", "Mã khách hàng");
        Optional<KhachHang> khach = cuaHang.getKhachHang().tim(maKhach);
        if (khach.isEmpty()) {
            NhapXuat.loi("Không tìm thấy khách hàng " + maKhach);
            NhapXuat.tamDung();
            return;
        }

        xemNhanVien();
        String maNhanVien = NhapXuat.docBatBuoc("Mã nhân viên bán: ", "Mã nhân viên");
        Optional<NhanVien> nhanVien = cuaHang.getNhanVien().tim(maNhanVien);
        if (nhanVien.isEmpty()) {
            NhapXuat.loi("Không tìm thấy nhân viên " + maNhanVien);
            NhapXuat.tamDung();
            return;
        }

        List<ChiTietHoaDon> gioHang = new ArrayList<>();
        System.out.println("\nNhập mã thú cưng để thêm vào giỏ, bỏ trống để kết thúc.");
        while (true) {
            xemThuCungConBan();
            String maThuCung = NhapXuat.doc("Mã thú cưng (bỏ trống để kết thúc): ");
            if (maThuCung.isEmpty()) {
                break;
            }
            Optional<ThuCung> tim = cuaHang.getThuCung().tim(maThuCung);
            if (tim.isEmpty()) {
                NhapXuat.loi("Không tìm thấy thú cưng " + maThuCung);
                continue;
            }
            ThuCung thuCung = tim.get();
            if (!thuCung.conBanDuoc()) {
                NhapXuat.loi(thuCung.getTen() + " hiện không bán được (trạng thái: "
                        + thuCung.getTrangThai().getNhan() + ")");
                continue;
            }
            int daCoTrongGio = soLuongTrongGio(gioHang, thuCung.getMa());
            int conLai = thuCung.getSoLuong() - daCoTrongGio;
            if (conLai <= 0) {
                NhapXuat.loi("Đã lấy hết số lượng còn lại của " + thuCung.getTen());
                continue;
            }
            int soLuong = NhapXuat.docSoNguyen("  Số lượng (còn " + conLai + "): ", 1, conLai);
            themVaoGio(gioHang, thuCung, soLuong);
            NhapXuat.thongBao("Đã thêm " + soLuong + " x " + thuCung.getTen()
                    + " (" + Tien.dinhDang(thuCung.getGiaBan()) + ")");
        }

        if (gioHang.isEmpty()) {
            NhapXuat.thongBao("Giỏ hàng trống, kết thúc bán hàng");
            NhapXuat.tamDung();
            return;
        }

        xemGioHang(gioHang, khach.get());
        if (!NhapXuat.xacNhan("Xác nhận thanh toán?")) {
            NhapXuat.thongBao("Đã hủy hóa đơn, không thay đổi dữ liệu");
            NhapXuat.tamDung();
            return;
        }

        try {
            HoaDon hoaDon = cuaHang.thanhToan(khach.get().getMa(), nhanVien.get().getMa(), gioHang);
            NhapXuat.thongBao("Đã thanh toán hóa đơn " + hoaDon.getMa());
            System.out.println();
            System.out.print(HoaDonText.tao(hoaDon));
        } catch (AppException ex) {
            NhapXuat.loi(ex.getMessage());
        }
        NhapXuat.tamDung();
    }

    private void xemKhachHang() {
        NhapXuat.muc("Khách hàng");
        List<String[]> dong = new ArrayList<>();
        for (KhachHang kh : cuaHang.getKhachHang().tatCa()) {
            dong.add(new String[]{
                    kh.getMa(),
                    kh.getHoTen(),
                    kh.getSoDienThoai(),
                    String.valueOf(kh.getDiemTichLuy()),
                    kh.getHang().getNhan(),
                    TrangTri.phanTram(kh.getTiLeChietKhau())
            });
        }
        NhapXuat.inBang(new String[]{"Mã", "Họ tên", "SĐT", "Điểm", "Hạng", "Ưu đãi"}, dong,
                new int[]{6, 20, 11, 6, 9, 7});
    }

    private void xemNhanVien() {
        NhapXuat.muc("Nhân viên");
        List<String[]> dong = new ArrayList<>();
        for (NhanVien nv : cuaHang.getNhanVien().tatCa()) {
            dong.add(new String[]{nv.getMa(), nv.getHoTen(), nv.getChucVu()});
        }
        NhapXuat.inBang(new String[]{"Mã", "Họ tên", "Chức vụ"}, dong, new int[]{6, 22, 14});
    }

    private void xemThuCungConBan() {
        NhapXuat.muc("Thú cưng đang bán");
        List<String[]> dong = new ArrayList<>();
        for (ThuCung t : cuaHang.getThuCung().conBanDuoc()) {
            dong.add(new String[]{
                    t.getMa(),
                    t.getTen(),
                    t.getLoai(),
                    Tien.dinhDang(t.getGiaBan()),
                    String.valueOf(t.getSoLuong())
            });
        }
        NhapXuat.inBang(new String[]{"Mã", "Tên", "Loại", "Giá bán", "Còn"}, dong,
                new int[]{6, 18, 6, 13, 4});
    }

    private void xemGioHang(List<ChiTietHoaDon> gioHang, KhachHang khach) {
        NhapXuat.muc("Giỏ hàng");
        List<String[]> dong = new ArrayList<>();
        double tamTinh = 0;
        for (ChiTietHoaDon ct : gioHang) {
            tamTinh += ct.getThanhTien();
            dong.add(new String[]{
                    ct.getMaThuCung(),
                    ct.getTenThuCung(),
                    ct.getLoaiThuCung(),
                    Tien.dinhDang(ct.getDonGia()),
                    String.valueOf(ct.getSoLuong()),
                    Tien.dinhDang(ct.getThanhTien())
            });
        }
        NhapXuat.inBang(COT_GIO, dong, RONG_GIO);

        double tiLe = khach.getTiLeChietKhau();
        double giam = Math.round(tamTinh * tiLe);
        System.out.println("  Khách hàng : " + khach.getHoTen() + " (" + khach.getMa() + ")");
        System.out.println("  Hạng       : " + khach.getHang().getNhan()
                + " - ưu đãi " + TrangTri.phanTram(tiLe));
        System.out.println("  Tạm tính   : " + Tien.dinhDang(tamTinh));
        System.out.println("  Giảm giá   : -" + Tien.dinhDang(giam));
        System.out.println("  TỔNG CỘNG  : " + Tien.dinhDang(tamTinh - giam));
    }

    private int soLuongTrongGio(List<ChiTietHoaDon> gioHang, String maThuCung) {
        for (ChiTietHoaDon ct : gioHang) {
            if (ct.getMaThuCung().equalsIgnoreCase(maThuCung)) {
                return ct.getSoLuong();
            }
        }
        return 0;
    }

    private void themVaoGio(List<ChiTietHoaDon> gioHang, ThuCung thuCung, int soLuong) {
        for (int i = 0; i < gioHang.size(); i++) {
            ChiTietHoaDon ct = gioHang.get(i);
            if (ct.getMaThuCung().equalsIgnoreCase(thuCung.getMa())) {
                gioHang.set(i, ct.voiSoLuong(ct.getSoLuong() + soLuong));
                return;
            }
        }
        gioHang.add(ChiTietHoaDon.tuThuCung(thuCung, soLuong));
    }
}
