package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;

public class NounChunkHeader
    extends ChunkHeader
{

    boolean _isThe;
    boolean _isProperNoun;
    boolean _isPerson;


    public NounChunkHeader()
    {
        super();
        _chunkType = ChunkType.NC;
        _isThe = false;
        _isProperNoun = false;
        _isPerson = false;
    }

    @Override
    public String getId()
    {
        StringBuilder id = new StringBuilder();
        id.append(super.getId());
        id.append(" [");
        id.append("_isThe:");
        id.append(_isThe);
        id.append("]");
        id.append(" [");
        id.append("ProperNoun:");
        id.append(_isProperNoun);
        id.append("]");
        id.append("isPerson:");
        id.append(_isPerson);
        return id.toString();
    }


    @Override
    public String getSpecificInformation()
    {
        StringBuilder output = new StringBuilder();
        output.append(" [");
        output.append("_isThe:");
        output.append(_isThe);
        output.append("]");
        output.append(" [");
        output.append("ProperNoun:");
        output.append(_isProperNoun);
        output.append("]");
        output.append("isPerson:");
        output.append(_isPerson);
        return output.toString();
    }


    public void setIsThe()
    {
        _isThe = true;
    }


    public void setIsProperNoun()
    {
        _isProperNoun = true;
    }

    public void setIsPerson()
    {
        _isPerson = true;
    }

    @Override
    public void specializedHeaderGeneration(ChunkOccurrence occurrence)
    {
        _isValueDerivable = true;
        _semanticValue = occurrence.getAt(occurrence.getPartsNbr()-1).getSemanticValue();

        //isThe
        for(ChunkPart occPart : occurrence.getChunkParts())
        {
            if(occPart.getTakenValue().toLowerCase().equals("the"))
            {
                setIsThe();
            }
        }

        //proper noun
        //TODO does not work
        String pos =  occurrence.getAt(occurrence.getPartsNbr()-1).getPos();

        if(pos.startsWith("NNP"))
        {
            setIsProperNoun();
        }

        //isPerson
        List<String> person = Arrays.asList("i","me","myself","you","yourself","she","her","herself","he","him","himself","we","us","ourself","ourselves","they","them","themself","themselves");
        if(person.contains(occurrence.getAt(occurrence.getPartsNbr()-1).getTakenValue()))
        {
            setIsPerson();
        }


    }
}
