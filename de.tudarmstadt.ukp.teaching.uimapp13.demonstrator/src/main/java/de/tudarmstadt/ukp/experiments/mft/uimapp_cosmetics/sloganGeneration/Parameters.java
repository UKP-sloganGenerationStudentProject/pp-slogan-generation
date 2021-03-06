package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import java.util.Arrays;
import java.util.List;

public class Parameters
{
    public static final String DONT_CARE = "don't care";
    public static final String NO_BODY_PART = "no body part";

    //patterns that are extracted from the corpus and that the user can select for the generation
    private static List<String> _patternsToAnnotate = Arrays.asList(DONT_CARE, "NC_", "NC_PC_NC_",
            "VC_NC_", "VC_NC_PC_NC_",  "NC_VC_", "NC_VC_ADJC_", "NC_VC_NC_","NC_VC_NC_PC_NC_"); //"VC_"

    //parts of the body that the user can select
    private static List<String> _partsOfBodyToSelect = Arrays.asList(NO_BODY_PART, "eye", "skin",
            "lip", "nail", "hair", "lash","hand");

    public static List<String> getSelectablePatterns()
    {
        return _patternsToAnnotate;
    }
    public static List<String> getSelectablePartsOfBody()
    {
        return _partsOfBodyToSelect;
    }
}


