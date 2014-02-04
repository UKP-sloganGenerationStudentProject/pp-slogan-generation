package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPartSolution;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.pattern.Pattern;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

/**
 * A Chunk represents a generator for chunks in linguistic.
 * It is defined by several parameters (see below).
 *
 * It is linked two other objects that play a role in the pattern representation.
 * - the Pattern that encapsulates this Chunk in the slogan it was derived from.
 * - the generic form of this Chunk (like an equivalence class that this instance is part of)
 *
 * A chunk is made of chunkParts (_chunkParts) that play the roles of token generators.
 *
 * _generated stores the slogans that have been once generated so that when a chunk is used several
 * times during slogan generation, we can use the chunkSolutions already generated to save time.
 * It is possible to reset _generated with resetCache().
 *
 * @author Matthieu Fraissinet-Tachet
 */


public class Chunk
    implements Serializable
{
    private static final long serialVersionUID = -6421940819649545578L;

    ChunkGeneric _genericForm;
    Pattern _containingPattern;

    ArrayList<ChunkPart> _chunkParts;
    List<ChunkSolution> _generated;

    private boolean _hasConstraint;
    private boolean _haveConstraintsBeenChecked;

    public Chunk()
    {
        this._chunkParts = new ArrayList<ChunkPart>();
        this._genericForm = new ChunkGeneric();
        this._generated = new ArrayList<>();
        this._containingPattern = null;
        this._hasConstraint = false;
        this._haveConstraintsBeenChecked = false;
    }

    public static Chunk createChunkOccurrence(final ChunkType chunkType)
    {
        Chunk output = new Chunk();

        if (chunkType.equals(ChunkType.NC)) {
            output = new NounChunk();
        }

        return output;
    }

    /**
     *add a new ChunkPart instance to the ChunkPart list of the chunk.
     * @param ChunkPart instance to add to the chunk
     */
    public void addChunkPart(final ChunkPart part)
    {
        this._chunkParts.add(part);
    }

    /**
     * set the ChunkGeneric object that contains all the similar chunks
     * @param generic
     */
    public void setGeneric(ChunkGeneric generic)
    {
        this._genericForm = generic;
    }

    /**
     * Return the associated ChunkGeneric that contains all the similar chunks
     * @return
     */
    public ChunkGeneric getGeneric()
    {
        return this._genericForm;
    }

    /**
     * Return the Pattern instance that contains this Chunk instance
     * @param pattern
     */
    public void setContainingPattern(final Pattern pattern)
    {
        this._containingPattern = pattern;
    }

    /**
     * return the ChunkType of this instance
     * @return
     */
    public ChunkType getChunkType()
    {
        return this._genericForm.getChunkType();
    }

    /**
     * return the semantic associated to this instance.
     * @return
     */
    public String getSemantic()
    {
        return this._genericForm.getSemanticValue();
    }

    /**
     * return the id of the ChunkGeneric instance associated to the current chunk.
     * @return
     */
    public String getGenericId()
    {
        return this._genericForm.getId();
    }

    /**
     * return the id of the current object.
     * @return
     */
    public String getId()
    {
        final StringBuilder output = new StringBuilder();

        for (final ChunkPart occ : this._chunkParts) {
            output.append(occ.getTakenValue());
            output.append(" ");
        }
        return output.toString();
    }

    /**
     * return a list of the chunks that can replace this one
     * @param resources
     * @return
     */
    public List<Chunk> getSimilarChunkOccurrences(final Resources resources)
    {
        /*
         * return all the occurrences similar to this instance, which is to say all the
         * instances that belong to the associated ChunkGeneric instance, which is like an equivalent
         * class.
         * BUT if the semantic of this chunk is unknown, we just return this instance, because equivalent
         * class is then full of Chunks with unknown semantics and they have nothing to do together.
         */
        List<Chunk> output = new ArrayList<>();
        if (this._genericForm.getSemanticValue().equals("UNKNOWN")) {
            output.add(this);
        }
        else {
            output = this._genericForm.getOccurrences(resources);
        }
        return output;
    }

    /**
     * generate intern information about the chunk, once all the chunkparts have been set
     * @param resources
     */
    public void generateInformation(final Resources resources)
    {
        /*
         * after the chunk has been constructed (during the pattern generation and NOT during the
         * slogan generation), some information has to be generated and this is done in this method
         *
         * This method should be overwritten by subclasses if necessary.
         */
    }

    /**
     * generate possible solutions to this chunk for the creation of new slogans
     * @param resources
     * @param originalChunk
     * @return list of ChunkSolution
     */
    public List<ChunkSolution> generateChunkSolutions(final Resources resources,
            final Chunk originalChunk)
    {
        if (this._generated.size() == 0) {
            //if the "cache" is empty, we generate the chunks from scratch

            //we initialize the list of solution with an empty solution (because the solutions will
            //be incrementally constructed by a sort of a concatenation
            List<ChunkSolution> newChunkSolutions = new ArrayList<>();
            final ChunkSolution initialChunkSolution = new ChunkSolution(this);
            newChunkSolutions.add(initialChunkSolution);

            for (final ChunkPart occ : this._chunkParts) {
                final List<ChunkSolution> newChunkSolutionsTemp = new ArrayList<>();
                for (final ChunkPart equiv : occ.getSimilarOccurrences(resources)) {
                    //for each chunkPart the chunk is made of, we extract all the chunkPart solutions and concatenate it to
                    //the partial solutions that have been constructed so far
                    final List<ChunkPartSolution> generatedParts = equiv.generateChunkPartSolution(resources,occ);
                    if(generatedParts.size()>0)
                    {
                        newChunkSolutionsTemp.addAll(ChunkSolution.concatenate(resources,newChunkSolutions,equiv.generateChunkPartSolution(resources,occ)));
                    }
                }
                newChunkSolutions = newChunkSolutionsTemp;
            }

            this._generated = newChunkSolutions;
        }

        return this._generated;

    }

    /**
     * return the number of chunkParts in the chunk
     * @return
     */
    public int getPartsNbr()
    {
        return this._chunkParts.size();
    }

    /**
     * return the ChunkPart at the i-th position in the list of chunkparts definint the chunk.
     * @param i
     * @return
     */
    public ChunkPart getAt(final int i)
    {
        return this._chunkParts.get(i);
    }

    /**
     * get the list of the chunkparts defining the current chunk
     * @return
     */
    public List<ChunkPart> getChunkParts()
    {
        return this._chunkParts;
    }

    /**
     * erase all the partial solutions that has been stored during the process
     *  of the slogan generation.
     */
    public void resetCache()
    {
        this._generated = new ArrayList<>();
    }

    /**
     * identify the elements that respect the constraints (over the suggested words) in the
     * ChunkGeneric (where all the equivalent chunks are stored)
     * @param resources
     */
    public void checkForGenericConstraints(final Resources resources)
    {
        this._genericForm.checkForConstraints(resources);
    }

    /**
     * process the constraints over the current chunk
     * @param resources
     */
    public void checkForConstraints(final Resources resources)
    {
        if (!this._haveConstraintsBeenChecked) {
            for (final ChunkPart part : this._chunkParts) {
                part.checkForGenericConstraints(resources);
                this._hasConstraint = this._hasConstraint || part.hasGenericConstraints();
            }
            this._haveConstraintsBeenChecked = true;
        }

    }

   /**
    * return true if the current chunk has a constraint
    * @return
    */
    public boolean hasConstraint()
    {
        return this._hasConstraint;
    }

    /**
     * return true if the associated ChunkGeneric object has constraints
     * @return
     */
    public boolean hasGenericConstraint()
    {
        return this._genericForm.hasConstraint();
    }

    /**
     * reset the chunk as if the constraints hadn't been processed
     */
    public void releaseConstraints()
    {
        this._hasConstraint = false;
        this._haveConstraintsBeenChecked = false;
    }

    @Override
    public String toString()
    {
        final StringBuilder output = new StringBuilder();
        output.append(" [hasConstraint:");
        output.append(this._hasConstraint);
        output.append("]");
        output.append(" [haveConstraintsBeenChecked:");
        output.append(this._haveConstraintsBeenChecked);
        output.append("]");

        for (final ChunkPart occ : this._chunkParts) {
            output.append(occ.getTakenValue());
            output.append(" ");
        }
        return output.toString();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Chunk)) {
            return false;
        }

        final Chunk occ = (Chunk) obj;

        return this.toString().toLowerCase().equals(occ.toString().toLowerCase());
    }

}
