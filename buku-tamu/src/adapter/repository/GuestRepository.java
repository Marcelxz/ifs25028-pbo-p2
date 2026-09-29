package adapter.repository;

import domain.entity.Guest;
import domain.repository.IGuestRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi in-memory dari {@link IGuestRepository}.
 * Bertanggung jawab membuat ID unik (auto-increment) saat menyimpan data.
 */
public class GuestRepository implements IGuestRepository {
    private final List<Guest> guests = new ArrayList<>();
    private int lastId = 0;

    @Override
    public List<Guest> findAll() {
        return new ArrayList<>(guests); // salinan defensif agar data internal tidak bocor
    }

    @Override
    public Optional<Guest> findById(int id) {
        return guests.stream()
                .filter(guest -> guest.getId() == id)
                .findFirst();
    }

    @Override
    public Guest save(String name, String purpose) {
        lastId++;
        Guest guest = new Guest(lastId, name, purpose);
        guests.add(guest);
        return guest;
    }

    @Override
    public boolean deleteById(int id) {
        return guests.removeIf(guest -> guest.getId() == id);
    }
}
