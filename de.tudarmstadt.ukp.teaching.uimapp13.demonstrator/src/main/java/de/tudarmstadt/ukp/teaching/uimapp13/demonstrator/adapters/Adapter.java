package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.ProductDomain;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public interface Adapter
    extends Serializable
{
    void initialize(final Map<String, Object> parameters)
        throws Exception;

    List<Slogan> generateSlogans(final Map<String, Object> parameters)
        throws Exception;

    ProductDomain getDomain();
}
