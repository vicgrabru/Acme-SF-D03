
package acme.features.sponsor.sponsorDashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.SystemConfiguration;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int sponsorId;
		SponsorDashboard dashboard;

		Integer totalNumberOfInvoicesWithLessOrEqualThan21;

		Integer totalNumberOfSponsorshipsWithLink;

		Money avgAmountOfSponsorships;
		Money devAmountOfSponsorships;
		Money minAmountOfSponsorships;
		Money maxAmountOfSponsorships;

		Money avgQuantityOfInvoices;
		Money devQuantityOfInvoices;
		Money minQuantityOfInvoices;
		Money maxQuantityOfInvoices;

		List<Money> amountOfSponsorships;
		List<Money> quantityOfInvoices;
		SystemConfiguration systemConfiguration;
		Double sumOfSquaresOfDifferencesSponsorships = 0.;
		Double averageSponsorships = 0.;
		Double sumOfSquaresOfDifferencesInvoices = 0.;
		Double averageInvoices = 0.;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();

		totalNumberOfInvoicesWithLessOrEqualThan21 = this.repository.findAllInvoicesWithTaxLessOrEqualThan21BySponsorId(sponsorId).size();

		totalNumberOfSponsorshipsWithLink = this.repository.findAllSponsorshipsWithLinkBySponsorId(sponsorId).size();

		amountOfSponsorships = this.repository.findAllSponsorshipsAmountsBySponsorId(sponsorId);
		systemConfiguration = this.repository.getSystemConfiguration();

		avgAmountOfSponsorships = new Money();
		averageSponsorships = amountOfSponsorships.stream().mapToDouble(a -> a.getAmount()).average().orElse(0.0);
		avgAmountOfSponsorships.setAmount(averageSponsorships);
		avgAmountOfSponsorships.setCurrency(systemConfiguration.getSystemCurrency());

		sumOfSquaresOfDifferencesSponsorships = amountOfSponsorships.stream().mapToDouble(a -> a.getAmount()).map(a -> a - amountOfSponsorships.stream().mapToDouble(a2 -> a2.getAmount()).average().orElse(0.0)).map(a -> a * a).sum();

		devAmountOfSponsorships = new Money();
		devAmountOfSponsorships.setAmount(Math.sqrt(sumOfSquaresOfDifferencesSponsorships / (amountOfSponsorships.size() - 1)));
		devAmountOfSponsorships.setCurrency(systemConfiguration.getSystemCurrency());

		minAmountOfSponsorships = new Money();
		minAmountOfSponsorships.setAmount(amountOfSponsorships.stream().mapToDouble(a -> a.getAmount()).min().orElse(0.0));
		minAmountOfSponsorships.setCurrency(systemConfiguration.getSystemCurrency());

		maxAmountOfSponsorships = new Money();
		maxAmountOfSponsorships.setAmount(amountOfSponsorships.stream().mapToDouble(a -> a.getAmount()).max().orElse(0.0));
		maxAmountOfSponsorships.setCurrency(systemConfiguration.getSystemCurrency());

		quantityOfInvoices = this.repository.findAllInvoicesQuantitiesBySponsorId(sponsorId);

		avgQuantityOfInvoices = new Money();
		averageInvoices = quantityOfInvoices.stream().mapToDouble(q -> q.getAmount()).average().orElse(0.0);
		avgQuantityOfInvoices.setAmount(averageInvoices);
		avgQuantityOfInvoices.setCurrency(systemConfiguration.getSystemCurrency());

		sumOfSquaresOfDifferencesInvoices = quantityOfInvoices.stream().mapToDouble(q -> q.getAmount()).map(q -> q - quantityOfInvoices.stream().mapToDouble(q2 -> q2.getAmount()).average().orElse(0.0)).map(q -> q * q).sum();

		devQuantityOfInvoices = new Money();
		devQuantityOfInvoices.setAmount(Math.sqrt(sumOfSquaresOfDifferencesInvoices / (quantityOfInvoices.size() - 1)));
		devQuantityOfInvoices.setCurrency(systemConfiguration.getSystemCurrency());

		minQuantityOfInvoices = new Money();
		minQuantityOfInvoices.setAmount(quantityOfInvoices.stream().mapToDouble(q -> q.getAmount()).min().orElse(0.0));
		minQuantityOfInvoices.setCurrency(systemConfiguration.getSystemCurrency());

		maxQuantityOfInvoices = new Money();
		maxQuantityOfInvoices.setAmount(quantityOfInvoices.stream().mapToDouble(q -> q.getAmount()).max().orElse(0.0));
		maxQuantityOfInvoices.setCurrency(systemConfiguration.getSystemCurrency());

		dashboard = new SponsorDashboard();

		dashboard.setTotalNumberOfInvoicesWithLessOrEqualThan21(totalNumberOfInvoicesWithLessOrEqualThan21);

		dashboard.setTotalNumberOfSponsorshipsWithLink(totalNumberOfSponsorshipsWithLink);

		dashboard.setAvgAmountOfSponsorships(avgAmountOfSponsorships);
		dashboard.setDevAmountOfSponsorships(devAmountOfSponsorships);
		dashboard.setMinAmountOfSponsorships(minAmountOfSponsorships);
		dashboard.setMaxAmountOfSponsorships(maxAmountOfSponsorships);

		dashboard.setAvgQuantityOfInvoices(avgQuantityOfInvoices);
		dashboard.setDevQuantityOfInvoices(devQuantityOfInvoices);
		dashboard.setMinQuantityOfInvoices(minQuantityOfInvoices);
		dashboard.setMaxQuantityOfInvoices(maxQuantityOfInvoices);

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final SponsorDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "totalNumberOfInvoicesWithLessOrEqualThan21", "totalNumberOfSponsorshipsWithLink", "avgAmountOfSponsorships", "devAmountOfSponsorships", "minAmountOfSponsorships", "maxAmountOfSponsorships", "avgQuantityOfInvoices",
			"devQuantityOfInvoices", "minQuantityOfInvoices", "maxQuantityOfInvoices");

		super.getResponse().addData(dataset);

	}

}
