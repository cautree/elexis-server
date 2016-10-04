package info.elexis.server.core.connector.elexis.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ch.rgw.io.FileTool;
import info.elexis.server.core.common.LocalProperties;
import info.elexis.server.core.connector.elexis.Properties;
import info.elexis.server.core.connector.elexis.internal.ElexisEntityManager;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.DocHandle;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.DocHandle_;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Kontakt;

public class DocHandleService extends AbstractService<DocHandle> {

	public static DocHandleService INSTANCE = InstanceHolder.INSTANCE;

	private static final class InstanceHolder {
		static final DocHandleService INSTANCE = new DocHandleService();
	}

	private DocHandleService() {
		super(DocHandle.class);
	}

	/**
	 * 
	 * @param contact
	 * @param title
	 * @param filename
	 *            the filename, has to include the resp. ending on mimetype
	 *            (e.g. <code>.pdf</code>);
	 * @param category
	 *            if <code>null</code> defaults to "default"
	 * @param document
	 *            <code>null</code> if to be stored on a network file system
	 *            path
	 * @return
	 */
	public DocHandle create(Kontakt contact, String title, String filename, String category, byte[] document) {
		em.getTransaction().begin();
		DocHandle docHandle = create(false);
		em.merge(contact);
		docHandle.setKontakt(contact);
		docHandle.setDatum(LocalDate.now());
		docHandle.setCategory((category != null) ? category : "default");
		docHandle.setMimetype(filename);
		docHandle.setTitle(title);

		omnivoreStoreContentConsideringNetworkPathStoreIfRequired(docHandle, document);

		em.getTransaction().commit();
		return docHandle;
	}

	/**
	 * 
	 * @return all omnivore categories available
	 */
	public static List<DocHandle> omnivoreGetAllCategories() {
		EntityManager em = ElexisEntityManager.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DocHandle> cq = cb.createQuery(DocHandle.class);
		Root<DocHandle> root = cq.from(DocHandle.class);
		Predicate like = cb.like(root.get(DocHandle_.mimetype), "%text/category%");
		cq = cq.where(like);
		TypedQuery<DocHandle> q = em.createQuery(cq);
		return q.getResultList();
	}

	protected static final String CONFIG_OMNIVORE_STORE_GLOBAL = "ch.elexis.omnivore/store_in_fs_global";
	protected static final String CONFIG_OMNIVORE_STORE_IN_FS = "ch.elexis.omnivore/store_in_fs";

	/**
	 * Retrieve an omnivore stored file, considering possible file system
	 * storage.
	 * 
	 * @param docHandle
	 * @return
	 */
	public static byte[] omnivoreGetContentConsideringNetworkPathStoreIfRequired(DocHandle docHandle) {
		byte[] doc = docHandle.getDoc();
		if (doc == null) {
			boolean storeGlobal = ConfigService.INSTANCE.get(CONFIG_OMNIVORE_STORE_GLOBAL, false);
			if (storeGlobal) {
				boolean storeFs = ConfigService.INSTANCE.get(CONFIG_OMNIVORE_STORE_IN_FS, false);
				if (storeFs) {
					File networkStorageFile = omnivoreDetermineNetworkStorageFile(docHandle);
					if (networkStorageFile != null && networkStorageFile.canRead()) {
						try {
							return Files.readAllBytes(networkStorageFile.toPath());
						} catch (IOException e) {
							log.warn("Error reading file [{}], returning null.", networkStorageFile.getAbsolutePath(),
									e);
						}
					}
				}
			}
		}
		return doc;
	}

	/**
	 * Store the data for the given {@link DocHandle}. If configured and
	 * functional, the data will be stored at the respective network path
	 * location. If not, and in case of error, data is stored in database.
	 * 
	 * @param docHandle
	 * @param data
	 */
	public static void omnivoreStoreContentConsideringNetworkPathStoreIfRequired(DocHandle docHandle, byte[] data) {
		boolean storeGlobal = ConfigService.INSTANCE.get(CONFIG_OMNIVORE_STORE_GLOBAL, false);
		if (storeGlobal) {
			boolean storeFs = ConfigService.INSTANCE.get(CONFIG_OMNIVORE_STORE_IN_FS, false);
			if (storeFs) {
				File networkStorageFile = omnivoreDetermineNetworkStorageFile(docHandle);
				if (networkStorageFile != null) {
					networkStorageFile.getParentFile().mkdirs();
					try {
						Files.write(networkStorageFile.toPath(), data);
						docHandle.setDoc(null);
						return;
					} catch (IOException e) {
						log.error(
								"Error storing file on network configure network location [{}], reverting to DB store.",
								e);
					}
				}
			}
		}
		docHandle.setDoc(data);
	}

	/**
	 * Determine the File that is bound to store the data for the given
	 * docHandle. This method does not guarantee that the file exists, nor that
	 * the required path exists, it simply provides the respective File handler.
	 * 
	 * @param docHandle
	 * @return the file handle, or <code>null</code> if none or invalid storage
	 *         path configured or other error
	 */
	public static File omnivoreDetermineNetworkStorageFile(DocHandle docHandle) {
		String pathname = LocalProperties.getProperty(Properties.PROPERTY_OMNIVORE_NETWORK_PATH, null);
		if (pathname != null) {
			File dir = new File(pathname);
			if (dir.isDirectory() && dir.canRead()) {
				if (docHandle.getKontakt() != null && docHandle.getKontakt().getPatientNr() != null) {
					String patientNr = docHandle.getKontakt().getPatientNr();
					if (!patientNr.isEmpty()) {
						File subdir = new File(dir, patientNr.trim());
						return new File(subdir,
								docHandle.getId() + "." + FileTool.getExtension(docHandle.getMimetype()));
					} else {
						log.warn("Invalid patient number for contact in docHandle [{}].", docHandle.getId());
					}
				} else {
					log.warn("Invalid patient contact for docHandle [{}].", docHandle.getId());
				}
			} else {
				log.warn("Provided network storage path [{}] is not a directory, or directory not accessible.",
						dir.getAbsolutePath());
			}
		} else {
			log.error("Requested network storage file name for [{}], but network path not configured.",
					docHandle.getId());
		}
		return null;
	}
}
