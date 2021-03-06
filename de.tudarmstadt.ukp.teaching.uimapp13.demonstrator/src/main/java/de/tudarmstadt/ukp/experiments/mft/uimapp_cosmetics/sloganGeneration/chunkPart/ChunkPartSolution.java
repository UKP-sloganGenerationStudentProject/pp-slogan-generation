package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.constraints.WordConstraint;


/**
*
* A ChunkPartSolution is an intermediary object generated by a ChunkPart during the slogan generation.
* It defines a solution for the ChunkPart, but still needs to be converted into texts.
*
* It contains a link to its {@link ChunkPart} model, the ids of the {@link WordConstraint} items that
* it complies with, the lemma that will be derived , and a boolean that says
* if it contains a body part.
*
 * @author Matthieu Fraissinet-Tachet
*/


public class ChunkPartSolution
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 7060731268874928166L;
    private final ChunkPart _model;
    private List<Integer> _constraintIds;
    private final String _lemmaSolution;
    private boolean _hasBodyPart;

    public ChunkPartSolution(ChunkPart model,List<Integer> constraintIds, String solutionLemma)
    {
        _model = model;
        _lemmaSolution = solutionLemma;
        _constraintIds = constraintIds;
        _hasBodyPart = false;
        if(_constraintIds == null)
        {
            _constraintIds = new ArrayList<>();
        }
    }

    public void setHasBodyPart(boolean tof)
    {
        _hasBodyPart = tof;
    }
    public boolean hasBodyPart()
    {
        return _hasBodyPart;
    }

    public ChunkPart getModel()
    {
        return this._model;
    }

    public String getLemma()
    {
        return this._lemmaSolution;
    }

    public List<Integer> getConstraintIds()
    {
        return _constraintIds;
    }

    public boolean addConstraintId(int id)
    {
        boolean output = false;
        if(!_constraintIds.contains(id))
        {
            output = true;
            _constraintIds.add(id);
        }
        return output;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof ChunkPartSolution))
        {
            return false;
        }

        ChunkPartSolution cps = (ChunkPartSolution) obj;

        if(!(this._lemmaSolution.equals(cps.getLemma()) && this._model==cps.getModel()))
        {
            return false;
        }

        boolean areSameConstraintSizes = this._constraintIds.size() == cps.getConstraintIds().size();
        if(areSameConstraintSizes)
        {
            List<Integer> idsCopy1 = new ArrayList<>(this._constraintIds);
            List<Integer> idsCopy2 = new ArrayList<>(cps.getConstraintIds());
            Collections.sort(idsCopy1);
            Collections.sort(idsCopy2);

            return idsCopy1.equals(idsCopy2);
        }

        return true;

    }

    public String generateText()
    {
        return _model.deriveFromLemmaForm(_lemmaSolution);
    }

    @Override
    public String toString()
    {
        return generateText();
    }


}