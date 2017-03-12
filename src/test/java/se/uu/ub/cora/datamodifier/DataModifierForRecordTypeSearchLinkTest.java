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

		assertCorrectCreatedSearch();

		assertCorrectModifiedGroup();

	}

	private void assertCorrectCreatedSearch() {
		String createdRecordType = recordStorage.createdType.get(0);
		assertEquals(createdRecordType, "search");
		DataGroup createdSearch = recordStorage.createdData.get(0);
		assertCorrectRecordInfo(createdSearch);

		assertCorrectMetadata(createdSearch);

		assertCorrectPresentation(createdSearch);

		assertCorrectRecordTypeToSearchIn(createdSearch);
	}

	private void assertCorrectRecordInfo(DataGroup createdSearch) {
		DataGroup recordInfo = createdSearch.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "bookSearch");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "cora");
	}

	private void assertCorrectMetadata(DataGroup createdSearch) {
		DataGroup metadata = createdSearch.getFirstGroupWithNameInData("metadataId");
		assertEquals(metadata.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataGroup");
		assertEquals(metadata.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"autocompleteSearchGroup");
	}

	private void assertCorrectPresentation(DataGroup createdSearch) {
		DataGroup presentation = createdSearch.getFirstGroupWithNameInData("presentationId");
		assertEquals(presentation.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"autocompleteSearchPGroup");
		assertEquals(presentation.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"presentationGroup");
	}

	private void assertCorrectRecordTypeToSearchIn(DataGroup createdSearch) {
		DataGroup recordTypeToSearchIn = createdSearch
				.getFirstGroupWithNameInData("recordTypeToSearchIn");
		assertEquals(recordTypeToSearchIn.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"book");
	}

	private void assertCorrectModifiedGroup() {
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		assertFalse(modifiedDataGroup.containsChildWithNameInData("searcMetadataId"));
		assertFalse(modifiedDataGroup.containsChildWithNameInData("searchPresentationFormId"));
		DataGroup search = modifiedDataGroup.getFirstGroupWithNameInData("search");
		assertEquals(search.getFirstAtomicValueWithNameInData("linkedRecordType"), "search");
		assertEquals(search.getFirstAtomicValueWithNameInData("linkedRecordId"), "bookSearch");
	}
}
