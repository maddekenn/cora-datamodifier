package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForMetadataWithAttributeReferencesSpy;

import java.util.List;

public class ModifierForAtomicAttributeReferencesToLinkTest {
	private RecordStorageForMetadataWithAttributeReferencesSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForAtomicAttributeReferencesToLink dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForMetadataWithAttributeReferencesSpy();

		dataModifier = new ModifierForAtomicAttributeReferencesToLink();
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
		dataModifier.modifyByRecordType("metadataGroup");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		assertCorrectlyModifiedAttributeReferences(modifiedDataGroup);
		assertEquals(linkCollector.noOfTimesCalled, 2);
	}

	private void assertCorrectlyModifiedAttributeReferences(DataGroup modifiedDataGroup) {
		DataGroup attributeReferences = modifiedDataGroup
				.getFirstGroupWithNameInData("attributeReferences");

		List<DataGroup> refs = attributeReferences.getAllGroupsWithNameInData("ref");
		assertCorrectlyCreatedRefWithLinkedIdAndRepeatId(refs.get(0), "namePartGivenNameTypeCollectionVar", "0");
		assertCorrectlyCreatedRefWithLinkedIdAndRepeatId(refs.get(1), "someOtherTypeCollectionVar", "1");
	}

	private void assertCorrectlyCreatedRefWithLinkedIdAndRepeatId(DataGroup ref, String linkedRecordId, String repeatId) {
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataCollectionVariable");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"),
				linkedRecordId);
		assertEquals(ref.getRepeatId(), repeatId);
	}
}
