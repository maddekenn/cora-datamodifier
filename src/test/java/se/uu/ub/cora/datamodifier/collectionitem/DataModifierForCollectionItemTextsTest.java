package se.uu.ub.cora.datamodifier.collectionitem;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordTypeSearchLink;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.datamodifier.RecordStorageSpy;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class DataModifierForCollectionItemTextsTest {
    private RecordStorageForCollectionItemSpy recordStorage;
    private DataRecordLinkCollectorSpy linkCollector;
    private DataModifierForCollectionItemTexts dataModifier;

    @BeforeMethod
    public void setUp() {
        linkCollector = new DataRecordLinkCollectorSpy();
        recordStorage = new RecordStorageForCollectionItemSpy();

        dataModifier = new DataModifierForCollectionItemTexts();
        dataModifier.setLinkCollector(linkCollector);
        dataModifier.setRecordStorage(recordStorage);
    }

    @Test
    public void testInit() {
        assertNotNull(dataModifier.getLinkCollector());
        assertNotNull(dataModifier.getRecordStorage());
    }

    @Test
    public void testModify() {
        dataModifier.modifyByRecordType("collectionItem");

        assertCorrectModifiedGroup();
        assertEquals(linkCollector.noOfTimesCalled, 1);
    }

    private void assertCorrectModifiedGroup() {
        DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);

        DataGroup textIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("textId");
        assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), "coraText");
        assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), "someText");

        DataGroup defTextIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("defTextId");
        assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), "coraText");
        assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), "someDefText");

    }
}
