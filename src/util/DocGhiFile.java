package util;

import entity.ChiTietHoaDon;
import entity.Chim;
import entity.Cho;
import entity.DichVu;
import entity.DichVuCatTia;
import entity.DichVuTam;
import entity.DichVuTiemPhong;
import entity.GioiTinh;
import entity.HangKhachHang;
import entity.HoaDon;
import entity.KhachHang;
import entity.Meo;
import entity.NhanVien;
import entity.PhuKien;
import entity.SanPham;
import entity.ThuCung;
import entity.ThucAn;
import entity.Thuoc;
import entity.TrangThaiThuCung;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc / ghi dữ liệu ra tệp văn bản .txt (UTF-8): mỗi dòng là một đối tượng.
 * Gồm: Codec (đổi đối tượng <-> dòng chữ), Kho (đọc/ghi cả tệp) và 6 Codec cụ thể.
 */
public final class DocGhiFile {

    private DocGhiFile() {
    }

    /**
     * Mã hóa/giải mã các ký tự đặc biệt dùng làm dấu phân cách trong tệp .txt.
     * Nhờ vậy tên thú cưng hay địa chỉ có chứa "|" hoặc ";" vẫn lưu được an toàn.
     */
    private static final class Esc {

        private Esc() {
        }

        public static String ma(String chuoi) {
            if (chuoi == null || chuoi.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder(chuoi.length() + 8);
            for (int i = 0; i < chuoi.length(); i++) {
                char c = chuoi.charAt(i);
                switch (c) {
                    case '\\' -> sb.append("\\\\");
                    case '|' -> sb.append("\\p");
                    case ';' -> sb.append("\\s");
                    case '~' -> sb.append("\\t");
                    case '\n' -> sb.append("\\n");
                    case '\r' -> sb.append("\\r");
                    default -> sb.append(c);
                }
            }
            return sb.toString();
        }

        public static String giaiMa(String chuoi) {
            if (chuoi == null || chuoi.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder(chuoi.length());
            for (int i = 0; i < chuoi.length(); i++) {
                char c = chuoi.charAt(i);
                if (c != '\\' || i == chuoi.length() - 1) {
                    sb.append(c);
                    continue;
                }
                char tiepTheo = chuoi.charAt(++i);
                switch (tiepTheo) {
                    case '\\' -> sb.append('\\');
                    case 'p' -> sb.append('|');
                    case 's' -> sb.append(';');
                    case 't' -> sb.append('~');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    default -> sb.append(tiepTheo);
                }
            }
            return sb.toString();
        }
    }

    /** Chuyển một đối tượng thành một dòng văn bản và ngược lại. */
    public interface Codec<T> {

        String toLine(T doiTuong);

        T fromLine(String dong);
    }

    /**
     * Kho dữ liệu dùng tệp văn bản UTF-8, mỗi dòng là một đối tượng.
     * Dùng lại được cho mọi kiểu T nhờ TextCodec (generic + interface).
     */
    public static class Kho<T> {

        private final Path tep;
        private final Codec<T> codec;

        public Kho(Path tep, Codec<T> codec) {
            this.tep = tep;
            this.codec = codec;
        }

        public Path getTep() {
            return tep;
        }

        public List<T> doc() {
            List<T> ketQua = new ArrayList<>();
            if (!Files.exists(tep)) {
                return ketQua;
            }
            try {
                List<String> cacDong = Files.readAllLines(tep, StandardCharsets.UTF_8);
                for (int i = 0; i < cacDong.size(); i++) {
                    String dong = cacDong.get(i).trim();
                    if (dong.isEmpty() || dong.startsWith("#")) {
                        continue;
                    }
                    try {
                        ketQua.add(codec.fromLine(dong));
                    } catch (RuntimeException ex) {
                        throw new AppException("Dữ liệu không hợp lệ trong " + tep.getFileName()
                                + ", dòng " + (i + 1) + ": " + ex.getMessage(), ex);
                    }
                }
            } catch (IOException ex) {
                throw new AppException("Không đọc được tệp " + tep + ": " + ex.getMessage(), ex);
            }
            return ketQua;
        }

        /** Ghi đè toàn bộ tệp (ghi ra tệp tạm rồi đổi tên để tránh mất dữ liệu). */
        public void ghi(List<T> danhSach) {
            try {
                Path thuMuc = tep.toAbsolutePath().getParent();
                if (thuMuc != null) {
                    Files.createDirectories(thuMuc);
                }
                List<String> cacDong = new ArrayList<>();
                cacDong.add("# " + tep.getFileName() + " - dữ liệu cửa hàng thú cưng (UTF-8)");
                for (T doiTuong : danhSach) {
                    cacDong.add(codec.toLine(doiTuong));
                }
                Path tepTam = tep.resolveSibling(tep.getFileName() + ".tmp");
                Files.write(tepTam, cacDong, StandardCharsets.UTF_8);
                Files.move(tepTam, tep, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                throw new AppException("Không ghi được tệp " + tep + ": " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Định dạng mỗi dòng:
     * loai|ma|ten|ngaySinh|giaBan|soLuong|trangThai|thuocTinhRieng...
     */
    public static class ThuCungCodec implements Codec<ThuCung> {

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

    /**
     * Định dạng mỗi dòng:
     * loai|ma|ten|giaBan|soLuong|thuocTinhRieng...
     * - Thức ăn : Cho/Mèo/Chim | khối lượng (kg) | hạn sử dụng
     * - Thuốc   : công dụng | hạn sử dụng | cần kê đơn (true/false)
     * - Phụ kiện: chất liệu | màu sắc
     */
    public static class SanPhamCodec implements Codec<SanPham> {

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

    /**
     * Định dạng mỗi dòng:
     * loai|ma|ten|giaBan|thoiGianPhut|thuocTinhRieng
     * - Tắm & sấy : kích thước thú cưng
     * - Cắt tỉa   : kiểu lông
     * - Tiêm phòng: loại vắc-xin
     */
    public static class DichVuCodec implements Codec<DichVu> {

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

    /** Định dạng mỗi dòng: ma|hoTen|gioiTinh|ngaySinh|soDienThoai|diaChi|diemTichLuy */
    public static class KhachHangCodec implements Codec<KhachHang> {

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

    /** Định dạng mỗi dòng: ma|hoTen|gioiTinh|ngaySinh|soDienThoai|diaChi|chucVu|luong|ngayVaoLam */
    public static class NhanVienCodec implements Codec<NhanVien> {

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

    /**
     * Định dạng mỗi dòng:
     * ma|ngayLap|maKhachHang|tenKhachHang|hang|maNhanVien|tenNhanVien|danhSachChiTiet
     * trong đó danhSachChiTiet là các bộ "maThuCung~ten~loai~soLuong~donGia" ngăn bởi ';'.
     */
    public static class HoaDonCodec implements Codec<HoaDon> {

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
}
