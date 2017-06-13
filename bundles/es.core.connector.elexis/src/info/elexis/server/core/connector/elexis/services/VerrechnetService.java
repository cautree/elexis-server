package info.elexis.server.core.connector.elexis.services;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.core.constants.StringConstants;
import ch.elexis.core.model.article.IArticle;
import ch.elexis.core.status.ObjectStatus;
import ch.rgw.tools.TimeTool;
import info.elexis.server.core.connector.elexis.billable.IBillable;
import info.elexis.server.core.connector.elexis.billable.VerrechenbarArtikel;
import info.elexis.server.core.connector.elexis.billable.VerrechenbarArtikelstammItem;
import info.elexis.server.core.connector.elexis.billable.VerrechenbarEigenleistung;
import info.elexis.server.core.connector.elexis.billable.VerrechenbarLabor2009Tarif;
import info.elexis.server.core.connector.elexis.billable.VerrechenbarTarmedLeistung;
import info.elexis.server.core.connector.elexis.billable.adjuster.VatVerrechnetAdjuster;
import info.elexis.server.core.connector.elexis.jpa.ElexisTypeMap;
import info.elexis.server.core.connector.elexis.jpa.StoreToStringService;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.AbstractDBObjectIdDeleted;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Artikel;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.ArtikelstammItem;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Behandlung;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Eigenleistung;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Fall;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Kontakt;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Labor2009Tarif;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.TarmedLeistung;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.VKPreis;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.VKPreis_;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Verrechnet;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Verrechnet_;
import info.elexis.server.core.connector.elexis.services.JPAQuery.QUERY;

public class VerrechnetService extends PersistenceService {

	private static Logger log = LoggerFactory.getLogger(VerrechnetService.class);

	public static class Builder extends AbstractBuilder<Verrechnet> {
		@SuppressWarnings("rawtypes")
		private final IBillable iv;

		public Builder(@SuppressWarnings("rawtypes") IBillable iv, Behandlung kons, int count, Kontakt userContact) {
			object = new Verrechnet();

			this.iv = iv;

			object.setLeistungenText(iv.getText());
			String keyForObject = ElexisTypeMap.getKeyForObject((AbstractDBObjectIdDeleted) iv.getEntity());
			object.setKlasse(keyForObject);
			object.setLeistungenCode(iv.getId());
			object.setLeistungenText(iv.getText());
			object.setBehandlung(kons);
			object.setZahl(count);
			object.setUser(userContact);

			TimeTool dat = new TimeTool(kons.getDatum());
			Fall fall = kons.getFall();
			int tp = iv.getTP(dat, fall);
			double factor = iv.getFactor(dat, fall);
			long preis = Math.round(tp * factor);

			object.setEk_kosten(Integer.parseInt(iv.getCost(dat).getCentsAsString()));
			object.setVk_tp(tp);
			object.setVk_scale(Double.toString(factor));
			object.setVk_preis((int) preis);
			object.setScale(100);
			object.setScale2(100);
		}

		@Override
		public Verrechnet build() {
			if (iv instanceof VerrechenbarArtikelstammItem || iv instanceof VerrechenbarArtikel) {
				new StockService().performSingleDisposal((IArticle) iv.getEntity(), object.getZahl(), null);
			}
			// call the adjusters
			new VatVerrechnetAdjuster().adjust(object);
			return super.build();
		}
	}

	/**
	 * convenience method
	 * 
	 * @param id
	 * @return
	 */
	public static Optional<Verrechnet> load(String id) {
		return PersistenceService.load(Verrechnet.class, id).map(v -> (Verrechnet) v);
	}

	/**
	 * convenience method
	 * 
	 * @param includeElementsMarkedDeleted
	 * @return
	 */
	public static List<Verrechnet> findAll(boolean includeElementsMarkedDeleted) {
		return PersistenceService.findAll(Verrechnet.class, includeElementsMarkedDeleted).stream()
				.map(v -> (Verrechnet) v).collect(Collectors.toList());
	}

	private static IBillable<? extends AbstractDBObjectIdDeleted> createVerrechenbarForObject(
			AbstractDBObjectIdDeleted object) {
		if (object instanceof TarmedLeistung) {
			return new VerrechenbarTarmedLeistung((TarmedLeistung) object);
		} else if (object instanceof Labor2009Tarif) {
			return new VerrechenbarLabor2009Tarif((Labor2009Tarif) object);
		} else if (object instanceof Eigenleistung) {
			return new VerrechenbarEigenleistung((Eigenleistung) object);
		} else if (object instanceof ArtikelstammItem) {
			return new VerrechenbarArtikelstammItem((ArtikelstammItem) object);
		} else if (object instanceof Artikel) {
			return new VerrechenbarArtikel((Artikel) object);
		}

		log.warn("Unsupported object for create verrechenbar {}", object.getClass().getName());
		return null;
	}

	/**
	 * The article or service this object was billed out of
	 * 
	 * @param vr
	 * @return
	 */
	public static Optional<AbstractDBObjectIdDeleted> getOriginService(Verrechnet vr) {
		String clazz = vr.getKlasse();
		String leistungenCode = vr.getLeistungenCode();

		return StoreToStringService.INSTANCE
				.createDetachedFromString(clazz + StringConstants.DOUBLECOLON + leistungenCode);
	}

	@SuppressWarnings("rawtypes")
	public static Optional<IBillable> getVerrechenbar(Verrechnet vr) {
		Optional<AbstractDBObjectIdDeleted> object = getOriginService(vr);
		if (object.isPresent()) {
			return Optional.ofNullable(createVerrechenbarForObject(object.get()));
		}
		return Optional.empty();
	}

	public static double getVKMultiplikator(TimeTool date, String billingSystem) {
		JPAQuery<VKPreis> vkPreise = new JPAQuery<VKPreis>(VKPreis.class);
		vkPreise.add(VKPreis_.typ, JPAQuery.QUERY.EQUALS, billingSystem);
		List<VKPreis> list = vkPreise.execute();

		Iterator<VKPreis> iter = list.iterator();
		while (iter.hasNext()) {
			VKPreis info = iter.next();
			TimeTool fromDate = new TimeTool(info.getDatum_von());
			TimeTool toDate = new TimeTool(info.getDatum_bis());
			if (date.isAfterOrEqual(fromDate) && date.isBeforeOrEqual(toDate)) {
				String value = info.getMultiplikator();
				if (value != null && !value.isEmpty()) {
					try {
						return Double.parseDouble(value);
					} catch (NumberFormatException nfe) {
						log.error("Exception handling multiplikator value " + value);
						return 0.0;
					}
				}
			}
		}
		return 1.0;
	}

	/**
	 * Perform a validated (returns Status error on fail) change on the count of
	 * the {@link Verrechnet}
	 * 
	 * @param verrechnet
	 * @param newCount
	 * @param mandatorContact
	 * @return {@link ObjectStatus} containing the refreshed {@link Verrechnet}
	 *         if {@link Status#OK_STATUS}, else an {@link IStatus}
	 */
	public static IStatus changeCountValidated(Verrechnet verrechnet, int newCount, Kontakt mandatorContact) {
		return changeCountValidated(verrechnet, (float) newCount, mandatorContact);
	}

	/**
	 * Perform a validated (returns Status error on fail) change on the count of
	 * the {@link Verrechnet}
	 * 
	 * @param verrechnet
	 * @param newCount
	 * @param mandatorContact
	 * @return {@link ObjectStatus} containing the refreshed {@link Verrechnet}
	 *         if {@link Status#OK_STATUS}, else an {@link IStatus}
	 */
	public static IStatus changeCountValidated(Verrechnet verrechnet, float newCount, Kontakt mandatorContact) {
		int previous = verrechnet.getZahl();
		if (newCount == previous && verrechnet.getScale() == 100 && verrechnet.getScale2() == 100) {
			return Status.OK_STATUS;
		}

		@SuppressWarnings("rawtypes")
		Optional<IBillable> verrechenbar = VerrechnetService.getVerrechenbar(verrechnet);
		if (newCount == 0) {
			log.trace("Removing Verrechnet [{}] as count == 0", verrechnet.getId());
			IStatus ret = verrechenbar.get().removeFromConsultation(verrechnet, mandatorContact);
			if (!ret.isOK()) {
				return ret;
			}
		}

		if (newCount % 1 == 0) {
			// integer -> full package
			int difference = (int) newCount - previous;
			if (difference > 0) {
				// addition
				for (int i = 0; i < difference; i++) {
					IStatus ret = verrechenbar.get().add(verrechnet.getBehandlung(), verrechnet.getUser(),
							mandatorContact);
					if (!ret.isOK()) {
						return ret;
					}
				}
				return ObjectStatus.OK_STATUS(VerrechnetService.reload(verrechnet).get());
			} else {
				// subtraction, we assume that this will never fail
				verrechnet.setZahl((int) newCount);
				verrechnet.setScale(100);
				verrechnet.setScale2(100);
				return ObjectStatus.OK_STATUS(VerrechnetService.save(verrechnet));
			}
		} else {
			// float -> fractional package
			verrechnet.setZahl(1);

			verrechnet.setScale(100);
			int scale2 = Math.round(newCount * 100);
			verrechnet.setScale2(scale2);
			return ObjectStatus.OK_STATUS(VerrechnetService.save(verrechnet));
		}
	}

	public static List<Verrechnet> getAllVerrechnetForBehandlung(Behandlung behandlung) {
		JPAQuery<Verrechnet> qre = new JPAQuery<Verrechnet>(Verrechnet.class);
		qre.add(Verrechnet_.behandlung, QUERY.EQUALS, behandlung);
		return qre.execute();
	}
}
