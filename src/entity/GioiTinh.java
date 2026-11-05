package entity;

public enum GioiTinh {

    NAM("Nam"),
    NU("Nữ"),
    KHAC("Khác");

    private final String nhan;

    GioiTinh(String nhan) {
        this.nhan = nhan;
    }

    public String getNhan() {
        return nhan;
    }

    /** Chuyển chuỗi bất kỳ (tên enum hoặc nhãn tiếng Việt) về enum, mặc định là KHAC. */
    public static GioiTinh tuChuoi(String chuoi) {
        if (chuoi == null) {
            return KHAC;
        }
        String s = chuoi.trim();
        for (GioiTinh gt : values()) {
            if (gt.name().equalsIgnoreCase(s) || gt.nhan.equalsIgnoreCase(s)) {
                return gt;
            }
        }
        return KHAC;
    }

    @Override
    public String toString() {
        return nhan;
    }
}
