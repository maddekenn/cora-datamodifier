package se.uu.ub.cora.datamodifier.collectionitem;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecordStorageForCollectionItemSpy implements RecordStorage{

    public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
    public List<String> readRecordTypes = new ArrayList<>();

    public List<DataGroup> createdData = new ArrayList<>();
    public List<String> createdType = new ArrayList<>();

    @Override
    public DataGroup read(String type, String id) {
        if ("metadataCollectionItem".equals(id)) {
            readRecordTypes.add(id);
            return createRecordTypeWithMetadataId("metadataCollectionItem", "metadataCollectionItemGroup");
        }
        return null;
    }

    private DataGroup createRecordTypeWithMetadataId(String recordId, String metadataId) {
        DataGroup collectionItem = DataCreator.createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
                recordId, "recordType", "recordType", "cora");
        DataGroup metadataIdGroup = DataGroup.withNameInData("metadataId");
        metadataIdGroup
                .addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
        metadataIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", metadataId));
        collectionItem.addChild(metadataIdGroup);
        return collectionItem;
    }

    @Override
    public void create(String s, String s1, DataGroup dataGroup, DataGroup dataGroup1, String s2) {

    }

    @Override
    public void deleteByTypeAndId(String s, String s1) {

    }

    @Override
    public boolean linksExistForRecord(String s, String s1) {
        return false;
    }

    @Override
    public void update(String type, String id, DataGroup record, DataGroup linkList,
                       String dataDivider) {
        modifiedDataGroupsSentToUpdate.add(record);
    }

    @Override
    public Collection<DataGroup> readList(String type) {
        List<DataGroup> recordList = new ArrayList<>();
        if ("metadataCollectionItem".equals(type)) {
            DataGroup collectionItem = DataCreator.createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider("someItem", "metadata", "collectionItem", "testSystem");
            collectionItem.addChild(DataAtomic.withNameInDataAndValue("nameInData", "some"));
            collectionItem.addChild(DataAtomic.withNameInDataAndValue("textId", "someText"));
            collectionItem.addChild(DataAtomic.withNameInDataAndValue("defTextId", "someDefText"));
            recordList.add(collectionItem);

        }
        return recordList;
    }

    @Override
    public Collection<DataGroup> readAbstractList(String s) {
        return null;
    }

    @Override
    public DataGroup readLinkList(String s, String s1) {
        return null;
    }

    @Override
    public Collection<DataGroup> generateLinkCollectionPointingToRecord(String s, String s1) {
        return null;
    }

    @Override
    public boolean recordsExistForRecordType(String s) {
        return false;
    }

    @Override
    public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String s, String s1) {
        return false;
    }
}
