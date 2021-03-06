package org.colomoto.biolqm.modifier.reduction;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.modifier.ModelModifier;

public class ReductionModifier implements ModelModifier {

    private final LogicalModel model;
    private final ReductionSettings settings;

    public ReductionModifier(LogicalModel model, ReductionSettings settings) {
        this.model = model;
        this.settings = settings;
    }

    @Override
    public LogicalModel getModifiedModel() {
    	if (!settings.hasReduction()) {
    		return model;
    	}
    	
        ModelReducer reducer = new ModelReducer(model);
        if (settings.handleOutputs) {
            reducer.removePseudoOutputs();
        }
        LogicalModel result = reducer.getModel();

        if (settings.pattern != null) {

        }

        if (settings.pattern != null) {
            result = new PatternReduction(result, settings.pattern).getModifiedModel();
        }
        
        if (settings.handleFixed) {
            result = FixedComponentRemover.reduceFixed(result, settings.purgeFixed);
        }

        if (settings.handleDuplicates) {
            result = DuplicateRemover.removeDuplicateComponents(result);
        }

        return result;
    }
}
