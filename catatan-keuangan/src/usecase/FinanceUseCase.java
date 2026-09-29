package usecase;

import domain.entity.SortOption;
import domain.entity.Transaction;
import domain.entity.TransactionType;
import domain.repository.ITransactionRepository;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Logika bisnis catatan keuangan. Bebas I/O: tidak memanggil System.in maupun System.out.
 */
public class FinanceUseCase {
    private final ITransactionRepository repository;

    public FinanceUseCase(ITransactionRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository tidak boleh null");
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    public Transaction addTransaction(String description, long amount, TransactionType type) {
        return repository.save(description, amount, type);
    }

    public boolean removeTransaction(int id) {
        return repository.deleteById(id);
    }

    /** Mencari transaksi berdasarkan keterangan tanpa membedakan huruf besar/kecil. */
    public List<Transaction> searchTransactions(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
                .filter(transaction ->
                        transaction.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword))
                .toList();
    }

    public List<Transaction> sortTransactions(SortOption option) {
        return repository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }

    /** Saldo berjalan: total pemasukan dikurangi total pengeluaran. */
    public long getBalance() {
        return repository.findAll().stream()
                .mapToLong(this::signedAmount)
                .sum();
    }

    private long signedAmount(Transaction transaction) {
        return transaction.getType() == TransactionType.INCOME
                ? transaction.getAmount()
                : -transaction.getAmount();
    }
}
