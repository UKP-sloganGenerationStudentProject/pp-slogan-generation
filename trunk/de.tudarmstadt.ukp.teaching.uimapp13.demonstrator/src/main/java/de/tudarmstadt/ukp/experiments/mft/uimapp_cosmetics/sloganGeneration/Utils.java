package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils
{
    public static void main(String[] args) throws Exception
    {
        List<String> values = Arrays.asList("bonjour","je","m'appelle","matthieu");

        System.out.println("Indices");
        for(int ind : getDistinctRandomIndices(values.size(), 2))
        {
            System.out.println(ind);
        }

        System.out.println("Values");
        for(String value : getSubList(values, 2))
        {
            System.out.println(value);
        }

    }



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

    public static <T> List<T> getSubList(List<T> list,List<Integer> inds)
    {
        ArrayList<T> subList = new ArrayList<T>();

        for(int ind : inds)
        {
            subList.add(list.listIterator(ind).next());
        }

        return subList;
    }

    public static  <T> List<T> getSubList(List<T> list,int nbrOfIndices)
    {
        return getSubList(list, getDistinctRandomIndices(list.size(), nbrOfIndices));
    }
}
