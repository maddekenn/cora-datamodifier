package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.ArrayList;
import java.util.Collection;

public abstract class DataModifierImp implements DataModifier {
    private String recordType;
    private ArrayList<DataGroup> modifiedList;
    RecordStorage recordStorage;
    DataRecordLinkCollector linkCollector;

    @Override
    public void modifyByRecordType(String recordType) {
        this.recordType = recordType;
        modifiedList = new ArrayList<>();
        Collection<DataGroup> recordList = recordStorage.readList(recordType);
        for (DataGroup dataGroup : recordList) {
            modifyDataGroup(dataGroup);
            modifiedList.add(dataGroup);
        }
        updateRecords();

    }

    protected void modifyDataGroup(DataGroup dataGroup) {
        if(dataGroup.containsChildWithNameInData("childReferences")) {
            DataGroup childReferences = dataGroup.getFirstGroupWithNameInData("childReferences");
            childReferences
                    .getAllGroupsWithNameInData("childReference").forEach(this::modifyChildReference);
        }
    }

    protected abstract void modifyChildReference(DataGroup childReference);

    @Override
    public void setRecordStorage(RecordStorage recordStorage) {
        this.recordStorage = recordStorage;
    }

    @Override
    public void setLinkCollector(DataRecordLinkCollector linkCollector) {
        this.linkCollector = linkCollector;
    }


    protected void updateRecords(){
        for (DataGroup modified : modifiedList) {
            updateModifiedDataGroup(modified);
        }
    }

    protected void updateModifiedDataGroup(DataGroup modified) {
        DataGroup recordInfo = modified.getFirstGroupWithNameInData("recordInfo");

        String id = recordInfo.getFirstAtomicValueWithNameInData("id");
        String type = recordInfo.getFirstAtomicValueWithNameInData("type");
        String metadataId = getMetadataId();
        DataGroup collectedLinks = linkCollector.collectLinks(metadataId, modified, type, id);

        String dataDivider = extractDataDivider(recordInfo);
        recordStorage.update(type, id, modified, collectedLinks, dataDivider);
    }

    protected String getMetadataId() {
        DataGroup recordTypeDataGroup = recordStorage.read("recordType", recordType);
        DataGroup metadataIdGroup = recordTypeDataGroup.getFirstGroupWithNameInData("metadataId");
        return metadataIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
    }

    protected String extractDataDivider(DataGroup recordInfo) {
        DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData("dataDivider");
        return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
    }
}
