package entity;

import util.DocGhiFile;

/** Quần áo cho thú cưng: kích cỡ và chất liệu. */
public class QuanAo extends SanPham {

    private String kichCo;
    private String chatLieu;

    public QuanAo(String ma, String ten, double giaBan, int soLuong,
                  String kichCo, String chatLieu) {
        super(ma, ten, giaBan, soLuong);
        this.kichCo = kichCo == null ? "" : kichCo;
        this.chatLieu = chatLieu == null ? "" : chatLieu;
    }

    public String getKichCo() {
        return kichCo;
    }

    public void setKichCo(String kichCo) {
        this.kichCo = kichCo == null ? "" : kichCo;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu == null ? "" : chatLieu;
    }

    @Override
    public String getLoai() {
        return "Quần áo";
    }

    @Override
    public String getDacDiem() {
        return "Cỡ " + kichCo + ", chất liệu " + chatLieu;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + DocGhiFile.maHoa(kichCo) + "|"
                + DocGhiFile.maHoa(chatLieu);
    }
}
