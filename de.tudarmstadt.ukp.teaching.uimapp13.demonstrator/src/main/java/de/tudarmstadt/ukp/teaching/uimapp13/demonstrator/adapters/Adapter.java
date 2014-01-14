package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public interface Adapter
{
    void initialize(final Map<String, Object> parameters);

    List<Slogan> generateSlogans(final Map<String, Object> parameters);
}
