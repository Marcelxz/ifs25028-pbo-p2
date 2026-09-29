package framework.util;

import java.util.Scanner;

/**
 * Utility untuk membaca input string dari keyboard.
 * Berada di layer framework karena bergantung pada {@link System#in}.
 */
public final class InputUtil {
    /** Scanner bersama untuk seluruh aplikasi (satu instance cukup). */
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputUtil() {
        // Utility class: tidak untuk di-instansiasi
    }

    /**
     * Menampilkan prompt dan membaca satu baris input dari user.
     *
     * @param label label yang ditampilkan sebelum input
     * @return teks yang diketik user
     */
    public static String input(String label) {
        System.out.print(label + " : ");
        return SCANNER.nextLine();
    }
}
