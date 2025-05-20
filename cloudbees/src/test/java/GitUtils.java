
import java.io.*;
import java.util.List;

public class GitUtils {

    public static void runCommand(List<String> command, File directory) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(directory);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed: " + String.join(" ", command));
        }
    }

    public static void writeToFile(File file, String content, boolean append) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            writer.write(content);
            writer.newLine();
        }
    }

    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                deleteDirectory(f);
            }
        }
        dir.delete();
    }
}