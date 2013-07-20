/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves.wizard;

import org.iesapp.framework.util.wizard.WizardPanelDescriptor;


public class ReservesDescriptor4 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP4";
    private final ReservesStep4 panel1;
    
    public ReservesDescriptor4() {
        panel1 = new ReservesStep4();
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel1);
    }
    
   
    @Override
    public void displayingPanel() {
       super.displayingPanel();      
       panel1.refreshUI();
       getWizard().setNextFinishButtonEnabled(true);
    }
    
    @Override
    public void aboutToHidePanel() {
        ReservesWiz.wizardMap.put("motiu", panel1.getMotiu());
    }   
 
    @Override
    public Object getBackPanelDescriptor() {
          return ReservesDescriptor3.IDENTIFIER;
    }  
    
    @Override
    public Object getNextPanelDescriptor() {
        
         return WizardPanelDescriptor.FINISH;
    }  
}
