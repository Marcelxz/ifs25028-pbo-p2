package adapter.repository;

import domain.entity.Item;
import domain.repository.IItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi in-memory dari {@link IItemRepository}.
 * Bertanggung jawab membuat ID unik (auto-increment) saat menyimpan data.
 */
public class ItemRepository implements IItemRepository {
    private final List<Item> items = new ArrayList<>();
    private int lastId = 0;

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items); // salinan defensif agar data internal tidak bocor
    }

    @Override
    public Optional<Item> findById(int id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst();
    }

    @Override
    public Item save(String name, int quantity, String category) {
        lastId++;
        Item item = new Item(lastId, name, quantity, category);
        items.add(item);
        return item;
    }

    @Override
    public boolean deleteById(int id) {
        return items.removeIf(item -> item.getId() == id);
    }

    @Override
    public void update(Item item) {
        for (int index = 0; index < items.size(); index++) {
            if (items.get(index).getId() == item.getId()) {
                items.set(index, item);
                return;
            }
        }
    }
}
