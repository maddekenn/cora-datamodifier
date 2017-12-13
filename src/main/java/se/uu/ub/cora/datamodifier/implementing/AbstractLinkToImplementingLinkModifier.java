package se.uu.ub.cora.datamodifier.implementing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class AbstractLinkToImplementingLinkModifier extends DataModifierForRecordType {

	private static final String LINKED_RECORD_TYPE = "linkedRecordType";
	private Collection<DataGroup> recordTypes;

	@Override
	public void modifyByRecordType(String recordType) {
		recordTypes = recordStorage.readList("recordType", DataGroup.withNameInData("filter"));
		Map<String, List<String>> parentRecordTypesWithChildren = new HashMap<>();
		putParentRecordTypesInMap(parentRecordTypesWithChildren);
		for (DataGroup readRecordType : recordTypes) {
			if (readRecordType.containsChildWithNameInData("parentId")) {
				DataGroup parent = readRecordType.getFirstGroupWithNameInData("parentId");
				String parentId = parent.getFirstAtomicValueWithNameInData("linkedRecordId");
				String id = extractidFromRecordInfoInDataGroup(readRecordType);
				parentRecordTypesWithChildren.get(parentId).add(id);
			}
		}
		super.modifyByRecordType(recordType);
	}

	private String extractidFromRecordInfoInDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		return recordInfo.getFirstAtomicValueWithNameInData("id");
	}

	private void putParentRecordTypesInMap(
			Map<String, List<String>> parentRecordTypesWithChildren) {
		for (DataGroup readRecordType : recordTypes) {
			if (readRecordType.containsChildWithNameInData("abstract") && "true"
					.equals(readRecordType.getFirstAtomicValueWithNameInData("abstract"))) {
				// DataGroup recordInfo =
				// readRecordType.getFirstGroupWithNameInData("recordInfo");
				String id = extractidFromRecordInfoInDataGroup(readRecordType);
				parentRecordTypesWithChildren.put(id, new ArrayList<>());
			}
		}
	}

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		for (DataElement dataElement : dataGroup.getChildren())
			if (dataElement instanceof DataGroup) {
				DataGroup child = (DataGroup) dataElement;
				if (childIsLink(child)) {
					modifyLink(child);
				}
			}
		// TODO: iterate if child is group
	}

	private void modifyLink(DataGroup child) {
		String linkedRecordType = child.getFirstAtomicValueWithNameInData(LINKED_RECORD_TYPE);
		String linkedRecordId = child.getFirstAtomicValueWithNameInData("linkedRecordId");

		List<String> implementingRecordTypes = getImplementingRecordTypes(linkedRecordType,
				recordTypes);
		String foundImplementingRecordType = null;
		for (String implementingRecordType : implementingRecordTypes) {
			try {
				recordStorage.read(implementingRecordType, linkedRecordId);
				foundImplementingRecordType = implementingRecordType;
			} catch (RecordNotFoundException e) {
				// do nothing
			}
		}
		if (foundImplementingRecordType != null) {
			child.removeFirstChildWithNameInData(LINKED_RECORD_TYPE);
			child.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE,
					foundImplementingRecordType));
		}
	}

	private boolean childIsLink(DataGroup child) {
		return child.containsChildWithNameInData(LINKED_RECORD_TYPE);
	}

	private List<String> getImplementingRecordTypes(String linkedRecordType,
			Collection<DataGroup> recordTypes) {
		List<String> implementingRecordTypes = new ArrayList<>();
		for (DataGroup recordType : recordTypes) {
			if (recordType.containsChildWithNameInData("parentId")) {
				DataGroup parentGroup = recordType.getFirstGroupWithNameInData("parentId");
				String parentId = parentGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
				if (parentId.equals(linkedRecordType)) {
					DataGroup recordInfo = recordType.getFirstGroupWithNameInData("recordInfo");

					implementingRecordTypes.add(recordInfo.getFirstAtomicValueWithNameInData("id"));
				}
			}
		}
		return implementingRecordTypes;
	}

}
