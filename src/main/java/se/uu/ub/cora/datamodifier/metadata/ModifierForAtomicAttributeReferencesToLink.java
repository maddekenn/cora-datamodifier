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
		DataGroup attributeReferences = dataGroup.getFirstGroupWithNameInData(ATTRIBUTE_REFERENCES);
		List<DataGroup> refGroups = new ArrayList<>();
		List<DataAtomic> refs = attributeReferences.getAllDataAtomicsWithNameInData("ref");
		for (DataAtomic ref : refs) {
			String value = ref.getValue();
			DataGroup refGroup = DataGroup.withNameInData("ref");
			refGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType",
					"metadataCollectionVariable"));
			refGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", value));
			refGroup.setRepeatId(ref.getRepeatId());

			refGroups.add(refGroup);
		}

		dataGroup.removeFirstChildWithNameInData(ATTRIBUTE_REFERENCES);
		DataGroup newAttributeReferences = DataGroup.withNameInData(ATTRIBUTE_REFERENCES);
		for (DataGroup refGroup : refGroups) {
			newAttributeReferences.addChild(refGroup);
		}
		dataGroup.addChild(newAttributeReferences);
	}

	// {
	// "name": "attributeReferences",
	// "children": [
	// {
	// "name": "ref",
	// "value": "namePartGivenNameTypeCollectionVar",
	// "repeatId": "0"
	// },
	// {
	// "name": "ref",
	// "value": "sfsfd",
	// "repeatId": "1"
	// }
	// ]
	// },
}
