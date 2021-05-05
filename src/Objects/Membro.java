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
public class Membro extends Pessoa {
    private String telefone;
    private Date dataRegistro;
    private String foto;
    private Date dataBatismo;
    private int numDependentes;
    private int Pessoa_idPessoa;
    private String email;
    private int Cargo;
    private int id;
    
    public int getCargo() {
        return Cargo;
    }

    public void setCargo(int Cargo) {
        this.Cargo = Cargo;
    }

    public int getPessoa_idPessoa() {
        return Pessoa_idPessoa;
    }

    public void setPessoa_idPessoa(int Pessoa_idPessoa) {
        this.Pessoa_idPessoa = Pessoa_idPessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getDataBatismo() {
        return dataBatismo;
    }

    public void setDataBatismo(Date dataBatismo) {
        this.dataBatismo = dataBatismo;
    }

    public int getNumDependentes() {
        return numDependentes;
    }

    public void setNumDependentes(int numDependentes) {
        this.numDependentes = numDependentes;
    }
    
    
    public static boolean cadastrarMembro(Membro membro){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO Membros(Pessoa_cpfPessoa,email,telefone,dataRegistro,foto,dataBatismo,numDependentes,desligado,cargo,disciplinado,usuario) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            /*Insere todos os atributos da classe dentro da query */
            if(Pessoa.cadastrarPessoa(membro)){
                stmt.setString(1,membro.getCPF());
                stmt.setString(2,membro.getEmail());
                stmt.setString(3,membro.getTelefone());
                stmt.setDate(4, membro.getDataRegistro());
                stmt.setString(5, membro.getFoto());
                stmt.setDate(6, membro.getDataBatismo());
                stmt.setInt(7, membro.getNumDependentes());
                stmt.setInt(8, 0);
                stmt.setInt(9,1);
                stmt.setInt(10, 0);
                stmt.setInt(11,0);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null,membro.getNome() + " cadastrado com Sucesso !"); 
            }else{
                return false;
            }
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar, tente novamente! ");
                return false;
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            
        }
        return true;
    }
    
    
    public static List<Membro> buscarMembros(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Membro> membros = new ArrayList<>();
        
        try {
                stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Pessoa,Membros WHERE Membros.desligado = ? AND Pessoa.cpf = Membros.Pessoa_cpfPessoa");
                stmt.setInt(1, 0);
                rs = stmt.executeQuery();
            
            while(rs.next()){
                Membro membro = new Membro();
                membro.setCPF(rs.getString("Pessoa.cpf"));
                membro.setNome(rs.getString("Pessoa.nome"));
                membro.setEndereco(rs.getString("Pessoa.endereço"));
                membro.setDataNascimento(rs.getDate("Pessoa.dataNascimento"));
                membro.setEstadoCivil(rs.getInt("Pessoa.estadoCivil"));
                membro.setRG(rs.getString("Pessoa.RG"));
                membro.setNaturalidade(rs.getString("Pessoa.naturalidade"));
                membro.setEmail(rs.getString("Membros.email"));
                membro.setTelefone(rs.getString("Membros.telefone"));
                membro.setDataBatismo(rs.getDate("Membros.dataBatismo"));
                membro.setDataRegistro(rs.getDate("Membros.dataRegistro"));
                membro.setNumDependentes(rs.getInt("Membros.numDependentes")); 
                membro.setFoto(rs.getString("Membros.foto"));
                membros.add(membro);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Membros! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        
        return membros;
    }
    
    public static List<Membro> buscarMembroDesligado(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Membro> membros = new ArrayList<>();
        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Membros,Pessoa WHERE Membros.desligado = ?  AND Pessoa.cpf = Membros.Pessoa_cpfPessoa ");
            stmt.setInt(1, 1);
            rs = stmt.executeQuery();
            
            while(rs.next() && rs != null ){
                Membro membro = new Membro();
                membro.setCPF(rs.getString("Pessoa.cpf"));
                membro.setNome(rs.getString("Pessoa.nome"));
                membro.setEndereco(rs.getString("Pessoa.endereço"));
                membro.setDataNascimento(rs.getDate("Pessoa.dataNascimento"));
                membro.setEstadoCivil(rs.getInt("Pessoa.estadoCivil"));
                membro.setRG(rs.getString("Pessoa.RG"));
                membro.setNaturalidade(rs.getString("Pessoa.naturalidade"));
                membro.setEmail(rs.getString("Membros.email"));
                membro.setTelefone(rs.getString("Membros.telefone"));
                membro.setDataBatismo(rs.getDate("Membros.dataBatismo"));
                membro.setDataRegistro(rs.getDate("Membros.dataRegistro"));
                membro.setNumDependentes(rs.getInt("Membros.numDependentes"));       
                membro.setFoto(rs.getString("Membros.foto"));
                membros.add(membro);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Membros Desligados! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con,  stmt, rs);
        }
        return membros;
    }
    public static List<Membro> buscarMembrosDisciplinados(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Membro> membros = new ArrayList<>();
        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Membros,Pessoa WHERE Membros.disciplinado = ?  AND Pessoa.cpf = Membros.Pessoa_cpfPessoa ");
            stmt.setInt(1, 1);
            rs = stmt.executeQuery();
            
            while(rs.next() && rs != null ){
                Membro membro = new Membro();
                membro.setCPF(rs.getString("Pessoa.cpf"));
                membro.setNome(rs.getString("Pessoa.nome"));
                membro.setEndereco(rs.getString("Pessoa.endereço"));
                membro.setDataNascimento(rs.getDate("Pessoa.dataNascimento"));
                membro.setEstadoCivil(rs.getInt("Pessoa.estadoCivil"));
                membro.setRG(rs.getString("Pessoa.RG"));
                membro.setNaturalidade(rs.getString("Pessoa.naturalidade"));
                membro.setEmail(rs.getString("Membros.email"));
                membro.setTelefone(rs.getString("Membros.telefone"));
                membro.setDataBatismo(rs.getDate("Membros.dataBatismo"));
                membro.setDataRegistro(rs.getDate("Membros.dataRegistro"));
                membro.setNumDependentes(rs.getInt("Membros.numDependentes")); 
                membro.setFoto(rs.getString("Membros.foto"));
                membros.add(membro);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Membros Disciplinados! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con,  stmt, rs);
        }
        return membros;
    }
    public static List<Membro> buscarMembrosNaoDisciplinados(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Membro> membros = new ArrayList<>();
        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Membros,Pessoa WHERE Membros.disciplinado = ?  AND Membros.desligado = ? AND Pessoa.cpf = Membros.Pessoa_cpfPessoa ");
            stmt.setInt(1, 0);
            stmt.setInt(2,0);
            rs = stmt.executeQuery();
            
            while(rs.next() && rs != null ){
                Membro membro = new Membro();
                membro.setCPF(rs.getString("Pessoa.cpf"));
                membro.setNome(rs.getString("Pessoa.nome"));
                membro.setEndereco(rs.getString("Pessoa.endereço"));
                membro.setDataNascimento(rs.getDate("Pessoa.dataNascimento"));
                membro.setEstadoCivil(rs.getInt("Pessoa.estadoCivil"));
                membro.setRG(rs.getString("Pessoa.RG"));
                membro.setNaturalidade(rs.getString("Pessoa.naturalidade"));
                membro.setEmail(rs.getString("Membros.email"));
                membro.setTelefone(rs.getString("Membros.telefone"));
                membro.setDataBatismo(rs.getDate("Membros.dataBatismo"));
                membro.setDataRegistro(rs.getDate("Membros.dataRegistro"));
                membro.setNumDependentes(rs.getInt("Membros.numDependentes"));      
                membro.setFoto((rs.getString("Membros.foto")));
                membros.add(membro);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Membros Disciplinados! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con,  stmt, rs);
        }
        return membros;
    }
    
    public static List<Membro> buscarMembrosNaoUsuarios(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Membro> membros = new ArrayList<>();
        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Membros,Pessoa WHERE Membros.usuario = ? AND Membros.desligado = ? AND Membros.disciplinado = ?  AND Pessoa.cpf = Membros.Pessoa_cpfPessoa ");
            stmt.setInt(1, 0);
            stmt.setInt(2, 0);
            stmt.setInt(3, 0);
            rs = stmt.executeQuery();
            
            while(rs.next() && rs != null ){
                Membro membro = new Membro();
                membro.setCPF(rs.getString("Pessoa.cpf"));
                membro.setNome(rs.getString("Pessoa.nome"));
                membro.setEndereco(rs.getString("Pessoa.endereço"));
                membro.setDataNascimento(rs.getDate("Pessoa.dataNascimento"));
                membro.setEstadoCivil(rs.getInt("Pessoa.estadoCivil"));
                membro.setRG(rs.getString("Pessoa.RG"));
                membro.setNaturalidade(rs.getString("Pessoa.naturalidade"));
                membro.setEmail(rs.getString("Membros.email"));
                membro.setTelefone(rs.getString("Membros.telefone"));
                membro.setDataBatismo(rs.getDate("Membros.dataBatismo"));
                membro.setDataRegistro(rs.getDate("Membros.dataRegistro"));
                membro.setNumDependentes(rs.getInt("Membros.numDependentes"));              
                membros.add(membro);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Membros Nao Usuarios ! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con,  stmt, rs);
        }
        return membros;
    }
    
    
    
    
    public static Membro perfilMembro( String cpf){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        com.mysql.jdbc.PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Membro membro = new Membro();
        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Pessoa WHERE cpf = ?");
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            stmt2 = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Membros WHERE Membros.Pessoa_cpfPessoa = ?");
            stmt2.setString(1, cpf);
            rs2 = stmt2.executeQuery();
           if(rs != null && rs2!=null && rs.next() && rs2.next()){
                membro.setCPF(rs.getString("cpf"));
                membro.setNome(rs.getString("nome"));
                membro.setEndereco(rs.getString("endereço"));
                membro.setDataNascimento(rs.getDate("dataNascimento"));
                membro.setEstadoCivil(rs.getInt("estadoCivil"));
                membro.setRG(rs.getString("RG"));
                membro.setNaturalidade(rs.getString("naturalidade"));
                membro.setEmail(rs2.getString("email"));
                membro.setTelefone(rs2.getString("telefone"));
                membro.setDataBatismo(rs2.getDate("dataBatismo"));
                membro.setDataRegistro(rs2.getDate("dataRegistro"));
                membro.setNumDependentes(rs2.getInt("numDependentes"));  
                membro.setFoto(rs2.getString("foto"));
                membro.setCargo(rs2.getInt("cargo"));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Membro! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con,  stmt, rs);
            ConnectionFactory.closeConnection(con, stmt2, rs2);
        }
        return membro;
    }

    
   public static void alterarMembro(Membro membro){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("UPDATE Membros SET email = ?, telefone = ?, foto = ?, dataBatismo = ?, numDependentes = ?, dataRegistro = ? WHERE Pessoa_cpfPessoa = ?");
            stmt.setString(1,membro.getEmail());
            stmt.setString(2,membro.getTelefone());
            stmt.setString(3,membro.getFoto());
            stmt.setDate(4,membro.getDataBatismo());
            stmt.setInt(5,membro.getNumDependentes());
            stmt.setDate(6,membro.getDataRegistro());
            stmt.setString(7,membro.getCPF());
            Pessoa.alterarPessoa(membro);

            
            stmt.executeUpdate();          

            JOptionPane.showMessageDialog(null,"Membro alterado com Sucesso !");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Alterar Membro"+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
        }
    }
   public static void atualizarBD(){
       Connection con = ConnectionFactory.getConnection();
       com.mysql.jdbc.PreparedStatement stmt = null;
       ResultSet rs = null;
       com.mysql.jdbc.PreparedStatement stmt2 = null;
        try {
                stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Membros,Disciplina WHERE Membros.disciplinado = 1 AND Membros.Pessoa_cpfPessoa = Disciplina.Pessoa_cpfPessoa");
                rs = stmt.executeQuery();
            
            while(rs.next()){
                Date dataFim = rs.getDate("Disciplina.dataFim");
                Date dataAtual = (Date) new java.sql.Date(Utilitarios.dataAtual().getTime());
                if(dataFim.before(dataAtual)){
                    stmt2 = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("UPDATE Membros SET disciplinado = 0 WHERE Pessoa_cpfPessoa = ? ");
                    stmt2.setString(1, rs.getString("Membros.Pessoa_cpfPessoa"));
                    stmt2.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao atualizar BD "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
            ConnectionFactory.closeConnection(con, stmt2);
        }

   }

   
   public String toString(){
       return this.getCPF();
    }

}
