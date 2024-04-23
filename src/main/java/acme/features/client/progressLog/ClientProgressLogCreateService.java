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

package acme.features.client.progressLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogCreateService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("contractId", int.class);
		contract = this.repository.findContractById(contractId);
		status = contract != null && //
			contract.isDraftMode() && //
			super.getRequest().getPrincipal().hasRole(contract.getClient());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("contractId", int.class);
		contract = this.repository.findContractById(contractId);

		object = new ProgressLog();
		object.setRegistrationMoment(MomentHelper.getCurrentMoment());
		object.setContract(contract);
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLog object) {
		assert object != null;

		super.bind(object, "recordId", "completeness", "comment", "responsiblePerson");
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			boolean duplicatedCode = this.repository.findAllProgressLogs().stream().anyMatch(pl -> pl.getRecordId().equals(object.getRecordId()));
			super.state(!duplicatedCode, "recordId", "client.progress-log.form.error.duplicated-record-id");
		}
	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;
		int contractId;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "draftMode");
		contractId = super.getRequest().getData("contractId", int.class);

		dataset.put("contractId", contractId);
		dataset.put("readOnlyCode", false);

		super.getResponse().addData(dataset);
	}

}
