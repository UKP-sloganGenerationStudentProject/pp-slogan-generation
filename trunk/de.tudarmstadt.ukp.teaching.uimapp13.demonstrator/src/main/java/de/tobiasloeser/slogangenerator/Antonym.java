package de.tobiasloeser.slogangenerator;

import java.io.Serializable;
import java.util.List;

import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.enums.ERelTypeSemantics;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.model.semantics.SynsetRelation;

public class Antonym
    implements Serializable
{

    private static final long serialVersionUID = 5241850566464965161L;
    private Uby uby;
    private Lexicon lex;

    public Antonym(final Uby _uby)
        throws UbyInvalidArgumentException
    {
        this.uby = _uby;
        this.lex = this.uby.getLexiconByName("OmegaWikieng");
    }

    public boolean HasAntonym(final String word)
    {
        final List<LexicalEntry> entries = this.uby.getLexicalEntries(word, this.lex);
        for (final LexicalEntry entry : entries) {
            final List<Synset> synsets = entry.getSynsets();
            for (final Synset synset : synsets) {
                final List<SynsetRelation> synsetRelations = synset.getSynsetRelations();
                for (final SynsetRelation synsetRelation : synsetRelations) {
                    if (synsetRelation.getRelType().equals(ERelTypeSemantics.complementary)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String GetAntonymAsSynset(final String word)
    {
        final List<LexicalEntry> entries = this.uby.getLexicalEntries(word, this.lex);
        for (final LexicalEntry entry : entries) {
            final List<Synset> synsets = entry.getSynsets();
            for (final Synset synset : synsets) {
                final List<SynsetRelation> synsetRelations = synset.getSynsetRelations();
                for (final SynsetRelation synsetRelation : synsetRelations) {
                    if (synsetRelation.getRelType().equals(ERelTypeSemantics.complementary)) {
                        return synsetRelation.getTarget().getId();
                    }
                }
            }
        }
        return null;
    }

}
