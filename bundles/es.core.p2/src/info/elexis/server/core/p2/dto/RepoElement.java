package info.elexis.server.core.p2.dto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RepoElement {
	private String id;
	private String location;
	private String username;
	private String password;

	private String name;
	private String version;
	private List<FeatureInfo> features = new ArrayList<FeatureInfo>();

	public RepoElement() {
	}

	public RepoElement(String location) {
		this.id = Integer.toString(location.hashCode());
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public URI getLocationAsUri() throws URISyntaxException {
		return new URI(getLocation());
	}

	@Override
	public String toString() {
		return "RepoElement [id=" + id + ", location=" + location + ", username=" + username + "]";
	}

	public void setFeatures(List<FeatureInfo> features) {
		this.features = features;
	}

	public List<FeatureInfo> getFeatures() {
		return features;
	}

	public void addInstallableUnit(FeatureInfo featureInfo) {
		features.add(featureInfo);
	}
}