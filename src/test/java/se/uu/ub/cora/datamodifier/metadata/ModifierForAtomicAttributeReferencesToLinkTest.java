package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;

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
		DataGroup attributeReferences = modifiedDataGroup
				.getFirstGroupWithNameInData("attributeReferences");
		DataGroup ref = attributeReferences.getFirstGroupWithNameInData("ref");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataCollectionVariable");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"namePartGivenNameTypeCollectionVar");
		assertEquals(ref.getRepeatId(), "0");
		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	@Test
	public void testModifyDataGroupWithNoAttributes() {
		dataModifier.modifyByRecordType("metadataGroup");
		// assertCorrectModifiedGroupWithLinkedRecordTypeAsLink("book");
		// TODO, lägg till en grupp utan attribute
		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

}
