package se.uu.ub.cora.datamodifier.implementing;

import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbstractLinkToImplementingLinkModifier extends DataModifierForRecordType {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		for(DataElement dataElement : dataGroup.getChildren())
			if (dataElement instanceof DataGroup) {
				DataGroup child = (DataGroup) dataElement;
				if (child.containsChildWithNameInData("linkedRecordType")) {
					String linkedRecordType = child.getFirstAtomicValueWithNameInData("linkedRecordType");
					String linkedRecordId = child.getFirstAtomicValueWithNameInData("linkedRecordId");
					Collection<DataGroup> recordTypes = recordStorage.readList("recordType", DataGroup.withNameInData("filter"));
					List<String> implementingRecordTypes = new ArrayList<>();
					for (DataGroup recordType : recordTypes){
						if(recordType.containsChildWithNameInData("parentId")){

							String parentId = recordType.getFirstAtomicValueWithNameInData("parentId");
							if(parentId.equals(linkedRecordType)){
								DataGroup recordInfo = recordType.getFirstGroupWithNameInData("recordInfo");

								implementingRecordTypes.add(recordInfo.getFirstAtomicValueWithNameInData("id"));
							}
						}

					}
				}
			}
	}

}
