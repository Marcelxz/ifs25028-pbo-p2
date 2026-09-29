package domain.repository;

import domain.entity.Item;
import java.util.List;
import java.util.Optional;

/**
 * Kontrak (port) penyimpanan barang.
 * Layer usecase hanya mengenal interface ini, bukan implementasinya.
 */
public interface IItemRepository {

    /** Mengambil seluruh barang yang tersimpan. */
    List<Item> findAll();

    /** Mencari barang berdasarkan ID; kosong jika tidak ditemukan. */
    Optional<Item> findById(int id);

    /** Menyimpan barang baru dan mengembalikannya lengkap dengan ID yang dibuat. */
    Item save(String name, int quantity, String category);

    /** Menghapus barang berdasarkan ID; true jika ada data yang terhapus. */
    boolean deleteById(int id);

    /** Menyimpan perubahan pada barang yang sudah ada (dicocokkan berdasarkan ID). */
    void update(Item item);
}
