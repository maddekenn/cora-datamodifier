package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForLinkedRecordTypeInTexts extends DataModifierForRecordType {
	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		changeLinkedRecordTypeForTextWithNameInData(dataGroup, "textId");
		changeLinkedRecordTypeForTextWithNameInData(dataGroup, "defTextId");
	}

	private void changeLinkedRecordTypeForTextWithNameInData(DataGroup dataGroup,
			String textNameInData) {
		if (dataGroup.containsChildWithNameInData(textNameInData)) {
			DataGroup textIdGroup = dataGroup.getFirstGroupWithNameInData(textNameInData);
			textIdGroup.removeFirstChildWithNameInData("linkedRecordType");
			textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		}
	}
}
