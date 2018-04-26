package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForModifyingLinkedRecordTypeSpy;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class ModifierForUpdateInfoInRecordInfoTest {
	private RecordStorageForModifyingLinkedRecordTypeSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForUpdateInfoInRecordInfo dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForModifyingLinkedRecordTypeSpy();

		dataModifier = new ModifierForUpdateInfoInRecordInfo();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test
	public void testModifyDataGroup() {
		dataModifier.modifyByRecordType("recordTypeForChangingUpdateStructure");
		assertCorrectModifiedGroupWithUpdatedInfoInRecordInfo(
				"recordTypeForChangingUpdateStructure");
		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	private void assertCorrectModifiedGroupWithUpdatedInfoInRecordInfo(String linkedType) {
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);

		DataGroup recordInfo = modifiedDataGroup.getFirstGroupWithNameInData("recordInfo");
		assertFalse(recordInfo.containsChildWithNameInData("tsUpdated"));
		assertFalse(recordInfo.containsChildWithNameInData("updatedBy"));

		DataGroup updated = recordInfo.getFirstGroupWithNameInData("updated");
		DataGroup updatedBy = updated.getFirstGroupWithNameInData("updatedBy");
		assertEquals(updatedBy.getFirstAtomicValueWithNameInData("linkedRecordId"), recordStorage.updatedBys.get(0));
		assertEquals(updatedBy.getFirstAtomicValueWithNameInData("linkedRecordType"), recordStorage.updatedByUserTypes.get(0));
		assertEquals(updated.getFirstAtomicValueWithNameInData("tsUpdated"), recordStorage.tsUpdates.get(0));
		assertEquals(updated.getRepeatId(), "0");

	}

	 public void testModifyingRecordTypeWithNoRecords() {
	 	dataModifier.modifyByRecordType("place");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 0);
	 }
}
