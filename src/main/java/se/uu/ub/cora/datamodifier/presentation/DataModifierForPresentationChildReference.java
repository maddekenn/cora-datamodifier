package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class DataModifierForPresentationChildReference implements DataModifier {
    RecordStorage recordStorage;
    DataRecordLinkCollector linkCollector;
    private List<DataGroup> modifiedList = new ArrayList<>();

    public void modifyByRecordType(String recordType) {
        Collection<DataGroup> recordList = recordStorage.readList(recordType);
        for (DataGroup dataGroup : recordList) {
            modifyDataGroup(dataGroup);
            modifiedList.add(dataGroup);
        }
        updateRecords();
    }

    private void modifyDataGroup(DataGroup dataGroup) {
        DataGroup childReferences = dataGroup.getFirstGroupWithNameInData("childReferences");
        for (DataGroup childReference : childReferences
                .getAllGroupsWithNameInData("childReference")) {
            modifyChildReference(childReference.getFirstGroupWithNameInData("refGroup"));
            if(childReference.containsChildWithNameInData("refMinGroup")){
                modifyChildReference(childReference.getFirstGroupWithNameInData("refMinGroup"));

            }
        }
    }

    private void modifyChildReference(DataGroup refGroup) {
        DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");
        ref.removeFirstChildWithNameInData("linkedRecordType");
        ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "presentation"));
        ref.getAttributes().remove("type");
    }

    private void updateRecords() {
        for (DataGroup modified : modifiedList) {
            updateModifiedDataGroup(modified);
        }
    }

    private void updateModifiedDataGroup(DataGroup modified) {
        DataGroup recordInfo = modified.getFirstGroupWithNameInData("recordInfo");

        String id = recordInfo.getFirstAtomicValueWithNameInData("id");
        String type = recordInfo.getFirstAtomicValueWithNameInData("type");
        String dataDivider = extractDataDivider(recordInfo);

        DataGroup collectedLinks = linkCollector.collectLinks("presentationGroupGroup", modified, type,
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
}
