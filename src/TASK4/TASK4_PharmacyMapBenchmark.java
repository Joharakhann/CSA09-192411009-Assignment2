package TASK4;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

class Medicine {
    private final String code;
    private final String name;
    private final int stock;

    public Medicine(String code, String name, int stock) {
        this.code = code;
        this.name = name;
        this.stock = stock;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getStock() { return stock; }

    @Override
    public String toString() {
        return code + ":" + name + "(" + stock + ")";
    }
}

public class TASK4_PharmacyMapBenchmark {

    private static void benchmarkMap(String mapName, Map<String, Medicine> map, int datasetSize) {
        // Populate Map
        for (int i = datasetSize; i >= 1; i--) {
            String code = String.format("MED%05d", i);
            map.put(code, new Medicine(code, "Medicine_" + i, 100 + i));
        }

        // Benchmark 100,000 lookups
        int lookups = 100_000;
        long startLookup = System.nanoTime();
        for (int i = 0; i < lookups; i++) {
            String searchCode = String.format("MED%05d", (i % datasetSize) + 1);
            Medicine m = map.get(searchCode);
        }
        long lookupTime = System.nanoTime() - startLookup;

        // Benchmark Iteration / Report Generation
        long startIter = System.nanoTime();
        int count = 0;
        for (Map.Entry<String, Medicine> entry : map.entrySet()) {
            count++;
        }
        long iterTime = System.nanoTime() - startIter;

        System.out.printf("%-15s | 100k Lookups: %7.2f ms | Report Iteration: %6.2f ms | Entries: %d\n",
                mapName, lookupTime / 1e6, iterTime / 1e6, count);
    }

    public static void runBenchmarkForSize(int size) {
        System.out.println("=========================================================");
        System.out.println("DATASET SIZE: " + size + " Medicines");
        System.out.println("=========================================================");

        benchmarkMap("HashMap", new HashMap<>(), size);
        benchmarkMap("TreeMap", new TreeMap<>(), size);
        benchmarkMap("LinkedHashMap", new LinkedHashMap<>(), size);
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== Q4: Pharmacy Map Lookup & Report Generation Benchmark ===");
        runBenchmarkForSize(500);    // Small Pharmacy
        runBenchmarkForSize(5_000);  // Regional Hospital
        runBenchmarkForSize(20_000); // Central Supply Depot
    }
}