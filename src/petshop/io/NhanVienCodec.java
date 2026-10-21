package petshop.io;

import java.time.LocalDate;

import petshop.model.GioiTinh;
import petshop.model.NhanVien;

/** Định dạng mỗi dòng: ma|hoTen|gioiTinh|ngaySinh|soDienThoai|diaChi|chucVu|luong|ngayVaoLam */
public class NhanVienCodec implements TextCodec<NhanVien> {

    @Override
    public String toLine(NhanVien nv) {
        return String.join("|",
                nv.getMa(),
                Esc.ma(nv.getHoTen()),
                nv.getGioiTinh().name(),
                nv.getNgaySinh() == null ? "" : nv.getNgaySinh().toString(),
                Esc.ma(nv.getSoDienThoai()),
                Esc.ma(nv.getDiaChi()),
                Esc.ma(nv.getChucVu()),
                String.valueOf(Math.round(nv.getLuong())),
                nv.getNgayVaoLam() == null ? "" : nv.getNgayVaoLam().toString());
    }

    @Override
    public NhanVien fromLine(String dong) {
        String[] p = dong.split("\\|", -1);
        if (p.length < 9) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/9)");
        }
        LocalDate ngaySinh = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
        LocalDate ngayVaoLam = p[8].isEmpty() ? null : LocalDate.parse(p[8]);
        return new NhanVien(
                p[0],
                Esc.giaiMa(p[1]),
                GioiTinh.tuChuoi(p[2]),
                ngaySinh,
                Esc.giaiMa(p[4]),
                Esc.giaiMa(p[5]),
                Esc.giaiMa(p[6]),
                Double.parseDouble(p[7]),
                ngayVaoLam);
    }
}
