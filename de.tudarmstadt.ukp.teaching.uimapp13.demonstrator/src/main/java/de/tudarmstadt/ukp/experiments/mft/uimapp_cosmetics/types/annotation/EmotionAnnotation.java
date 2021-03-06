

/* First created by JCasGen Mon Dec 16 23:14:59 CET 2013 */
package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jan 04 17:57:44 CET 2014
 * XML source: /media/Storage/Programming/eclipseWorkspace/de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics/src/main/resources/de/tudarmstadt/ukp/experiments/mft/uimapp_cosmetics/types/typeSystemDescriptor.xml
 * @generated */
public class EmotionAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(EmotionAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected EmotionAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public EmotionAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public EmotionAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public EmotionAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: valence

  /** getter for valence - gets 
   * @generated */
  public String getValence() {
    if (EmotionAnnotation_Type.featOkTst && ((EmotionAnnotation_Type)jcasType).casFeat_valence == null)
      jcasType.jcas.throwFeatMissing("valence", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((EmotionAnnotation_Type)jcasType).casFeatCode_valence);}
    
  /** setter for valence - sets  
   * @generated */
  public void setValence(String v) {
    if (EmotionAnnotation_Type.featOkTst && ((EmotionAnnotation_Type)jcasType).casFeat_valence == null)
      jcasType.jcas.throwFeatMissing("valence", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((EmotionAnnotation_Type)jcasType).casFeatCode_valence, v);}    
  }

    