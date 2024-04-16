/*
 * EmployerApplicationUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.banner;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

@Service
public class AdministratorBannerCreateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBannerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Banner object;
		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();

		object = new Banner();
		object.setInstantiationMoment(instantiationMoment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;

		super.bind(object, "periodStart", "periodEnd", "pictureLink", "slogan", "webDocumentLink");
	}

	@Override
	public void validate(final Banner object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("periodStart")) {
			super.state(object.getPeriodStart().after(object.getInstantiationMoment()), "periodStart", "administrator.banner.form.error.period-start.not-after-instantiation");
			if (!super.getBuffer().getErrors().hasErrors("periodEnd")) {
				Date targetDate;
				targetDate = MomentHelper.deltaFromMoment(object.getPeriodStart(), 1, ChronoUnit.WEEKS);
				super.state(object.getPeriodEnd().after(targetDate), "periodEnd", "administrator.banner.form.error.period-too-short");
			}
		}
	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "periodStart", "periodEnd", "pictureLink", "slogan", "webDocumentLink");

		super.getResponse().addData(dataset);
	}

}
