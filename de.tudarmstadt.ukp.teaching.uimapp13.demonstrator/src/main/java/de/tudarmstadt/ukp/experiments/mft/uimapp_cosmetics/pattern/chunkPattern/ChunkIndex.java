package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkHeader.ChunkType;

public class ChunkIndex
{
    Hashtable<String,ChunkHeader> _elementsIndex;

    public ChunkIndex()
    {
        _elementsIndex = new Hashtable<String, ChunkHeader>();
    }

    public ChunkHeader addHeader(ChunkHeader header)
    {

        ChunkHeader output;

        String patternElementId = header.getId();
        ChunkHeader assocHeader = _elementsIndex.get(patternElementId);
        if(assocHeader==null)
        {
            output = header;
            _elementsIndex.put(patternElementId, header);
        }
        else
        {
            output = assocHeader;
        }

        return output;

    }

    public ChunkHeader get(String id)
    {
        return _elementsIndex.get(id);
    }

    public Set<String> getIdSet()
    {
        return _elementsIndex.keySet();
    }

    public Collection<ChunkHeader> getPatternElements()
    {
        return _elementsIndex.values();
    }

    @Override
    public String toString()
    {
        Hashtable<ChunkType, StringBuilder> partialOutput = new Hashtable<ChunkHeader.ChunkType, StringBuilder>();

        for(ChunkHeader element : _elementsIndex.values())
        {
            StringBuilder stb =  partialOutput.get(element.getChunkType());
            if(stb == null)
            {
                stb = new StringBuilder();
                partialOutput.put(element.getChunkType(), stb);
            }
            stb.append(element.toString());
            stb.append("\n");
        }

        StringBuilder output = new StringBuilder();

        for(ChunkType key : partialOutput.keySet())
        {
            output.append("\n");
            output.append(partialOutput.get(key).toString());
        }

        return output.toString();
    }

}