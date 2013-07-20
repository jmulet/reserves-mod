/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves.wizard;

import java.util.Calendar;
import java.util.Date;
import org.iesapp.clients.iesdigital.dates.DatesControl;
import org.iesapp.framework.data.User;
import org.iesapp.framework.util.CoreCfg;

/**
 *
 * @author Josep
 */
public class ReservesStep2 extends javax.swing.JPanel {
    private final CoreCfg coreCfg;


    /**
     * Creates new form StartupStep1
     */
    public ReservesStep2(CoreCfg coreCfg) {
        this.coreCfg = coreCfg;
        initComponents();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        java.util.Date min = cal.getTime();
        
        java.util.Date max = null;
        if(coreCfg.getUserInfo().getGrant()!=User.ADMIN && coreCfg.getUserInfo().getGrant()!=User.PREF)
        {
            int maxdies = (Integer) CoreCfg.configTableMap.get("maxDiesAntelacio");
            jLabel1.setText("Es pot reservar amb una antelació màxima de "+maxdies+" dies.");        
            cal.add(Calendar.DAY_OF_MONTH, maxdies);
            max = cal.getTime();
        }
        
        jCalendar1.getDayChooser().addDateEvaluator(new org.iesapp.framework.util.FestiusDateEvaluator(min,max,coreCfg));
        jCalendar1.setDate(new java.util.Date());
        jCalendar1.getDayChooser().revalidate();
        jCalendar1.getDayChooser().repaint();
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCalendar1 = new com.toedter.calendar.JCalendar();
        jLabel1 = new javax.swing.JLabel();

        jCalendar1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCalendar1PropertyChange(evt);
            }
        });

        jLabel1.setText("* ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCalendar1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCalendar1PropertyChange
        Date date = jCalendar1.getDate();
        if(!new DatesControl(date,coreCfg.getIesClient()).esFestiu())
        {
            ReservesWiz.getWizard().setNextFinishButtonEnabled(true);
            ReservesWiz.wizardMap.put("date", date);
        }
    }//GEN-LAST:event_jCalendar1PropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    
    public void refreshUI() {
       //
    }

    public boolean isValidated() {
         Date date = jCalendar1.getDate();
         return(!new DatesControl(date,coreCfg.getIesClient()).esFestiu());
    }

 
}
