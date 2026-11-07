# Quản Lý Cửa Hàng Bán Thú Cưng

Bài tập lớn môn **Lập trình hướng đối tượng** — ứng dụng Java **chạy trên console** (menu dòng lệnh)
quản lý cửa hàng bán thú cưng, lưu dữ liệu bằng tệp văn bản `.txt` (UTF-8).
Không dùng giao diện đồ họa, không cần cài đặt cơ sở dữ liệu, không dùng Maven/Gradle.

> **Mục lục**: [1. Chức năng](#1-chức-năng-chính) · [2. Công nghệ](#2-công-nghệ) · [3. Cách chạy](#3-cách-chạy) · [4. Bản đồ menu](#4-bản-đồ-menu) · [5. Cấu trúc thư mục](#5-cấu-trúc-thư-mục) · [6. Thiết kế OOP](#6-thiết-kế-hướng-đối-tượng) · [7. Cách hoạt động chi tiết](#7-cách-hoạt-động-chi-tiết) · [8. Định dạng tệp](#8-định-dạng-tệp-dữ-liệu) · [9. Ghi chú bảo vệ](#9-ghi-chú-khi-báo-cáo--bảo-vệ) · [10. VS Code](#10-chạy-trong-vs-code-tránh-lỗi-fonttiếng-việt)

## 1. Chức năng chính

| Nhóm chức năng | Mô tả |
| --- | --- |
| Quản lý thú cưng | Xem, thêm, cập nhật, xóa, tìm kiếm / lọc theo loại, sắp xếp theo giá. Mỗi loài (Chó, Mèo, Chim) có thuộc tính riêng; theo dõi tồn kho và trạng thái (còn bán / đã bán / cách ly) |
| Quản lý sản phẩm | Xem, thêm, cập nhật, xóa, tìm kiếm / lọc theo loại (thức ăn, thuốc, phụ kiện, đồ chơi, quần áo), sắp xếp theo giá. Mỗi loại có thuộc tính riêng: khối lượng + hạn dùng, công dụng + kê đơn, chất liệu + màu sắc, loại đồ chơi + kích thước, kích cỡ + chất liệu |
| Quản lý dịch vụ | Xem, thêm, cập nhật, xóa, tìm kiếm / lọc theo loại (tắm & sấy, cắt tỉa, tiêm phòng), sắp xếp theo giá, quản lý thời gian thực hiện |
| Quản lý khách hàng | Hồ sơ khách hàng, điểm tích lũy, tự động xếp hạng (Thường, Bạc, Vàng, Kim cương) và mức chiết khấu tương ứng; chống trùng số điện thoại |
| Quản lý nhân viên | Hồ sơ, chức vụ, lương, ngày vào làm; xem tổng quỹ lương |
| Bán hàng | Chọn khách hàng + nhân viên, thêm **thú cưng / sản phẩm / dịch vụ** vào giỏ, kiểm tra tồn kho, tính chiết khấu theo hạng, thanh toán và in hóa đơn |
| Quản lý hóa đơn | Xem danh sách, xem chi tiết, tạo mới (bán hàng), cập nhật (đổi nhân viên lập), hủy hóa đơn (hoàn kho + trừ điểm khách), tìm kiếm theo mã / khách hàng / ngày |
| Thống kê | Doanh thu, số hóa đơn, số mặt hàng đã bán, giá trị tồn kho, top hàng bán chạy, doanh thu theo loại hàng, hóa đơn gần đây, xuất báo cáo `.txt` |

Quy tắc nghiệp vụ tiêu biểu:

- Không bán vượt số lượng tồn kho; thú cưng bán hết tự chuyển trạng thái *Đã bán*.
- Dịch vụ không quản lý tồn kho (bán bao nhiêu lượt cũng được).
- Điểm tích lũy: **1 điểm / 100.000 đ** giá trị hóa đơn.
- Chiết khấu theo hạng: Thường 0%, Bạc 3%, Vàng 7%, Kim cương 10%.
- Hủy hóa đơn sẽ hoàn hàng về kho và trừ lại đúng số điểm đã cộng.
- Dữ liệu được kiểm tra chặt chẽ (số điện thoại 10 số, ngày hợp lệ, giá > 0, mã không trùng...).
- Không có thao tác nào ghi đè dữ liệu cũ khi thao tác bị hủy hoặc nhập sai.

## 2. Công nghệ

- **Java 11+** (đã kiểm thử với **JDK 21** trên máy chạy đồ án), giao diện **console** (menu + bảng văn bản).
- Lưu trữ: tệp văn bản UTF-8, mỗi dòng là một bản ghi, có mã hóa ký tự phân cách; ghi tệp an toàn
  qua tệp tạm rồi đổi tên.
- Chỉ dùng `ArrayList`, vòng lặp `for`, `Collections.sort`, `switch - case`; không dùng generic tự viết,
  Stream API, lambda, `var`, `Optional`, switch expression hay pattern matching.
- Không dùng thư viện ngoài, không dùng build tool.

## 3. Cách chạy

Yêu cầu: đã cài **JDK 11 trở lên** và có lệnh `javac`, `java` trong PATH. Project được chạy và kiểm thử bằng **JDK 21**.

```bat
run.bat
```

Hoặc biên dịch thủ công:

```bat
mkdir out
dir /s /b src\*.java > out\sources.txt
javac -encoding UTF-8 -d out @out\sources.txt
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Dstdin.encoding=UTF-8 -cp out main.Main
```

Kiểm tra nhanh phần nghiệp vụ (không cần nhập liệu):

```bat
run.bat --check
```

Kết quả mong đợi: in ra 7 bước (nạp dữ liệu mẫu, bán hỗn hợp thú cưng + sản phẩm + dịch vụ,
kiểm tra tồn kho và điểm, chặn bán vượt kho, hủy hóa đơn hoàn kho, đọc lại tệp, thuộc tính static)
và dòng `KIỂM TRA THÀNH CÔNG`.

> **Lưu ý về tiếng Việt trên Windows:** `run.bat` đã gọi `chcp 65001` và truyền các tham số
> `-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Dstdin.encoding=UTF-8`, đồng thời chương trình
> tự bọc `System.out` bằng UTF-8. Nếu tự chạy lệnh `java`, hãy mở cửa sổ cmd và chạy `chcp 65001`
> trước, nếu không dấu tiếng Việt có thể hiển thị sai.

### Mở bằng IDE

- **IntelliJ IDEA**: `File > Open` → chọn thư mục dự án → đánh dấu `src` là *Sources Root* → chạy lớp `main.Main`.
- **Eclipse**: `File > New > Java Project` → bỏ chọn *Use default location* và trỏ tới thư mục này → chạy `Main`.
- **VS Code**: cài *Extension Pack for Java*, mở thư mục dự án rồi bấm Run ở `Main.java`.

> Lần chạy đầu tiên, nếu thư mục `data/` đang trống, chương trình tự nạp dữ liệu mẫu
> (10 thú cưng, 8 sản phẩm, 4 dịch vụ, 4 khách hàng, 3 nhân viên) để tiện demo.

## 4. Bản đồ menu

```
MENU CHÍNH
  1. Quản lý thú cưng ── 1 Xem danh sách      4 Xóa
  │                      2 Thêm               5 Tìm kiếm / lọc theo loại
  │                      3 Cập nhật           6 Sắp xếp theo giá
  2. Quản lý sản phẩm ── 1 Xem   2 Thêm   3 Cập nhật   4 Xóa   5 Tìm   6 Sắp xếp theo giá
  3. Quản lý dịch vụ ─── 1 Xem   2 Thêm   3 Cập nhật   4 Xóa   5 Tìm   6 Sắp xếp theo giá
  4. Quản lý khách hàng  1 Xem   2 Thêm   3 Cập nhật   4 Xóa   5 Tìm   6 Sắp xếp theo điểm
  5. Quản lý nhân viên ─ 1 Xem   2 Thêm   3 Cập nhật   4 Xóa   5 Tìm   6 Tổng quỹ lương
  6. Bán hàng ── chọn khách hàng → chọn nhân viên → nhập mã TC/SP/DV + số lượng
  │              → xem tạm tính / chiết khấu → xác nhận → in hóa đơn
  7. Quản lý hóa đơn ── 1 Xem danh sách     4 Cập nhật (đổi nhân viên)
  │                     2 Xem chi tiết      5 Hủy hóa đơn (hoàn kho)
  │                     3 Tạo mới (bán)     6 Tìm kiếm theo mã / khách / ngày
  8. Thống kê - báo cáo ─ 1 Tổng quan   2 Hàng bán chạy   3 Doanh thu theo loại
  │                        4 Hóa đơn gần đây   5 Xuất báo cáo .txt
  9. Lưu dữ liệu
  0. Thoát (tự động lưu trước khi thoát)
```

Trong các câu hỏi cập nhật, **để trống = giữ nguyên giá trị cũ**. Câu hỏi đúng/sai nhận `c` (có)
hoặc `k` (không). Nhấn `Ctrl+C` để thoát ngay, `Ctrl+Z` rồi `Enter` để kết thúc nhập an toàn.

## 5. Cấu trúc thư mục

```
QuanLyCuaHangBanThuCung/
├── run.bat                     # biên dịch + chạy
├── README.md
├── src/
│   ├── entity/                 # thực thể (19 lớp): Nguoi, KhachHang, NhanVien, ThuCung, Cho, Meo, Chim,
│   │                           #   SanPham, ThucAn, Thuoc, PhuKien, DoChoi, QuanAo, DichVu, DichVuTam,
│   │                           #   DichVuCatTia, DichVuTiemPhong, HoaDon, ChiTietHoaDon
│   │                           #   + 3 enum: GioiTinh, HangKhachHang, TrangThaiThuCung
│   ├── inter/                  # interface: CoMaKhoa, MatHang
│   ├── manager/                # 6 lớp quản lý dùng ArrayList + CuaHang điều phối
│   ├── menu/                   # màn hình console: MenuChinh + 8 menu con, NhapXuat, HoaDonText
│   ├── util/                   # DocGhiFile (đọc/ghi .txt), AppException, Validator, Tien,
│   │                           #   TrangTri
│   └── main/                   # Main, dữ liệu mẫu, kiểm tra nhanh (--check)
├── data/                       # sinh ra khi chạy: thucung, sanpham, dichvu,
│                               #   khachhang, nhanvien, hoadon (.txt)
└── reports/                    # báo cáo thống kê xuất ra (.txt)
```

Luồng một phiên làm việc:

```
Main → nạp dữ liệu (data/) → MenuChinh
     → menu con (thú cưng / sản phẩm / dịch vụ / khách hàng / nhân viên / bán hàng / hóa đơn / thống kê)
     → mỗi thao tác thay đổi dữ liệu đều gọi *QuanLy.luu() ghi ngay xuống tệp
     → thoát: CuaHang.luuTatCa() lần cuối
```

## 6. Thiết kế hướng đối tượng

### 6.1. Sơ đồ lớp

```
                        CoMaKhoa (interface)
                              ▲
                    ┌─────────┴──────────┐
              MatHang (interface) ──────────────┐
                    ▲            ▲              ▲
             ThuCung (abstract) SanPham (abstract) DichVu (abstract)
             ▲      ▲      ▲      ▲     ▲     ▲     ▲     ▲     ▲       ▲        ▲
           Cho    Meo    Chim  ThucAn Thuoc PhuKien DoChoi QuanAo DichVuTam DichVuCatTia DichVuTiemPhong

             Nguoi (abstract)
             ▲            ▲
        KhachHang     NhanVien

        HoaDon ◆── ChiTietHoaDon      (mỗi dòng lưu mã/tên/loại/đơn giá tại lúc bán)

QuanLyThuCung, QuanLySanPham, QuanLyDichVu,
QuanLyKhachHang, QuanLyNhanVien, QuanLyHoaDon
        │  mỗi lớp bọc một ArrayList và tự đọc/ghi tệp của mình
        ▼
     CuaHang ── điều phối 6 danh mục + nghiệp vụ bán hàng / hủy hóa đơn
        ▲
     MenuChinh ── 8 menu con (thú cưng, sản phẩm, dịch vụ, khách hàng, nhân viên,
                  bán hàng, hóa đơn, thống kê)

DocGhiFile ── đọc/ghi tệp text trực tiếp (doc, ghi, maHoa, giaiMa)
```

#### Quan hệ giữa các lớp

| Kiểu quan hệ | Cặp lớp | Ý nghĩa |
| --- | --- | --- |
| Kế thừa | `Nguoi` ← `KhachHang`, `NhanVien` | Dùng lại họ tên, giới tính, ngày sinh, SĐT, địa chỉ |
| Kế thừa | `ThuCung` ← `Cho`, `Meo`, `Chim` | Dùng lại mã, tên, giá, tồn kho, trạng thái |
| Kế thừa | `SanPham` ← `ThucAn`, `Thuoc`, `PhuKien`, `DoChoi`, `QuanAo` | Dùng lại mã, tên, giá, tồn kho |
| Kế thừa | `DichVu` ← `DichVuTam`, `DichVuCatTia`, `DichVuTiemPhong` | Dùng lại mã, tên, giá, thời gian |
| Thực hiện interface | `MatHang` ← `ThuCung`, `SanPham`, `DichVu` | Ba nhánh đều “bán được” theo cùng hợp đồng |
| Thực hiện interface | `CoMaKhoa` ← `MatHang`, `Nguoi` | Mọi đối tượng đều có mã |
| Kết hợp (has-a) | `CuaHang` chứa 6 `QuanLy*` | Một cửa hàng có 6 danh mục |
| Kết hợp (has-a) | `QuanLy*` chứa `ArrayList` | Mỗi danh mục giữ danh sách đối tượng |
| Kết hợp (has-a) | `HoaDon` chứa `List<ChiTietHoaDon>` | Một hóa đơn có nhiều dòng hàng |
| Phụ thuộc | `ChiTietHoaDon` dùng `MatHang` | Chụp thông tin mặt hàng tại lúc bán |
| Phụ thuộc | `Menu*` dùng `CuaHang` | Giao diện gọi nghiệp vụ |
| Phụ thuộc | `QuanLy*`, entity dùng `DocGhiFile` | Chuyển đối tượng thành dòng text và ngược lại |

### 6.2. Các kỹ thuật OOP được sử dụng

- **19 lớp đối tượng** trong gói `entity`:
  `Nguoi`, `KhachHang`, `NhanVien`, `ThuCung`, `Cho`, `Meo`, `Chim`,
  `SanPham`, `ThucAn`, `Thuoc`, `PhuKien`, `DoChoi`, `QuanAo`, `DichVu`, `DichVuTam`,
  `DichVuCatTia`, `DichVuTiemPhong`, `HoaDon`, `ChiTietHoaDon` (chưa kể 3 enum và 2 interface).
- **Hàm thiết lập (constructor)**: mọi lớp đều có constructor; lớp con gọi `super(...)`;
  `ChiTietHoaDon` có thêm factory `tuMatHang(MatHang, int)`.
- **Trừu tượng hóa**: `Nguoi`, `ThuCung`, `SanPham`, `DichVu` là lớp trừu tượng,
  có phương thức trừu tượng (`getLoaiNguoi`, `getLoai`, `getDacDiem`, `getChiPhiChamSoc`...).
- **Kế thừa**: `Cho`, `Meo`, `Chim` kế thừa `ThuCung`; `KhachHang`, `NhanVien` kế thừa `Nguoi`;
  `ThucAn`, `Thuoc`, `PhuKien`, `DoChoi`, `QuanAo` kế thừa `SanPham`; 3 lớp dịch vụ kế thừa `DichVu`.
- **Đa hình**: cùng gọi `getLoai()`, `getDacDiem()`, `truKho()`, `hoanKho()` qua tham chiếu
  `MatHang` / `ThuCung`; giỏ hàng và hóa đơn chỉ làm việc với interface `MatHang` nên bán được
  lẫn lộn thú cưng, sản phẩm và dịch vụ.
- **Interface**: `CoMaKhoa` (đối tượng có mã khóa) và `MatHang` (mặt hàng bán được, có
  `getMa`, `getTen`, `getGiaBan`, `getLoai`, `quanLyTonKho`, `truKho`, `hoanKho`).
- **Thuộc tính static + phương thức static**:
  - `ThuCung.soLuongDaTao` + `ThuCung.getSoLuongDaTao()` đếm số đối tượng thú cưng đã tạo trong phiên
    (hiển thị ở đầu menu Quản lý thú cưng).
  - `SanPham.soLuongDaTao` + `SanPham.getSoLuongDaTao()`.
  - `HoaDon.TIEN_MOI_DIEM` (hằng số dùng chung cho quy tắc tích điểm).
- **Lớp danh sách đối tượng**: 6 lớp `QuanLyThuCung`, `QuanLySanPham`, `QuanLyDichVu`,
  `QuanLyKhachHang`, `QuanLyNhanVien`, `QuanLyHoaDon` — mỗi lớp bọc một `ArrayList`,
  cung cấp `them`, `capNhat`, `xoa`, `tim`, `tatCa`, `sinhMa`, `luu`, `napDuLieu`; vòng lặp `for` được dùng
  để tìm kiếm, lọc và `Collections.sort` để sắp xếp.
- **Đọc/ghi tệp text**: `DocGhiFile` cung cấp `doc`, `ghi`, `tach`, `maHoa`, `giaiMa`;
  mỗi entity tự chuyển mình thành dòng text bằng `toFileString()` và đọc lại bằng `fromFileString()`.
  Ghi tệp qua tệp tạm rồi đổi tên để tránh mất dữ liệu.
- **Đóng gói**: thuộc tính `private`, thay đổi qua phương thức có kiểm tra (`truKho`, `congDiem`, `truDiem`...).
- **Xử lý ngoại lệ**: `AppException` → `AppException.DuLieuKhongHopLe`, `AppException.KhongTimThay`,
  `AppException.NghiepVu`; tầng console bắt và in thông điệp tiếng Việt thay vì để chương trình dừng.
- **Collection cơ bản**: `ArrayList` cho danh mục, `LinkedHashMap` để gộp dòng trùng khi thống kê,
  `Collections.sort` với `Comparator` để sắp xếp; không dùng Stream API hay lambda.
- **Điều khiển luồng cổ điển**: toàn bộ menu dùng `switch - case` với `break`, vòng lặp `for`,
  `while`; không dùng switch expression hay pattern matching.
- **Bất biến**: `HoaDon` và `ChiTietHoaDon` không đổi sau khi tạo; hóa đơn lưu lại thông tin
  tại thời điểm bán nên dữ liệu gốc thay đổi vẫn không làm sai hóa đơn cũ.
  "Sửa hóa đơn" = tạo hóa đơn mới thay thế với nhân viên lập khác; "xóa" = hủy hóa đơn.

### 6.3. Đối chiếu yêu cầu của đề (bảng chi tiết)

Bảng dưới đây là “bảng bảo vệ”: mỗi yêu cầu của đề đều chỉ rõ **minh chứng nằm ở lớp/phương thức nào**, **cách demo trên console** và **câu trả lời ngắn** khi giáo viên hỏi.

| # | Yêu cầu của đề | Minh chứng trong bài | Cách demo nhanh | Gợi ý trả lời bảo vệ |
| --- | --- | --- | --- | --- |
| 1 | Có ít nhất 15 lớp đối tượng | 19 lớp trong gói `entity`: 15 lớp concrete (`KhachHang`, `NhanVien`, `Cho`, `Meo`, `Chim`, `ThucAn`, `Thuoc`, `PhuKien`, `DoChoi`, `QuanAo`, `DichVuTam`, `DichVuCatTia`, `DichVuTiemPhong`, `HoaDon`, `ChiTietHoaDon`) + 4 lớp abstract (`Nguoi`, `ThuCung`, `SanPham`, `DichVu`). Chưa kể 3 enum và 2 interface | Mở thư mục `src/entity/` hoặc chạy `dir src\entity` | “Nếu chỉ đếm lớp concrete thì có 15; nếu tính cả abstract thì 19. Abstract vẫn là lớp và có vai trò thật trong thiết kế.” |
| 2 | Có hàm thiết lập (constructor) | Mọi lớp đều có constructor; lớp con gọi `super(...)`; `ChiTietHoaDon` có thêm factory `tuMatHang(MatHang, int)` | Chạy `--check` hoặc thêm mới một thú cưng | “Constructor lớp con nhận đủ tham số, gọi `super` để khởi tạo phần chung, sau đó gán phần riêng.” |
| 3 | Kế thừa hợp lý | 4 nhánh: `Nguoi` → `KhachHang`, `NhanVien`; `ThuCung` → `Cho`, `Meo`, `Chim`; `SanPham` → `ThucAn`, `Thuoc`, `PhuKien`, `DoChoi`, `QuanAo`; `DichVu` → `DichVuTam`, `DichVuCatTia`, `DichVuTiemPhong` | Mở `src/entity/Cho.java`, chỉ dòng `extends ThuCung` | “Kế thừa để dùng lại mã, tên, giá, tồn kho và chỉ viết thêm thuộc tính đặc thù.” |
| 4 | Lớp trừu tượng + phương thức trừu tượng | `Nguoi` (`getLoaiNguoi`), `ThuCung` (`getLoai`, `getDacDiem`, `getChiPhiChamSoc`), `SanPham` (`getLoai`, `getDacDiem`), `DichVu` (`getLoai`, `getDacDiem`) | Thử `new ThuCung(...)` sẽ không biên dịch được | “Lớp abstract không thể khởi tạo trực tiếp; nó buộc lớp con cung cấp hành vi riêng.” |
| 5 | Đa hình | `CuaHang` và `HoaDon` làm việc với `MatHang`; `ChiTietHoaDon.tuMatHang(MatHang, int)`; `truKho`/`hoanKho`/`quanLyTonKho` được gọi qua interface | Bán lẫn thú cưng + sản phẩm + dịch vụ trong một hóa đơn | “Cùng một lời gọi `matHang.truKho(...)` nhưng `ThuCung` đổi trạng thái, `SanPham` trừ số lượng, `DichVu` không làm gì.” |
| 6 | Interface | `CoMaKhoa` (`getMa`) và `MatHang` (`getTen`, `getLoai`, `getGiaBan`, `getSoLuongTon`, `conBanDuoc`, `truKho`, `hoanKho`, `getDacDiem`, `quanLyTonKho`) | Mở `src/inter/MatHang.java` | “Interface là hợp đồng chung để giỏ hàng và hóa đơn không cần biết loại cụ thể.” |
| 7 | Đóng gói dữ liệu | Thuộc tính `private`/`private final`; thay đổi qua phương thức có kiểm tra: `truKho`, `hoanKho`, `congDiem`, `truDiem`, `setSoLuong`... | Sửa số lượng xuống âm sẽ bị chặn bởi `Math.max(0, ...)` hoặc `truKho` | “Không cho truy cập trực tiếp biến, mọi thay đổi đều đi qua phương thức để giữ bất biến.” |
| 8 | Thuộc tính static + phương thức static | `ThuCung.soLuongDaTao`/`getSoLuongDaTao()`, `SanPham.soLuongDaTao`/`getSoLuongDaTao()`, `HoaDon.TIEN_MOI_DIEM` | Menu Quản lý thú cưng hiện số đối tượng đã tạo; `--check` bước 7 | “Static thuộc về lớp, dùng chung cho mọi đối tượng; đếm số đối tượng đã tạo trong phiên chạy.” |
| 9 | Lớp mảng / danh sách đối tượng | 6 lớp `QuanLyThuCung`, `QuanLySanPham`, `QuanLyDichVu`, `QuanLyKhachHang`, `QuanLyNhanVien`, `QuanLyHoaDon`, mỗi lớp bọc `ArrayList` | Chọn chức năng 1 ở bất kỳ menu con | “Mỗi danh mục có một lớp quản lý riêng, không dùng mảng thô; thêm/xóa/tìm đều thao tác trên `ArrayList`.” |
| 10 | Tìm kiếm, lọc, sắp xếp | `timKiem(tuKhoa, loai)` ở các lớp quản lý; `Collections.sort` + `Comparator` trong `sapXepTheoGia`, `sapXepTheoDiem`, `moiNhatTruoc`, `topBanChay` | Menu con chọn 5 (tìm) hoặc 6 (sắp xếp) | “Tìm kiếm duyệt bằng vòng `for`, so khớp mã/tên/đặc điểm; sắp xếp bằng `Collections.sort` với `Comparator`, không dùng Stream.” |
| 11 | Đọc/ghi tệp văn bản | 6 tệp `.txt` UTF-8 trong `data/`; `DocGhiFile.doc`, `ghi`, `tach`, `maHoa`, `giaiMa`; mỗi entity có `toFileString`/`fromFileString` | Mở `data/sanpham.txt`, chạy `--check` bước 6 | “Ghi qua tệp tạm `.tmp` rồi đổi tên để tránh mất dữ liệu; ký tự phân cách được mã hóa.” |
| 12 | Xử lý ngoại lệ | `AppException` và 3 lớp con `DuLieuKhongHopLe`, `KhongTimThay`, `NghiepVu`; menu bắt và in `[LỖI]` | Nhập số điện thoại sai, bán vượt tồn kho | “Ngoại lệ được ném ở tầng nghiệp vụ, bắt ở tầng menu để chương trình không bị dừng.” |
| 13 | Đủ xem/thêm/sửa/xóa/tìm | 5 danh mục chính + hóa đơn đều có đủ 5 thao tác (hóa đơn: xem/tạo/sửa nhân viên/hủy/tìm) | Đi qua menu 1–7 | “Mỗi menu con đều có cùng bộ thao tác; riêng hóa đơn là chứng từ nên ‘xóa’ được hiểu là hủy và hoàn kho.” |
| 14 | Nghiệp vụ bán hàng, hóa đơn, thống kê | `CuaHang.thanhToan`, `huyHoaDon`, `capNhatNhanVien`; `QuanLyHoaDon`; `MenuThongKe`; `HoaDonText` | Chạy `--check`, sau đó bán thử và hủy | “Thanh toán kiểm tra tồn kho hai lượt, trừ kho, cộng điểm, lưu tệp; hủy hóa đơn làm ngược lại.” |
| 15 | Chỉ dùng kiến thức cơ bản, không dùng công nghệ ngoài | Toàn bộ dùng `ArrayList`, `for`, `Collections.sort`, `switch - case`; không có Stream API, lambda, `var`, `Optional`, generic tự viết | Có thể tìm thử `stream(`, `->`, `Optional`, `var ` trong `src/` | “Bài chỉ dùng kiến thức trên lớp để dễ đọc, dễ giải thích và dễ bảo vệ.” |

## 7. Cách hoạt động chi tiết

Phần này giải thích chương trình **chạy như thế nào**, **lớp nào gọi lớp nào**, và **dữ liệu đi qua những tầng ra sao**. Mục tiêu là đọc xong có thể tự tin chỉ vào từng đoạn code khi bảo vệ.

### 7.1. Kiến trúc 4 tầng và chiều phụ thuộc

Chương trình chia thành các gói theo trách nhiệm. Chiều phụ thuộc **một chiều**, không có vòng lặp phụ thuộc:

```
main  →  menu  →  manager  →  entity / inter  →  util
  │        │         │              │              │
  │        │         │              │              └─ đọc/ghi tệp, validate,
  │        │         │              │                 định dạng tiền, ngoại lệ
  │        │         │              └─ dữ liệu + quy tắc trên từng đối tượng
  │        │         └─ nghiệp vụ, ArrayList, đọc/ghi từng danh mục
  │        └─ nhập/xuất console, điều hướng menu
  └─ khởi động, nạp dữ liệu mẫu, kiểm tra nhanh
```

| Tầng | Gói | Thành phần chính | Trách nhiệm | Điều cấm kỵ |
| --- | --- | --- | --- | --- |
| Khởi động | `main` | `Main`, `DuLieuMau`, `KiemTraNhanh` | Tạo `CuaHang`, nạp dữ liệu mẫu, mở `MenuChinh`, chạy kiểm tra `--check` | Không chứa nghiệp vụ chi tiết |
| Giao diện | `menu` | `MenuChinh`, 8 menu con, `NhapXuat`, `HoaDonText` | Hỏi người dùng, gọi `CuaHang`, in bảng/in hóa đơn | Không tự đọc `data/*.txt`, không tự sửa `ArrayList` của manager |
| Nghiệp vụ | `manager` | `CuaHang` + 6 lớp `QuanLy*` | Quản lý danh sách, kiểm tra quy tắc, bán hàng, hủy hóa đơn, lưu tệp | Không in menu, không đọc bàn phím |
| Mô hình | `entity`, `inter` | 19 lớp entity + 3 enum + 2 interface | Giữ dữ liệu, tự tính toán phần của mình, chuyển thành dòng text | Không gọi `manager`/`menu` |
| Hạ tầng | `util` | `DocGhiFile`, `Validator`, `AppException`, `Tien`, `TrangTri` | Đọc/ghi tệp, kiểm tra dữ liệu, định dạng hiển thị, ngoại lệ | Không chứa quy tắc riêng của cửa hàng |

Hệ quả của cách chia này:

- Muốn đổi giao diện (console → JavaFX/Android) chỉ cần thay gói `menu`, giữ nguyên `manager` và `entity`.
- Muốn đổi cách lưu (text → CSV/JSON) chỉ cần sửa `DocGhiFile` và `toFileString`/`fromFileString`.
- Mỗi lớp có một lý do để thay đổi, không dồn mọi thứ vào `Main`.

#### Bảng trách nhiệm từng lớp

| Lớp | Vai trò chính | Phương thức/điểm đáng chú ý |
| --- | --- | --- |
| `CoMaKhoa` | Interface: mọi đối tượng có mã định danh | `getMa()` |
| `MatHang` | Interface: mọi thứ bán được | `getTen`, `getGiaBan`, `getLoai`, `conBanDuoc`, `quanLyTonKho`, `truKho`, `hoanKho`, `getDacDiem` |
| `Nguoi` | Lớp abstract cho con người | `hoTen`, `gioiTinh`, `ngaySinh`, `soDienThoai`, `diaChi`, `getTuoi()`, abstract `getLoaiNguoi()` |
| `KhachHang` | Khách hàng, điểm và hạng | `congDiem`, `truDiem`, `getHang()`, `getTiLeChietKhau()` (suy ra từ điểm) |
| `NhanVien` | Nhân viên, chức vụ, lương | `getChucVu`, `getLuong`, `getNgayVaoLam` |
| `ThuCung` | Lớp abstract cho thú cưng | `getSoLuong`, `conBanDuoc`, `truKho`, `hoanKho`, `getTuoiThang`, static `soLuongDaTao` |
| `Cho` | Thú cưng loài chó | `giong`, `kichThuoc`, `daTiemPhong`, `getChiPhiChamSoc()` theo kích cỡ |
| `Meo` | Thú cưng loài mèo | `giong`, `daiLong`, `daTrietSan`, chi phí theo độ dài lông |
| `Chim` | Thú cưng loài chim | `loaiChim`, `bietNoi`, `mauLong`, chi phí theo khả năng nói |
| `SanPham` | Lớp abstract cho sản phẩm | `getSoLuong`, `truKho`, `hoanKho`, static `soLuongDaTao` |
| `ThucAn` | Thức ăn | `loaiThuCung`, `khoiLuongKg`, `hanSuDung` |
| `Thuoc` | Thuốc | `congDung`, `hanSuDung`, `canKeDon` |
| `PhuKien` | Phụ kiện | `chatLieu`, `mauSac` |
| `DoChoi` | Đồ chơi | `loaiDoChoi`, `kichThuoc` |
| `QuanAo` | Quần áo | `kichCo`, `chatLieu` |
| `DichVu` | Lớp abstract cho dịch vụ | `thoiGianPhut`, `quanLyTonKho()` trả `false`, `truKho`/`hoanKho` không đổi dữ liệu |
| `DichVuTam` | Tắm & sấy | `kichThuoc` |
| `DichVuCatTia` | Cắt tỉa | `kieuLong` |
| `DichVuTiemPhong` | Tiêm phòng | `loaiVacXin` |
| `HoaDon` | Chứng từ bán hàng bất biến | `getTamTinh`, `getTienGiam`, `getTongTien`, `getDiemCong`, `toFileString`/`fromFileString` |
| `ChiTietHoaDon` | Một dòng hàng đã “chụp” tại lúc bán | `tuMatHang(MatHang, int)`, `getThanhTien`, `voiSoLuong(int)` |
| `QuanLyThuCung` | Danh mục thú cưng | `them`, `capNhat`, `xoa`, `tim`, `timKiem`, `sapXepTheoGia`, `napDuLieu`, `luu` |
| `QuanLySanPham` | Danh mục sản phẩm | Như trên, thêm `conBanDuoc`, `tongGiaTriTonKho` |
| `QuanLyDichVu` | Danh mục dịch vụ | `them`, `capNhat`, `xoa`, `tim`, `timKiem`, `sapXepTheoGia`, `napDuLieu`, `luu` |
| `QuanLyKhachHang` | Danh mục khách hàng | `timTheoDienThoai`, `sapXepTheoDiem`, `timKiem` |
| `QuanLyNhanVien` | Danh mục nhân viên | `timKiem`, `tongLuong` |
| `QuanLyHoaDon` | Danh mục hóa đơn + thống kê | `moiNhatTruoc`, `trongKhoang`, `tongDoanhThu`, `topBanChay`, `doanhThuTheoLoai` |
| `CuaHang` | Điều phối toàn cửa hàng | `timMatHang`, `thanhToan`, `capNhatNhanVien`, `huyHoaDon`, `napTatCa`, `luuTatCa` |
| `MenuChinh` | Menu gốc | Giữ 8 menu con, `switch - case` 0..9, `finally` lưu dữ liệu |
| `MenuThuCung`, `MenuSanPham`, `MenuDichVu`, `MenuKhachHang`, `MenuNhanVien` | Menu CRUD từng danh mục | Xem/thêm/sửa/xóa/tìm/sắp xếp |
| `MenuBanHang` | Menu bán hàng | Chọn khách/nhân viên, thêm giỏ, xác nhận, gọi `CuaHang.thanhToan` |
| `MenuHoaDon` | Menu hóa đơn | Xem, xem chi tiết, tạo mới, đổi nhân viên, hủy, tìm |
| `MenuThongKe` | Menu thống kê | 5 chức năng, xuất báo cáo `.txt` |
| `NhapXuat` | Nhập/xuất console | `doc`, `docSoNguyen`, `xacNhan`, `inBang`, `tamDung` |
| `HoaDonText` | Sinh văn bản hóa đơn/báo cáo | `tao(HoaDon)`, `taoBaoCao(...)` |
| `DocGhiFile` | Đọc/ghi tệp text | `doc`, `ghi`, `tach`, `maHoa`, `giaiMa` |
| `Validator` | Kiểm tra dữ liệu nhập | `chuoi`, `soNguyen`, `soTien`, `ngay`, `soDienThoai` |
| `AppException` | Ngoại lệ nghiệp vụ | 3 lớp con `DuLieuKhongHopLe`, `KhongTimThay`, `NghiepVu` |
| `Tien`, `TrangTri` | Định dạng hiển thị | Tiền Việt, ngày, phần trăm, bảng |
| `Main`, `DuLieuMau`, `KiemTraNhanh` | Khởi động, dữ liệu mẫu, kiểm tra | `main`, `napNeuTrong`, `chay` |

### 7.2. Luồng khởi động từ lúc bấm `run.bat`

```
run.bat
  → javac biên dịch toàn bộ src vào out/
  → java main.Main
      → Main.main()
          → bọc System.out bằng UTF-8
          → nếu có tham số --check: KiemTraNhanh.chay() rồi return
          → new CuaHang("data")
              → tạo 6 lớp quản lý, mỗi lớp nhận đường dẫn data/*.txt
              → napTatCa()
                  → QuanLyThuCung.napDuLieu()  → DocGhiFile.doc("data/thucung.txt")
                  → QuanLySanPham.napDuLieu()  → DocGhiFile.doc("data/sanpham.txt")
                  → ... (dịch vụ, khách hàng, nhân viên, hóa đơn)
          → DuLieuMau.napNeuTrong(cuaHang)
              → nếu CẢ 6 danh sách đều rỗng: thêm dữ liệu mẫu + luuTatCa()
              → nếu đã có dữ liệu: bỏ qua, không ghi đè
          → new MenuChinh(cuaHang).chay()
              → vòng lặp: hiện menu → đọc 0..9 → switch - case
          → finally: cuaHang.luuTatCa() lần cuối
```

Giải thích từng điểm:

1. **`CuaHang(String thuMucDuLieu)`** nhận thư mục dữ liệu thay vì hard-code `"data"`. Nhờ vậy `KiemTraNhanh` có thể truyền một thư mục tạm (`Files.createTempDirectory`) và không làm bẩn dữ liệu thật.
2. **`napTatCa()`** gọi `napDuLieu()` của cả 6 manager. Nếu tệp chưa tồn tại, `DocGhiFile.doc` trả về danh sách rỗng chứ không ném lỗi — đây là lý do lần chạy đầu vẫn khởi động bình thường.
3. **`DuLieuMau.napNeuTrong`** chỉ nạp mẫu khi **tất cả** danh sách đều rỗng, tránh trường hợp người dùng đã xóa sạch một danh mục rồi chương trình tự nạp lại.
4. **`MenuChinh`** giữ sẵn 8 menu con trong constructor, mỗi menu con nhận cùng một `CuaHang`. Vì `CuaHang` giữ 6 manager, mọi menu đều nhìn thấy cùng một dữ liệu.
5. **`finally`** trong `MenuChinh.chay()` luôn gọi `luuTatCa()` kể cả khi người dùng nhấn `Ctrl+Z`/`Ctrl+D`, nên không mất thay đổi đang có.

Riêng nhánh `--check`:

```
Main.main(args) thấy "--check"
  → KiemTraNhanh.chay()
      → tạo thư mục tạm
      → new CuaHang(thư mục tạm)
      → nạp dữ liệu mẫu
      → bán thử 3 loại hàng
      → thử bán vượt tồn kho (phải bị chặn)
      → hủy hóa đơn (phải hoàn kho + trả điểm)
      → tạo CuaHang mới đọc lại từ tệp
      → in 7 bước và "KIỂM TRA THÀNH CÔNG"
```

### 7.3. Một thao tác CRUD đi qua những lớp nào

Ví dụ **thêm thú cưng** (menu chính chọn 1, menu con chọn 2):

```
MenuChinh.chay()
  case 1 → MenuThuCung.chay()
      case 2 → MenuThuCung.them()
          → cuaHang.getThuCung().sinhMa()          // TC001 → TC011
          → MenuThuCung.nhapThongTin(ma, null)     // hỏi loại, tên, ngày sinh, giá, số lượng...
              → NhapXuat.chuoi / ngay / tien ...   // nhập có kiểm tra, hỏi lại khi sai
              → new Cho(...) / new Meo(...) / new Chim(...)
          → cuaHang.getThuCung().them(moi)          // kiểm tra trùng mã
          → cuaHang.getThuCung().luu()              // ghi NGAY xuống tệp
              → DocGhiFile.ghi("data/thucung.txt", cacDong)
```

Bảng đối chiếu 5 thao tác:

| Thao tác | Menu gọi | Manager xử lý | Ghi tệp | Điểm cần nói khi bảo vệ |
| --- | --- | --- | --- | --- |
| Xem danh sách | `xemDanhSach(...)` | `tatCa()` trả `ArrayList` | Không | Menu chỉ đọc, không sửa dữ liệu |
| Thêm | `them()` | `sinhMa()` + `them(moi)` kiểm tra trùng mã | `luu()` ngay | Mã tự sinh, không cho nhập tay để tránh trùng |
| Cập nhật | `capNhat()` | `tim(ma)` lấy bản cũ, tạo bản mới, `capNhat(moi)` thay tại vị trí | `luu()` ngay | “Để trống = giữ nguyên” nhờ `NhapXuat.chuoi(nhan, giaTriCu)` |
| Xóa | `xoa()` | `tim(ma)` rồi `remove(i)` | `luu()` ngay | Xóa theo mã, báo lỗi nếu không thấy |
| Tìm / sắp xếp | `timKiem()` / `sapXep()` | `timKiem(tuKhoa, loai)` / `Collections.sort` trên bản sao | Không | Không làm thay đổi thứ tự gốc của danh sách |

Điểm quan trọng: **mọi thao tác thay đổi đều lưu ngay** sau khi thành công. Nếu dữ liệu nhập sai, `Validator` hoặc constructor ném `AppException` trước khi `them/capNhat` được gọi, nên danh sách và tệp vẫn giữ nguyên. Menu bắt ngoại lệ này và in `[LỖI] ...` thay vì làm sập chương trình.

### 7.4. Luồng bán hàng chi tiết

Đây là luồng dài nhất và là phần dễ bị hỏi nhất.

#### Bước 1 — Chọn khách hàng và nhân viên

`MenuBanHang.chay()` kiểm tra trước:

- Phải có ít nhất 1 khách hàng và 1 nhân viên.
- Phải có ít nhất 1 mặt hàng đang bán (thú cưng còn bán, sản phẩm còn tồn, hoặc có dịch vụ).

Sau đó in bảng khách hàng, đọc mã, tìm bằng `cuaHang.getKhachHang().tim(ma)`. Làm tương tự với nhân viên. Nếu mã sai thì kết thúc, không tạo giỏ.

#### Bước 2 — Chọn mặt hàng (đa hình tại đây)

```
MenuBanHang
  → cuaHang.timMatHang(maHang)
      → thuCung.tim(ma)  → nếu thấy trả về ThuCung
      → sanPham.tim(ma)  → nếu thấy trả về SanPham
      → dichVu.tim(ma)   → nếu thấy trả về DichVu
      → không thấy: null
```

`timMatHang` trả về `MatHang` — interface chung. Nhờ vậy vòng lặp giỏ hàng không cần biết món đó là chó, thức ăn hay dịch vụ tắm. Nó chỉ gọi:

- `matHang.conBanDuoc()`
- `matHang.quanLyTonKho()`
- `matHang.getSoLuongTon()`
- `matHang.getGiaBan()`
- `ChiTietHoaDon.tuMatHang(matHang, soLuong)` — “chụp lại” mã, tên, loại, đơn giá tại thời điểm bán.

Khi thêm vào giỏ, `themVaoGio` kiểm tra mã đã có chưa:

- Có rồi → tạo `ChiTietHoaDon` mới bằng `voiSoLuong(...)` và thay vào vị trí cũ.
- Chưa có → thêm dòng mới.

Số lượng tối đa:

- Thú cưng/sản phẩm: `getSoLuongTon() - số đã có trong giỏ`.
- Dịch vụ: tối đa 99 (không quản lý tồn kho).

#### Bước 3 — Xem giỏ và xác nhận

`xemGioHang` in từng dòng, tính:

```
thành tiền   = đơn giá × số lượng
tạm tính     = tổng thành tiền
tỉ lệ giảm   = khachHang.getTiLeChietKhau()   // lấy từ hạng
giảm giá     = round(tạm tính × tỉ lệ giảm)
tổng cộng    = tạm tính − giảm giá
điểm cộng    = floor(tổng cộng / 100.000)
```

Nếu người dùng trả lời `k`, chương trình kết thúc và **không thay đổi gì**.

#### Bước 4 — `CuaHang.thanhToan` (phần quan trọng nhất)

```
CuaHang.thanhToan(maKhachHang, maNhanVien, gioHang)
  1. Giỏ rỗng? → ném NghiepVu
  2. Tìm khách hàng, nhân viên; không thấy → ném KhongTimThay
  3. Gộp số lượng theo mã bằng LinkedHashMap
     (vì giỏ có thể có nhiều dòng cùng một mặt hàng)
  4. Lượt kiểm tra 1 — chưa trừ kho:
     với từng mã:
       - mặt hàng phải tồn tại
       - phải conBanDuoc()
       - nếu quanLyTonKho() thì tổng số lượng ≤ tồn kho
  5. Tạo HoaDon mới:
       - mã do hoaDon.sinhMa()
       - ngày giờ hiện tại
       - chụp lại mã/tên khách, hạng khách, mã/tên nhân viên
       - sao chép danh sách chi tiết
     HoaDon tự tính tiền giảm, tổng tiền, điểm cộng
  6. Lượt kiểm tra 2 — trừ kho:
       với từng mã: batBuocTimMatHang(ma).truKho(tổng số lượng)
  7. khachHang.congDiem(hoaDon.getDiemCong())
  8. hoaDon.them(hoaDonMoi)
  9. luuTatCa()
 10. trả về hóa đơn vừa tạo
```

Vì sao phải kiểm tra **hai lượt**? Nếu vừa kiểm tra vừa trừ từng món, giỏ có thể bị trừ dở dang khi món sau không đủ hàng. Kiểm tra toàn bộ trước rồi mới trừ giúp dữ liệu nhất quán: hoặc cả hóa đơn thành công, hoặc không món nào bị trừ.

`truKho` là **đa hình**:

| Lớp | `quanLyTonKho()` | `truKho(n)` | `hoanKho(n)` |
| --- | --- | --- | --- |
| `ThuCung` | `true` | Trừ số lượng; về 0 thì chuyển trạng thái `DA_BAN` | Cộng lại; nếu đang `DA_BAN` thì về `CON_BAN` |
| `SanPham` | `true` | Trừ số lượng, không cho âm | Cộng lại |
| `DichVu` | `false` | Chỉ kiểm tra `n > 0`, không đổi dữ liệu | Chỉ kiểm tra `n > 0` |

#### Bước 5 — In hóa đơn

`HoaDonText.tao(hoaDon)` tạo chuỗi nhiều dòng: mã, ngày, khách, hạng, nhân viên, từng dòng hàng, tạm tính, giảm giá, tổng cộng. Menu in chuỗi này ra console.

### 7.5. Luồng cập nhật và hủy hóa đơn

**Cập nhật hóa đơn** (`CuaHang.capNhatNhanVien`):

```
tìm hóa đơn cũ → tìm nhân viên mới
→ new HoaDon(cùng mã, cùng ngày, cùng khách, cùng hạng, nhân viên mới, cùng chi tiết)
→ QuanLyHoaDon.capNhat(hoaDonMoi)   // thay tại vị trí cũ
→ luuTatCa()
```

`HoaDon` là **bất biến** (các trường `final`, danh sách chi tiết bọc `unmodifiableList`). Vì vậy “sửa” không thay đổi object cũ mà tạo object mới thay thế. Cách này giữ cho chứng từ cũ không bị sửa lén.

**Hủy hóa đơn** (`CuaHang.huyHoaDon`):

```
tìm hóa đơn
→ với từng ChiTietHoaDon:
     timMatHang(ct.getMaHang())
     nếu còn tìm thấy: matHang.hoanKho(ct.getSoLuong())
→ tìm khách hàng theo mã
     nếu còn: khachHang.truDiem(hoaDon.getDiemCong())
→ QuanLyHoaDon.xoa(ma)
→ luuTatCa()
```

Điểm đáng nói: `truDiem` dùng `Math.max(0, ...)` nên điểm không bao giờ âm. Nếu mặt hàng gốc đã bị xóa khỏi danh mục, chương trình bỏ qua việc hoàn kho cho dòng đó nhưng vẫn trả điểm và xóa hóa đơn — đây là tình huống biên có thể nêu khi bảo vệ.

### 7.6. Luồng thống kê và xuất báo cáo

`MenuThongKe` không tự tính toán; nó gọi các phương thức của `QuanLyHoaDon`:

| Chức năng | Phương thức | Cách tính |
| --- | --- | --- |
| Tổng quan | `soLuong`, `tongDoanhThu`, `tongTienGiam`, `tongSoMatHangDaBan` | Duyệt hóa đơn, cộng `getTongTien`, `getTienGiam`, `getTongSoLuong` |
| Hàng bán chạy | `topBanChay(10)` | `LinkedHashMap` đếm số lượng theo “tên (loại)”, `Collections.sort` giảm dần, lấy 10 |
| Doanh thu theo loại | `doanhThuTheoLoai()` | Mỗi hóa đơn tính `tỉ lệ sau giảm = tổng tiền / tạm tính`, nhân vào từng dòng để phân bổ chiết khấu |
| Hóa đơn gần đây | `moiNhatTruoc()` | Sao chép danh sách, `Collections.sort` theo ngày giảm dần |
| Lọc theo khoảng ngày (hàm hỗ trợ) | `trongKhoang(tuNgay, denNgay)` | So sánh `LocalDate` của `ngayLap`, cho phép một đầu `null`; hiện chưa gắn vào menu, dùng khi cần mở rộng báo cáo theo kỳ |

Xuất báo cáo:

```
MenuThongKe.xuatBaoCao()
  → tạo thư mục reports/
  → tên tệp: bao-cao-yyyyMMdd-HHmmss.txt
  → HoaDonText.taoBaoCao(...) tạo nội dung
  → Files.write(tep, noiDung.getBytes(UTF_8))
```

Báo cáo gồm: số hóa đơn, doanh thu, tiền giảm, số mặt hàng đã bán, giá trị tồn kho, doanh thu theo loại, top bán chạy.

### 7.7. Luồng đọc/ghi tệp và tính bền vững dữ liệu

```
QuanLy*.luu()
  → duyệt ArrayList
  → gọi entity.toFileString() cho từng đối tượng
  → DocGhiFile.ghi(duongDan, cacDong)
      → tạo thư mục nếu chưa có
      → thêm dòng chú thích "# ..."
      → ghi tất cả vào <ten>.txt.tmp
      → Files.move(.tmp → .txt, REPLACE_EXISTING)

QuanLy*.napDuLieu()
  → DocGhiFile.doc(duongDan)
      → Files.readAllLines(UTF_8)
      → bỏ dòng trống và dòng bắt đầu bằng '#'
  → với từng dòng: entity.fromFileString(dong)
  → lỗi ở dòng nào thì bọc thành AppException kèm tên tệp + số dòng
```

Vì sao ghi qua `.tmp`? Nếu chương trình bị tắt giữa lúc ghi, tệp `.tmp` có thể hỏng nhưng tệp `.txt` cũ vẫn còn nguyên. Đây là cách đơn giản để tránh mất dữ liệu mà không cần cơ sở dữ liệu.

Mã hóa ký tự phân cách trong `DocGhiFile`:

| Ký tự gốc | Được ghi thành | Lý do |
| --- | --- | --- |
| `\` | `\\` | Không nhầm với ký tự mở đầu mã hóa |
| `\|` | `\p` | `\|` là phân cách trường |
| `;` | `\s` | `;` là phân cách chi tiết hóa đơn |
| `~` | `\t` | `~` là phân cách trong một chi tiết |
| xuống dòng | `\n` | Mỗi bản ghi phải nằm trên một dòng |
| `\r` | `\r` | Tránh lỗi trên Windows |

Nhờ vậy tên sản phẩm có thể chứa `|`, `;`, `~` mà không làm hỏng cấu trúc tệp. `giaiMa` làm ngược lại khi đọc.

**Kiểm tra round-trip** (ghi rồi đọc lại) đã có trong `--check` bước 6: tạo `CuaHang` mới trên cùng thư mục tạm, đọc lại hóa đơn, doanh thu, sản phẩm, dịch vụ và tồn kho.

### 7.8. Bảng “ai gọi ai”

| Từ | Đến | Qua phương thức | Mục đích |
| --- | --- | --- | --- |
| `Main` | `CuaHang` | constructor (bên trong gọi `napTatCa()`) | Tạo và nạp 6 danh mục |
| `Main` | `DuLieuMau` | `napNeuTrong(CuaHang)` | Nạp mẫu khi dữ liệu trống |
| `Main` | `MenuChinh` | `new ... .chay()` | Mở giao diện |
| `MenuChinh` | 8 menu con | `chay()` | Điều hướng chức năng |
| `Menu*` | `CuaHang` | `getThuCung()`, `getSanPham()`... | Lấy manager tương ứng |
| `Menu*` | `QuanLy*` | `them`, `capNhat`, `xoa`, `tim`, `luu` | CRUD danh mục |
| `MenuBanHang` | `CuaHang` | `timMatHang`, `thanhToan` | Tìm mặt hàng và lập hóa đơn |
| `MenuHoaDon` | `CuaHang` | `capNhatNhanVien`, `huyHoaDon` | Sửa/hủy hóa đơn |
| `MenuThongKe` | `QuanLyHoaDon` | `tongDoanhThu`, `topBanChay`... | Lấy số liệu thống kê |
| `CuaHang` | 6 `QuanLy*` | `tim`, `sinhMa`, `them`, `capNhat`, `xoa`, `luu` | Điều phối nghiệp vụ |
| `CuaHang` | `MatHang` | `truKho`, `hoanKho`, `conBanDuoc` | Đa hình khi bán/hủy |
| `CuaHang` | `KhachHang` | `congDiem`, `truDiem` | Cập nhật điểm tích lũy |
| `QuanLy*` | entity | `toFileString`, `fromFileString` | Chuyển đối tượng ↔ dòng text |
| `QuanLy*` | `DocGhiFile` | `doc`, `ghi` | Đọc/ghi tệp |
| `HoaDon` | `ChiTietHoaDon` | `getThanhTien`, `getSoLuong` | Tính tiền và điểm |
| `ChiTietHoaDon` | `MatHang` | `tuMatHang(MatHang, int)` | Chụp thông tin mặt hàng |
| `MenuThongKe` | `HoaDonText` | `taoBaoCao(...)` | Tạo nội dung báo cáo |

### 7.9. Ví dụ xuyên suốt: bán một hóa đơn rồi hủy

Đây chính là kịch bản `run.bat --check` đang chạy, có thể dùng để demo:

**Dữ liệu ban đầu**

- Khách `KH001` Nguyễn Văn An có 520 điểm → hạng **Kim cương** → giảm 10%.
- Nhân viên `NV001` Trần Quốc Bảo.
- `TC001` Milu, giá 6.500.000 đ, tồn 2.
- `SP001` Hạt Royal Canin, giá 320.000 đ, tồn 20.
- `DV001` Tắm sấy chó nhỏ, giá 150.000 đ.

**Giỏ hàng**

| Mặt hàng | SL | Thành tiền |
| --- | ---: | ---: |
| Milu (Chó) | 1 | 6.500.000 đ |
| Hạt Royal Canin (Thức ăn) | 2 | 640.000 đ |
| Tắm sấy chó nhỏ (Tắm & sấy) | 1 | 150.000 đ |
| **Tạm tính** | | **7.290.000 đ** |

**Tính tiền**

```
Giảm giá = 10% × 7.290.000 = 729.000 đ
Tổng     = 7.290.000 − 729.000 = 6.561.000 đ
Điểm     = floor(6.561.000 / 100.000) = 65 điểm
```

**Sau khi thanh toán**

- Tồn Milu: 2 → 1.
- Tồn Hạt Royal Canin: 20 → 18.
- Điểm khách: 520 → 585.
- Hóa đơn `HD001` được thêm và cả 6 tệp được ghi lại.
- Sản phẩm/dịch vụ: sản phẩm bị trừ tồn kho như bình thường; riêng dịch vụ không quản lý tồn kho nên vẫn bán tiếp được.

**Thử bán vượt tồn kho**

Gọi thanh toán với Milu số lượng 999 → `CuaHang.thanhToan` phát hiện `999 > 1` và ném:

```
Không đủ hàng cho Milu (chỉ còn 1)
```

Không có dòng nào bị trừ, không có hóa đơn mới.

**Hủy hóa đơn `HD001`**

- Hoàn Milu: 1 → 2.
- Hoàn Hạt Royal Canin: 18 → 20.
- Trừ điểm: 585 → 520.
- Xóa `HD001` khỏi danh sách, số hóa đơn về 0.
- Ghi lại tệp.

Đây là bằng chứng rõ nhất cho tính đúng đắn của nghiệp vụ và cho việc đọc/ghi tệp.

### 7.10. Kịch bản demo gợi ý (khoảng 5 phút)

1. **Mở đầu**: chạy `run.bat --check` để chứng minh nghiệp vụ và đọc/ghi tệp hoạt động, không cần nhập tay.
2. **Kiến trúc**: mở sơ đồ lớp ở mục 6.1, nói 4 nhánh kế thừa và 2 interface.
3. **CRUD**: vào Quản lý thú cưng → thêm một con mới → xem danh sách → sửa giá → tìm theo tên → xóa.
4. **Đa hình**: vào Bán hàng, chọn 1 thú cưng + 1 sản phẩm + 1 dịch vụ trong cùng một hóa đơn. Chỉ vào `MatHang` trong code.
5. **Nghiệp vụ**: chạy `--check` để thấy thông báo chặn bán vượt tồn kho; sau đó trên menu hủy một hóa đơn để thấy hoàn kho + trả điểm.
6. **Lưu trữ**: mở `data/hoadon.txt` và `data/thucung.txt` để chỉ ra định dạng dòng, dấu phân cách và mã hóa.
7. **Kết luận**: nhấn mạnh không dùng Stream/lambda/generic tự viết; mọi thứ nằm trong kiến thức cơ bản của môn học.

## 8. Định dạng tệp dữ liệu

Các tệp nằm trong `data/`, UTF-8, dòng bắt đầu bằng `#` là chú thích. Ký tự `|`, `;`, `~`
trong nội dung được mã hóa để không lẫn với dấu phân cách.

```
# thucung.txt
Chó|TC001|Milu|2025-03-12|6500000|2|CON_BAN|Poodle|Nhỏ|true

# sanpham.txt
Thức ăn|SP001|Hạt Royal Canin|320000|20|Chó|2.00|2027-05-30
Thuốc|SP003|Thuốc xổ giun|120000|15|Tẩy giun cho chó mèo|2026-12-31|false
Phụ kiện|SP005|Dây dắt da|180000|12|Da bò|Nâu
Đồ chơi|SP007|Bóng cao su|60000|25|Bóng|Nhỏ
Quần áo|SP008|Áo hoodie mèo|150000|10|M|Cotton

# dichvu.txt
Tắm & sấy|DV001|Tắm sấy chó nhỏ|150000|45|Nhỏ
Cắt tỉa|DV003|Cắt tỉa tạo kiểu|200000|50|Tạo kiểu
Tiêm phòng|DV004|Tiêm phòng dại|180000|15|Vắc-xin dại

# khachhang.txt
KH001|Nguyễn Văn An|NAM|1995-04-12|0901234567|12 Lê Lợi, Quận 1, TP.HCM|520

# nhanvien.txt
NV001|Trần Quốc Bảo|NAM|1990-02-08|0903111222|5 Pasteur, Quận 3|Quản lý|15000000|2020-05-02

# hoadon.txt
HD001|2026-10-21T09:30:15|KH001|Nguyễn Văn An|KIM_CUONG|NV001|Trần Quốc Bảo|TC001~Milu~Chó~1~6500000
```

Các trường được sắp theo thứ tự cột cố định của từng loại. Dòng đầu mỗi tệp là chú thích `#`.
Mỗi entity tự chuyển thành dòng text bằng `toFileString()` và đọc lại bằng `fromFileString()`;
lớp quản lý chỉ gọi `DocGhiFile.doc(...)` / `DocGhiFile.ghi(...)`.

Mỗi dòng hóa đơn lưu kèm tên khách hàng, tên nhân viên (đã mã hóa) và danh sách chi tiết ngăn bởi `;`,
mỗi chi tiết có dạng `ma~ten~loai~soLuong~donGia`; nhờ vậy hóa đơn cũ không bị sai lệch khi dữ liệu gốc thay đổi.

## 9. Ghi chú khi báo cáo / bảo vệ

- **Điểm nhấn nên trình bày**: sơ đồ lớp ở mục 6, bảng trách nhiệm ở mục 7.1, bảng đối chiếu yêu cầu ở mục 6.3, luồng
  `Bán hàng → CuaHang.thanhToan → kiểm tra tồn kho → trừ kho + cộng điểm + ghi tệp`,
  luồng `Hủy hóa đơn → hoàn kho + trừ điểm`, và cách 6 lớp quản lý dùng `ArrayList` + `DocGhiFile`
  để lưu từng danh mục mà không cần generic phức tạp.
- **Có thể chỉ ra tính đa hình**: `CuaHang` và `HoaDon` chỉ làm việc với `MatHang`, nên bán lẫn lộn
  thú cưng, sản phẩm và dịch vụ; mỗi lớp con tự quyết định `truKho`/`hoanKho` và `quanLyTonKho`.
- **Có thể xóa thư mục `data/`** để chương trình nạp lại dữ liệu mẫu từ đầu.
- Ứng dụng chưa có đăng nhập/phân quyền — phù hợp phạm vi đồ án môn học.
- **Nếu bị hỏi “vì sao không dùng generic/Stream/lambda?”**: đề yêu cầu kiến thức cơ bản; cách viết
  hiện tại dùng `ArrayList`, vòng `for`, `Collections.sort` và `switch - case`, dễ đọc và dễ giải thích.
- **Nếu bị hỏi “vì sao hóa đơn không sửa được chi tiết?”**: hóa đơn là chứng từ tại thời điểm bán;
  sửa chi tiết sẽ làm sai lệch doanh thu và tồn kho. Vì vậy chỉ cho đổi nhân viên lập, còn muốn đổi
  hàng thì hủy hóa đơn rồi lập lại.
- **Nếu bị hỏi “vì sao lưu ngay sau mỗi thao tác?”**: để tránh mất dữ liệu nếu chương trình bị đóng
  đột ngột; `finally` vẫn lưu lần cuối khi thoát bình thường.
- **Nếu bị hỏi “dữ liệu có bị hỏng khi đang ghi không?”**: ghi ra `.tmp` rồi `Files.move` đè lên tệp
  chính, nên tệp cũ chỉ bị thay khi tệp tạm đã ghi xong.
- **Nếu bị hỏi “tại sao có 19 lớp mà không phải 15?”**: 15 lớp concrete là mức tối thiểu; 4 lớp abstract
  (`Nguoi`, `ThuCung`, `SanPham`, `DichVu`) giúp gom thuộc tính chung và tạo 4 nhánh kế thừa rõ ràng.

## 10. Chạy trong VS Code (tránh lỗi font/tiếng Việt)

Lỗi ký tự kiểu `Cß╗¼A HÀNG...` hoặc `Qu?n lý...` trong VS Code **không phải lỗi font** mà là
lệch bảng mã: chương trình ghi UTF-8 nhưng terminal lại đọc theo code page cũ (437/1252).

Cách chạy đúng:

1. Mở terminal tích hợp trong VS Code (Ctrl + dấu backtick) và chạy:
   ```
   run.bat
   ```
   hoặc
   ```
   cmd /c run.bat
   ```
   `run.bat` đã tự gọi `chcp 65001` và truyền các cờ `-Dfile.encoding`, `-Dstdout.encoding`,
   `-Dstderr.encoding`, `-Dstdin.encoding` cho Java.

2. Project đã có sẵn `.vscode/settings.json`:
   - Terminal mặc định là **Command Prompt (UTF-8)** (tự chạy `chcp 65001`).
   - Font terminal: `Cascadia Mono`, `Cascadia Code`, `Consolas`, `Courier New`.
   - Cấu hình `java.debug.settings.vmArgs` cho Java extension.

3. Nếu terminal đang mở từ trước, hãy đóng terminal và mở lại (hoặc
   `Terminal: Select Default Profile` → chọn **Command Prompt (UTF-8)**).

4. Có thể chạy nhanh bằng `Ctrl + Shift + B` (task **Chạy chương trình (run.bat)**)
   hoặc task **Kiểm tra nhanh (run.bat --check)**.

5. Không chạy trực tiếp `java -cp out main.Main` trong terminal đang ở code page 437/1252;
   nếu buộc phải chạy, gõ `chcp 65001` trước.
