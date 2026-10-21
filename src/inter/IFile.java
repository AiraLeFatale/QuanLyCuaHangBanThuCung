package inter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface doc/ghi file text.
 * Moi lop quan ly phai cai dat docFile va ghiFile.
 */
public interface IFile {
    void docFile(String tenFile);

    void ghiFile(String tenFile);

    /** Doc tat ca cac dong (bo dong trong va dong bat dau bang #) cua 1 file UTF-8. */
    static List<String> docCacDong(String tenFile) {
        List<String> kq = new ArrayList<>();
        File f = new File(tenFile);
        if (!f.exists()) {
            return kq;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    kq.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Loi doc file " + tenFile + ": " + e.getMessage());
        }
        return kq;
    }

    /** Ghi danh sach dong ra file UTF-8 (tu tao thu muc cha neu chua co). */
    static void ghiCacDong(String tenFile, List<String> cacDong) {
        File f = new File(tenFile);
        if (f.getParentFile() != null) {
            f.getParentFile().mkdirs();
        }
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
            for (String s : cacDong) {
                pw.println(s);
            }
        } catch (IOException e) {
            System.out.println("Loi ghi file " + tenFile + ": " + e.getMessage());
        }
    }
}
