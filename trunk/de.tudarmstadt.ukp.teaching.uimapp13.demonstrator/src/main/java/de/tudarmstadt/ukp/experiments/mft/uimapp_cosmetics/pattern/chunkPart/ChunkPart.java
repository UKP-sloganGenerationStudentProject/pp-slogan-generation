package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart;

import java.util.ArrayList;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator.Resources;


public class ChunkPart
{

    protected ChunkPartType _chunkPartType;
    protected boolean _isValueDerivable;
    protected String _semanticValue;
    protected String _takenValue;
    protected String _lemma;
    protected String _pos;

    public static final String NOT_DEFINED = "notDefined";

    public enum ChunkPartType
    {
        N("N"),
        PUNC("PUNC"),
        ART("ART"),
        V("V"),
        ADJ("ADJ"),
        PP("PP"),
        PR("PR"),
        CARD("CARD"),
        CONJ("CONJ"),
        PRT("PRT"),
        ADV("ADV"),
        PRODUCT_NAME("productname"),
        UNDEFINED("UNDEFINED");

        private String name ="";
        ChunkPartType(String name)
        {
            this.name = name;
        }

        public static ChunkPartType getTypeOf(String val)
        {
            ChunkPartType type = null;
            if(val.equals("NP")||val.equals("NN"))
            {
                type = N;
            }
            else
            {
                try
                {
                    type = valueOf(val);
                }
                catch(IllegalArgumentException e)
                {
                    type = UNDEFINED;
                }
            }
            return type;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }


    public ChunkPart()
    {
        _isValueDerivable = false;
        _semanticValue = "";
        _takenValue = "NO_INFORMATION";
        _chunkPartType = ChunkPartType.UNDEFINED;
        _lemma = "";
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
        return _chunkPartType;
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
        String signature = _chunkPartType.toString();
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
        output.append(_chunkPartType.toString());
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


        output.append(getSemanticValue());

        output.append("] ");

        return output.toString();
    }

}
