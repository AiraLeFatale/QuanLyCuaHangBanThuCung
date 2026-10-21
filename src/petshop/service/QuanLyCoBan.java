package petshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import petshop.exception.KhongTimThayException;
import petshop.exception.NghiepVuException;
import petshop.io.TextFileRepository;
import petshop.model.CoMaKhoa;

/**
 * Cài đặt dùng chung cho mọi danh mục: giữ danh sách trong bộ nhớ,
 * kiểm tra trùng mã, sinh mã tự động và đồng bộ xuống tệp.
 */
public abstract class QuanLyCoBan<T extends CoMaKhoa> implements QuanLy<T> {

    protected final List<T> danhSach = new ArrayList<>();
    private final TextFileRepository<T> kho;
    private final String tienToMa;

    protected QuanLyCoBan(TextFileRepository<T> kho, String tienToMa) {
        this.kho = kho;
        this.tienToMa = tienToMa;
    }

    /** Nạp lại toàn bộ dữ liệu từ tệp (dùng khi khởi động). */
    public final void napDuLieu() {
        danhSach.clear();
        danhSach.addAll(kho.doc());
    }

    @Override
    public List<T> tatCa() {
        return Collections.unmodifiableList(danhSach);
    }

    @Override
    public int soLuong() {
        return danhSach.size();
    }

    @Override
    public Optional<T> tim(String ma) {
        if (ma == null) {
            return Optional.empty();
        }
        String khoa = ma.trim();
        return danhSach.stream()
                .filter(t -> t.getMa().equalsIgnoreCase(khoa))
                .findFirst();
    }

    @Override
    public boolean chuaCo(String ma) {
        return tim(ma).isEmpty();
    }

    protected T batBuocTim(String ma) {
        return tim(ma).orElseThrow(() -> new KhongTimThayException("Không tìm thấy mã " + ma));
    }

    protected int chiSo(String ma) {
        if (ma != null) {
            for (int i = 0; i < danhSach.size(); i++) {
                if (danhSach.get(i).getMa().equalsIgnoreCase(ma.trim())) {
                    return i;
                }
            }
        }
        throw new KhongTimThayException("Không tìm thấy mã " + ma);
    }

    @Override
    public void them(T doiTuong) {
        Objects.requireNonNull(doiTuong, "Đối tượng không được null");
        if (!chuaCo(doiTuong.getMa())) {
            throw new NghiepVuException("Mã " + doiTuong.getMa() + " đã tồn tại");
        }
        danhSach.add(doiTuong);
    }

    @Override
    public void capNhat(T doiTuong) {
        Objects.requireNonNull(doiTuong, "Đối tượng không được null");
        danhSach.set(chiSo(doiTuong.getMa()), doiTuong);
    }

    @Override
    public void xoa(String ma) {
        danhSach.remove(chiSo(ma));
    }

    @Override
    public String sinhMa() {
        int lonNhat = 0;
        for (T doiTuong : danhSach) {
            String ma = doiTuong.getMa();
            if (ma == null || !ma.toUpperCase(Locale.ROOT).startsWith(tienToMa)) {
                continue;
            }
            String phanSo = ma.substring(tienToMa.length()).replaceAll("\\D", "");
            if (phanSo.isEmpty()) {
                continue;
            }
            try {
                lonNhat = Math.max(lonNhat, Integer.parseInt(phanSo));
            } catch (NumberFormatException ignored) {
                // mã không theo quy ước thì bỏ qua
            }
        }
        return String.format("%s%03d", tienToMa, lonNhat + 1);
    }

    @Override
    public void luu() {
        kho.ghi(danhSach);
    }
}
