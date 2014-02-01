package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPattern;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.PatternGenerator;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;

public class NounChunk
    extends Chunk
{
    boolean _isBodyPart;
    String _bodyPartName;

    public NounChunk()
    {
        super();
        _isBodyPart = false;
        _bodyPartName = "UNKNOWN";
    }

    @Override
    public void generateInformation(Resources resources)
    {

        super.generateInformation(resources);

        for(ChunkPart occPart : _chunkParts)
        {
            if(occPart.getChunkPartType().equals(ChunkPartType.N) && occPart.getSemanticValue().equals("body") && PatternGenerator.getSelectablePartsOfBody().contains(occPart.getLemma()) && PatternGenerator.getSelectablePartsOfBody().contains(occPart.getLemma()))
            {
                _isBodyPart = true;
                _bodyPartName = occPart.getLemma();
            }
        }
    }

    public void setIsBodyPart()
    {
        _isBodyPart = true;
    }

    public boolean isBodyPart()
    {
        return _isBodyPart;
    }

    public String getBodyPartName()
    {
        return _bodyPartName;
    }

    @Override
    public String toString()
    {
        return super.toString()+" [ isBodyPart : "+_isBodyPart+"; bodyPartName : "+_bodyPartName+"]";
    }

}
