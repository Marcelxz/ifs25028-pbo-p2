package framework.view;

import adapter.presenter.FinancePresenter;
import domain.entity.SortOption;
import domain.entity.TransactionType;
import framework.util.InputUtil;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import usecase.FinanceUseCase;

/**
 * Tampilan konsol: menampilkan menu, membaca input pengguna,
 * memanggil usecase, lalu menyerahkan hasilnya ke presenter.
 */
public class FinanceView {
    private static final String CANCEL = "x";
    private static final String MENU_ADD_INCOME = "1";
    private static final String MENU_ADD_EXPENSE = "2";
    private static final String MENU_SEARCH = "3";
    private static final String MENU_SORT = "4";
    private static final String MENU_BALANCE = "5";
    private static final String MENU_REMOVE = "6";
    private static final String MENU_EXIT = CANCEL;

    private static final String SORT_AMOUNT_ASC = "1";
    private static final String SORT_AMOUNT_DESC = "2";
    private static final String SORT_INCOME_FIRST = "3";
    private static final String SORT_EXPENSE_FIRST = "4";

    private final FinanceUseCase useCase;
    private final FinancePresenter presenter;

    public FinanceView(FinanceUseCase useCase, FinancePresenter presenter) {
        this.useCase = Objects.requireNonNull(useCase, "useCase tidak boleh null");
        this.presenter = Objects.requireNonNull(presenter, "presenter tidak boleh null");
    }

    /** Menjalankan loop menu utama sampai pengguna memilih keluar. */
    public void show() {
        boolean running = true;
        while (running) {
            presenter.showTransactions(useCase.getAllTransactions(), useCase.getBalance());
            printMenu();

            switch (InputUtil.input("Pilih")) {
                case MENU_ADD_INCOME -> addTransaction(TransactionType.INCOME);
                case MENU_ADD_EXPENSE -> addTransaction(TransactionType.EXPENSE);
                case MENU_SEARCH -> searchTransaction();
                case MENU_SORT -> sortTransaction();
                case MENU_BALANCE -> presenter.showBalance(useCase.getBalance());
                case MENU_REMOVE -> removeTransaction();
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
        System.out.println("1. Tambah Pemasukan");
        System.out.println("2. Tambah Pengeluaran");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Lihat Saldo");
        System.out.println("6. Hapus");
        System.out.println("x. Keluar");
    }

    private void addTransaction(TransactionType type) {
        System.out.println("[Tambah " + type.getLabel() + "]");

        Optional<String> description = inputOrCancel("Keterangan");
        if (description.isEmpty()) return;

        // Prompt jumlah tidak menampilkan "(x Jika Batal)", tetapi "x" tetap membatalkan
        String rawAmount = InputUtil.input("Jumlah");
        if (rawAmount.equals(CANCEL)) return;

        OptionalLong amount = parsePositiveAmount(rawAmount);
        if (amount.isEmpty()) {
            presenter.showInvalidAmount();
            return;
        }

        presenter.showAddSuccess(useCase.addTransaction(description.get(), amount.getAsLong(), type));
    }

    private void searchTransaction() {
        System.out.println("[Cari Transaksi]");
        Optional<String> keyword = inputOrCancel("Kata Kunci");
        if (keyword.isEmpty()) return;

        presenter.showSearchResults(useCase.searchTransactions(keyword.get()), keyword.get());
    }

    private void sortTransaction() {
        System.out.println("[Urutkan Transaksi]");
        System.out.println("1. Jumlah (Terkecil)");
        System.out.println("2. Jumlah (Terbesar)");
        System.out.println("3. Pemasukan Dulu");
        System.out.println("4. Pengeluaran Dulu");
        System.out.println("x. Batal");

        String choice = InputUtil.input("Pilih");
        if (choice.equals(CANCEL)) return;

        Optional<SortOption> option = mapSortOption(choice);
        if (option.isEmpty()) {
            presenter.showInvalidSortOption();
            return;
        }

        presenter.showSortedTransactions(useCase.sortTransactions(option.get()));
    }

    private void removeTransaction() {
        System.out.println("[Hapus Transaksi]");
        Optional<String> rawId = inputOrCancel("ID Transaksi");
        if (rawId.isEmpty()) return;

        OptionalInt id = parseId(rawId.get());
        if (id.isEmpty()) {
            presenter.showInvalidId();
            return;
        }

        if (useCase.removeTransaction(id.getAsInt())) {
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

    /** Mengurai jumlah nominal; kosong jika bukan angka bulat atau tidak lebih dari nol. */
    private OptionalLong parsePositiveAmount(String value) {
        try {
            long amount = Long.parseLong(value);
            return amount > 0 ? OptionalLong.of(amount) : OptionalLong.empty();
        } catch (NumberFormatException e) {
            return OptionalLong.empty();
        }
    }

    private Optional<SortOption> mapSortOption(String choice) {
        return switch (choice) {
            case SORT_AMOUNT_ASC -> Optional.of(SortOption.AMOUNT_ASC);
            case SORT_AMOUNT_DESC -> Optional.of(SortOption.AMOUNT_DESC);
            case SORT_INCOME_FIRST -> Optional.of(SortOption.INCOME_FIRST);
            case SORT_EXPENSE_FIRST -> Optional.of(SortOption.EXPENSE_FIRST);
            default -> Optional.empty();
        };
    }
}
