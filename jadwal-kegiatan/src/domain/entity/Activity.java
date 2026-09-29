package domain.entity;

import java.util.Objects;

/**
 * Entity inti yang merepresentasikan satu kegiatan.
 * Berada di layer domain: tidak bergantung pada layer lain dan bebas dari
 * urusan tampilan maupun penyimpanan.
 */
public class Activity {
    private final int id;
    private String title;
    private String day;
    private String time;

    /**
     * Membuat kegiatan baru.
     *
     * @param id    ID unik kegiatan, tidak dapat diubah setelah dibuat
     * @param title judul kegiatan
     * @param day   hari pelaksanaan (contoh: Senin)
     * @param time  waktu pelaksanaan (format HH:mm, contoh: 08:00)
     */
    public Activity(int id, String title, String day, String time) {
        this.id = id;
        this.title = Objects.requireNonNull(title, "title tidak boleh null");
        this.day = Objects.requireNonNull(day, "day tidak boleh null");
        this.time = Objects.requireNonNull(time, "time tidak boleh null");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public void changeTitle(String title) {
        this.title = Objects.requireNonNull(title, "title tidak boleh null");
    }

    public void changeDay(String day) {
        this.day = Objects.requireNonNull(day, "day tidak boleh null");
    }

    public void changeTime(String time) {
        this.time = Objects.requireNonNull(time, "time tidak boleh null");
    }
}
