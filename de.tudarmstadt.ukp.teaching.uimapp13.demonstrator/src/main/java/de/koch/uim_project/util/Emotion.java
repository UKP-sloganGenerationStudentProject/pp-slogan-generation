package de.koch.uim_project.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;

/**
 * Enum for all emotions which are recored in custom database with their ID used
 * in database
 * 
 * @author Frerik Koch
 * 
 */
public enum Emotion
{
    ANGER(3), ANTICIPATION(4), DISGUST(5), FEAR(6), JOY(7), NEGATIVE(2), POSITIVE(1), SADNESS(8), SURPRISE(
            9), TRUST(10), NONE(0);

    public final int DATABASE_ID;

    Emotion(final int dbId)
    {
        this.DATABASE_ID = dbId;
    }

    public static Emotion fromId(final int id)
    {
        switch (id) {
        case 0:
            return NONE;
        case 3:
            return ANGER;
        case 4:
            return ANTICIPATION;
        case 5:
            return DISGUST;
        case 6:
            return FEAR;
        case 7:
            return JOY;
        case 2:
            return NEGATIVE;
        case 1:
            return POSITIVE;
        case 8:
            return SADNESS;
        case 9:
            return SURPRISE;
        case 10:
            return TRUST;
        default:
            return null;
        }
    }

    public static List<String> valuesAsString()
    {
        return new ArrayList<>(Collections2.transform(Arrays.asList(values()),
                Functions.toStringFunction()));
    }

}
