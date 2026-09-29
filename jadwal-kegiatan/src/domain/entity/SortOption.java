package domain.entity;

import java.util.Comparator;
import java.util.Locale;

/**
 * Kriteria pengurutan kegiatan.
 * Setiap opsi membawa comparator-nya sendiri sehingga logika pengurutan
 * terpusat di domain, bukan tersebar sebagai angka ajaib di view/use case.
 */
public enum SortOption {
    /** Urutkan berdasarkan hari (Senin sampai Minggu), lalu waktu. */
    DAY(Comparator.comparingInt((Activity activity) -> dayOrder(activity.getDay()))
            .thenComparing(Activity::getTime)),

    /** Urutkan berdasarkan waktu dari yang paling awal. */
    TIME(Comparator.comparing(Activity::getTime)),

    /** Urutkan judul dari A ke Z (case-insensitive). */
    TITLE_ASC(Comparator.comparing(Activity::getTitle, String.CASE_INSENSITIVE_ORDER)),

    /** Urutkan judul dari Z ke A (case-insensitive). */
    TITLE_DESC(Comparator.comparing(Activity::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());

    /** Urutan untuk hari yang tidak dikenal: diletakkan setelah Minggu. */
    private static final int UNKNOWN_DAY_ORDER = 8;

    private final Comparator<Activity> comparator;

    SortOption(Comparator<Activity> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Activity> comparator() {
        return comparator;
    }

    /** Menentukan urutan hari dalam seminggu (Senin = 1 sampai Minggu = 7). */
    private static int dayOrder(String day) {
        return switch (day.trim().toLowerCase(Locale.ROOT)) {
            case "senin" -> 1;
            case "selasa" -> 2;
            case "rabu" -> 3;
            case "kamis" -> 4;
            case "jumat" -> 5;
            case "sabtu" -> 6;
            case "minggu" -> 7;
            default -> UNKNOWN_DAY_ORDER;
        };
    }
}
