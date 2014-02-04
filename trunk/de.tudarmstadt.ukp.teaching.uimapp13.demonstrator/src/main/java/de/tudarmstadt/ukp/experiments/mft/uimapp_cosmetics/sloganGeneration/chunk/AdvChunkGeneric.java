package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

public class AdvChunkGeneric
    extends ChunkGeneric
{

    private static final long serialVersionUID = 2167117216771551930L;

    public AdvChunkGeneric()
    {
        super();
        this._chunkType = ChunkType.ADVC;
    }

    @Override
    public void specializedGenericGeneration(final Chunk occurrence)
    {
        this._isValueDerivable = false;
    }

}
