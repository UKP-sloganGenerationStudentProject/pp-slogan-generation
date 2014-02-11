package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.constraints.WordConstraint;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index.IndexElement;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;

/*
 * a ChunkPartGeneric instance builds an equivalence class of ChunkParts.
 * We don't use it in the same way like ChunkGeneric.. we don't say that all the elements from a
 * same equivalence class can be permuted, but we say that if there is a suggested word that fits
 * into an equivalence class, then it can replace every element of this class during the slogan
 * generation.
 */
public class ChunkPartGeneric extends IndexElement implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -1507232787235496603L;
    /*
     * parameters that define all the ChunkParts contained in this ChunkPratGeneric instance
     */
    protected ChunkPartType _chunkPartType;
    protected String _semanticValue;
    protected boolean _isValueDerivable;
    private boolean _hasConstraint;
    private boolean _haveConstraintsBeenChecked;

    /*
     * ChunkPart members
     */
    protected final ArrayList<ChunkPart> _occurrences;

    /*
     * ChunkParts derived from the sugggested words that apply to this ChunkPartGeneric instance
     */
    private final List<ChunkPart> _constrainedElements;


    public ChunkPartGeneric()
    {
        _isValueDerivable = false;
        _semanticValue = "";
        _chunkPartType = ChunkPartType.UNDEFINED;
        _occurrences = new ArrayList<ChunkPart>();
        _hasConstraint = false;
        _constrainedElements = new ArrayList<>();
        _haveConstraintsBeenChecked = false;

    }

    public static ChunkPartGeneric createChunkPartGeneric(ChunkPartType type,String semantic)
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

    public void checkForConstraints(Resources resources)
    {
        if(!_haveConstraintsBeenChecked)
        {

            for(WordConstraint constraint : resources.getConstraints())
            {
                if(constraint.getChunkPartType().equals(this._chunkPartType) && constraint.getSemantic().equals(this._semanticValue))
                {
                    /* the constraint corresponds to this element */
                    /* create a chunkpart deriving from this constraint */
                    ChunkPart part = ChunkPart.createChunkPart(this._chunkPartType);
                    part.setDerivable(false);
                    part.setGeneric(this);
                    part.setLemma(constraint.getLemma());
                    part.setSemanticValue(this._semanticValue);
                    part.associateConstraint(constraint.getId());
                    _constrainedElements.add(part);
                }
            }
            _hasConstraint = _constrainedElements.size()>0;
            _haveConstraintsBeenChecked = true;
        }

    }

    public void releaseConstraints()
    {
        _hasConstraint = false;
        _constrainedElements.clear();
        _haveConstraintsBeenChecked = false;
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
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append(_chunkPartType.toString());
        output.append(" [semantics:");
        output.append(_semanticValue);
        output.append("]");
        output.append(" [hasConstraint:");
        output.append(_hasConstraint);
        output.append("]");
        output.append(" [haveConstraintsBeenChecks:");
        output.append(_haveConstraintsBeenChecked);
        output.append("]");
        output.append(" [constrained elements:");
        for(ChunkPart chunkPart : _constrainedElements)
        {
            output.append(chunkPart.getLemma());
            output.append(";");
        }
        output.append("]");

        for(ChunkPart occ : _occurrences)
        {
            output.append("\n\t");
            output.append(occ.toString());
        }

        return output.toString();
    }

    @Override
    protected String getTypeAsString()
    {
        return _chunkPartType.toString();
    }

}
