package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPartHeader.ChunkPartType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.Chunk;


public class ChunkPart
{

    protected ChunkPartHeader _header;
    protected Chunk _containingChunk;
    protected boolean _isValueDerivable;
    protected String _semanticValue;
    protected String _takenValue;
    protected String _lemma;
    protected String _pos;


    public static final String NOT_DEFINED = "notDefined";




    public ChunkPart()
    {
        _isValueDerivable = false;
        _semanticValue = "";
        _takenValue = "NO_INFORMATION";
        _lemma = "";
        _containingChunk = null;
    }


    public static ChunkPart createChunkPart(ChunkPartType chunkPartType)
    {

        ChunkPart output = new ChunkPart();

        if(chunkPartType.equals(ChunkPartType.N))
        {
            return new NounChunkPart();
        }

        if(chunkPartType.equals(ChunkPartType.ADJ))
        {
            return new AdjChunkPart();
        }

        if(chunkPartType.equals(ChunkPartType.V))
        {
            return new VerbChunkPart();
        }

        if(chunkPartType.equals(ChunkPartType.PP))
        {
            return new PrepChunkPart();
        }

        if(chunkPartType.equals(ChunkPartType.PRODUCT_NAME))
        {
            return new ProductNameChunkPart();
        }

        return output;
    }

    public static ChunkPart createChunkPart(ChunkPartType chunkPartType,String fixedValue,String lemma,String semanticValue,String pos)
    {
        ChunkPart output = createChunkPart(chunkPartType);

        output.setSemanticValue(semanticValue);
        output.setTakenValue(fixedValue);
        output.setPos(pos);
        output.setLemma(lemma);

        return output;
    }

    public void setHeader(ChunkPartHeader header)
    {
        _header = header;
    }

    public void setContainingChunk(Chunk chunk)
    {
        _containingChunk = chunk;
    }

    public List<ChunkPart> getSimilarOccurrences()
    {
        List<ChunkPart> output =  _header.getOccurrences();
        if(output.size()==0)
        {
            output.add(this);
        }
        return output;
    }

    public ArrayList<String> generate(Resources resources)
    {
        ArrayList<String> output = new ArrayList<String>();
        output.add(_takenValue);
        return output;
    }

    public boolean isValueDerivable()
    {
        return _isValueDerivable;
    }

    public void setDerivable(boolean tof)
    {
        _isValueDerivable = tof;
    }

    public String getTakenValue()
    {
        return _takenValue;
    }

    public void setTakenValue(String val)
    {
        _takenValue = val;
    }

    public void setLemma(String lemma)
    {
        _lemma = lemma;
    }

    public void setPos(String pos)
    {
        _pos = pos;
    }

    public String getPos()
    {
        return _pos;
    }

    public ChunkPartType getChunkPartType()
    {
        return _header.getChunkPartType();
    }

    public void setSemanticValue(String value)
    {
        _semanticValue = value;
    }

    public String getSemanticValue()
    {
        return _semanticValue;
    }

    public String getLemma()
    {
        return _lemma;
    }

    public String getId()
    {
        String signature = _header.getChunkPartType().toString();
        if(_isValueDerivable)
        {
            signature = signature + "-VAR" + "[sem:" + getSemanticValue() + "]";
        }
        else
        {
            signature = signature + "-FIX-" + _takenValue;
        }
        return signature;
    }


    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();

        output.append(_takenValue);

        output.append(" [ ");


        output.append(getSemanticValue());

        output.append("] ");

        return output.toString();
    }

}
