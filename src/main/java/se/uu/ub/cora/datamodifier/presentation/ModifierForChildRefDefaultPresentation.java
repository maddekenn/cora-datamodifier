package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import java.util.List;

public class ModifierForChildRefDefaultPresentation extends DataModifierImp {


    protected void modifyChildReference(DataGroup childReference) {
        String defaultValue = childReference.getFirstAtomicValueWithNameInData("default");
        childReference.removeFirstChildWithNameInData("default");

        DataGroup refGroup = childReference.getFirstGroupWithNameInData("refGroup");
        refGroup.setRepeatId("0");

        possiblyModifyRefMinGroup(childReference);
        changeOrderOfGroupsIfRefMinIsDefault(childReference, defaultValue, refGroup);
    }

    private void possiblyModifyRefMinGroup(DataGroup childReference) {
        if(refMinGroupExists(childReference)){
            modifyRefMinGroup(childReference);
        }
    }

    private boolean refMinGroupExists(DataGroup childReference) {
        return childReference.containsChildWithNameInData("refMinGroup");
    }

    private void modifyRefMinGroup(DataGroup childReference) {
        DataGroup refMinGroup = childReference.getFirstGroupWithNameInData("refMinGroup");
        DataGroup newRefGroup = changeNameInDataForMinGroup(refMinGroup);
        newRefGroup.setRepeatId("1");

        removeOldMinGroupAndAddNewRefGroupToChildReference(childReference, newRefGroup);
    }

    private DataGroup changeNameInDataForMinGroup(DataGroup refMinGroup) {
        List<DataElement> children = refMinGroup.getChildren();
        DataGroup newRefGroup = DataGroup.withNameInData("refGroup");
        children.forEach(newRefGroup::addChild);
        return newRefGroup;
    }

    private void removeOldMinGroupAndAddNewRefGroupToChildReference(DataGroup childReference, DataGroup newRefGroup) {
        childReference.removeFirstChildWithNameInData("refMinGroup");
        childReference.addChild(newRefGroup);
    }

    private void changeOrderOfGroupsIfRefMinIsDefault(DataGroup childReference, String defaultValue, DataGroup refGroup) {
        if("refMin".equals(defaultValue)){
            childReference.removeFirstChildWithNameInData("refGroup");
            childReference.addChild(refGroup);
        }
    }
}
