package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonModifierPresentationChildren extends JsonModifier{

    private static final String TYPE = "type";
	private static final String REF_MINIMIZED = "refMinimized";

	public JsonModifierPresentationChildren(String basePath) {
        this.basePath = basePath;
    }

    public void convertChildReferencesWithFilePrefix(String filePrefix) {
        readAndTransformJson(filePrefix);
    }


    protected void transformJSON(){
        for(Map.Entry<String, DividerGroup> entry : dataGroupList.entrySet()){
            DataGroup childReferences = extractChildReferencesFromEntry(entry);
            transformChildReferences(childReferences);

        }
    }

    private DataGroup extractChildReferencesFromEntry(Map.Entry<String, DividerGroup> entry) {
        DividerGroup dividerGroup = entry.getValue();
        DataGroup dataGroup = dividerGroup.dataGroup;
        return dataGroup.getFirstGroupWithNameInData("childReferences");
    }

    private void transformChildReferences(DataGroup childReferences) {
        List<DataElement> newChildrenList = new ArrayList<>();
        newChildrenList.addAll(childReferences.getChildren());

        for (DataElement dataElement : newChildrenList) {
            convertChild(dataElement);

        }
    }

    private void convertChild(DataElement childElement) {
        if("childReference".equals(childElement.getNameInData())){
            DataGroup childReference = (DataGroup) childElement;
            convertRef(childReference);
            convertRefMinimized(childReference);
        }
    }

	private void convertRef(DataGroup childReference) {
		if(childReference.containsChildWithNameInData("ref")){
			DataGroup ref = extractAndThenRemoveRef(childReference);	
			DataGroup refGroup = DataGroup.withNameInData("refGroup");
			refGroup.addChild(ref);
			childReference.addChild(refGroup);
		}
	}

	private DataGroup extractAndThenRemoveRef(DataGroup childReference) {
		return extractAndRemoveChildDataGroup(childReference, "ref");
	}
	
	private DataGroup extractAndRemoveChildDataGroup(DataGroup childReference, String nameInData) {
		DataGroup refMin = childReference.getFirstGroupWithNameInData(nameInData);
		childReference.removeFirstChildWithNameInData(nameInData);
		return refMin;
	}

	private void convertRefMinimized(DataGroup childReference) {
		if(childReference.containsChildWithNameInData(REF_MINIMIZED)){
			DataGroup ref = renameRefMinimizedToRef(childReference);
			DataGroup refMinGroup = DataGroup.withNameInData("refMinGroup");
			refMinGroup.addChild(ref);
			childReference.addChild(refMinGroup);
			
		}
	}

	private DataGroup renameRefMinimizedToRef(DataGroup childReference) {
		DataGroup refMin = extractAndRemoveRefMinimized(childReference);
		DataGroup ref = DataGroup.withNameInData("ref");
        addDataFromRefMinimizedToRef(refMin, ref);
		return ref;
	}

	private DataGroup extractAndRemoveRefMinimized(DataGroup childReference) {
		return extractAndRemoveChildDataGroup(childReference, REF_MINIMIZED);
	}

    private void addDataFromRefMinimizedToRef(DataGroup refMin, DataGroup ref) {
        addChildrenFromRefMinToRef(refMin, ref);
        addAttributesFromRefMinToRef(refMin, ref);
    }

    private void addAttributesFromRefMinToRef(DataGroup refMin, DataGroup ref) {
		ref.addAttributeByIdWithValue(TYPE, refMin.getAttribute(TYPE));
		addRepeatAttributeIfContainer(refMin, ref);
	}

	private void addRepeatAttributeIfContainer(DataGroup refMin, DataGroup ref) {
		if("container".equals(refMin.getAttribute(TYPE))){
			ref.addAttributeByIdWithValue("repeat", refMin.getAttribute("repeat"));
		}
	}

	private void addChildrenFromRefMinToRef(DataGroup refMin, DataGroup ref) {
		for(DataElement dataElement : refMin.getChildren()){
			ref.addChild(dataElement);
		}
	}

}
