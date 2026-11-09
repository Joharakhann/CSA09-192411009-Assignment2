package TASK1;

import java.util.HashMap;
import java.util.Map;

public class TASK1_StringBenchmark {

    // Helper method to measure heap memory usage
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    // Workload generator simulating railway ticket fields
    private static Map<String, String> generateTicketFields(int fieldCount) {
        Map<String, String> fields = new HashMap<>();
        fields.put("PNR", "8492019482");
        fields.put("TrainNo", "12624");
        fields.put("TrainName", "Chennai Mail");
        fields.put("From", "MAS");
        fields.put("To", "SBC");
        fields.put("Class", "3A");
        fields.put("Berth", "72-SUB");
        fields.put("Fare", "1250.00");
        fields.put("Passenger", "Fathi Muthu Johara M");
        fields.put("Status", "CNF");

        for (int i = 10; i < fieldCount; i++) {
            fields.put("ExtraField_" + i, "Value_" + i);
        }
        return fields;
    }

    // 1. String Concatenation (+)
    private static String buildTicketString(Map<String, String> fields, int tickets) {
        String result = "";
        for (int t = 0; t < tickets; t++) {
            String ticket = "";
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                ticket = ticket + entry.getKey() + ":" + entry.getValue() + " | ";
            }
            result += ticket;
        }
        return result;
    }

    // 2. StringBuilder (Non-thread-safe, fast)
    private static String buildTicketStringBuilder(Map<String, String> fields, int tickets) {
        StringBuilder result = new StringBuilder();
        for (int t = 0; t < tickets; t++) {
            StringBuilder ticket = new StringBuilder();
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                ticket.append(entry.getKey()).append(":").append(entry.getValue()).append(" | ");
            }
            result.append(ticket);
        }
        return result.toString();
    }

    // 3. StringBuffer (Thread-safe, synchronized)
    private static String buildTicketStringBuffer(Map<String, String> fields, int tickets) {
        StringBuffer result = new StringBuffer();
        for (int t = 0; t < tickets; t++) {
            StringBuffer ticket = new StringBuffer();
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                ticket.append(entry.getKey()).append(":").append(entry.getValue()).append(" | ");
            }
            result.append(ticket);
        }
        return result.toString();
    }

    public static void runWorkload(String workloadName, int ticketCount, int fieldCount) {
        System.out.println("=================================================");
        System.out.println("WORKLOAD: " + workloadName + " (" + ticketCount + " tickets, " + fieldCount + " fields/ticket)");
        System.out.println("=================================================");

        Map<String, String> fields = generateTicketFields(fieldCount);

        // --- Test 1: String (+) ---
        System.gc();
        long memBefore1 = getUsedMemory();
        long startTime1 = System.nanoTime();
        buildTicketString(fields, ticketCount);
        long endTime1 = System.nanoTime();
        long memAfter1 = getUsedMemory();
        double timeMs1 = (endTime1 - startTime1) / 1_000_000.0;
        System.out.printf("1. String (+)       : Time = %8.2f ms | Memory Delta = %6.2f MB\n", 
                timeMs1, (memAfter1 - memBefore1) / (1024.0 * 1024.0));

        // --- Test 2: StringBuilder ---
        System.gc();
        long memBefore2 = getUsedMemory();
        long startTime2 = System.nanoTime();
        buildTicketStringBuilder(fields, ticketCount);
        long endTime2 = System.nanoTime();
        long memAfter2 = getUsedMemory();
        double timeMs2 = (endTime2 - startTime2) / 1_000_000.0;
        System.out.printf("2. StringBuilder    : Time = %8.2f ms | Memory Delta = %6.2f MB\n", 
                timeMs2, (memAfter2 - memBefore2) / (1024.0 * 1024.0));

        // --- Test 3: StringBuffer ---
        System.gc();
        long memBefore3 = getUsedMemory();
        long startTime3 = System.nanoTime();
        buildTicketStringBuffer(fields, ticketCount);
        long endTime3 = System.nanoTime();
        long memAfter3 = getUsedMemory();
        double timeMs3 = (endTime3 - startTime3) / 1_000_000.0;
        System.out.printf("3. StringBuffer     : Time = %8.2f ms | Memory Delta = %6.2f MB\n", 
                timeMs3, (memAfter3 - memBefore3) / (1024.0 * 1024.0));
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== Q1: String vs StringBuilder vs StringBuffer Benchmark ===");
        
        // Warmup JVM
        generateTicketFields(10);

        // Workload 1: Short single ticket (Light load)
        runWorkload("Workload 1 (Short Ticket)", 1, 10);

        // Workload 2: High volume batch (1,000 tickets with 50 fields each)
        runWorkload("Workload 2 (Heavy Load)", 1000, 50);

        // Workload 3: Custom medium load (5,000 tickets with 20 fields each)
        runWorkload("Workload 3 (Custom Medium Load)", 5000, 20);
    }
}