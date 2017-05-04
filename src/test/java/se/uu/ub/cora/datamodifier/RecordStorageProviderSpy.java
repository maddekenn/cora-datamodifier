package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageProviderSpy implements RecordStorageProvider {

    @Override
    public RecordStorage getRecordStorageWithBasePath(String basePath) {
        return new RecordStorageSpy();
    }
}
