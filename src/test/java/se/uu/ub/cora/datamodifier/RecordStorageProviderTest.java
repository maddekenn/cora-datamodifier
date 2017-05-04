package se.uu.ub.cora.datamodifier;

import org.testng.annotations.Test;
import se.uu.ub.cora.spider.record.storage.RecordStorage;
import se.uu.ub.cora.storage.RecordStorageOnDisk;

import static org.testng.Assert.assertTrue;

public class RecordStorageProviderTest {

    @Test
    public void init(){
        String basePath = "/home/madde/workspace/modify/";
        RecordStorageProviderImp provider = new RecordStorageProviderImp();
        RecordStorage recordStorage = provider.getRecordStorageWithBasePath(basePath);
        assertTrue(recordStorage instanceof RecordStorageOnDisk);
    }

}
