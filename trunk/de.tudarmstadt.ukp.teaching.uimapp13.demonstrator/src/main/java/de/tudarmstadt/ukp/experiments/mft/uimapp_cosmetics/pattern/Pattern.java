package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkHeader.ChunkType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkOccurrence;

public class Pattern
{
    private final ArrayList<ChunkOccurrence> _elementList;
    private final ArrayList<String> _valueOccurrences;
    private String _patternType;
    private boolean _isBodyPart;

    public Pattern()
    {
        _elementList = new ArrayList<ChunkOccurrence>();
        _valueOccurrences = new ArrayList<String>();
        _patternType = "UNDEFINED";
        _isBodyPart = false;
    }

    public void generatePatternType()
    {
        StringBuilder type = new StringBuilder();

        for(ChunkOccurrence occ : _elementList)
        {
            type.append(occ.getChunkType().toString());
            type.append("_");
        }

        _patternType = type.toString();

    }

    public void generateIsBodyPart()
    {
        for(ChunkOccurrence occ : _elementList)
        {
            if(occ.getChunkType().equals(ChunkType.NC) && occ.getSemantic().equals("body"))
            {
                _isBodyPart = true;
                break;
            }
        }
    }

    public String getPatternType()
    {
        return _patternType;
    }

    public boolean isBodyPart()
    {
        return _isBodyPart;
    }

    public void generateInformations()
    {
        generatePatternType();
        generateIsBodyPart();
    }

    public void addElement(ChunkOccurrence el)
    {
        _elementList.add(el);
    }

    public void addValueOccurrence(String value)
    {
        _valueOccurrences.add(value);
    }

    public ArrayList<String> getValueOccurrences()
    {
        return _valueOccurrences;
    }

    public String getId()
    {
        String id = "";

        for(ChunkOccurrence elem: _elementList)
        {
            id = id + elem.getHeaderId() + "_";
        }

        return id;
    }

    public List<ChunkOccurrence> getPatternElementList()
    {
        return _elementList;
    }


    public void addOccurrence(Pattern other)
    {
        if(!getId().equals(other.getId()))
        {
            return;
        }

        _valueOccurrences.addAll(other.getValueOccurrences());
    }


    public String getElementIdAt(int ind)
    {
        return _elementList.get(ind).getHeaderId();
    }

    public List<String> generateSlogans(Resources resources,int numberOfSlogans)
    {
        List<String> newSlogans = new ArrayList<String>();
        newSlogans.add("");

        for(ChunkOccurrence elem : _elementList)
        {
            List<String> temp = new ArrayList<String>();
            temp = Utils.concatenate(newSlogans, elem.generateSloganParts(resources,numberOfSlogans));
            newSlogans = temp;
        }

        List<String> slogansSublist = null;

        if(numberOfSlogans>0)
        {
            slogansSublist = Utils.getSubList(newSlogans, numberOfSlogans);
        }
        else
        {
           slogansSublist = newSlogans;
        }

        return slogansSublist;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof Pattern))
        {
            return false;
        }

        Pattern patt = (Pattern) obj;

        return getId().equals(patt.getId());
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();

        for(ChunkOccurrence elem: _elementList)
        {
            output.append(elem.getHeaderId());
            output.append(" _ ");
        }

        output.append("\n");

        for(String value : _valueOccurrences)
        {
            output.append(value);
            output.append("\n");
        }

        return output.toString();
    }
}