# Quan Ly Cua Hang Thu Cung (Java OOP)

## Chay bang NetBeans / IntelliJ / Eclipse
1. Tao Java project moi, copy thu muc `src/` va `data/` vao thu muc goc project
   (`data/` phai nam ngang hang voi `src/`).
2. Chay file `src/main/Main.java`.

## Chay bang dong lenh (can JDK)
Windows: double-click `chay.bat`
Linux/Mac:
    javac -encoding UTF-8 -d out src/inter/*.java src/entity/*.java src/manager/*.java src/main/*.java
    java -cp out main.Main
(Luon chay tu thu muc goc QuanLyCuaHangThuCung de doc dung `data/*.txt`.)

## Dinh dang file data (phan cach bang |)
- thucung.txt  : LOAI|ma|ten|tuoiThang|gia|gioiTinh|daBan|... (CHO: giong|canNang, MEO: mauLong|longDai, CHIM: mauSac|bietNoi)
- sanpham.txt  : LOAI|ma|ten|gia|soLuong|... (THUCAN: loaiThuCung|kg|HSD, THUOC: congDung|HSD, PHUKIEN: chatLieu|mauSac)
- khachhang.txt: ma|hoTen|sdt|diaChi|diemTichLuy
- nhanvien.txt : ma|hoTen|sdt|diaChi|chucVu|luong
- hoadon.txt   : maHD|maKH|maNV|ngay|maHang;tenHang;sl;donGia;giam#...
