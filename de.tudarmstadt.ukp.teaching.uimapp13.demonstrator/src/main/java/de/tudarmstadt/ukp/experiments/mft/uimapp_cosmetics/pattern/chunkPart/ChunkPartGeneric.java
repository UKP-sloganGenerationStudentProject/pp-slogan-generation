package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.constraints.WordConstraint;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.index.IndexElement;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;

public class ChunkPartGeneric extends IndexElement
{
    protected ChunkPartType _chunkPartType;
    protected String _semanticValue;
    protected boolean _isValueDerivable;
    protected final ArrayList<ChunkPart> _occurrences;
    private boolean _hasConstraint;
    private final List<ChunkPart> _constrainedElements;


    public ChunkPartGeneric()
    {
        _isValueDerivable = false;
        _semanticValue = "";
        _chunkPartType = ChunkPartType.UNDEFINED;
        _occurrences = new ArrayList<ChunkPart>();
        _hasConstraint = false;
        _constrainedElements = new ArrayList<>();

    }

    public static ChunkPartGeneric createChunkHeader(ChunkPartType type,String semantic)
    {
        ChunkPartGeneric output = new ChunkPartGeneric();
        output.setPosType(type);
        output.setSemanticValue(semantic);
        if(type.equals(ChunkPartType.ADJ))
        {
            output.setValueDerivable(true);
        }
        else
        {
            output.setValueDerivable(false);
        }

        return output;
    }

    public void addOccurrence(ChunkPart occ)
    {
        for(ChunkPart occ2 : _occurrences)
        {
            if(occ.toString().toLowerCase().equals(occ2.toString().toLowerCase()))
            {
                return;
            }
        }
        _occurrences.add(occ);
    }

    public List<ChunkPart> getOccurrences()
    {
        List<ChunkPart> output = new ArrayList<>();
        if(_isValueDerivable && !_semanticValue.equals("UNKNOWN"))
        {
            output = _occurrences;
        }
        return _occurrences;
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


        output.append(_semanticValue);

        output.append("] ");

        for(ChunkPart occ : _occurrences)
        {
            output.append("\n\t");
            output.append(occ.toString());
        }

        return output.toString();
    }

    private void setPosType(ChunkPartType type)
    {
        _chunkPartType = type;
    }

    public ChunkPartType getChunkPartType()
    {
        return _chunkPartType;
    }

    private void setSemanticValue(String semantic)
    {
        _semanticValue = semantic;
    }

    public String getSemanticValue()
    {
        return _semanticValue;
    }

    public boolean isValueDerivable()
    {
        return _isValueDerivable;
    }

    private  void setValueDerivable(boolean tof)
    {
        _isValueDerivable = tof;
    }

    public void selectElementsWithConstraint(Resources resources)
    {
        for(WordConstraint constraint : resources.getConstraints())
        {
            if(constraint.getChunkPartType().equals(this._chunkPartType) && constraint.getSemantic().equals(this._semanticValue))
            {
                /* the constraint corresponds to this element */
                ChunkPart part = new ChunkPart();
                part.setDerivable(false);
                part.setHeader(this);
                part.setLemma(constraint.getLemma());
                part.setSemanticValue(this._semanticValue);
                _constrainedElements.add(part);
            }
        }
        _hasConstraint = _constrainedElements.size()>0;

    }

    public void releaseConstraints()
    {
        _hasConstraint = false;
        _constrainedElements.clear();
    }

    public boolean hasConstraint()
    {
        return _hasConstraint;
    }

    public List<ChunkPart> getConstrainedElements()
    {
        return _constrainedElements;
    }


    @Override
    protected String getId()
    {
        StringBuilder output = new StringBuilder();

        output.append(_chunkPartType.toString());
        output.append(" [semantics:");
        output.append(_semanticValue);
        output.append("]");

        return output.toString();
    }

    @Override
    protected String getTypeAsString()
    {
        return _chunkPartType.toString();
    }

}