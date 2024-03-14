package evolutionSimulation.orchestrationOfEvolutionIterations.SPLCandidateSelectionStrategies;

import java.util.List;


public interface SPLNextEvolutionIterationCandidateSelectionStrategy {

	public List<String> selectNextEvolutionIterationCandidates(String evolutionDirectory);
}
