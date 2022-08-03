package jensha.utils;

import jensha.jobs._directories.JobDir;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobPathsTest {

    @Test
    public void create() {
        JobDir jobFolder = JobDir.jenkins_management;
        JobDslUtil jobPaths = new JobDslUtil(null, SharedLibrary.getAnnotation(), jobFolder, "some_job", "Some Job");
        assertEquals("Some Job", jobPaths.displayName);
        assertEquals("jenkins_management/some_job", jobPaths.pathToPipelineJob);
        assertEquals("src/jensha/jobs/jenkins_management/some_job/JenkinsFile", jobPaths.pathToJenkinsFile);
    }

}
