package se.uu.ub.cora.datamodifier.metadata;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.datamodifier.RecordStorageSpy;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ModifierForLinkedRecordTypeInMetadataRecordLinkTest {
	private RecordStorageSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForLinkedRecordTypeInRecordTypeRecordLink dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageSpy();

		dataModifier = new ModifierForLinkedRecordTypeInRecordTypeRecordLink();
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
		dataModifier.modifyByRecordType("metadataRecordLink");
		assertCorrectModifiedGroupWithLinkedRecordTypeAsLink("book");
		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	private void assertCorrectModifiedGroupWithLinkedRecordTypeAsLink(String linkedType) {
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);

		DataGroup type = modifiedDataGroup.getFirstGroupWithNameInData("linkedRecordType");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordType"), "recordType");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordId"), linkedType);

	}
}
