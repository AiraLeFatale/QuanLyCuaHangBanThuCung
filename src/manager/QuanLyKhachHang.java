package manager;

import entity.KhachHang;
import inter.IFile;

import java.util.ArrayList;
import java.util.List;

public class QuanLyKhachHang implements IFile {
    private ArrayList<KhachHang> ds = new ArrayList<>();

    public ArrayList<KhachHang> getDs() {
        return ds;
    }

    public void them(KhachHang kh) {
        ds.add(kh);
    }

    public KhachHang timTheoMa(String ma) {
        for (KhachHang kh : ds) {
            if (kh.getMa().equalsIgnoreCase(ma)) return kh;
        }
        return null;
    }

    public boolean sua(String ma, String hoTen, String sdt, String diaChi) {
        KhachHang kh = timTheoMa(ma);
        if (kh == null) return false;
        kh.setHoTen(hoTen);
        kh.setSdt(sdt);
        kh.setDiaChi(diaChi);
        return true;
    }

    public boolean xoa(String ma) {
        KhachHang kh = timTheoMa(ma);
        return kh != null && ds.remove(kh);
    }

    public ArrayList<KhachHang> timTheoTen(String tuKhoa) {
        ArrayList<KhachHang> kq = new ArrayList<>();
        for (KhachHang kh : ds) {
            if (kh.getHoTen().toLowerCase().contains(tuKhoa.toLowerCase())) kq.add(kh);
        }
        return kq;
    }

    public void hienThiTatCa() {
        if (ds.isEmpty()) {
            System.out.println("Danh sach khach hang trong.");
            return;
        }
        for (KhachHang kh : ds) System.out.println(kh);
        System.out.println("Tong: " + ds.size() + " khach hang.");
    }

    @Override
    public void docFile(String tenFile) {
        ds.clear();
        for (String line : IFile.docCacDong(tenFile)) {
            try {
                String[] p = line.split("\\|", -1);
                ds.add(new KhachHang(p[0], p[1], p[2], p[3], Integer.parseInt(p[4])));
            } catch (RuntimeException e) {
                System.out.println("Bo qua dong loi: " + line);
            }
        }
    }

    @Override
    public void ghiFile(String tenFile) {
        List<String> dong = new ArrayList<>();
        for (KhachHang kh : ds) dong.add(kh.toFileString());
        IFile.ghiCacDong(tenFile, dong);
    }
}
