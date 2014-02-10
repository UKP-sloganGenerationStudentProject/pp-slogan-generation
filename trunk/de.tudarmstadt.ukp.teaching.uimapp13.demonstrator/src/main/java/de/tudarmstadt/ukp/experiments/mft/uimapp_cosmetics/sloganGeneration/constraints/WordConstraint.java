package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.constraints;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.meta.SemanticLabel;

public class WordConstraint
{
    /*
     * a word constraint represents  a constraint derived from a suggested word for the slogan
     * generation.
     * A constraint is defined by : the string value it was derived from, the corresponding lemma,
     * the corresponding ChunkPartType, the associated semantic and finally the id associated to the
     * suggested word this constraint was derived from.
     * The id depends on the suggested word because several constraints are derived from a same
     * suggested word (it can have several ChunkPartType (ie POS) or several semantics. But we
     * don't want to have several times the same suggested word in the same slogan.
     */

    String _takenValue;
    String _lemma;
    ChunkPartType _chunkPartType;
    String _semantic;
    int _id;


    public WordConstraint(int id)
    {
        this._takenValue = "";
        this._lemma = "";
        this._chunkPartType = ChunkPartType.UNDEFINED;
        this._semantic = "";
        this._id=id;
    }


    public WordConstraint(int id,String takenValue, String lemma, ChunkPartType chunkPartType,
            String semantic)
    {
        this(id);
        this._takenValue = takenValue;
        this._lemma = lemma;
        this._chunkPartType = chunkPartType;
        this._semantic = semantic;
    }


    public static List<WordConstraint> generateWordConstraints(Uby uby, List<String> suggestedWords)
    {
        //generate all the WordCosntraints that can be derivated from the list of suggersted words

        List<WordConstraint> output = new ArrayList<>();

        Lexicon lex;
        try {
            lex = uby.getLexiconByName("WordNet");
        }
        catch (UbyInvalidArgumentException e)
        {
            e.printStackTrace();
            return output;
        }

        int id = -1;

        for(String word : suggestedWords)
        {
            id = id + 1;
            for(LexicalEntry entry : uby.getLexicalEntries(word, null, null))//lex))
            {
                String lemma = entry.getLemmaForm();
                EPartOfSpeech pos = entry.getPartOfSpeech();
                if(pos == null)
                {
                    continue;
                }

                ChunkPartType type = ChunkPartType.getTypeOf(pos);
                if(type.equals(ChunkPartType.UNDEFINED))
                {
                    continue;
                }
                ArrayList<String> sems = new ArrayList<>();
                for(Sense sens : entry.getSenses())
                {
                    for (SemanticLabel sem : sens.getSemanticLabels())
                    {
                        String[] semValueTable = sem.getLabel().split("\\.");
                        if(semValueTable.length>0)
                        {
                            String semValue = semValueTable[semValueTable.length-1];
                            if(!sems.contains(semValue))
                            {
                                sems.add(semValue);
                            }
                        }
                    }
                }

                for(String sem : sems)
                {
                   WordConstraint constraint = new WordConstraint(id,word,lemma,type,sem);
                   output.add(constraint);
                }
            }

        }

        for(WordConstraint contr : output)
        {
            System.out.println(contr);
        }

        return output;
    }

    public String getTakenValue()
    {
        return _takenValue;
    }
    public void setTakenValue(String takenValue)
    {
        this._takenValue = takenValue;
    }
    public String getLemma()
    {
        return _lemma;
    }
    public void setLemma(String lemma)
    {
        this._lemma = lemma;
    }
    public String getSemantic()
    {
        return _semantic;
    }
    public void setSemantic(String semantic)
    {
        this._semantic = semantic;
    }
    public ChunkPartType getChunkPartType()
    {
        return _chunkPartType;
    }
    public void setChunkPartType(ChunkPartType chunkPartType)
    {
        this._chunkPartType = chunkPartType;
    }

    public int getId()
    {
        return this._id;
    }

    @Override
    public String toString()
    {
        return "WordConstraint : "+_id+" "+_takenValue+" "+_lemma+" "+_chunkPartType.toString()+" "+_semantic;
    }
}
