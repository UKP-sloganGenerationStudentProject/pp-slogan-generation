package de.koch.uim_project.util;

import java.util.Arrays;
import java.util.List;

/**
 * Enum over all patterns this project can generate.
 * @author Frerik Koch
 *
 */
public enum Pattern
{
    JJNNJJNN, VBVBVB, JJNN, NNVVN;

    public static List<Pattern> getAll()
    {
        return Arrays.asList(values());
    }
}
