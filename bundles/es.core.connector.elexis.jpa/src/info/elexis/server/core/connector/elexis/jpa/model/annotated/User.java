package info.elexis.server.core.connector.elexis.jpa.model.annotated;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.Convert;

@Entity
@Table(name = "USER_")
@XmlRootElement(name = "user")
public class User extends AbstractDBObjectIdDeletedExtInfo {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KONTAKT_ID")
	protected Kontakt kontakt;

	@Column(length = 64, name = "HASHED_PASSWORD")
	protected String hashedPassword;

	@Column(length = 64)
	protected String salt;
	
	@Convert("booleanStringConverter")
	@Column(name = "is_active")
	protected boolean active;
	
	@Convert("booleanStringConverter")
	@Column(name = "is_administrator")
	protected boolean administrator;
	
	@Basic(fetch = FetchType.LAZY)
	@Lob()
	protected String keystore;
	
	@Column(length = 64)
	protected String apiKey;
	
	@ManyToMany
    @JoinTable(name="USER_ROLE_JOINT",
                joinColumns=
                     @JoinColumn(name="USER_ID"),
                inverseJoinColumns=
                     @JoinColumn(name="ID")
    )
	protected Collection<Role> roles = new HashSet<>();

	public Kontakt getKontakt() {
		return kontakt;
	}

	public void setKontakt(Kontakt kontakt) {
		this.kontakt = kontakt;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public String getKeystore() {
		return keystore;
	}

	public void setKeystore(String keystore) {
		this.keystore = keystore;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public Collection<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
}
