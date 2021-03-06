package de.koch.uim_project.main;

import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.gui.MainFrame;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.ConfigValidator;
import de.koch.uim_project.util.Constants;
import de.koch.uim_project.analyse.AnalyseException;
import de.koch.uim_project.analyse.fileoutput.FileWriterUimException;
import de.koch.uim_project.database.DbException;

/**
 * This class is the entrypoint of this application. Additionally some global
 * values are stored
 * 
 * @author Frerik Koch
 * 
 */
public class Main {

	
	private static MainFrame mainWindow = null;
	private static Logger log = Logger.getRootLogger();

	public static void generateSlogans() {
		mainWindow.getControlPanel().setButtonsEnabled(false);
		Config config = mainWindow.getConfig();
		if (ConfigValidator.getInstance().validate(config)) {
			try {
				Generator gen = Generator.getInstance(new File(Constants.FILESYSTEM.WEB1T_DEFAULT_PATH),config);
				for (int i = 0; i < config.getSloganCount(); i++) {
					try {
						String slogan = gen.generateSlogan();
						Main.writeToSlogans(slogan);
					} catch (SloganNotCreatedException e) {
						log.error("A slogan was not created", e);
						Main.writeToConsole("Failed to create one slogan");
					}
				}
			} catch (DbException e) {
				log.error("DBException caught.", e);
				Main.writeToConsole("Database exception! Please check your DB configuration or try again");
			}
		}
		mainWindow.getControlPanel().setButtonsEnabled(true);
	}

	public static synchronized void  clearSlogans() {
		if(mainWindow != null){
			mainWindow.getSloganPanel().getListmodel().removeAllElements();
		}
	}

	public static synchronized void writeToSlogans(String slogan) {
		if(mainWindow != null){
		mainWindow.getSloganPanel().getListmodel().addElement(slogan);
		}
	}

	public static synchronized void clearConsole() {
		if(mainWindow != null){
		mainWindow.getConsolePanel().getListmodel().removeAllElements();
		}
	}

	public static synchronized void writeToConsole(String toWrite) {
		if(mainWindow != null){
		mainWindow.getConsolePanel().getListmodel().addElement(toWrite);
		}
	}

	public static void main(String[] args) throws DbException, UIMAException, IOException, AnalyseException, FileWriterUimException {
		mainWindow = MainFrame.getInstance();
		mainWindow.setDefaultValues();
		mainWindow.setVisible(true);
	}

}
