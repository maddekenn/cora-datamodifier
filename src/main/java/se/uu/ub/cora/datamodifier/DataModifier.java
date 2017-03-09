package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public interface DataModifier {
    void modifyByRecordType(String recordType);

    void setRecordStorage(RecordStorage recordStorage);

    void setLinkCollector(DataRecordLinkCollector linkCollector);
}
