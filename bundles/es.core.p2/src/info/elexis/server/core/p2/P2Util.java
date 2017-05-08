package info.elexis.server.core.p2;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.core.status.ObjectStatus;
import info.elexis.server.core.p2.dto.FeatureInfo;
import info.elexis.server.core.p2.dto.RepoElement;
import info.elexis.server.core.p2.dto.RepoInfo;
import info.elexis.server.core.p2.internal.BundleConstants;
import info.elexis.server.core.p2.internal.Provisioner;
import info.elexis.server.core.p2.internal.ProvisioningHelper;

public class P2Util {

	private static Logger log = LoggerFactory.getLogger(P2Util.class);

	private IMetadataRepositoryManager metadataRepoMgr = Provisioner.getInstance().getMetadataRepositoryManager();
	private IArtifactRepositoryManager articaftRepoMgr = Provisioner.getInstance().getArtifactRepositoryManager();

	public RepoInfo getRepoInfo() {
		RepoInfo info = new RepoInfo();

		HashSet<URI> metadataRepositories = new HashSet<>();
		if (metadataRepoMgr != null) {
			metadataRepositories = new HashSet<URI>(
					Arrays.asList(metadataRepoMgr.getKnownRepositories(IRepositoryManager.REPOSITORIES_ALL)));
		} else {
			log.warn("metadataRepoMgr is null");
		}
		HashSet<URI> artifactRepositories = new HashSet<>();
		if (articaftRepoMgr != null) {
			artifactRepositories = new HashSet<URI>(
					Arrays.asList(articaftRepoMgr.getKnownRepositories(IRepositoryManager.REPOSITORIES_ALL)));
		} else {
			log.warn("articaftRepoMgr is null");
		}

		metadataRepositories.retainAll(artifactRepositories);

		for (URI uri : metadataRepositories) {
			info.addRepository(uri);
		}

		return info;
	}

	public IStatus doRepositoryAdd(String locationString, String username, String password) {
		URI location = null;
		try {
			locationString = URLDecoder.decode(locationString, "ASCII");
			location = new URI(locationString);
		} catch (URISyntaxException | UnsupportedEncodingException e) {
			log.warn("Exception parsing URI " + locationString, e);
			return new Status(Status.ERROR, BundleConstants.BUNDLE_ID, e.getLocalizedMessage(), e);
		}

		if (location.isAbsolute()) {
			ProvisioningHelper.addRepository(location, username, password);
			return Status.OK_STATUS;
		}

		String warn = "Tried to add non absolute location: " + location;
		log.warn(warn);
		return new Status(Status.WARNING, BundleConstants.BUNDLE_ID, warn);
	}

	public IStatus doRepositoryRemove(String id) {
		RepoElement repo = fetchRepoById(id);
		if (repo != null) {
			URI locationURI;
			try {
				locationURI = repo.getLocationAsUri();
				boolean result = ProvisioningHelper.removeRepository(locationURI);
				if (result) {
					return Status.OK_STATUS;
				}
			} catch (URISyntaxException e) {
				return new Status(Status.ERROR, BundleConstants.BUNDLE_ID, e.getMessage());
			}

		}
		return new Status(Status.ERROR, BundleConstants.BUNDLE_ID, "Could not resolve repository with id [" + id + "]");
	}

	public IStatus doUpdateAllFeatures() {
		return ProvisioningHelper.updateAllFeatures();
	}

	public IStatus doFeatureInstall(String featureName) {
		// TODO Auto-generated method stub
		return null;
	}

	public IStatus doFeatureUninstall(String featureName) {
		// TODO Auto-generated method stub
		return null;
	}

	public IStatus getRepositoryStatus(String id) {
		RepoElement repoElement = fetchRepoById(id);
		if (repoElement != null) {
			try {
				URI location = repoElement.getLocationAsUri();
				articaftRepoMgr.loadRepository(location,
						new NullProgressMonitor());
				IMetadataRepository metadataRepositoryManager = metadataRepoMgr.loadRepository(location,
						new NullProgressMonitor());
				repoElement.setName(metadataRepositoryManager.getName());
				repoElement.setVersion(metadataRepositoryManager.getVersion());

				IQueryResult<IInstallableUnit> result = metadataRepositoryManager.query(QueryUtil.createIUGroupQuery(),
						new NullProgressMonitor());
				result.iterator().forEachRemaining(entry -> repoElement
						.addInstallableUnit(new FeatureInfo(entry.getId(), entry.getVersion().toString())));

				return ObjectStatus.OK_STATUS(repoElement);
			} catch (ProvisionException | URISyntaxException e) {
				return new Status(Status.ERROR, BundleConstants.BUNDLE_ID, e.getMessage());
			}
		}
		return new Status(Status.ERROR, BundleConstants.BUNDLE_ID, "Could not resolve repository with id [" + id + "]");
	}

	private RepoElement fetchRepoById(String id) {
		RepoInfo repoInfo = getRepoInfo();
		for (RepoElement repoElement : repoInfo.getRepositories()) {
			if (id.equals(repoElement.getId())) {
				return repoElement;
			}
		}
		return null;
	}

}
