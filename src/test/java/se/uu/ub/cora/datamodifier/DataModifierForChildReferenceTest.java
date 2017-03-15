package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifierForChildReferenceTest {

	private DataModifierForChildReference dataModifier;
	private RecordStorage recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageSpy();
		dataModifier = new DataModifierForChildReference();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);

	}

	@Test
	public void testModifyChildReference() {
		dataModifier.modifyByRecordType("metadataGroup");

		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);
		DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup childReferences = modifiedDataGroup
				.getFirstGroupWithNameInData("childReferences");
		DataGroup childReference = childReferences.getFirstGroupWithNameInData("childReference");
		DataGroup ref = childReference.getFirstGroupWithNameInData("ref");

		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "metadata");
		assertEquals(ref.getAttributes().size(), 0);
		assertTrue(linkCollector.collectLinksWasCalled);
	}
}
