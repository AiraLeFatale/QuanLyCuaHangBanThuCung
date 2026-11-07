package entity;

import util.DocGhiFile;

/** Đồ chơi cho thú cưng: loại đồ chơi và kích thước. */
public class DoChoi extends SanPham {

    private String loaiDoChoi;
    private String kichThuoc;

    public DoChoi(String ma, String ten, double giaBan, int soLuong,
                  String loaiDoChoi, String kichThuoc) {
        super(ma, ten, giaBan, soLuong);
        this.loaiDoChoi = loaiDoChoi == null ? "" : loaiDoChoi;
        this.kichThuoc = kichThuoc == null ? "" : kichThuoc;
    }

    public String getLoaiDoChoi() {
        return loaiDoChoi;
    }

    public void setLoaiDoChoi(String loaiDoChoi) {
        this.loaiDoChoi = loaiDoChoi == null ? "" : loaiDoChoi;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc == null ? "" : kichThuoc;
    }

    @Override
    public String getLoai() {
        return "Đồ chơi";
    }

    @Override
    public String getDacDiem() {
        return "Loại " + loaiDoChoi + ", kích thước " + kichThuoc;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + DocGhiFile.maHoa(loaiDoChoi) + "|"
                + DocGhiFile.maHoa(kichThuoc);
    }
}
