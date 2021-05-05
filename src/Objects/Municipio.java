/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import SqlConnection.ConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author 20171TPMI0371
 */
public class Municipio {
    private int id;
    private String nome;
    private int estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    public static List<Municipio> buscarMunicipios(int idEstado){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Municipio> municipios = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM cidade WHERE estado = ?");
            stmt.setInt(1,idEstado);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Municipio municipio = new Municipio();
                municipio.setId(rs.getInt("id"));
                municipio.setNome(rs.getString("nome"));
                municipio.setEstado(rs.getInt("estado"));
                municipios.add(municipio);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return municipios;
    }
    public String toString(){
       return this.nome;
    }
}
