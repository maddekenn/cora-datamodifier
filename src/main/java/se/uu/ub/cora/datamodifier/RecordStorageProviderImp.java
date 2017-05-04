package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.spider.record.storage.RecordStorage;
import se.uu.ub.cora.storage.RecordStorageOnDisk;

public class RecordStorageProviderImp implements RecordStorageProvider {


    @Override
    public RecordStorage getRecordStorageWithBasePath(String basePath) {
        return RecordStorageOnDisk
                .createRecordStorageOnDiskWithBasePath(basePath);
    }
}
