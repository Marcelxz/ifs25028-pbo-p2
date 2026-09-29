package domain.entity;

import java.util.Comparator;

/**
 * Pilihan pengurutan barang. Setiap opsi membawa {@link Comparator}-nya sendiri.
 * Pengurutan nama tidak membedakan huruf besar/kecil.
 */
public enum SortOption {
    NAME_ASC(Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER)),
    NAME_DESC(Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER).reversed()),
    QTY_ASC(Comparator.comparingInt(Item::getQuantity)),
    QTY_DESC(Comparator.comparingInt(Item::getQuantity).reversed());

    private final Comparator<Item> comparator;

    SortOption(Comparator<Item> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Item> comparator() {
        return comparator;
    }
}
