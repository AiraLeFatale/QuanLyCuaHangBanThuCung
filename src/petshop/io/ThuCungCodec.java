package petshop.io;

import java.time.LocalDate;

import petshop.model.Cho;
import petshop.model.Chim;
import petshop.model.Meo;
import petshop.model.ThuCung;
import petshop.model.TrangThaiThuCung;

/**
 * Định dạng mỗi dòng:
 * loai|ma|ten|ngaySinh|giaBan|soLuong|trangThai|thuocTinhRieng...
 */
public class ThuCungCodec implements TextCodec<ThuCung> {

    private static final int SO_TRUONG_TOI_THIEU = 10;

    @Override
    public String toLine(ThuCung t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getLoai()).append('|')
                .append(t.getMa()).append('|')
                .append(Esc.ma(t.getTen())).append('|')
                .append(t.getNgaySinh() == null ? "" : t.getNgaySinh()).append('|')
                .append(Math.round(t.getGiaBan())).append('|')
                .append(t.getSoLuong()).append('|')
                .append(t.getTrangThai().name()).append('|');

        if (t instanceof Cho cho) {
            sb.append(Esc.ma(cho.getGiong())).append('|')
                    .append(Esc.ma(cho.getKichThuoc())).append('|')
                    .append(cho.isDaTiemPhong());
        } else if (t instanceof Meo meo) {
            sb.append(Esc.ma(meo.getGiong())).append('|')
                    .append(meo.isDaiLong()).append('|')
                    .append(meo.isDaTrietSan());
        } else if (t instanceof Chim chim) {
            sb.append(Esc.ma(chim.getLoaiChim())).append('|')
                    .append(chim.isBietNoi()).append('|')
                    .append(Esc.ma(chim.getMauLong()));
        } else {
            throw new IllegalArgumentException("Không hỗ trợ loại thú cưng: " + t.getClass().getSimpleName());
        }
        return sb.toString();
    }

    @Override
    public ThuCung fromLine(String dong) {
        String[] p = dong.split("\\|", -1);
        if (p.length < SO_TRUONG_TOI_THIEU) {
            throw new IllegalArgumentException("thiếu trường dữ liệu (" + p.length + "/" + SO_TRUONG_TOI_THIEU + ")");
        }
        String loai = p[0];
        String ma = p[1];
        String ten = Esc.giaiMa(p[2]);
        LocalDate ngaySinh = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
        double giaBan = Double.parseDouble(p[4]);
        int soLuong = Integer.parseInt(p[5]);
        TrangThaiThuCung trangThai = TrangThaiThuCung.valueOf(p[6]);

        return switch (loai) {
            case "Chó" -> new Cho(ma, ten, ngaySinh, giaBan, soLuong, trangThai,
                    Esc.giaiMa(p[7]), Esc.giaiMa(p[8]), Boolean.parseBoolean(p[9]));
            case "Mèo" -> new Meo(ma, ten, ngaySinh, giaBan, soLuong, trangThai,
                    Esc.giaiMa(p[7]), Boolean.parseBoolean(p[8]), Boolean.parseBoolean(p[9]));
            case "Chim" -> new Chim(ma, ten, ngaySinh, giaBan, soLuong, trangThai,
                    Esc.giaiMa(p[7]), Boolean.parseBoolean(p[8]), Esc.giaiMa(p[9]));
            default -> throw new IllegalArgumentException("loại thú cưng không hợp lệ: " + loai);
        };
    }
}
