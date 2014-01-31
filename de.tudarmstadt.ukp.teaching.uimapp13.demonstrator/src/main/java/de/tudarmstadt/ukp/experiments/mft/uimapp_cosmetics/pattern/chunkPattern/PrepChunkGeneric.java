package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;


public class PrepChunkGeneric
    extends ChunkGeneric
{
    private static final long serialVersionUID = -4041465958122119822L;

    public PrepChunkGeneric()
    {
        super();
        this._chunkType = ChunkType.PC;
    }

    @Override
    public void specializedHeaderGeneration(final Chunk occurrence)
    {
        this._isValueDerivable = false;
    }

}