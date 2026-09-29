package domain.entity;

import java.util.Objects;

/**
 * Entitas inti yang merepresentasikan satu barang di inventaris.
 * Hanya jumlah stok yang dapat berubah; identitas, nama, dan kategori bersifat tetap.
 */
public class Item {
    private final int id;
    private final String name;
    private final String category;
    private int quantity;

    public Item(int id, String name, int quantity, String category) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name tidak boleh null");
        this.quantity = quantity;
        this.category = Objects.requireNonNull(category, "category tidak boleh null");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
