

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Feature("Add New File via CLI")
public class GitAddFileTest {

    @Test(description = "Clone repo and add new file")
    public void testAddNewFile() throws Exception {
        String repoUrl = "https://github.com/deepalib0511/cloudbeesAssignment.git";
        String token = System.getenv("GITHUB_TOKEN");
        String username = "deepalib0511";
        String cloneUrl = repoUrl.replace("https://", "https://" + username + ":" + token + "@");
        String localDir = "RepoClonedFromGitHub";
        String fileName = "TestFile.txt";
        String content = "This is a new file.";

        File dir = new File(localDir);
        GitUtils.deleteDirectory(dir);

        // Step 1: Clone the repo
        GitUtils.runCommand(Arrays.asList("git", "clone", cloneUrl, localDir), new File("."));
        File clonedRepoDir = new File(localDir);
        Assert.assertTrue(clonedRepoDir.exists(), "Cloning failed! Repo directory not found.");



        // Step 2: Create a new file
        File newFile = new File(dir, fileName);
        GitUtils.writeToFile(newFile, content, false);
        Assert.assertTrue(newFile.exists(), "File not created: " + fileName);

        // Step 3: git add, commit, push
        GitUtils.runCommand(Arrays.asList("git", "add", fileName), dir);
        GitUtils.runCommand(Arrays.asList("git", "commit", "-m", "Add " + fileName), dir);
        GitUtils.runCommand(Arrays.asList("git", "push"), dir);
    }
}