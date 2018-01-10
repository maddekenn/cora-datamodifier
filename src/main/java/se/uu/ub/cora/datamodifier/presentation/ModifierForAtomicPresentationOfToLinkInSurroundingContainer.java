package se.uu.ub.cora.datamodifier.presentation;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicPresentationOfToLinkInSurroundingContainer
		extends DataModifierForRecordType {

	private static final String PRESENTATION_OF = "presentationOf";

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		DataGroup presentationsOf = dataGroup.getFirstGroupWithNameInData("presentationsOf");

		List<DataGroup> newPresentationOfList = createNewPresentationOfChildrenAsLinks(
				presentationsOf);
		removeOldPresentationOfChildren(presentationsOf);
		addNewPresentationOfChildren(presentationsOf, newPresentationOfList);
	}

	private List<DataGroup> createNewPresentationOfChildrenAsLinks(DataGroup presentationsOf) {
		List<DataGroup> newPresentationOfList = new ArrayList<>();

		for (DataAtomic presentationOf : presentationsOf
				.getAllDataAtomicsWithNameInData(PRESENTATION_OF)) {
			DataGroup presentationOfGroup = createPresentationOfLink(presentationOf);
			newPresentationOfList.add(presentationOfGroup);
		}
		return newPresentationOfList;
	}

	private DataGroup createPresentationOfLink(DataAtomic presentationOf) {
		String value = presentationOf.getValue();
		String repeatId = presentationOf.getRepeatId();
		return createPresentationOfGroupWithValueAndRepeatId(value, repeatId);
	}

	private DataGroup createPresentationOfGroupWithValueAndRepeatId(String value, String repeatId) {
		DataGroup presentationOfGroup = DataGroup.withNameInData(PRESENTATION_OF);
		presentationOfGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
		presentationOfGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", value));
		presentationOfGroup.setRepeatId(repeatId);
		return presentationOfGroup;
	}

	private void addNewPresentationOfChildren(DataGroup presentationsOf,
			List<DataGroup> newPresentationOfList) {
		for (DataGroup presentationOf : newPresentationOfList) {
			presentationsOf.addChild(presentationOf);
		}
	}

	private void removeOldPresentationOfChildren(DataGroup presentationsOf) {
		while (presentationsOf.containsChildWithNameInData(PRESENTATION_OF)) {
			presentationsOf.removeFirstChildWithNameInData(PRESENTATION_OF);
		}
	}

}
