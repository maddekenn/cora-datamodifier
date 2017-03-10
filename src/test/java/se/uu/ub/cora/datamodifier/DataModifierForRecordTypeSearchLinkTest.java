package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;

public class DataModifierForRecordTypeSearchLinkTest {

	private RecordStorageSpy recordStorage;
	private DataRecordLinkCollector linkCollector;
	private DataModifierForRecordTypeSearchLink dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageSpy();

		dataModifier = new DataModifierForRecordTypeSearchLink();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.linkCollector);
		assertNotNull(dataModifier.recordStorage);
	}

	@Test
	public void testModify() {
		dataModifier.modifyByRecordType("recordType");

		DataGroup createdSearch = recordStorage.createdData.get(0);
		DataGroup recordInfo = createdSearch.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "bookSearch");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "cora");

		DataGroup presentation = createdSearch.getFirstGroupWithNameInData("presentationId");
		assertEquals(presentation.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"autocompleteSearchPGroup");

		// DataGroup

		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		assertFalse(modifiedDataGroup.containsChildWithNameInData("searcMetadataId"));
		assertFalse(modifiedDataGroup.containsChildWithNameInData("searchPresentationFormId"));
		// DataGroup search =
		// modifiedDataGroup.getFirstGroupWithNameInData("search");
		// assertEquals(search.getFirstAtomicValueWithNameInData("linkedRecordType"),
		// "search");

	}
}
