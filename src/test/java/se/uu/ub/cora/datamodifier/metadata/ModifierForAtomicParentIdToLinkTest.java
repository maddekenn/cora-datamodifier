package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.data.DataMissingException;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForParentIdToLinksSpy;

public class ModifierForAtomicParentIdToLinkTest {
	private RecordStorageForParentIdToLinksSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForAtomicParentIdToLink dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForParentIdToLinksSpy();

		dataModifier = new ModifierForAtomicParentIdToLink();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test(expectedExceptions = DataMissingException.class)
	public void testModifyAtomicValueRemoved() {
		dataModifier.modifyByRecordType("metadataGroup");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		modifiedDataGroup.getFirstAtomicValueWithNameInData("refParentId");
	}

	@Test
	public void testModifyMetadataGroup() {
		dataModifier.modifyByRecordType("metadataGroup");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup parentIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("refParentId");
		assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataGroup");
		assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someParentGroup");

		assertEquals(linkCollector.noOfTimesCalled, 2);
	}

	@Test
	public void testModifyMetadataTextVariable() {
		dataModifier.modifyByRecordType("metadataTextVariable");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup parentIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("refParentId");
		assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataTextVariable");
		assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someParentTextVar");

		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	@Test
	public void testModifyMetadataTRecordLink() {
		dataModifier.modifyByRecordType("metadataRecordLink");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup parentIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("refParentId");
		assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataRecordLink");
		assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someParentLink");

		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	@Test
	public void testModifyMetadataTCollectionVariabel() {
		dataModifier.modifyByRecordType("metadataCollectionVariable");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup parentIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("refParentId");
		assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataCollectionVariable");
		assertEquals(parentIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someParentCollectionVar");

		assertEquals(linkCollector.noOfTimesCalled, 1);
	}
}
