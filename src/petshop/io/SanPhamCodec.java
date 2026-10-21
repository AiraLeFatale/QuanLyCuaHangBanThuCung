package petshop.io;

import java.time.LocalDate;

import petshop.model.PhuKien;
import petshop.model.SanPham;
import petshop.model.ThucAn;
import petshop.model.Thuoc;

/**
 * Định dạng mỗi dòng:
 * loai|ma|ten|giaBan|soLuong|thuocTinhRieng...
 * - Thức ăn : Cho/Mèo/Chim | khối lượng (kg) | hạn sử dụng
 * - Thuốc   : công dụng | hạn sử dụng | cần kê đơn (true/false)
 * - Phụ kiện: chất liệu | màu sắc
 */
public class SanPhamCodec implements TextCodec<SanPham> {

    @Override
    public String toLine(SanPham sp) {
        StringBuilder sb = new StringBuilder();
        sb.append(sp.getLoai()).append('|')
                .append(sp.getMa()).append('|')
                .append(Esc.ma(sp.getTen())).append('|')
                .append(Math.round(sp.getGiaBan())).append('|')
                .append(sp.getSoLuong()).append('|');

        if (sp instanceof ThucAn thucAn) {
            sb.append(Esc.ma(thucAn.getLoaiThuCung())).append('|')
                    .append(String.format(java.util.Locale.ROOT, "%.2f", thucAn.getKhoiLuongKg())).append('|')
                    .append(thucAn.getHanSuDung() == null ? "" : thucAn.getHanSuDung());
        } else if (sp instanceof Thuoc thuoc) {
            sb.append(Esc.ma(thuoc.getCongDung())).append('|')
                    .append(thuoc.getHanSuDung() == null ? "" : thuoc.getHanSuDung()).append('|')
                    .append(thuoc.isCanKeDon());
        } else if (sp instanceof PhuKien phuKien) {
            sb.append(Esc.ma(phuKien.getChatLieu())).append('|')
                    .append(Esc.ma(phuKien.getMauSac()));
        } else {
            throw new IllegalArgumentException("Không hỗ trợ loại sản phẩm: " + sp.getClass().getSimpleName());
        }
        return sb.toString();
    }

    @Override
    public SanPham fromLine(String dong) {
        String[] p = dong.split("\\|", -1);
        if (p.length < 6) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/6)");
        }
        String loai = p[0];
        String ma = p[1];
        String ten = Esc.giaiMa(p[2]);
        double gia = Double.parseDouble(p[3]);
        int soLuong = Integer.parseInt(p[4]);

        return switch (loai) {
            case "Thức ăn" -> new ThucAn(ma, ten, gia, soLuong,
                    Esc.giaiMa(p[5]),
                    p.length > 6 ? Double.parseDouble(p[6]) : 0,
                    p.length > 7 && !p[7].isEmpty() ? LocalDate.parse(p[7]) : null);
            case "Thuốc" -> new Thuoc(ma, ten, gia, soLuong,
                    Esc.giaiMa(p[5]),
                    p.length > 6 && !p[6].isEmpty() ? LocalDate.parse(p[6]) : null,
                    p.length > 7 && Boolean.parseBoolean(p[7]));
            case "Phụ kiện" -> new PhuKien(ma, ten, gia, soLuong,
                    Esc.giaiMa(p[5]),
                    p.length > 6 ? Esc.giaiMa(p[6]) : "");
            default -> throw new IllegalArgumentException("loại sản phẩm không hợp lệ: " + loai);
        };
    }
}
