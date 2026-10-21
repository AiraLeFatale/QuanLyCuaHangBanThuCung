package petshop.model;

public enum TrangThaiThuCung {

    CON_BAN("Còn bán"),
    DA_BAN("Đã bán"),
    CACH_LY("Cách ly");

    private final String nhan;

    TrangThaiThuCung(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }

    public static TrangThaiThuCung tuChuoi(String chuoi) {
        if (chuoi == null) {
            return CON_BAN;
        }
        String s = chuoi.trim();
        for (TrangThaiThuCung tt : values()) {
            if (tt.name().equalsIgnoreCase(s) || tt.nhan.equalsIgnoreCase(s)) {
                return tt;
            }
        }
        return CON_BAN;
    }

    @Override
    public String toString() {
        return nhan;
    }
}
