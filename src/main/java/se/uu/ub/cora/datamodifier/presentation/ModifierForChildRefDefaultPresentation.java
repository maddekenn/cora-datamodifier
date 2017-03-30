package se.uu.ub.cora.datamodifier.presentation;

import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

public class ModifierForChildRefDefaultPresentation extends DataModifierImp {

	private static final String REF_GROUP = "refGroup";
	private static final String REF_MIN_GROUP = "refMinGroup";

	@Override
	protected void modifyChildReference(DataGroup childReference) {
		String defaultValue = childReference.getFirstAtomicValueWithNameInData("default");
		childReference.removeFirstChildWithNameInData("default");

		DataGroup refGroup = childReference.getFirstGroupWithNameInData(REF_GROUP);
		refGroup.setRepeatId("0");

		possiblyModifyRefMinGroup(childReference);
		changeOrderOfGroupsIfRefMinIsDefault(childReference, defaultValue, refGroup);
	}

	private void possiblyModifyRefMinGroup(DataGroup childReference) {
		if (refMinGroupExists(childReference)) {
			modifyRefMinGroup(childReference);
		}
	}

	private boolean refMinGroupExists(DataGroup childReference) {
		return childReference.containsChildWithNameInData(REF_MIN_GROUP);
	}

	private void modifyRefMinGroup(DataGroup childReference) {
		DataGroup refMinGroup = childReference.getFirstGroupWithNameInData(REF_MIN_GROUP);
		DataGroup newRefGroup = changeNameInDataForMinGroup(refMinGroup);
		newRefGroup.setRepeatId("1");

		removeOldMinGroupAndAddNewRefGroupToChildReference(childReference, newRefGroup);
	}

	private DataGroup changeNameInDataForMinGroup(DataGroup refMinGroup) {
		List<DataElement> children = refMinGroup.getChildren();
		DataGroup newRefGroup = DataGroup.withNameInData(REF_GROUP);
		children.forEach(newRefGroup::addChild);
		return newRefGroup;
	}

	private void removeOldMinGroupAndAddNewRefGroupToChildReference(DataGroup childReference,
			DataGroup newRefGroup) {
		childReference.removeFirstChildWithNameInData(REF_MIN_GROUP);
		childReference.addChild(newRefGroup);
	}

	private void changeOrderOfGroupsIfRefMinIsDefault(DataGroup childReference, String defaultValue,
			DataGroup refGroup) {
		if ("refMinimized".equals(defaultValue)) {
			childReference.removeFirstChildWithNameInData(REF_GROUP);
			childReference.addChild(refGroup);
		}
	}
}
