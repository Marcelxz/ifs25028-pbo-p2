package adapter.presenter;

import domain.entity.Guest;
import java.util.List;

/**
 * Mengubah hasil usecase menjadi teks yang ditampilkan ke layar.
 * Format tampilan sengaja ditempatkan di sini, bukan di entitas.
 */
public class GuestPresenter {

    private String format(Guest guest) {
        return String.format("%d | %s | %s", guest.getId(), guest.getName(), guest.getPurpose());
    }

    private void printList(List<Guest> guests, String header, String emptyMessage) {
        System.out.println(header);
        if (guests.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        guests.forEach(guest -> System.out.println(format(guest)));
    }

    public void showGuests(List<Guest> guests) {
        printList(guests, "Daftar Tamu:", "- Data tamu belum tersedia!");
    }

    public void showSearchResults(List<Guest> guests, String keyword) {
        printList(guests, "Hasil Pencarian: \"" + keyword + "\"", "- Tamu tidak ditemukan!");
    }

    public void showAddSuccess(Guest guest) {
        System.out.printf("Berhasil mendaftarkan tamu: %s%n", format(guest));
    }

    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus tamu.");
    }

    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus tamu dengan ID: %d.%n", id);
    }

    public void showInvalidChoice() {
        System.out.println("[!] Pilihan tidak dimengerti.");
    }

    public void showInvalidId() {
        System.out.println("[!] ID tidak valid!");
    }
}
