package info.elexis.server.core.p2.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FeatureInfo {
	private String id;
	private String version;

	public FeatureInfo() {
	}

	public FeatureInfo(String id, String version) {
		this.id = id;
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
