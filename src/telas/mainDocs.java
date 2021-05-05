/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import Objects.Membro;
import Objects.Relatorios;
import Objects.Utilitarios;

/**
 *
 * @author SynMcall
 */
public class mainDocs extends javax.swing.JFrame {

    /**
     * Creates new form mainDocs
     */
    public mainDocs() {
        initComponents();
        this.setLocationRelativeTo(null);
        atualizarBox();
    }
    
    private void atualizarBox(){
        for(Membro membro : Membro.buscarMembros()){
            boxMembros.addItem(membro);
        }
    }
    private void atualizarCamposCadastroUsuario(Membro membro){
        if(membro !=null){
            membro = Membro.perfilMembro(membro.getCPF());
            fotoMembro.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(), fotoMembro));
            nomeMembro.setText(membro.getNome());
    }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        btnEmitirCarteiraMembro = new javax.swing.JButton();
        btnCancelarCarteira = new javax.swing.JButton();
        boxMembros = new javax.swing.JComboBox<>();
        jLabel159 = new javax.swing.JLabel();
        jLabel160 = new javax.swing.JLabel();
        nomeMembro = new javax.swing.JTextField();
        fotoMembro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Carteirinha de Membro");

        main.setBackground(new java.awt.Color(255, 255, 255));
        main.setToolTipText("");
        main.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        main.setPreferredSize(new java.awt.Dimension(900, 570));
        main.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mainMouseMoved(evt);
            }
        });

        btnEmitirCarteiraMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEmitirCarteiraMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnEmitirCarteiraMembro.setText("Emitir Carteira");
        btnEmitirCarteiraMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEmitirCarteiraMembroMouseClicked(evt);
            }
        });

        btnCancelarCarteira.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarCarteira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarCarteira.setText("Cancelar");
        btnCancelarCarteira.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarCarteiraMouseClicked(evt);
            }
        });

        boxMembros.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel159.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel159.setText("CPF Membro:");
        jLabel159.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel159.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel160.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel160.setText("Nome:");
        jLabel160.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel160.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nomeMembro.setEditable(false);
        nomeMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nomeMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nomeMembroMouseEntered(evt);
            }
        });

        fotoMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembro.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembro.setMinimumSize(new java.awt.Dimension(256, 180));

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainLayout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(fotoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainLayout.createSequentialGroup()
                                .addComponent(btnEmitirCarteiraMembro)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelarCarteira))
                            .addGroup(mainLayout.createSequentialGroup()
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel159)
                                    .addComponent(jLabel160))
                                .addGap(61, 61, 61)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nomeMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(boxMembros, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fotoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxMembros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel159))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel160)
                    .addComponent(nomeMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarCarteira)
                    .addComponent(btnEmitirCarteiraMembro))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 455, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(main, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 457, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(main, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEmitirCarteiraMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmitirCarteiraMembroMouseClicked
        Relatorios.carteiraMembro((Membro) boxMembros.getSelectedItem());
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEmitirCarteiraMembroMouseClicked

    private void btnCancelarCarteiraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarCarteiraMouseClicked
        this.dispose();
// TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarCarteiraMouseClicked

    private void nomeMembroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nomeMembroMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeMembroMouseEntered

    private void mainMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainMouseMoved
        if(boxMembros.getSelectedItem()!=null){
            Membro membro = (Membro) boxMembros.getSelectedItem();
            atualizarCamposCadastroUsuario(membro);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_mainMouseMoved

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainDocs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainDocs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainDocs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainDocs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainDocs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<Object> boxMembros;
    private javax.swing.JButton btnCancelarCarteira;
    private javax.swing.JButton btnEmitirCarteiraMembro;
    private javax.swing.JLabel fotoMembro;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JPanel main;
    private javax.swing.JTextField nomeMembro;
    // End of variables declaration//GEN-END:variables

}
