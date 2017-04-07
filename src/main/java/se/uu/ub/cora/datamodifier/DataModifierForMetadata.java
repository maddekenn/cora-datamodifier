package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataGroupRecordLinkCollector;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DataModifierForMetadata implements DataModifier {
    protected String recordType;
    protected RecordStorage recordStorage;
    private List<DataGroup> modifiedList = new ArrayList<>();
    protected DataRecordLinkCollector linkCollector;

    @Override
    public void modifyByRecordType(String recordType) {
        this.recordType = recordType;
        Collection<DataGroup> recordList = recordStorage.readList(recordType);
        for (DataGroup dataGroup : recordList) {
            modifyDataGroup(dataGroup);
            modifiedList.add(dataGroup);
        }
        updateRecords();
    }

    protected abstract void modifyDataGroup(DataGroup dataGroup);

    protected void updateRecords(){
        for (DataGroup modified : modifiedList) {
            updateModifiedDataGroup(modified);
        }
    }

    protected void updateModifiedDataGroup(DataGroup modified) {
        DataGroup recordInfo = modified.getFirstGroupWithNameInData("recordInfo");

        String id = recordInfo.getFirstAtomicValueWithNameInData("id");
        String type = recordInfo.getFirstAtomicValueWithNameInData("type");
        String dataDivider = extractDataDivider(recordInfo);

        DataGroup collectedLinks = linkCollector.collectLinks(recordType, modified, type,
                id);

        recordStorage.update(type, id, modified, collectedLinks, dataDivider);
    }



    private String extractDataDivider(DataGroup recordInfo) {
        DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData("dataDivider");
        return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
    }

    @Override
    public void setRecordStorage(RecordStorage recordStorage) {
        this.recordStorage = recordStorage;
    }

    @Override
    public void setLinkCollector(DataRecordLinkCollector linkCollector) {
        this.linkCollector = linkCollector;
    }

    public RecordStorage getRecordStorage() {
        return recordStorage;
    }

    public DataRecordLinkCollector getLinkCollector() {
        return linkCollector;
    }
}
