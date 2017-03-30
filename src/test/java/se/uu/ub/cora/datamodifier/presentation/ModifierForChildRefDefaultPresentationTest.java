package se.uu.ub.cora.datamodifier.presentation;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.datamodifier.RecordStorageSpy;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class ModifierForChildRefDefaultPresentationTest {
    private RecordStorage recordStorage;
    private DataRecordLinkCollectorSpy linkCollector;
    protected ModifierForChildRefDefaultPresentation dataModifier;

    @BeforeMethod
    public void setUp() {
        dataModifier = new ModifierForChildRefDefaultPresentation();
        linkCollector = new DataRecordLinkCollectorSpy();
        recordStorage = new RecordStorageForStyleAndDefaultPresentationSpy();
        dataModifier.setLinkCollector(linkCollector);
        dataModifier.setRecordStorage(recordStorage);
    }


    @Test
    public void testInit(){
        assertNotNull(dataModifier.recordStorage);
        assertNotNull(dataModifier.linkCollector);
    }

    @Test
    public void testModifyDefaultPresentation(){
        dataModifier.modifyByRecordType("presentationGroup");

        RecordStorageForStyleAndDefaultPresentationSpy recordStorageSpy = ((RecordStorageForStyleAndDefaultPresentationSpy) recordStorage);
        assertCorrectChildrenInFirstModifiedDataGroup(recordStorageSpy);

        assertCorrectChildrenInSecondModifiedDataGroup(recordStorageSpy);
    }

    private void assertCorrectChildrenInSecondModifiedDataGroup(RecordStorageForStyleAndDefaultPresentationSpy recordStorageSpy) {
        List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 1);
        assertToRefGroupsInFirstChildReference(children);

        assertFormerMinGroupIsFirst(children);
    }

    private void assertToRefGroupsInFirstChildReference(List<DataGroup> children) {
        DataGroup firstChildReference = children.get(0);
        assertFalse(firstChildReference.containsChildWithNameInData("default"));
        DataGroup refGroup = firstChildReference.getFirstGroupWithNameInData("refGroup");
        assertEquals(refGroup.getRepeatId(), "0");
        assertEquals(firstChildReference.getAllGroupsWithNameInData("refGroup").size(), 2);
    }

    private void assertFormerMinGroupIsFirst(List<DataGroup> children) {
        DataGroup secondChildReference = children.get(1);
        assertFalse(secondChildReference.containsChildWithNameInData("default"));
        DataGroup secondRefGroup1 = secondChildReference.getAllGroupsWithNameInData("refGroup").get(0);
        assertEquals(secondRefGroup1.getRepeatId(), "1");
        DataGroup ref = secondRefGroup1.getFirstGroupWithNameInData("ref");
        assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"), "userRoleMinimizedOutputPLink");

        DataGroup secondRefGroup2 = secondChildReference.getAllGroupsWithNameInData("refGroup").get(1);
        assertEquals(secondRefGroup2.getRepeatId(), "0");
        DataGroup ref2 = secondRefGroup2.getFirstGroupWithNameInData("ref");
        assertEquals(ref2.getFirstAtomicValueWithNameInData("linkedRecordId"), "someText");
    }

    private void assertCorrectChildrenInFirstModifiedDataGroup(RecordStorageForStyleAndDefaultPresentationSpy recordStorageSpy) {
        List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 0);
        DataGroup firstChildReference = children.get(0);

        assertFalse(firstChildReference.containsChildWithNameInData("default"));

        DataGroup refGroup = firstChildReference.getFirstGroupWithNameInData("refGroup");
        assertEquals(refGroup.getRepeatId(), "0");
        assertEquals(firstChildReference.getAllGroupsWithNameInData("refGroup").size(), 1);
    }

    private List<DataGroup> extractChildrenFromModifiedByIndex(
            RecordStorageForStyleAndDefaultPresentationSpy recordStorageSpy, int index) {
        DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(index);
        DataGroup childReferences = modifiedDataGroup
                .getFirstGroupWithNameInData("childReferences");

        List<DataGroup> children = childReferences.getAllGroupsWithNameInData("childReference");
        return children;
    }

    @Test
    public void testModifyDefaultPresentationInResourceLink(){
        recordStorage = new RecordStorageSpy();
        dataModifier.setRecordStorage(recordStorage);
        dataModifier.modifyByRecordType("presentationResourceLink");

        RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);
        DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(0);
        DataGroup childReferences = modifiedDataGroup
                .getFirstGroupWithNameInData("childReferences");

        List<DataGroup> children = childReferences.getAllGroupsWithNameInData("childReference");
        DataGroup firstChildReference = children.get(0);
        assertFalse(firstChildReference.containsChildWithNameInData("default"));
    }

}
