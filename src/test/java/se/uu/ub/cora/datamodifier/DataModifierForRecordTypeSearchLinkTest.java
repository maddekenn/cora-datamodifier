package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;

public class DataModifierForRecordTypeSearchLinkTest {

	private RecordStorageSpy recordStorage;
	private DataRecordLinkCollector linkCollector;
	private DataModifierForRecordTypeSearchLink dataModifier;

	@Test
	public void testInit() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageSpy();

		dataModifier = new DataModifierForRecordTypeSearchLink();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
		assertNotNull(dataModifier.linkCollector);
		assertNotNull(dataModifier.recordStorage);
	}

	@Test
	public void testModify() {
		dataModifier.modifyByRecordType("recordType");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		assertFalse(modifiedDataGroup.containsChildWithNameInData("searcMetadataId"));
//		DataGroup search = modifiedDataGroup.getFirstGroupWithNameInData("search");
//		assertEquals(search.getFirstAtomicValueWithNameInData("linkedRecordType"), "search");
	}
}
