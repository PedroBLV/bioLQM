package org.colomoto.biolqm.tool.fixpoints;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.tool.AbstractTool;
import org.colomoto.biolqm.tool.LogicalModelTool;
import org.mangosdk.spi.ProviderFor;


@ProviderFor(LogicalModelTool.class)
public class FixpointTool extends AbstractTool<FixpointList, FixpointSettings> {

	public static final String UID = "fixpoints";
	public static final String[] ALIASES = { "stable", "fixed", "fp" };

	public static final String HELP_LINE = "Search fixed (stable) states";
	public static final String HELP_MESSAGE = "arguments: asp pattern";

	public FixpointTool() {
		super(UID, ALIASES, HELP_LINE, HELP_MESSAGE, true);
	}

	@Override
	public FixpointSettings getSettings(LogicalModel model, String ... parameters) {
		FixpointSettings settings = new FixpointSettings(model);

		for (String p: parameters) {
			p = p.trim();
			if ("asp".equalsIgnoreCase(p )) {
				settings.method = FixpointMethod.ASP;
			}
			if ("pattern".equalsIgnoreCase(p )) {
				settings.pattern = true;
			}
		}
		return settings;
	}

	@Override
	public void run(LogicalModel model, String ... parameters) {
		FixpointList result = null;
		try {
			result = getResult(model, parameters);
		} catch(Exception e) {
			System.out.println("Error while constructing the result");
			e.printStackTrace();
			return;
		}
		
		if (result == null || result.size() < 1) {
			System.out.println("NO RESULTS");
			return;
		}
		
		// print out the result
    	for (NodeInfo node : model.getComponents()) {
    		System.out.print(node.getNodeID() + " ");
    	}
    	System.out.println();

    	for (byte[] path: result) {
	        for (int i: path) {
	        	if (i<0) {
	        		System.out.print("-");
	        	} else {
	        		System.out.print(i);
	        	}
	        }
	        System.out.println();
    	}
	}

	public FixpointTask getTask(FixpointSettings settings) {
		return new FixpointTask(settings);
	}

	@Override
	public FixpointList getResult(FixpointSettings settings) throws Exception {
		return getTask(settings).call();
	}

}
