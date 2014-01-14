package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model;

import java.io.Serializable;

public class Slogan
    implements Serializable
{
    private static final long serialVersionUID = 8284458888720229810L;
    private String text;

    public Slogan(final String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }
}
