package manager;

import entity.*;
import inter.IFile;

import java.util.ArrayList;
import java.util.List;

/** Lop mang doi tuong: danh sach thu cung + CRUD. */
public class QuanLyThuCung implements IFile {
    private ArrayList<ThuCung> ds = new ArrayList<>();

    public ArrayList<ThuCung> getDs() {
        return ds;
    }

    // ===== CRUD =====
    public void them(ThuCung t) {
        ds.add(t);
    }

    public ThuCung timTheoMa(String ma) {
        for (ThuCung t : ds) {
            if (t.getMaThuCung().equalsIgnoreCase(ma)) return t;
        }
        return null;
    }

    public boolean sua(String ma, String ten, int tuoiThang, double gia, String gioiTinh) {
        ThuCung t = timTheoMa(ma);
        if (t == null) return false;
        t.setTen(ten);
        t.setTuoiThang(tuoiThang);
        t.setGia(gia);
        t.setGioiTinh(gioiTinh);
        return true;
    }

    public boolean xoa(String ma) {
        ThuCung t = timTheoMa(ma);
        return t != null && ds.remove(t);
    }

    public ArrayList<ThuCung> timTheoTen(String tuKhoa) {
        ArrayList<ThuCung> kq = new ArrayList<>();
        for (ThuCung t : ds) {
            if (t.getTen().toLowerCase().contains(tuKhoa.toLowerCase())) kq.add(t);
        }
        return kq;
    }

    public void hienThiTatCa() {
        if (ds.isEmpty()) {
            System.out.println("Danh sach thu cung trong.");
            return;
        }
        for (ThuCung t : ds) System.out.println(t);
        System.out.println("Tong: " + ds.size() + " thu cung.");
    }

    /** Minh hoa da hinh: moi con vat keu theo cach rieng cua no. */
    public void choTatCaKeu() {
        for (ThuCung t : ds) {
            System.out.println(t.getLoai() + " " + t.getTen() + ": " + t.tiengKeu());
        }
    }

    // ===== File =====
    @Override
    public void docFile(String tenFile) {
        ds.clear();
        for (String line : IFile.docCacDong(tenFile)) {
            try {
                String[] p = line.split("\\|", -1);
                String ma = p[1], ten = p[2], gt = p[5];
                int tuoi = Integer.parseInt(p[3]);
                double gia = Double.parseDouble(p[4]);
                boolean daBan = Boolean.parseBoolean(p[6]);
                switch (p[0]) {
                    case "CHO":
                        ds.add(new Cho(ma, ten, tuoi, gia, gt, daBan, p[7], Double.parseDouble(p[8])));
                        break;
                    case "MEO":
                        ds.add(new Meo(ma, ten, tuoi, gia, gt, daBan, p[7], Boolean.parseBoolean(p[8])));
                        break;
                    case "CHIM":
                        ds.add(new Chim(ma, ten, tuoi, gia, gt, daBan, p[7], Boolean.parseBoolean(p[8])));
                        break;
                    default:
                        System.out.println("Bo qua dong khong hop le: " + line);
                }
            } catch (RuntimeException e) {
                System.out.println("Bo qua dong loi: " + line);
            }
        }
    }

    @Override
    public void ghiFile(String tenFile) {
        List<String> dong = new ArrayList<>();
        for (ThuCung t : ds) dong.add(t.toFileString());
        IFile.ghiCacDong(tenFile, dong);
    }
}
