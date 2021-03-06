package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index.IndexElement;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

/**
 * A ChunkGeneric instance represents an equivalence class for Chunk instances. It contains
 * equivalent chunks.
 *
 * A ChunkGeneric instance is defined by its {@link ChunkType}, its associated semantics value and
 * also another parameter "isDerivable" that says if the equivalent class accepts other equivalents.
 *
 * @author Matthieu Fraissinet-Tachet
 */

/*
 * The chunk elements that are parts of this equivalence class are listed in this._occurrences
*  its parameters caracterize all the members if the equivalence class, except :
*      * _hasConstraint indicates if at least one member of the class respect a suggested words constraint
*      * _haveConstraintsBeenChecked indicates if the constraints have been processed over
*          all the members of the equivalence class
*      * _takenValue is used for Chunks that shouldn't have other equivalent than themselves. It
*          stores the text value (from the slogan) of the chunk. Those cases are characterized
*          by _isValueDerivable = false; ex : PrepChunkGeneric
*/

public class ChunkGeneric
    extends IndexElement
    implements Serializable
{

    private static final long serialVersionUID = 1325933957546266085L;

    /*
     * parameters that define all the chunks contained in this ChunkGeneric instance
     */
    protected ChunkType _chunkType;
    protected boolean _isValueDerivable;
    protected String _semanticValue;

    /*
     * Chunk members
     */
    private final ArrayList<Chunk> _occurrences;

    private boolean _hasConstraint;
    private boolean _haveConstraintsBeenChecked;

    protected String _takenValue;

    public static final String NOT_DEFINED = "notDefined";



    public ChunkGeneric()
    {
        this._isValueDerivable = true;
        this._semanticValue = "";
        this._takenValue = "NO_INFORMATION";
        this._chunkType = ChunkType.UNDEFINED;
        this._occurrences = new ArrayList<Chunk>();
        _haveConstraintsBeenChecked = false;
    }

    public static ChunkGeneric createChunkGeneric(final ChunkType chunkType)
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

    /**
     * add a new Chunk to the equivalence class
     * @param occ
     */
    public void addOccurrence(final Chunk occ)
    {

        for (final Chunk occ2 : this._occurrences) {
            if (occ.toString().toLowerCase().equals(occ2.toString().toLowerCase())) {
                return;
            }
        }
        this._occurrences.add(occ);
    }

    /**
     * retrieve the list of chunks from this equivalence class
     * @param resources
     * @return
     */
    public List<Chunk> getOccurrences(Resources resources)
    {

        List<Chunk> chunks = null;
        chunks = this._occurrences;

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

    /**
     * initialize this ChunkGeneric to contain all Chunk equivalent to the one given as parameter
     * @param occurrence
     */
    public void generateGeneric(final Chunk occurrence)
    {
        // general operation
        this._takenValue = occurrence.toString();

        // generation specialized to the header type
        this.specializedGenericGeneration(occurrence);
    }

    /**
     * This method is to be overwritten by the subclasses. It is used  in generateGeneric()
     * @param occurrence
     */
    public void specializedGenericGeneration(final Chunk occurrence)
    {
        // to be implemented by the subclasses
    }

    /**
     * process the constraints over the current chunkGeneric (ie. check if at least one element
     * of the current chunk can generate chunks containing at least one suggested word).
     * @param resources
     */
    public void checkForConstraints (Resources resources)
    {
        if(!_haveConstraintsBeenChecked)
        {
            for(Chunk chunk : _occurrences)
            {
                chunk.checkForConstraints(resources);
                if(chunk.hasConstraint())
                {
                    _hasConstraint = true;
                }
            }
            _haveConstraintsBeenChecked = true;
        }

    }

    /**
     * return true if this object has constraints
     * @return
     */
    public boolean hasConstraint()
    {
        return _hasConstraint;
    }

    /**
     * reset the chunkGeneric as if the constraints hadn't been processed (undo checkForConstraints())
     */
    public void releaseConstraints()
    {
        _hasConstraint = false;
        _haveConstraintsBeenChecked = false;
        for(Chunk chunk : _occurrences)
        {
            chunk.releaseConstraints();
        }
    }

    /**
     * erase all the partial solutions that has been stored during the process
     *  of the slogan generation.
     */
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