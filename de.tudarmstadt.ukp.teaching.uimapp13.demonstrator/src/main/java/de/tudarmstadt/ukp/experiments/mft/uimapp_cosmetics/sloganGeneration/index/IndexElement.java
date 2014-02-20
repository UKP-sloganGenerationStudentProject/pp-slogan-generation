package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index;


/**
 *
 * Abstract class for elements to be used by the class Index.
 * ChunkGeneric and ChunkPartGeneric are deriving from IndexElement
 *
 * Matthieu Fraissinet-Tachet
 */

public abstract class IndexElement
{
    abstract protected String getId();
    abstract protected String getTypeAsString();
}
