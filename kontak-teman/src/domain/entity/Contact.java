package domain.entity;

import java.util.Objects;

/**
 * Entity inti yang merepresentasikan satu kontak teman.
 * Berada di layer domain: tidak bergantung pada layer lain dan bebas dari
 * urusan tampilan maupun penyimpanan.
 */
public class Contact {
    private final int id;
    private String name;
    private String phone;
    private String email;

    /**
     * Membuat kontak baru.
     *
     * @param id    ID unik kontak, tidak dapat diubah setelah dibuat
     * @param name  nama kontak
     * @param phone nomor telepon kontak
     * @param email alamat email kontak
     */
    public Contact(int id, String name, String phone, String email) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name tidak boleh null");
        this.phone = Objects.requireNonNull(phone, "phone tidak boleh null");
        this.email = Objects.requireNonNull(email, "email tidak boleh null");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void changeName(String name) {
        this.name = Objects.requireNonNull(name, "name tidak boleh null");
    }

    public void changePhone(String phone) {
        this.phone = Objects.requireNonNull(phone, "phone tidak boleh null");
    }

    public void changeEmail(String email) {
        this.email = Objects.requireNonNull(email, "email tidak boleh null");
    }
}
