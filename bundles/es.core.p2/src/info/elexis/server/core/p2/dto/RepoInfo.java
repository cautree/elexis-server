package info.elexis.server.core.p2.dto;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RepoInfo {

	private List<RepoElement> repositories = new ArrayList<RepoElement>();

	public List<RepoElement> getRepositories() {
		return repositories;
	}
	
	public void setRepositories(List<RepoElement> repositories) {
		this.repositories = repositories;
	}

	public void addRepository(URI uri) {
		repositories.add(new RepoElement(uri.toString()));
	}
}
