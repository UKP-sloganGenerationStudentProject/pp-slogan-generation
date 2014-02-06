package de.koch.uim_project.gui;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class StylisticDeviceWeightPanel
    extends JPanel
{

    private JLabel label = new JLabel("Stylistic Device Weights");
    private LabeledInputPanel sdNone = new LabeledInputPanel("None", false);
    private LabeledInputPanel sdAlliteration = new LabeledInputPanel("Alliteration", false);
    private LabeledInputPanel sdParallelism = new LabeledInputPanel("Parallelism", false);
    private LabeledInputPanel sdOxymoron = new LabeledInputPanel("Oxymoron", false);
    private LabeledInputPanel sdMetaphor = new LabeledInputPanel("Metaphor", false);

    /**
     * Eclipse generated serial
     */
    private static final long serialVersionUID = -544526560973144358L;

    public StylisticDeviceWeightPanel()
    {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.add(this.label);
        this.add(Box.createHorizontalStrut(5));
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(Box.createHorizontalStrut(5));
        this.add(this.sdNone);
        this.add(Box.createHorizontalStrut(5));
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(Box.createHorizontalStrut(5));
        this.add(this.sdAlliteration);
        this.add(Box.createHorizontalStrut(5));
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(Box.createHorizontalStrut(5));
        this.add(this.sdParallelism);
        this.add(Box.createHorizontalStrut(5));
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(Box.createHorizontalStrut(5));
        this.add(this.sdOxymoron);
        this.add(Box.createHorizontalStrut(5));
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(Box.createHorizontalStrut(5));
        this.add(this.sdMetaphor);
    }

    public JLabel getLabel()
    {
        return this.label;
    }

    public LabeledInputPanel getSdNone()
    {
        return this.sdNone;
    }

    public LabeledInputPanel getSdAlliteration()
    {
        return this.sdAlliteration;
    }

    public LabeledInputPanel getSdParallelism()
    {
        return this.sdParallelism;
    }

    public LabeledInputPanel getSdOxymoron()
    {
        return this.sdOxymoron;
    }

    public LabeledInputPanel getSdMetaphor()
    {
        return this.sdMetaphor;
    }

}
