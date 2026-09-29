package domain.repository;

import domain.entity.Guest;
import java.util.List;
import java.util.Optional;

/**
 * Kontrak (port) penyimpanan data tamu.
 * Layer usecase hanya mengenal interface ini, bukan implementasinya.
 */
public interface IGuestRepository {

    /** Mengambil seluruh tamu yang tersimpan. */
    List<Guest> findAll();

    /** Mencari tamu berdasarkan ID; kosong jika tidak ditemukan. */
    Optional<Guest> findById(int id);

    /** Menyimpan tamu baru dan mengembalikannya lengkap dengan ID yang dibuat. */
    Guest save(String name, String purpose);

    /** Menghapus tamu berdasarkan ID; true jika ada data yang terhapus. */
    boolean deleteById(int id);
}
