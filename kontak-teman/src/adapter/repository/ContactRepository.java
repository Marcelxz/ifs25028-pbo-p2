package adapter.repository;

import domain.entity.Contact;
import domain.repository.IContactRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi repository menggunakan penyimpanan in-memory berbasis {@link List}.
 * Berada di layer adapter: mengimplementasikan port dari domain sekaligus
 * menyembunyikan detail struktur data dari layer di atasnya.
 */
public class ContactRepository implements IContactRepository {
    private final List<Contact> contacts = new ArrayList<>();

    /** ID terakhir yang diberikan; bertambah setiap kali kontak baru disimpan. */
    private int lastId = 0;

    @Override
    public List<Contact> findAll() {
        return new ArrayList<>(contacts); // salinan defensif agar data internal tidak bocor
    }

    @Override
    public Optional<Contact> findById(int id) {
        return contacts.stream()
                .filter(contact -> contact.getId() == id)
                .findFirst();
    }

    @Override
    public Contact save(String name, String phone, String email) {
        Contact contact = new Contact(nextId(), name, phone, email);
        contacts.add(contact);
        return contact;
    }

    @Override
    public boolean deleteById(int id) {
        return contacts.removeIf(contact -> contact.getId() == id);
    }

    @Override
    public void update(Contact contact) {
        for (int index = 0; index < contacts.size(); index++) {
            if (contacts.get(index).getId() == contact.getId()) {
                contacts.set(index, contact);
                return;
            }
        }
    }

    /** Menghasilkan ID unik berikutnya. */
    private int nextId() {
        lastId++;
        return lastId;
    }
}
