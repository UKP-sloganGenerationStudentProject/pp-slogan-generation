package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.index.IndexElement;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

public class ChunkGeneric
    extends IndexElement
    implements Serializable
{

    private static final long serialVersionUID = 1325933957546266085L;
    protected ChunkType _chunkType;
    protected boolean _isValueDerivable;
    protected String _semanticValue;
    protected String _takenValue;
    private final ArrayList<Chunk> _occurrences;
    private boolean _hasConstraint;
    private final List<Chunk> _constrainedElements;
    private boolean _haveConstraintsBeenChecked;

    public static final String NOT_DEFINED = "notDefined";



    public ChunkGeneric()
    {
        this._isValueDerivable = true;
        this._semanticValue = "";
        this._takenValue = "NO_INFORMATION";
        this._chunkType = ChunkType.UNDEFINED;
        this._occurrences = new ArrayList<Chunk>();
        _constrainedElements = new ArrayList<>();
        _haveConstraintsBeenChecked = false;
    }

    public static ChunkGeneric createChunkHeader(final ChunkType chunkType)
    {
        ChunkGeneric output = new ChunkGeneric();

        if (chunkType.equals(ChunkType.NC)) {
            output = new NounChunkGeneric();
        }

        if (chunkType.equals(ChunkType.ADJC)) {
            output = new AdjChunkGeneric();
        }

        if (chunkType.equals(ChunkType.VC)) {
            output = new VerbChunkGeneric();
        }

        if (chunkType.equals(ChunkType.PC)) {
            output = new PrepChunkGeneric();
        }

        if (chunkType.equals(ChunkType.ADVC)) {
            output = new AdvChunkGeneric();
        }

        return output;
    }

    public void addOccurrence(final Chunk occ)
    {
        for (final Chunk occ2 : this._occurrences) {
            if (occ.toString().toLowerCase().equals(occ2.toString().toLowerCase())) {
                return;
            }
        }
        this._occurrences.add(occ);
    }

    public List<Chunk> getOccurrences(Resources resources)
    {

        List<Chunk> chunks = null;

        if(resources.hasConstraints() && this._hasConstraint)
        {
            chunks = this._constrainedElements;
        }
        else
        {
            chunks = this._occurrences;
        }

        return chunks;
    }

    public boolean isValueDerivable()
    {
        return this._isValueDerivable;
    }

    public void setFixedValue(final String fixedValue)
    {
        this._isValueDerivable = false;
        this._takenValue = fixedValue;
    }

    public ChunkType getChunkType()
    {
        return this._chunkType;
    }

    public void setSemanticValue(final String sem)
    {
        this._semanticValue = sem;
    }

    public String getSemanticValue()
    {
        return this._semanticValue;
    }

    @Override
    public String getId()
    {
        String signature = this._chunkType.toString();
        if (this._isValueDerivable) {
            signature = signature + "-VAR" + "[sem:" + this._semanticValue + "]";
        }
        else {
            signature = signature + "-FIX-" + this._takenValue;
        }
        return signature;
    }

    public void generateHeader(final Chunk occurrence)
    {
        // general operation
        this._takenValue = occurrence.toString();

        // generation specialized to the header type
        this.specializedHeaderGeneration(occurrence);
    }

    public void specializedHeaderGeneration(final Chunk occurrence)
    {
        // to be implemented by the subclasses
    }

    public void checkForConstraints (Resources resources)
    {
        if(!_haveConstraintsBeenChecked)
        {
            for(Chunk chunk : _occurrences)
            {
                chunk.checkForConstraints(resources);
                if(chunk.hasConstraint())
                {
                    _constrainedElements.add(chunk);
                }
            }
            _hasConstraint = _constrainedElements.size()>0;
            _haveConstraintsBeenChecked = true;
        }

    }

    public List<Chunk> getConstrainedElements()
    {
        return _constrainedElements;
    }

    public boolean hasConstraint()
    {
        return _hasConstraint;
    }

    public void releaseConstraints()
    {
        _constrainedElements.clear();
        _hasConstraint = false;
        _haveConstraintsBeenChecked = false;
        for(Chunk chunk : _occurrences)
        {
            chunk.releaseConstraints();
        }
    }


    public void resetCache()
    {
        for (final Chunk occ : this._occurrences) {
            occ.resetCache();
        }
    }

    @Override
    public String toString()
    {
        final StringBuilder output = new StringBuilder();
        output.append(this._chunkType.toString());
        output.append(" [semantics:");
        output.append(_semanticValue);
        output.append("]");
        output.append(" [derivability:");
        output.append(_isValueDerivable);
        output.append("]");
        output.append(" [hasConstraint:");
        output.append(_hasConstraint);
        output.append("]");
        output.append(" [haveConstraintsBeenChecked:");
        output.append(_haveConstraintsBeenChecked);
        output.append("]");
        output.append(" [constrained elements:");
        for(Chunk chunk : _constrainedElements)
        {
            output.append(chunk.getId());
            output.append(";");
        }
        output.append("]");
        output.append(this.getSpecificInformation());

        for (final Chunk occ : this._occurrences) {
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
        return this.getChunkType().toString();
    }
}