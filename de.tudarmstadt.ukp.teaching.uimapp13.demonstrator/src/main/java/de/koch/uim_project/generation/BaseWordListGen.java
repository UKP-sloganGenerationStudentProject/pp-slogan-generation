package de.koch.uim_project.generation;

import de.koch.uim_project.generation.exception.NoMorGenerationPossibleException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.analyse.FeatureListConverter;
import de.koch.uim_project.analyse.UbyAnalyser;
import de.koch.uim_project.database.DbException;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;



public class BaseWordListGen {
	
		private ArrayList<Set<Word>> wordLists = new ArrayList<Set<Word>>();
		private Set<Synset> visitedSynsets;
		private boolean useEmotion;
		private Config config;
		private Generator gen;

		public BaseWordListGen(boolean useEmotion, Config config, Generator gen) throws DbException {
			this.useEmotion = useEmotion;
			this.config = config;
			this.gen = gen;
			if(useEmotion){
				wordLists.add(gen.getCustomDb().getEmotionBatch(FeatureListConverter.getInstance().transformFeatureList(config.getFeatureList(),gen.getUby())));
				
			}else{
				wordLists.add(FeatureListConverter.getInstance().transformFeatureList(config.getFeatureList(),gen.getUby()));
			}
			if(!config.getGameName().matches("\\s*") && !config.getGameName().equals("No Name") ){
				wordLists.get(0).add((new Word(config.getGameName(),EPartOfSpeech.noun,true)));
			}
			visitedSynsets = new HashSet<Synset>();

		}
		
		private Set<Word> generateMore() throws DbException, NoMorGenerationPossibleException {
			
			if(this.getWordCount() > config.getMaxWordListLength() && this.wordLists.size() > config.getMaxSynsetDepth()){
				throw new NoMorGenerationPossibleException();
			}
			Set<Word> result = new HashSet<Word>();
			Set<Synset> canidates = new HashSet<Synset>();
			canidates.addAll(UbyAnalyser.getInstance().generateSynsetsFromWords(wordLists.get(wordLists.size()-1),gen.getUby()));
			canidates.removeAll(visitedSynsets);
			visitedSynsets.addAll(canidates);
			result.addAll(UbyAnalyser.getInstance().retrieveWordsFromSynsets(canidates));
			
			for(Set<Word> wSet : wordLists){
				result.removeAll(wSet);
			}
			if(useEmotion){
				wordLists.add(gen.getCustomDb().getEmotionBatch(result));
			}else{
				wordLists.add(result);
			}
			return result;
		}
		
		private int getWordCount() {
			int result = 0;
			for(Set<Word> set : this.wordLists){
				result += set.size();
			}
			return result;
		}

		public Set<Word> getMore(int synsetDepth) throws DbException, NoMorGenerationPossibleException{
			if(wordLists.size() <= synsetDepth){
				return generateMore();
			}else{
				return wordLists.get(synsetDepth);
			}
		}

		

		public boolean isUseEmotion() {
			return useEmotion;
		}
		
		public Set<Word> getInitialSet(){
			return wordLists.get(0);
		}
}
