package org.colomoto.logicalmodel.tool.reduction;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.LogicalModelImpl;
import org.colomoto.logicalmodel.LogicalModelModifier;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.RegulatorRemovalOperation;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.MDDVariable;

import java.util.List;

/**
 * Detect fixed components and propagate their fixed value to their targets
 *
 * @author Aurelien Naldi
 */
public class FixedComponentRemover implements LogicalModelModifier {

    @Override
    public LogicalModel apply(LogicalModel model) {

        MDDManager ddmanager = model.getMDDManager();
        int[] oldFunctions = model.getLogicalFunctions();
        int[] functions = new int[oldFunctions.length];
        for (int i=0 ; i<functions.length ; i++) {
            int f = oldFunctions[i];
            ddmanager.use(f);
            functions[i] = f;
        }

        boolean[] knownFixed = new boolean[ functions.length ];

        // detect and propagate fixed components until no new ones are found
        boolean changed = false;
        boolean hasNewFixed = true;
        while(hasNewFixed) {
            hasNewFixed = false;

            // detect new fixed components
            for (int i=0 ; i<functions.length ; i++) {
                int f = functions[i];
                if (!knownFixed[i] && ddmanager.isleaf(f)) {
                    knownFixed[i] = true;
                    hasNewFixed = true;

                    MDDVariable var = ddmanager.getNodeVariable(i);
                    RegulatorRemovalOperation op = new RegulatorRemovalOperation(ddmanager, var, f);

                    for (int j=0 ; j<functions.length ; j++) {
                        int curFunc = functions[j];
                        int newFunc = op.restrict( curFunc );
                        if (newFunc != curFunc) {
                            changed = true;
                            ddmanager.free(curFunc);
                            functions[j] = newFunc;
                        } else {
                            ddmanager.free(newFunc);
                        }
                    }
                }
            }

        }

        // construct an updated model if needed
        if (changed) {
            List<NodeInfo> core = model.getNodeOrder();
            List<NodeInfo> extra = model.getExtraComponents();
            int[] extraFunctions = model.getExtraLogicalFunctions();
            LogicalModel newModel = new LogicalModelImpl(ddmanager, core, functions, extra, extraFunctions);

            return newModel;
        }

        return model;
    }

}
