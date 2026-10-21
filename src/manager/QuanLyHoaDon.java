package manager;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import inter.IFile;

import java.util.ArrayList;
import java.util.List;

public class QuanLyHoaDon implements IFile {
    private ArrayList<HoaDon> ds = new ArrayList<>();

    public ArrayList<HoaDon> getDs() {
        return ds;
    }

    public void them(HoaDon hd) {
        ds.add(hd);
    }

    public HoaDon timTheoMa(String ma) {
        for (HoaDon hd : ds) {
            if (hd.getMaHD().equalsIgnoreCase(ma)) return hd;
        }
        return null;
    }

    public boolean sua(String ma, String maNV, String ngayLap) {
        HoaDon hd = timTheoMa(ma);
        if (hd == null) return false;
        hd.setMaNV(maNV);
        hd.setNgayLap(ngayLap);
        return true;
    }

    public boolean xoa(String ma) {
        HoaDon hd = timTheoMa(ma);
        return hd != null && ds.remove(hd);
    }

    public ArrayList<HoaDon> timTheoKhachHang(String maKH) {
        ArrayList<HoaDon> kq = new ArrayList<>();
        for (HoaDon hd : ds) {
            if (hd.getMaKH().equalsIgnoreCase(maKH)) kq.add(hd);
        }
        return kq;
    }

    /** Sinh ma hoa don moi dang HD001, HD002, ... */
    public String taoMaMoi() {
        int max = 0;
        for (HoaDon hd : ds) {
            try {
                max = Math.max(max, Integer.parseInt(hd.getMaHD().replaceAll("\\D", "")));
            } catch (NumberFormatException e) {
                // bo qua ma khong co so
            }
        }
        return String.format("HD%03d", max + 1);
    }

    public double tongDoanhThu() {
        double tong = 0;
        for (HoaDon hd : ds) tong += hd.tongTien();
        return tong;
    }

    public void hienThiTatCa() {
        if (ds.isEmpty()) {
            System.out.println("Chua co hoa don nao.");
            return;
        }
        for (HoaDon hd : ds) hd.inChiTiet();
        System.out.println("Tong: " + ds.size() + " hoa don.");
    }

    @Override
    public void docFile(String tenFile) {
        ds.clear();
        for (String line : IFile.docCacDong(tenFile)) {
            try {
                String[] p = line.split("\\|", -1);
                HoaDon hd = new HoaDon(p[0], p[1], p[2], p[3]);
                if (p.length > 4 && !p[4].isEmpty()) {
                    for (String s : p[4].split("#")) {
                        hd.themChiTiet(ChiTietHoaDon.fromFileString(s));
                    }
                }
                ds.add(hd);
            } catch (RuntimeException e) {
                System.out.println("Bo qua dong loi: " + line);
            }
        }
    }

    @Override
    public void ghiFile(String tenFile) {
        List<String> dong = new ArrayList<>();
        for (HoaDon hd : ds) dong.add(hd.toFileString());
        IFile.ghiCacDong(tenFile, dong);
    }
}
