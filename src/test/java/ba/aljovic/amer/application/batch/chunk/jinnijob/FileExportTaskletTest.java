package ba.aljovic.amer.application.batch.chunk.jinnijob;

import ba.aljovic.amer.Application;
import ba.aljovic.amer.application.batch.launcher.FileExportJobLauncher;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("spring.profiles.active=production")
public class FileExportTaskletTest extends TestCase
{
    @Autowired
    FileExportJobLauncher jobLauncher;

    @Test
    public void test() throws Exception
    {
        jobLauncher.launch();
    }
}