/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves.wizard;

import org.iesapp.framework.util.CoreCfg;
import org.iesapp.framework.util.wizard.WizardPanelDescriptor;


public class ReservesDescriptor3 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP3";
    private final ReservesStep3 panel1;
    
    public ReservesDescriptor3(CoreCfg coreCfg) {
        panel1 = new ReservesStep3(coreCfg);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel1);
    }
    
   
    @Override
    public void displayingPanel() {
       super.displayingPanel();      
       panel1.refreshUI();
       getWizard().setNextFinishButtonEnabled(panel1.isValidated());
    }
    
    @Override
    public void aboutToHidePanel() {
        
    }   
 
    @Override
    public Object getBackPanelDescriptor() {
          return ReservesDescriptor2.IDENTIFIER;
    }  
    
    @Override
    public Object getNextPanelDescriptor() {
        
         return ReservesDescriptor4.IDENTIFIER;
    }  
}
