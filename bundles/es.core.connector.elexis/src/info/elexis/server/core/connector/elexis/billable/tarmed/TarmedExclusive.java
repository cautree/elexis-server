package info.elexis.server.core.connector.elexis.billable.tarmed;

import java.util.List;

import ch.rgw.tools.TimeTool;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.TarmedGroup;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.TarmedKumulation;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.TarmedLeistung;
import info.elexis.server.core.connector.elexis.services.TarmedLeistungService;

public class TarmedExclusive {

	private String slaveCode;
	private TarmedKumulationType slaveType;

	public TarmedExclusive(TarmedKumulation kumulation) {
		slaveCode = kumulation.getSlaveCode();
		slaveType = TarmedKumulationType.ofArt(kumulation.getSlaveArt());
	}

	public boolean isMatching(TarmedLeistung tarmedLeistung, TimeTool date) {
		if (slaveType == TarmedKumulationType.CHAPTER) {
			return isMatchingChapter(tarmedLeistung);
		} else if (slaveType == TarmedKumulationType.SERVICE) {
			return slaveCode.equals(tarmedLeistung.getCode());
		} else if (slaveType == TarmedKumulationType.GROUP) {
			List<String> groups = tarmedLeistung.getServiceGroups(date);
			return groups.contains(slaveCode);
		} else if (slaveType == TarmedKumulationType.BLOCK) {
			List<String> blocks = tarmedLeistung.getServiceBlocks(date);
			return blocks.contains(slaveCode);
		}
		return false;
	}

	private boolean isMatchingChapter(TarmedLeistung tarmedLeistung) {
		if (slaveCode.equals(tarmedLeistung.getCode())) {
			return true;
		} else {
			String parentId = tarmedLeistung.getParent();
			if (parentId != null && !parentId.equals("NIL")) {
				return isMatchingChapter(TarmedLeistungService.load(parentId).get());
			} else {
				return false;
			}
		}
	}

	public boolean isMatching(TarmedGroup tarmedGroup) {
		if (slaveType != TarmedKumulationType.GROUP) {
			return false;
		}
		return slaveCode.equals(tarmedGroup.getCode());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(TarmedKumulationType.toString(slaveType)).append(" ").append(slaveCode);
		return sb.toString();
	}

	public TarmedKumulationType getSlaveType() {
		return slaveType;
	}
}
