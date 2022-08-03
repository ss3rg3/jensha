package jensha.gitrepos;

import jensha._testutils.PScriptFake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GitRepoTest {

    @Test
    void repoName() {
        ClusterRepo clusterRepo = new ClusterRepo(new PScriptFake(), "https://github.com/surg3/ccli_uc.git", "user:password");
        assertEquals("ccli_uc", clusterRepo.getRepositoryName());
    }
}
