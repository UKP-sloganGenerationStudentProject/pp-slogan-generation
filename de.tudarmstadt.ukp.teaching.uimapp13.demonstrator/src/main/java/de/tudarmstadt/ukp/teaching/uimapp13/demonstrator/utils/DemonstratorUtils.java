package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.utils;

import java.util.Arrays;

import com.google.common.base.Joiner;

public class DemonstratorUtils
{

    public static String convertSeparator(final String wordsSeparatedBySepFrom,
            final String sepFrom, final String sepTo)
    {
        return Joiner.on(sepTo).join(Arrays.asList(wordsSeparatedBySepFrom.split(sepFrom)));
    }

    public static String convertSeparatorFromHtmlTextareaToComma(final String string)
    {
        return convertSeparator(string, "\r\n", ",");
    }

    public static String convertSeparatorFromCommaToHtmlTextarea(final String string)
    {
        return convertSeparator(string, ",", "\r\n");
    }

}
