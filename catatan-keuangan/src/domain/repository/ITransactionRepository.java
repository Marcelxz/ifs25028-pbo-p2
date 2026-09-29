package domain.repository;

import domain.entity.Transaction;
import domain.entity.TransactionType;
import java.util.List;
import java.util.Optional;

/**
 * Kontrak (port) penyimpanan transaksi.
 * Layer usecase hanya mengenal interface ini, bukan implementasinya.
 */
public interface ITransactionRepository {

    /** Mengambil seluruh transaksi yang tersimpan. */
    List<Transaction> findAll();

    /** Mencari transaksi berdasarkan ID; kosong jika tidak ditemukan. */
    Optional<Transaction> findById(int id);

    /** Menyimpan transaksi baru dan mengembalikannya lengkap dengan ID yang dibuat. */
    Transaction save(String description, long amount, TransactionType type);

    /** Menghapus transaksi berdasarkan ID; true jika ada data yang terhapus. */
    boolean deleteById(int id);
}
