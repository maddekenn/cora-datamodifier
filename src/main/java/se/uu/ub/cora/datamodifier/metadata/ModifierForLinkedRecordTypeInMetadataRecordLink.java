package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForMetadata;

public class ModifierForLinkedRecordTypeInMetadataRecordLink extends DataModifierForMetadata {

    private static final String LINKED_RECORD_TYPE = "linkedRecordType";

    @Override
    protected void modifyDataGroup(DataGroup dataGroup) {
        String linkedRecordType = dataGroup.getFirstAtomicValueWithNameInData(LINKED_RECORD_TYPE);
        dataGroup.removeFirstChildWithNameInData(LINKED_RECORD_TYPE);

        createAndAddTypeAsLink(dataGroup, linkedRecordType);
    }

    private void createAndAddTypeAsLink(DataGroup dataGroup, String linkedRecordType) {
        DataGroup typeGroup = DataGroup.withNameInData(LINKED_RECORD_TYPE);
        typeGroup.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, "recordType"));
        typeGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordType));
        dataGroup.addChild(typeGroup);
    }
}
