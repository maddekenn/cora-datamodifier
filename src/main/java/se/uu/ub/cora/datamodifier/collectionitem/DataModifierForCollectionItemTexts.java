package se.uu.ub.cora.datamodifier.collectionitem;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.datamodifier.DataModifierForMetadata;
import se.uu.ub.cora.datamodifier.presentation.DataModifierForPresentations;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataModifierForCollectionItemTexts extends DataModifierForMetadata {

    protected void modifyDataGroup(DataGroup dataGroup) {
        createAndAddTextGroupAndRemoveAtomicValueForTextType(dataGroup, "textId");
        createAndAddTextGroupAndRemoveAtomicValueForTextType(dataGroup, "defTextId");
    }

    private void createAndAddTextGroupAndRemoveAtomicValueForTextType(DataGroup dataGroup, String textType) {
        String textId = dataGroup.getFirstAtomicValueWithNameInData(textType);
        dataGroup.removeFirstChildWithNameInData(textType);

        DataGroup textIdGroup = createTextAsLink(textType, textId);
        dataGroup.addChild(textIdGroup);
    }

    private DataGroup createTextAsLink(String textType, String textId) {
        DataGroup textIdGroup = DataGroup.withNameInData(textType);
        textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "coraText"));
        textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", textId));
        return textIdGroup;
    }

}
