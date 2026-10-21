# Quản Lý Cửa Hàng Bán Thú Cưng

Bài tập lớn môn **Lập trình hướng đối tượng** — ứng dụng Java **chạy trên console** (menu dòng lệnh)
quản lý cửa hàng bán thú cưng, lưu dữ liệu bằng tệp văn bản `.txt` (UTF-8).
Không dùng giao diện đồ họa, không cần cài đặt cơ sở dữ liệu, không dùng Maven/Gradle.

## 1. Chức năng chính

| Nhóm chức năng | Mô tả |
| --- | --- |
| Quản lý thú cưng | Xem, thêm, cập nhật, xóa, tìm kiếm / lọc theo loại, sắp xếp theo giá. Mỗi loài (Chó, Mèo, Chim) có thuộc tính riêng; theo dõi tồn kho và trạng thái (còn bán / đã bán / cách ly) |
| Quản lý khách hàng | Hồ sơ khách hàng, điểm tích lũy, tự động xếp hạng (Thường, Bạc, Vàng, Kim cương) và mức chiết khấu tương ứng; chống trùng số điện thoại |
| Quản lý nhân viên | Hồ sơ, chức vụ, lương, ngày vào làm; xem tổng quỹ lương |
| Bán hàng | Chọn khách hàng + nhân viên, thêm thú cưng vào giỏ, kiểm tra tồn kho, tính chiết khấu theo hạng, thanh toán và in hóa đơn ra màn hình |
| Thống kê | Doanh thu, số hóa đơn, số thú cưng đã bán, giá trị tồn kho, top thú cưng bán chạy, doanh thu theo loại, hóa đơn gần đây, xuất báo cáo `.txt` |

Quy tắc nghiệp vụ tiêu biểu:

- Không bán vượt số lượng tồn kho; bán hết thì thú cưng tự chuyển sang trạng thái *Đã bán*.
- Điểm tích lũy: **1 điểm / 100.000 đ** giá trị hóa đơn.
- Chiết khấu theo hạng: Thường 0%, Bạc 3%, Vàng 7%, Kim cương 10%.
- Dữ liệu được kiểm tra chặt chẽ (số điện thoại 10 số, ngày hợp lệ, giá > 0, mã không trùng...).
- Không có thao tác nào ghi đè dữ liệu cũ khi thao tác bị hủy hoặc nhập sai.

## 2. Công nghệ

- **Java 17+** (đã kiểm thử với JDK 21), giao diện **console** (menu + bảng văn bản).
- Lưu trữ: tệp văn bản UTF-8, mỗi dòng là một bản ghi, có mã hóa ký tự phân cách; ghi tệp an toàn
  qua tệp tạm rồi đổi tên.
- Không dùng thư viện ngoài, không dùng build tool.

## 3. Cách chạy

Yêu cầu: đã cài JDK 17 trở lên và có lệnh `javac`, `java` trong PATH.

```bat
run.bat
```

Hoặc biên dịch thủ công:

```bat
mkdir out
dir /s /b src\*.java > out\sources.txt
javac -encoding UTF-8 -d out @out\sources.txt
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out petshop.app.Main
```

Kiểm tra nhanh phần nghiệp vụ (không cần nhập liệu):

```bat
run.bat --check
```

Kết quả mong đợi: in ra các bước nạp dữ liệu mẫu, lập hóa đơn, chặn bán vượt tồn kho,
đọc lại từ tệp và dòng `KIỂM TRA THÀNH CÔNG`.

> **Lưu ý về tiếng Việt trên Windows:** `run.bat` đã gọi `chcp 65001` và truyền các tham số
> `-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstdin.encoding=UTF-8`, đồng thời chương trình
> tự bọc `System.out` bằng UTF-8. Nếu tự chạy lệnh `java`, hãy mở cửa sổ cmd và chạy `chcp 65001`
> trước, nếu không dấu tiếng Việt có thể hiển thị sai.

### Mở bằng IDE

- **IntelliJ IDEA**: `File > Open` → chọn thư mục dự án → đánh dấu `src` là *Sources Root* → chạy lớp `petshop.app.Main`.
- **Eclipse**: `File > New > Java Project` → bỏ chọn *Use default location* và trỏ tới thư mục này → chạy `Main`.
- **VS Code**: cài *Extension Pack for Java*, mở thư mục dự án rồi bấm Run ở `Main.java`.

> Lần chạy đầu tiên, nếu thư mục `data/` đang trống, chương trình tự nạp dữ liệu mẫu
> (10 thú cưng, 4 khách hàng, 3 nhân viên) để tiện demo.

## 4. Bản đồ menu

```
MENU CHÍNH
  1. Quản lý thú cưng ── 1 Xem danh sách      4 Xóa
  │                      2 Thêm               5 Tìm kiếm / lọc theo loại
  │                      3 Cập nhật           6 Sắp xếp theo giá
  2. Quản lý khách hàng ─ 1 Xem   2 Thêm   3 Cập nhật   4 Xóa   5 Tìm   6 Sắp xếp theo điểm
  3. Quản lý nhân viên ── 1 Xem   2 Thêm   3 Cập nhật   4 Xóa   5 Tìm   6 Tổng quỹ lương
  4. Bán hàng ── chọn khách hàng → chọn nhân viên → thêm thú cưng vào giỏ
  │              → xem tạm tính / chiết khấu → xác nhận → in hóa đơn
  5. Thống kê - báo cáo ─ 1 Tổng quan   2 Bán chạy   3 Doanh thu theo loại
  │                       4 Hóa đơn gần đây   5 Xuất báo cáo .txt
  6. Lưu dữ liệu
  0. Thoát (tự động lưu trước khi thoát)
```

Trong các câu hỏi cập nhật, **để trống = giữ nguyên giá trị cũ**. Câu hỏi đúng/sai nhận `c` (có)
hoặc `k` (không). Nhấn `Ctrl+C` để thoát ngay, `Ctrl+Z` rồi `Enter` để kết thúc nhập an toàn.

## 5. Cấu trúc thư mục

```
QuanLyCuaHangBanThuCung/
├── run.bat                     # biên dịch + chạy
├── README.md
├── src/petshop/
│   ├── app/                    # Main, dữ liệu mẫu, kiểm tra nhanh (--check)
│   ├── console/                # menu console: MenuChinh, MenuThuCung, MenuBanHang, NhapXuat...
│   ├── model/                  # thực thể: Nguoi, KhachHang, NhanVien, ThuCung, Cho, Meo, Chim, HoaDon...
│   ├── service/                # nghiệp vụ: QuanLy<T>, QuanLyCoBan<T>, 4 lớp quản lý, CuaHang
│   ├── io/                     # lưu tệp: TextCodec, TextFileRepository, các *Codec
│   ├── util/                   # Validator, Tien, TrangTri, HoaDonText
│   └── exception/              # AppException và các lớp con
├── data/                       # sinh ra khi chạy: thucung, khachhang, nhanvien, hoadon (.txt)
└── reports/                    # báo cáo thống kê xuất ra (.txt)
```

Luồng một phiên làm việc:

```
Main → nạp dữ liệu (data/) → MenuChinh
     → menu con (thú cưng / khách hàng / nhân viên / bán hàng / thống kê)
     → mỗi thao tác thay đổi dữ liệu đều gọi *QuanLy.luu() ghi ngay xuống tệp
     → thoát: CuaHang.luuTatCa() lần cuối
```

## 6. Thiết kế hướng đối tượng

```
            CoMaKhoa (interface)
             ▲            ▲
        Nguoi (abstract)  ThuCung (abstract)      HoaDon
        ▲          ▲      ▲       ▲      ▲
   KhachHang   NhanVien  Cho     Meo    Chim

QuanLy<T> (interface) ─── QuanLyCoBan<T> (abstract)
                              ▲        ▲        ▲
                    QuanLyThuCung  QuanLyKhachHang  QuanLyNhanVien, QuanLyHoaDon

TextCodec<T> (interface) ── TextFileRepository<T> ── ThuCungCodec, KhachHangCodec, ...
CuaHang ── điều phối 4 danh mục + nghiệp vụ thanh toán
MenuChinh ── MenuThuCung, MenuKhachHang, MenuNhanVien, MenuBanHang, MenuThongKe
```

Các kỹ thuật OOP được sử dụng:

- **Trừu tượng hóa**: `Nguoi`, `ThuCung`, `QuanLyCoBan` định nghĩa phần chung, lớp con cài đặt phần riêng.
- **Kế thừa**: `Cho`, `Meo`, `Chim` kế thừa `ThuCung`; `KhachHang`, `NhanVien` kế thừa `Nguoi`.
- **Đa hình**: `getDacDiem()`, `getChiPhiChamSoc()`, `getLoai()` được gọi qua tham chiếu `ThuCung`;
  `quanLy.them(...)` chạy trên nhiều kiểu danh mục khác nhau.
- **Đóng gói**: thuộc tính `private`, thay đổi qua phương thức có kiểm tra (`truKho`, `congDiem`...).
- **Interface & generic**: `QuanLy<T>`, `QuanLyCoBan<T extends CoMaKhoa>`, `TextCodec<T>`,
  `TextFileRepository<T>` dùng lại cho cả 4 loại dữ liệu.
- **Xử lý ngoại lệ**: hệ thống ngoại lệ riêng (`AppException` → `DuLieuKhongHopLeException`,
  `KhongTimThayException`, `NghiepVuException`), tầng console bắt và in thông điệp tiếng Việt thay vì
  để chương trình dừng.
- **Collection & Stream API**: `List`, `Map`, `Comparator`, lọc/tìm kiếm/thống kê bằng stream.
- **Bất biến**: `HoaDon` và `ChiTietHoaDon` không đổi sau khi tạo; hóa đơn lưu lại thông tin
  tại thời điểm bán nên dữ liệu gốc thay đổi vẫn không làm sai hóa đơn cũ.

## 7. Định dạng tệp dữ liệu

Các tệp nằm trong `data/`, UTF-8, dòng bắt đầu bằng `#` là chú thích. Ký tự `|`, `;`, `~`
trong nội dung được mã hóa để không lẫn với dấu phân cách.

```
# thucung.txt
Chó|TC001|Milu|2025-03-12|6500000|2|CON_BAN|Poodle|Nhỏ|true
# khachhang.txt
KH001|Nguyễn Văn An|NAM|1995-04-12|0901234567|12 Lê Lợi, Quận 1, TP.HCM|520
# nhanvien.txt
NV001|Trần Quốc Bảo|NAM|1990-02-08|0903111222|5 Pasteur, Quận 3|Quản lý|15000000|2020-05-02
# hoadon.txt
HD001|2026-10-21T09:30:15|KH001|Nguyễn Văn An|KIM_CUONG|NV001|Trần Quốc Bảo|TC001~Milu~Chó~1~6500000
```

Các trường được sắp theo thứ tự cột cố định của từng loại; dòng hóa đơn lưu kèm tên khách hàng,
tên nhân viên và tên thú cưng tại thời điểm bán để hóa đơn cũ không bị sai lệch.

## 8. Ghi chú khi báo cáo / bảo vệ

- Điểm nhấn nên trình bày: sơ đồ lớp ở mục 6, luồng `Bán hàng → CuaHang.thanhToan → trừ kho + cộng điểm + ghi tệp`,
  và việc dùng generic để tái sử dụng tầng lưu trữ cho 4 loại dữ liệu.
- Có thể xóa thư mục `data/` để chương trình nạp lại dữ liệu mẫu từ đầu.
- Ứng dụng chưa có đăng nhập/phân quyền — phù hợp phạm vi đồ án môn học.
