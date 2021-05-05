/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import SqlConnection.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author SynMcall
 */
public class Acesso_Alteracao {
    private int id;
    private int acesso_id;
    private int alteracao_id;
    private Date data;

    public Date getData() {
        return data;
    }

    public void setData(Date alteracao) {
        this.data = alteracao;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAcesso_id() {
        return acesso_id;
    }

    public void setAcesso_id(int Acesso_id) {
        this.acesso_id = Acesso_id;
    }

    public int getAlteracao_id() {
        return alteracao_id;
    }

    public void setAlteracao_id(int Alteracao_id) {
        this.alteracao_id = Alteracao_id;
    }
    
    public static boolean cadastrarAcesso(Acesso_Alteracao alteracao){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO Acesso_Alteracao(Acesso_id,Alteracao_id,dataAlteracao) VALUES(?,?,now())");
            stmt.setInt(1,alteracao.getAcesso_id());
            stmt.setInt(2,alteracao.getAlteracao_id());
            /*Executa a query SQL*/
            stmt.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Erro ao Registrar Alteração, contate o Administrador ");
                return false;
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
        }
        return true;
    }
    
    public static List<Acesso_Alteracao> buscarAcessoAlteracoes(int id){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        List<Acesso_Alteracao> alteracoes = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Acesso_Alteracao WHERE Acesso_id = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            while(rs.next()){
                Acesso_Alteracao alteracao = new Acesso_Alteracao();
                alteracao.setId(rs.getInt("id"));
                alteracao.setAcesso_id(rs.getInt("Acesso_id"));
                alteracao.setAlteracao_id(rs.getInt("Alteracao_id"));
                alteracao.setData(rs.getDate("dataAlteracao"));
                alteracoes.add(alteracao);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return alteracoes;
    }
    
}
