package usecase;

import domain.entity.Guest;
import domain.repository.IGuestRepository;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Logika bisnis buku tamu. Bebas I/O: tidak memanggil System.in maupun System.out.
 */
public class GuestUseCase {
    private final IGuestRepository repository;

    public GuestUseCase(IGuestRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository tidak boleh null");
    }

    public List<Guest> getAllGuests() {
        return repository.findAll();
    }

    public Guest registerGuest(String name, String purpose) {
        return repository.save(name, purpose);
    }

    public boolean removeGuest(int id) {
        return repository.deleteById(id);
    }

    /** Mencari tamu berdasarkan nama tanpa membedakan huruf besar/kecil. */
    public List<Guest> searchGuests(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
                .filter(guest -> guest.getName().toLowerCase(Locale.ROOT).contains(normalizedKeyword))
                .toList();
    }
}
