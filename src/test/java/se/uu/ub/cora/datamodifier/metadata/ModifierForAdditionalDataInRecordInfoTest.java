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

		assertCorrectRecordInfoWIthIndexAndUserId(0, "someUserId");

		assertCorrectRecordInfoWIthIndexAndUserId(1, "someOtherUserId");

	}

	private void assertCorrectRecordInfoWIthIndexAndUserId(int index, String userId) {
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(index);
		DataGroup recordInfo = modifiedDataGroup.getFirstGroupWithNameInData("recordInfo");
		assertCorrectUserIdInUpdatedBy(recordInfo, userId);

		assertTrue(recordInfo.containsChildWithNameInData("tsCreated"));
		assertTrue(recordInfo.containsChildWithNameInData("tsUpdated"));

		String tsCreated = recordInfo.getFirstAtomicValueWithNameInData("tsCreated");
		String tsUpdated = recordInfo.getFirstAtomicValueWithNameInData("tsUpdated");
		assertEquals(tsCreated, "2017-10-01 00:00:00");
		assertFalse(tsCreated.equals(tsUpdated));
	}

	private void assertCorrectUserIdInUpdatedBy(DataGroup recordInfo, String userId) {
		DataGroup updatedByGroup = recordInfo.getFirstGroupWithNameInData("updatedBy");

		assertEquals(updatedByGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), "user");
		assertEquals(updatedByGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), userId);
	}


}
