package petshop.io;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import petshop.model.ChiTietHoaDon;
import petshop.model.HangKhachHang;
import petshop.model.HoaDon;

/**
 * Định dạng mỗi dòng:
 * ma|ngayLap|maKhachHang|tenKhachHang|hang|maNhanVien|tenNhanVien|danhSachChiTiet
 * trong đó danhSachChiTiet là các bộ "maThuCung~ten~loai~soLuong~donGia" ngăn bởi ';'.
 */
public class HoaDonCodec implements TextCodec<HoaDon> {

    @Override
    public String toLine(HoaDon hd) {
        StringBuilder chiTiet = new StringBuilder();
        for (ChiTietHoaDon ct : hd.getChiTiet()) {
            if (chiTiet.length() > 0) {
                chiTiet.append(';');
            }
            chiTiet.append(Esc.ma(ct.getMaHang())).append('~')
                    .append(Esc.ma(ct.getTenHang())).append('~')
                    .append(Esc.ma(ct.getLoaiHang())).append('~')
                    .append(ct.getSoLuong()).append('~')
                    .append(Math.round(ct.getDonGia()));
        }
        return String.join("|",
                hd.getMa(),
                hd.getNgayLap().toString(),
                Esc.ma(hd.getMaKhachHang()),
                Esc.ma(hd.getTenKhachHang()),
                hd.getHangKhachHang().name(),
                Esc.ma(hd.getMaNhanVien()),
                Esc.ma(hd.getTenNhanVien()),
                chiTiet.toString());
    }

    @Override
    public HoaDon fromLine(String dong) {
        String[] p = dong.split("\\|", -1);
        if (p.length < 8) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/8)");
        }
        List<ChiTietHoaDon> chiTiet = new ArrayList<>();
        if (!p[7].isEmpty()) {
            for (String bo : p[7].split(";", -1)) {
                String[] f = bo.split("~", -1);
                if (f.length < 5) {
                    throw new IllegalArgumentException("chi tiết hóa đơn không hợp lệ: " + bo);
                }
                chiTiet.add(new ChiTietHoaDon(
                        Esc.giaiMa(f[0]),
                        Esc.giaiMa(f[1]),
                        Esc.giaiMa(f[2]),
                        Integer.parseInt(f[3]),
                        Double.parseDouble(f[4])));
            }
        }
        return new HoaDon(
                p[0],
                LocalDateTime.parse(p[1]),
                Esc.giaiMa(p[2]),
                Esc.giaiMa(p[3]),
                HangKhachHang.valueOf(p[4]),
                Esc.giaiMa(p[5]),
                Esc.giaiMa(p[6]),
                chiTiet);
    }
}
