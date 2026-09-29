package framework.view;

import adapter.presenter.ActivityPresenter;
import domain.entity.SortOption;
import framework.util.InputUtil;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import usecase.ActivityUseCase;

/**
 * Tampilan konsol aplikasi jadwal kegiatan.
 * Menerima input user, memanggil use case, dan menampilkan hasil via presenter.
 * Layer ini tidak mengandung logika bisnis: hanya menangani interaksi user.
 */
public class ActivityView {
    private static final String CANCEL = "x";
    private static final String CANCEL_HINT = " (x Jika Batal)";
    private static final String KEEP_HINT = " Baru (Kosongkan jika tidak ingin mengubah)";

    private static final String MENU_ADD = "1";
    private static final String MENU_UPDATE = "2";
    private static final String MENU_SEARCH = "3";
    private static final String MENU_SORT = "4";
    private static final String MENU_REMOVE = "5";
    private static final String MENU_EXIT = CANCEL;

    private static final String SORT_DAY = "1";
    private static final String SORT_TIME = "2";
    private static final String SORT_TITLE_ASC = "3";
    private static final String SORT_TITLE_DESC = "4";

    /** Use case yang menjalankan operasi bisnis. */
    private final ActivityUseCase activityUseCase;

    /** Presenter yang memformat hasil ke output layar. */
    private final ActivityPresenter presenter;

    public ActivityView(ActivityUseCase activityUseCase, ActivityPresenter presenter) {
        this.activityUseCase = Objects.requireNonNull(activityUseCase, "activityUseCase tidak boleh null");
        this.presenter = Objects.requireNonNull(presenter, "presenter tidak boleh null");
    }

    /** Menampilkan menu utama dan loop interaksi user sampai user memilih keluar. */
    public void show() {
        boolean running = true;
        while (running) {
            presenter.showActivities(activityUseCase.getAllActivities());
            printMenu();

            switch (InputUtil.input("Pilih")) {
                case MENU_ADD -> addActivity();
                case MENU_UPDATE -> updateActivity();
                case MENU_SEARCH -> searchActivity();
                case MENU_SORT -> sortActivity();
                case MENU_REMOVE -> removeActivity();
                case MENU_EXIT -> running = false;
                default -> presenter.showInvalidChoice();
            }

            if (running) {
                System.out.println();
            }
        }
    }

    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Tambah");
        System.out.println("2. Ubah");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Hapus");
        System.out.println("x. Keluar");
    }

    /** Form tambah kegiatan baru. */
    private void addActivity() {
        System.out.println("[Menambah Kegiatan]");
        Optional<String> title = inputOrCancel("Judul" + CANCEL_HINT);
        if (title.isEmpty()) return;

        Optional<String> day = inputOrCancel("Hari" + CANCEL_HINT);
        if (day.isEmpty()) return;

        Optional<String> time = inputOrCancel("Waktu" + CANCEL_HINT);
        if (time.isEmpty()) return;

        presenter.showAddSuccess(activityUseCase.addActivity(title.get(), day.get(), time.get()));
    }

    /** Form ubah judul, hari, dan/atau waktu kegiatan (update parsial). */
    private void updateActivity() {
        System.out.println("[Mengubah Kegiatan]");
        Optional<String> rawId = inputOrCancel("ID Kegiatan yang diubah" + CANCEL_HINT);
        if (rawId.isEmpty()) return;

        OptionalInt id = parseId(rawId.get());
        if (id.isEmpty()) {
            presenter.showInvalidId();
            return;
        }

        // null berarti field tersebut tidak diubah
        String title = inputOrNullIfBlank("Judul" + KEEP_HINT);
        String day = inputOrNullIfBlank("Hari" + KEEP_HINT);
        String time = inputOrNullIfBlank("Waktu" + KEEP_HINT);

        if (activityUseCase.updateActivity(id.getAsInt(), title, day, time)) {
            presenter.showUpdateSuccess();
        } else {
            presenter.showUpdateFailed(id.getAsInt());
        }
    }

    /** Form cari kegiatan berdasarkan kata kunci judul. */
    private void searchActivity() {
        System.out.println("[Mencari Kegiatan]");
        Optional<String> keyword = inputOrCancel("Kata Kunci" + CANCEL_HINT);
        if (keyword.isEmpty()) return;

        presenter.showSearchResults(activityUseCase.searchActivities(keyword.get()), keyword.get());
    }

    /** Form urutkan kegiatan berdasarkan pilihan user. */
    private void sortActivity() {
        System.out.println("[Mengurutkan Kegiatan]");
        System.out.println("Pilihan Pengurutan:");
        System.out.println("1. Hari (Senin -> Minggu)");
        System.out.println("2. Waktu (Awal -> Akhir)");
        System.out.println("3. Judul (A-Z)");
        System.out.println("4. Judul (Z-A)");
        System.out.println("x. Batal");

        Optional<String> choice = inputOrCancel("Pilih");
        if (choice.isEmpty()) return;

        Optional<SortOption> option = mapSortOption(choice.get());
        if (option.isEmpty()) {
            presenter.showInvalidSortOption();
            return;
        }

        presenter.showSortedActivities(activityUseCase.sortActivities(option.get()));
    }

    /** Form hapus kegiatan berdasarkan ID. */
    private void removeActivity() {
        System.out.println("[Menghapus Kegiatan]");
        Optional<String> rawId = inputOrCancel("[ID Kegiatan] yang dihapus" + CANCEL_HINT);
        if (rawId.isEmpty()) return;

        OptionalInt id = parseId(rawId.get());
        if (id.isEmpty()) {
            presenter.showInvalidId();
            return;
        }

        if (activityUseCase.removeActivity(id.getAsInt())) {
            presenter.showRemoveSuccess();
        } else {
            presenter.showRemoveFailed(id.getAsInt());
        }
    }

    /** Membaca input dengan opsi batal; kosong jika user mengetik "x". */
    private Optional<String> inputOrCancel(String prompt) {
        String value = InputUtil.input(prompt);
        return value.equals(CANCEL) ? Optional.empty() : Optional.of(value);
    }

    /** Membaca input; mengembalikan null jika user mengosongkannya (field tidak diubah). */
    private String inputOrNullIfBlank(String prompt) {
        String value = InputUtil.input(prompt);
        return value.isBlank() ? null : value;
    }

    /** Mengonversi input string menjadi ID; kosong jika bukan angka bulat. */
    private OptionalInt parseId(String value) {
        try {
            return OptionalInt.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    /** Memetakan pilihan menu (1-4) ke {@link SortOption} domain. */
    private Optional<SortOption> mapSortOption(String choice) {
        return switch (choice) {
            case SORT_DAY -> Optional.of(SortOption.DAY);
            case SORT_TIME -> Optional.of(SortOption.TIME);
            case SORT_TITLE_ASC -> Optional.of(SortOption.TITLE_ASC);
            case SORT_TITLE_DESC -> Optional.of(SortOption.TITLE_DESC);
            default -> Optional.empty();
        };
    }
}
