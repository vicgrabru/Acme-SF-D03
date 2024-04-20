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

package acme.features.administrator.objective;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.objective.Objective;

@Service
public class AdministratorObjectiveCreateService extends AbstractService<Administrator, Objective> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorObjectiveRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Objective object;
		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();

		object = new Objective();
		object.setInstantiationMoment(instantiationMoment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Objective object) {
		assert object != null;

		super.bind(object, "instantiationMoment", "title", "description", "priority", "isCritical", "startDateDuration", "endDateDuration", "link");
	}

	@Override
	public void validate(final Objective object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("startDateDuration")) {
			super.state(object.getStartDateDuration().after(object.getInstantiationMoment()), "startDateDuration", "administrator.objective.form.error.startDateDuration.not-after-instantiation");
			if (!super.getBuffer().getErrors().hasErrors("endDateDuration"))
				super.state(object.getEndDateDuration().after(object.getStartDateDuration()), "endDateDuration", "administrator.objective.form.error.endDateDuration.not-after-startDateDuration");
		}
	}

	@Override
	public void perform(final Objective object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Objective object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "title", "description", "priority", "isCritical", "startDateDuration", "endDateDuration", "link");

		super.getResponse().addData(dataset);
	}

}
