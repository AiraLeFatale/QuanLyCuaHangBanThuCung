package manager;

import entity.NhanVien;
import inter.IFile;

import java.util.ArrayList;
import java.util.List;

public class QuanLyNhanVien implements IFile {
    private ArrayList<NhanVien> ds = new ArrayList<>();

    public ArrayList<NhanVien> getDs() {
        return ds;
    }

    public void them(NhanVien nv) {
        ds.add(nv);
    }

    public NhanVien timTheoMa(String ma) {
        for (NhanVien nv : ds) {
            if (nv.getMa().equalsIgnoreCase(ma)) return nv;
        }
        return null;
    }

    public boolean sua(String ma, String hoTen, String sdt, String diaChi, String chucVu, double luong) {
        NhanVien nv = timTheoMa(ma);
        if (nv == null) return false;
        nv.setHoTen(hoTen);
        nv.setSdt(sdt);
        nv.setDiaChi(diaChi);
        nv.setChucVu(chucVu);
        nv.setLuong(luong);
        return true;
    }

    public boolean xoa(String ma) {
        NhanVien nv = timTheoMa(ma);
        return nv != null && ds.remove(nv);
    }

    public ArrayList<NhanVien> timTheoTen(String tuKhoa) {
        ArrayList<NhanVien> kq = new ArrayList<>();
        for (NhanVien nv : ds) {
            if (nv.getHoTen().toLowerCase().contains(tuKhoa.toLowerCase())) kq.add(nv);
        }
        return kq;
    }

    public void hienThiTatCa() {
        if (ds.isEmpty()) {
            System.out.println("Danh sach nhan vien trong.");
            return;
        }
        for (NhanVien nv : ds) System.out.println(nv);
        System.out.println("Tong: " + ds.size() + " nhan vien.");
    }

    @Override
    public void docFile(String tenFile) {
        ds.clear();
        for (String line : IFile.docCacDong(tenFile)) {
            try {
                String[] p = line.split("\\|", -1);
                ds.add(new NhanVien(p[0], p[1], p[2], p[3], p[4], Double.parseDouble(p[5])));
            } catch (RuntimeException e) {
                System.out.println("Bo qua dong loi: " + line);
            }
        }
    }

    @Override
    public void ghiFile(String tenFile) {
        List<String> dong = new ArrayList<>();
        for (NhanVien nv : ds) dong.add(nv.toFileString());
        IFile.ghiCacDong(tenFile, dong);
    }
}
