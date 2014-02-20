package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestUtils
{


    @Test
    public void testGetDistinctRandomIndices()
    {

        List<Integer> inds = Utils.getDistinctRandomIndices(10, 6);

        boolean correctSize = inds.size()==6;
        boolean allIndicesDiff = true;
        int biggest = -1;
        int lowest = 10;
        List<Integer> otherInds = new ArrayList<>();

        for(int ind : inds)
        {
            if(ind>biggest)
            {
                biggest = ind;
            }


            if(ind<lowest)
            {
                lowest = ind;
            }

            if(!otherInds.contains(ind))
            {
                otherInds.add(ind);
            }
            else
            {
                allIndicesDiff = false;
            }

        }

        assertTrue("Check the number of generated indices",correctSize);
        assertTrue("Check if all indices are different",allIndicesDiff);
        assertTrue("check if the generated indices are correct for the given size array",lowest>=0&&biggest<10);

    }

    /*
     * get the sublist of a list corresponding to the indices
     */
    @Test
    public void testGetSubListWithIndicesAsInput()
    {

        List<Integer> values = Arrays.asList(0,1,2,3,4);
        List<Integer> indices = Arrays.asList(1,3);

        List<Integer> sublist = Utils.getSubList(values, indices);

        assertTrue(indices.equals(sublist));
    }

    /*
     * get a random sublist of given size out of a list
     */
    public void testGetRandomSubList()
    {
        List<Integer> values = Arrays.asList(0,1,2,3,4,5);
        List<Integer> randomSubList = Utils.getSubList(values, 3);

        boolean correctSize = randomSubList.size()==3;
        boolean allValuesDiff = true;
        boolean allValuesFromValues = values.containsAll(randomSubList);
        List<Integer> otherInds = new ArrayList<>();

        for(int ind : randomSubList)
        {

            if(!otherInds.contains(ind))
            {
                otherInds.add(ind);
            }
            else
            {
                allValuesDiff = false;
            }

        }

        assertTrue("Check the number of extracted objects",correctSize);
        assertTrue("Check that all the extracted objects come from diffehrent indices",allValuesDiff);
        assertTrue("check that all the extracted objects come from the good list",allValuesFromValues);
    }
}
