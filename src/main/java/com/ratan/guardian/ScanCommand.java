package com.ratan.guardian;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import picocli.CommandLine.Command;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "scan", description = "Scans a pom.xml and checks for outdated dependencies.")
public class ScanCommand implements Callable<Integer> {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    public Integer call() throws Exception {
        ConsoleStyler.printBanner(); // <--- Print the cool logo

        Path pomPath = Paths.get("pom.xml");
        if (!Files.exists(pomPath)) {
            System.err.println(ConsoleStyler.RED + "❌ ERROR: No pom.xml file found." + ConsoleStyler.RESET);
            return 1;
        }

        System.out.println("🔍 Scanning local repository...");
        
        MavenXpp3Reader reader = new MavenXpp3Reader();
        List<Dependency> dependencies;
        try (FileReader fileReader = new FileReader(pomPath.toFile())) {
            Model model = reader.read(fileReader);
            dependencies = model.getDependencies();
        }

        if (dependencies.isEmpty()) {
            System.out.println("ℹ️ No dependencies found.");
            return 0;
        }

        // Print the Table Header
        ConsoleStyler.printHeader();

        int outdatedCount = 0;

        for (Dependency d : dependencies) {
            String artifactKey = d.getGroupId() + ":" + d.getArtifactId();
            String currentVersion = d.getVersion();
            
            // Fetch Latest
            String latestVersion = getLatestVersion(d.getGroupId(), d.getArtifactId());
            boolean isOutdated = !currentVersion.equals(latestVersion) && !latestVersion.equals("N/A");

            if (isOutdated) outdatedCount++;

            // Print the pretty row
            ConsoleStyler.printRow(artifactKey, currentVersion, latestVersion, isOutdated);
        }

        // Print the Footer Stats
        ConsoleStyler.printFooter(dependencies.size(), outdatedCount);
        
        return 0;
    }

    private String getLatestVersion(String groupId, String artifactId) {
        String url = String.format("https://search.maven.org/solrsearch/select?q=g:\"%s\"+AND+a:\"%s\"&core=gav&rows=1&wt=json",
                groupId, artifactId);

        Request request = new Request.Builder().url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) return "N/A";
            MavenSearchResponse searchResponse = gson.fromJson(response.body().string(), MavenSearchResponse.class);
            if (searchResponse.response != null && searchResponse.response.docs.length > 0) {
                return searchResponse.response.docs[0].v;
            }
        } catch (Exception e) { return "N/A"; }
        return "N/A";
    }

    private static class MavenSearchResponse { ResponseData response; }
    private static class ResponseData { Doc[] docs; }
    private static class Doc { String v; }
}