package adapter.presenter;

import domain.entity.Item;
import java.util.List;

/**
 * Mengubah hasil usecase menjadi teks yang ditampilkan ke layar.
 * Format tampilan sengaja ditempatkan di sini, bukan di entitas.
 */
public class ItemPresenter {

    private String format(Item item) {
        return String.format("%d | %s | %d | %s",
                item.getId(), item.getName(), item.getQuantity(), item.getCategory());
    }

    private void printList(List<Item> items, String header, String emptyMessage) {
        System.out.println(header);
        if (items.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        items.forEach(item -> System.out.println(format(item)));
    }

    public void showItems(List<Item> items) {
        printList(items, "Daftar Barang:", "- Data barang belum tersedia!");
    }

    public void showSearchResults(List<Item> items, String keyword) {
        printList(items, "Hasil Pencarian: \"" + keyword + "\"", "- Barang tidak ditemukan!");
    }

    public void showSortedItems(List<Item> items) {
        printList(items, "Daftar Barang (Terurut):", "- Data barang belum tersedia!");
    }

    public void showAddSuccess(Item item) {
        System.out.println("Berhasil menambah barang: " + format(item));
    }

    public void showUpdateSuccess() {
        System.out.println("Berhasil mengubah stok barang.");
    }

    public void showUpdateFailed(int id) {
        System.out.printf("[!] Gagal mengubah stok barang dengan ID: %d.%n", id);
    }

    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus barang.");
    }

    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus barang dengan ID: %d.%n", id);
    }

    public void showInvalidChoice() {
        System.out.println("[!] Pilihan tidak dimengerti.");
    }

    public void showInvalidId() {
        System.out.println("[!] ID tidak valid!");
    }

    public void showInvalidQuantity() {
        System.out.println("[!] Jumlah stok tidak valid!");
    }

    public void showInvalidSortOption() {
        System.out.println("[!] Pilihan tidak valid!");
    }
}
