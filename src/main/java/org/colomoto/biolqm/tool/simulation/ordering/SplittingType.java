package org.colomoto.biolqm.tool.simulation.ordering;

public enum SplittingType {

    MERGED, POSITIVE, NEGATIVE;

    public String toString() {
        switch (this) {
            case POSITIVE:
                return "[+]";
            case NEGATIVE:
                return "[-]";
        }
        return "";
    }

}
