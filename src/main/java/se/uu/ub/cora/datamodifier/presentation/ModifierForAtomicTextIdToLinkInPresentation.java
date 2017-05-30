package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicTextIdToLinkInPresentation extends DataModifierForRecordType {

	private static final String EMPTY_TEXT_ID = "emptyTextId";

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		if (presentationHasEmptyTextId(dataGroup)) {
			changeEmptyTextIdToLink(dataGroup);
		}
	}

	private boolean presentationHasEmptyTextId(DataGroup dataGroup) {
		return dataGroup.containsChildWithNameInData(EMPTY_TEXT_ID);
	}

	private void changeEmptyTextIdToLink(DataGroup dataGroup) {
		String textId = dataGroup.getFirstAtomicValueWithNameInData(EMPTY_TEXT_ID);
		dataGroup.removeFirstChildWithNameInData(EMPTY_TEXT_ID);
		createAndAddEmptyTextIdGroup(dataGroup, textId);
	}

	private void createAndAddEmptyTextIdGroup(DataGroup dataGroup, String textId) {
		DataGroup emptyTextIdGroup = DataGroup.withNameInData(EMPTY_TEXT_ID);
		emptyTextIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		emptyTextIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", textId));
		dataGroup.addChild(emptyTextIdGroup);
	}

}
