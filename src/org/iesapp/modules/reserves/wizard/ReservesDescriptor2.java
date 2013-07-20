/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves.wizard;

import org.iesapp.framework.util.CoreCfg;
import org.iesapp.framework.util.wizard.WizardPanelDescriptor;


public class ReservesDescriptor2 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP2";
    private final ReservesStep2 panel1;
    
    public ReservesDescriptor2(CoreCfg coreCfg) {
        panel1 = new ReservesStep2(coreCfg);
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
          return ReservesDescriptor1.IDENTIFIER;
    }  
    
    @Override
    public Object getNextPanelDescriptor() {
        
         return ReservesDescriptor3.IDENTIFIER;
    }  
}
