# Quản Lý Cửa Hàng Bán Thú Cưng

Bài tập lớn môn **Lập trình hướng đối tượng** — ứng dụng Java **chạy trên console** (menu dòng lệnh)
quản lý cửa hàng bán thú cưng, lưu dữ liệu bằng tệp văn bản `.txt` (UTF-8).
Không dùng giao diện đồ họa, không cần cài đặt cơ sở dữ liệu, không dùng Maven/Gradle.

## 1. Chức năng chính

| Nhóm chức năng | Mô tả |
| --- | --- |
| Quản lý thú cưng | Xem, thêm, cập nhật, xóa, tìm kiếm / lọc theo loại, sắp xếp theo giá. Mỗi loài (Chó, Mèo, Chim) có thuộc tính riêng; theo dõi tồn kho và trạng thái (còn bán / đã bán / cách ly) |
| Quản lý sản phẩm | Xem, thêm, cập nhật, xóa, tìm kiếm / lọc theo loại (thức ăn, thuốc, phụ kiện), sắp xếp theo giá. Mỗi loại có thuộc tính riêng: khối lượng + hạn dùng, công dụng + kê đơn, chất liệu + màu sắc |
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

Kết quả mong đợi: in ra 7 bước (nạp dữ liệu mẫu, bán hỗn hợp thú cưng + sản phẩm + dịch vụ,
kiểm tra tồn kho và điểm, chặn bán vượt kho, hủy hóa đơn hoàn kho, đọc lại tệp, thuộc tính static)
và dòng `KIỂM TRA THÀNH CÔNG`.

> **Lưu ý về tiếng Việt trên Windows:** `run.bat` đã gọi `chcp 65001` và truyền các tham số
> `-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstdin.encoding=UTF-8`, đồng thời chương trình
> tự bọc `System.out` bằng UTF-8. Nếu tự chạy lệnh `java`, hãy mở cửa sổ cmd và chạy `chcp 65001`
> trước, nếu không dấu tiếng Việt có thể hiển thị sai.

### Mở bằng IDE

- **IntelliJ IDEA**: `File > Open` → chọn thư mục dự án → đánh dấu `src` là *Sources Root* → chạy lớp `petshop.app.Main`.
- **Eclipse**: `File > New > Java Project` → bỏ chọn *Use default location* và trỏ tới thư mục này → chạy `Main`.
- **VS Code**: cài *Extension Pack for Java*, mở thư mục dự án rồi bấm Run ở `Main.java`.

> Lần chạy đầu tiên, nếu thư mục `data/` đang trống, chương trình tự nạp dữ liệu mẫu
> (10 thú cưng, 6 sản phẩm, 4 dịch vụ, 4 khách hàng, 3 nhân viên) để tiện demo.

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
├── src/petshop/
│   ├── app/                    # Main, dữ liệu mẫu, kiểm tra nhanh (--check)
│   ├── console/                # menu console: MenuChinh, MenuThuCung, MenuSanPham,
│   │                           #   MenuDichVu, MenuHoaDon, MenuBanHang, NhapXuat...
│   ├── model/                  # thực thể: Nguoi, KhachHang, NhanVien, ThuCung, Cho, Meo, Chim,
│   │                           #   SanPham, ThucAn, Thuoc, PhuKien, DichVu, DichVuTam,
│   │                           #   DichVuCatTia, DichVuTiemPhong, HoaDon, ChiTietHoaDon
│   ├── service/                # nghiệp vụ: QuanLy<T>, QuanLyCoBan<T>, 6 lớp quản lý, CuaHang
│   ├── io/                     # lưu tệp: TextCodec, TextFileRepository, 6 *Codec, Esc
│   ├── util/                   # Validator, Tien, TrangTri, HoaDonText
│   └── exception/              # AppException và các lớp con
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
             ▲      ▲      ▲      ▲     ▲     ▲     ▲       ▲        ▲
           Cho    Meo    Chim  ThucAn Thuoc PhuKien DichVuTam DichVuCatTia DichVuTiemPhong

             Nguoi (abstract)
             ▲            ▲
        KhachHang     NhanVien

        HoaDon ◆── ChiTietHoaDon      (mỗi dòng lưu mã/tên/loại/đơn giá tại lúc bán)

QuanLy<T> (interface) ─── QuanLyCoBan<T> (abstract)
                              ▲        ▲         ▲        ▲         ▲
                     QuanLyThuCung QuanLySanPham QuanLyDichVu QuanLyKhachHang QuanLyNhanVien (+ QuanLyHoaDon)

TextCodec<T> (interface) ── TextFileRepository<T> ── 6 lớp *Codec
CuaHang ── điều phối 6 danh mục + nghiệp vụ bán hàng / hủy hóa đơn
MenuChinh ── 8 menu con (thú cưng, sản phẩm, dịch vụ, khách hàng, nhân viên,
             bán hàng, hóa đơn, thống kê)
```

### 6.2. Các kỹ thuật OOP được sử dụng

- **17 lớp đối tượng** trong gói `model`:
  `Nguoi`, `KhachHang`, `NhanVien`, `ThuCung`, `Cho`, `Meo`, `Chim`,
  `SanPham`, `ThucAn`, `Thuoc`, `PhuKien`, `DichVu`, `DichVuTam`, `DichVuCatTia`,
  `DichVuTiemPhong`, `HoaDon`, `ChiTietHoaDon` (chưa kể 3 enum và 4 interface).
- **Hàm thiết lập (constructor)**: mọi lớp đều có constructor; lớp con gọi `super(...)`;
  `ChiTietHoaDon` có thêm factory `tuMatHang(MatHang, int)`.
- **Trừu tượng hóa**: `Nguoi`, `ThuCung`, `SanPham`, `DichVu`, `QuanLyCoBan` là lớp trừu tượng,
  có phương thức trừu tượng (`getLoaiNguoi`, `getLoai`, `getDacDiem`, `getChiPhiChamSoc`...).
- **Kế thừa**: `Cho`, `Meo`, `Chim` kế thừa `ThuCung`; `KhachHang`, `NhanVien` kế thừa `Nguoi`;
  `ThucAn`, `Thuoc`, `PhuKien` kế thừa `SanPham`; 3 lớp dịch vụ kế thừa `DichVu`.
- **Đa hình**: cùng gọi `getLoai()`, `getDacDiem()`, `truKho()`, `hoanKho()` qua tham chiếu
  `MatHang` / `ThuCung`; giỏ hàng và hóa đơn chỉ làm việc với interface `MatHang` nên bán được
  lẫn lộn thú cưng, sản phẩm và dịch vụ.
- **Interface & generic**: `MatHang` (mặt hàng bán được), `QuanLy<T>`, `QuanLyCoBan<T extends CoMaKhoa>`,
  `TextCodec<T>`, `TextFileRepository<T>` dùng lại cho cả 6 loại dữ liệu.
- **Thuộc tính static + phương thức static**:
  - `ThuCung.soLuongDaTao` + `ThuCung.getSoLuongDaTao()` đếm số đối tượng thú cưng đã tạo trong phiên
    (hiển thị ở đầu menu Quản lý thú cưng).
  - `SanPham.soLuongDaTao` + `SanPham.getSoLuongDaTao()`.
  - `HoaDon.TIEN_MOI_DIEM` (hằng số dùng chung cho quy tắc tích điểm).
- **Lớp danh sách đối tượng**: `QuanLyCoBan<T>` bọc `List<T>` và cung cấp thêm/xóa/cập nhật/tìm/lưu;
  các lớp `QuanLyThuCung`, `QuanLySanPham`, `QuanLyDichVu`... thêm nghiệp vụ lọc và sắp xếp.
- **Đọc/ghi tệp text**: `TextFileRepository<T>` + các `*Codec`, ghi qua tệp tạm rồi đổi tên.
- **Đóng gói**: thuộc tính `private`, thay đổi qua phương thức có kiểm tra (`truKho`, `congDiem`, `truDiem`...).
- **Xử lý ngoại lệ**: `AppException` → `DuLieuKhongHopLeException`, `KhongTimThayException`,
  `NghiepVuException`; tầng console bắt và in thông điệp tiếng Việt thay vì để chương trình dừng.
- **Collection & Stream API**: `List`, `Map`, `Comparator`, lọc/tìm kiếm/thống kê bằng stream.
- **Bất biến**: `HoaDon` và `ChiTietHoaDon` không đổi sau khi tạo; hóa đơn lưu lại thông tin
  tại thời điểm bán nên dữ liệu gốc thay đổi vẫn không làm sai hóa đơn cũ.
  "Sửa hóa đơn" = tạo hóa đơn mới thay thế với nhân viên lập khác; "xóa" = hủy hóa đơn.

### 6.3. Đối chiếu yêu cầu của đề

| Yêu cầu | Đáp ứng |
| --- | --- |
| Xem, thêm, sửa, xóa, tìm kiếm | Có đủ ở 5 danh mục (thú cưng, sản phẩm, dịch vụ, khách hàng, nhân viên) + hóa đơn (xem/tạo/sửa/hủy/tìm) |
| Ít nhất 15 lớp đối tượng | 17 lớp trong gói `model` |
| Có constructor | Tất cả các lớp |
| Kế thừa hợp lý | 3 nhánh kế thừa: `Nguoi`, `ThuCung`, `SanPham`, `DichVu` |
| Đa hình | Gọi phương thức qua `MatHang` / `ThuCung`; giỏ hàng đa loại |
| Lớp mảng / danh sách đối tượng | `QuanLyCoBan<T>` bọc `List<T>` cho 6 danh mục |
| Đọc/ghi file text | 6 tệp `.txt` UTF-8 trong `data/` |
| Thuộc tính static, phương thức static | `ThuCung.soLuongDaTao` / `getSoLuongDaTao()`, `SanPham.soLuongDaTao`, `HoaDon.TIEN_MOI_DIEM` |
| Lớp trừu tượng, hàm trừu tượng | `Nguoi`, `ThuCung`, `SanPham`, `DichVu`, `QuanLyCoBan` |
| Interface | `MatHang`, `CoMaKhoa`, `QuanLy<T>`, `TextCodec<T>` |

## 7. Định dạng tệp dữ liệu

Các tệp nằm trong `data/`, UTF-8, dòng bắt đầu bằng `#` là chú thích. Ký tự `|`, `;`, `~`
trong nội dung được mã hóa để không lẫn với dấu phân cách.

```
# thucung.txt
Chó|TC001|Milu|2025-03-12|6500000|2|CON_BAN|Poodle|Nhỏ|true

# sanpham.txt
Thức ăn|SP001|Hạt Royal Canin|320000|20|Chó|2.00|2027-05-30
Thuốc|SP003|Thuốc xổ giun|120000|15|Tẩy giun cho chó mèo|2026-12-31|false
Phụ kiện|SP005|Dây dắt da|180000|12|Da bò|Nâu

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

Các trường được sắp theo thứ tự cột cố định của từng loại; mỗi dòng hóa đơn lưu kèm tên khách hàng,
tên nhân viên và tên mặt hàng tại thời điểm bán để hóa đơn cũ không bị sai lệch.

## 8. Ghi chú khi báo cáo / bảo vệ

- Điểm nhấn nên trình bày: sơ đồ lớp ở mục 6, luồng
  `Bán hàng → CuaHang.thanhToan → kiểm tra tồn kho → trừ kho + cộng điểm + ghi tệp`,
  luồng `Hủy hóa đơn → hoàn kho + trừ điểm`, và việc dùng generic để tái sử dụng tầng lưu trữ cho 6 loại dữ liệu.
- Có thể xóa thư mục `data/` để chương trình nạp lại dữ liệu mẫu từ đầu.
- Ứng dụng chưa có đăng nhập/phân quyền — phù hợp phạm vi đồ án môn học.
