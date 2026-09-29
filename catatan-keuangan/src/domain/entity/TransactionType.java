package domain.entity;

/**
 * Jenis transaksi. Urutan deklarasi (INCOME lalu EXPENSE) dipakai
 * oleh {@link SortOption} sebagai urutan alami.
 */
public enum TransactionType {
    INCOME("Pemasukan"),
    EXPENSE("Pengeluaran");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
