package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.index.IndexElement;

public class ChunkHeader extends IndexElement
{

    protected ChunkType _chunkType;
    protected boolean _isValueDerivable;
    protected String _semanticValue;
    protected String _takenValue;
    private final ArrayList<Chunk> _occurrences;


    public static final String NOT_DEFINED = "notDefined";

    public enum ChunkType
    {
        NC("NC"),
        VC("VC"),
        ADJC("ADJC"),
        PC("PC"),
        ADVC("ADVC"),
        UNDEFINED("UNDEFINED");

        private String name ="";
        ChunkType(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }


    public ChunkHeader()
    {
        _isValueDerivable = true;
        _semanticValue = "";
        _takenValue = "NO_INFORMATION";
        _chunkType = ChunkType.UNDEFINED;
        _occurrences = new ArrayList<Chunk>();

    }

    public static ChunkHeader createChunkHeader(ChunkType chunkType)
    {
        ChunkHeader output = new ChunkHeader();

        if(chunkType.equals(ChunkType.NC))
        {
            output = new NounChunkHeader();
        }

        if(chunkType.equals(ChunkType.ADJC))
        {
            output = new AdjChunkHeader();
        }

        if(chunkType.equals(ChunkType.VC))
        {
            output = new VerbChunkHeader();
        }

        if(chunkType.equals(ChunkType.PC))
        {
            output = new PrepChunkHeader();
        }

        if(chunkType.equals(ChunkType.ADVC))
        {
            output = new AdvChunkHeader();
        }

        return output;
    }


    public void addOccurrence(Chunk occ)
    {
        for(Chunk occ2 : _occurrences)
        {
            if(occ.toString().toLowerCase().equals(occ2.toString().toLowerCase()))
            {
                return;
            }
        }
        _occurrences.add(occ);
    }

    public List<Chunk> getOccurrences()
    {
        return _occurrences;
    }

    public boolean isValueDerivable()
    {
        return _isValueDerivable;
    }

    public void setFixedValue(String fixedValue)
    {
        _isValueDerivable = false;
        _takenValue = fixedValue;
    }

    public ChunkType getChunkType()
    {
        return _chunkType;
    }

    public void setSemanticValue(String sem)
    {
        _semanticValue = sem;
    }

    public String getSemanticValue()
    {
        return _semanticValue;
    }

    @Override
    public String getId()
    {
        String signature = _chunkType.toString();
        if(_isValueDerivable)
        {
            signature = signature + "-VAR" + "[sem:" + _semanticValue + "]";
        }
        else
        {
            signature = signature + "-FIX-" + _takenValue;
        }
        return signature;
    }

    public void generateHeader(Chunk occurrence)
    {
        //general operation
        _takenValue=occurrence.toString();

        //generation specialized to the header type
        specializedHeaderGeneration(occurrence);
    }


    public void specializedHeaderGeneration(Chunk occurrence)
    {
        //to be implemented  by the subclasses
    }

    public void resetCache()
    {
        for(Chunk occ : _occurrences)
        {
            occ.resetCache();
        }
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append(_chunkType.toString());
        output.append("#");
        if(_isValueDerivable)
        {
            output.append("D");
        }
        else
        {
            output.append("ND");
        }

        output.append(" [ ");


        output.append(_semanticValue);

        output.append("] ");

        output.append(getSpecificInformation());

        for(Chunk occ : _occurrences)
        {
            output.append("\n\t");
            output.append(occ.toString());
        }

        return output.toString();
    }

    public String getSpecificInformation()
    {
        return "";
    }

    @Override
    protected String getTypeAsString()
    {
        return getChunkType().toString();
    }
}