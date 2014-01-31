package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.index.IndexElement;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

public class ChunkHeader
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

    public static final String NOT_DEFINED = "notDefined";



    public ChunkHeader()
    {
        this._isValueDerivable = true;
        this._semanticValue = "";
        this._takenValue = "NO_INFORMATION";
        this._chunkType = ChunkType.UNDEFINED;
        this._occurrences = new ArrayList<Chunk>();
        _constrainedElements = new ArrayList<>();
    }

    public static ChunkHeader createChunkHeader(final ChunkType chunkType)
    {
        ChunkHeader output = new ChunkHeader();

        if (chunkType.equals(ChunkType.NC)) {
            output = new NounChunkHeader();
        }

        if (chunkType.equals(ChunkType.ADJC)) {
            output = new AdjChunkHeader();
        }

        if (chunkType.equals(ChunkType.VC)) {
            output = new VerbChunkHeader();
        }

        if (chunkType.equals(ChunkType.PC)) {
            output = new PrepChunkHeader();
        }

        if (chunkType.equals(ChunkType.ADVC)) {
            output = new AdvChunkHeader();
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

    public List<Chunk> getOccurrences()
    {
        return this._occurrences;
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

    public void selectElementsWithConstraint(Resources resources)
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
        output.append("#");
        if (this._isValueDerivable) {
            output.append("D");
        }
        else {
            output.append("ND");
        }

        output.append(" [ ");

        output.append(this._semanticValue);

        output.append("] ");

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