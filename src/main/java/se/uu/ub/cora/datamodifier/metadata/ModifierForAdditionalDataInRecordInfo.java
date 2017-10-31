package se.uu.ub.cora.datamodifier.metadata;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAdditionalDataInRecordInfo extends DataModifierForRecordType {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		addUpdatedByToRecordInfo(recordInfo);

		addTsCreated(recordInfo);
		addTsUpdated(recordInfo);
	}

	private void addUpdatedByToRecordInfo(DataGroup recordInfo) {
		String userId = extractUserIdFromCreatedBy(recordInfo);
		DataGroup updatedBy = createUpdatedBy(userId);
		recordInfo.addChild(updatedBy);
	}

	private String extractUserIdFromCreatedBy(DataGroup recordInfo) {
		DataGroup createdByGroup = recordInfo.getFirstGroupWithNameInData("createdBy");
		return createdByGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private DataGroup createUpdatedBy(String userId) {
		DataGroup updatedBy = DataGroup.withNameInData("updatedBy");
		updatedBy.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "user"));
		updatedBy.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", userId));
		return updatedBy;
	}

	private void addTsCreated(DataGroup recordInfo) {
		LocalDateTime tsCreated = LocalDateTime.of(2017, 10, 01, 00, 00, 00);
		String dateTimeString = getLocalTimeDateAsString(tsCreated);
		recordInfo
				.addChild(DataAtomic.withNameInDataAndValue("tsCreated", dateTimeString));
	}

	private void addTsUpdated(DataGroup recordInfo) {
		String dateTimeString = getLocalTimeDateAsString(LocalDateTime.now());
		recordInfo
				.addChild(DataAtomic.withNameInDataAndValue("tsUpdated", dateTimeString));
	}

	private String getLocalTimeDateAsString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return localDateTime.format(formatter);
	}

}