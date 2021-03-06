/*
 * Copyright 2015, 2018 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;

public class DataRecordLinkCollectorSpy implements DataRecordLinkCollector {

	public boolean collectLinksWasCalled = false;
	public String metadataId = null;
	public int noOfTimesCalled = 0;
	public List<String> metadataIds = new ArrayList<>();
	public List<String> fromRecordTypes = new ArrayList<>();
	public List<String> fromRecordIds = new ArrayList<>();
	public List<DataGroup> dataGroups = new ArrayList<>();

	public DataGroup collectedDataLinks = DataGroup.withNameInData("collectedDataLinks");

	@Override
	public DataGroup collectLinks(String metadataId, DataGroup dataGroup, String fromRecordType,
			String fromRecordId) {
		this.metadataId = metadataId;
		collectLinksWasCalled = true;
		noOfTimesCalled++;
		metadataIds.add(metadataId);
		fromRecordTypes.add(fromRecordType);
		fromRecordIds.add(fromRecordId);
		dataGroups.add(dataGroup);
		return collectedDataLinks;
	}

}
