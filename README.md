# Quản Lý Cửa Hàng Bán Thú Cưng (Java OOP)

Đồ án môn **Lập trình hướng đối tượng** — ứng dụng Java **chạy trên console**, quản lý cửa hàng
bán thú cưng và sản phẩm, lưu dữ liệu bằng tệp văn bản `.txt` (UTF-8).
Không dùng giao diện đồ họa, không dùng cơ sở dữ liệu, không dùng Maven/Gradle.

## 1. Chức năng chính

| Nhóm chức năng | Mô tả |
| --- | --- |
| Quản lý thú cưng | Xem danh sách, thêm, sửa, xóa, tìm kiếm theo tên; phân loại **Chó / Mèo / Chim** với thuộc tính riêng; đánh dấu đã bán; minh họa đa hình bằng chức năng "nghe tiếng kêu" |
| Quản lý sản phẩm | Xem, thêm, sửa, xóa, tìm kiếm theo tên; phân loại **thức ăn / thuốc / phụ kiện**; theo dõi số lượng tồn kho |
| Quản lý khách hàng | Xem, thêm, sửa, xóa, tìm kiếm theo tên; quản lý điểm tích lũy và mức giảm giá theo điểm |
| Quản lý nhân viên | Xem, thêm, sửa, xóa, tìm kiếm theo tên; hồ sơ gồm chức vụ và lương |
| Bán hàng / hóa đơn | Xem danh sách, lập hóa đơn mới (bán thú cưng và sản phẩm), sửa hóa đơn (nhân viên lập, ngày lập), xóa hóa đơn, tìm hóa đơn theo mã khách hàng, thống kê doanh thu |
| Tính giá | Giảm giá kết hợp giữa mặt hàng và hạng khách hàng, tổng mức giảm tối đa 30%/dòng; in chi tiết hóa đơn ra màn hình |

## 2. Quy tắc nghiệp vụ

- **Giảm giá theo mặt hàng** (`IGiamGia`):
  - Chó: 10% nếu từ 12 tháng tuổi, ngược lại 5%.
  - Mèo: 10% nếu từ 12 tháng tuổi, ngược lại 3%.
  - Chim: 2%.
  - Thức ăn: 5% — Thuốc: 0% — Phụ kiện: 10%.
- **Giảm giá theo khách hàng**: từ 500 điểm giảm 10%, từ 100 điểm giảm 5%, dưới 100 điểm không giảm.
- **Tổng giảm giá mỗi dòng** = giảm của mặt hàng + giảm của khách hàng, nhưng **không vượt quá 30%**
  (`Main.tinhGiam`).
- **Tích điểm**: cứ 10.000 đ giá trị hóa đơn (sau giảm giá) được 1 điểm (`KhachHang.congDiem`).
- Bán thú cưng sẽ đánh dấu `daBan = true`; bán sản phẩm sẽ trừ số lượng tồn kho.
- Mã hóa đơn tự sinh tăng dần: `HD001`, `HD002`, ...
- Sau mỗi thao tác thêm / sửa / xóa / lập hóa đơn, dữ liệu được ghi ngay xuống tệp tương ứng.

## 3. Công nghệ

- **Java 8+** (đã kiểm thử với JDK 17/21), giao diện console.
- Lưu trữ: 5 tệp văn bản UTF-8 trong thư mục `data/`.
- Không dùng thư viện ngoài, không dùng build tool.

## 4. Cách chạy

Yêu cầu: đã cài JDK và có lệnh `javac`, `java` trong PATH. **Luôn chạy từ thư mục gốc dự án**
vì chương trình đọc/ghi tệp theo đường dẫn tương đối `data/...`.

```bat
chay.bat
```

Hoặc biên dịch và chạy thủ công:

```bat
mkdir out
javac -encoding UTF-8 -d out src\inter\*.java src\entity\*.java src\manager\*.java src\main\*.java
java -cp out main.Main
```

### Mở bằng IDE

- **IntelliJ IDEA**: `File > Open` → chọn thư mục dự án → đánh dấu `src` là *Sources Root* → chạy `main.Main`.
- **Eclipse**: `File > New > Java Project` → bỏ chọn *Use default location* và trỏ tới thư mục này → chạy `Main`.
- **NetBeans**: tạo Java Application mới, copy `src/` và `data/` vào thư mục gốc project → chạy `Main.java`.
- **VS Code**: cài *Extension Pack for Java*, mở thư mục dự án rồi bấm Run ở `Main.java`.

> Menu và thông báo trong chương trình viết **không dấu** để hiển thị ổn định trên mọi cửa sổ console
> Windows. Dữ liệu trong tệp vẫn là UTF-8 và có thể chứa tiếng Việt có dấu.

## 5. Bản đồ menu

```
MENU CHINH
  1. Quan ly thu cung
       1 Xem danh sach          4 Xoa
       2 Them                   5 Tim kiem theo ten
       3 Sua                    6 Nghe tieng keu (da hinh)
  2. Quan ly san pham
       1 Xem danh sach          4 Xoa
       2 Them                   5 Tim kiem theo ten
       3 Sua
  3. Quan ly khach hang
       1 Xem danh sach          4 Xoa
       2 Them                   5 Tim kiem theo ten
       3 Sua
  4. Quan ly nhan vien
       1 Xem danh sach          4 Xoa
       2 Them                   5 Tim kiem theo ten
       3 Sua
  5. Quan ly hoa don / ban hang
       1 Xem danh sach hoa don
       2 Lap hoa don moi (ban hang)
       3 Sua hoa don (nhan vien, ngay lap)
       4 Xoa hoa don
       5 Tim hoa don theo ma khach hang
       6 Thong ke doanh thu
  0. Thoat
```

Khi lập hóa đơn: chọn `1` để thêm thú cưng, `2` để thêm sản phẩm, `0` để kết thúc.
Số lượng sản phẩm được kiểm tra theo tồn kho; thú cưng đã bán sẽ bị từ chối.
Ký tự phân cách của tệp (`|`, `;`, `#`) được tự động loại bỏ khỏi dữ liệu nhập.

## 6. Cấu trúc thư mục

```
QuanLyCuaHangThuCung/
├── chay.bat                 # biên dịch + chạy
├── README.md
├── data/                    # dữ liệu mẫu và dữ liệu làm việc (UTF-8)
│   ├── thucung.txt
│   ├── sanpham.txt
│   ├── khachhang.txt
│   ├── nhanvien.txt
│   └── hoadon.txt
├── out/                     # sinh ra khi biên dịch (không cần nộp)
└── src/
    ├── entity/              # 13 lớp thực thể
    │   ├── Nguoi.java, KhachHang.java, NhanVien.java
    │   ├── ThuCung.java, Cho.java, Meo.java, Chim.java
    │   ├── SanPham.java, ThucAn.java, Thuoc.java, PhuKien.java
    │   └── HoaDon.java, ChiTietHoaDon.java
    ├── inter/               # 2 interface: IFile, IGiamGia
    ├── manager/             # 5 lớp danh sách + CRUD + đọc/ghi tệp
    │   ├── QuanLyThuCung.java, QuanLySanPham.java
    │   ├── QuanLyKhachHang.java, QuanLyNhanVien.java
    │   └── QuanLyHoaDon.java
    └── main/Main.java       # menu, nhập liệu, nghiệp vụ bán hàng
```

Luồng chạy:

```
Main.main()
  → QuanLy*.docFile(data/*.txt)
  → vòng lặp menu chính
      → menu con (thú cưng / sản phẩm / khách hàng / nhân viên / hóa đơn)
      → thao tác CRUD hoặc lập hóa đơn
      → QuanLy*.ghiFile(...) ghi ngay xuống tệp
  → chọn 0 để thoát
```

## 7. Thiết kế hướng đối tượng

### 7.1. Sơ đồ lớp

```
                    IGiamGia (interface)
          ▲                  ▲                    ▲
   ThuCung (abstract)   SanPham (abstract)    KhachHang
     ▲    ▲    ▲          ▲      ▲      ▲
    Cho  Meo  Chim      ThucAn  Thuoc  PhuKien

                     Nguoi (abstract)
                      ▲           ▲
                 KhachHang    NhanVien

   IFile (interface) ── QuanLyThuCung, QuanLySanPham,
                        QuanLyKhachHang, QuanLyNhanVien, QuanLyHoaDon

   HoaDon ◆──── ChiTietHoaDon        (một hóa đơn gồm nhiều dòng hàng)

   Main ── menu + nhập liệu + nghiệp vụ bán hàng
```

> `KhachHang` vừa kế thừa `Nguoi`, vừa cài đặt `IGiamGia`; `ThuCung` và `SanPham` là hai nhánh
> trừu tượng độc lập, các lớp con cài đặt hành vi riêng.

### 7.2. Các kỹ thuật OOP được sử dụng

- **Lớp và đối tượng**: 21 tệp Java, gồm **13 lớp thực thể**, **5 lớp quản lý**, **1 lớp Main**
  và **2 interface**.
- **Hàm thiết lập (constructor)**: mọi lớp đều có constructor; các lớp con gọi `super(...)`;
  một số lớp có thêm constructor không tham số để thuận tiện khởi tạo.
- **Trừu tượng hóa**: `Nguoi`, `ThuCung`, `SanPham` là lớp trừu tượng, có phương thức trừu tượng:
  `getVaiTro()`, `getLoai()`, `tiengKeu()`, `moTa()`, `thongTinRieng()`...
- **Kế thừa**:
  - `KhachHang`, `NhanVien` kế thừa `Nguoi`.
  - `Cho`, `Meo`, `Chim` kế thừa `ThuCung`.
  - `ThucAn`, `Thuoc`, `PhuKien` kế thừa `SanPham`.
- **Đa hình**:
  - `QuanLyThuCung.choTatCaKeu()` gọi `tiengKeu()` trên danh sách `ThuCung`; mỗi lớp con trả về
    tiếng kêu khác nhau (`Gau gau!`, `Meo meo!`, `Chiu chiu!`...).
  - `Main.tinhGiam(IGiamGia hang, IGiamGia khach)` nhận bất kỳ đối tượng nào cài đặt `IGiamGia`
    (thú cưng, sản phẩm, khách hàng) và gọi `tinhGiamGia()` để tính giảm giá.
  - `inDanhSach(List<?>)` in từng đối tượng qua `toString()` đã được ghi đè.
- **Interface**:
  - `IGiamGia` — chuẩn hóa cách tính giảm giá cho nhiều loại đối tượng.
  - `IFile` — chuẩn hóa `docFile`, `ghiFile`, kèm hai phương thức static dùng chung
    `docCacDong`, `ghiCacDong`.
- **Lớp danh sách đối tượng**: `QuanLyThuCung`, `QuanLySanPham`, `QuanLyKhachHang`,
  `QuanLyNhanVien`, `QuanLyHoaDon` đều bọc `ArrayList<T>` và cung cấp thêm / sửa / xóa / tìm / hiển thị.
- **Thuộc tính static + phương thức static**:
  - `ThuCung.soLuongDaTao` (private static) đếm số đối tượng thú cưng đã khởi tạo;
    `ThuCung.getSoLuongDaTao()` là phương thức static, hiển thị ở đầu menu quản lý thú cưng.
  - `HoaDon.ngayHienTai()` trả về ngày hiện tại dạng `dd/MM/yyyy`.
  - `Main.sc` (Scanner dùng chung) và các hàm nhập liệu `nhapInt`, `nhapDouble`, `nhapChuoi`... là static.
  - `IFile.docCacDong`, `IFile.ghiCacDong` là static method trong interface.
- **Đọc / ghi tệp text**: `IFile` đọc UTF-8, bỏ qua dòng trống và dòng chú thích `#`,
  loại bỏ BOM; ghi đè toàn bộ danh sách sau mỗi thao tác.
- **Đóng gói**: thuộc tính dùng `private`/`protected`, truy cập qua getter/setter.
- **Xử lý lỗi**: dữ liệu tệp sai định dạng bị bỏ qua kèm thông báo; nhập số sai được hỏi lại;
  kiểm tra trùng mã, kiểm tra tồn kho, kiểm tra thú cưng đã bán.

### 7.3. Đối chiếu yêu cầu của đề

| Yêu cầu | Đáp ứng |
| --- | --- |
| Xem, thêm, sửa, xóa, tìm kiếm | Có đủ ở 4 danh mục (thú cưng, sản phẩm, khách hàng, nhân viên) và hóa đơn (xem / lập / sửa / xóa / tìm theo mã khách hàng) |
| Ít nhất 15 lớp đối tượng | 21 lớp Java: 13 lớp thực thể + 5 lớp quản lý + 1 lớp Main, cùng 2 interface |
| Có hàm thiết lập (constructor) | Tất cả các lớp |
| Kế thừa hợp lý | 3 nhánh: `Nguoi`, `ThuCung`, `SanPham` |
| Cài đặt và sử dụng đa hình | `tiengKeu()`, `moTa()`, `toString()`, `tinhGiamGia()` được gọi qua tham chiếu cha/interface |
| Lớp mảng các đối tượng (danh sách) | 5 lớp quản lý bọc `ArrayList<T>` |
| Đọc / ghi dữ liệu lên file text | `IFile` + 5 tệp `.txt` UTF-8 |
| Thuộc tính static, phương thức static | `ThuCung.soLuongDaTao` / `getSoLuongDaTao()`, `HoaDon.ngayHienTai()`, các hàm static trong `Main` và `IFile` |
| Lớp trừu tượng, hàm trừu tượng | `Nguoi`, `ThuCung`, `SanPham` |
| Interface | `IFile`, `IGiamGia` |

## 8. Định dạng tệp dữ liệu

Các tệp nằm trong `data/`, mã hóa UTF-8. Dòng trống và dòng bắt đầu bằng `#` được bỏ qua khi đọc.
Ký tự `|` phân tách các trường của bản ghi; riêng hóa đơn dùng thêm `;` và `#` (xem bên dưới).

```
# thucung.txt
CHO|TC001|Lulu|6|5000000|Duc|false|Poodle|3.5
MEO|TC003|Gugu|5|3500000|Cai|true|Xam xanh|false
CHIM|TC005|Vet|8|1200000|Duc|false|Xanh|true
# LOAI|ma|ten|tuoiThang|gia|gioiTinh|daBan|thongTinRieng
#   CHO : giong|canNang          MEO : mauLong|longDai        CHIM : mauSac|bietNoi

# sanpham.txt
THUCAN|SP001|Hat Royal Canin 2kg|250000|20|Cho|2.0|31/12/2027
THUOC|SP003|Thuoc xit ve ray|120000|9|Tri ve ray bo chet|01/08/2027
PHUKIEN|SP004|Vong co da|90000|30|Da|Do
# LOAI|ma|ten|gia|soLuong|thongTinRieng
#   THUCAN : loaiThuCung|trongLuong|hanSuDung
#   THUOC  : congDung|hanSuDung
#   PHUKIEN: chatLieu|mauSac

# khachhang.txt
KH001|Nguyen Van An|0901234567|Quan 5, TP.HCM|510
# ma|hoTen|sdt|diaChi|diemTichLuy

# nhanvien.txt
NV001|Pham Quoc Dat|0933111222|Quan 1, TP.HCM|Quan ly|15000000
# ma|hoTen|sdt|diaChi|chucVu|luong

# hoadon.txt
HD001|KH001|NV002|01/10/2026|SP001;Hat Royal Canin 2kg;2;250000;5.0#SP004;Vong co da;1;90000;10.0
# maHD|maKH|maNV|ngayLap|danhSachChiTiet
# moi chi tiet: maHang;tenHang;soLuong;donGia;tiLeGiam
# nhieu chi tiet cach nhau bang dau #
```

Thông tin `tenHang`, `donGia`, `tiLeGiam` được lưu ngay tại thời điểm bán, nên khi giá sản phẩm
thay đổi sau này thì hóa đơn cũ vẫn giữ đúng số liệu.

## 9. Ghi chú khi báo cáo / bảo vệ

- Điểm nhấn nên trình bày:
  - Sơ đồ lớp ở mục 7.1 và 3 nhánh kế thừa.
  - Đa hình qua `IGiamGia`: cùng một lời gọi `tinhGiamGia()` nhưng thú cưng, sản phẩm và khách hàng
    trả về mức giảm khác nhau; `Main.tinhGiam` chỉ làm việc với interface.
  - Đa hình qua `ThuCung.tiengKeu()` ở menu "Nghe tiếng kêu".
  - Luồng lập hóa đơn: kiểm tra khách/nhân viên → thêm thú cưng/sản phẩm → tính giảm giá →
    cộng điểm → ghi 4 tệp.
  - Thuộc tính static `ThuCung.soLuongDaTao` hiển thị trực tiếp trên menu.
- Dữ liệu mẫu có sẵn trong `data/` giúp demo ngay; có thể sửa hoặc xóa nội dung các tệp để thử
  chức năng thêm mới.
- **Hạn chế đã biết** (có thể nêu khi bảo vệ hoặc nâng cấp):
  - Chưa có chức năng sắp xếp danh sách.
  - Sửa thú cưng / sản phẩm chỉ cập nhật các thuộc tính chung; thuộc tính riêng của từng loài
    (giống, cân nặng, màu lông...) giữ nguyên.
  - Xóa hóa đơn chỉ xóa bản ghi, không tự hoàn kho và không trừ lại điểm khách hàng.
  - Tìm hóa đơn theo đúng mã khách hàng (không tìm theo tên khách hoặc ngày).
  - Chưa kiểm tra trùng số điện thoại, số âm hay tính hợp lệ của ngày nhập.
  - Chưa có đăng nhập / phân quyền.
