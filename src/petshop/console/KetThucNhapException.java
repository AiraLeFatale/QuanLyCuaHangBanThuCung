package petshop.console;

/** Ném ra khi người dùng kết thúc nhập (Ctrl+Z / Ctrl+D) để thoát chương trình an toàn. */
public class KetThucNhapException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public KetThucNhapException() {
        super("Kết thúc nhập");
    }
}
