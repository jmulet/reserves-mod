/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.help.CSH;
import org.iesapp.framework.util.CoreCfg;
import org.iesapp.framework.util.wizard.Wizard;
import org.iesapp.framework.util.wizard.WizardPanelDescriptor;

/**
 *
 * @author Josep
 */
public class ReservesWiz {
    private static Wizard wizard;
    public static ResourceBundle bundle;
    public static HashMap<String, Object> wizardMap;
    private final CoreCfg coreCfg;
     
    public ReservesWiz(CoreCfg coreCfg)       
    {
        this.coreCfg = coreCfg;
        wizardMap = new HashMap<String, Object>();
        bundle = ResourceBundle.getBundle("org/iesapp/modules/reserves/wizard/Bundle");
        wizard = new org.iesapp.framework.util.wizard.Wizard();
        wizard.getDialog().setTitle(bundle.getString("nova"));
        
        WizardPanelDescriptor descriptor1 = new ReservesDescriptor1();
        wizard.registerWizardPanel(ReservesDescriptor1.IDENTIFIER, "1. Material o aula", descriptor1);

        WizardPanelDescriptor descriptor2 = new ReservesDescriptor2(coreCfg);
        wizard.registerWizardPanel(ReservesDescriptor2.IDENTIFIER, "2. Dia reserva", descriptor2);
        
        WizardPanelDescriptor descriptor3 = new ReservesDescriptor3(coreCfg);
        wizard.registerWizardPanel(ReservesDescriptor3.IDENTIFIER, "3. Hora/es", descriptor3);

        WizardPanelDescriptor descriptor4 = new ReservesDescriptor4();
        wizard.registerWizardPanel(ReservesDescriptor4.IDENTIFIER, "4. Confirmació", descriptor4);
        
        wizard.setCurrentPanel(ReservesDescriptor1.IDENTIFIER);
 
    }
    
    public static org.iesapp.framework.util.wizard.Wizard getWizard()
    {
        return wizard;
    }

    public int showModalDialog() {
        coreCfg.getMainHelpBroker().enableHelpKey(wizard.getDialog().getRootPane(), 
                "org-iesapp-modules-reserves-nova", null);
        
        int returnCode = wizard.showModalDialog();
        if(returnCode==Wizard.FINISH_RETURN_CODE)
        {
            java.util.Date date = (java.util.Date) ReservesWiz.wizardMap.get("date");
            String type = (String) ReservesWiz.wizardMap.get("type");
            String idRecurs = (String) ReservesWiz.wizardMap.get("idRecurs");
            ArrayList<Integer> listHores = (ArrayList<Integer>) ReservesWiz.wizardMap.get("selectedHoras");
            String motiu = (String) ReservesWiz.wizardMap.get("motiu");
            coreCfg.getIesClient().getReservesClient().getReservesCollection().makeReserva(coreCfg.getUserInfo().getAbrev(), type, idRecurs, date, listHores, motiu);
            }
        return returnCode;
    }
}
