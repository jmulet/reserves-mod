/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves.wizard;

import org.iesapp.framework.util.wizard.WizardPanelDescriptor;


public class ReservesDescriptor1 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP1";
    private final ReservesStep1 panel1;
    
    public ReservesDescriptor1() {
        panel1 = new ReservesStep1();
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel1);
        //getWizard().setNextFinishButtonEnabled(false);
    }
    
   
    @Override
    public void displayingPanel() {
       super.displayingPanel();      
       getWizard().setNextFinishButtonEnabled(panel1.isValidated());
    }
      
 
    @Override
    public Object getBackPanelDescriptor() {
          return  null;
    }  
    
    @Override
    public Object getNextPanelDescriptor() {
        
         return panel1.isValidated()?ReservesDescriptor2.IDENTIFIER:ReservesDescriptor1.IDENTIFIER;
    }  
}
