package com.ratan.guardian;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import picocli.CommandLine.Command;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "update", description = "Updates outdated dependencies in pom.xml to their latest versions.")
public class UpdateCommand implements Callable<Integer> {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    public Integer call() throws Exception {
        Path pomPath = Paths.get("pom.xml");
        if (!Files.exists(pomPath)) {
            System.err.println("❌ ERROR: No pom.xml file found in the current directory.");
            return 1;
        }

        System.out.println("🔍 Scanning and updating dependencies...");
        
        
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model;
        try (FileReader fileReader = new FileReader(pomPath.toFile())) {
            model = reader.read(fileReader);
        }

        List<Dependency> dependencies = model.getDependencies();
        boolean changesMade = false;

        if (dependencies.isEmpty()) {
            System.out.println("ℹ️ No dependencies found to update.");
            return 0;
        }

        
        for (Dependency d : dependencies) {
            String currentVersion = d.getVersion();
            
            
            if (currentVersion == null) {
                System.out.printf("   Skipping %s:%s (Inherited version)%n", d.getGroupId(), d.getArtifactId());
                continue;
            }

            System.out.printf("Checking %s:%s... ", d.getGroupId(), d.getArtifactId());
            
            String latestVersion = getLatestVersion(d.getGroupId(), d.getArtifactId());

            if (!"N/A".equals(latestVersion) && !currentVersion.equals(latestVersion)) {
                System.out.printf("⏬ Update found: %s -> %s%n", currentVersion, latestVersion);
                
                
                d.setVersion(latestVersion);
                changesMade = true;
            } else {
                System.out.println("✅ Up to date.");
            }
        }

        
        if (changesMade) {
            System.out.println("💾 Writing changes to pom.xml...");
            MavenXpp3Writer writer = new MavenXpp3Writer();
            try (FileWriter fileWriter = new FileWriter(pomPath.toFile())) {
                writer.write(fileWriter, model);
            }
            System.out.println("🚀 Success! All dependencies updated.");
        } else {
            System.out.println("✨ Everything is already up to date. No changes made.");
        }

        return 0;
    }

    
    private String getLatestVersion(String groupId, String artifactId) {
        String url = String.format("https://search.maven.org/solrsearch/select?q=g:\"%s\"+AND+a:\"%s\"&core=gav&rows=1&wt=json",
                groupId, artifactId);

        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) return "N/A";

            String responseBody = response.body().string();
            MavenSearchResponse searchResponse = gson.fromJson(responseBody, MavenSearchResponse.class);
            
            if (searchResponse.response != null && searchResponse.response.docs.length > 0) {
                return searchResponse.response.docs[0].v;
            }
        } catch (Exception e) {
            return "N/A";
        }
        return "N/A";
    }

    
    private static class MavenSearchResponse { ResponseData response; }
    private static class ResponseData { Doc[] docs; }
    private static class Doc { String v; }
}