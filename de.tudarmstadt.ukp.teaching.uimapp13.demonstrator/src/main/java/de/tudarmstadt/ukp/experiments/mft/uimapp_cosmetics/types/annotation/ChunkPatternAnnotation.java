

/* First created by JCasGen Tue Dec 10 09:14:27 CET 2013 */
package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jan 04 17:57:44 CET 2014
 * XML source: /media/Storage/Programming/eclipseWorkspace/de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics/src/main/resources/de/tudarmstadt/ukp/experiments/mft/uimapp_cosmetics/types/typeSystemDescriptor.xml
 * @generated */
public class ChunkPatternAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(ChunkPatternAnnotation.class);
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
  protected ChunkPatternAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public ChunkPatternAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public ChunkPatternAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public ChunkPatternAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: pattern

  /** getter for pattern - gets 
   * @generated */
  public String getPattern() {
    if (ChunkPatternAnnotation_Type.featOkTst && ((ChunkPatternAnnotation_Type)jcasType).casFeat_pattern == null)
      jcasType.jcas.throwFeatMissing("pattern", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.ChunkPatternAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ChunkPatternAnnotation_Type)jcasType).casFeatCode_pattern);}
    
  /** setter for pattern - sets  
   * @generated */
  public void setPattern(String v) {
    if (ChunkPatternAnnotation_Type.featOkTst && ((ChunkPatternAnnotation_Type)jcasType).casFeat_pattern == null)
      jcasType.jcas.throwFeatMissing("pattern", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.ChunkPatternAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((ChunkPatternAnnotation_Type)jcasType).casFeatCode_pattern, v);}    
  }

    