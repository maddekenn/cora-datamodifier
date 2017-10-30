package se.uu.ub.cora.datamodifier.metadata;

import java.time.LocalDateTime;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAdditionalDataInRecordInfo extends DataModifierForRecordType {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		addUpdatedByToRecordInfo(recordInfo);

		addTsCreated(recordInfo);
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
		LocalDateTime localDateTime = LocalDateTime.now();
		recordInfo
				.addChild(DataAtomic.withNameInDataAndValue("tsCreated", localDateTime.toString()));
	}

}
