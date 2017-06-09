package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForCreatedByToLinksSpy;

public class ModifierForAtomicCreatedByToLinkTest {
	private RecordStorageForCreatedByToLinksSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForAtomicCreatedByToLink dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForCreatedByToLinksSpy();

		dataModifier = new ModifierForAtomicCreatedByToLink();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test
	public void testModifyMetadataGroup() {
		dataModifier.modifyByRecordType("metadataGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 1);
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		// DataGroup parentIdGroup =
		// modifiedDataGroup.getFirstGroupWithNameInData("refParentId");
		// assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
		// "metadataGroup");
		// assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
		// "someParentGroup");
		//
		// assertEquals(linkCollector.noOfTimesCalled, 2);
	}
}
