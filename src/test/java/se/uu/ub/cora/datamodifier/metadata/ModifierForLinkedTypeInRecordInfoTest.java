package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForModifyingLinkedRecordTypeSpy;

public class ModifierForLinkedTypeInRecordInfoTest {
	private RecordStorageForModifyingLinkedRecordTypeSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForLinkedTypeInRecordInfo dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForModifyingLinkedRecordTypeSpy();

		dataModifier = new ModifierForLinkedTypeInRecordInfo();
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
		dataModifier.modifyByRecordType("metadataCollectionItem");
		assertCorrectModifiedGroupWithLinkedTypeInRecordInfo("metadataCollectionItem");
		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	private void assertCorrectModifiedGroupWithLinkedTypeInRecordInfo(String linkedType) {
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);

		DataGroup recordInfo = modifiedDataGroup.getFirstGroupWithNameInData("recordInfo");
		DataGroup type = recordInfo.getFirstGroupWithNameInData("type");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordType"), "recordType");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordId"), linkedType);

	}

	// TODO: test fwith recordtype with no reords (RecordNotFoundException)

	@Test
	public void testModifingRecordTypeWithNoRecords() {
		dataModifier.modifyByRecordType("place");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 0);
	}
}
