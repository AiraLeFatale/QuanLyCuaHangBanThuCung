package petshop.io;

/** Chuyển một đối tượng thành một dòng văn bản và ngược lại. */
public interface TextCodec<T> {

    String toLine(T doiTuong);

    T fromLine(String dong);
}
