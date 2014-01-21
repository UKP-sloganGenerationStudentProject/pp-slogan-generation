package de.koch.uim_project.analyse;

import de.koch.uim_project.generation.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.koch.uim_project.database.DbException;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.enums.ERelTypeSemantics;
import de.tudarmstadt.ukp.lmf.model.semantics.SenseRelation;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.model.semantics.SynsetRelation;

/**
 * This class packages uby analysis
 * 
 * @author Frerik Koch
 * 
 */
public class UbyAnalyser {

	private static UbyAnalyser ubyAna;
	private static Logger log = Logger.getRootLogger();

	private UbyAnalyser() {

	}

	/**
	 * Return the UbyAnalyser instance
	 * 
	 * @return
	 */
	public static UbyAnalyser getInstance() {
		if (ubyAna == null) {
			ubyAna = new UbyAnalyser();
		}
		return ubyAna;
	}

	/**
	 * Retrieves {@link Synset} from uby
	 * 
	 * @param word
	 *            word to retrieve {@link Synset} for
	 * @param pos
	 *            POS of the word
	 * @return All {@link Synset} a given word is part of
	 * @throws DbException
	 */
	public List<Synset> getSynset(String word, EPartOfSpeech pos, Uby uby) throws DbException {
		List<Synset> result = new ArrayList<Synset>();
		try {

			List<LexicalEntry> lens = getLexicalEntries(word, pos, null, uby);
			for (LexicalEntry len : lens) {
				result.addAll(len.getSynsets());
			}

			return result;
		} catch (Exception e) {
			log.error("Failed to retrieve synsets from uby", e);
			throw new DbException("Failed to retrieve synsets from uby");
		}
	}

	/**
	 * Retrieves {@link Sense}s from uby for a given word
	 * 
	 * @param word
	 *            Word to retrieve {@link Sense}s for
	 * @param pos
	 *            POS of given word
	 * @param lexiconName
	 *            Lexicon to retrieve {@link Sense}s from. Maybe null to check
	 *            all Lexica
	 * @return {@link Sense}s for the given word
	 * @throws DbException
	 */
	public List<Sense> getSenses(String word, EPartOfSpeech pos, String lexiconName, Uby uby) throws DbException {
		ArrayList<Sense> result = new ArrayList<Sense>();

		try {
			for (LexicalEntry len : uby.getLexicalEntries(word, pos, lexiconName == null ? null : uby.getLexiconByName(lexiconName))) {
				result.addAll(len.getSenses());
			}

		} catch (Exception e) {
			log.error("Failed to retrieve senses from uby", e);
			throw new DbException("Failed to retrieve senses from uby");
		}

		return result;

	}

	/**
	 * Retrieves all {@link LexicalEntry}s from uby for a given word
	 * 
	 * @param word
	 *            Word to retrieve {@link LexicalEntry}s for
	 * @param pos
	 *            POS of given word
	 * @param lexiconName
	 *            Lexicon to retrieve {@link LexicalEntry}s from. Maybe null to
	 *            check all lexica
	 * @return {@link LexicalEntry}s for given word
	 * @throws DbException
	 */
	public List<LexicalEntry> getLexicalEntries(String word, EPartOfSpeech pos, String lexiconName, Uby uby) throws DbException {
		List<LexicalEntry> result;

		try {
			result = uby.getLexicalEntries(word, pos, lexiconName == null ? null : uby.getLexiconByName(lexiconName));

		} catch (Exception e) {
			log.error("Failed to retrieve lexical entries from uby", e);
			throw new DbException("Failed to retrieve lexical entries from uby");
		}

		return result;
	}

	public Set<Synset> generateSynsetsFromWords(Set<Word> words, Uby uby) throws DbException {
		Set<Synset> result = new HashSet<Synset>();
		for (Word word : words) {
			result.addAll(UbyAnalyser.getInstance().getSynset(word.getLemma(), word.getPos(), uby));
		}
		return result;
	}

	public Set<Synset> generateSynsetsFromWords(Word word, Uby uby) throws DbException {
		Set<Word> words = new HashSet<Word>();
		words.add(word);
		return generateSynsetsFromWords(words, uby);
	}

	public Set<Word> getRelatedWordsSense(Word word, Collection<String> relNames, Collection<ERelTypeSemantics> relTypes, Uby uby) throws DbException {
		Set<Word> result = new HashSet<Word>();
		Set<Sense> sourceSenses = new HashSet<Sense>();
		Set<Sense> targetSenses = new HashSet<Sense>();

		sourceSenses.addAll(getSenses(word.getLemma(), word.getPos(), null, uby));
		for (Sense sense : sourceSenses) {
			for (SenseRelation rel : sense.getSenseRelations()) {
				if (rel.getTarget() != null && (relNames == null || relNames.contains(rel.getRelName())) && (relTypes == null || relTypes.contains(rel.getRelType()))) {
					targetSenses.add(rel.getTarget());
				}
			}
		}
		
		for(Sense sense : targetSenses){
			Word newWord = Word.fromLexicalEntry(sense.getLexicalEntry());
			if (newWord != null) {
				result.add(newWord);
			}
		}
		return result;
	}

	public Set<Word> getRelatedWordsSynset(Word word, Collection<String> relNames, Collection<ERelTypeSemantics> relTypes, Uby uby)
			throws DbException {
		Set<Synset> sourceSynsets = generateSynsetsFromWords(word, uby);
		Set<Synset> targetSynsets = new HashSet<Synset>();
		for (Synset synset : sourceSynsets) {
			targetSynsets.addAll(getRelatedSynsets(synset, relNames, relTypes));
		}
		return retrieveWordsFromSynsets(targetSynsets);

	}

	/**
	 * Retrieves related synsets filtered by relation name and relation type. If
	 * either value is null the corresponding filtering is omitted
	 * 
	 * @param relationSource
	 *            Synset to retrieve related synsets for
	 * @param relNames
	 *            Relation name to filter for. If null no filtering happens on
	 *            this attribute
	 * @param relTypes
	 *            Relation type to filter for. If null no filtering happens on
	 *            this attribute
	 * @return Related synsets
	 */
	public Set<Synset> getRelatedSynsets(Synset relationSource, Collection<String> relNames, Collection<ERelTypeSemantics> relTypes) {
		Set<Synset> result = new HashSet<Synset>();
		for (SynsetRelation rel : relationSource.getSynsetRelations()) {
			if ((relNames == null || relNames.contains(rel.getRelName())) && (relTypes == null || relTypes.contains(rel.getRelType()))) {
				result.add(rel.getTarget());
			}
		}
		return result;
	}

	/**
	 * This method extracts all words from a {@link Collection} of
	 * {@link Synset}s.
	 * 
	 * @param synsets
	 *            {@link Synset}s to extract words from
	 * @return Words from {@link Synset}s
	 */
	public Set<Word> retrieveWordsFromSynsets(Collection<Synset> synsets) {
		Set<Word> result = new HashSet<Word>();
		for (Synset synset : synsets) {
			for (Sense sense : synset.getSenses()) {
				Word newWord = Word.fromLexicalEntry(sense.getLexicalEntry());
				if (newWord != null) {
					result.add(newWord);
				}
			}
		}
		return result;
	}

}
