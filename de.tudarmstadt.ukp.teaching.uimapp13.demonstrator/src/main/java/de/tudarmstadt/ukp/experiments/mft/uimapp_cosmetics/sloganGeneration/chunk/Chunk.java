package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPartSolution;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.pattern.Pattern;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

public class Chunk
    implements Serializable
{
    private static final long serialVersionUID = -6421940819649545578L;
    ChunkGeneric _genericForm;
    Pattern _containingPattern;
    ArrayList<ChunkPart> _chunkParts;
    int _mainChunkInd;
    List<ChunkSolution> _generated;
    private boolean _hasConstraint;
    private boolean _haveConstraintsBeenChecked;

    public Chunk()
    {
        this._chunkParts = new ArrayList<ChunkPart>();
        this._mainChunkInd = -1;
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

    public void addChunkPart(final ChunkPart part)
    {
        this._chunkParts.add(part);
    }

    public void setHeader(final ChunkGeneric header)
    {
        this._genericForm = header;
    }

    public ChunkGeneric getHeader()
    {
        return this._genericForm;
    }

    public void setContainingPattern(final Pattern pattern)
    {
        this._containingPattern = pattern;
    }

    public ChunkType getChunkType()
    {
        return this._genericForm.getChunkType();
    }

    public String getSemantic()
    {
        return this._genericForm.getSemanticValue();
    }

    public String getHeaderId()
    {
        return this._genericForm.getId();
    }

    public String getId()
    {
        final StringBuilder output = new StringBuilder();

        for (final ChunkPart occ : this._chunkParts) {
            output.append(occ.getTakenValue());
            output.append(" ");
        }
        return output.toString();
    }

    public List<Chunk> getSimilarChunkOccurrences(final Resources resources)
    {
        List<Chunk> output = new ArrayList<>();
        if (this._genericForm.getSemanticValue().equals("UNKNOWN")) {
            output.add(this);
        }
        else {
            output = this._genericForm.getOccurrences(resources);
        }
        return output;
    }

    public void generateInformation(final Resources resources)
    {
    }

    public List<ChunkSolution> generateChunkSolutions(final Resources resources,
            final Chunk originalChunk)
    {
        if (this._generated.size() == 0) {

            List<ChunkSolution> newChunkSolutions = new ArrayList<>();
            final ChunkSolution initialChunkSolution = new ChunkSolution(this);
            newChunkSolutions.add(initialChunkSolution);

            for (final ChunkPart occ : this._chunkParts) {
                final List<ChunkSolution> newChunkSolutionsTemp = new ArrayList<>();
                for (final ChunkPart equiv : occ.getSimilarOccurrences(resources)) {
                    final List<ChunkPartSolution> generatedParts = equiv.generate(resources, occ);
                    if (generatedParts.size() > 0) {
                        newChunkSolutionsTemp.addAll(ChunkSolution.concatenate(resources,
                                newChunkSolutions, equiv.generate(resources, occ)));
                    }
                }
                newChunkSolutions = newChunkSolutionsTemp;
            }

            this._generated = newChunkSolutions;
        }

        return this._generated;

    }

    public int getPartsNbr()
    {
        return this._chunkParts.size();
    }

    public ChunkPart getAt(final int i)
    {
        return this._chunkParts.get(i);
    }

    public List<ChunkPart> getChunkParts()
    {
        return this._chunkParts;
    }

    public void resetCache()
    {
        this._generated = new ArrayList<>();
    }

    public void checkForGenericConstraints(final Resources resources)
    {
        this._genericForm.checkForConstraints(resources);
    }

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

    public boolean hasConstraint()
    {
        return this._hasConstraint;
    }

    public boolean hasGenericConstraint()
    {
        return this._genericForm.hasConstraint();
    }

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
