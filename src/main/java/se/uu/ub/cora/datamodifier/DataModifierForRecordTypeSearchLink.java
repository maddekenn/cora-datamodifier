package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataModifierForRecordTypeSearchLink implements DataModifier {

	RecordStorage recordStorage;
	DataRecordLinkCollector linkCollector;
	private List<DataGroup> modifiedList = new ArrayList<>();

	@Override
	public void modifyByRecordType(String recordType) {
		Collection<DataGroup> recordList = recordStorage.readList(recordType);
		for (DataGroup dataGroup : recordList) {
			modifyDataGroup(dataGroup);
			modifiedList.add(dataGroup);
		}
		updateRecords();
	}

	private void modifyDataGroup(DataGroup dataGroup) {
		//searchMetadataId ska tas bort
		dataGroup.removeFirstChildWithNameInData("searcMetadataId");
		//searchPresentationFormId ska tas bort
		//search för den här recordTypen ska skapas - searchen ska peka på
		//metadatagruppen autocompleteSearchGroup
		//och motsvarande presentationsgrupp (som inte finns än, behöver skapas i gui
		//länken till den skapade searchen ska läggas till som länk i recordType
	}

	@Override
	public void setRecordStorage(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;

	}

	@Override
	public void setLinkCollector(DataRecordLinkCollector linkCollector) {
		this.linkCollector = linkCollector;

	}

	private void updateRecords() {
		for (DataGroup modified : modifiedList) {
			updateModifiedDataGroup(modified);
		}
	}

	private void updateModifiedDataGroup(DataGroup modified) {
		DataGroup recordInfo = modified.getFirstGroupWithNameInData("recordInfo");

		String id = recordInfo.getFirstAtomicValueWithNameInData("id");
		String type = recordInfo.getFirstAtomicValueWithNameInData("type");
		String dataDivider = extractDataDivider(recordInfo);

		DataGroup collectedLinks = linkCollector.collectLinks("metadataGroupGroup", modified, type,
				id);

		recordStorage.update(type, id, modified, collectedLinks, dataDivider);
	}


	private String extractDataDivider(DataGroup recordInfo) {
		DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData("dataDivider");
		return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

}
