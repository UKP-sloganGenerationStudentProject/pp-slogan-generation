package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPattern;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Pattern;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;


public class Chunk
{
    ChunkGeneric _genericForm;
    Pattern _containingPattern;
    ArrayList<ChunkPart> _chunkParts;
    int _mainChunkInd;
    List<ChunkSolution> _generated;
    private boolean _hasConstraint;
    private boolean _haveConstraintsBeenChecked;


    public Chunk()
    {
        _chunkParts = new ArrayList<ChunkPart>();
        _mainChunkInd = -1;
        _genericForm = new ChunkGeneric();
        _generated = new ArrayList<>();
        _containingPattern = null;
        _hasConstraint = false;
        _haveConstraintsBeenChecked = false;
    }

    public static Chunk createChunkOccurrence(ChunkType chunkType)
    {
        Chunk output = new Chunk();

        if(chunkType.equals(ChunkType.NC))
        {
            output = new NounChunk();
        }

        return output;
    }



    public void addChunkPart(ChunkPart part)
    {
        _chunkParts.add(part);
    }

    public void setHeader(ChunkGeneric header)
    {
        _genericForm = header;
    }

    public ChunkGeneric getHeader()
    {
        return _genericForm;
    }

    public void setContainingPattern(Pattern pattern)
    {
        _containingPattern = pattern;
    }

    public ChunkType getChunkType()
    {
        return _genericForm.getChunkType();
    }

    public String getSemantic()
    {
        return _genericForm.getSemanticValue();
    }

    public String getHeaderId()
    {
        return _genericForm.getId();
    }

    public String getId()
    {
        StringBuilder output = new StringBuilder();

        for(ChunkPart occ : _chunkParts)
        {
            output.append(occ.getTakenValue());
            output.append(" ");
        }
        return output.toString();
    }

    public List<Chunk> getSimilarChunkOccurrences(Resources resources)
    {
        List<Chunk> output = new ArrayList<>();
        if(_genericForm.getSemanticValue().equals("UNKNOWN"))
        {
            output.add(this);
        }
        else
        {
            output = _genericForm.getOccurrences(resources);
        }
        return output;
    }

    public void generateInformation(Resources resources)
    {
    }


    public List<ChunkSolution> generateChunkSolutions(Resources resources, Chunk originalChunk)
    {
        if(_generated.size()==0)
        {

            List<ChunkSolution> newChunkSolutions = new ArrayList<>();
            ChunkSolution initialChunkSolution = new ChunkSolution(this);
            newChunkSolutions.add(initialChunkSolution);

            for(ChunkPart occ : _chunkParts)
            {
                List<ChunkSolution> newChunkSolutionsTemp = new ArrayList<>();
                for(ChunkPart equiv : occ.getSimilarOccurrences(resources))
                {
                    newChunkSolutionsTemp.addAll(ChunkSolution.concatenate(newChunkSolutions,equiv.generate(resources,occ)));
                }
                newChunkSolutions = newChunkSolutionsTemp;
            }

            _generated = newChunkSolutions;
        }

        return _generated;

    }

    public int getPartsNbr()
    {
        return _chunkParts.size();
    }

    public ChunkPart getAt(int i)
    {
        return _chunkParts.get(i);
    }

    public List<ChunkPart> getChunkParts()
    {
        return _chunkParts;
    }

    public void resetCache()
    {
        _generated = new ArrayList<>();
    }

    public void checkForGenericConstraints(Resources resources)
    {
        _genericForm.checkForConstraints(resources);
    }

    public void checkForConstraints(Resources resources)
    {
        if(!_haveConstraintsBeenChecked)
        {
            for(ChunkPart part : _chunkParts)
            {
                part.checkForGenericConstraints(resources);
                _hasConstraint = _hasConstraint || part.hasGenericConstraints();
            }
            _haveConstraintsBeenChecked = true;
        }

    }

    public boolean hasConstraint()
    {
        return _hasConstraint;
    }

    public boolean hasGenericConstraint()
    {
        return _genericForm.hasConstraint();
    }

    public void releaseConstraints()
    {
        _hasConstraint = false;
        _haveConstraintsBeenChecked = false;
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append(" [hasConstraint:");
        output.append(_hasConstraint);
        output.append("]");
        output.append(" [haveConstraintsBeenChecked:");
        output.append(_haveConstraintsBeenChecked);
        output.append("]");

        for(ChunkPart occ : _chunkParts)
        {
            output.append(occ.getTakenValue());
            output.append(" ");
        }
        return output.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof Chunk))
        {
            return false;
        }

        Chunk occ = (Chunk) obj;

        return toString().toLowerCase().equals(occ.toString().toLowerCase());
    }

}
