package se.uu.ub.cora.datamodifier.presentation;

import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicLinkedPresentationToLinkInPLink extends DataModifierForRecordType {
	private static final String LINKED_RECORD_TYPE = "linkedRecordType";

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		if (dataGroup.containsChildWithNameInData("linkedRecordPresentations")) {
			modifyLinkedPresentations(dataGroup);
		}
	}

	private void modifyLinkedPresentations(DataGroup dataGroup) {
		List<DataGroup> linkedPresentationList = getListOfLinkedPresentations(dataGroup);
		for (DataGroup linkedPresentation : linkedPresentationList) {
			modifyLinkedPresentation(linkedPresentation);
		}
	}

	private List<DataGroup> getListOfLinkedPresentations(DataGroup dataGroup) {
		DataGroup linkedPresentations = dataGroup
				.getFirstGroupWithNameInData("linkedRecordPresentations");
		return linkedPresentations.getAllGroupsWithNameInData("linkedRecordPresentation");
	}

	private void modifyLinkedPresentation(DataGroup linkedPresentation) {
		DataGroup presentedRecordType = createPresentedRecordType(linkedPresentation);
		DataGroup presentation = createPresentation(linkedPresentation);

		linkedPresentation.addChild(presentedRecordType);
		linkedPresentation.addChild(presentation);

		removeDeprecatedChildren(linkedPresentation);
	}

	private DataGroup createPresentedRecordType(DataGroup linkedPresentation) {
		String linkedRecordTypeValue = extractRecordTypeValue(linkedPresentation);
		DataGroup presentedRecordType = DataGroup.withNameInData("presentedRecordType");
		presentedRecordType
				.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, "recordType"));
		presentedRecordType.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordTypeValue));
		return presentedRecordType;
	}

	private DataGroup createPresentation(DataGroup linkedPresentation) {
		String linkedRecordId = extractPresentationId(linkedPresentation);
		DataGroup presentation = DataGroup.withNameInData("presentation");
		presentation
				.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, "presentation"));
		presentation.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return presentation;
	}

	private String extractRecordTypeValue(DataGroup linkedPresentation) {
		return linkedPresentation.getFirstAtomicValueWithNameInData(LINKED_RECORD_TYPE);
	}

	private String extractPresentationId(DataGroup linkedPresentation) {
		return linkedPresentation.getFirstAtomicValueWithNameInData("presentationId");
	}

	private void removeDeprecatedChildren(DataGroup linkedPresentation) {
		linkedPresentation.removeFirstChildWithNameInData("presentationId");
		linkedPresentation.removeFirstChildWithNameInData(LINKED_RECORD_TYPE);
	}
}
