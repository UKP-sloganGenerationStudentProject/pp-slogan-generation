package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index;

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

/*
 *
 * This class modelizes an index of element of  type IndexElement.
 * By adding a new element, checks if it already contains an element with the same id.
 * If there is such an element, it returns it.
 *
 */

public class Index<Element extends IndexElement>
    implements Serializable
{
    private static final long serialVersionUID = 6772044159525718129L;



    Hashtable<String, Element> _elementsIndex;

    public Index()
    {
        this._elementsIndex = new Hashtable<String, Element>();
    }

    public Element addElement(final Element generic)
    {

        Element output;

        final String patternElementId = generic.getId();
        final Element assocGeneric = this._elementsIndex.get(patternElementId);
        if (assocGeneric == null) {
            output = generic;
            this._elementsIndex.put(patternElementId, generic);
        }
        else {
            output = assocGeneric;
        }

        return output;

    }

    public Element get(final String id)
    {
        return this._elementsIndex.get(id);
    }

    public Set<String> getIdSet()
    {
        return this._elementsIndex.keySet();
    }

    public Collection<Element> getElements()
    {
        return this._elementsIndex.values();
    }

    @Override
    public String toString()
    {
        final Hashtable<String, StringBuilder> partialOutput = new Hashtable<String, StringBuilder>();

        for (final Element element : this._elementsIndex.values()) {
            StringBuilder stb = partialOutput.get(element.getTypeAsString());
            if (stb == null) {
                stb = new StringBuilder();
                partialOutput.put(element.getTypeAsString(), stb);
            }
            stb.append(element.toString());
            stb.append("\n");
        }

        final StringBuilder output = new StringBuilder();

        for (final String key : partialOutput.keySet()) {
            output.append("\n");
            output.append(partialOutput.get(key).toString());
        }

        return output.toString();
    }

}