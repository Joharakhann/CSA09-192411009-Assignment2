package TASK7;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Song {
    final String id;
    final String title;
    final String artist;

    Song(String id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}

// --- ORIGINAL SOLUTION: Plain ArrayList ---
class OriginalPlaylist {
    private final List<Song> playlist = new ArrayList<>();

    void addSong(Song song) {
        playlist.add(song);
    }

    void insertFront(Song song) {
        playlist.add(0, song);
    }

    Song lookupById(String id) {
        // Bottleneck: Linear O(N) scan
        for (Song s : playlist) {
            if (s.id.equals(id)) return s;
        }
        return null;
    }
}

// --- REDESIGNED SOLUTION: Composite LinkedHashMap + Deque ---
class RedesignedPlaylist {
    private final Map<String, Song> songMap = new HashMap<>();
    private final Deque<Song> queue = new ArrayDeque<>();

    void addSong(Song song) {
        songMap.put(song.id, song);
        queue.addLast(song);
    }

    void insertFront(Song song) {
        songMap.put(song.id, song);
        queue.addFirst(song);
    }

    Song lookupById(String id) {
        // Optimized: Constant time O(1) direct lookup
        return songMap.get(id);
    }
}

public class Day4_DesignChallenge {

    public static void benchmark(int playlistSize) {
        System.out.println("=================================================");
        System.out.println("DESIGN CHALLENGE BENCHMARK (Size: " + playlistSize + " Songs)");
        System.out.println("=================================================");

        OriginalPlaylist original = new OriginalPlaylist();
        RedesignedPlaylist redesigned = new RedesignedPlaylist();

        // 1. Population
        for (int i = 0; i < playlistSize; i++) {
            Song s = new Song("S" + i, "Song_" + i, "Artist_" + i);
            original.addSong(s);
            redesigned.addSong(s);
        }

        // 2. Lookup Performance Test (10,000 random ID lookups)
        int lookupCount = 10_000;
        String targetId = "S" + (playlistSize - 100);

        long startOrigLookup = System.nanoTime();
        for (int i = 0; i < lookupCount; i++) {
            original.lookupById(targetId);
        }
        long timeOrigLookup = System.nanoTime() - startOrigLookup;

        long startRedesLookup = System.nanoTime();
        for (int i = 0; i < lookupCount; i++) {
            redesigned.lookupById(targetId);
        }
        long timeRedesLookup = System.nanoTime() - startRedesLookup;

        System.out.printf("Lookup (10k ops)    : Original = %7.2f ms | Redesigned = %7.2f ms\n",
                timeOrigLookup / 1e6, timeRedesLookup / 1e6);

        // 3. Front Insertion Test (1,000 insertions)
        int insertCount = 1000;
        long startOrigInsert = System.nanoTime();
        for (int i = 0; i < insertCount; i++) {
            original.insertFront(new Song("INS_" + i, "Insert", "Artist"));
        }
        long timeOrigInsert = System.nanoTime() - startOrigInsert;

        long startRedesInsert = System.nanoTime();
        for (int i = 0; i < insertCount; i++) {
            redesigned.insertFront(new Song("INS_" + i, "Insert", "Artist"));
        }
        long timeRedesInsert = System.nanoTime() - startRedesInsert;

        System.out.printf("Front Insert (1k ops): Original = %7.2f ms | Redesigned = %7.2f ms\n\n",
                timeOrigInsert / 1e6, timeRedesInsert / 1e6);
    }

    public static void main(String[] args) {
        System.out.println("=== Q7: Day-4 Design Challenge - Diagnose, Redesign & Validate ===");
        benchmark(1_000);   // Small Scale
        benchmark(10_000);  // Medium Scale
        benchmark(50_000);  // Large Scale
    }
}