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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author SynMcall
 */
public class Setores {
    private int id;
    private String nome;
    private String dsc;
    private String foto;
    private Date data;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }
    
    
    public static void cadastrarSetor(Setores setor){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        
        try {

            stmt = con.prepareStatement("INSERT INTO Setores(nome,dsc,data,foto) VALUES(?,?,?,?)");
            /*Insere todos os atributos da classe dentro da query */
            
            stmt.setString(1,setor.getNome());
            stmt.setString(2, setor.getDsc());
            stmt.setDate(3, setor.getData());
            stmt.setString(4, setor.getFoto());
            
            /*Executa a query SQL*/
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Setor cadastrado com Sucesso");
            
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            
        }
    
    }

    public static List<Setores> setoresSemRegistroDeCaixa(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Setores> setores = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Setores WHERE Setores.idSetores NOT IN (SELECT Setores_idSetores FROM Caixas)");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Setores setor = new Setores();
                setor.setId(rs.getInt("Setores.idSetores"));
                setor.setData(rs.getDate("Setores.data"));
                setor.setDsc(rs.getString("Setores.dsc"));
                setor.setFoto(rs.getString("Setores.foto"));
                setor.setNome(rs.getString("Setores.nome"));
                setores.add(setor);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return setores;
    }
    public static List<Setores> setoresComRegistroCaixa(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Setores> setores = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Setores WHERE Setores.idSetores IN (SELECT Setores_idSetores FROM Caixas)");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Setores setor = new Setores();
                setor.setId(rs.getInt("idSetores"));
                setor.setData(rs.getDate("data"));
                setor.setDsc(rs.getString("dsc"));
                setor.setFoto(rs.getString("foto"));
                setor.setNome(rs.getString("nome"));
                setores.add(setor);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return setores;
    }
    public static List<Setores> buscarSetores(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Setores> setores = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Setores");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Setores setor = new Setores();
                setor.setId(rs.getInt("idSetores"));
                setor.setData(rs.getDate("data"));
                setor.setDsc(rs.getString("dsc"));
                setor.setFoto(rs.getString("foto"));
                setor.setNome(rs.getString("nome"));
                setores.add(setor);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return setores;
    }
    
    
    public static Setores buscarSetor(int id){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        Setores setor = new Setores();
        setor.setId(id);
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Setores WHERE idSetores = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()){
                setor.setData(rs.getDate("data"));
                setor.setDsc(rs.getString("dsc"));
                setor.setFoto(rs.getString("foto"));
                setor.setNome(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return setor;
    }
    public String toString(){
        return this.getNome();
    }
}
