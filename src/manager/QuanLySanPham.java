package manager;

import entity.*;
import inter.IFile;

import java.util.ArrayList;
import java.util.List;

public class QuanLySanPham implements IFile {
    private ArrayList<SanPham> ds = new ArrayList<>();

    public ArrayList<SanPham> getDs() {
        return ds;
    }

    public void them(SanPham sp) {
        ds.add(sp);
    }

    public SanPham timTheoMa(String ma) {
        for (SanPham sp : ds) {
            if (sp.getMaSP().equalsIgnoreCase(ma)) return sp;
        }
        return null;
    }

    public boolean sua(String ma, String ten, double gia, int soLuong) {
        SanPham sp = timTheoMa(ma);
        if (sp == null) return false;
        sp.setTenSP(ten);
        sp.setGia(gia);
        sp.setSoLuong(soLuong);
        return true;
    }

    public boolean xoa(String ma) {
        SanPham sp = timTheoMa(ma);
        return sp != null && ds.remove(sp);
    }

    public ArrayList<SanPham> timTheoTen(String tuKhoa) {
        ArrayList<SanPham> kq = new ArrayList<>();
        for (SanPham sp : ds) {
            if (sp.getTenSP().toLowerCase().contains(tuKhoa.toLowerCase())) kq.add(sp);
        }
        return kq;
    }

    public void hienThiTatCa() {
        if (ds.isEmpty()) {
            System.out.println("Danh sach san pham trong.");
            return;
        }
        for (SanPham sp : ds) System.out.println(sp);
        System.out.println("Tong: " + ds.size() + " san pham.");
    }

    @Override
    public void docFile(String tenFile) {
        ds.clear();
        for (String line : IFile.docCacDong(tenFile)) {
            try {
                String[] p = line.split("\\|", -1);
                String ma = p[1], ten = p[2];
                double gia = Double.parseDouble(p[3]);
                int sl = Integer.parseInt(p[4]);
                switch (p[0]) {
                    case "THUCAN":
                        ds.add(new ThucAn(ma, ten, gia, sl, p[5], Double.parseDouble(p[6]), p[7]));
                        break;
                    case "THUOC":
                        ds.add(new Thuoc(ma, ten, gia, sl, p[5], p[6]));
                        break;
                    case "PHUKIEN":
                        ds.add(new PhuKien(ma, ten, gia, sl, p[5], p[6]));
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
        for (SanPham sp : ds) dong.add(sp.toFileString());
        IFile.ghiCacDong(tenFile, dong);
    }
}
