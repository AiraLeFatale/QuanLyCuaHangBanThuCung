package petshop.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import petshop.exception.AppException;
import petshop.model.ChiTietHoaDon;
import petshop.model.DichVu;
import petshop.model.HoaDon;
import petshop.model.KhachHang;
import petshop.model.MatHang;
import petshop.model.NhanVien;
import petshop.model.SanPham;
import petshop.model.ThuCung;
import petshop.service.CuaHang;
import petshop.util.HoaDonText;
import petshop.util.Tien;
import petshop.util.TrangTri;

/**
 * Menu bán hàng trên console: chọn khách, nhân viên, thêm thú cưng / sản phẩm / dịch vụ
 * vào giỏ rồi thanh toán.
 */
public class MenuBanHang {

    private static final String[] COT_GIO = {"Mã", "Tên mặt hàng", "Loại", "Đơn giá", "SL", "Thành tiền"};
    private static final int[] RONG_GIO = {6, 22, 12, 13, 4, 14};

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
        if (cuaHang.getThuCung().conBanDuoc().isEmpty()
                && cuaHang.getSanPham().conBanDuoc().isEmpty()
                && cuaHang.getDichVu().soLuong() == 0) {
            NhapXuat.loi("Hiện không có mặt hàng nào đang bán");
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
        System.out.println("\nNhập mã mặt hàng (TC = thú cưng, SP = sản phẩm, DV = dịch vụ),"
                + " bỏ trống để kết thúc.");
        while (true) {
            xemMatHang();
            String maHang = NhapXuat.doc("Mã mặt hàng (bỏ trống để kết thúc): ");
            if (maHang.isEmpty()) {
                break;
            }
            Optional<MatHang> tim = cuaHang.timMatHang(maHang);
            if (tim.isEmpty()) {
                NhapXuat.loi("Không tìm thấy mặt hàng " + maHang);
                continue;
            }
            MatHang matHang = tim.get();
            if (!matHang.conBanDuoc()) {
                NhapXuat.loi(matHang.getTen() + " hiện không bán được");
                continue;
            }
            int daCoTrongGio = soLuongTrongGio(gioHang, matHang.getMa());
            int toiDa;
            String goiY;
            if (matHang.quanLyTonKho()) {
                toiDa = matHang.getSoLuongTon() - daCoTrongGio;
                if (toiDa <= 0) {
                    NhapXuat.loi("Đã lấy hết số lượng còn lại của " + matHang.getTen());
                    continue;
                }
                goiY = "  Số lượng (còn " + toiDa + "): ";
            } else {
                toiDa = 99;
                goiY = "  Số lượng: ";
            }
            int soLuong = NhapXuat.docSoNguyen(goiY, 1, toiDa);
            themVaoGio(gioHang, matHang, soLuong);
            NhapXuat.thongBao("Đã thêm " + soLuong + " x " + matHang.getTen()
                    + " (" + Tien.dinhDang(matHang.getGiaBan()) + " / "
                    + (matHang.quanLyTonKho() ? "cái" : "lần") + ")");
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

    private void xemMatHang() {
        NhapXuat.muc("Thú cưng đang bán");
        List<String[]> thuCung = new ArrayList<>();
        for (ThuCung t : cuaHang.getThuCung().conBanDuoc()) {
            thuCung.add(new String[]{
                    t.getMa(), t.getTen(), t.getLoai(),
                    Tien.dinhDang(t.getGiaBan()), String.valueOf(t.getSoLuong())
            });
        }
        NhapXuat.inBang(new String[]{"Mã", "Tên", "Loại", "Giá bán", "Còn"}, thuCung,
                new int[]{6, 18, 6, 13, 4});

        NhapXuat.muc("Sản phẩm đang bán");
        List<String[]> sanPham = new ArrayList<>();
        for (SanPham sp : cuaHang.getSanPham().conBanDuoc()) {
            sanPham.add(new String[]{
                    sp.getMa(), sp.getTen(), sp.getLoai(),
                    Tien.dinhDang(sp.getGiaBan()), String.valueOf(sp.getSoLuong())
            });
        }
        NhapXuat.inBang(new String[]{"Mã", "Tên", "Loại", "Giá bán", "Còn"}, sanPham,
                new int[]{6, 18, 10, 13, 4});

        NhapXuat.muc("Dịch vụ");
        List<String[]> dichVu = new ArrayList<>();
        for (DichVu dv : cuaHang.getDichVu().tatCa()) {
            dichVu.add(new String[]{
                    dv.getMa(), dv.getTen(), dv.getLoai(), Tien.dinhDang(dv.getGiaBan())
            });
        }
        NhapXuat.inBang(new String[]{"Mã", "Tên", "Loại", "Giá"}, dichVu,
                new int[]{6, 22, 12, 13});
    }

    private void xemGioHang(List<ChiTietHoaDon> gioHang, KhachHang khach) {
        NhapXuat.muc("Giỏ hàng");
        List<String[]> dong = new ArrayList<>();
        double tamTinh = 0;
        for (ChiTietHoaDon ct : gioHang) {
            tamTinh += ct.getThanhTien();
            dong.add(new String[]{
                    ct.getMaHang(),
                    ct.getTenHang(),
                    ct.getLoaiHang(),
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
        System.out.println("  Điểm cộng  : +" + (int) ((tamTinh - giam) / HoaDon.TIEN_MOI_DIEM));
    }

    private int soLuongTrongGio(List<ChiTietHoaDon> gioHang, String maHang) {
        for (ChiTietHoaDon ct : gioHang) {
            if (ct.getMaHang().equalsIgnoreCase(maHang)) {
                return ct.getSoLuong();
            }
        }
        return 0;
    }

    private void themVaoGio(List<ChiTietHoaDon> gioHang, MatHang matHang, int soLuong) {
        for (int i = 0; i < gioHang.size(); i++) {
            ChiTietHoaDon ct = gioHang.get(i);
            if (ct.getMaHang().equalsIgnoreCase(matHang.getMa())) {
                gioHang.set(i, ct.voiSoLuong(ct.getSoLuong() + soLuong));
                return;
            }
        }
        gioHang.add(ChiTietHoaDon.tuMatHang(matHang, soLuong));
    }
}
