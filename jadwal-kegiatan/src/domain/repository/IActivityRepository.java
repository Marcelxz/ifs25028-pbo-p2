package domain.repository;

import domain.entity.Activity;
import java.util.List;
import java.util.Optional;

/**
 * Port (kontrak) penyimpanan data kegiatan.
 * Interface ini berada di domain agar use case tidak bergantung pada
 * implementasi konkret maupun struktur data yang dipakai untuk menyimpan.
 */
public interface IActivityRepository {

    /** Mengambil semua data kegiatan dari penyimpanan. */
    List<Activity> findAll();

    /** Mencari satu kegiatan berdasarkan ID. Mengembalikan empty jika tidak ditemukan. */
    Optional<Activity> findById(int id);

    /**
     * Menyimpan kegiatan baru. Implementasi bertanggung jawab memberi ID unik.
     *
     * @param title judul kegiatan
     * @param day   hari kegiatan
     * @param time  waktu kegiatan
     * @return kegiatan yang tersimpan (lengkap dengan ID)
     */
    Activity save(String title, String day, String time);

    /** Menghapus kegiatan berdasarkan ID. Mengembalikan true jika berhasil. */
    boolean deleteById(int id);

    /** Menyimpan perubahan pada kegiatan yang sudah ada (dicocokkan berdasarkan ID). */
    void update(Activity activity);
}
