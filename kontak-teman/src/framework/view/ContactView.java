package framework.view;

import adapter.presenter.ContactPresenter;
import domain.entity.SortOption;
import framework.util.InputUtil;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import usecase.ContactUseCase;

/**
 * Tampilan konsol aplikasi kontak teman.
 * Menerima input user, memanggil use case, dan menampilkan hasil via presenter.
 * Layer ini tidak mengandung logika bisnis: hanya menangani interaksi user.
 */
public class ContactView {
    private static final String CANCEL = "x";
    private static final String CANCEL_HINT = " (x Jika Batal)";
    private static final String KEEP_HINT = " Baru (Kosongkan jika tidak ingin mengubah)";

    private static final String MENU_ADD = "1";
    private static final String MENU_UPDATE = "2";
    private static final String MENU_SEARCH = "3";
    private static final String MENU_SORT = "4";
    private static final String MENU_REMOVE = "5";
    private static final String MENU_EXIT = CANCEL;

    private static final String SORT_NAME_ASC = "1";
    private static final String SORT_NAME_DESC = "2";

    /** Use case yang menjalankan operasi bisnis. */
    private final ContactUseCase contactUseCase;

    /** Presenter yang memformat hasil ke output layar. */
    private final ContactPresenter presenter;

    public ContactView(ContactUseCase contactUseCase, ContactPresenter presenter) {
        this.contactUseCase = Objects.requireNonNull(contactUseCase, "contactUseCase tidak boleh null");
        this.presenter = Objects.requireNonNull(presenter, "presenter tidak boleh null");
    }

    /** Menampilkan menu utama dan loop interaksi user sampai user memilih keluar. */
    public void show() {
        boolean running = true;
        while (running) {
            presenter.showContacts(contactUseCase.getAllContacts());
            printMenu();

            switch (InputUtil.input("Pilih")) {
                case MENU_ADD -> addContact();
                case MENU_UPDATE -> updateContact();
                case MENU_SEARCH -> searchContact();
                case MENU_SORT -> sortContact();
                case MENU_REMOVE -> removeContact();
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

    /** Form tambah kontak baru. */
    private void addContact() {
        System.out.println("[Menambah Kontak]");
        Optional<String> name = inputOrCancel("Nama" + CANCEL_HINT);
        if (name.isEmpty()) return;

        // Prompt telepon dan email tidak menampilkan hint batal, tetapi "x" tetap membatalkan
        Optional<String> phone = inputOrCancel("Telepon");
        if (phone.isEmpty()) return;

        Optional<String> email = inputOrCancel("Email");
        if (email.isEmpty()) return;

        presenter.showAddSuccess(contactUseCase.addContact(name.get(), phone.get(), email.get()));
    }

    /** Form ubah nama, telepon, dan/atau email kontak (update parsial). */
    private void updateContact() {
        System.out.println("[Mengubah Kontak]");
        Optional<String> rawId = inputOrCancel("ID Kontak yang diubah" + CANCEL_HINT);
        if (rawId.isEmpty()) return;

        OptionalInt id = parseId(rawId.get());
        if (id.isEmpty()) {
            presenter.showInvalidId();
            return;
        }

        // null berarti field tersebut tidak diubah
        String name = inputOrNullIfBlank("Nama" + KEEP_HINT);
        String phone = inputOrNullIfBlank("Telepon" + KEEP_HINT);
        String email = inputOrNullIfBlank("Email" + KEEP_HINT);

        if (contactUseCase.updateContact(id.getAsInt(), name, phone, email)) {
            presenter.showUpdateSuccess();
        } else {
            presenter.showUpdateFailed(id.getAsInt());
        }
    }

    /** Form cari kontak berdasarkan kata kunci nama. */
    private void searchContact() {
        System.out.println("[Mencari Kontak]");
        Optional<String> keyword = inputOrCancel("Kata Kunci" + CANCEL_HINT);
        if (keyword.isEmpty()) return;

        presenter.showSearchResults(contactUseCase.searchContacts(keyword.get()), keyword.get());
    }

    /** Form urutkan kontak berdasarkan pilihan user. */
    private void sortContact() {
        System.out.println("[Mengurutkan Kontak]");
        System.out.println("Pilihan Pengurutan:");
        System.out.println("1. Nama (A-Z)");
        System.out.println("2. Nama (Z-A)");
        System.out.println("x. Batal");

        Optional<String> choice = inputOrCancel("Pilih");
        if (choice.isEmpty()) return;

        Optional<SortOption> option = mapSortOption(choice.get());
        if (option.isEmpty()) {
            presenter.showInvalidSortOption();
            return;
        }

        presenter.showSortedContacts(contactUseCase.sortContacts(option.get()));
    }

    /** Form hapus kontak berdasarkan ID. */
    private void removeContact() {
        System.out.println("[Menghapus Kontak]");
        Optional<String> rawId = inputOrCancel("[ID Kontak] yang dihapus" + CANCEL_HINT);
        if (rawId.isEmpty()) return;

        OptionalInt id = parseId(rawId.get());
        if (id.isEmpty()) {
            presenter.showInvalidId();
            return;
        }

        if (contactUseCase.removeContact(id.getAsInt())) {
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

    /** Memetakan pilihan menu (1-2) ke {@link SortOption} domain. */
    private Optional<SortOption> mapSortOption(String choice) {
        return switch (choice) {
            case SORT_NAME_ASC -> Optional.of(SortOption.NAME_ASC);
            case SORT_NAME_DESC -> Optional.of(SortOption.NAME_DESC);
            default -> Optional.empty();
        };
    }
}
