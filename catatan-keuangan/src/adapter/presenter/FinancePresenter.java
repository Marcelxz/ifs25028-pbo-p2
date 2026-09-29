package adapter.presenter;

import domain.entity.Transaction;
import java.util.List;

/**
 * Mengubah hasil usecase menjadi teks yang ditampilkan ke layar.
 * Format tampilan sengaja ditempatkan di sini, bukan di entitas.
 */
public class FinancePresenter {

    private String format(Transaction transaction) {
        return String.format("%d | %s | Rp %d | %s",
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType().getLabel());
    }

    private void printList(List<Transaction> transactions, String header, String emptyMessage) {
        System.out.println(header);
        if (transactions.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        transactions.forEach(transaction -> System.out.println(format(transaction)));
    }

    /** Daftar transaksi beserta saldo; dipakai di tampilan menu utama. */
    public void showTransactions(List<Transaction> transactions, long balance) {
        printList(transactions, "Daftar Transaksi:", "- Belum ada transaksi!");
        System.out.println("Saldo: Rp " + balance);
    }

    public void showSearchResults(List<Transaction> transactions, String keyword) {
        printList(transactions, "Hasil Pencarian: \"" + keyword + "\"", "- Transaksi tidak ditemukan!");
    }

    public void showSortedTransactions(List<Transaction> transactions) {
        printList(transactions, "Daftar Transaksi (Terurut):", "- Data belum tersedia!");
    }

    /** Khusus untuk menu "Lihat Saldo". */
    public void showBalance(long balance) {
        System.out.println("Saldo saat ini: Rp " + balance);
    }

    public void showAddSuccess(Transaction transaction) {
        System.out.printf("Berhasil menambah transaksi: %s%n", format(transaction));
    }

    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus transaksi.");
    }

    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus transaksi dengan ID: %d.%n", id);
    }

    public void showInvalidChoice() {
        System.out.println("[!] Pilihan tidak dimengerti.");
    }

    public void showInvalidId() {
        System.out.println("[!] ID tidak valid!");
    }

    public void showInvalidAmount() {
        System.out.println("[!] Jumlah tidak valid!");
    }

    public void showInvalidSortOption() {
        System.out.println("[!] Pilihan tidak valid!");
    }
}
