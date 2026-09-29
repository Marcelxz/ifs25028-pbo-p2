package adapter.repository;

import domain.entity.Transaction;
import domain.entity.TransactionType;
import domain.repository.ITransactionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi in-memory dari {@link ITransactionRepository}.
 * Bertanggung jawab membuat ID unik (auto-increment) saat menyimpan data.
 */
public class TransactionRepository implements ITransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();
    private int lastId = 0;

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions); // salinan defensif agar data internal tidak bocor
    }

    @Override
    public Optional<Transaction> findById(int id) {
        return transactions.stream()
                .filter(transaction -> transaction.getId() == id)
                .findFirst();
    }

    @Override
    public Transaction save(String description, long amount, TransactionType type) {
        lastId++;
        Transaction transaction = new Transaction(lastId, description, amount, type);
        transactions.add(transaction);
        return transaction;
    }

    @Override
    public boolean deleteById(int id) {
        return transactions.removeIf(transaction -> transaction.getId() == id);
    }
}
