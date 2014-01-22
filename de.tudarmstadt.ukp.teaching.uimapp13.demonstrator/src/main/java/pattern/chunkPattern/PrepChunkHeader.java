package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;



public class PrepChunkHeader
    extends ChunkHeader
{

    public PrepChunkHeader()
    {
        super();
        _chunkType = ChunkType.PC;
    }

    @Override
    public void specializedHeaderGeneration(ChunkOccurrence occurrence)
    {
        _isValueDerivable = false;
    }

}
