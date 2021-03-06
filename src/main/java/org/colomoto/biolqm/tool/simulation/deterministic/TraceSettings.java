package org.colomoto.biolqm.tool.simulation.deterministic;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.tool.ToolSettings;
import org.colomoto.biolqm.tool.simulation.InitialStateFactory;
import org.colomoto.biolqm.tool.simulation.ordering.DeterministicGrouping;

public class TraceSettings extends ToolSettings {

    DeterministicGrouping grouping = null;
    boolean isSequential = false;
    byte[] state = null;
    int max_steps = 1000;

    public TraceSettings(LogicalModel model) {
        super(model);
    }

    public void parseParameters(String ... parameters) {
        if (parameters == null || parameters.length < 1) {
            return;
        }

        StringBuffer tmp = new StringBuffer();

        for (int idx=0 ; idx<parameters.length ; idx++) {

            String s = parameters[idx].trim();
            if (s == null || s.length() == 0) {
                continue;
            }

            String next = null;
            if (s.length() == 2 && s.charAt(0) == '-') {
                while (++idx < parameters.length) {
                    String snext = parameters[idx];
                    if (snext.startsWith("-")) {
                        idx--;
                        break;
                    }

                    if (next == null) {
                        next = snext;
                    } else {
                        next += " " + snext;
                    }
                }

                switch (s.charAt(1)) {
                    case 'u':
                        parseUpdater(next);
                        continue;
                    case 'm':
                        parseMax(next);
                        continue;
                    case 'i':
                        this.state = InitialStateFactory.parseInitialState(this.model, next);
                        continue;
                }
            }

            throw new RuntimeException("Unrecognized parameter: "+s);
        }
    }


    public void parseMax(String s) {
        max_steps = Integer.parseInt(s);
    }

    public void parseUpdater(String s) {
        if (s.equalsIgnoreCase("sequential")) {
            isSequential = true;
            grouping = null;
            return;
        }

        if (s.equalsIgnoreCase("synchronous")) {
            isSequential = false;
            grouping = null;
            return;
        }

        if (s.startsWith("sequential ")) {
            isSequential = true;
            grouping = new DeterministicGrouping(model, s.substring(11));
            return;
        }

        if (s.startsWith("priority ")) {
            isSequential = false;
            grouping = new DeterministicGrouping(model, s.substring(9));
            return;
        }

        throw new RuntimeException("Unrecognized updater: "+s);
    }

    public DeterministicSimulation getSimulation() {
        byte[] state = this.state;
        if (state == null) {
            state = new byte[model.getComponents().size()];
        }

        DeterministicUpdater updater = getUpdater();
        return new DeterministicSimulation(updater, state, max_steps);
    }

    public DeterministicUpdater getUpdater() {
        if (grouping != null) {
            if (isSequential) {
                return grouping.getBlockSequentialUpdater();
            }
            return grouping.getPriorityUpdater();
        }

        if (isSequential) {
            return new SequentialUpdater(model);
        }
        return new SynchronousUpdater(model);
    }
}

