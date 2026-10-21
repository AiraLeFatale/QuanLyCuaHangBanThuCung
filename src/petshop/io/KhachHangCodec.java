package petshop.io;

import java.time.LocalDate;

import petshop.model.GioiTinh;
import petshop.model.KhachHang;

/** Định dạng mỗi dòng: ma|hoTen|gioiTinh|ngaySinh|soDienThoai|diaChi|diemTichLuy */
public class KhachHangCodec implements TextCodec<KhachHang> {

    @Override
    public String toLine(KhachHang kh) {
        return String.join("|",
                kh.getMa(),
                Esc.ma(kh.getHoTen()),
                kh.getGioiTinh().name(),
                kh.getNgaySinh() == null ? "" : kh.getNgaySinh().toString(),
                Esc.ma(kh.getSoDienThoai()),
                Esc.ma(kh.getDiaChi()),
                String.valueOf(kh.getDiemTichLuy()));
    }

    @Override
    public KhachHang fromLine(String dong) {
        String[] p = dong.split("\\|", -1);
        if (p.length < 7) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/7)");
        }
        LocalDate ngaySinh = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
        return new KhachHang(
                p[0],
                Esc.giaiMa(p[1]),
                GioiTinh.tuChuoi(p[2]),
                ngaySinh,
                Esc.giaiMa(p[4]),
                Esc.giaiMa(p[5]),
                Integer.parseInt(p[6]));
    }
}
