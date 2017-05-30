package se.uu.ub.cora.datamodifier.presentation;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.datamodifier.metadata.RecordStorageForAtomicTextsToLinksSpy;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ModifierForAtomicPresentationOfToLinkInRepeatingContainerTest {
    private RecordStorageForPresentationContainersSpy recordStorage;
    private DataRecordLinkCollectorSpy linkCollector;
    private ModifierForAtomicPresentationOfToLinkInRepeatingContainer dataModifier;

    @BeforeMethod
    public void setUp() {
        linkCollector = new DataRecordLinkCollectorSpy();
        recordStorage = new RecordStorageForPresentationContainersSpy();

        dataModifier = new ModifierForAtomicPresentationOfToLinkInRepeatingContainer();
        dataModifier.setLinkCollector(linkCollector);
        dataModifier.setRecordStorage(recordStorage);
    }

    @Test
    public void testInit() {
        assertNotNull(dataModifier.getLinkCollector());
        assertNotNull(dataModifier.getRecordStorage());
    }

    @Test
    public void testModifyPresentationOf() {
        dataModifier.modifyByRecordType("presentationRepeatingContainer");
        DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
        DataGroup presentationOf = modifiedDataGroup.getFirstGroupWithNameInData("presentationOf");
        assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType"),
                "metadata");
        assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"),
                "someLink");
    }
}
