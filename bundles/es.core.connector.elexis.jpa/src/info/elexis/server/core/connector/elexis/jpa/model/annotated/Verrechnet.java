package info.elexis.server.core.connector.elexis.jpa.model.annotated;

import java.util.Hashtable;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Convert;

@Entity
@Table(name = "LEISTUNGEN")
public class Verrechnet extends AbstractDBObjectIdDeleted {

	@Column(length = 80)
	private String klasse;

	@Column(length = 25, name = "leistg_code")
	private String leistungenCode;

	@Column(length = 255, name = "leistg_txt")
	private String leistungenText;

	@OneToOne
	@JoinColumn(name = "BEHANDLUNG")
	private Behandlung behandlung;

	@Convert(value = "IntegerStringConverter")
	private int zahl;

	@Convert(value = "IntegerStringConverter")
	private int ek_kosten;

	@Convert(value = "IntegerStringConverter")
	private int vk_tp;

	@Column(length = 8)
	private String vk_scale;

	@Convert(value = "IntegerStringConverter")
	private int vk_preis;

	@Convert(value = "IntegerStringConverter")
	private int scale;

	@Convert(value = "IntegerStringConverter")
	private int scale2;

	@OneToOne
	@JoinColumn(name = "userID")
	private Kontakt user;

	@Basic(fetch = FetchType.LAZY)
	@Convert(value = "ElexisExtInfoMapConverter")
	private Map<Object, Object> detail;

	@Transient
	public void setTP(double tp) {
		setVk_tp((int) Math.round(tp));
	}

	@Transient
	public void setPrimaryScaleFactor(double scale) {
		int sca = (int) Math.round(scale * 100);
		setScale(sca);
	}

	@Transient
	public void setSecondaryScaleFactor(double scale) {
		int sca = (int) Math.round(scale * 100);
		setScale2(sca);
	}

	@Transient
	public String getText() {
		return getLeistungenText();
	}

	@Transient
	public float getScaledCount() {
		return getZahl() * (getScale2() / 100f);
	}

	public Map<Object, Object> getDetail() {
		if (detail == null) {
			detail = new Hashtable<Object, Object>();
		}
		return detail;
	}

	public void setDetail(final String key, final String value) {
		if (value == null) {
			getDetail().remove(key);
		} else {
			getDetail().put(key, value);
		}
	}

	public String getLeistungenText() {
		return leistungenText;
	}

	public void setLeistungenText(String leistungenText) {
		this.leistungenText = leistungenText;
	}

	public Behandlung getBehandlung() {
		return behandlung;
	}

	public void setBehandlung(Behandlung behandlung) {
		this.behandlung = behandlung;
	}

	public int getZahl() {
		return zahl;
	}

	public void setZahl(int zahl) {
		this.zahl = zahl;
	}

	public int getEk_kosten() {
		return ek_kosten;
	}

	public void setEk_kosten(int ek_kosten) {
		this.ek_kosten = ek_kosten;
	}

	public int getVk_tp() {
		return vk_tp;
	}

	public void setVk_tp(int vk_tp) {
		this.vk_tp = vk_tp;
	}

	public String getVk_scale() {
		return vk_scale;
	}

	public void setVk_scale(String vk_scale) {
		this.vk_scale = vk_scale;
	}

	public int getVk_preis() {
		return vk_preis;
	}

	public void setVk_preis(int vk_preis) {
		this.vk_preis = vk_preis;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getScale2() {
		return scale2;
	}

	public void setScale2(int scale2) {
		this.scale2 = scale2;
	}

	public String getKlasse() {
		return klasse;
	}

	public void setKlasse(String klasse) {
		this.klasse = klasse;
	}

	public String getLeistungenCode() {
		return leistungenCode;
	}

	public void setLeistungenCode(String leistungenCode) {
		this.leistungenCode = leistungenCode;
	}

	public Kontakt getUser() {
		return user;
	}

	public void setUser(Kontakt user) {
		this.user = user;
	}
}
