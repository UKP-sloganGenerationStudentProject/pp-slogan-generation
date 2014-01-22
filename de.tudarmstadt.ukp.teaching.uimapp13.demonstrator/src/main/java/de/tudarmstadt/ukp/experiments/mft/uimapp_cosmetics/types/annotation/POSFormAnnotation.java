

/* First created by JCasGen Sat Jan 04 17:57:44 CET 2014 */
package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jan 04 17:57:44 CET 2014
 * XML source: /media/Storage/Programming/eclipseWorkspace/de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics/src/main/resources/de/tudarmstadt/ukp/experiments/mft/uimapp_cosmetics/types/typeSystemDescriptor.xml
 * @generated */
public class POSFormAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(POSFormAnnotation.class);
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
  protected POSFormAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public POSFormAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public POSFormAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public POSFormAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: form

  /** getter for form - gets 
   * @generated */
  public String getForm() {
    if (POSFormAnnotation_Type.featOkTst && ((POSFormAnnotation_Type)jcasType).casFeat_form == null)
      jcasType.jcas.throwFeatMissing("form", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.POSFormAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((POSFormAnnotation_Type)jcasType).casFeatCode_form);}
    
  /** setter for form - sets  
   * @generated */
  public void setForm(String v) {
    if (POSFormAnnotation_Type.featOkTst && ((POSFormAnnotation_Type)jcasType).casFeat_form == null)
      jcasType.jcas.throwFeatMissing("form", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.POSFormAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((POSFormAnnotation_Type)jcasType).casFeatCode_form, v);}    
  }

    