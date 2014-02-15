package de.tobiasloeser.slogangenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

public class ImportExport {

	/**
	 * This method exports a list of templates to a given path.
	 * 
	 * @param templates a list of templates
	 * @param target the path to store the templates
	 */
	public void exportTemplates(List<SloganTemplate> templates, String target)
	{
		OutputStream fos = null;
		try
		{
		  fos = new FileOutputStream(target);
		  ObjectOutputStream o = new ObjectOutputStream(fos);
		  o.writeObject(templates);
		  fos.close();
		}
		catch ( IOException e ) 
		{
			System.err.println( e ); 
		}
	}
	
	/**
	 * This method imports a list of templates from a given path. 
	 * 
	 * @param source the path where the list of templates is
	 * @return the imported list of templates
	 */
	@SuppressWarnings("unchecked")
	public List<SloganTemplate> importTemplates(String source)
	{
		List<SloganTemplate> templates = null;
		InputStream fis = null;
		try
		{
		  fis = new FileInputStream(source);
		  ObjectInputStream o = new ObjectInputStream(fis);
		  templates = (List<SloganTemplate>)o.readObject();
		  fis.close();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		return templates;
	}
	
	/**
	 * This method exports a list of emotions to a given path.
	 * 
	 * @param emotions a list of emotions
	 * @param target the path to store the emotions
	 */
	public void exportEmotions(List<Emotion> emotions, String target)
	{
		OutputStream fos = null;
		try
		{
		  fos = new FileOutputStream(target);
		  ObjectOutputStream o = new ObjectOutputStream(fos);
		  o.writeObject(emotions);
		  fos.close();
		}
		catch ( IOException e ) 
		{
			System.err.println( e ); 
		}	
	}
	
	/**
	 * This method imports a list of emotions from a given path. 
	 * 
	 * @param source the path where the list of emotions is
	 * @return the imported list of emotions
	 */
	@SuppressWarnings("unchecked")
	public List<Emotion> importEmotions(String source)
	{
		List<Emotion> emotions = null;
		InputStream fis = null;
		try
		{
		  fis = new FileInputStream(source);
		  ObjectInputStream o = new ObjectInputStream(fis);
		  emotions = (List<Emotion>)o.readObject();
		  fis.close();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		return emotions;	
	}

}
