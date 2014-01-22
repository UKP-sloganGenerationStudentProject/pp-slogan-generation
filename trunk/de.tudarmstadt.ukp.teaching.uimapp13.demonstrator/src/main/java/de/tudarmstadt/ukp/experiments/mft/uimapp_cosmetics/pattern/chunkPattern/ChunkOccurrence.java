package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.Utils;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;


public class ChunkOccurrence
{
    ChunkHeader _header;
    ArrayList<ChunkPart> _chunkParts;
    int _mainChunkInd;

    public ChunkOccurrence()
    {
        _chunkParts = new ArrayList<ChunkPart>();
        _mainChunkInd = -1;
        _header = new ChunkHeader();
    }

    public void addChunkPart(ChunkPart part)
    {
        _chunkParts.add(part);
    }

    public void setHeader(ChunkHeader header)
    {
        _header = header;
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

    public ArrayList<String> generateSloganParts(Resources resources)
    {
        if(_header.getSemanticValue()=="UNKNOWN")
        {
            //if the semantic is unknown we don't look for similar chunks
            //as these will be all the chunk whose semantic is unknown
            //and they have nothing to do togehter
            return generateSloganPartsIntern(resources);
        }
        return _header.generateSloganParts(resources);
    }

    public ArrayList<String> generateSloganPartsIntern(Resources resources)
    {
        ArrayList<String> output = new ArrayList<String>();
        output.add("");
        ArrayList<String> space = new ArrayList<String>();
        space.add(" ");

        for(ChunkPart occ : _chunkParts)
        {
            output = Utils.concatenate(output, occ.generate(resources));
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
