package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.Utils;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkHeader.ChunkType;


public class ChunkOccurrence
{
    ChunkHeader _header;
    ArrayList<ChunkPart> _chunkParts;
    int _mainChunkInd;
    List<String> _generated;

    public ChunkOccurrence()
    {
        _chunkParts = new ArrayList<ChunkPart>();
        _mainChunkInd = -1;
        _header = new ChunkHeader();
        _generated = new ArrayList<>();
    }

    public void addChunkPart(ChunkPart part)
    {
        _chunkParts.add(part);
    }

    public void setHeader(ChunkHeader header)
    {
        _header = header;
    }

    public ChunkType getChunkType()
    {
        return _header.getChunkType();
    }

    public String getSemantic()
    {
        return _header.getSemanticValue();
    }

    public String getHeaderId()
    {
        return _header.getId();
    }

    public String getId()
    {
        StringBuilder output = new StringBuilder();

        for(ChunkPart occ : _chunkParts)
        {
            output.append(occ.getTakenValue());
            output.append(" ");
        }
        return output.toString();
    }

    public List<String> generateSloganParts(Resources resources, int nbr)
    {
        if(_header.getSemanticValue()=="UNKNOWN")
        {
            //if the semantic is unknown we don't look for similar chunks
            //as these will be all the chunk whose semantic is unknown
            //and they have nothing to do togehter
            if(_generated.size()==0)
            {
                _generated = generateSloganPartsIntern(resources,nbr);
            }
            return _generated;

        }

        return _header.generateSloganParts(resources,nbr);
    }

    public List<String> generateSloganPartsIntern(Resources resources)
    {
        return generateSloganPartsIntern(resources, -1);
    }

    public List<String> generateSloganPartsIntern(Resources resources, int nbr)
    {

        List<String> output = new ArrayList<String>();
        output.add("");
        ArrayList<String> space = new ArrayList<String>();
        space.add(" ");

        for(ChunkPart occ : _chunkParts)
        {
            output = Utils.concatenate(output, occ.generate(resources));
        }

        if(nbr>0)
        {
            output = Utils.getSubList(output, nbr);

        }

        return output;

    }

    public int getPartsNbr()
    {
        return _chunkParts.size();
    }

    public ChunkPart getAt(int i)
    {
        return _chunkParts.get(i);
    }

    public List<ChunkPart> getChunkParts()
    {
        return _chunkParts;
    }

    public void resetCache()
    {
        _generated = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();

        for(ChunkPart occ : _chunkParts)
        {
            output.append(occ.getTakenValue());
            output.append(" ");
        }
        return output.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof ChunkOccurrence))
        {
            return false;
        }

        ChunkOccurrence occ = (ChunkOccurrence) obj;

        return toString().toLowerCase().equals(occ.toString().toLowerCase());
    }

}
