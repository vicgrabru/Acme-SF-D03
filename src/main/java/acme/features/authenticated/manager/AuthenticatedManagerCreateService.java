/*
 * AuthenticatedConsumerCreateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.roles.Manager;
import spamDetector.SpamDetector;

@Service
public class AuthenticatedManagerCreateService extends AbstractService<Authenticated, Manager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedManagerRepository repository;

	// AbstractService<Authenticated, Manager> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Manager object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Manager();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Manager object) {
		assert object != null;

		super.bind(object, "degree", "overview", "certifications", "optionalLink");
	}

	@Override
	public void validate(final Manager object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("degree"))
			super.state(!SpamDetector.checkTextValue(object.getDegree()), "degree", "authenticated.manager.form.error.spam-in-degree");

		if (!super.getBuffer().getErrors().hasErrors("overview"))
			super.state(!SpamDetector.checkTextValue(object.getOverview()), "overview", "authenticated.manager.form.error.spam-in-overview");

		if (!super.getBuffer().getErrors().hasErrors("certifications"))
			super.state(!SpamDetector.checkTextValue(object.getCertifications()), "certifications", "authenticated.manager.form.error.spam-in-certifications");
	}

	@Override
	public void perform(final Manager object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Manager object) {
		Dataset dataset;

		dataset = super.unbind(object, "degree", "overview", "certifications", "optionalLink");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
