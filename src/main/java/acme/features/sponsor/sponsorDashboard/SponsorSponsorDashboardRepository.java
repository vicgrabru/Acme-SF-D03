
package acme.features.sponsor.sponsorDashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.SystemConfiguration;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;

@Repository
public interface SponsorSponsorDashboardRepository extends AbstractRepository {

	@Query("select i from Invoice i where i.tax <= 21 and i.draftMode = false and i.sponsorship.sponsor.id = :sponsorId")
	List<Invoice> findAllInvoicesWithTaxLessOrEqualThan21BySponsorId(int sponsorId);

	@Query("select s from Sponsorship s where s.link is not null and s.draftMode = false and s.sponsor.id = :sponsorId")
	List<Sponsorship> findAllSponsorshipsWithLinkBySponsorId(int sponsorId);

	@Query("select s.amount from Sponsorship s where s.draftMode = false and s.sponsor.id = :sponsorId")
	List<Money> findAllSponsorshipsAmountsBySponsorId(int sponsorId);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfiguration();

	@Query("select i.quantity from Invoice i where i.draftMode = false and i.sponsorship.sponsor.id = :sponsorId")
	List<Money> findAllInvoicesQuantitiesBySponsorId(int sponsorId);

}
