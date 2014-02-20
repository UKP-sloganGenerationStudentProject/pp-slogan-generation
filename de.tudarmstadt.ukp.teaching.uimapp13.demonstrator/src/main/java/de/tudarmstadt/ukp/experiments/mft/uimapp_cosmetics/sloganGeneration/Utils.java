package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class includes some methods usefull during the slogan generation
 * @author Matthieu Fraissinet-Tachet
 *
 */
public class Utils
{

    /**
     * create a list of a given number of random indices given an array size
     * @param arraySize
     * @param nbrOfIndices
     * @return
     */
    public static List<Integer> getDistinctRandomIndices(int arraySize, int nbrOfIndices)
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arraySize; i++) {
            list.add(i);
        }
        Collections.shuffle(list);

        if(arraySize<nbrOfIndices)
        {
            return list;
        }

        return list.subList(0, nbrOfIndices);
    }

    /**
     * get the list of the elements from a list, which positions are defined by inds
     * @param list : from which to extract elements
     * @param inds : position of the elements to be extracted
     * @return
     */
    public static <T> List<T> getSubList(List<T> list,List<Integer> inds)
    {
        ArrayList<T> subList = new ArrayList<T>();

        for(int ind : inds)
        {
            subList.add(list.listIterator(ind).next());
        }

        return subList;
    }

    /**
     *
     * get a random sublist of given size
     *
     * @param list
     * @param nbrOfIndices
     * @return
     */

    public static  <T> List<T> getSubList(List<T> list,int nbrOfIndices)
    {
        return getSubList(list, getDistinctRandomIndices(list.size(), nbrOfIndices));
    }
}
