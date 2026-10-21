package petshop.io;

import petshop.model.DichVu;
import petshop.model.DichVuCatTia;
import petshop.model.DichVuTam;
import petshop.model.DichVuTiemPhong;

/**
 * Định dạng mỗi dòng:
 * loai|ma|ten|giaBan|thoiGianPhut|thuocTinhRieng
 * - Tắm & sấy : kích thước thú cưng
 * - Cắt tỉa   : kiểu lông
 * - Tiêm phòng: loại vắc-xin
 */
public class DichVuCodec implements TextCodec<DichVu> {

    @Override
    public String toLine(DichVu dv) {
        String rieng;
        if (dv instanceof DichVuTam tam) {
            rieng = Esc.ma(tam.getKichThuoc());
        } else if (dv instanceof DichVuCatTia catTia) {
            rieng = Esc.ma(catTia.getKieuLong());
        } else if (dv instanceof DichVuTiemPhong tiemPhong) {
            rieng = Esc.ma(tiemPhong.getLoaiVacXin());
        } else {
            throw new IllegalArgumentException("Không hỗ trợ loại dịch vụ: " + dv.getClass().getSimpleName());
        }
        return String.join("|",
                dv.getLoai(),
                dv.getMa(),
                Esc.ma(dv.getTen()),
                String.valueOf(Math.round(dv.getGiaBan())),
                String.valueOf(dv.getThoiGianPhut()),
                rieng);
    }

    @Override
    public DichVu fromLine(String dong) {
        String[] p = dong.split("\\|", -1);
        if (p.length < 6) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/6)");
        }
        String loai = p[0];
        String ma = p[1];
        String ten = Esc.giaiMa(p[2]);
        double gia = Double.parseDouble(p[3]);
        int thoiGian = Integer.parseInt(p[4]);
        String rieng = Esc.giaiMa(p[5]);

        return switch (loai) {
            case "Tắm & sấy" -> new DichVuTam(ma, ten, gia, thoiGian, rieng);
            case "Cắt tỉa" -> new DichVuCatTia(ma, ten, gia, thoiGian, rieng);
            case "Tiêm phòng" -> new DichVuTiemPhong(ma, ten, gia, thoiGian, rieng);
            default -> throw new IllegalArgumentException("loại dịch vụ không hợp lệ: " + loai);
        };
    }
}
