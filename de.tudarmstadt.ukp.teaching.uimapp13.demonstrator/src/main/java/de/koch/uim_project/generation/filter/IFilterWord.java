package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

/**
 * Implementing classes accept or reject a single word
 * @author Frerik Koch
 *
 */
public interface IFilterWord {

	/**
	 * Decides if the given word is accepted(true) or rejected(false)
	 * ATTENTION: Implementing classes must not alter the given {@link Word}
	 * @param word {@link Word} to test
	 * @return <code>false</code>: {@link Word} rejected, <code>true</code>: {@link Word} accepted
	 */
	public boolean filterWord(final Word word);
}
