package usecase;

import domain.entity.Item;
import domain.entity.SortOption;
import domain.repository.IItemRepository;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Logika bisnis inventaris barang. Bebas I/O: tidak memanggil System.in maupun System.out.
 */
public class ItemUseCase {
    private final IItemRepository repository;

    public ItemUseCase(IItemRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository tidak boleh null");
    }

    public List<Item> getAllItems() {
        return repository.findAll();
    }

    public Item addItem(String name, int quantity, String category) {
        return repository.save(name, quantity, category);
    }

    public boolean removeItem(int id) {
        return repository.deleteById(id);
    }

    /**
     * Mengubah stok barang secara parsial.
     *
     * @param id          ID barang yang diubah
     * @param newQuantity stok baru, atau {@code null} jika stok lama dipertahankan
     * @return false jika barang dengan ID tersebut tidak ditemukan
     */
    public boolean updateStock(int id, Integer newQuantity) {
        Optional<Item> found = repository.findById(id);
        if (found.isEmpty()) {
            return false;
        }

        Item item = found.get();
        if (newQuantity != null) {
            item.changeQuantity(newQuantity);
        }
        repository.update(item);
        return true;
    }

    /** Mencari barang berdasarkan nama tanpa membedakan huruf besar/kecil. */
    public List<Item> searchItems(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
                .filter(item -> item.getName().toLowerCase(Locale.ROOT).contains(normalizedKeyword))
                .toList();
    }

    public List<Item> sortItems(SortOption option) {
        return repository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }
}
