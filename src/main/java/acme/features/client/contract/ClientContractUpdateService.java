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

package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.project.Project;
import acme.roles.Client;
import acme.roles.Provider;

@Service
public class ClientContractUpdateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractById(contractId);
		status = contract != null && super.getRequest().getPrincipal().hasRole(contract.getClient());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		super.bind(object, "goals", "budget", "provider", "customerName");
		if (object.getProvider() != null)
			object.setProviderName(object.getProvider().getIdentity().getName());

	}

	@Override
	public void validate(final Contract object) {
		assert object != null;
		String currencies;

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			currencies = this.repository.findAcceptedCurrencies();
			super.state(currencies.contains(object.getBudget().getCurrency()), "budget", "client.contract.form.error.bugdet.invalid-currency");
			super.state(object.getBudget().getAmount() <= object.getProject().getCost().getAmount(), "budget", "client.contract.form.error.budget.budget-over-project-cost");
		}

	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesProject;
		SelectChoices choicesProvider;

		Collection<Provider> providers;
		Collection<Project> projects;

		providers = this.repository.findAllProviders();
		projects = this.repository.findPublishedProjects();

		choicesProvider = SelectChoices.from(providers, "userAccount.identity.name", object.getProvider());
		choicesProject = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, "code", "goals", "budget", "customerName", "instantiationMoment", "draftMode");
		dataset.put("provider", choicesProvider.getSelected().getKey());
		dataset.put("providers", choicesProvider);
		dataset.put("project", choicesProject.getSelected().getKey());
		dataset.put("projects", choicesProject);

		dataset.put("projectId", object.getProject().getId());
		dataset.put("contractId", object.getId());
		dataset.put("readOnlyCode", true);

		super.getResponse().addData(dataset);
	}

}
