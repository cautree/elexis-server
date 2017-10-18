package info.elexis.server.core.connector.elexis.jpa;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.core.constants.StringConstants;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.AbstractDBObjectIdDeleted;

/**
 * Resolve storeToString entities into the resp. objects, and serialize those
 * entities into storeToString strings.
 */
public enum StoreToStringService {

	INSTANCE;

	private static Logger log = LoggerFactory.getLogger(StoreToStringService.class);

	/**
	 * Find an object according to its storeToString
	 * 
	 * @param storeToString
	 * @return a detached {@link AbstractDBObjectIdDeleted}
	 */
	public Optional<AbstractDBObjectIdDeleted> createDetachedFromString(String storeToString) {
		if (storeToString == null) {
			log.warn("StoreToString is null");
			return Optional.empty();
		}

		String[] split = info.elexis.server.core.service.StoreToStringService.splitIntoTypeAndId(storeToString);

		// map string to classname
		String className = split[0];
		String id = split[1];
		Class<? extends AbstractDBObjectIdDeleted> clazz = ElexisTypeMap.get(className);
		if (clazz == null) {
			log.warn("Could not resolve class [{}] from [{}]", className, storeToString);
			return Optional.empty();
		}

		EntityManager em = ProvidedEntityManager.em();
		try {
			return Optional.ofNullable(em.find(clazz, id));
		} finally {
			em.close();
		}
	}

	/**
	 * Generate an Elexis compatible store to string. This requires the mapping
	 * of local entities to their Elexis-respective, Persistent-object-based
	 * counter entities.
	 * 
	 * @param adbo
	 * @return
	 */
	public static String storeToString(AbstractDBObjectIdDeleted adbo) {
		String classKey = ElexisTypeMap.getKeyForObject(adbo);
		if (classKey == null) {
			log.warn("Could not resolve [{}] to storeToString name", (adbo != null) ? adbo.getClass() : "null");
			return null;
		}

		return classKey + StringConstants.DOUBLECOLON + adbo.getId();
	}
}
