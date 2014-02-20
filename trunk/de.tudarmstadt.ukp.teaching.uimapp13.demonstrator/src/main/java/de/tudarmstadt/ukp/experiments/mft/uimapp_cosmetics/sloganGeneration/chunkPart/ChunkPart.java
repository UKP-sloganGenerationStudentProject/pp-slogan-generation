package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;

/**
 * A ChunkPart is equivalent to a token generator.
 * It is defined by several parameters (see below).
 *
 * It is linked two other objects that play a role in the pattern representation.
 * - the {@link Chunk} that encapsulates this ChunkPart in the slogan it was derived from.
 * - the generic form {@link ChunkPartGeneric} of this ChunkPart (like an equivalence class that this instance is part of)
 *
 *  @author Matthieu Fraissinet-Tachet
 */


public class ChunkPart implements Serializable
{


    private static final long serialVersionUID = -5552300315457638822L;

    //indicate if the value generated has to be the original value or if it can be deerived from it
    protected boolean _isValueDerivable;

    protected String _semanticValue;
    protected String _takenValue;
    protected String _lemma;
    protected String _pos;


    protected ChunkPartGeneric _genericForm;
    protected Chunk _containingChunk;

    //say if the given chunkPart has a constraint on itself
    private boolean _hasConstraint;
    private int _associatedConstraintId;


    public static final String NOT_DEFINED = "notDefined";


    public ChunkPart()
    {
        _isValueDerivable = false;
        _semanticValue = "";
        _takenValue = "NO_INFORMATION";
        _lemma = "";
        _containingChunk = null;
        _hasConstraint = false;
        _associatedConstraintId = -1;
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
        output.generateInformation();

        return output;
    }

    public void generateInformation()
    {

    }

    public void setGeneric(ChunkPartGeneric generic)
    {
        _genericForm = generic;
    }

    public ChunkPartGeneric getGeneric()
    {
        return _genericForm;
    }

    public void setContainingChunk(Chunk chunk)
    {
        _containingChunk = chunk;
    }

    /**
     * generate the ChunkPartSolution solutions list for this instance.
     *if this instance has constraints then the generated solutions also have constsraints.
     * this method is overwritten by the child classes.
     * @param resources
     * @param originalPart : ChunkPart instance from which started the generation process. This is in
     * the pattern associated to this original ChunkPart that the generated ChunkPartSolutions are
     * going to be inserted.  In some cases (for instance for VerbChunkPart)
     *we use the information from the originalPart to adapt the ChunkPartSolution to its context
     *(for instance to conjugate it)
     * @return
     */
    public List<ChunkPartSolution> generateChunkPartSolution(Resources resources, ChunkPart originalPart)
    {
        //generate the ChunkPartSolution solutions list for this instance.
        //if this instance has constraints then the generated solutions also have constsraints.
        //the last parameter : original part, corresponds to the ChunkPart instance in the pattern  which place
        //the generated Chunkpartsolutions are going to take. In some cases (for instance for VerbChunkPart)
        //we use the information from the originalPart to adapt the ChunkPartSolution to its context
        //(for instance to conjugate it). This will be done automatically. We just have to transmit
        //the originalPart to the ChunkPartSolutions
        List<ChunkPartSolution> output = new ArrayList<ChunkPartSolution>();
        ChunkPartSolution solution = new ChunkPartSolution(originalPart,null,_lemma);
        if(this._hasConstraint)
        {
            solution.addConstraintId(this._associatedConstraintId);
        }
        output.add(solution);
        return output;
    }

    public List<ChunkPart> getSimilarOccurrences(Resources resources)
    {
        List<ChunkPart> chunkParts = new ArrayList<>();

        //unlike for Chunk, here we just return the current chunkPart and the chunkParts derived
        //from the constraints of the
        //chunkPartGeneric this instance is associated to
        chunkParts.add(this);
        chunkParts.addAll(this._genericForm.getConstrainedElements());

        return chunkParts;
    }

    public void checkForGenericConstraints(Resources resources)
    {
        _genericForm.checkForConstraints(resources);
    }

    public boolean hasGenericConstraints()
    {
        return _genericForm.hasConstraint();
    }

    public boolean hasConstraint()
    {
        return _hasConstraint;
    }

    public void associateConstraint(int id)
    {
        _associatedConstraintId = id;
        _hasConstraint = true;
    }

    public int getConstraintId()
    {
        return _associatedConstraintId;
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
        return _genericForm.getChunkPartType();
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

    /**
     * this method is used to derive the given lemma in the form of the current chunk part.
     * It is implemented by subclasses.
     * @param lemma
     * @return
     */
    public String deriveFromLemmaForm(String lemma)
    {
        return lemma;
    }

    public String getId()
    {
        String signature = _genericForm.getChunkPartType().toString();
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
