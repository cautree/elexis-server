package info.elexis.server.core.connector.elexis.jpa.model.annotated;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import ch.elexis.core.model.article.Constants;
import ch.elexis.core.model.article.IArticle;
import ch.rgw.tools.StringTool;
import info.elexis.server.core.connector.elexis.jpa.POHelper;

@Entity
@Table(name = "artikel")
public class Artikel extends AbstractDBObjectIdDeletedExtInfo implements IArticle {

	public static final String TYP_EIGENARTIKEL = "Eigenartikel";
	public static final String TYP_MIGEL = "MiGeL";
	public static final String TYP_MEDICAL = "Medical";
	public static final String TYP_MEDIKAMENT = "Medikament";

	/**
	 * @deprecated switch to EigenartikelConstants after beta build
	 */
	public static final String FLD_EXTINFO_SELLUNIT = "Verkaufseinheit";

	@Column(length = 15)
	private String ean;

	@Column(length = 20, name = "SubID")
	private String subId;

	@Column(length = 80)
	private String klasse;

	@Column(length = 127)
	private String name;

	@Column(length = 127, name = "Name_intern")
	private String nameIntern;

	@Column(length = 8, name = "EK_Preis")
	private String ekPreis;

	/**
	 * user-defined prices are stored as negative value,
	 * hence Math.abs should be applied for billing
	 */
	@Column(length = 8, name = "VK_Preis")
	private String vkPreis;

	@Column(length = 15)
	private String Typ;

	@Column(length = 10)
	private String codeclass;

	@Column(length = 25)
	private String extId;

	@Column(length = 8)
	private String lastImport;

	@Column(length = 8)
	private LocalDate validFrom;

	@Column(length = 8)
	private LocalDate validTo;

	@Column(length = 255, name = "ATC_code")
	private String atcCode;

	@Override
	public String toString() {
		return super.toString() + "name=["+getName()+"]";
	}
	
	public String getLabel() {
		String ret = getNameIntern();
		if (StringTool.isNothing(ret)) {
			ret = getName();
		}
		return ret;
	}
	
	@Transient
	public String getGTIN() {
		return getEan();
	};

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getKlasse() {
		return klasse;
	}

	public void setKlasse(String klasse) {
		this.klasse = klasse;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameIntern() {
		return nameIntern;
	}

	public void setNameIntern(String nameIntern) {
		this.nameIntern = nameIntern;
	}

	public String getEkPreis() {
		return ekPreis;
	}

	public void setEkPreis(String ekPreis) {
		this.ekPreis = ekPreis;
	}

	public String getVkPreis() {
		return vkPreis;
	}

	public void setVkPreis(String vkPreis) {
		this.vkPreis = vkPreis;
	}

	public String getTyp() {
		return Typ;
	}

	public void setTyp(String typ) {
		Typ = typ;
	}

	public String getCodeclass() {
		return codeclass;
	}

	public void setCodeclass(String codeclass) {
		this.codeclass = codeclass;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public String getLastImport() {
		return lastImport;
	}

	public void setLastImport(String lastImport) {
		this.lastImport = lastImport;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDate getValidTo() {
		return validTo;
	}

	public void setValidTo(LocalDate validTo) {
		this.validTo = validTo;
	}

	public String getAtcCode() {
		return atcCode;
	}

	public void setAtcCode(String atcCode) {
		this.atcCode = atcCode;
	}

	@Transient
	@Override
	public int getPackageUnit() {
		String extInfoAsString = getExtInfoAsString(Constants.FLD_EXT_PACKAGE_UNIT_INT);
		return POHelper.checkZero(extInfoAsString);
	}

	@Transient
	@Override
	public int getSellingUnit() {
		String extInfoAsString = getExtInfoAsString(Constants.FLD_EXT_SELL_UNIT);
		return POHelper.checkZero(extInfoAsString);
	}

	@Transient
	@Override
	public boolean isProduct() {
		return false;
	}
}
