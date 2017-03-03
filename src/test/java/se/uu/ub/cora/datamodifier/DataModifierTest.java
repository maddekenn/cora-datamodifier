package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifierTest {

	private DataModifier dataModifier;
	private RecordStorage recordStorage;

	@BeforeMethod
	public void testInit() {
		recordStorage = new RecordStorageSpy();
		dataModifier = new DataModifier(recordStorage);

	}

	@Test
	public void testModifyChildReference() {
		dataModifier.modifyByRecordType("metadataGroup");

		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);
		DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup ref = modifiedDataGroup.getFirstGroupWithNameInData("ref");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "metadata");
	}
}
