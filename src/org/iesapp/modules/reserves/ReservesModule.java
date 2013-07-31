/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves;

import com.l2fprod.common.swing.StatusBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javax.help.CSH;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.iesapp.clients.iesdigital.professorat.BeanProfessor;
import org.iesapp.clients.iesdigital.reserves.BeanMaterial;
import org.iesapp.clients.iesdigital.reserves.BeanReserves;
import org.iesapp.clients.iesdigital.spaces.BeanEspai;
import org.iesapp.framework.data.User;
import org.iesapp.framework.table.CellDateRenderer;
import org.iesapp.framework.table.CellTableState;
import org.iesapp.framework.table.MyIconLabelRenderer;
import org.iesapp.framework.table.TextAreaRenderer;
import org.iesapp.framework.util.IconUtils;
import org.iesapp.framework.util.JarClassLoader;
import org.iesapp.framework.util.wizard.Wizard;
import org.iesapp.modules.reserves.util.Cfg;
import org.iesapp.modules.reserves.util.MySorter;
import org.iesapp.modules.reserves.wizard.ReservesWiz;
import org.iesapp.util.StringUtils;
import org.openide.util.Lookup;


/**
 *
 * @author Josep
 */
public class ReservesModule extends org.iesapp.framework.pluggable.TopModuleWindow {
    
    private boolean listening;
    private DefaultTableModel modelTable0;
    private Date avui;
    private String[] sortableFields;
    private MySorter msort;
    private String[] horesCentre;
    private DefaultComboBoxModel modelCombo1;
//    private static  HashMap<String,String> abrev2prof;
//    private static  HashMap<String,String> abrev2depart;
//   public static HashMap<Integer, Integer> material2Id;
//    public static ArrayList<String> llistaMaterials;
//    public static ArrayList<String> llistaMaterialsUbicacio;
//    public static ArrayList<String> llistaMaterialsDescripcio;
//    public static ArrayList<ImageIcon> llistaMaterialsIcones;
//    public static ArrayList<String> llistaAules;
//    public static ArrayList<String> llistaAulesDescripcio;
    private final ResourceBundle bundle;
    public static ArrayList<BeanProfessor> listProfessors;
    public static ArrayList<BeanMaterial> listMaterials;
    public static ArrayList<BeanEspai> listEspais;
    private final Icon[] icons0;

    
    /**
     * Creates new form ModulLlistat
     */
    public ReservesModule() {
        bundle = java.util.ResourceBundle.getBundle("org/iesapp/modules/reserves/ReservesBundle"); // NOI18N
        this.moduleName = "reserves";
        this.moduleDisplayName = bundle.getString("reserves");
        this.moduleDescription = bundle.getString("descripcio");
        icons0 = new Icon[]{
            IconUtils.getBlankIcon(), IconUtils.getDeleteIcon()
        };
        
     }
    
    //All code after constructor (pointers are initialized here)
    @Override
    public void postInitialize() {

        initComponents();
        jTable0.setIntercellSpacing( new java.awt.Dimension(2,2) );
        jTable0.setGridColor(java.awt.Color.gray);
        jTable0.setShowGrid(true);
  
        ActionListener novaAction = new ActionListener()
        {
           public void actionPerformed(ActionEvent e) {
                ReservesWiz wiz = new ReservesWiz(coreCfg);
                int showModalDialog = wiz.showModalDialog();
                if(showModalDialog==Wizard.FINISH_RETURN_CODE)
                {
                    fillTable();                }
            }
        };
        jMenuNovaReserva.addActionListener(novaAction);
        jButton1.addActionListener(novaAction);
        associateLookup(this);
        
//        abrev2prof = new HashMap<String,String>();
//        abrev2depart = new HashMap<String,String>();
//       material2Id = new HashMap<Integer,Integer>();

        Cfg.initializeModule();
        
        modelCombo1 = new DefaultComboBoxModel();
        modelCombo1.addElement(bundle.getString("All")+" [*]");
        modelCombo1.addElement("ADMINISTRADOR [ADMIN]");
       // modelCombo1.addElement("PREFECTURA [PREF]");
        modelCombo1.addElement("GUARDIES [GUARD]");
        jComboBox1.setModel(modelCombo1);

        CSH.setHelpIDString(jButton1,"org-iesapp-modules-reserves-nova");
        
        //////consulta de tot el professorat del centre
        listProfessors = coreCfg.getIesClient().getProfessoratData().listProfessors("");
        for (BeanProfessor bp : listProfessors) {
            modelCombo1.addElement(bp.getNombre() + " [" + bp.getAbrev() + "]");
        }
 
        jComboBox1.setModel(modelCombo1);
        avui = coreCfg.getMysql().getServerDate(); //contains date and time

        sortableFields = new String[]{"id", "abrev", "tipus", "id_recurs", "data", "hora", "motiu"};
        msort = new MySorter(sortableFields);
        msort.setFirst("usuari");
        msort.setFirst("hora");
        msort.setFirst("data");

        JTableHeader header = jTable0.getTableHeader();
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTable table = ((JTableHeader) evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();

                int index = colModel.getColumnIndexAtX(evt.getX());

                msort.setFirst(sortableFields[index]);
                fillTable();

                java.awt.Rectangle headerRect = table.getTableHeader().getHeaderRect(index);
                if (index == 0) {
                    headerRect.width -= 10;
                } else {
                    headerRect.grow(-10, 0);
                }
                if (!headerRect.contains(evt.getX(), evt.getY())) {
                    int vLeftColIndex = index;
                    if (evt.getX() < headerRect.x) {
                        vLeftColIndex--;
                    }
                }
            }
        });

        //////consulta les hores del centre
        
        Time[] horesClase = coreCfg.getIesClient().getDatesCollection().getHoresClase();
        Time[] horesClase_fi = coreCfg.getIesClient().getDatesCollection().getHoresClase_fi();
        horesCentre = new String[]{StringUtils.formatTime(horesClase[0]) + "-" + StringUtils.formatTime(horesClase_fi[0]),
            StringUtils.formatTime(horesClase[1]) + "-" + StringUtils.formatTime(horesClase_fi[1]),
            StringUtils.formatTime(horesClase[2]) + "-" + StringUtils.formatTime(horesClase_fi[2]),
            StringUtils.formatTime(horesClase[3]) + "-" + StringUtils.formatTime(horesClase_fi[3]),
            StringUtils.formatTime(horesClase[4]) + "-" + StringUtils.formatTime(horesClase_fi[4]),
            StringUtils.formatTime(horesClase[5]) + "-" + StringUtils.formatTime(horesClase_fi[5]),
            StringUtils.formatTime(horesClase[6]) + "-" + StringUtils.formatTime(horesClase_fi[6]),
            StringUtils.formatTime(horesClase[7]) + "-" + StringUtils.formatTime(horesClase_fi[7]),
            StringUtils.formatTime(horesClase[8]) + "-" + StringUtils.formatTime(horesClase_fi[8]),
            StringUtils.formatTime(horesClase[9]) + "-" + StringUtils.formatTime(horesClase_fi[9]),
            StringUtils.formatTime(horesClase[10]) + "-" + StringUtils.formatTime(horesClase_fi[10]),
            StringUtils.formatTime(horesClase[11]) + "-" + StringUtils.formatTime(horesClase_fi[11]),
            StringUtils.formatTime(horesClase[12]) + "-" + StringUtils.formatTime(horesClase_fi[12]),
            ""};

        listMaterials = coreCfg.getIesClient().getReservesClient().getReservesCollection().listMaterials("1=1 ORDER BY MATERIAL");
        for(BeanMaterial bm: listMaterials)
        {
            if(bm.getImatge()!=null)
            {
                bm.setIcon(new ImageIcon(bm.getImatge()));
            }
            else
            {
                bm.setIcon(new ImageIcon(getClass().getResource("/org/iesapp/modules/reserves/icons/material.gif")));
            }
        }
        
        listEspais = coreCfg.getIesClient().getSpacesCollection().listEspais("reservable=1 ORDER BY descripcio");
  
        
//        abrev2prof.put("ADMIN", "ADMINISTRADOR");
//        abrev2prof.put("PREF", "PREFECTURA");
//        abrev2prof.put("GUARD", "GUARDIES");
//        abrev2prof = StringUtils.getSortedMap(abrev2prof); //ordena per llinatge
        ///////////// Consulta el material disponible
        //        llistaMaterials = new ArrayList();
        //        llistaMaterialsUbicacio = new ArrayList();
        //        llistaMaterialsDescripcio = new ArrayList();
        //        llistaMaterialsIcones = new ArrayList();
  
        //        try {
        //            //llegeix de la base de dades el material disponible
        //            String SQL1 = "SELECT * FROM sig_reserves_material order by material";
        //            ResultSet rs1 = null;
        //            Statement st = coreCfg.getMysql().createStatement();
        //            rs1 = coreCfg.getMysql().getResultSet(SQL1, st);
        //            int i = 0;
        //            while (rs1 != null && rs1.next()) {
        //                int id = rs1.getInt("id");
        //
        //
        //                String material = rs1.getString("material");
        //                String ubicacio = rs1.getString("ubicacio");
        //                String descripcio = rs1.getString("descripcio");
        //                byte[] foto = rs1.getBytes("imatge");
        //
        //
        //                llistaMaterials.add(material);
        //                llistaMaterialsUbicacio.add(ubicacio);
        //                llistaMaterialsDescripcio.add(descripcio);
        //                if (foto != null) {
        //                    llistaMaterialsIcones.add(new ImageIcon(foto));
        //                } else {
        //                    llistaMaterialsIcones.add(new ImageIcon(getClass().getResource("/org/iesapp/modules/reserves/icons/material.gif")));
        //                }
        //
        //                i += 1;
        //            }
        //            if (rs1 != null) {
        //                rs1.close();
        //                st.close();
        //            }
        //        } catch (SQLException ex) {
        //            //
        //        }
        ///////////// Consulta d'aules reservables
        //        llistaAules = new ArrayList();
        //        llistaAulesDescripcio = new ArrayList();
        //
        //        try {
        //            //llegeix de la base de dades el material disponible
        //            String SQL1 = "SELECT id, aula, descripcio FROM sig_espais WHERE reservable=1 ORDER BY descripcio";
        //            ResultSet rs1 = null;
        //            Statement st = coreCfg.getMysql().createStatement();
        //            rs1 = coreCfg.getMysql().getResultSet(SQL1, st);
        //            int i = 0;
        //            while (rs1 != null && rs1.next()) {
        //                //int id = rs1.getInt("id");
        //                //material2Id.put(i,id);
        //
        //                String aula = rs1.getString("aula");
        //                String descripcio = rs1.getString("descripcio");
        //                llistaAules.add(aula);
        //                llistaAulesDescripcio.add(descripcio);
        //
        //                i += 1;
        //            }
        //            if (rs1 != null) {
        //                rs1.close();
        //                st.close();
        //            }
        //        } catch (SQLException ex) {
        //            //
        //        }
        //
        //
        //        String[] llista = new String[ReservesModule.llistaMaterials.size()];
        //        for (int i = 0; i < llista.length; i++) {
        //            String mat = (String) ReservesModule.llistaMaterials.get(i);
        //            String ubiq = (String) ReservesModule.llistaMaterialsUbicacio.get(i);
        //            llista[i] = mat + " [" + ubiq + "]";
        //        }
        //        //jList2.setListData(llista);
        //
        //        //////
        //
        //
        //        String[] llista2 = new String[ReservesModule.llistaAules.size()];
        //        for (int i = 0; i < llista2.length; i++) {
        //            String mat = (String) ReservesModule.llistaAules.get(i);
        //            String ubiq = (String) ReservesModule.llistaAulesDescripcio.get(i);
        //            llista2[i] = mat + " [" + ubiq + "]";
        //        }


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuReserves = new javax.swing.JMenu();
        jMenuNovaReserva = new javax.swing.JMenuItem();
        jPanelCerca = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable0 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        jCheckBox4 = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/iesapp/modules/reserves/ReservesBundle"); // NOI18N
        jMenuReserves.setText(bundle.getString("reserves")); // NOI18N
        jMenuReserves.setName("jMenuReserves"); // NOI18N

        jMenuNovaReserva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/modules/reserves/icons/insert.gif"))); // NOI18N
        jMenuNovaReserva.setText(bundle.getString("novareserva")); // NOI18N
        jMenuReserves.add(jMenuNovaReserva);

        jPanelCerca.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("searchConditions"))); // NOI18N

        jLabel8.setText(bundle.getString("usuari")); // NOI18N

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText(bundle.getString("aules")); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText(bundle.getString("material")); // NOI18N
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCercaLayout = new javax.swing.GroupLayout(jPanelCerca);
        jPanelCerca.setLayout(jPanelCercaLayout);
        jPanelCercaLayout.setHorizontalGroup(
            jPanelCercaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCercaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelCercaLayout.setVerticalGroup(
            jPanelCercaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCercaLayout.createSequentialGroup()
                .addGroup(jPanelCercaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addGap(2, 2, 2))
        );

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 204));
        jLabel2.setText(bundle.getString("myreservations")); // NOI18N
        jPanel8.add(jLabel2);

        jCheckBox3.setSelected(true);
        jCheckBox3.setText(bundle.getString("mostrapassades")); // NOI18N
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel8.add(jCheckBox3);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/modules/reserves/icons/insert.gif"))); // NOI18N
        jButton1.setText(bundle.getString("nova")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton1);

        modelTable0 = new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "id", bundle.getString("Usuari"), bundle.getString("Tipus"), bundle.getString("Concepte"), bundle.getString("Data"), bundle.getString("Hora"), bundle.getString("Motiu")
            }
        );
        jTable0.setModel(modelTable0);
        jTable0.getColumnModel().getColumn(0).setCellRenderer(new MyIconLabelRenderer(icons0));
        jTable0.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
        jTable0.getColumnModel().getColumn(3).setCellRenderer(new MyIconLabelRenderer(icons0));
        jTable0.getColumnModel().getColumn(4).setCellRenderer(new CellDateRenderer());
        jTable0.getColumnModel().getColumn(5).setCellRenderer(new MyIconLabelRenderer(icons0));
        jTable0.getColumnModel().getColumn(6).setCellRenderer(new TextAreaRenderer());
        jTable0.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTable0.getColumnModel().getColumn(3).setPreferredWidth(220);
        jTable0.getColumnModel().getColumn(5).setPreferredWidth(150);
        jTable0.setRowHeight(32);
        jTable0.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable0.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable0MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable0);

        jCheckBox4.setSelected(true);
        jCheckBox4.setText(bundle.getString("autoajusta")); // NOI18N
        jCheckBox4.setOpaque(false);
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentContainer());
        getContentContainer().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox4)
                .addContainerGap(363, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanelCerca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(319, Short.MAX_VALUE)
                .addComponent(jCheckBox4))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanelCerca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addGap(20, 20, 20)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (!listening) {
            return;
        }
        fillTable();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (!listening) {
            return;
        }
        fillTable();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if (!listening) {
            return;
        }
        fillTable();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        fillTable();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jTable0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable0MouseClicked

        int col = jTable0.getSelectedColumn();
        int row = jTable0.getSelectedRow();

        if (col == 0)        {
            CellTableState cts0 = (CellTableState) jTable0.getValueAt(row, 0);
            //System.out.println(cts0.getState());
            if(cts0.getState()==1)
            {
                //permet esborrar la reserva
                //Custom button text
                Object[] options = {bundle.getString("no"),bundle.getString("si")};
                String missatge = bundle.getString("cancelaconfirma"); //"Estau segur que voleu cancel·lar aquesta reserva?";

                int n = JOptionPane.showOptionDialog(null,
                    missatge, bundle.getString("cancela"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

                if(n!=1) {
                    return;
                }
                //refresh

                //.addAction("cancelReserva");
                int id = cts0.getCode();

                String SQL1 = "DELETE FROM sig_reserves WHERE id="+id;
                int nup = coreCfg.getMysql().executeUpdate(SQL1);
                fillTable();
            }
        }
        //            else
        //            {
            ////                //Modifica la reserva amb les dades actuals
            ////                tipus = ;
            ////                concept = ;
            ////                idrecurs = ;
            //            }

    }//GEN-LAST:event_jTable0MouseClicked

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        fillTable();
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuNovaReserva;
    private javax.swing.JMenu jMenuReserves;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelCerca;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable0;
    // End of variables declaration//GEN-END:variables

    private void fillTable() {
        listening = false;
        while (jTable0.getRowCount() > 0) {
            modelTable0.removeRow(0);
        }
        if(jCheckBox4.isSelected())
        {
            jTable0.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
        else
        {
            jTable0.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        

        
        String abreva = "";
        if(coreCfg.getUserInfo().getGrant()==User.ADMIN ||coreCfg.getUserInfo().getGrant()==User.PREF)
        {
            String prof = (String) jComboBox1.getSelectedItem();
            abreva = StringUtils.AfterLast(prof, "[");
            abreva = StringUtils.BeforeLast(abreva, "]");
        }
        else
        {
            abreva = coreCfg.getUserInfo().getAbrev();
        }
        
        ArrayList<BeanReserves> listReserves = coreCfg.getIesClient().getReservesClient().getReservesCollection().getReservesOf(abreva, jCheckBox3.isSelected(),
                                 jCheckBox1.isSelected(), jCheckBox2.isSelected(), msort.getMysqlOrder());
      
        
        for(BeanReserves bean: listReserves)
        {
                //condicio sobre quan puc cancel·lar una reserva
                CellTableState cts0 = null;
                java.util.Date sel = new java.util.Date(bean.getDia().getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(sel);
                Time currentHora = coreCfg.getIesClient().getDatesCollection().getHoresClase()[bean.getHora() - 1];
                cal.set(Calendar.HOUR_OF_DAY, currentHora.getHours());
                cal.set(Calendar.MINUTE, currentHora.getMinutes());
                java.util.Date sel2 = cal.getTime();

                //   System.out.println(avui+";"+sel2);

                boolean addToTable = true;

                if (avui.compareTo(sel2) <= 0) {
                    cts0 = new CellTableState("", bean.getId(), 1);     //son reserves que no han caducat
                    cts0.setTooltip(bundle.getString("cancela"));
                } else {
                    cts0 = new CellTableState("", bean.getId(), 0);     //son reserves passades
                    addToTable = jCheckBox3.isSelected();   //es mostren si està marcat el checkbox
                }

                String descrec = bean.getConcepte();
                    

                if (addToTable) {
                    String profe = bean.getNombreProfesor();
                    profe = profe.isEmpty()?bean.getAbrev():profe;
                   
                    String tipos = bundle.getString("aules");
                    if(bean.getTipusConcepte()==BeanReserves.MATERIAL)
                    {
                        tipos = bundle.getString("material");
                    }
                    
                    if (coreCfg.getUserInfo().getGrant() != User.ADMIN && coreCfg.getUserInfo().getGrant() != User.PREF) {
                       modelTable0.addRow(new Object[]{cts0, tipos,
                                    new CellTableState(descrec, -bean.getId(), 0), bean.getDia(),
                                    new CellTableState(horesCentre[bean.getHora() - 1] + "   [" + bean.getHora()+ "a]", -bean.getHora(), 0),
                                    bean.getMotiu()});
                    } else {
                        modelTable0.addRow(new Object[]{cts0, profe, tipos,
                                    new CellTableState(descrec, -bean.getId(), 0), bean.getDia(),
                                    new CellTableState(horesCentre[bean.getHora() - 1] + "   [" + bean.getHora() + "a]", -bean.getHora(), 0),
                                    bean.getMotiu()});
                    }
                }
       }
         
        listening = true;
    }

    @Override
    public ImageIcon getModuleIcon() {
        return null;
    }

    @Override
    public boolean isMultipleInstance() {
        return false;
    }

    
    @Override
    public void refreshUI() {
        jPanelCerca.setVisible(coreCfg.getUserInfo().getGrant()==User.ADMIN || coreCfg.getUserInfo().getGrant()==User.PREF );
        
        
        if(coreCfg.getUserInfo().getGrant()==User.ADMIN || coreCfg.getUserInfo().getGrant()==User.PREF )
        {
            modelTable0 = new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "id", bundle.getString("Usuari"), bundle.getString("Tipus"), 
                    bundle.getString("Concepte"), bundle.getString("Data"), 
                    bundle.getString("Hora"), bundle.getString("Motiu")
                    });
            jTable0.setModel(modelTable0);
            jTable0.getColumnModel().getColumn(0).setCellRenderer(new MyIconLabelRenderer(icons0));
            jTable0.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
            jTable0.getColumnModel().getColumn(3).setCellRenderer(new MyIconLabelRenderer(icons0));
            jTable0.getColumnModel().getColumn(4).setCellRenderer(new CellDateRenderer());
            jTable0.getColumnModel().getColumn(5).setCellRenderer(new MyIconLabelRenderer(icons0));
            jTable0.getColumnModel().getColumn(6).setCellRenderer(new TextAreaRenderer());
            jTable0.getColumnModel().getColumn(1).setPreferredWidth(150);
            jTable0.getColumnModel().getColumn(3).setPreferredWidth(220);
            jTable0.getColumnModel().getColumn(5).setPreferredWidth(150);
            jTable0.setRowHeight(32);
        } else {
            
             modelTable0 = new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "id", bundle.getString("Tipus"), bundle.getString("Concepte"), 
                     bundle.getString("Data"), bundle.getString("Hora"), bundle.getString("Motiu")
                    });
            jTable0.setModel(modelTable0);
            jTable0.getColumnModel().getColumn(0).setCellRenderer(new MyIconLabelRenderer(icons0));
            jTable0.getColumnModel().getColumn(2).setCellRenderer(new MyIconLabelRenderer(icons0));
            jTable0.getColumnModel().getColumn(3).setCellRenderer(new CellDateRenderer());
            jTable0.getColumnModel().getColumn(4).setCellRenderer(new MyIconLabelRenderer(icons0));
            jTable0.getColumnModel().getColumn(5).setCellRenderer(new TextAreaRenderer());
            jTable0.getColumnModel().getColumn(0).setPreferredWidth(150);
            jTable0.getColumnModel().getColumn(2).setPreferredWidth(220);
            jTable0.getColumnModel().getColumn(4).setPreferredWidth(150);
            jTable0.setRowHeight(32);
        }
        fillTable();
    }

    @Override
    public Lookup getLookup() {
        return moduleLookup;
    }

   
    @Override
    public void setMenus(JMenuBar jMenuBar1, JToolBar jToolbar1, StatusBar jStatusBar1) {
       jMenuBar1.add(jMenuReserves, 2);
    }


   
}
