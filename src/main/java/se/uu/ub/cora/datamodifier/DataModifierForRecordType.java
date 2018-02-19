package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public abstract class DataModifierForRecordType implements DataModifier {
	protected String recordType;
	protected RecordStorage recordStorage;
	protected List<DataGroup> modifiedList = new ArrayList<>();
	protected DataRecordLinkCollector linkCollector;

	@Override
	public void modifyByRecordType(String recordType) {
		this.recordType = recordType;
		modifiedList = new ArrayList<>();
		try {
			DataGroup emptyFilter = DataGroup.withNameInData("filter");
			Collection<DataGroup> recordList = recordStorage.readList(recordType, emptyFilter);
			for (DataGroup dataGroup : recordList) {
				modifyDataGroup(dataGroup);
				modifiedList.add(dataGroup);
			}
		} catch (RecordNotFoundException e) {
			// do nothing
			// will end up here if the recordType has no records
		}
		updateRecords();
	}

	protected abstract void modifyDataGroup(DataGroup dataGroup);

	protected void updateRecords() {
		for (DataGroup modified : modifiedList) {
			updateModifiedDataGroup(modified);
		}
	}

	protected void updateModifiedDataGroup(DataGroup modified) {
		DataGroup recordInfo = modified.getFirstGroupWithNameInData("recordInfo");

		String id = recordInfo.getFirstAtomicValueWithNameInData("id");

		DataElement typeChild = recordInfo.getFirstChildWithNameInData("type");
		String type = extractType(recordInfo, typeChild);
		String dataDivider = extractDataDivider(recordInfo);
		String metadataId = getMetadataId();
		DataGroup collectedLinks = linkCollector.collectLinks(metadataId, modified, type, id);

		// emptyCollectedData was added after the data already had been modified
		DataGroup emptyCollectedData = DataGroup.withNameInData("collectedData");
		recordStorage.update(type, id, modified, emptyCollectedData, collectedLinks, dataDivider);
	}

	private String extractType(DataGroup recordInfo, DataElement typeChild) {
		String type;
		if (typeChild instanceof DataAtomic) {
			type = recordInfo.getFirstAtomicValueWithNameInData("type");
		} else {
			DataGroup typeGroup = (DataGroup) typeChild;
			type = typeGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
		}
		return type;
	}

	protected String getMetadataId() {
		DataGroup recordTypeDataGroup = recordStorage.read("recordType", recordType);
		DataGroup metadataIdGroup = recordTypeDataGroup.getFirstGroupWithNameInData("metadataId");
		return metadataIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private String extractDataDivider(DataGroup recordInfo) {
		DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData("dataDivider");
		return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	@Override
	public void setRecordStorage(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;
	}

	@Override
	public void setLinkCollector(DataRecordLinkCollector linkCollector) {
		this.linkCollector = linkCollector;
	}

	public RecordStorage getRecordStorage() {
		return recordStorage;
	}

	public DataRecordLinkCollector getLinkCollector() {
		return linkCollector;
	}
}
