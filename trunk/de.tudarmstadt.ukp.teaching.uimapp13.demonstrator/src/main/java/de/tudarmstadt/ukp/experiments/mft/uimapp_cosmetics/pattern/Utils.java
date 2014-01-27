package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern;

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

    public static List<String> concatenate(List<String> array1, List<String> array2)
    {
        List<String> newValues = new ArrayList<String>();
        for(String val1 : array1)
        {
            for(String val2: array2)
            {
                newValues.add(val1 + " " + val2);
            }
        }

        return newValues;
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

    public static List<String> getSubList(List<String> list,List<Integer> inds)
    {
        ArrayList<String> subList = new ArrayList<String>();

        for(int ind : inds)
        {
            subList.add(list.listIterator(ind).next());
        }

        return subList;
    }

    public static List<String> getSubList(List<String> list,int nbrOfIndices)
    {
        return getSubList(list, getDistinctRandomIndices(list.size(), nbrOfIndices));
    }
}
