package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class JsonModifierRecordTypeLinks extends JsonModifier {

    public JsonModifierRecordTypeLinks(String basePath) {
        this.basePath = basePath;
    }

    public void convertAtomicDataToLinks(String filePrefix) {
        readAndTransformJson(filePrefix);
    }


    protected void transformJSON() {
        for (Entry<String, DividerGroup> entry : dataGroupList.entrySet()) {
            DividerGroup dividerGroup = entry.getValue();
            DataGroup dataGroup = dividerGroup.dataGroup;
            transformChildren(dataGroup);

        }
    }

    private void transformChildren(DataGroup dataGroup) {
        List<DataElement> newChildrenList = new ArrayList<>();
        newChildrenList.addAll(dataGroup.getChildren());

        for (DataElement dataElement : newChildrenList) {
            convertChild(dataGroup, dataElement);

        }
    }

    private void convertChild(DataGroup dataGroup, DataElement dataElement) {
        String nameInData = dataElement.getNameInData();
        String linkedRecordType = getLinkedRecordType(nameInData);
        if (!"".equals(linkedRecordType)) {
            dataGroup.removeFirstChildWithNameInData(nameInData);
            DataGroup childDataGroup = DataGroup.withNameInData(nameInData);
            childDataGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
            childDataGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", ((DataAtomic) dataElement).getValue()));
            dataGroup.addChild(childDataGroup);
        }
    }

    private String getLinkedRecordType(String nameInData) {
        switch (nameInData) {
            case "metadataId":
            case "newMetadataId":
                return "metadataGroup";
            case "presentationViewId":
            case "presentationFormId":
            case "newPresentationFormId":
            case "listPresentationViewId":
            case "menuPresentationViewId":
                return "presentationGroup";
            case "parentId":
                return "recordType";
            case "textId":
            case "defTextId":
                return "coraText";
            default:
                return "";
        }
    }
}
