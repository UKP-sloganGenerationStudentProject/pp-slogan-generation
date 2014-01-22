package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;



public class AdvChunkHeader
    extends ChunkHeader
{

    public AdvChunkHeader()
    {
        super();
        _chunkType = ChunkType.ADVC;
    }

    @Override
    public void specializedHeaderGeneration(ChunkOccurrence occurrence)
    {
        _isValueDerivable = false;
    }

}
