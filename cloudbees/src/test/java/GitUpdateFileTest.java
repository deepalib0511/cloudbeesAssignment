
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;

@Epic("Git CLI Automation")
@Feature("Update File via CLI")
public class GitUpdateFileTest {

    @Test(description = "Clone repo and update existing file")
    public void testUpdateFile() throws Exception {
        String repoUrl = "https://github.com/deepalib0511/cloudbeesAssignment.git";
        String token = "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";
        String username = "deepalib0511";
        String cloneUrl = repoUrl.replace("https://", "https://" + username + ":" + token + "@");
        String localDir = "RepoClonedFromGitHub";
        String fileName = "TestFile.txt";
        String content = "\nAppended content at " + System.currentTimeMillis();

        File dir = new File(localDir);
        GitUtils.deleteDirectory(dir);

        // Step 1: Clone the repo
        GitUtils.runCommand(Arrays.asList("git", "clone", cloneUrl, localDir), new File("."));
        File clonedRepoDir = new File(localDir);
        Assert.assertTrue(clonedRepoDir.exists(), "Cloning failed! Repo directory not found.");

        // Step 2: Append content
        File fileToUpdate = new File(dir, fileName);
        Assert.assertTrue(fileToUpdate.exists(), "File not found: " + fileName);
        GitUtils.writeToFile(fileToUpdate, content, true);


        // Step 3: git add, commit, push
        GitUtils.runCommand(Arrays.asList("git", "add", fileName), dir);
        GitUtils.runCommand(Arrays.asList("git", "commit", "-m", "Update " + fileName), dir);
        GitUtils.runCommand(Arrays.asList("git", "push"), dir);
    }
}
