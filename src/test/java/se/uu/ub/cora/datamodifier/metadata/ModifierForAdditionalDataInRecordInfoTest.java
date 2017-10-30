package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.datamodifier.RecordStorageSpy;

public class ModifierForAdditionalDataInRecordInfoTest {
	private RecordStorageSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForAdditionalDataInRecordInfo dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageSpy();

		dataModifier = new ModifierForAdditionalDataInRecordInfo();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test
	public void testModifyRecordInfoInMetadataGroup() {
		dataModifier.modifyByRecordType("metadataGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 2);
		assertEquals(linkCollector.noOfTimesCalled, 2);

		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup recordInfo = modifiedDataGroup.getFirstGroupWithNameInData("recordInfo");
		assertCorrectUserIdInUpdatedBy(recordInfo, "someUserId");

		assertTrue(recordInfo.containsChildWithNameInData("tsCreated"));

		DataGroup modifiedDataGroup2 = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
		DataGroup recordInfo2 = modifiedDataGroup2.getFirstGroupWithNameInData("recordInfo");
		assertCorrectUserIdInUpdatedBy(recordInfo2, "someOtherUserId");
		assertTrue(recordInfo2.containsChildWithNameInData("tsCreated"));

		assertTsCreatedIsDifferentForTwoDifferentGroups(recordInfo, recordInfo2);
	}

	private void assertCorrectUserIdInUpdatedBy(DataGroup recordInfo, String userId) {
		DataGroup updatedByGroup = recordInfo.getFirstGroupWithNameInData("updatedBy");

		assertEquals(updatedByGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), "user");
		assertEquals(updatedByGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), userId);
	}

	private void assertTsCreatedIsDifferentForTwoDifferentGroups(DataGroup recordInfo,
			DataGroup recordInfo2) {
		String tsCreated = recordInfo.getFirstAtomicValueWithNameInData("tsCreated");
		String tsCreated2 = recordInfo2.getFirstAtomicValueWithNameInData("tsCreated");
		assertFalse(tsCreated == tsCreated2);
	}

}
