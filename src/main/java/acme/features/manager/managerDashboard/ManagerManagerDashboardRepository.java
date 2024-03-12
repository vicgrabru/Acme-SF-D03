/*
 * AdministratorDashboardRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.managerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.SystemConfiguration;

@Repository
public interface ManagerManagerDashboardRepository extends AbstractRepository {

	@Query("select count(us) from UserStory us where us.priority = acme.entities.project.Priority.MUST")
	Long totalNumberOfUserStoriesWithMustPriority();

	@Query("select count(us) from UserStory us where us.priority = acme.entities.project.Priority.SHOULD")
	Long totalNumberOfUserStoriesWithShouldPriority();

	@Query("select count(us) from UserStory us where us.priority = acme.entities.project.Priority.COULD")
	Long totalNumberOfUserStoriesWithCouldPriority();

	@Query("select count(us) from UserStory us where us.priority = acme.entities.project.Priority.WONT")
	Long totalNumberOfUserStoriesWithWontPriority();

	@Query("select avg(us.estimatedCost) from UserStory us")
	Double avgEstimatedCostOfUserStories();

	@Query("select min(us.estimatedCost) from UserStory us")
	Integer minEstimatedCostOfUserStories();

	@Query("select max(us.estimatedCost) from UserStory us")
	Integer maxEstimatedCostOfUserStories();

	@Query("select stddev(us.estimatedCost) from UserStory us")
	Double stdEstimatedCostOfUserStories();

	@Query("select p.cost from Project p")
	Collection<Money> getCostOfAllProjects();

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfiguration();

}
