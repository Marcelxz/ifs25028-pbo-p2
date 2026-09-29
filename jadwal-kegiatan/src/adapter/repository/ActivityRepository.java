package adapter.repository;

import domain.entity.Activity;
import domain.repository.IActivityRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi repository menggunakan penyimpanan in-memory berbasis {@link List}.
 * Berada di layer adapter: mengimplementasikan port dari domain sekaligus
 * menyembunyikan detail struktur data dari layer di atasnya.
 */
public class ActivityRepository implements IActivityRepository {
    private final List<Activity> activities = new ArrayList<>();

    /** ID terakhir yang diberikan; bertambah setiap kali kegiatan baru disimpan. */
    private int lastId = 0;

    @Override
    public List<Activity> findAll() {
        return new ArrayList<>(activities); // salinan defensif agar data internal tidak bocor
    }

    @Override
    public Optional<Activity> findById(int id) {
        return activities.stream()
                .filter(activity -> activity.getId() == id)
                .findFirst();
    }

    @Override
    public Activity save(String title, String day, String time) {
        Activity activity = new Activity(nextId(), title, day, time);
        activities.add(activity);
        return activity;
    }

    @Override
    public boolean deleteById(int id) {
        return activities.removeIf(activity -> activity.getId() == id);
    }

    @Override
    public void update(Activity activity) {
        for (int index = 0; index < activities.size(); index++) {
            if (activities.get(index).getId() == activity.getId()) {
                activities.set(index, activity);
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
