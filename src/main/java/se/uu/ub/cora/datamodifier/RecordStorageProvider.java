package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.spider.record.storage.RecordStorage;

public interface RecordStorageProvider {
    public RecordStorage getRecordStorageWithBasePath(String basePath);
}
