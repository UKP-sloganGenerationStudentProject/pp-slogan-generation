package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class BeautyAdapter
    implements Adapter
{

    public void initialize(final Map<String, Object> parameters)
    {

    }

    public List<Slogan> generateSlogans(final Map<String, Object> parameters)
    {
        return Arrays.asList(new Slogan("Testslogan for Beauty"));
    }
}
