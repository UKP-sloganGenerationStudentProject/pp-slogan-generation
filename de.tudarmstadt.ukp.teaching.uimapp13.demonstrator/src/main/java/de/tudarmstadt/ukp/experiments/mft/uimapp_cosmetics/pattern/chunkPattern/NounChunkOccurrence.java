package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart.ChunkPartType;

public class NounChunkOccurrence
    extends ChunkOccurrence
{
    boolean _isBodyPart;
    String _bodyPartName;

    public NounChunkOccurrence()
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
            if(occPart.getChunkPartType().equals(ChunkPartType.N) && occPart.getSemanticValue().equals("body") && PatternGenerator.getSelectablePartsOfBody().contains(occPart.getLemma()))
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

}
