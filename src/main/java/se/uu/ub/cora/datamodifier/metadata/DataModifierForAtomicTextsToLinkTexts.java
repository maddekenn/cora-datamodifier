package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForMetadata;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class DataModifierForAtomicTextsToLinkTexts extends DataModifierForMetadata {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		createAndAddTextGroupAndRemoveAtomicValueForTextType(dataGroup, "textId");
		createAndAddTextGroupAndRemoveAtomicValueForTextType(dataGroup, "defTextId");
	}

	private void createAndAddTextGroupAndRemoveAtomicValueForTextType(DataGroup dataGroup,
			String textType) {
		String textId = dataGroup.getFirstAtomicValueWithNameInData(textType);
		dataGroup.removeFirstChildWithNameInData(textType);

		DataGroup textIdGroup = createTextAsLink(textType, textId);
		dataGroup.addChild(textIdGroup);
	}

	private DataGroup createTextAsLink(String textType, String textId) {
		DataGroup textIdGroup = DataGroup.withNameInData(textType);
		textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", textId));
		return textIdGroup;
	}

}
