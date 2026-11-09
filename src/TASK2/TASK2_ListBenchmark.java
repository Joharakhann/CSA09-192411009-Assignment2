package TASK2;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

class Song {
    private final String title;
    private final String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
}

public class TASK2_ListBenchmark {

    private static void benchmarkOperations(int size) {
        System.out.println("=================================================");
        System.out.println("PLAYLIST SIZE: " + size + " Songs");
        System.out.println("=================================================");

        // Setup lists
        List<Song> arrayList = new ArrayList<>();
        List<Song> linkedList = new LinkedList<>();
        ArrayDeque<Song> arrayDeque = new ArrayDeque<>();

        Song sample = new Song("Test Song", "Test Artist");

        // 1. Add at End
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) arrayList.add(new Song("Song " + i, "Artist"));
        long timeArrayListAddEnd = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < size; i++) linkedList.add(new Song("Song " + i, "Artist"));
        long timeLinkedListAddEnd = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < size; i++) arrayDeque.addLast(new Song("Song " + i, "Artist"));
        long timeArrayDequeAddEnd = System.nanoTime() - start;

        System.out.printf("Add End  : ArrayList = %7.2f ms | LinkedList = %7.2f ms | ArrayDeque = %7.2f ms\n",
                timeArrayListAddEnd / 1e6, timeLinkedListAddEnd / 1e6, timeArrayDequeAddEnd / 1e6);

        // 2. Access k-th element (Random Access across 10,000 queries)
        int iterations = 10_000;
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Song s = arrayList.get(size / 2);
        }
        long timeArrayListGet = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Song s = linkedList.get(size / 2);
        }
        long timeLinkedListGet = System.nanoTime() - start;

        System.out.printf("Get Middle (10k ops): ArrayList = %7.2f ms | LinkedList = %7.2f ms\n",
                timeArrayListGet / 1e6, timeLinkedListGet / 1e6);

        // 3. Insert Front (1,000 insertions)
        int insertCount = 1000;
        start = System.nanoTime();
        for (int i = 0; i < insertCount; i++) arrayList.add(0, sample);
        long timeArrayListAddFront = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < insertCount; i++) linkedList.add(0, sample);
        long timeLinkedListAddFront = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < insertCount; i++) arrayDeque.addFirst(sample);
        long timeArrayDequeAddFront = System.nanoTime() - start;

        System.out.printf("Insert Front (1k ops): ArrayList = %7.2f ms | LinkedList = %7.2f ms | ArrayDeque = %7.2f ms\n",
                timeArrayListAddFront / 1e6, timeLinkedListAddFront / 1e6, timeArrayDequeAddFront / 1e6);

        // 4. Remove Front (1,000 removals)
        start = System.nanoTime();
        for (int i = 0; i < insertCount; i++) arrayList.remove(0);
        long timeArrayListRemoveFront = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < insertCount; i++) linkedList.remove(0);
        long timeLinkedListRemoveFront = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < insertCount; i++) arrayDeque.removeFirst();
        long timeArrayDequeRemoveFront = System.nanoTime() - start;

        System.out.printf("Remove Front (1k ops): ArrayList = %7.2f ms | LinkedList = %7.2f ms | ArrayDeque = %7.2f ms\n\n",
                timeArrayListRemoveFront / 1e6, timeLinkedListRemoveFront / 1e6, timeArrayDequeRemoveFront / 1e6);
    }

    public static void main(String[] args) {
        System.out.println("=== Q2: ArrayList vs LinkedList vs ArrayDeque Benchmark ===");
        benchmarkOperations(1_000);   // Small Playlist
        benchmarkOperations(10_000);  // Medium Playlist
        benchmarkOperations(50_000);  // Large Playlist
    }
}