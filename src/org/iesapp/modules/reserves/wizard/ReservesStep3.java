/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves.wizard;

import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.iesapp.clients.iesdigital.reserves.BeanSelectHores;
import org.iesapp.framework.table.MyCheckBoxRenderer;
import org.iesapp.framework.util.CoreCfg;

/**
 *
 * @author Josep
 */
public class ReservesStep3 extends javax.swing.JPanel {
    private DefaultTableModel modelTable1;
    private ArrayList<BeanSelectHores> listDisponibilitat;
    private final CoreCfg coreCfg;


    /**
     * Creates new form StartupStep1
     */
    public ReservesStep3(CoreCfg coreCfg) {
        this.coreCfg = coreCfg;
        initComponents();
        ButtonGroup group1 = new ButtonGroup();
        group1.add(jRadioButton1);
        group1.add(jRadioButton2);
        jTable1.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                ReservesWiz.getWizard().setNextFinishButtonEnabled(isValidated());
                ArrayList<Integer> selectedHoras = new ArrayList<Integer>();
                ArrayList<String> selectedHorasNom = new ArrayList<String>();
                for(int i=0; i<jTable1.getRowCount(); i++)
                {
                    if(((Boolean) jTable1.getValueAt(i, 0)))
                    {
                        selectedHoras.add(listDisponibilitat.get(i).getIdHora());
                        selectedHorasNom.add(listDisponibilitat.get(i).getHora());
                    }
                }
                ReservesWiz.wizardMap.put("selectedHoras", selectedHoras);
                ReservesWiz.wizardMap.put("selectedHorasNom", selectedHorasNom);
            }
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable()
        {
            public boolean isCellEditable(int row, int col)
            {
                return listDisponibilitat.get(row).isDisponible();
            }
        }
        ;

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Matí");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Tarda");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        modelTable1 = new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Select", "Hora", "Status"
            }
        );
        jTable1.setModel(modelTable1);
        JCheckBox checkbox = new JCheckBox();
        jTable1.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor( checkbox ));
        jTable1.getColumnModel().getColumn(0).setCellRenderer(new MyCheckBoxRenderer());
        jTable1.getColumnModel().getColumn(1).setCellRenderer(new HightLighter());
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new HightLighter());
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
        jTable1.setRowHeight(32);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton2)
                        .addGap(0, 313, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
       fillTable();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
       fillTable();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    
    public void refreshUI() {
       fillTable();
    }

    public boolean isValidated() {
        
         int numSelected = 0;
         for(int i=0; i<jTable1.getRowCount(); i++)
         {
             if( (Boolean) jTable1.getValueAt(i, 0))
             {
                 numSelected += 1;
             }
         }
         return numSelected>0;
    }

    private void fillTable() {
        while(jTable1.getRowCount()>0) {
            modelTable1.removeRow(0);
        }
        
        java.util.Date date = (java.util.Date) ReservesWiz.wizardMap.get("date");
        int type = Integer.parseInt( (String) ReservesWiz.wizardMap.get("type") );
        String idRecurs = (String) ReservesWiz.wizardMap.get("idRecurs");
        
        //Obté una llista amb les hores disponibles
        //public static ArrayList<BeanSelectHores> listDisponibilitat(MyDatabase mysql, java.util.Date dia, String torn, String tipusRecurs, String idRecurs) {
        String torn="1";
        if(jRadioButton2.isSelected())
        {
            torn="2";
        }
        listDisponibilitat = coreCfg.getIesClient().getReservesClient().getReservesCollection().listDisponibilitat(date, torn, type, idRecurs);
        for(BeanSelectHores bean: listDisponibilitat)
        {
            modelTable1.addRow(new Object[]{bean.isTriat(), bean.getHora(), bean.getComment()});
        }
    }

 
}