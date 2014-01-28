package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkHeader.ChunkType;

public class ChunkIndex
    implements Serializable
{
    private static final long serialVersionUID = 6772044159525718129L;

    Hashtable<String, ChunkHeader> _elementsIndex;

    public ChunkIndex()
    {
        this._elementsIndex = new Hashtable<String, ChunkHeader>();
    }

    public ChunkHeader addHeader(final ChunkHeader header)
    {

        ChunkHeader output;

        final String patternElementId = header.getId();
        final ChunkHeader assocHeader = this._elementsIndex.get(patternElementId);
        if (assocHeader == null) {
            output = header;
            this._elementsIndex.put(patternElementId, header);
        }
        else {
            output = assocHeader;
        }

        return output;

    }

    public ChunkHeader get(final String id)
    {
        return this._elementsIndex.get(id);
    }

    public Set<String> getIdSet()
    {
        return this._elementsIndex.keySet();
    }

    public Collection<ChunkHeader> getPatternElements()
    {
        return this._elementsIndex.values();
    }

    @Override
    public String toString()
    {
        final Hashtable<ChunkType, StringBuilder> partialOutput = new Hashtable<ChunkHeader.ChunkType, StringBuilder>();

        for (final ChunkHeader element : this._elementsIndex.values()) {
            StringBuilder stb = partialOutput.get(element.getChunkType());
            if (stb == null) {
                stb = new StringBuilder();
                partialOutput.put(element.getChunkType(), stb);
            }
            stb.append(element.toString());
            stb.append("\n");
        }

        final StringBuilder output = new StringBuilder();

        for (final ChunkType key : partialOutput.keySet()) {
            output.append("\n");
            output.append(partialOutput.get(key).toString());
        }

        return output.toString();
    }

}