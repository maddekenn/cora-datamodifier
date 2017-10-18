package se.uu.ub.cora.datamodifier.presentation;

import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicLinkedPresentationToLinkInPLink extends DataModifierForRecordType {
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
		String linkedRecordId = extractPresentationId(linkedPresentation);
		linkedPresentation.removeFirstChildWithNameInData("presentationId");
		linkedPresentation
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
	}

	private String extractPresentationId(DataGroup linkedPresentation) {
		return linkedPresentation
				.getFirstAtomicValueWithNameInData("presentationId");
	}
}
