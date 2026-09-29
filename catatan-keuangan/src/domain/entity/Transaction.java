package domain.entity;

import java.util.Objects;

/**
 * Entitas inti yang merepresentasikan satu transaksi keuangan.
 * Seluruh field bersifat final karena transaksi tidak dapat diubah setelah dicatat.
 */
public class Transaction {
    private final int id;
    private final String description;
    private final long amount;
    private final TransactionType type;

    public Transaction(int id, String description, long amount, TransactionType type) {
        this.id = id;
        this.description = Objects.requireNonNull(description, "description tidak boleh null");
        this.amount = amount;
        this.type = Objects.requireNonNull(type, "type tidak boleh null");
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }
}
