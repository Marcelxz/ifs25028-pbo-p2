package domain.repository;

import domain.entity.Contact;
import java.util.List;
import java.util.Optional;

/**
 * Port (kontrak) penyimpanan data kontak.
 * Interface ini berada di domain agar use case tidak bergantung pada
 * implementasi konkret maupun struktur data yang dipakai untuk menyimpan.
 */
public interface IContactRepository {

    /** Mengambil semua data kontak dari penyimpanan. */
    List<Contact> findAll();

    /** Mencari satu kontak berdasarkan ID. Mengembalikan empty jika tidak ditemukan. */
    Optional<Contact> findById(int id);

    /**
     * Menyimpan kontak baru. Implementasi bertanggung jawab memberi ID unik.
     *
     * @param name  nama kontak
     * @param phone nomor telepon kontak
     * @param email email kontak
     * @return kontak yang tersimpan (lengkap dengan ID)
     */
    Contact save(String name, String phone, String email);

    /** Menghapus kontak berdasarkan ID. Mengembalikan true jika berhasil. */
    boolean deleteById(int id);

    /** Menyimpan perubahan pada kontak yang sudah ada (dicocokkan berdasarkan ID). */
    void update(Contact contact);
}
