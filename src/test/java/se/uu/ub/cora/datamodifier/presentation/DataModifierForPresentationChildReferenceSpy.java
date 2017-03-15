package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifierForPresentationChildReferenceSpy implements DataModifier {
    public String recordType;
    @Override
    public void modifyByRecordType(String recordType) {
        this.recordType = recordType;
    }

    @Override
    public void setRecordStorage(RecordStorage recordStorage) {

    }

    @Override
    public void setLinkCollector(DataRecordLinkCollector linkCollector) {

    }
}
