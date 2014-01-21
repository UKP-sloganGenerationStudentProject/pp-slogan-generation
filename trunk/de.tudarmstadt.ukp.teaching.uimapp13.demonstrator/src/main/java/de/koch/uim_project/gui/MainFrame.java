package de.koch.uim_project.gui;


import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Constants;
import de.koch.uim_project.util.DbConfig;
import de.koch.uim_project.util.Emotion;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.koch.uim_project.database.DbException;

/**
 * This class represents the window, the program is running in Since their
 * should only be one main window this class implements the singleton-pattern
 * 
 * @author Frerik Koch
 * 
 */
public class MainFrame extends JFrame {

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = -8026416994513756565L;

	private static MainFrame instance = null;
	private WindowListener windowListener = null;
	private GridLayout mainLayout = new GridLayout(1, 2, 10, 0);
	private ConsolePanel consolePanel = new ConsolePanel();
	private ConfigPanel configPanel = new ConfigPanel();
	private FeaturePanel featurePanel = new FeaturePanel();
	private SloganPanel sloganPanel = new SloganPanel();
	private ControlPanel controlPanel = new ControlPanel();

	/**
	 * Constructor initializing the main window
	 */
	private MainFrame() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setWindowListener();
		this.setPreferredSize(new Dimension(1200, 800));
		this.setTitle("Game Advertising Slogan Generation");
		this.setLayout(mainLayout);
		createLayout();

		this.pack();
	}

	/**
	 * Singleton getter for this class
	 * 
	 * @return Instance of this {@link MainFrame}
	 */
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	/**
	 * Creates the underlying {@link GridBagLayout}, adds components and adds it
	 * to this frame
	 * 
	 */
	private void createLayout() {
		GridLayout slogansConsoleControl = new GridLayout(1, 2, 10, 0);
		GridLayout configFeatures = new GridLayout(2, 1, 0, 10);
		GridLayout consoleControl = new GridLayout(2, 1, 0, 10);

		JPanel slogansConsoleControlPanel = new JPanel(slogansConsoleControl);
		JPanel configFeaturesPanel = new JPanel(configFeatures);
		JPanel consoleControlPanel = new JPanel(consoleControl);

		consoleControlPanel.add(this.consolePanel);
		consoleControlPanel.add(this.controlPanel);

		slogansConsoleControlPanel.add(sloganPanel);
		slogansConsoleControlPanel.add(consoleControlPanel);

		configFeaturesPanel.add(configPanel);
		configFeaturesPanel.add(featurePanel);

		this.getContentPane().add(configFeaturesPanel);
		this.getContentPane().add(slogansConsoleControlPanel);

	}

	/**
	 * This method generates the appropriate {@link WindowListener} and adds it
	 * to this {@link JFrame}
	 */
	private void setWindowListener() {
		this.windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		this.addWindowListener(windowListener);

	}

	public void setDefaultValues() throws DbException {
		Config config = Config.getDefaultConfig();
		this.getConfigPanel().getBasicConfigPanel().getGameName().getTextField().setText(config.getGameName());
		this.getConfigPanel().getBasicConfigPanel().getEmotion().getEmotionCombo().setSelectedItem(config.getEmotion().toString());
		this.getConfigPanel().getBasicConfigPanel().getSloganCount().getTextField().setText(config.getSloganCount().toString());
		this.getConfigPanel().getBasicConfigPanel().getRandomSeed().getTextField().setText(config.getRandomSeed().toString());
		this.getConfigPanel().getBasicConfigPanel().getMinWordListForGeneration().getTextField().setText(""+config.getMinWordlistForGeneration());
		this.getConfigPanel().getBasicConfigPanel().getMaxSynsetDepth().getTextField().setText(""+config.getMaxSynsetDepth());

		this.getConfigPanel().getPatternWeightPanel().getPatternJJNN().getTextField()
				.setText(config.getPatternweights().get(Pattern.JJNN).toString());
		this.getConfigPanel().getPatternWeightPanel().getPatternJJNNJJNN().getTextField()
				.setText(config.getPatternweights().get(Pattern.JJNNJJNN).toString());
		this.getConfigPanel().getPatternWeightPanel().getPatternNCVC().getTextField()
				.setText(config.getPatternweights().get(Pattern.NNVVN).toString());
		this.getConfigPanel().getPatternWeightPanel().getPatternVBVBVB().getTextField()
				.setText(config.getPatternweights().get(Pattern.VBVBVB).toString());

		this.getConfigPanel().getStylisticDeviceWeightPanel().getSdAlliteration().getTextField()
				.setText(config.getSdweights().get(StylisticDevice.Alliteration).toString());
		this.getConfigPanel().getStylisticDeviceWeightPanel().getSdMetaphor().getTextField()
				.setText(config.getSdweights().get(StylisticDevice.Metapher).toString());
		this.getConfigPanel().getStylisticDeviceWeightPanel().getSdNone().getTextField()
				.setText(config.getSdweights().get(StylisticDevice.None).toString());
		this.getConfigPanel().getStylisticDeviceWeightPanel().getSdOxymeron().getTextField()
				.setText(config.getSdweights().get(StylisticDevice.Oxymeron).toString());
		this.getConfigPanel().getStylisticDeviceWeightPanel().getSdParallelism().getTextField()
				.setText(config.getSdweights().get(StylisticDevice.Parallelism).toString());

		this.getFeaturePanel().getFeatures().setList(config.getFeatureList());
		this.getFeaturePanel().getAlienFeatures().setList(config.getAlienFeatureList());

	}

	public Config getConfig() {
		Map<Pattern, Double> patternWeights = new HashMap<Pattern, Double>();
		patternWeights.put(Pattern.JJNN, Double.parseDouble(this.getConfigPanel().getPatternWeightPanel().getPatternJJNN().getTextField().getText()));
		patternWeights.put(Pattern.JJNNJJNN,
				Double.parseDouble(this.getConfigPanel().getPatternWeightPanel().getPatternJJNNJJNN().getTextField().getText()));
		patternWeights.put(Pattern.NNVVN, Double.parseDouble(this.getConfigPanel().getPatternWeightPanel().getPatternNCVC().getTextField().getText()));
		patternWeights.put(Pattern.VBVBVB,
				Double.parseDouble(this.getConfigPanel().getPatternWeightPanel().getPatternVBVBVB().getTextField().getText()));

		Map<StylisticDevice, Double> sdWeights = new HashMap<StylisticDevice, Double>();
		sdWeights.put(StylisticDevice.Alliteration,
				Double.parseDouble(this.getConfigPanel().getStylisticDeviceWeightPanel().getSdAlliteration().getTextField().getText()));
		sdWeights.put(StylisticDevice.Metapher,
				Double.parseDouble(this.getConfigPanel().getStylisticDeviceWeightPanel().getSdMetaphor().getTextField().getText()));
		sdWeights.put(StylisticDevice.Oxymeron,
				Double.parseDouble(this.getConfigPanel().getStylisticDeviceWeightPanel().getSdOxymeron().getTextField().getText()));
		sdWeights.put(StylisticDevice.Parallelism,
				Double.parseDouble(this.getConfigPanel().getStylisticDeviceWeightPanel().getSdParallelism().getTextField().getText()));
		sdWeights.put(StylisticDevice.None,
				Double.parseDouble(this.getConfigPanel().getStylisticDeviceWeightPanel().getSdNone().getTextField().getText()));

		Set<String> featureList = this.getFeaturePanel().getFeatures().getList();
		Set<String> alienFeatureList = this.getFeaturePanel().getAlienFeatures().getList();

		String gameName = this.getConfigPanel().getBasicConfigPanel().getGameName().getTextField().getText();
		Integer sloganCount = Integer.parseInt(this.getConfigPanel().getBasicConfigPanel().getSloganCount().getTextField().getText());
		Emotion emotion = Emotion.valueOf((String) this.getConfigPanel().getBasicConfigPanel().getEmotion().getEmotionCombo().getSelectedItem());
		Long randomSeed = Long.parseLong(this.getConfigPanel().getBasicConfigPanel().getRandomSeed().getTextField().getText());
		
		DbConfig customDb = new DbConfig(Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_URL, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_USER, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_PASS);
		DbConfig ubyConfig = new DbConfig(Constants.DATABASE.UBY.DEFAULT_UBY_URL, Constants.DATABASE.UBY.DEFAULT_UBY_LOGIN, Constants.DATABASE.UBY.DEFAULT_UBY_PASS);
		
		int minWordlistForGeneration = Integer.parseInt(this.getConfigPanel().getBasicConfigPanel().getMinWordListForGeneration().getTextField().getText());
		int maxSynsetDepth = Integer.parseInt(this.getConfigPanel().getBasicConfigPanel().getMaxSynsetDepth().getTextField().getText());
		
		return new Config(gameName, randomSeed, sloganCount, emotion, patternWeights, sdWeights, featureList, alienFeatureList,minWordlistForGeneration,maxSynsetDepth,ubyConfig,customDb);
	}

	public ConsolePanel getConsolePanel() {
		return consolePanel;
	}

	public ConfigPanel getConfigPanel() {
		return configPanel;
	}

	public FeaturePanel getFeaturePanel() {
		return featurePanel;
	}

	public SloganPanel getSloganPanel() {
		return sloganPanel;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

}
