package framework.view;

import adapter.presenter.GuestPresenter;
import framework.util.InputUtil;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import usecase.GuestUseCase;

/**
 * Tampilan konsol: menampilkan menu, membaca input pengguna,
 * memanggil usecase, lalu menyerahkan hasilnya ke presenter.
 */
public class GuestView {
    private static final String CANCEL = "x";
    private static final String MENU_REGISTER = "1";
    private static final String MENU_SEARCH = "2";
    private static final String MENU_REMOVE = "3";
    private static final String MENU_EXIT = CANCEL;

    private final GuestUseCase useCase;
    private final GuestPresenter presenter;

    public GuestView(GuestUseCase useCase, GuestPresenter presenter) {
        this.useCase = Objects.requireNonNull(useCase, "useCase tidak boleh null");
        this.presenter = Objects.requireNonNull(presenter, "presenter tidak boleh null");
    }

    /** Menjalankan loop menu utama sampai pengguna memilih keluar. */
    public void show() {
        boolean running = true;
        while (running) {
            presenter.showGuests(useCase.getAllGuests());
            printMenu();

            switch (InputUtil.input("Pilih")) {
                case MENU_REGISTER -> registerGuest();
                case MENU_SEARCH -> searchGuest();
                case MENU_REMOVE -> removeGuest();
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
        System.out.println("1. Daftarkan");
        System.out.println("2. Cari");
        System.out.println("3. Hapus");
        System.out.println("x. Keluar");
    }

    private void registerGuest() {
        System.out.println("[Mendaftarkan Tamu]");
        Optional<String> name = inputOrCancel("Nama");
        if (name.isEmpty()) return;

        Optional<String> purpose = inputOrCancel("Tujuan Kunjungan");
        if (purpose.isEmpty()) return;

        presenter.showAddSuccess(useCase.registerGuest(name.get(), purpose.get()));
    }

    private void searchGuest() {
        System.out.println("[Mencari Tamu]");
        Optional<String> keyword = inputOrCancel("Nama");
        if (keyword.isEmpty()) return;

        presenter.showSearchResults(useCase.searchGuests(keyword.get()), keyword.get());
    }

    private void removeGuest() {
        System.out.println("[Menghapus Tamu]");
        Optional<String> rawId = inputOrCancel("[ID Tamu] yang dihapus");
        if (rawId.isEmpty()) return;

        OptionalInt id = parseId(rawId.get());
        if (id.isEmpty()) {
            presenter.showInvalidId();
            return;
        }

        if (useCase.removeGuest(id.getAsInt())) {
            presenter.showRemoveSuccess();
        } else {
            presenter.showRemoveFailed(id.getAsInt());
        }
    }

    /** Membaca input dengan opsi batal; kosong jika pengguna mengetik "x". */
    private Optional<String> inputOrCancel(String label) {
        String value = InputUtil.input(label + " (" + CANCEL + " Jika Batal)");
        return value.equals(CANCEL) ? Optional.empty() : Optional.of(value);
    }

    private OptionalInt parseId(String value) {
        try {
            return OptionalInt.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
}
