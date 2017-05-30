package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicPresentationOfToLinkInRepeatingContainer extends DataModifierForRecordType {

    private static final String PRESENTATION_OF = "presentationOf";

    @Override
    protected void modifyDataGroup(DataGroup dataGroup) {
        String presentationOfValue = dataGroup.getFirstAtomicValueWithNameInData(PRESENTATION_OF);
        dataGroup.removeFirstChildWithNameInData(PRESENTATION_OF);

        createAndAddPresentationOfGroup(dataGroup, presentationOfValue);
    }

    private void createAndAddPresentationOfGroup(DataGroup dataGroup, String presentationOfValue) {
        DataGroup presentationOf = DataGroup.withNameInData(PRESENTATION_OF);
        presentationOf.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
        presentationOf.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", presentationOfValue));
        dataGroup.addChild(presentationOf);
    }
}
