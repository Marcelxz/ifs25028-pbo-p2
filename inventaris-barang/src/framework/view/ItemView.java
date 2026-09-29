package framework.view;

import adapter.presenter.ItemPresenter;
import domain.entity.SortOption;
import framework.util.InputUtil;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import usecase.ItemUseCase;

/**
 * Tampilan konsol: menampilkan menu, membaca input pengguna,
 * memanggil usecase, lalu menyerahkan hasilnya ke presenter.
 * Perintah batal ("x") tidak membedakan huruf besar/kecil.
 */
public class ItemView {
    private static final String CANCEL = "x";
    private static final String CANCEL_HINT = " (x Jika Batal)";

    private static final String MENU_ADD = "1";
    private static final String MENU_UPDATE_STOCK = "2";
    private static final String MENU_SEARCH = "3";
    private static final String MENU_SORT = "4";
    private static final String MENU_REMOVE = "5";

    private static final String SORT_NAME_ASC = "1";
    private static final String SORT_NAME_DESC = "2";
    private static final String SORT_QTY_ASC = "3";
    private static final String SORT_QTY_DESC = "4";

    private final ItemUseCase useCase;
    private final ItemPresenter presenter;

    public ItemView(ItemUseCase useCase, ItemPresenter presenter) {
        this.useCase = Objects.requireNonNull(useCase, "useCase tidak boleh null");
        this.presenter = Objects.requireNonNull(presenter, "presenter tidak boleh null");
    }

    /** Menjalankan loop menu utama sampai pengguna memilih keluar. */
    public void show() {
        boolean running = true;
        while (running) {
            presenter.showItems(useCase.getAllItems());
            printMenu();

            String choice = InputUtil.input("Pilih");
            if (isCancel(choice)) {
                running = false;
            } else {
                handleMenuChoice(choice);
                System.out.println();
            }
        }
    }

    private void handleMenuChoice(String choice) {
        switch (choice) {
            case MENU_ADD -> addItem();
            case MENU_UPDATE_STOCK -> updateStock();
            case MENU_SEARCH -> searchItem();
            case MENU_SORT -> sortItem();
            case MENU_REMOVE -> removeItem();
            default -> presenter.showInvalidChoice();
        }
    }

    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Tambah");
        System.out.println("2. Ubah Stok");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Hapus");
        System.out.println("x. Keluar");
    }

    private void addItem() {
        System.out.println("[Menambah Barang]");
        Optional<String> name = inputOrCancel("Nama" + CANCEL_HINT);
        if (name.isEmpty()) return;

        // Prompt jumlah tidak menampilkan hint batal, tetapi "x" tetap membatalkan
        Optional<String> rawQuantity = inputOrCancel("Jumlah");
        if (rawQuantity.isEmpty()) return;

        OptionalInt quantity = parsePositiveQuantity(rawQuantity.get());
        if (quantity.isEmpty()) {
            presenter.showInvalidQuantity();
            return;
        }

        Optional<String> category = inputOrCancel("Kategori" + CANCEL_HINT);
        if (category.isEmpty()) return;

        presenter.showAddSuccess(useCase.addItem(name.get(), quantity.getAsInt(), category.get()));
    }

    private void updateStock() {
        System.out.println("[Mengubah Stok]");
        Optional<String> rawId = inputOrCancel("ID Barang yang diubah" + CANCEL_HINT);
        if (rawId.isEmpty()) return;

        OptionalInt id = parseId(rawId.get());
        if (id.isEmpty()) {
            presenter.showInvalidId();
            return;
        }

        Optional<String> rawQuantity = inputOrCancel("Jumlah Baru (Kosongkan jika tidak ingin mengubah)");
        if (rawQuantity.isEmpty()) return;

        // null berarti stok lama dipertahankan (update parsial)
        Integer newQuantity = null;
        if (!rawQuantity.get().isBlank()) {
            OptionalInt parsed = parsePositiveQuantity(rawQuantity.get());
            if (parsed.isEmpty()) {
                presenter.showInvalidQuantity();
                return;
            }
            newQuantity = parsed.getAsInt();
        }

        if (useCase.updateStock(id.getAsInt(), newQuantity)) {
            presenter.showUpdateSuccess();
        } else {
            presenter.showUpdateFailed(id.getAsInt());
        }
    }

    private void searchItem() {
        System.out.println("[Mencari Barang]");
        Optional<String> keyword = inputOrCancel("Kata Kunci" + CANCEL_HINT);
        if (keyword.isEmpty()) return;

        presenter.showSearchResults(useCase.searchItems(keyword.get()), keyword.get());
    }

    private void sortItem() {
        System.out.println("[Mengurutkan Barang]");
        System.out.println("Pilihan Pengurutan:");
        System.out.println("1. Nama (A-Z)");
        System.out.println("2. Nama (Z-A)");
        System.out.println("3. Jumlah (Terkecil -> Terbesar)");
        System.out.println("4. Jumlah (Terbesar -> Terkecil)");
        System.out.println("x. Batal");

        Optional<String> choice = inputOrCancel("Pilih");
        if (choice.isEmpty()) return;

        Optional<SortOption> option = mapSortOption(choice.get());
        if (option.isEmpty()) {
            presenter.showInvalidSortOption();
            return;
        }

        presenter.showSortedItems(useCase.sortItems(option.get()));
    }

    private void removeItem() {
        System.out.println("[Menghapus Barang]");
        Optional<String> rawId = inputOrCancel("[ID Barang] yang dihapus" + CANCEL_HINT);
        if (rawId.isEmpty()) return;

        OptionalInt id = parseId(rawId.get());
        if (id.isEmpty()) {
            presenter.showInvalidId();
            return;
        }

        if (useCase.removeItem(id.getAsInt())) {
            presenter.showRemoveSuccess();
        } else {
            presenter.showRemoveFailed(id.getAsInt());
        }
    }

    private boolean isCancel(String value) {
        return value.equalsIgnoreCase(CANCEL);
    }

    /** Membaca input dengan opsi batal; kosong jika pengguna mengetik "x" (huruf besar/kecil). */
    private Optional<String> inputOrCancel(String prompt) {
        String value = InputUtil.input(prompt);
        return isCancel(value) ? Optional.empty() : Optional.of(value);
    }

    private OptionalInt parseId(String value) {
        try {
            return OptionalInt.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    /** Mengurai jumlah stok; kosong jika bukan angka bulat atau tidak lebih dari nol. */
    private OptionalInt parsePositiveQuantity(String value) {
        try {
            int quantity = Integer.parseInt(value);
            return quantity > 0 ? OptionalInt.of(quantity) : OptionalInt.empty();
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    private Optional<SortOption> mapSortOption(String choice) {
        return switch (choice) {
            case SORT_NAME_ASC -> Optional.of(SortOption.NAME_ASC);
            case SORT_NAME_DESC -> Optional.of(SortOption.NAME_DESC);
            case SORT_QTY_ASC -> Optional.of(SortOption.QTY_ASC);
            case SORT_QTY_DESC -> Optional.of(SortOption.QTY_DESC);
            default -> Optional.empty();
        };
    }
}
