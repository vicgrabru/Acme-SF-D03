
package acme.forms;

import java.util.Date;
import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	private Map<Date, Integer>		numberOfTrainingModulesPerUpdateMoment;

	private Map<String, Integer>	numberOfTrainingSessionPerLink;

	private Double					averageTrainingModuleTime;
	private Double					deviationrainingModuleTime;
	private Double					minimumTrainingModuleTime;
	private Double					maximumTrainingModuleTime;
}
