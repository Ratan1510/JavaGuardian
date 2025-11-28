package com.ratan.guardian;

public class ConsoleStyler {
    // ANSI Color Codes
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE_BOLD = "\033[1;37m";

    public static void printBanner() {
        System.out.println(CYAN + """
           ______                     _ _             
          / ____/___  __  ___  ____  (_) | ____  ____ 
         / / __/ __ \\/ / / / |/_/ / / / / / __ \\/ __ \\
        / /_/ / /_/ / /_/ />  </ /_/ / / / /_/ / / / /
        \\____/\\____/\\__,_/_/|_|\\__,_/_/_/\\__,_/_/ /_/ 
                                            v1.0
        """ + RESET);
    }

    public static void printHeader() {
        System.out.println(WHITE_BOLD + "---------------------------------------------------------------------------------" + RESET);
        System.out.printf(WHITE_BOLD + "| %-40s | %-12s | %-12s | %-8s |%n" + RESET, "DEPENDENCY", "CURRENT", "LATEST", "STATUS");
        System.out.println(WHITE_BOLD + "---------------------------------------------------------------------------------" + RESET);
    }

    public static void printRow(String name, String current, String latest, boolean isOutdated) {
        String status = isOutdated ? RED + "⚠ UPDATE" + RESET : GREEN + "✔ OK" + RESET;
        String colorCurrent = isOutdated ? YELLOW + current + RESET : current;
        String colorLatest = isOutdated ? GREEN + latest + RESET : latest;
        
        // Truncate name if it's too long
        if (name.length() > 38) name = name.substring(0, 35) + "...";

        System.out.printf("| %-40s | %-20s | %-20s | %-15s |%n", name, colorCurrent, colorLatest, status);
    }

    public static void printFooter(int total, int outdated) {
        System.out.println(WHITE_BOLD + "---------------------------------------------------------------------------------" + RESET);
        System.out.println();
        System.out.println("📊 PROJECT HEALTH SUMMARY:");
        System.out.println("   Total Dependencies: " + total);
        if (outdated == 0) {
            System.out.println("   Status: " + GREEN + "EXCELLENT. System Secure." + RESET);
        } else {
            System.out.println("   Status: " + RED + outdated + " Updates Required." + RESET);
            System.out.println("   Action: Run " + YELLOW + "guardian update" + RESET + " to patch.");
        }
        System.out.println();
    }
}