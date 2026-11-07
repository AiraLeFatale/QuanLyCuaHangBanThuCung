package entity;

import java.time.LocalDate;

import util.DocGhiFile;

/** Chim cảnh. Đặc điểm riêng: loài chim, có biết nói không, màu lông. */
public class Chim extends ThuCung {

    private String loaiChim;
    private boolean bietNoi;
    private String mauLong;

    public Chim(String ma, String ten, LocalDate ngaySinh, double giaBan, int soLuong,
                TrangThaiThuCung trangThai, String loaiChim, boolean bietNoi, String mauLong) {
        super(ma, ten, ngaySinh, giaBan, soLuong, trangThai);
        this.loaiChim = loaiChim == null ? "" : loaiChim;
        this.bietNoi = bietNoi;
        this.mauLong = mauLong == null ? "" : mauLong;
    }

    public String getLoaiChim() {
        return loaiChim;
    }

    public void setLoaiChim(String loaiChim) {
        this.loaiChim = loaiChim == null ? "" : loaiChim;
    }

    public boolean isBietNoi() {
        return bietNoi;
    }

    public void setBietNoi(boolean bietNoi) {
        this.bietNoi = bietNoi;
    }

    public String getMauLong() {
        return mauLong;
    }

    public void setMauLong(String mauLong) {
        this.mauLong = mauLong == null ? "" : mauLong;
    }

    @Override
    public String getLoai() {
        return "Chim";
    }

    @Override
    public String getDacDiem() {
        return "Loài " + loaiChim + ", lông màu " + mauLong + (bietNoi ? ", biết nói" : "");
    }

    @Override
    public String toFileString() {
        return super.toFileString() + DocGhiFile.maHoa(loaiChim) + "|"
                + bietNoi + "|" + DocGhiFile.maHoa(mauLong);
    }

    @Override
    public double getChiPhiChamSoc() {
        return bietNoi ? 350_000 : 250_000;
    }
}
