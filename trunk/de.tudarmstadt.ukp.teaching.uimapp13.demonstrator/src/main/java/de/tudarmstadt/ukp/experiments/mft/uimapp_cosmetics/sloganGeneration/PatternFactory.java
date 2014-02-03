package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.ChunkGeneric;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPartGeneric;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index.Index;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.pattern.Pattern;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

public class PatternFactory
    implements Serializable
{
    /*
     * Use this class to create patterns and then to extract new slogans out of them.
     * The pattern creation with this class is not trivial... The way to use this class
     * is implemented in PatternGenerator.
     */

    private static final long serialVersionUID = -7074314512406365028L;

    /*
     * pattern information
     */
    Hashtable<String, Pattern> _patterns;
    Index<ChunkGeneric> _chunkGenericIndex;
    Index<ChunkPartGeneric> _chunkPartGenericIndex;
    Pattern _currentPattern;
    Chunk _currentChunk;
    ChunkType _currentChunkType;
    String _currentSloganValue;
    boolean _isCurrentPattern;

    public PatternFactory()
    {
        this._patterns = new Hashtable<String, Pattern>();
        this._chunkGenericIndex = new Index<ChunkGeneric>();
        this._chunkPartGenericIndex = new Index<ChunkPartGeneric>();
        this._currentPattern = new Pattern();
        this._currentChunk = new Chunk();
        this._currentSloganValue = "";
        this._isCurrentPattern = false;
    }

    public void startNewChunk(final ChunkType type)
    {
        //create a new instance of chunk in which we are going to add the different chunkParts
        this._currentChunk = Chunk.createChunkOccurrence(type);
        this._currentChunkType = type;
    }

    public void addPartToChunk(ChunkPartType type,final ChunkPart chunkPart)
    {
        // create the corresponding ChunkPartGeneric instance
        final ChunkPartGeneric generic = ChunkPartGeneric.createChunkPartGeneric(type, chunkPart.getSemanticValue());
        // add the generic instance to the index. If a similar instance already exist we return
        // this one.
        final ChunkPartGeneric genericInIndex = _chunkPartGenericIndex.addElement(generic);
        //we link the generic to the occurrence and the occurrence to the generic.
        genericInIndex.addOccurrence(chunkPart);
        chunkPart.setGeneric(genericInIndex);
        //we add the newly created chunkpart to the current chunk (and link in both directions)
        chunkPart.setContainingChunk(_currentChunk);
        this._currentChunk.addChunkPart(chunkPart);
    }

    public void finishChunk(final Resources resources)
    {
        //once the chunk is finished, it can generate some information to describe itself
        this._currentChunk.generateInformation(resources);
        // create the corresponding ChunkGeneric instance
        final ChunkGeneric generic = ChunkGeneric.createChunkGeneric(this._currentChunkType);
        // update it to represent the currentChunk
        generic.generateGeneric(this._currentChunk);
        //add the generic to the index of chunk generics.
        final ChunkGeneric headerInIndex = this._chunkGenericIndex.addElement(generic);
        //link the occurrence and its genric form
        headerInIndex.addOccurrence(this._currentChunk);
        this._currentChunk.setGeneric(headerInIndex);

        //if the current chunk is a part of a pattern, add it to this patterns
        if (this._isCurrentPattern) {
            this._currentPattern.addElement(this._currentChunk);
            this._currentChunk.setContainingPattern(this._currentPattern);
        }
    }

    public void startNewPattern()
    {
        this._currentPattern = new Pattern();
        this._isCurrentPattern = true;
    }

    public void finishPattern()
    {

        //we store the value of the slogan that leaded to this pattern, just to be able to track things.
        this._currentPattern.addSloganOccurrence(this._currentSloganValue);

        // retrieve the id of the pattern
        final String patternId = this._currentPattern.getId();

        //look if a similar pattern already exists
        final Pattern simiPattern = this._patterns.get(patternId);

        if (simiPattern != null) {
            simiPattern.addOccurrence(this._currentPattern);
            //simiPattern.generateInformations();
        }
        else {
            this._currentPattern.generateInformations();
            this._patterns.put(patternId, this._currentPattern);
        }

        this._isCurrentPattern = false;
    }

    public void setSloganToCurrentPattern(final String value)
    {
        //set the value of the slogan that leaded to the current pattern
        this._currentSloganValue = value;
    }

    public void resetTheCacheData()
    {
        //during the generation some data has been stored inside the different objects
        //this is what we delete here. The cache will be automatically reset on chunks, chunkParts, chunkPartGenerics
        for (final ChunkGeneric header : this._chunkGenericIndex.getElements()) {
            header.resetCache();
        }
    }

    public List<String> generateSlogans(final Resources resources, final int nbrOfSlogans)
    {


        this.resetTheCacheData();

        resources.generateSuggestedWordsConstraints();

        //look where the constraints that have been set affect all the patterns, and take it into account
        this.checkForConstraints(resources);

        // output all the parameters
        System.out.println("Pattern Generation STARTS... ");
        System.out.println("\nParameters :");
        System.out.println("\tproductName : " + resources.getProductName());
        System.out.print("\tSelectionned Patterns: "+ resources.getPatternToGenerate());
        System.out.println();
        System.out.print("\tSelectionned part of body : " + resources.getSelectedBodyPart());
        System.out.println();
        System.out.print("\tSuggested words : ");
        for (final String word : resources.getSuggestedWords()) {
            System.out.print(word);
            System.out.print(" ; ");
        }
        System.out.println();


        final List<Pattern> filteredPatterns = new ArrayList<>();

        // filter the patterns and keep just those who correspond to the different creteria

        for (final Pattern pattern : this._patterns.values())
        {

            //we look only for the pattern that can potentially include at least one suggested word
            if(resources.hasSuggestedWordConstraints() && !pattern.hasSuggestedWordConstraint())
            {
                //this pattern doens't include one of the constraints
                continue;
            }

            //we keep only the patterns which type respects the requested one
            if (resources.hasPatternTypeConstraint() && !resources.getPatternToGenerate().equals(pattern.getPatternType()))
            {
                    continue;
            }

            filteredPatterns.add(pattern);
        }

        //randomize those patterns
        Collections.shuffle(filteredPatterns);


        List<String> output = new ArrayList<>();

        /*
         * generate the patterns
         */

        int nbrPatterns = 0;
        for (final Pattern pattern : filteredPatterns)
        {
            nbrPatterns = nbrPatterns + 1;

            output.addAll(pattern.generateSlogans(resources, nbrOfSlogans));
            if(output.size()>nbrOfSlogans*nbrOfSlogans/1.3)
            {
                //we don't need to do the generation for all the patterns if the amount of generated slogans is already sufficient
                //sufficient = big enough so that when we pick randomly among these slogans, we don't have too many slogans looking
                // the same.
                break;
            }
        }

        //constraints were set regarding the suggested words on all the patterns and their subparts.
        //we erase these constraints
        this.releaseConstraints();

        //we only select a subset of the generated slogans
        output = Utils.getSubList(output, nbrOfSlogans);

        return output;
    }

    public void releaseConstraints()
    {
        //we release the constraints at all level of the modelization : Pattern, ChunkGeneric, and ChunkPartGeneric
        //the release is automatically transmitted to Chunk and ChunkPart
        for(Pattern pattern : _patterns.values())
        {
            pattern.releaseConstraints();
        }
        for(ChunkGeneric header : _chunkGenericIndex.getElements())
        {
            header.releaseConstraints();
        }
        for(ChunkPartGeneric header : _chunkPartGenericIndex.getElements())
        {
            header.releaseConstraints();
        }
    }

    public void checkForConstraints(Resources resources)
    {
        //look where the constraints that have been set affect all the patterns, and take it into account
        //the check will be automatically done at all the levels : Chunk,ChunkPart,ChunkGeneric,ChunkPartGeneric
        if(resources.hasSuggestedWordConstraints())
        {
            for(Pattern pattern : _patterns.values())
            {
                pattern.checkForConstraints(resources);
            }
        }
    }

    public Object getChunkIndex()
    {
        return this._chunkGenericIndex;
    }

    public int getNbrOfPatterns()
    {
        return this._patterns.size();
    }

    @Override
    public String toString()
    {
        final StringBuilder output = new StringBuilder();
        output.append("##################PATTERNS##################\n\n");
        for (final Pattern pattern : this._patterns.values()) {
            output.append(pattern.toString());
            output.append("\n");
        }

        output.append("\n\n##################ChunkIndex###############\n\n");
        output.append(this._chunkGenericIndex.toString());

        output.append("\n\n##################ChunkPartIndex###############\n\n");
        output.append(this._chunkPartGenericIndex.toString());



        return output.toString();
    }

}
