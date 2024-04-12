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

package acme.features.manager.userStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Priority;
import acme.entities.project.Project;
import acme.entities.project.UserStory;
import acme.entities.project.UserStoryAssign;
import acme.roles.Manager;

@Service
public class ManagerUserStoryCreateService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(projectId);
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(project.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(projectId);

		object = new UserStory();
		object.setProject(project);
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "optionalLink", "draftMode");
	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;

		UserStoryAssign relationship;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(projectId);

		relationship = new UserStoryAssign();

		relationship.setProject(project);
		relationship.setUserStory(object);

		this.repository.save(object);
		this.repository.save(relationship);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Priority.class, object.getPriority());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "optionalLink", "draftMode");
		dataset.put("masterId", object.getProject().getId());
		dataset.put("priorities", choices);
		dataset.put("projectCode", object.getProject().getCode());
		dataset.put("projectTitle", object.getProject().getTitle());

		super.getResponse().addData(dataset);
	}

}
