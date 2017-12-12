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
		if ("presentationRecordLink".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("presentationRecordLink",
					"presentationRecordLinkGroup");
		}
		return null;
	}

	@Override
	public void create(String s, String s1, DataGroup dataGroup, DataGroup collectedTerms,
			DataGroup dataGroup1, String s2) {

	}

	@Override
	public void deleteByTypeAndId(String s, String s1) {

	}

	@Override
	public boolean linksExistForRecord(String s, String s1) {
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup record, DataGroup collectdTerms,
			DataGroup linkList, String dataDivider) {
		modifiedDataGroupsSentToUpdate.add(record);
	}

	@Override
	public Collection<DataGroup> readList(String type, DataGroup filter) {
		List<DataGroup> recordList = new ArrayList<>();
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
					"someRecordType", "someRecordId", "0");
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
					"someRecordType", "someRecordId", "1");
			linkedRecordPresentations2.addChild(linkedRecordPresentation2);
			DataGroup linkedRecordPresentation3 = createLinkedPresentationWithTypeAndId(
					"someOtherRecordType", "someOtherRecordId", "2");
			linkedRecordPresentations2.addChild(linkedRecordPresentation3);
			pLinkWithTwoLinks.addChild(linkedRecordPresentations2);
			recordList.add(pLinkWithTwoLinks);

		}

		return recordList;
	}

	private DataGroup createLinkedPresentationWithTypeAndId(String linkedRecordType,
			String presentationId, String repeatId) {
		DataGroup linkedRecordPresentation = DataGroup.withNameInData("linkedRecordPresentation");
		linkedRecordPresentation
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		linkedRecordPresentation
				.addChild(DataAtomic.withNameInDataAndValue("presentationId", presentationId));
		linkedRecordPresentation.setRepeatId(repeatId);
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
	public Collection<DataGroup> readAbstractList(String type, DataGroup filter) {
		// TODO Auto-generated method stub
		return null;
	}
}
