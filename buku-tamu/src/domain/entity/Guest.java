package domain.entity;

import java.util.Objects;

/**
 * Entitas inti yang merepresentasikan seorang tamu.
 * ID bersifat final karena identitas tamu tidak boleh berubah setelah dibuat.
 */
public class Guest {
    private final int id;
    private final String name;
    private final String purpose;

    public Guest(int id, String name, String purpose) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name tidak boleh null");
        this.purpose = Objects.requireNonNull(purpose, "purpose tidak boleh null");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
    }
}
