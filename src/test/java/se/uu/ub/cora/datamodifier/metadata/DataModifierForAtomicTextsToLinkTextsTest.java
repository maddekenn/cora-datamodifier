package se.uu.ub.cora.datamodifier.metadata;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForAtomicTextsToLinksSpy;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class DataModifierForAtomicTextsToLinkTextsTest {
    private RecordStorageForAtomicTextsToLinksSpy recordStorage;
    private DataRecordLinkCollectorSpy linkCollector;
    private DataModifierForAtomicTextsToLinkTexts dataModifier;

    @BeforeMethod
    public void setUp() {
        linkCollector = new DataRecordLinkCollectorSpy();
        recordStorage = new RecordStorageForAtomicTextsToLinksSpy();

        dataModifier = new DataModifierForAtomicTextsToLinkTexts();
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
        dataModifier.modifyByRecordType("metadataCollectionItem");

        assertCorrectModifiedGroupWithLinkedRecordTypeTextIdAndDefTextId("text", "someText", "someDefText");
        assertEquals(linkCollector.noOfTimesCalled, 1);
    }

    private void assertCorrectModifiedGroupWithLinkedRecordTypeTextIdAndDefTextId(String linkedRecordType, String textId, String defTextId) {
        DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);

        DataGroup textIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("textId");
        assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), linkedRecordType);
        assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), textId);

        DataGroup defTextIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("defTextId");
        assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), linkedRecordType);
        assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), defTextId);
    }

    @Test
    public void testModifyTextsInGroup() {
        dataModifier.modifyByRecordType("metadataGroup");

        assertCorrectModifiedGroupWithLinkedRecordTypeTextIdAndDefTextId("text", "someTestGroupText", "someTestGroupDefText");
        assertEquals(linkCollector.noOfTimesCalled, 1);
    }
}
