package de.koch.uim_project.util;

import java.util.Arrays;
import java.util.List;

/**
 * Enumeration of stylistic devices supported by this application
 * 
 * @author Frerik Koch
 * 
 */
public enum StylisticDevice
{
    Metaphor, Oxymoron, Alliteration, Parallelism, None;

    public static List<StylisticDevice> getAll()
    {
        return Arrays.asList(values());
    }
}
