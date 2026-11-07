package entity;

import util.DocGhiFile;

/** Phụ kiện cho thú cưng: chất liệu và màu sắc. */
public class PhuKien extends SanPham {

    private String chatLieu;
    private String mauSac;

    public PhuKien(String ma, String ten, double giaBan, int soLuong,
                   String chatLieu, String mauSac) {
        super(ma, ten, giaBan, soLuong);
        this.chatLieu = chatLieu == null ? "" : chatLieu;
        this.mauSac = mauSac == null ? "" : mauSac;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu == null ? "" : chatLieu;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac == null ? "" : mauSac;
    }

    @Override
    public String getLoai() {
        return "Phụ kiện";
    }

    @Override
    public String getDacDiem() {
        return "Chất liệu " + chatLieu + ", màu " + mauSac;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + DocGhiFile.maHoa(chatLieu) + "|"
                + DocGhiFile.maHoa(mauSac);
    }
}
