package info.elexis.server.core.connector.elexis.jpa.model.annotated;

import info.elexis.server.core.connector.elexis.jpa.model.annotated.TarmedExtension;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20160725-rNA")
@StaticMetamodel(TarmedLeistung.class)
public class TarmedLeistung_ { 

    public static volatile SingularAttribute<TarmedLeistung, String> parent;
    public static volatile SingularAttribute<TarmedLeistung, TarmedExtension> extension;
    public static volatile SingularAttribute<TarmedLeistung, String> law;
    public static volatile SingularAttribute<TarmedLeistung, String> code_;
    public static volatile SingularAttribute<TarmedLeistung, String> digniQuanti;
    public static volatile SingularAttribute<TarmedLeistung, String> tx255;
    public static volatile SingularAttribute<TarmedLeistung, String> nickname;
    public static volatile SingularAttribute<TarmedLeistung, String> sparte;
    public static volatile SingularAttribute<TarmedLeistung, LocalDate> gueltigBis;
    public static volatile SingularAttribute<TarmedLeistung, LocalDate> gueltigVon;
    public static volatile SingularAttribute<TarmedLeistung, String> digniQuali;
    public static volatile SingularAttribute<TarmedLeistung, Boolean> isChapter;

}