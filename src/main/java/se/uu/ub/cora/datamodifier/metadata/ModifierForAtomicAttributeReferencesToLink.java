package se.uu.ub.cora.datamodifier.metadata;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicAttributeReferencesToLink extends DataModifierForRecordType {

	private static final String ATTRIBUTE_REFERENCES = "attributeReferences";

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		if(dataGroupHasAttributeReferences(dataGroup)) {
			modifyAttributeReferences(dataGroup);
		}
	}

	private boolean dataGroupHasAttributeReferences(DataGroup dataGroup) {
		return dataGroup.containsChildWithNameInData(ATTRIBUTE_REFERENCES);
	}

	private void modifyAttributeReferences(DataGroup dataGroup) {
		DataGroup attributeReferences = dataGroup.getFirstGroupWithNameInData(ATTRIBUTE_REFERENCES);
		List<DataGroup> refGroups = createRefsAsGroups(attributeReferences);

		removeOldAndAddNewAttributeReferences(dataGroup, refGroups);
	}

	private List<DataGroup> createRefsAsGroups(DataGroup attributeReferences) {
		List<DataGroup> refGroups = new ArrayList<>();

		List<DataAtomic> refs = attributeReferences.getAllDataAtomicsWithNameInData("ref");
		for (DataAtomic ref : refs) {
            createRefAsGroupAndAddToRefGroups(ref, refGroups);
        }
		return refGroups;
	}

	private void createRefAsGroupAndAddToRefGroups(DataAtomic ref, List<DataGroup> refGroups) {
		DataGroup refGroup = DataGroup.withNameInData("ref");
		addChildrenToRefGroup(refGroup, ref.getValue());
		refGroup.setRepeatId(ref.getRepeatId());
		refGroups.add(refGroup);
	}

	private void addChildrenToRefGroup(DataGroup refGroup, String value) {
		refGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType",
                "metadataCollectionVariable"));
		refGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", value));
	}

	private void removeOldAndAddNewAttributeReferences(DataGroup dataGroup, List<DataGroup> refGroups) {
		dataGroup.removeFirstChildWithNameInData(ATTRIBUTE_REFERENCES);
		addModifiedAttributeReferencesToDataGroup(dataGroup, refGroups);
	}

	private void addModifiedAttributeReferencesToDataGroup(DataGroup dataGroup, List<DataGroup> refGroups) {
		DataGroup newAttributeReferences = DataGroup.withNameInData(ATTRIBUTE_REFERENCES);
		for (DataGroup refGroup : refGroups) {
			newAttributeReferences.addChild(refGroup);
		}
		dataGroup.addChild(newAttributeReferences);
	}
}
