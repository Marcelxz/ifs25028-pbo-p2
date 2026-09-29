package framework.util;

import java.util.Scanner;

/**
 * Pembaca input konsol berbasis {@link Scanner} atas System.in.
 */
public final class InputUtil {
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputUtil() {
        // Utility class: tidak untuk di-instansiasi
    }

    /**
     * Menampilkan label lalu membaca satu baris input dari pengguna.
     * Jika input sudah habis, program berhenti secara normal (tanpa error).
     */
    public static String input(String label) {
        System.out.print(label + " : ");
        if (!SCANNER.hasNextLine()) {
            System.out.flush();
            System.exit(0);
        }
        return SCANNER.nextLine();
    }
}
