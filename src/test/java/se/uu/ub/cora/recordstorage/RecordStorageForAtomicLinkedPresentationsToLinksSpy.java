package se.uu.ub.cora.recordstorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForAtomicLinkedPresentationsToLinksSpy implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	public List<DataGroup> createdData = new ArrayList<>();
	public List<String> createdType = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		// if ("metadataCollectionItem".equals(id)) {
		// readRecordTypes.add(id);
		// return
		// DataCreator.createRecordTypeWithMetadataId("metadataCollectionItem",
		// "metadataCollectionItemGroup");
		// }
		// if ("metadataGroup".equals(id)) {
		// readRecordTypes.add(id);
		// return DataCreator.createRecordTypeWithMetadataId("metadataGroup",
		// "metadataGroupGroup");
		// }
		// if ("presentationVar".equals(id)) {
		// readRecordTypes.add(id);
		// return DataCreator.createRecordTypeWithMetadataId("presentationVar",
		// "presentationVarGroup");
		// }
		if ("presentationRecordLink".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("presentationRecordLink",
					"presentationRecordLinkGroup");
		}
		return null;
	}

	@Override
	public void create(String s, String s1, DataGroup dataGroup, DataGroup dataGroup1, String s2) {

	}

	@Override
	public void deleteByTypeAndId(String s, String s1) {

	}

	@Override
	public boolean linksExistForRecord(String s, String s1) {
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
		modifiedDataGroupsSentToUpdate.add(record);
	}

	@Override
	public Collection<DataGroup> readList(String type) {
		List<DataGroup> recordList = new ArrayList<>();
		// if ("metadataCollectionItem".equals(type)) {
		// DataGroup collectionItem = DataCreator
		// .createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someItem",
		// "metadata",
		// "collectionItem", "testSystem");
		// collectionItem.addChild(DataAtomic.withNameInDataAndValue("nameInData",
		// "some"));
		// collectionItem.addChild(DataAtomic.withNameInDataAndValue("textId",
		// "someText"));
		// collectionItem.addChild(DataAtomic.withNameInDataAndValue("defTextId",
		// "someDefText"));
		// recordList.add(collectionItem);
		//
		// }
		// if ("metadataGroup".equals(type)) {
		// DataGroup metadataGroup = DataCreator
		// .createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someTestGroup",
		// "metadata", "metadataGroup", "testSystem");
		// metadataGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData",
		// "some"));
		// metadataGroup
		// .addChild(DataAtomic.withNameInDataAndValue("textId",
		// "someTestGroupText"));
		// metadataGroup.addChild(
		// DataAtomic.withNameInDataAndValue("defTextId",
		// "someTestGroupDefText"));
		// recordList.add(metadataGroup);
		//
		// }
		if ("presentationRecordLink".equals(type)) {
			DataGroup pLinkWithoutLink = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
							"textIdWithoutOutputPLink", "presentation", "presentationRecordLink",
							"testSystem");

			DataGroup presentationOf = DataGroup.withNameInData("presentationOf");
			presentationOf.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataRecordLink"));
			presentationOf
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "textIdLink"));
			pLinkWithoutLink.addChild(presentationOf);

			pLinkWithoutLink.addChild(DataAtomic.withNameInDataAndValue("mode", "output"));

			pLinkWithoutLink.addAttributeByIdWithValue("type", "pRecordLink");
			recordList.add(pLinkWithoutLink);

			DataGroup pLinkWithLink = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("textIdOutputPLink",
							"presentation", "presentationRecordLink", "testSystem");
			pLinkWithLink.addChild(presentationOf);

			DataGroup linkedRecordPresentations = DataGroup
					.withNameInData("linkedRecordPresentations");
			DataGroup linkedRecordPresentation = createLinkedPresentationWithTypeAndId(
					"someRecordType", "someRecordId");
			linkedRecordPresentations.addChild(linkedRecordPresentation);
			pLinkWithLink.addChild(linkedRecordPresentations);
			recordList.add(pLinkWithLink);

			DataGroup pLinkWithTwoLinks = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("textIdOutputPLink",
							"presentation", "presentationRecordLink", "testSystem");
			pLinkWithTwoLinks.addChild(presentationOf);

			DataGroup linkedRecordPresentations2 = DataGroup
					.withNameInData("linkedRecordPresentations");
			DataGroup linkedRecordPresentation2 = createLinkedPresentationWithTypeAndId(
					"someRecordType", "someRecordId");
			linkedRecordPresentations2.addChild(linkedRecordPresentation2);
			DataGroup linkedRecordPresentation3 = createLinkedPresentationWithTypeAndId(
					"someOtherRecordType", "someOtherRecordId");
			linkedRecordPresentations2.addChild(linkedRecordPresentation3);
			pLinkWithTwoLinks.addChild(linkedRecordPresentations2);
			recordList.add(pLinkWithTwoLinks);

			// DataGroup pVarWithNoEmptyTextId = DataCreator
			// .createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
			// "anotherPresentationRefPVar", "presentation", "presentationVar",
			// "testSystem");
			// pVarWithNoEmptyTextId.addAttributeByIdWithValue("type", "pVar");
			// recordList.add(pVarWithNoEmptyTextId);
			// }
			// if ("presentationCollectionVar".equals(type)) {
			// DataGroup pCollVar =
			// DataCreator.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
			// "inputTypeCollectionVarPCollVar", "presentation",
			// "presentationCollectionVar", "testSystem");
			// pCollVar.addChild(DataAtomic.withNameInDataAndValue("emptyTextId",
			// "initialEmptyValueText"));
			// pCollVar.addAttributeByIdWithValue("type", "pCollVar");
			// recordList.add(pCollVar);
			//
			// DataGroup pCollVarWithNoEmptyTextId = DataCreator
			// .createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
			// "anotherPresentationPCollVar", "presentation",
			// "presentationCollectionVar",
			// "testSystem");
			// pCollVarWithNoEmptyTextId.addAttributeByIdWithValue("type",
			// "pCollVar");
			// recordList.add(pCollVarWithNoEmptyTextId);
		}

		return recordList;
	}

	private DataGroup createLinkedPresentationWithTypeAndId(String linkedRecordType,
			String presentationId) {
		DataGroup linkedRecordPresentation = DataGroup.withNameInData("linkedRecordPresentation");
		linkedRecordPresentation
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		linkedRecordPresentation
				.addChild(DataAtomic.withNameInDataAndValue("presentationId", presentationId));
		return linkedRecordPresentation;
	}

	@Override
	public DataGroup readLinkList(String s, String s1) {
		return null;
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String s, String s1) {
		return null;
	}

	@Override
	public boolean recordsExistForRecordType(String s) {
		return false;
	}

	@Override
	public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String s, String s1) {
		return false;
	}

	@Override
	public Collection<DataGroup> readAbstractList(String type) {
		// TODO Auto-generated method stub
		return null;
	}
}
