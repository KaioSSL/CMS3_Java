/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;
import Objects.*;
import java.awt.Color;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author SynMcall
 */
public class mainSecretaria extends javax.swing.JFrame {
    private SimpleDateFormat padraoData = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat padraoDataHora = new SimpleDateFormat("dd/MM/yyyy HH/mm/ss");
    private int idAcesso = Acesso.idAtualAcesso();
    private java.sql.Date sqlDate;
    private login userLogin;
    public String user;
    public String pass;
    String caminhoFoto;
    Color corFundoPanel = java.awt.Color.white;
    Color corFundoPanelAtivado = new Color(102, 179, 255);
    private Acesso acesso;
    
    
    /**
     * Creates new form main
     * @param user
     * @param pass
     * @param acesso
     */
    public mainSecretaria(String user,String pass, Acesso acesso) {
        initComponents();
        // Recebe os Parametros de Usuario da Tela Login
        this.user = user;
        this.pass = pass;
        this.setLocationRelativeTo(null);
        this.acesso = acesso;

        // Atualiza para o desk Home e Seta o Titulo
        Utilitarios.atualizarPainel(desk,home);
        logoHeaderDesk.setText("CMS - Início");
        
        //Atualiza tabelas e Combo Box do sistema
        atualizarComboBox();
        atualizarTabelas();
        
        //Seta permissões do usuario de acordo com seu ModoDeAcess
       
    }

    private mainSecretaria() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void atualizarMunicipios(Estado estado){
        naturalidadeMunicipioMembro.removeAllItems();
        naturalidadeMunicipioAlterarMembro.removeAllItems();
        for(Municipio municipio: Municipio.buscarMunicipios(estado.getId())){
            naturalidadeMunicipioMembro.addItem(municipio);
            naturalidadeMunicipioAlterarMembro.addItem(municipio);
        }
    }
    private void atualizarCamposAlterarUsuario(){

        //Dados
        nomeAlterarUsuario.setText(nomeUsuarioPerfil.getText());
        rgAlterarUsuario.setText(rgUsuario.getText());
        cpfAlterarUsuario.setText(cpfUsuario.getText());
        telefoneAlterarUsuario.setText(telefoneUsuario.getText());
        enderecoAlterarUsuario.setText(enderecoUsuario.getText());
        boxModoUsuarioAlterarUsuario.getModel().setSelectedItem(boxModoUsuarioPerfil.getSelectedItem());
        naturalidadeAlterarUsuario.setText(naturalidadeUsuario.getText());
        dependentesAlterarUsuario.getModel().setSelectedItem(dependentesUsuario.getSelectedItem());
        dataBatismoAlterarUsuario.setText(dataBatismoUsuario.getText());
        dataNascimentoAlterarUsuario.setText(dataNascimentoUsuario.getText());
        dataRegistroAlterarUsuario.setText(dataRegistroUsuario.getText());
        estadoCivilAlterarUsuario.getModel().setSelectedItem(boxEstadoCivilUsuario.getSelectedItem());
        emailAlterarUsuario.setText(emailUsuario.getText());
        loginAlterarUsuario.setText(loginUsuarioPerfil.getText());
        fotoAlterarUsuario.setIcon(fotoUsuarioPerfil.getIcon());
        //Limpadores e Atualizadores
        Utilitarios.fotoDefault(fotoUsuarioPerfil);
        Utilitarios.atualizarPainel(desk, alterarUsuario);
        Utilitarios.limparField(perfilUsuario);
        logoHeaderDesk.setText("CMS - Alterar Usuario");
    
    }

    private void atualizarCamposPerfilUsuario(Usuario usuario){
        if(usuario.getCpf()!=null){
        Membro membro = Membro.perfilMembro(usuario.getCpf());
        nomeUsuarioPerfil.setText(membro.getNome());
        rgUsuario.setText(membro.getRG());
        cpfUsuario.setText(membro.getCPF());
        telefoneUsuario.setText(membro.getTelefone());
        enderecoUsuario.setText(membro.getEndereco());
        Cargos cargo = Cargos.buscarCargo(membro.getCargo());
        boxUsuarios.getModel().setSelectedItem(cargo);
        ModoUsuario modo = ModoUsuario.buscarModoUsuario(usuario.getModoUsuario());
        boxModoUsuarioPerfil.getModel().setSelectedItem(modo);
        String naturalidade;
        naturalidade = membro.getNaturalidade().substring(0,2) + " - " +  membro.getNaturalidade().substring(2);
        naturalidadeUsuario.setText(naturalidade);
        dependentesUsuario.getModel().setSelectedItem(String.valueOf(membro.getNumDependentes()));
        
        dataBatismoUsuario.setText(Utilitarios.reconstruirData(membro.getDataBatismo().toString()));
        dataNascimentoUsuario.setText(Utilitarios.reconstruirData(membro.getdataNascimento().toString()));
        dataRegistroUsuario.setText(Utilitarios.reconstruirData(membro.getDataRegistro().toString()));

        EstadoCivil estado = EstadoCivil.buscarEstado(membro.getEstadoCivil());
        boxEstadoCivilUsuario.getModel().setSelectedItem(estado);
        emailUsuario.setText(membro.getEmail());
        loginUsuarioPerfil.setText(usuario.getLogin());
        senhaUsuarioPerfil.setText(usuario.getPassword());
        Utilitarios.atualizarPainel(desk, perfilUsuario);
        logoHeaderDesk.setText("CMS - Perfil Usuario");
        if(membro.getFoto()!="null"){
            fotoUsuarioPerfil.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(), fotoUsuarioPerfil));
            caminhoFoto = membro.getFoto();
        }
    }
    }
    private void atualizarCamposCadastroUsuario(Membro membro){
        if(membro !=null){
            membro = Membro.perfilMembro(membro.getCPF());
            fotoUsuario.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(), fotoUsuario));
            nomeUsuario.setText(membro.getNome());
    }
    
    }
    
    private void atualizarCamposPerfilAcesso(int id){
        DefaultTableModel modeloAcessoAlteracao = (DefaultTableModel) tableAlteracoes.getModel();
        tableAlteracoes.setRowSorter(new TableRowSorter(modeloAcessoAlteracao));
        modeloAcessoAlteracao.setNumRows(0);
        
        for(Acesso_Alteracao alteracao : Acesso_Alteracao.buscarAcessoAlteracoes(id)){
            modeloAcessoAlteracao.addRow(new Object[]{
               alteracao.getId(),
               alteracao.getAcesso_id(),
               Alteracao.perfilAlteracao(alteracao.getAlteracao_id()),
               Acesso.perfilAcesso(alteracao.getAcesso_id()),
               alteracao.getData(),
               
            });
        }
        Utilitarios.atualizarPainel(desk,logAlteracoes);
    }
    
    private void atualizarCamposCaixaPerfil(Caixas caixa){
        DefaultTableModel modeloOfertas = (DefaultTableModel) tableCaixaOfertas.getModel();
        tableCaixaOfertas.setRowSorter(new TableRowSorter(modeloOfertas));
        modeloOfertas.setNumRows(0);

    
        DefaultTableModel modeloDespesas = (DefaultTableModel) tableCaixaDespesas.getModel();
        tableCaixaDespesas.setRowSorter(new TableRowSorter(modeloDespesas));
        modeloDespesas.setNumRows(0);
        
        for(Oferta oferta: Oferta.buscarOfertas(caixa.getSetores_idSetores())){
            modeloOfertas.addRow(new Object[]{
                Setores.buscarSetor(oferta.getSetores_idSetores()),
                oferta.getDsc(),
                oferta.getData(),
                oferta.getValor(),
            });
        }
        for(Despesa despesa : Despesa.buscarDespesas(caixa.getSetores_idSetores())){
            modeloDespesas.addRow(new Object[]{
                Setores.buscarSetor(despesa.getSetores_idSetores()),
                despesa.getDsc(),
                despesa.getData(),
                despesa.getValor(),
            });
        }
        Utilitarios.atualizarPainel(desk, perfilCaixa);
        }
    private void atualizarCamposDisciplinaPerfil(Disciplina disciplina){
        Utilitarios.atualizarPainel(desk,perfilDisciplina);
        Membro membro = Membro.perfilMembro(disciplina.getCpf());
        nomeMembroDisciplinaPerfil.setText(membro.getNome());
        boxMembroPerfilDisciplina.getModel().setSelectedItem(membro);
        
        dataDisciplinaPerfilInicio.setText(Utilitarios.reconstruirData(disciplina.getDataInicio().toString()));
        dataDisciplinaPerfilFim.setText(Utilitarios.reconstruirData(disciplina.getDataFim().toString()));
        dataDisciplinaPerfilRegistro.setText(Utilitarios.reconstruirData(disciplina.getDataRegistro().toString()));
        
        dscDisciplinaPerfil.setText(disciplina.getDsc());
        fotoMembroDisciplinaPerfil.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(),fotoMembroDisciplinaPerfil));
    }
    private void atualizarCamposAlterarDisciplina(){
        nomeMembroDisciplinaAlterar.setText(nomeMembroDisciplinaPerfil.getText());
        boxMembroAlterarDisciplina.getModel().setSelectedItem(boxMembroPerfilDisciplina.getSelectedItem());
        
        dataAlterarDisciplinaInicio.setText(dataDisciplinaPerfilInicio.getText());
        dataAlterarDisciplinaFim.setText(dataDisciplinaPerfilFim.getText());
        
        dscAlterarDisciplina.setText(dscDisciplinaPerfil.getText());
        fotoMembroDisciplinaAlterar.setIcon(fotoMembroDisciplinaPerfil.getIcon());
         Utilitarios.atualizarPainel(desk,alterarDisciplina);
         Utilitarios.limparField(perfilDisciplina);
    }
    private void atualizarCamposDesligamentoPerfil(Desligamento desligamento){
        Utilitarios.atualizarPainel(desk,perfilDesligamento);
        Membro membro = Membro.perfilMembro(desligamento.getCpf());
        nomeMembroDesligamentoPerfil.setText(membro.getNome());
        boxMembroPerfilDesligamento.getModel().setSelectedItem(membro); 
        dataPerfilDesligamento.setText(Utilitarios.reconstruirData(desligamento.getData().toString()));
        dscPerfilDesligamento.setText(desligamento.getDsc());
        fotoMembroDesligamentoPerfil.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(),fotoMembroDesligamentoPerfil));
    }
    private void atualizarCamposDesligamentoAlterar(){
        nomeMembroDesligamentoAlterar.setText(nomeMembroDesligamentoPerfil.getText());
        boxMembroAlterarDesligamento.getModel().setSelectedItem(boxMembroPerfilDesligamento.getSelectedItem());
        dataAlterarDesligamento.setText(dataPerfilDesligamento.getText());
        dscAlterarDesligamento.setText(dscPerfilDesligamento.getText());
        fotoMembroDesligamentoAlterar.setIcon(fotoMembroDesligamentoPerfil.getIcon());
        Utilitarios.atualizarPainel(desk,alterarDesligamento);
        Utilitarios.limparField(perfilDesligamento);
    }
    private void atualizarCamposMembroPerfil(Membro membro){
        nomeMembroPerfil.setText(membro.getNome());
        rgMembroPerfil.setText(membro.getRG());
        cpfMembroPerfil.setText(membro.getCPF());
        emailMembroPerfil.setText(membro.getEmail());
        telefoneMembroPerfil.setText(membro.getTelefone());
        enderecoMembroPerfil.setText(membro.getEndereco());
        String naturalidade;
        naturalidade = membro.getNaturalidade().substring(0,2) + " - " +  membro.getNaturalidade().substring(2);
        naturalidadeMembroPerfil.setText(naturalidade);        
        dependentesMembroPerfil.getModel().setSelectedItem(String.valueOf(membro.getNumDependentes()));
        
        dataBatismoMembroPerfil.setText(Utilitarios.reconstruirData(membro.getDataBatismo().toString()));
        dataNascimentoMembroPerfil.setText(Utilitarios.reconstruirData(membro.getdataNascimento().toString()));        
        dataRegistroMembroPerfil.setText(Utilitarios.reconstruirData(membro.getDataRegistro().toString()));
        cargosMembroPerfil.getModel().setSelectedItem(Cargos.buscarCargo(membro.getCargo()));
        estadoCivilMembroPerfil.getModel().setSelectedItem(EstadoCivil.buscarEstado(membro.getEstadoCivil()));
        fotoMembroPerfil.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(), fotoMembroPerfil));
        caminhoFoto = membro.getFoto();
        Utilitarios.atualizarPainel(desk, perfilMembro);
    }
    private void atualizarCamposAlterarMembro(){
        
        nomeAlterarMembro.setText(nomeMembroPerfil.getText());
        rgAlterarMembro.setText(rgMembroPerfil.getText());
        cpfAlterarMembro.setText(cpfMembroPerfil.getText());
        telefoneAlterarMembro.setText(telefoneMembroPerfil.getText());
        enderecoAlterarMembro.setText(enderecoMembroPerfil.getText());
        dependentesAlterarMembro.getModel().setSelectedItem(dependentesMembroPerfil.getSelectedItem());
        dataBatismoAlterarMembro.setText(dataBatismoMembroPerfil.getText());
        dataNascimentoAlterarMembro.setText(dataNascimentoMembroPerfil.getText());
        dataRegistroAlterarMembro.setText(dataRegistroMembroPerfil.getText());
        emailAlterarMembro.setText(emailMembroPerfil.getText());
        estadoCivilAlterarMembro.getModel().setSelectedItem(estadoCivilMembroPerfil.getSelectedItem());
        fotoAlterarMembro.setIcon(fotoMembroPerfil.getIcon());
        
        Utilitarios.atualizarPainel(desk,alterarMembro);
        Utilitarios.limparField(perfilMembro);
        
    
    }
    private void atualizarCamposConsagracaoPerfil(Consagracao consagracao){
        Membro membro = Membro.perfilMembro(consagracao.getCpf());
        boxCargosPerfilConsagracao.getModel().setSelectedItem(Cargos.buscarCargo(consagracao.getCargos_idCargo()));
        boxMembroPerfilConsagracao.getModel().setSelectedItem(membro);
        nomeMembroConsagracaoPerfil.setText(membro.getNome());
        dataConsagracaoPerfil.setText(Utilitarios.reconstruirData(consagracao.getData().toString()));   
        dscConsagracaoPerfil.setText(consagracao.getDsc());
        fotoMembroConsagracaoPerfil.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(),fotoMembroConsagracaoPerfil));
        Utilitarios.atualizarPainel(desk, perfilConsagracao);
    
    }
    private void atualizarCamposConsagracaoAlterar(){
        nomeMembroConsagracaoAlterar.setText(nomeMembroConsagracaoPerfil.getText());
        
        boxCargosConsagracaoAlterar.getModel().setSelectedItem(boxCargosPerfilConsagracao.getSelectedItem());
        boxMembroConsagracaoAlterar.getModel().setSelectedItem(boxMembroPerfilConsagracao.getSelectedItem());
        dataConsagracaoAlterar.setText(dataConsagracaoPerfil.getText());
        dscConsagracaoAlterar.setText(dataConsagracaoPerfil.getText());
        fotoMembroConsagracaoAlterar.setIcon(fotoMembroConsagracaoPerfil.getIcon());
        Utilitarios.atualizarPainel(desk,alterarConsagracao);
        Utilitarios.limparField(perfilConsagracao);
    
    
    }
    private void atualizarTabelas(){
        Membro.atualizarBD();
        
        DefaultTableModel modeloAcesso = (DefaultTableModel) tableAcessos.getModel();
        tableAcessos.setRowSorter(new TableRowSorter(modeloAcesso));
        modeloAcesso.setNumRows(0);
        
        DefaultTableModel modeloUsuarios = (DefaultTableModel) tableUsuarios.getModel();
        tableUsuarios.setRowSorter(new TableRowSorter(modeloUsuarios));
        modeloUsuarios.setNumRows(0);
        
        DefaultTableModel modeloDizimos = (DefaultTableModel) tableDizimos.getModel();
        tableDizimos.setRowSorter(new TableRowSorter(modeloDizimos));
        modeloDizimos.setNumRows(0);
        
        DefaultTableModel modeloOfertas = (DefaultTableModel) tableOfertas.getModel();
        tableOfertas.setRowSorter(new TableRowSorter(modeloOfertas));
        modeloOfertas.setNumRows(0);
        
        DefaultTableModel modeloDespesas = (DefaultTableModel) tableDespesas.getModel();
        tableDespesas.setRowSorter(new TableRowSorter(modeloDespesas));
        modeloDespesas.setNumRows(0);
        
        DefaultTableModel modeloCaixas = (DefaultTableModel) tableCaixas.getModel();
        tableCaixas.setRowSorter(new TableRowSorter(modeloCaixas));
        modeloCaixas.setNumRows(0);
        
        DefaultTableModel modeloSetores = (DefaultTableModel) tableSetores.getModel();
        tableSetores.setRowSorter(new TableRowSorter(modeloSetores));
        modeloSetores.setNumRows(0);
        
        DefaultTableModel modeloMembroDesligado = (DefaultTableModel) tableMembrosDesligados.getModel();
        tableMembrosDesligados.setRowSorter(new TableRowSorter(modeloMembroDesligado));
        modeloMembroDesligado.setNumRows(0);
        
        DefaultTableModel modeloMembrosDisciplinados = (DefaultTableModel) tableMembrosDisciplinados.getModel();
        tableMembrosDisciplinados.setRowSorter(new TableRowSorter(modeloMembrosDisciplinados));
        modeloMembrosDisciplinados.setNumRows(0);
        
        DefaultTableModel modeloMembro = (DefaultTableModel) tableMembros.getModel();
        tableMembros.setRowSorter(new TableRowSorter(modeloMembro));
        modeloMembro.setNumRows(0);
        
        DefaultTableModel modeloDisciplina = (DefaultTableModel) tableDisciplina.getModel();
        tableDisciplina.setRowSorter(new TableRowSorter(modeloDisciplina));
        modeloDisciplina.setNumRows(0);
        
        DefaultTableModel modeloConsagracao = (DefaultTableModel) tableConsagracao.getModel();
        tableConsagracao.setRowSorter(new TableRowSorter(modeloConsagracao));
        modeloConsagracao.setNumRows(0);
        
        DefaultTableModel modeloExclusao = (DefaultTableModel) tableDesligamento.getModel();
        tableDesligamento.setRowSorter(new TableRowSorter(modeloExclusao));
        modeloExclusao.setNumRows(0);
        
        
        
        for(Acesso acesso : Acesso.buscarAcessos()){
            modeloAcesso.addRow(new Object[]{
                acesso.getId(),
                acesso.getUserLogin(),
                acesso.getDataLogin(),
                acesso.getDataLogout(),
            });
        
        }
        
        for(Membro membroAux : Membro.buscarMembrosDisciplinados()){
            modeloMembrosDisciplinados.addRow(new Object[]{
                    membroAux.getCPF(),
                    membroAux.getNome(),
                    membroAux.getRG(),
                    membroAux.getNaturalidade(),
                    EstadoCivil.buscarEstado(membroAux.getEstadoCivil()),
                    membroAux.getdataNascimento(),
                    membroAux.getDataBatismo(),
                    membroAux.getDataRegistro(),
                    membroAux.getNumDependentes(),
            
            });
        }
        for(Membro membroAux: Membro.buscarMembroDesligado()){
            modeloMembroDesligado.addRow(new Object[]{
                      membroAux.getCPF(),
                      membroAux.getNome(),
                      membroAux.getRG(),
                      membroAux.getNaturalidade(),
                      EstadoCivil.buscarEstado(membroAux.getEstadoCivil()),
                      membroAux.getdataNascimento(),
                      membroAux.getDataBatismo(),
                      membroAux.getDataRegistro(),
                      membroAux.getNumDependentes(),
            });
        }
        for (Membro membroAux: Membro.buscarMembros()) {
            modeloMembro.addRow(new Object[]{
                      membroAux.getCPF(),
                      membroAux.getNome(),
                      membroAux.getRG(),
                      membroAux.getNaturalidade(),
                      EstadoCivil.buscarEstado(membroAux.getEstadoCivil()),
                      membroAux.getdataNascimento(),
                      membroAux.getDataBatismo(),
                      membroAux.getDataRegistro(),
                      membroAux.getNumDependentes(),
              });
      }
        

        for(Disciplina disciplinaAux : Disciplina.buscarDisciplinas()){
            modeloDisciplina.addRow(new Object[]{
                disciplinaAux.getCpf(),
                disciplinaAux.getDataInicio(),
                disciplinaAux.getDataFim(),
                disciplinaAux.getDataRegistro(),
                disciplinaAux.getDsc(),
                
            });
        }
        
        for(Desligamento desligamento : Desligamento.buscarDesligamentos()){
            modeloExclusao.addRow(new Object[]{
                desligamento.getId(),
                desligamento.getCpf(),
                desligamento.getData(),
                desligamento.getDsc(),
            });
        }
        for(Consagracao consagracao : Consagracao.buscarConsagracoes()){
            modeloConsagracao.addRow(new Object[]{
                consagracao.getCpf(),
                Cargos.buscarCargo(consagracao.getCargos_idCargo()),
                consagracao.getData(),
                consagracao.getDsc(),
            });
        }
        //Atualiza A tabela 'tableSetores' Do JPanel 'caixaESetores'
        for(Setores setor : Setores.buscarSetores()){
            modeloSetores.addRow(new Object[]{
                setor.getId(),
                setor.getNome(),
                setor.getDsc(),
                setor.getData(),
            });
         
        }
        
        //Atualiza a Tabela 'tableCaixas' do JPanel 'caixasESetores'
        for(Caixas caixa : Caixas.buscarCaixas()){
            modeloCaixas.addRow(new Object[]{
                caixa.getId(),
                Setores.buscarSetor(caixa.getSetores_idSetores()),
                caixa.getReceita(),
                caixa.getData(),
                caixa.getDsc(),
            });
        }
        
        for(Despesa despesa : Despesa.buscarDespesas()){
            modeloDespesas.addRow(new Object[] {
                Setores.buscarSetor(despesa.getSetores_idSetores()),
                despesa.getDsc(),
                despesa.getData(),
                despesa.getValor(),
            });
        }
        
        for(Oferta oferta : Oferta.buscarOfertas()){
            modeloOfertas.addRow(new Object[] {
                Setores.buscarSetor(oferta.getSetores_idSetores()),
                oferta.getDsc(),
                oferta.getData(),
                oferta.getValor(),
            });
        }
        
        
        for(Dizimos dizimo : Dizimos.buscarDizimos()){
            modeloDizimos.addRow(new Object[]{
            Membro.perfilMembro(dizimo.getPessoa_cpfPessoa()),
            dizimo.getData(),
            dizimo.getValor(),
            });
        }
        for(Usuario usuario: Usuario.buscarUsuarios()){
            modeloUsuarios.addRow(new Object[]{
                usuario.getLogin(),
                usuario.getPassword(),
                usuario.getCpf(),
                Membro.perfilMembro(usuario.getCpf()).getNome(),
                ModoUsuario.buscarModoUsuario(usuario.getModoUsuario()),
                usuario.getData(),
            });
        
        }
    }
    private void atualizarComboBox(){
        //--Membros -------------
        //EstadoCivil
        
        naturalidadeEstadoMembro.removeAllItems();
        naturalidadeEstadoAlterarMembro.removeAllItems();
        for(Estado estado : Estado.buscarEstados()){
            naturalidadeEstadoMembro.addItem(estado);
            naturalidadeEstadoAlterarMembro.addItem(estado);
        }
        
        estadoCivilAlterarMembro.removeAllItems();
        estadoCivilMembro.removeAllItems();
        estadoCivilMembroPerfil.removeAllItems();
        for(EstadoCivil estadoCivil : EstadoCivil.buscarEstados()){
            estadoCivilAlterarMembro.addItem(estadoCivil);
            estadoCivilMembro.addItem(estadoCivil);
            estadoCivilMembroPerfil.addItem(estadoCivil);

        }
        
        boxCargosConsagracaoAlterar.removeAllItems();
        boxCargosPerfilConsagracao.removeAllItems();
        boxCargoConsagracao.removeAllItems();
        cargosMembroPerfil.removeAllItems();
        for(Cargos cargo: Cargos.buscarCargos()){
            //Membro 
            cargosMembroPerfil.addItem(cargo);
            //Consagracao 
            boxCargoConsagracao.addItem(cargo);
            
            boxCargosPerfilConsagracao.addItem(cargo);
            
            boxCargosConsagracaoAlterar.addItem(cargo);
            
            
        }
        boxMembroDesligamento.removeAllItems();
        boxMembroPerfilDesligamento.removeAllItems();
        boxMembroAlterarDesligamento.removeAllItems();
        for(Membro membro : Membro.buscarMembros()){
            //Exclusao
            boxMembroDesligamento.addItem(membro);
            boxMembroPerfilDesligamento.addItem(membro);
            boxMembroAlterarDesligamento.addItem(membro);

        }
        
        boxMembroDisciplina.removeAllItems();
        boxMembroAlterarDisciplina.removeAllItems();
        boxMembroPerfilDisciplina.removeAllItems();
        
        boxMembroConsagracao.removeAllItems();
        boxMembroPerfilConsagracao.removeAllItems();
        boxMembroConsagracaoAlterar.removeAllItems();
        boxMembroDizimo.removeAllItems();
        for(Membro membro: Membro.buscarMembrosNaoDisciplinados()){
            //Disciplina
            boxMembroDisciplina.addItem(membro);
            boxMembroAlterarDisciplina.addItem(membro);
            boxMembroPerfilDisciplina.addItem(membro);
            //Consagracao
            boxMembroConsagracao.addItem(membro);
            boxMembroPerfilConsagracao.addItem(membro);
            boxMembroConsagracaoAlterar.addItem(membro);
            //Dizimo
            boxMembroDizimo.addItem(membro);
        
        }
        
        
        //Atualiza comboBox 'boxSetoresCaixa' do JPanel 'cadastroCaixa'
        boxSetoresCaixa.removeAllItems();
        for(Setores setor : Setores.setoresSemRegistroDeCaixa()){
            boxSetoresCaixa.addItem(setor);
        }
        boxSetorDespesa.removeAllItems();
        boxSetorOferta.removeAllItems();
        for(Setores setor : Setores.setoresComRegistroCaixa()){
            boxSetorOferta.addItem(setor);
            boxSetorDespesa.addItem(setor);
        }
        
        
        boxModoUsuario.removeAllItems();
        for(ModoUsuario modo: ModoUsuario.buscarModoUsuario()){
            boxModoUsuario.addItem(modo);
        }
        // Box Membro Usuario, Só permite membros Nao cadastrados como Usuarios
        boxMembroUsuario.removeAllItems();
        for(Membro membro: Membro.buscarMembrosNaoUsuarios()){
            boxMembroUsuario.addItem(membro);
        
        }
    
    }
    private void filtroTable(String filter, JTable table){
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> filtro = new TableRowSorter<>(modelo);
        table.setRowSorter(filtro);
        filtro.setRowFilter(RowFilter.regexFilter(filter));
        
    
    }
    
    
    
    
    
    
    
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuMembrosPop = new javax.swing.JPopupMenu();
        menuMembros = new javax.swing.JMenu();
        cadastrarMembro = new javax.swing.JMenuItem();
        menuDisciplina = new javax.swing.JMenu();
        cadastrarDisciplina = new javax.swing.JMenuItem();
        menuDesligamentos = new javax.swing.JMenu();
        cadastrarDesligamento = new javax.swing.JMenuItem();
        menuConsagracao = new javax.swing.JMenu();
        cadastrarConsagracao = new javax.swing.JMenuItem();
        menuFinancasPop = new javax.swing.JPopupMenu();
        menuCaixasSetores = new javax.swing.JMenu();
        cadastrarSetor = new javax.swing.JMenuItem();
        cadastrarCaixa = new javax.swing.JMenuItem();
        menuDespesas = new javax.swing.JMenu();
        cadastrarDespesa = new javax.swing.JMenuItem();
        menuOfertasEDizimos = new javax.swing.JMenu();
        cadastrarOferta = new javax.swing.JMenuItem();
        cadastrarDizimo = new javax.swing.JMenuItem();
        menuContasPop = new javax.swing.JPopupMenu();
        menuContas = new javax.swing.JMenu();
        cadastrarConta = new javax.swing.JMenuItem();
        menuSuaConta = new javax.swing.JMenuItem();
        logAcessos = new javax.swing.JMenuItem();
        menuRelatoriosPop = new javax.swing.JPopupMenu();
        relatoriosMembros = new javax.swing.JMenu();
        relatorioMembros = new javax.swing.JMenuItem();
        relatorioDesligamentos = new javax.swing.JMenuItem();
        relatorioConsagracoes = new javax.swing.JMenuItem();
        relatorioDisciplinas = new javax.swing.JMenuItem();
        relatoriosFinancas = new javax.swing.JMenu();
        relatorioCaixas = new javax.swing.JMenuItem();
        relatorioDizimos = new javax.swing.JMenuItem();
        relatorioDespesas = new javax.swing.JMenuItem();
        relatorioOfertas = new javax.swing.JMenuItem();
        relatoriosUsuarios = new javax.swing.JMenu();
        relatorioUsuarios = new javax.swing.JMenuItem();
        menuDocsPop = new javax.swing.JPopupMenu();
        carteiraMembro = new javax.swing.JMenuItem();
        main = new javax.swing.JPanel();
        menu = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        membroPanel = new javax.swing.JPanel();
        membroLabel = new javax.swing.JLabel();
        financasPanel = new javax.swing.JPanel();
        agendaLabel = new javax.swing.JLabel();
        relatoriosPanel = new javax.swing.JPanel();
        docsLabel = new javax.swing.JLabel();
        logoPanel = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        docsPanel = new javax.swing.JPanel();
        confLabel = new javax.swing.JLabel();
        contaPanel = new javax.swing.JPanel();
        contaLabel = new javax.swing.JLabel();
        ajudaPanel = new javax.swing.JPanel();
        ajudaLabel = new javax.swing.JLabel();
        sairPanel = new javax.swing.JPanel();
        sairLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        headerDesk = new javax.swing.JPanel();
        logoHeaderDesk = new javax.swing.JLabel();
        desk = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel156 = new javax.swing.JLabel();
        membros = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableMembrosDesligados = new javax.swing.JTable();
        jScrollPane15 = new javax.swing.JScrollPane();
        tableMembros = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tableMembrosDisciplinados = new javax.swing.JTable();
        jLabel32 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        filtroMembrosAtivos = new javax.swing.JFormattedTextField();
        filtroMembrosDisciplinados = new javax.swing.JFormattedTextField();
        jLabel40 = new javax.swing.JLabel();
        filtroMembrosDesligados = new javax.swing.JFormattedTextField();
        jLabel45 = new javax.swing.JLabel();
        cadastroMembros = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        nomeMembro = new javax.swing.JTextField();
        rgMembro = new javax.swing.JFormattedTextField();
        cpfMembro = new javax.swing.JFormattedTextField();
        telefoneMembro = new javax.swing.JFormattedTextField();
        enderecoMembro = new javax.swing.JTextField();
        dataBatismoMembro = new javax.swing.JFormattedTextField();
        dataNascimentoMembro = new javax.swing.JFormattedTextField();
        dataRegistroMembro = new javax.swing.JFormattedTextField();
        fotoMembro = new javax.swing.JLabel();
        btnConcluirMembro = new javax.swing.JButton();
        btnCancelarMembro = new javax.swing.JButton();
        btnLimparMembro = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        emailMembro = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        estadoCivilMembro = new javax.swing.JComboBox<>();
        dependentesMembro = new javax.swing.JComboBox<>();
        naturalidadeEstadoMembro = new javax.swing.JComboBox<>();
        naturalidadeMunicipioMembro = new javax.swing.JComboBox<>();
        perfilMembro = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        nomeMembroPerfil = new javax.swing.JTextField();
        rgMembroPerfil = new javax.swing.JFormattedTextField();
        cpfMembroPerfil = new javax.swing.JFormattedTextField();
        telefoneMembroPerfil = new javax.swing.JFormattedTextField();
        enderecoMembroPerfil = new javax.swing.JTextField();
        naturalidadeMembroPerfil = new javax.swing.JTextField();
        fotoMembroPerfil = new javax.swing.JLabel();
        btnAlterarMembro = new javax.swing.JButton();
        btnSairMembro = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        cargosMembroPerfil = new javax.swing.JComboBox<>();
        btnDisciplinarMembroPerfil = new javax.swing.JButton();
        btnExcluirMembroPerfil = new javax.swing.JButton();
        emailMembroPerfil = new javax.swing.JTextField();
        labe222 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        estadoCivilMembroPerfil = new javax.swing.JComboBox<>();
        btnExcluirMembroPerfil1 = new javax.swing.JButton();
        dataBatismoMembroPerfil = new javax.swing.JFormattedTextField();
        dataNascimentoMembroPerfil = new javax.swing.JFormattedTextField();
        dataRegistroMembroPerfil = new javax.swing.JFormattedTextField();
        dependentesMembroPerfil = new javax.swing.JComboBox<>();
        alterarMembro = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        nomeAlterarMembro = new javax.swing.JTextField();
        rgAlterarMembro = new javax.swing.JFormattedTextField();
        cpfAlterarMembro = new javax.swing.JFormattedTextField();
        telefoneAlterarMembro = new javax.swing.JFormattedTextField();
        enderecoAlterarMembro = new javax.swing.JTextField();
        dataBatismoAlterarMembro = new javax.swing.JFormattedTextField();
        dataNascimentoAlterarMembro = new javax.swing.JFormattedTextField();
        dataRegistroAlterarMembro = new javax.swing.JFormattedTextField();
        fotoAlterarMembro = new javax.swing.JLabel();
        btnConcluirAlterarMembro = new javax.swing.JButton();
        btnCancelarAlterarMembro = new javax.swing.JButton();
        javax.swing.JButton btnLimparAlterarMembro = new javax.swing.JButton();
        jLabel75 = new javax.swing.JLabel();
        emailAlterarMembro = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        estadoCivilAlterarMembro = new javax.swing.JComboBox<>();
        dependentesAlterarMembro = new javax.swing.JComboBox<>();
        naturalidadeEstadoAlterarMembro = new javax.swing.JComboBox<>();
        naturalidadeMunicipioAlterarMembro = new javax.swing.JComboBox<>();
        disciplinas = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDisciplina = new javax.swing.JTable();
        filtroCpfDisciplina = new javax.swing.JFormattedTextField();
        jLabel53 = new javax.swing.JLabel();
        cadastroDisciplina = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        dataInicio = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dscDisciplina = new javax.swing.JTextArea();
        btnConcluirDisciplina = new javax.swing.JToggleButton();
        btnCancelarDisciplina = new javax.swing.JToggleButton();
        btnLimparDisciplina = new javax.swing.JToggleButton();
        boxMembroDisciplina = new javax.swing.JComboBox<>();
        jLabel106 = new javax.swing.JLabel();
        nomeMembroDisciplina = new javax.swing.JTextField();
        fotoMembroDisciplina = new javax.swing.JLabel();
        dataFim = new javax.swing.JFormattedTextField();
        jLabel158 = new javax.swing.JLabel();
        perfilDisciplina = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        dscDisciplinaPerfil = new javax.swing.JTextArea();
        jLabel31 = new javax.swing.JLabel();
        btnAlterarDisciplinaPerfil = new javax.swing.JToggleButton();
        btnSairDisciplinaPerfil = new javax.swing.JToggleButton();
        boxMembroPerfilDisciplina = new javax.swing.JComboBox<>();
        jLabel107 = new javax.swing.JLabel();
        nomeMembroDisciplinaPerfil = new javax.swing.JTextField();
        dataDisciplinaPerfilInicio = new javax.swing.JTextField();
        fotoMembroDisciplinaPerfil = new javax.swing.JLabel();
        jLabel159 = new javax.swing.JLabel();
        dataDisciplinaPerfilFim = new javax.swing.JTextField();
        jLabel161 = new javax.swing.JLabel();
        dataDisciplinaPerfilRegistro = new javax.swing.JTextField();
        alterarDisciplina = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        dataAlterarDisciplinaInicio = new javax.swing.JFormattedTextField();
        jLabel56 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        dscAlterarDisciplina = new javax.swing.JTextArea();
        btnConcluirAlterarDisciplina = new javax.swing.JToggleButton();
        btnCancelarAlterarDisciplina = new javax.swing.JToggleButton();
        btnLimparAlterarDisciplina = new javax.swing.JToggleButton();
        boxMembroAlterarDisciplina = new javax.swing.JComboBox<>();
        jLabel111 = new javax.swing.JLabel();
        nomeMembroDisciplinaAlterar = new javax.swing.JTextField();
        fotoMembroDisciplinaAlterar = new javax.swing.JLabel();
        jLabel160 = new javax.swing.JLabel();
        dataAlterarDisciplinaFim = new javax.swing.JFormattedTextField();
        desligamentos = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableDesligamento = new javax.swing.JTable();
        filtroCpfDesligamentos = new javax.swing.JFormattedTextField();
        jLabel57 = new javax.swing.JLabel();
        cadastroDesligamento = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        dataDesligamento = new javax.swing.JFormattedTextField();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        dscDesligamento = new javax.swing.JTextArea();
        btnConcluirDesligamento = new javax.swing.JButton();
        btnCancelarDesligamento = new javax.swing.JButton();
        btnLimparDesligamento = new javax.swing.JButton();
        boxMembroDesligamento = new javax.swing.JComboBox<>();
        jLabel112 = new javax.swing.JLabel();
        nomeMembroDesligamento = new javax.swing.JTextField();
        fotoMembroDesligamento = new javax.swing.JLabel();
        perfilDesligamento = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        dscPerfilDesligamento = new javax.swing.JTextArea();
        btnAlterarDesligamentoPerfil = new javax.swing.JButton();
        btnSairDesligamentoPerfil = new javax.swing.JButton();
        boxMembroPerfilDesligamento = new javax.swing.JComboBox<>();
        jLabel114 = new javax.swing.JLabel();
        nomeMembroDesligamentoPerfil = new javax.swing.JTextField();
        fotoMembroDesligamentoPerfil = new javax.swing.JLabel();
        dataPerfilDesligamento = new javax.swing.JFormattedTextField();
        btnSairDesligamentoPerfil1 = new javax.swing.JButton();
        alterarDesligamento = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        dataAlterarDesligamento = new javax.swing.JFormattedTextField();
        jLabel60 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        dscAlterarDesligamento = new javax.swing.JTextArea();
        btnConcluirAlterarDesligamento = new javax.swing.JButton();
        btnCancelarAlterarDesligamento = new javax.swing.JButton();
        btnLimparAlterarDesligamento = new javax.swing.JButton();
        boxMembroAlterarDesligamento = new javax.swing.JComboBox<>();
        jLabel113 = new javax.swing.JLabel();
        nomeMembroDesligamentoAlterar = new javax.swing.JTextField();
        fotoMembroDesligamentoAlterar = new javax.swing.JLabel();
        consagracoes = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tableConsagracao = new javax.swing.JTable();
        filtroCpfConsagracoes = new javax.swing.JFormattedTextField();
        jLabel61 = new javax.swing.JLabel();
        cadastroConsagracao = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        dataConsagracao = new javax.swing.JFormattedTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        boxCargoConsagracao = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        dscConsagracao = new javax.swing.JTextArea();
        btnConcluirConsagracao = new javax.swing.JToggleButton();
        btnCancelarConsagracao = new javax.swing.JToggleButton();
        btnLimparConsagracao = new javax.swing.JToggleButton();
        boxMembroConsagracao = new javax.swing.JComboBox<>();
        jLabel115 = new javax.swing.JLabel();
        nomeMembroConsagracao = new javax.swing.JTextField();
        fotoMembroConsagracao = new javax.swing.JLabel();
        perfilConsagracao = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        dscConsagracaoPerfil = new javax.swing.JTextArea();
        btnAlterarConsagracaoPerfil = new javax.swing.JToggleButton();
        btnSairConsagracaoPerfil = new javax.swing.JToggleButton();
        boxMembroPerfilConsagracao = new javax.swing.JComboBox<>();
        boxCargosPerfilConsagracao = new javax.swing.JComboBox<>();
        jLabel116 = new javax.swing.JLabel();
        nomeMembroConsagracaoPerfil = new javax.swing.JTextField();
        dataConsagracaoPerfil = new javax.swing.JTextField();
        fotoMembroConsagracaoPerfil = new javax.swing.JLabel();
        alterarConsagracao = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        dataConsagracaoAlterar = new javax.swing.JFormattedTextField();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        boxCargosConsagracaoAlterar = new javax.swing.JComboBox<>();
        jLabel77 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        dscConsagracaoAlterar = new javax.swing.JTextArea();
        btnConcluirConsagracaoAlterar = new javax.swing.JToggleButton();
        btnCancelarConsagracaoAlterar = new javax.swing.JToggleButton();
        btnLimparConsagracaoAlterar = new javax.swing.JToggleButton();
        boxMembroConsagracaoAlterar = new javax.swing.JComboBox<>();
        jLabel117 = new javax.swing.JLabel();
        nomeMembroConsagracaoAlterar = new javax.swing.JTextField();
        fotoMembroConsagracaoAlterar = new javax.swing.JLabel();
        caixasESetores = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        tableCaixas = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tableSetores = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        filtroSetores = new javax.swing.JFormattedTextField();
        jLabel100 = new javax.swing.JLabel();
        filtroCaixas = new javax.swing.JFormattedTextField();
        jLabel101 = new javax.swing.JLabel();
        cadastroSetor = new javax.swing.JPanel();
        btnConcluirSetor = new javax.swing.JButton();
        btnCancelarSetor = new javax.swing.JButton();
        btnLimparSetor = new javax.swing.JButton();
        jLabel78 = new javax.swing.JLabel();
        nomeSetor = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        fotoSetor = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        dscSetor = new javax.swing.JTextArea();
        dataSetor = new javax.swing.JFormattedTextField();
        cadastroCaixa = new javax.swing.JPanel();
        btnLimparCaixa = new javax.swing.JButton();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        btnConcluirCaixas = new javax.swing.JButton();
        jScrollPane20 = new javax.swing.JScrollPane();
        dscCaixa = new javax.swing.JTextArea();
        btnCancelarMembro2 = new javax.swing.JButton();
        boxSetoresCaixa = new javax.swing.JComboBox<>();
        dataCaixa = new javax.swing.JFormattedTextField();
        jLabel84 = new javax.swing.JLabel();
        receitaCaixa = new javax.swing.JFormattedTextField();
        perfilCaixa = new javax.swing.JPanel();
        jScrollPane27 = new javax.swing.JScrollPane();
        tableCaixaDespesas = new javax.swing.JTable();
        jLabel105 = new javax.swing.JLabel();
        filtroCaixaDespesa = new javax.swing.JFormattedTextField();
        jLabel108 = new javax.swing.JLabel();
        jScrollPane29 = new javax.swing.JScrollPane();
        tableCaixaOfertas = new javax.swing.JTable();
        jLabel109 = new javax.swing.JLabel();
        filtroCaixaOferta = new javax.swing.JFormattedTextField();
        jLabel110 = new javax.swing.JLabel();
        despesas = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        tableDespesas = new javax.swing.JTable();
        jLabel86 = new javax.swing.JLabel();
        filtroDespesas = new javax.swing.JFormattedTextField();
        jLabel102 = new javax.swing.JLabel();
        cadastroDespesa = new javax.swing.JPanel();
        btnConcluirDepesa = new javax.swing.JButton();
        btnCancelarDespesa = new javax.swing.JButton();
        btnLimparDepesa = new javax.swing.JButton();
        jLabel85 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        dscDespesa = new javax.swing.JTextArea();
        dataDespesa = new javax.swing.JFormattedTextField();
        boxSetorDespesa = new javax.swing.JComboBox<>();
        valorDespesa = new javax.swing.JFormattedTextField();
        jLabel89 = new javax.swing.JLabel();
        ofertasEDizimos = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        tableOfertas = new javax.swing.JTable();
        jLabel90 = new javax.swing.JLabel();
        jScrollPane26 = new javax.swing.JScrollPane();
        tableDizimos = new javax.swing.JTable();
        jLabel99 = new javax.swing.JLabel();
        filtroDizimos = new javax.swing.JFormattedTextField();
        jLabel103 = new javax.swing.JLabel();
        filtroOfertas = new javax.swing.JFormattedTextField();
        jLabel104 = new javax.swing.JLabel();
        cadastroOferta = new javax.swing.JPanel();
        btnConcluirOferta = new javax.swing.JButton();
        btnCancelarOferta = new javax.swing.JButton();
        btnLimparOferta = new javax.swing.JButton();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        dscOferta = new javax.swing.JTextArea();
        dataOferta = new javax.swing.JFormattedTextField();
        boxSetorOferta = new javax.swing.JComboBox<>();
        valorOferta = new javax.swing.JFormattedTextField();
        jLabel94 = new javax.swing.JLabel();
        cadastroDizimo = new javax.swing.JPanel();
        btnConcluirDizimo = new javax.swing.JButton();
        btnCancelarDizimo = new javax.swing.JButton();
        btnLimparDizimo = new javax.swing.JButton();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
        dscDizimo = new javax.swing.JTextArea();
        dataDizimo = new javax.swing.JFormattedTextField();
        boxMembroDizimo = new javax.swing.JComboBox<>();
        valorDizimo = new javax.swing.JFormattedTextField();
        jLabel98 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        nomeMembroDizimo = new javax.swing.JTextField();
        usuarios = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        tableUsuarios = new javax.swing.JTable();
        jLabel134 = new javax.swing.JLabel();
        filtroUsuarios = new javax.swing.JFormattedTextField();
        jLabel135 = new javax.swing.JLabel();
        cadastroUsuario = new javax.swing.JPanel();
        btnConcluirMembro1 = new javax.swing.JButton();
        btnCancelarMembro1 = new javax.swing.JButton();
        btnLimparMembro1 = new javax.swing.JButton();
        jLabel145 = new javax.swing.JLabel();
        loginUsuarioConta = new javax.swing.JTextField();
        jLabel146 = new javax.swing.JLabel();
        senhaUsuarioConta = new javax.swing.JPasswordField();
        boxMembroUsuario = new javax.swing.JComboBox<>();
        jLabel147 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        nomeUsuario = new javax.swing.JTextField();
        boxModoUsuario = new javax.swing.JComboBox<>();
        jLabel149 = new javax.swing.JLabel();
        fotoUsuario = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        dataConta = new javax.swing.JFormattedTextField();
        perfilUsuario = new javax.swing.JPanel();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        nomeUsuarioPerfil = new javax.swing.JTextField();
        rgUsuario = new javax.swing.JFormattedTextField();
        cpfUsuario = new javax.swing.JFormattedTextField();
        telefoneUsuario = new javax.swing.JFormattedTextField();
        enderecoUsuario = new javax.swing.JTextField();
        naturalidadeUsuario = new javax.swing.JTextField();
        fotoUsuarioPerfil = new javax.swing.JLabel();
        btnAlterarMembro1 = new javax.swing.JButton();
        btnSairPerfilUsuario = new javax.swing.JButton();
        jLabel129 = new javax.swing.JLabel();
        boxModoUsuarioPerfil = new javax.swing.JComboBox<>();
        emailUsuario = new javax.swing.JTextField();
        labe223 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        boxEstadoCivilUsuario = new javax.swing.JComboBox<>();
        jLabel131 = new javax.swing.JLabel();
        loginUsuarioPerfil = new javax.swing.JTextField();
        jLabel132 = new javax.swing.JLabel();
        senhaUsuarioPerfil = new javax.swing.JPasswordField();
        jLabel133 = new javax.swing.JLabel();
        boxUsuarios = new javax.swing.JComboBox<>();
        dataBatismoUsuario = new javax.swing.JTextField();
        dataNascimentoUsuario = new javax.swing.JTextField();
        dataRegistroUsuario = new javax.swing.JTextField();
        dependentesUsuario = new javax.swing.JComboBox<>();
        alterarUsuario = new javax.swing.JPanel();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        jLabel144 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        nomeAlterarUsuario = new javax.swing.JTextField();
        rgAlterarUsuario = new javax.swing.JFormattedTextField();
        cpfAlterarUsuario = new javax.swing.JFormattedTextField();
        telefoneAlterarUsuario = new javax.swing.JFormattedTextField();
        enderecoAlterarUsuario = new javax.swing.JTextField();
        naturalidadeAlterarUsuario = new javax.swing.JTextField();
        dataBatismoAlterarUsuario = new javax.swing.JFormattedTextField();
        dataNascimentoAlterarUsuario = new javax.swing.JFormattedTextField();
        dataRegistroAlterarUsuario = new javax.swing.JFormattedTextField();
        fotoAlterarUsuario = new javax.swing.JLabel();
        btnConcluirAlterarUsuario = new javax.swing.JButton();
        btnCancelarAlterarUsuario = new javax.swing.JButton();
        javax.swing.JButton btnLimparAlterarUsuario = new javax.swing.JButton();
        jLabel152 = new javax.swing.JLabel();
        emailAlterarUsuario = new javax.swing.JTextField();
        jLabel153 = new javax.swing.JLabel();
        estadoCivilAlterarUsuario = new javax.swing.JComboBox<>();
        jLabel154 = new javax.swing.JLabel();
        loginAlterarUsuario = new javax.swing.JTextField();
        jLabel155 = new javax.swing.JLabel();
        novaSenhaAlterarUsuario = new javax.swing.JPasswordField();
        boxModoUsuarioAlterarUsuario = new javax.swing.JComboBox<>();
        jLabel157 = new javax.swing.JLabel();
        dependentesAlterarUsuario = new javax.swing.JComboBox<>();
        ajuda = new javax.swing.JPanel();
        jScrollPane30 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        logPanel = new javax.swing.JPanel();
        jLabel162 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAcessos = new javax.swing.JTable();
        jLabel163 = new javax.swing.JLabel();
        filtroTableAcessos = new javax.swing.JTextField();
        logAlteracoes = new javax.swing.JPanel();
        jLabel164 = new javax.swing.JLabel();
        jScrollPane31 = new javax.swing.JScrollPane();
        tableAlteracoes = new javax.swing.JTable();
        jLabel165 = new javax.swing.JLabel();
        filtroTableAlteracoes = new javax.swing.JTextField();

        menuMembrosPop.setBackground(new java.awt.Color(255, 255, 255));
        menuMembrosPop.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        menuMembrosPop.setAutoscrolls(true);
        menuMembrosPop.setBorder(null);

        menuMembros.setBackground(new java.awt.Color(255, 255, 255));
        menuMembros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/membros2.png"))); // NOI18N
        menuMembros.setText("Membros");
        menuMembros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuMembros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuMembrosMouseClicked(evt);
            }
        });

        cadastrarMembro.setBackground(new java.awt.Color(255, 255, 255));
        cadastrarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/adicionar.png"))); // NOI18N
        cadastrarMembro.setText("Cadastrar Membro");
        cadastrarMembro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarMembroActionPerformed(evt);
            }
        });
        menuMembros.add(cadastrarMembro);

        menuMembrosPop.add(menuMembros);

        menuDisciplina.setBackground(new java.awt.Color(255, 255, 255));
        menuDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/disciplina.png"))); // NOI18N
        menuDisciplina.setText("Disciplinas");
        menuDisciplina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuDisciplinaMouseClicked(evt);
            }
        });

        cadastrarDisciplina.setBackground(new java.awt.Color(255, 255, 255));
        cadastrarDisciplina.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/disciplinar32.png"))); // NOI18N
        cadastrarDisciplina.setText("Registrar Disciplina");
        cadastrarDisciplina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarDisciplinaActionPerformed(evt);
            }
        });
        menuDisciplina.add(cadastrarDisciplina);

        menuMembrosPop.add(menuDisciplina);

        menuDesligamentos.setBackground(new java.awt.Color(255, 255, 255));
        menuDesligamentos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluirMembro.png"))); // NOI18N
        menuDesligamentos.setText("Desligamentos");
        menuDesligamentos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuDesligamentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuDesligamentosMouseClicked(evt);
            }
        });

        cadastrarDesligamento.setBackground(new java.awt.Color(255, 255, 255));
        cadastrarDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarDesligamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluirMembro.png"))); // NOI18N
        cadastrarDesligamento.setText("Registrar Desligamento");
        cadastrarDesligamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarDesligamentoActionPerformed(evt);
            }
        });
        menuDesligamentos.add(cadastrarDesligamento);

        menuMembrosPop.add(menuDesligamentos);

        menuConsagracao.setBackground(new java.awt.Color(255, 255, 255));
        menuConsagracao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/consagrar.png"))); // NOI18N
        menuConsagracao.setText("Consagracoes");
        menuConsagracao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuConsagracao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuConsagracaoMouseClicked(evt);
            }
        });

        cadastrarConsagracao.setBackground(new java.awt.Color(255, 255, 255));
        cadastrarConsagracao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarConsagracao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/casagração.png"))); // NOI18N
        cadastrarConsagracao.setText("Registrar Consagração");
        cadastrarConsagracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarConsagracaoActionPerformed(evt);
            }
        });
        menuConsagracao.add(cadastrarConsagracao);

        menuMembrosPop.add(menuConsagracao);

        menuCaixasSetores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/caixaeOfertas.png"))); // NOI18N
        menuCaixasSetores.setText("Caixas e Setores");
        menuCaixasSetores.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuCaixasSetores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuCaixasSetoresMouseClicked(evt);
            }
        });

        cadastrarSetor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarSetor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/setor.png"))); // NOI18N
        cadastrarSetor.setText("Cadastrar Setor");
        cadastrarSetor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarSetorActionPerformed(evt);
            }
        });
        menuCaixasSetores.add(cadastrarSetor);

        cadastrarCaixa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarCaixa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/caixa.png"))); // NOI18N
        cadastrarCaixa.setText("Cadastrar Caixa");
        cadastrarCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarCaixaActionPerformed(evt);
            }
        });
        menuCaixasSetores.add(cadastrarCaixa);

        menuFinancasPop.add(menuCaixasSetores);

        menuDespesas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/despesa.png"))); // NOI18N
        menuDespesas.setText("Despesas");
        menuDespesas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuDespesas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuDespesasMouseClicked(evt);
            }
        });

        cadastrarDespesa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarDespesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/registrarDespesa.png"))); // NOI18N
        cadastrarDespesa.setText("Cadastrar Despesa");
        cadastrarDespesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarDespesaActionPerformed(evt);
            }
        });
        menuDespesas.add(cadastrarDespesa);

        menuFinancasPop.add(menuDespesas);

        menuOfertasEDizimos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ofertas.png"))); // NOI18N
        menuOfertasEDizimos.setText("Ofertas e Dizimos");
        menuOfertasEDizimos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuOfertasEDizimos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuOfertasEDizimosMouseClicked(evt);
            }
        });

        cadastrarOferta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarOferta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/oferta.png"))); // NOI18N
        cadastrarOferta.setText("Cadastrar Oferta");
        cadastrarOferta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarOfertaActionPerformed(evt);
            }
        });
        menuOfertasEDizimos.add(cadastrarOferta);

        cadastrarDizimo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarDizimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/dizimo.png"))); // NOI18N
        cadastrarDizimo.setText("Cadastrar Dizimo");
        cadastrarDizimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarDizimoActionPerformed(evt);
            }
        });
        menuOfertasEDizimos.add(cadastrarDizimo);

        menuFinancasPop.add(menuOfertasEDizimos);

        menuContas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/menuContas.png"))); // NOI18N
        menuContas.setText("Contas de Usuarios");
        menuContas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuContas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuContasMouseClicked(evt);
            }
        });

        cadastrarConta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarConta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/adicionar.png"))); // NOI18N
        cadastrarConta.setText("Cadastrar Conta");
        cadastrarConta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarContaActionPerformed(evt);
            }
        });
        menuContas.add(cadastrarConta);

        menuContasPop.add(menuContas);

        menuSuaConta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuSuaConta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/user.png"))); // NOI18N
        menuSuaConta.setText("Sua Conta");
        menuSuaConta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSuaContaActionPerformed(evt);
            }
        });
        menuContasPop.add(menuSuaConta);

        logAcessos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        logAcessos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/acessos.png"))); // NOI18N
        logAcessos.setText("Log de Acessos");
        logAcessos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logAcessosActionPerformed(evt);
            }
        });
        menuContasPop.add(logAcessos);

        relatoriosMembros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/membros2.png"))); // NOI18N
        relatoriosMembros.setText("Membros");
        relatoriosMembros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        relatorioMembros.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioMembros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/membros2.png"))); // NOI18N
        relatorioMembros.setText("Relatório Membros");
        relatorioMembros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioMembrosActionPerformed(evt);
            }
        });
        relatoriosMembros.add(relatorioMembros);

        relatorioDesligamentos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioDesligamentos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluirMembro.png"))); // NOI18N
        relatorioDesligamentos.setText("Relatorio Desligamentos");
        relatorioDesligamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioDesligamentosActionPerformed(evt);
            }
        });
        relatoriosMembros.add(relatorioDesligamentos);

        relatorioConsagracoes.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioConsagracoes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/consagrar.png"))); // NOI18N
        relatorioConsagracoes.setText("Relatorio de Consagrações");
        relatorioConsagracoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioConsagracoesActionPerformed(evt);
            }
        });
        relatoriosMembros.add(relatorioConsagracoes);

        relatorioDisciplinas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioDisciplinas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/disciplinar32.png"))); // NOI18N
        relatorioDisciplinas.setText("Relatório Disciplinas");
        relatorioDisciplinas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioDisciplinasActionPerformed(evt);
            }
        });
        relatoriosMembros.add(relatorioDisciplinas);

        menuRelatoriosPop.add(relatoriosMembros);

        relatoriosFinancas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/financas32.png"))); // NOI18N
        relatoriosFinancas.setText("Finanças");
        relatoriosFinancas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        relatorioCaixas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioCaixas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/caixa.png"))); // NOI18N
        relatorioCaixas.setText("Relatorio de Caixa");
        relatorioCaixas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioCaixasActionPerformed(evt);
            }
        });
        relatoriosFinancas.add(relatorioCaixas);

        relatorioDizimos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioDizimos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/dizimo.png"))); // NOI18N
        relatorioDizimos.setText("Relatório Dízimos");
        relatorioDizimos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioDizimosActionPerformed(evt);
            }
        });
        relatoriosFinancas.add(relatorioDizimos);

        relatorioDespesas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioDespesas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/despesa.png"))); // NOI18N
        relatorioDespesas.setText("Relatório Despesas");
        relatorioDespesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioDespesasActionPerformed(evt);
            }
        });
        relatoriosFinancas.add(relatorioDespesas);

        relatorioOfertas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioOfertas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/oferta.png"))); // NOI18N
        relatorioOfertas.setText("Relatório Ofertas");
        relatorioOfertas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioOfertasActionPerformed(evt);
            }
        });
        relatoriosFinancas.add(relatorioOfertas);

        menuRelatoriosPop.add(relatoriosFinancas);

        relatoriosUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/contaConf32.png"))); // NOI18N
        relatoriosUsuarios.setText("Usuários");
        relatoriosUsuarios.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        relatorioUsuarios.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        relatorioUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/user.png"))); // NOI18N
        relatorioUsuarios.setText("Relatório Usuários");
        relatorioUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioUsuariosActionPerformed(evt);
            }
        });
        relatoriosUsuarios.add(relatorioUsuarios);

        menuRelatoriosPop.add(relatoriosUsuarios);

        carteiraMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        carteiraMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/carteirinha.png"))); // NOI18N
        carteiraMembro.setText("Carteirinha de Membro");
        carteiraMembro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carteiraMembroActionPerformed(evt);
            }
        });
        menuDocsPop.add(carteiraMembro);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CMS-Church Management System");
        setLocationByPlatform(true);
        setPreferredSize(new java.awt.Dimension(1182, 755));
        setResizable(false);

        main.setBackground(new java.awt.Color(51, 102, 255));
        main.setPreferredSize(new java.awt.Dimension(1182, 738));

        menu.setBackground(new java.awt.Color(51, 102, 255));
        menu.setAutoscrolls(true);
        menu.setPreferredSize(new java.awt.Dimension(263, 730));

        jSeparator2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        membroPanel.setBackground(new java.awt.Color(255, 255, 255));
        membroPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        membroPanel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        membroPanel.setPreferredSize(new java.awt.Dimension(280, 85));
        membroPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                membroPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                membroPanelMouseExited(evt);
            }
        });

        membroLabel.setBackground(new java.awt.Color(255, 255, 255));
        membroLabel.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N
        membroLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/membros.png"))); // NOI18N
        membroLabel.setText("Membros");

        javax.swing.GroupLayout membroPanelLayout = new javax.swing.GroupLayout(membroPanel);
        membroPanel.setLayout(membroPanelLayout);
        membroPanelLayout.setHorizontalGroup(
            membroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(membroLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
        );
        membroPanelLayout.setVerticalGroup(
            membroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(membroLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        financasPanel.setBackground(new java.awt.Color(255, 255, 255));
        financasPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        financasPanel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        financasPanel.setPreferredSize(new java.awt.Dimension(280, 85));
        financasPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                financasPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                financasPanelMouseExited(evt);
            }
        });

        agendaLabel.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N
        agendaLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/financas.png"))); // NOI18N
        agendaLabel.setText("Finanças");

        javax.swing.GroupLayout financasPanelLayout = new javax.swing.GroupLayout(financasPanel);
        financasPanel.setLayout(financasPanelLayout);
        financasPanelLayout.setHorizontalGroup(
            financasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(agendaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        financasPanelLayout.setVerticalGroup(
            financasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(agendaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        relatoriosPanel.setBackground(new java.awt.Color(255, 255, 255));
        relatoriosPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        relatoriosPanel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        relatoriosPanel.setPreferredSize(new java.awt.Dimension(280, 85));
        relatoriosPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                relatoriosPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                relatoriosPanelMouseExited(evt);
            }
        });

        docsLabel.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N
        docsLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/documentos.png"))); // NOI18N
        docsLabel.setText("Relatórios");

        javax.swing.GroupLayout relatoriosPanelLayout = new javax.swing.GroupLayout(relatoriosPanel);
        relatoriosPanel.setLayout(relatoriosPanelLayout);
        relatoriosPanelLayout.setHorizontalGroup(
            relatoriosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(relatoriosPanelLayout.createSequentialGroup()
                .addComponent(docsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
        relatoriosPanelLayout.setVerticalGroup(
            relatoriosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(docsLabel)
        );

        logoPanel.setBackground(new java.awt.Color(255, 255, 255));
        logoPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logoPanel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        logoPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoPanelMouseEntered(evt);
            }
        });

        logoLabel.setBackground(new java.awt.Color(255, 255, 255));
        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo.jpg"))); // NOI18N

        javax.swing.GroupLayout logoPanelLayout = new javax.swing.GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        logoPanelLayout.setVerticalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoLabel, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        docsPanel.setBackground(new java.awt.Color(255, 255, 255));
        docsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        docsPanel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        docsPanel.setPreferredSize(new java.awt.Dimension(170, 50));
        docsPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                docsPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                docsPanelMouseExited(evt);
            }
        });

        confLabel.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N
        confLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/pdfDoc.png"))); // NOI18N
        confLabel.setText("Documentos");

        javax.swing.GroupLayout docsPanelLayout = new javax.swing.GroupLayout(docsPanel);
        docsPanel.setLayout(docsPanelLayout);
        docsPanelLayout.setHorizontalGroup(
            docsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(docsPanelLayout.createSequentialGroup()
                .addComponent(confLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
        docsPanelLayout.setVerticalGroup(
            docsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(confLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        contaPanel.setBackground(new java.awt.Color(255, 255, 255));
        contaPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        contaPanel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        contaPanel.setPreferredSize(new java.awt.Dimension(170, 50));
        contaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contaPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                contaPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                contaPanelMouseExited(evt);
            }
        });

        contaLabel.setFont(new java.awt.Font("Arial", 3, 18)); // NOI18N
        contaLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/contaConf.png"))); // NOI18N
        contaLabel.setText("Usuários");

        javax.swing.GroupLayout contaPanelLayout = new javax.swing.GroupLayout(contaPanel);
        contaPanel.setLayout(contaPanelLayout);
        contaPanelLayout.setHorizontalGroup(
            contaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contaPanelLayout.createSequentialGroup()
                .addComponent(contaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        contaPanelLayout.setVerticalGroup(
            contaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, Short.MAX_VALUE)
        );

        ajudaPanel.setBackground(new java.awt.Color(255, 255, 255));
        ajudaPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ajudaPanel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        ajudaPanel.setPreferredSize(new java.awt.Dimension(170, 50));
        ajudaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ajudaPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ajudaPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ajudaPanelMouseExited(evt);
            }
        });

        ajudaLabel.setBackground(new java.awt.Color(255, 255, 255));
        ajudaLabel.setFont(new java.awt.Font("Arial", 3, 18)); // NOI18N
        ajudaLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ajuda.png"))); // NOI18N
        ajudaLabel.setText("Ajuda ?");

        javax.swing.GroupLayout ajudaPanelLayout = new javax.swing.GroupLayout(ajudaPanel);
        ajudaPanel.setLayout(ajudaPanelLayout);
        ajudaPanelLayout.setHorizontalGroup(
            ajudaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ajudaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
        );
        ajudaPanelLayout.setVerticalGroup(
            ajudaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ajudaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        sairPanel.setBackground(new java.awt.Color(255, 255, 255));
        sairPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sairPanel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        sairPanel.setPreferredSize(new java.awt.Dimension(170, 50));
        sairPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sairPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sairPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sairPanelMouseExited(evt);
            }
        });

        sairLabel.setBackground(new java.awt.Color(255, 255, 255));
        sairLabel.setFont(new java.awt.Font("Arial", 3, 18)); // NOI18N
        sairLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/iconOut.png"))); // NOI18N
        sairLabel.setText("Sair");

        javax.swing.GroupLayout sairPanelLayout = new javax.swing.GroupLayout(sairPanel);
        sairPanel.setLayout(sairPanelLayout);
        sairPanelLayout.setHorizontalGroup(
            sairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sairLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sairPanelLayout.setVerticalGroup(
            sairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sairLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSeparator3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(membroPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(relatoriosPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addComponent(financasPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                    .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(docsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(sairPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .addComponent(ajudaPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .addComponent(contaPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(menuLayout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(membroPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(financasPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(relatoriosPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(docsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ajudaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sairPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        headerDesk.setBackground(new java.awt.Color(255, 255, 255));
        headerDesk.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        logoHeaderDesk.setFont(new java.awt.Font("Arial", 3, 48)); // NOI18N
        logoHeaderDesk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/bibli.png"))); // NOI18N

        javax.swing.GroupLayout headerDeskLayout = new javax.swing.GroupLayout(headerDesk);
        headerDesk.setLayout(headerDeskLayout);
        headerDeskLayout.setHorizontalGroup(
            headerDeskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerDeskLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(logoHeaderDesk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerDeskLayout.setVerticalGroup(
            headerDeskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoHeaderDesk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
        );

        desk.setBackground(new java.awt.Color(153, 153, 153));
        desk.setAutoscrolls(true);
        desk.setPreferredSize(new java.awt.Dimension(900, 570));
        desk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deskMouseEntered(evt);
            }
        });
        desk.setLayout(new java.awt.CardLayout());

        home.setBackground(new java.awt.Color(255, 255, 255));
        home.setPreferredSize(new java.awt.Dimension(900, 570));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("CMS - Church Management System é um Sistema desenvolvido e distribuído por @SYNMCALL. \n\t\t@Copyright Todos os Direitos Reservados.");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(null);
        jTextArea1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTextArea1.setDisabledTextColor(new java.awt.Color(255, 0, 255));

        jLabel156.setFont(new java.awt.Font("Arial", 2, 36)); // NOI18N
        jLabel156.setText("Bem vindo ao CMS - Church Management System");

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel156))
                    .addGroup(homeLayout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 659, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel156)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 454, Short.MAX_VALUE)
                .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        desk.add(home, "card5");

        membros.setBackground(new java.awt.Color(255, 255, 255));
        membros.setAutoscrolls(true);
        membros.setPreferredSize(new java.awt.Dimension(900, 570));

        tableMembrosDesligados.setAutoCreateRowSorter(true);
        tableMembrosDesligados.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableMembrosDesligados.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableMembrosDesligados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CPF", "Nome", "RG", "Naturalidade", "Estado Civil", "Data Nascimento", "Data Batismo", "Data Cadastro", "Numero Dependentes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableMembrosDesligados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableMembrosDesligados.setGridColor(new java.awt.Color(51, 102, 255));
        tableMembrosDesligados.setRowHeight(30);
        tableMembrosDesligados.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tableMembrosDesligados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMembrosDesligadosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableMembrosDesligados);
        tableMembrosDesligados.setSize(120,30);

        tableMembros.setAutoCreateRowSorter(true);
        tableMembros.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableMembros.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableMembros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CPF", "Nome", "RG", "Naturalidade", "Estado Civil", "Data Nascimento", "Data Batismo", "Data Cadastro", "Numero Dependentes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableMembros.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableMembros.setGridColor(new java.awt.Color(51, 102, 255));
        tableMembros.setRowHeight(30);
        tableMembros.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tableMembros.setVerifyInputWhenFocusTarget(false);
        tableMembros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMembrosMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tableMembrosMouseExited(evt);
            }
        });
        jScrollPane15.setViewportView(tableMembros);

        jLabel24.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel24.setText("Membros Ativos:");

        jLabel30.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel30.setText("Membros Desligados:");

        tableMembrosDisciplinados.setAutoCreateRowSorter(true);
        tableMembrosDisciplinados.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableMembrosDisciplinados.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableMembrosDisciplinados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CPF", "Nome", "RG", "Naturalidade", "Estado Civil", "Data Nascimento", "Data Batismo", "Data Cadastro", "Numero Dependentes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableMembrosDisciplinados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableMembrosDisciplinados.setGridColor(new java.awt.Color(51, 102, 255));
        tableMembrosDisciplinados.setRowHeight(30);
        tableMembrosDisciplinados.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tableMembrosDisciplinados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMembrosDisciplinadosMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tableMembrosDisciplinados);

        jLabel32.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel32.setText("Membros Disciplinados:");

        jLabel36.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel36.setText("Filtro:");

        filtroMembrosAtivos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroMembrosAtivos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroMembrosAtivosKeyReleased(evt);
            }
        });

        filtroMembrosDisciplinados.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroMembrosDisciplinados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroMembrosDisciplinadosKeyReleased(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel40.setText("Filtro:");

        filtroMembrosDesligados.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroMembrosDesligados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroMembrosDesligadosKeyReleased(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel45.setText("Filtro:");

        javax.swing.GroupLayout membrosLayout = new javax.swing.GroupLayout(membros);
        membros.setLayout(membrosLayout);
        membrosLayout.setHorizontalGroup(
            membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(membrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(membrosLayout.createSequentialGroup()
                        .addGroup(membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane16)
                            .addGroup(membrosLayout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(filtroMembrosAtivos, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42))
                            .addComponent(jScrollPane15)
                            .addComponent(jScrollPane3))
                        .addContainerGap())
                    .addGroup(membrosLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 389, Short.MAX_VALUE)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filtroMembrosDisciplinados, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))
                    .addGroup(membrosLayout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filtroMembrosDesligados, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))))
        );
        membrosLayout.setVerticalGroup(
            membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, membrosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel36)
                    .addComponent(filtroMembrosAtivos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32)
                    .addGroup(membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel40)
                        .addComponent(filtroMembrosDisciplinados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(membrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel45)
                        .addComponent(filtroMembrosDesligados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        desk.add(membros, "card2");

        cadastroMembros.setBackground(new java.awt.Color(255, 255, 255));
        cadastroMembros.setToolTipText("");
        cadastroMembros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cadastroMembros.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Nome:");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("RG:");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("CPF:");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Telefone:");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Endereço:");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Naturalidade:");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Numero Dependentes:");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("Data Batismo:");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Data Nascimento:");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setText("Data Registro:");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nomeMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            rgMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        rgMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            cpfMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cpfMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            telefoneMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telefoneMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        enderecoMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataBatismoMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataBatismoMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataNascimentoMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataNascimentoMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataRegistroMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataRegistroMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembro.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembro.setMinimumSize(new java.awt.Dimension(256, 180));
        fotoMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoMembroMouseClicked(evt);
            }
        });

        btnConcluirMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirMembro.setText("Concluir");
        btnConcluirMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirMembroMouseClicked(evt);
            }
        });

        btnCancelarMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarMembro.setText("Cancelar");
        btnCancelarMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarMembroMouseClicked(evt);
            }
        });

        btnLimparMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparMembro.setText("Limpar");
        btnLimparMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparMembroMouseClicked(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel50.setText("Email:");
        jLabel50.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel50.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        emailMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel51.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel51.setText("Estado Civil:");
        jLabel51.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel51.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        estadoCivilMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dependentesMembro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        dependentesMembro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));

        naturalidadeEstadoMembro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                naturalidadeEstadoMembroFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                naturalidadeEstadoMembroFocusLost(evt);
            }
        });
        naturalidadeEstadoMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                naturalidadeEstadoMembroMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                naturalidadeEstadoMembroMousePressed(evt);
            }
        });

        naturalidadeMunicipioMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                naturalidadeMunicipioMembroMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout cadastroMembrosLayout = new javax.swing.GroupLayout(cadastroMembros);
        cadastroMembros.setLayout(cadastroMembrosLayout);
        cadastroMembrosLayout.setHorizontalGroup(
            cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroMembrosLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroMembrosLayout.createSequentialGroup()
                        .addComponent(btnConcluirMembro)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarMembro)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparMembro))
                    .addGroup(cadastroMembrosLayout.createSequentialGroup()
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(cadastroMembrosLayout.createSequentialGroup()
                                .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel50))
                                .addGap(21, 21, 21)
                                .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(enderecoMembro)
                                    .addComponent(emailMembro)))
                            .addGroup(cadastroMembrosLayout.createSequentialGroup()
                                .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3))
                                .addGap(27, 27, 27)
                                .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nomeMembro)
                                    .addComponent(rgMembro)
                                    .addComponent(cpfMembro)
                                    .addComponent(telefoneMembro, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))))
                        .addGap(82, 82, 82)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel51))
                        .addGap(35, 35, 35)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dataRegistroMembro)
                                .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dataBatismoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dataNascimentoMembro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(estadoCivilMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dependentesMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(cadastroMembrosLayout.createSequentialGroup()
                                .addComponent(naturalidadeEstadoMembro, 0, 127, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(naturalidadeMunicipioMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(fotoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        cadastroMembrosLayout.setVerticalGroup(
            cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroMembrosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroMembrosLayout.createSequentialGroup()
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nomeMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rgMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpfMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(telefoneMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(11, 11, 11)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(enderecoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(emailMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cadastroMembrosLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(naturalidadeEstadoMembro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(naturalidadeMunicipioMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cadastroMembrosLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel8))
                            .addGroup(cadastroMembrosLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dependentesMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataBatismoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataNascimentoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataRegistroMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(estadoCivilMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(40, 40, 40)
                .addGroup(cadastroMembrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarMembro)
                    .addComponent(btnConcluirMembro)
                    .addComponent(btnLimparMembro))
                .addContainerGap())
        );

        desk.add(cadastroMembros, "card6");

        perfilMembro.setBackground(new java.awt.Color(255, 255, 255));
        perfilMembro.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Nome:");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setText("RG:");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("CPF:");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setText("Telefone:");
        jLabel14.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel15.setText("Endereço:");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setText("Naturalidade");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel17.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel17.setText("Numero Dependentes:");
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel18.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel18.setText("Data Batismo:");
        jLabel18.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel19.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel19.setText("Data Nascimento:");
        jLabel19.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel20.setText("Data Registro:");
        jLabel20.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nomeMembroPerfil.setEditable(false);
        nomeMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        rgMembroPerfil.setEditable(false);
        try {
            rgMembroPerfil.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        rgMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        cpfMembroPerfil.setEditable(false);
        try {
            cpfMembroPerfil.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cpfMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        telefoneMembroPerfil.setEditable(false);
        try {
            telefoneMembroPerfil.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telefoneMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        enderecoMembroPerfil.setEditable(false);
        enderecoMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        naturalidadeMembroPerfil.setEditable(false);
        naturalidadeMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/setor_1.png"))); // NOI18N
        fotoMembroPerfil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnAlterarMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAlterarMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/editar.png"))); // NOI18N
        btnAlterarMembro.setText("Alterar");
        btnAlterarMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAlterarMembroMouseClicked(evt);
            }
        });

        btnSairMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSairMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/iconVoltar.png"))); // NOI18N
        btnSairMembro.setText("Sair");
        btnSairMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSairMembroMouseClicked(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel21.setText("Cargo:");
        jLabel21.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        cargosMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cargosMembroPerfil.setEnabled(false);

        btnDisciplinarMembroPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDisciplinarMembroPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/iconDisciplina.png"))); // NOI18N
        btnDisciplinarMembroPerfil.setText("Disciplinar");
        btnDisciplinarMembroPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDisciplinarMembroPerfilMouseClicked(evt);
            }
        });

        btnExcluirMembroPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnExcluirMembroPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluirMembro64.png"))); // NOI18N
        btnExcluirMembroPerfil.setText("Desligar");
        btnExcluirMembroPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExcluirMembroPerfilMouseClicked(evt);
            }
        });

        emailMembroPerfil.setEditable(false);
        emailMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        labe222.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labe222.setText("Email:");
        labe222.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        labe222.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel52.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel52.setText("Estado Civil:");
        jLabel52.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel52.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        estadoCivilMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        estadoCivilMembroPerfil.setEnabled(false);

        btnExcluirMembroPerfil1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnExcluirMembroPerfil1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/consagrar64.png"))); // NOI18N
        btnExcluirMembroPerfil1.setText("Consagrar");
        btnExcluirMembroPerfil1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExcluirMembroPerfil1MouseClicked(evt);
            }
        });

        dataBatismoMembroPerfil.setEditable(false);
        try {
            dataBatismoMembroPerfil.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataBatismoMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dataNascimentoMembroPerfil.setEditable(false);
        try {
            dataNascimentoMembroPerfil.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataNascimentoMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dataRegistroMembroPerfil.setEditable(false);
        try {
            dataRegistroMembroPerfil.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataRegistroMembroPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dependentesMembroPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        dependentesMembroPerfil.setEnabled(false);

        javax.swing.GroupLayout perfilMembroLayout = new javax.swing.GroupLayout(perfilMembro);
        perfilMembro.setLayout(perfilMembroLayout);
        perfilMembroLayout.setHorizontalGroup(
            perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilMembroLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(perfilMembroLayout.createSequentialGroup()
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(perfilMembroLayout.createSequentialGroup()
                                .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel12))
                                .addGap(27, 27, 27)
                                .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nomeMembroPerfil)
                                    .addComponent(rgMembroPerfil)
                                    .addComponent(cpfMembroPerfil)
                                    .addComponent(telefoneMembroPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                            .addGroup(perfilMembroLayout.createSequentialGroup()
                                .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel21)
                                    .addComponent(labe222))
                                .addGap(21, 21, 21)
                                .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(enderecoMembroPerfil)
                                    .addComponent(emailMembroPerfil)
                                    .addComponent(cargosMembroPerfil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(82, 82, 82)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel52))
                        .addGap(35, 35, 35)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(estadoCivilMembroPerfil, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dataRegistroMembroPerfil, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dataBatismoMembroPerfil, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                .addComponent(dataNascimentoMembroPerfil, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                .addComponent(dependentesMembroPerfil, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(naturalidadeMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(perfilMembroLayout.createSequentialGroup()
                        .addComponent(btnAlterarMembro)
                        .addGap(18, 18, 18)
                        .addComponent(btnSairMembro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDisciplinarMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnExcluirMembroPerfil)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluirMembroPerfil1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        perfilMembroLayout.setVerticalGroup(
            perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, perfilMembroLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(fotoMembroPerfil)
                .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(perfilMembroLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(naturalidadeMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(dependentesMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(perfilMembroLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(6, 6, 6)
                                .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(perfilMembroLayout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(jLabel19))
                                    .addGroup(perfilMembroLayout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addComponent(jLabel20)))
                                .addGap(21, 21, 21)
                                .addComponent(jLabel52))
                            .addGroup(perfilMembroLayout.createSequentialGroup()
                                .addComponent(dataBatismoMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dataNascimentoMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dataRegistroMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(estadoCivilMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(perfilMembroLayout.createSequentialGroup()
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nomeMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rgMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpfMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(telefoneMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(11, 11, 11)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(enderecoMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labe222))
                        .addGap(11, 11, 11)
                        .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(cargosMembroPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(29, 29, 29)
                .addGroup(perfilMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAlterarMembro)
                    .addComponent(btnSairMembro)
                    .addComponent(btnDisciplinarMembroPerfil)
                    .addComponent(btnExcluirMembroPerfil)
                    .addComponent(btnExcluirMembroPerfil1))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        desk.add(perfilMembro, "card6");

        alterarMembro.setBackground(new java.awt.Color(255, 255, 255));
        alterarMembro.setToolTipText("");
        alterarMembro.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel65.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel65.setText("Nome:");
        jLabel65.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel65.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel66.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel66.setText("RG:");
        jLabel66.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel66.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel67.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel67.setText("CPF:");
        jLabel67.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel67.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel68.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel68.setText("Telefone:");
        jLabel68.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel68.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel69.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel69.setText("Endereço:");
        jLabel69.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel69.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel70.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel70.setText("Naturalidade");
        jLabel70.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel70.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel71.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel71.setText("Numero Dependentes:");
        jLabel71.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel71.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel72.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel72.setText("Data Batismo:");
        jLabel72.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel72.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel73.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel73.setText("Data Nascimento:");
        jLabel73.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel73.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel74.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel74.setText("Data Registro:");
        jLabel74.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel74.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nomeAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            rgAlterarMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        rgAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        cpfAlterarMembro.setEditable(false);
        try {
            cpfAlterarMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cpfAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            telefoneAlterarMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telefoneAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        enderecoAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataBatismoAlterarMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataBatismoAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataNascimentoAlterarMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataNascimentoAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataRegistroAlterarMembro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataRegistroAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoAlterarMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoAlterarMembro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoAlterarMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoAlterarMembroMouseClicked(evt);
            }
        });

        btnConcluirAlterarMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirAlterarMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirAlterarMembro.setText("Concluir");
        btnConcluirAlterarMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirAlterarMembroMouseClicked(evt);
            }
        });

        btnCancelarAlterarMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarAlterarMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarAlterarMembro.setText("Cancelar");
        btnCancelarAlterarMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarAlterarMembroMouseClicked(evt);
            }
        });

        btnLimparAlterarMembro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparAlterarMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparAlterarMembro.setText("Limpar");
        btnLimparAlterarMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparAlterarMembroMouseClicked(evt);
            }
        });

        jLabel75.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel75.setText("Email:");
        jLabel75.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel75.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        emailAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel76.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel76.setText("Estado Civil:");
        jLabel76.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel76.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        estadoCivilAlterarMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dependentesAlterarMembro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));

        naturalidadeEstadoAlterarMembro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                naturalidadeEstadoAlterarMembroFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                naturalidadeEstadoAlterarMembroFocusLost(evt);
            }
        });
        naturalidadeEstadoAlterarMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                naturalidadeEstadoAlterarMembroMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                naturalidadeEstadoAlterarMembroMousePressed(evt);
            }
        });

        naturalidadeMunicipioAlterarMembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                naturalidadeMunicipioAlterarMembroMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout alterarMembroLayout = new javax.swing.GroupLayout(alterarMembro);
        alterarMembro.setLayout(alterarMembroLayout);
        alterarMembroLayout.setHorizontalGroup(
            alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alterarMembroLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarMembroLayout.createSequentialGroup()
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(alterarMembroLayout.createSequentialGroup()
                                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel65)
                                    .addComponent(jLabel67)
                                    .addComponent(jLabel68)
                                    .addComponent(jLabel66))
                                .addGap(27, 27, 27)
                                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nomeAlterarMembro)
                                    .addComponent(rgAlterarMembro)
                                    .addComponent(cpfAlterarMembro)
                                    .addComponent(telefoneAlterarMembro, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                            .addGroup(alterarMembroLayout.createSequentialGroup()
                                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel69)
                                    .addComponent(jLabel75))
                                .addGap(21, 21, 21)
                                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(enderecoAlterarMembro)
                                    .addComponent(emailAlterarMembro)))
                            .addComponent(fotoAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel71)
                            .addComponent(jLabel72)
                            .addComponent(jLabel73)
                            .addComponent(jLabel74)
                            .addComponent(jLabel76)
                            .addComponent(jLabel70))
                        .addGap(35, 35, 35)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(alterarMembroLayout.createSequentialGroup()
                                .addComponent(naturalidadeEstadoAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(naturalidadeMunicipioAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dataRegistroAlterarMembro)
                                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dataBatismoAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dataNascimentoAlterarMembro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(estadoCivilAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dependentesAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(alterarMembroLayout.createSequentialGroup()
                        .addComponent(btnConcluirAlterarMembro)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarAlterarMembro)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparAlterarMembro)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        alterarMembroLayout.setVerticalGroup(
            alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alterarMembroLayout.createSequentialGroup()
                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarMembroLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(fotoAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nomeAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rgAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel66))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpfAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel67))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(telefoneAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel68))
                        .addGap(10, 10, 10)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(enderecoAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel69)))
                    .addGroup(alterarMembroLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel70, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(naturalidadeEstadoAlterarMembro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(naturalidadeMunicipioAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel71)
                            .addComponent(dependentesAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataBatismoAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel72))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataNascimentoAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel73))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataRegistroAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel74))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel75)
                        .addComponent(emailAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel76)
                    .addComponent(estadoCivilAlterarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(alterarMembroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarAlterarMembro)
                    .addComponent(btnConcluirAlterarMembro)
                    .addComponent(btnLimparAlterarMembro))
                .addGap(41, 41, 41))
        );

        desk.add(alterarMembro, "card6");

        disciplinas.setBackground(new java.awt.Color(255, 255, 255));
        disciplinas.setPreferredSize(new java.awt.Dimension(900, 570));

        tableDisciplina.setAutoCreateRowSorter(true);
        tableDisciplina.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableDisciplina.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CPF", "Data Inicio", "Data Fim", "Data Registro", "Descrição"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDisciplina.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableDisciplina.setGridColor(new java.awt.Color(51, 102, 255));
        tableDisciplina.setRequestFocusEnabled(false);
        tableDisciplina.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tableDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDisciplinaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableDisciplina);

        filtroCpfDisciplina.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroCpfDisciplina.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroCpfDisciplinaKeyReleased(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel53.setText("Filtro:");

        javax.swing.GroupLayout disciplinasLayout = new javax.swing.GroupLayout(disciplinas);
        disciplinas.setLayout(disciplinasLayout);
        disciplinasLayout.setHorizontalGroup(
            disciplinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(disciplinasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(disciplinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addGroup(disciplinasLayout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filtroCpfDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        disciplinasLayout.setVerticalGroup(
            disciplinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, disciplinasLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(disciplinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filtroCpfDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        desk.add(disciplinas, "card8");

        cadastroDisciplina.setBackground(new java.awt.Color(255, 255, 255));
        cadastroDisciplina.setPreferredSize(new java.awt.Dimension(900, 570));
        cadastroDisciplina.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cadastroDisciplinaMouseMoved(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel25.setText("CPF Membro:");

        jLabel26.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel26.setText("Data Inicio:");

        try {
            dataInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataInicio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel27.setText("Descrição:");

        dscDisciplina.setColumns(20);
        dscDisciplina.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscDisciplina.setRows(5);
        dscDisciplina.setPreferredSize(new java.awt.Dimension(220, 90));
        dscDisciplina.setSelectionColor(new java.awt.Color(51, 102, 255));
        jScrollPane4.setViewportView(dscDisciplina);

        btnConcluirDisciplina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirDisciplina.setText("Concluir");
        btnConcluirDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirDisciplinaMouseClicked(evt);
            }
        });

        btnCancelarDisciplina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarDisciplina.setText("Cancelar");
        btnCancelarDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarDisciplinaMouseClicked(evt);
            }
        });

        btnLimparDisciplina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparDisciplina.setText("Limpar");
        btnLimparDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparDisciplinaMouseClicked(evt);
            }
        });

        boxMembroDisciplina.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel106.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel106.setText("Nome Membro:");

        nomeMembroDisciplina.setEditable(false);
        nomeMembroDisciplina.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroDisciplina.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroDisciplina.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroDisciplina.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroDisciplina.setMinimumSize(new java.awt.Dimension(256, 180));
        fotoMembroDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoMembroDisciplinaMouseClicked(evt);
            }
        });

        try {
            dataFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataFim.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel158.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel158.setText("Data Fim:");

        javax.swing.GroupLayout cadastroDisciplinaLayout = new javax.swing.GroupLayout(cadastroDisciplina);
        cadastroDisciplina.setLayout(cadastroDisciplinaLayout);
        cadastroDisciplinaLayout.setHorizontalGroup(
            cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroDisciplinaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoMembroDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cadastroDisciplinaLayout.createSequentialGroup()
                        .addComponent(btnConcluirDisciplina)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarDisciplina)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparDisciplina))
                    .addGroup(cadastroDisciplinaLayout.createSequentialGroup()
                        .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26))
                        .addGap(18, 18, 18)
                        .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boxMembroDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cadastroDisciplinaLayout.createSequentialGroup()
                                .addComponent(jLabel158)
                                .addGap(18, 18, 18)
                                .addComponent(dataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(cadastroDisciplinaLayout.createSequentialGroup()
                                .addComponent(jLabel106)
                                .addGap(18, 18, 18)
                                .addComponent(nomeMembroDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(cadastroDisciplinaLayout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 42, Short.MAX_VALUE))
        );
        cadastroDisciplinaLayout.setVerticalGroup(
            cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroDisciplinaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(boxMembroDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel106)
                    .addComponent(nomeMembroDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel158)
                        .addComponent(dataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26)
                        .addComponent(dataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cadastroDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirDisciplina)
                    .addComponent(btnCancelarDisciplina)
                    .addComponent(btnLimparDisciplina))
                .addGap(33, 33, 33))
        );

        desk.add(cadastroDisciplina, "card10");

        perfilDisciplina.setBackground(new java.awt.Color(255, 255, 255));
        perfilDisciplina.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel28.setText("Data Inicio:");

        jLabel29.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel29.setText("Descrição:");

        dscDisciplinaPerfil.setEditable(false);
        dscDisciplinaPerfil.setColumns(20);
        dscDisciplinaPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscDisciplinaPerfil.setLineWrap(true);
        dscDisciplinaPerfil.setRows(5);
        dscDisciplinaPerfil.setWrapStyleWord(true);
        jScrollPane5.setViewportView(dscDisciplinaPerfil);

        jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel31.setText("CPF Membro:");

        btnAlterarDisciplinaPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAlterarDisciplinaPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/editar.png"))); // NOI18N
        btnAlterarDisciplinaPerfil.setText("Alterar");
        btnAlterarDisciplinaPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAlterarDisciplinaPerfilMouseClicked(evt);
            }
        });

        btnSairDisciplinaPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSairDisciplinaPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/iconVoltar.png"))); // NOI18N
        btnSairDisciplinaPerfil.setText("Sair");
        btnSairDisciplinaPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSairDisciplinaPerfilMouseClicked(evt);
            }
        });

        boxMembroPerfilDisciplina.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxMembroPerfilDisciplina.setEnabled(false);

        jLabel107.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel107.setText("Nome Membro:");

        nomeMembroDisciplinaPerfil.setEditable(false);
        nomeMembroDisciplinaPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dataDisciplinaPerfilInicio.setEditable(false);
        dataDisciplinaPerfilInicio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroDisciplinaPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroDisciplinaPerfil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroDisciplinaPerfil.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroDisciplinaPerfil.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroDisciplinaPerfil.setMinimumSize(new java.awt.Dimension(256, 180));
        fotoMembroDisciplinaPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoMembroDisciplinaPerfilMouseClicked(evt);
            }
        });

        jLabel159.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel159.setText("Data Fim:");

        dataDisciplinaPerfilFim.setEditable(false);
        dataDisciplinaPerfilFim.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel161.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel161.setText("Data Registro:");

        dataDisciplinaPerfilRegistro.setEditable(false);
        dataDisciplinaPerfilRegistro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout perfilDisciplinaLayout = new javax.swing.GroupLayout(perfilDisciplina);
        perfilDisciplina.setLayout(perfilDisciplinaLayout);
        perfilDisciplinaLayout.setHorizontalGroup(
            perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilDisciplinaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(perfilDisciplinaLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(perfilDisciplinaLayout.createSequentialGroup()
                        .addComponent(btnAlterarDisciplinaPerfil)
                        .addGap(18, 18, 18)
                        .addComponent(btnSairDisciplinaPerfil))
                    .addGroup(perfilDisciplinaLayout.createSequentialGroup()
                        .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(perfilDisciplinaLayout.createSequentialGroup()
                                .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(jLabel28))
                                .addGap(18, 18, 18)
                                .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(boxMembroPerfilDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dataDisciplinaPerfilInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(fotoMembroDisciplinaPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel107)
                            .addComponent(jLabel159))
                        .addGap(18, 18, 18)
                        .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nomeMembroDisciplinaPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(perfilDisciplinaLayout.createSequentialGroup()
                                .addComponent(dataDisciplinaPerfilFim, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel161)
                                .addGap(18, 18, 18)
                                .addComponent(dataDisciplinaPerfilRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 36, Short.MAX_VALUE))
        );
        perfilDisciplinaLayout.setVerticalGroup(
            perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilDisciplinaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroDisciplinaPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel107)
                        .addComponent(nomeMembroDisciplinaPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(boxMembroPerfilDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel31)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel161)
                        .addComponent(dataDisciplinaPerfilRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel159)
                        .addComponent(dataDisciplinaPerfilFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel28)
                        .addComponent(dataDisciplinaPerfilInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(perfilDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAlterarDisciplinaPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSairDisciplinaPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        desk.add(perfilDisciplina, "card9");

        alterarDisciplina.setBackground(new java.awt.Color(255, 255, 255));
        alterarDisciplina.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel54.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel54.setText("CPF Membro:");

        jLabel55.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel55.setText("Data Inicio:");

        try {
            dataAlterarDisciplinaInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataAlterarDisciplinaInicio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel56.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel56.setText("Descrição:");

        dscAlterarDisciplina.setColumns(20);
        dscAlterarDisciplina.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscAlterarDisciplina.setRows(5);
        jScrollPane12.setViewportView(dscAlterarDisciplina);

        btnConcluirAlterarDisciplina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirAlterarDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirAlterarDisciplina.setText("Concluir");
        btnConcluirAlterarDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirAlterarDisciplinaMouseClicked(evt);
            }
        });

        btnCancelarAlterarDisciplina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarAlterarDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarAlterarDisciplina.setText("Cancelar");
        btnCancelarAlterarDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarAlterarDisciplinaMouseClicked(evt);
            }
        });

        btnLimparAlterarDisciplina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparAlterarDisciplina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparAlterarDisciplina.setText("Limpar");
        btnLimparAlterarDisciplina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparAlterarDisciplinaMouseClicked(evt);
            }
        });

        boxMembroAlterarDisciplina.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxMembroAlterarDisciplina.setEnabled(false);

        jLabel111.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel111.setText("Nome Membro:");

        nomeMembroDisciplinaAlterar.setEditable(false);
        nomeMembroDisciplinaAlterar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroDisciplinaAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroDisciplinaAlterar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroDisciplinaAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroDisciplinaAlterar.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroDisciplinaAlterar.setMinimumSize(new java.awt.Dimension(256, 180));
        fotoMembroDisciplinaAlterar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoMembroDisciplinaAlterarMouseClicked(evt);
            }
        });

        jLabel160.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel160.setText("Data Fim:");

        try {
            dataAlterarDisciplinaFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataAlterarDisciplinaFim.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout alterarDisciplinaLayout = new javax.swing.GroupLayout(alterarDisciplina);
        alterarDisciplina.setLayout(alterarDisciplinaLayout);
        alterarDisciplinaLayout.setHorizontalGroup(
            alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alterarDisciplinaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarDisciplinaLayout.createSequentialGroup()
                        .addComponent(jLabel56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alterarDisciplinaLayout.createSequentialGroup()
                        .addComponent(btnConcluirAlterarDisciplina)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelarAlterarDisciplina)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLimparAlterarDisciplina))
                    .addGroup(alterarDisciplinaLayout.createSequentialGroup()
                        .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(alterarDisciplinaLayout.createSequentialGroup()
                                .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel54)
                                    .addComponent(jLabel55))
                                .addGap(18, 18, 18)
                                .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(boxMembroAlterarDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dataAlterarDisciplinaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(fotoMembroDisciplinaAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(alterarDisciplinaLayout.createSequentialGroup()
                                .addComponent(jLabel160)
                                .addGap(18, 18, 18)
                                .addComponent(dataAlterarDisciplinaFim, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(alterarDisciplinaLayout.createSequentialGroup()
                                .addComponent(jLabel111)
                                .addGap(18, 18, 18)
                                .addComponent(nomeMembroDisciplinaAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 36, Short.MAX_VALUE))
        );
        alterarDisciplinaLayout.setVerticalGroup(
            alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alterarDisciplinaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroDisciplinaAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel111)
                            .addComponent(nomeMembroDisciplinaAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(boxMembroAlterarDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel54))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel160)
                        .addComponent(dataAlterarDisciplinaFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel55)
                        .addComponent(dataAlterarDisciplinaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(alterarDisciplinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirAlterarDisciplina)
                    .addComponent(btnCancelarAlterarDisciplina)
                    .addComponent(btnLimparAlterarDisciplina))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        desk.add(alterarDisciplina, "card10");

        desligamentos.setBackground(new java.awt.Color(255, 255, 255));

        tableDesligamento.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableDesligamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CPF", "Data", "Descrição"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDesligamento.setGridColor(new java.awt.Color(51, 102, 255));
        tableDesligamento.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tableDesligamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDesligamentoMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tableDesligamento);
        if (tableDesligamento.getColumnModel().getColumnCount() > 0) {
            tableDesligamento.getColumnModel().getColumn(0).setResizable(false);
        }

        filtroCpfDesligamentos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroCpfDesligamentos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroCpfDesligamentosKeyReleased(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel57.setText("Filtro:");

        javax.swing.GroupLayout desligamentosLayout = new javax.swing.GroupLayout(desligamentos);
        desligamentos.setLayout(desligamentosLayout);
        desligamentosLayout.setHorizontalGroup(
            desligamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(desligamentosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(desligamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
                    .addGroup(desligamentosLayout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filtroCpfDesligamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        desligamentosLayout.setVerticalGroup(
            desligamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, desligamentosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(desligamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filtroCpfDesligamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        desk.add(desligamentos, "card12");

        cadastroDesligamento.setBackground(new java.awt.Color(255, 255, 255));
        cadastroDesligamento.setPreferredSize(new java.awt.Dimension(900, 570));
        cadastroDesligamento.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cadastroDesligamentoMouseMoved(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel33.setText("CPF Membro:");

        jLabel34.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel34.setText("Data Desligamento:");

        try {
            dataDesligamento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel35.setText("Descrição:");

        dscDesligamento.setColumns(20);
        dscDesligamento.setRows(5);
        jScrollPane6.setViewportView(dscDesligamento);

        btnConcluirDesligamento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirDesligamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirDesligamento.setText("Concluir");
        btnConcluirDesligamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirDesligamentoMouseClicked(evt);
            }
        });

        btnCancelarDesligamento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarDesligamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarDesligamento.setText("Cancelar");
        btnCancelarDesligamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarDesligamentoMouseClicked(evt);
            }
        });

        btnLimparDesligamento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparDesligamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparDesligamento.setText("Limpar");
        btnLimparDesligamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparDesligamentoMouseClicked(evt);
            }
        });

        boxMembroDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel112.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel112.setText("Nome Membro:");

        nomeMembroDesligamento.setEditable(false);
        nomeMembroDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroDesligamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroDesligamento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroDesligamento.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroDesligamento.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroDesligamento.setMinimumSize(new java.awt.Dimension(256, 180));

        javax.swing.GroupLayout cadastroDesligamentoLayout = new javax.swing.GroupLayout(cadastroDesligamento);
        cadastroDesligamento.setLayout(cadastroDesligamentoLayout);
        cadastroDesligamentoLayout.setHorizontalGroup(
            cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroDesligamentoLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoMembroDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cadastroDesligamentoLayout.createSequentialGroup()
                        .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel34))
                        .addGap(18, 18, 18)
                        .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dataDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(cadastroDesligamentoLayout.createSequentialGroup()
                                .addComponent(boxMembroDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel112)
                                .addGap(18, 18, 18)
                                .addComponent(nomeMembroDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(cadastroDesligamentoLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroDesligamentoLayout.createSequentialGroup()
                        .addComponent(btnConcluirDesligamento)
                        .addGap(29, 29, 29)
                        .addComponent(btnCancelarDesligamento)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparDesligamento)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        cadastroDesligamentoLayout.setVerticalGroup(
            cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroDesligamentoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel112)
                            .addComponent(nomeMembroDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(boxMembroDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cadastroDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirDesligamento)
                    .addComponent(btnCancelarDesligamento)
                    .addComponent(btnLimparDesligamento))
                .addGap(30, 30, 30))
        );

        desk.add(cadastroDesligamento, "card10");

        perfilDesligamento.setBackground(new java.awt.Color(255, 255, 255));
        perfilDesligamento.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel37.setText("CPF Membro:");

        jLabel38.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel38.setText("Data Desligamento:");

        jLabel39.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel39.setText("Descrição:");

        dscPerfilDesligamento.setEditable(false);
        dscPerfilDesligamento.setColumns(20);
        dscPerfilDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscPerfilDesligamento.setRows(5);
        jScrollPane8.setViewportView(dscPerfilDesligamento);

        btnAlterarDesligamentoPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAlterarDesligamentoPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/editar.png"))); // NOI18N
        btnAlterarDesligamentoPerfil.setText("Alterar");
        btnAlterarDesligamentoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAlterarDesligamentoPerfilMouseClicked(evt);
            }
        });

        btnSairDesligamentoPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSairDesligamentoPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/iconVoltar.png"))); // NOI18N
        btnSairDesligamentoPerfil.setText("Sair");
        btnSairDesligamentoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSairDesligamentoPerfilMouseClicked(evt);
            }
        });

        boxMembroPerfilDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxMembroPerfilDesligamento.setEnabled(false);

        jLabel114.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel114.setText("Nome Membro:");

        nomeMembroDesligamentoPerfil.setEditable(false);
        nomeMembroDesligamentoPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroDesligamentoPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroDesligamentoPerfil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroDesligamentoPerfil.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroDesligamentoPerfil.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroDesligamentoPerfil.setMinimumSize(new java.awt.Dimension(256, 180));

        dataPerfilDesligamento.setEditable(false);
        try {
            dataPerfilDesligamento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataPerfilDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        btnSairDesligamentoPerfil1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSairDesligamentoPerfil1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/adicionar.png"))); // NOI18N
        btnSairDesligamentoPerfil1.setText("Religar Membro");
        btnSairDesligamentoPerfil1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSairDesligamentoPerfil1MouseClicked(evt);
            }
        });
        btnSairDesligamentoPerfil1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairDesligamentoPerfil1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout perfilDesligamentoLayout = new javax.swing.GroupLayout(perfilDesligamento);
        perfilDesligamento.setLayout(perfilDesligamentoLayout);
        perfilDesligamentoLayout.setHorizontalGroup(
            perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilDesligamentoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnSairDesligamentoPerfil1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlterarDesligamentoPerfil)
                .addGap(22, 22, 22)
                .addComponent(btnSairDesligamentoPerfil)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, perfilDesligamentoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoMembroDesligamentoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(perfilDesligamentoLayout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(perfilDesligamentoLayout.createSequentialGroup()
                        .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(jLabel37))
                        .addGap(18, 18, 18)
                        .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boxMembroPerfilDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dataPerfilDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52)
                        .addComponent(jLabel114)
                        .addGap(18, 18, 18)
                        .addComponent(nomeMembroDesligamentoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        perfilDesligamentoLayout.setVerticalGroup(
            perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilDesligamentoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroDesligamentoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel114)
                            .addComponent(nomeMembroDesligamentoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(boxMembroPerfilDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(dataPerfilDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(perfilDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btnSairDesligamentoPerfil1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSairDesligamentoPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterarDesligamentoPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        desk.add(perfilDesligamento, "card10");

        alterarDesligamento.setBackground(new java.awt.Color(255, 255, 255));
        alterarDesligamento.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel58.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel58.setText("CPF Membro:");

        jLabel59.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel59.setText("Data Desligamento:");

        try {
            dataAlterarDesligamento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataAlterarDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel60.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel60.setText("Descrição:");

        dscAlterarDesligamento.setColumns(20);
        dscAlterarDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscAlterarDesligamento.setRows(5);
        jScrollPane13.setViewportView(dscAlterarDesligamento);

        btnConcluirAlterarDesligamento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirAlterarDesligamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirAlterarDesligamento.setText("Concluir");
        btnConcluirAlterarDesligamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirAlterarDesligamentoMouseClicked(evt);
            }
        });

        btnCancelarAlterarDesligamento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarAlterarDesligamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarAlterarDesligamento.setText("Cancelar");
        btnCancelarAlterarDesligamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarAlterarDesligamentoMouseClicked(evt);
            }
        });

        btnLimparAlterarDesligamento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparAlterarDesligamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparAlterarDesligamento.setText("Limpar");
        btnLimparAlterarDesligamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparAlterarDesligamentoMouseClicked(evt);
            }
        });

        boxMembroAlterarDesligamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxMembroAlterarDesligamento.setEnabled(false);

        jLabel113.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel113.setText("Nome Membro:");

        nomeMembroDesligamentoAlterar.setEditable(false);
        nomeMembroDesligamentoAlterar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroDesligamentoAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroDesligamentoAlterar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroDesligamentoAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroDesligamentoAlterar.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroDesligamentoAlterar.setMinimumSize(new java.awt.Dimension(256, 180));

        javax.swing.GroupLayout alterarDesligamentoLayout = new javax.swing.GroupLayout(alterarDesligamento);
        alterarDesligamento.setLayout(alterarDesligamentoLayout);
        alterarDesligamentoLayout.setHorizontalGroup(
            alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alterarDesligamentoLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoMembroDesligamentoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(alterarDesligamentoLayout.createSequentialGroup()
                        .addComponent(btnConcluirAlterarDesligamento)
                        .addGap(29, 29, 29)
                        .addComponent(btnCancelarAlterarDesligamento)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparAlterarDesligamento))
                    .addGroup(alterarDesligamentoLayout.createSequentialGroup()
                        .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58)
                            .addComponent(jLabel59))
                        .addGap(18, 18, 18)
                        .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dataAlterarDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(alterarDesligamentoLayout.createSequentialGroup()
                                .addComponent(boxMembroAlterarDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel113)
                                .addGap(18, 18, 18)
                                .addComponent(nomeMembroDesligamentoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(alterarDesligamentoLayout.createSequentialGroup()
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
        );
        alterarDesligamentoLayout.setVerticalGroup(
            alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alterarDesligamentoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroDesligamentoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel113)
                        .addComponent(nomeMembroDesligamentoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel58)
                        .addComponent(boxMembroAlterarDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(dataAlterarDesligamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alterarDesligamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirAlterarDesligamento)
                    .addComponent(btnCancelarAlterarDesligamento)
                    .addComponent(btnLimparAlterarDesligamento))
                .addContainerGap())
        );

        desk.add(alterarDesligamento, "card10");

        consagracoes.setBackground(new java.awt.Color(255, 255, 255));
        consagracoes.setPreferredSize(new java.awt.Dimension(900, 570));

        jScrollPane9.setBackground(new java.awt.Color(102, 255, 255));

        tableConsagracao.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableConsagracao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CPF", "Cargo", "Data", "Descrição"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableConsagracao.setGridColor(new java.awt.Color(51, 102, 255));
        tableConsagracao.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tableConsagracao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableConsagracaoMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tableConsagracao);

        filtroCpfConsagracoes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroCpfConsagracoes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroCpfConsagracoesKeyReleased(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel61.setText("Filtro:");

        javax.swing.GroupLayout consagracoesLayout = new javax.swing.GroupLayout(consagracoes);
        consagracoes.setLayout(consagracoesLayout);
        consagracoesLayout.setHorizontalGroup(
            consagracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consagracoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(consagracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(consagracoesLayout.createSequentialGroup()
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filtroCpfConsagracoes, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 659, Short.MAX_VALUE))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        consagracoesLayout.setVerticalGroup(
            consagracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consagracoesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(consagracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filtroCpfConsagracoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addContainerGap())
        );

        desk.add(consagracoes, "card16");

        cadastroConsagracao.setBackground(new java.awt.Color(255, 255, 255));
        cadastroConsagracao.setPreferredSize(new java.awt.Dimension(900, 570));
        cadastroConsagracao.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cadastroConsagracaoMouseMoved(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel41.setText("CPF Membro:");

        try {
            dataConsagracao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataConsagracao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel42.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel42.setText("Data Consagração:");

        jLabel43.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel43.setText("Cargo Consagração:");

        boxCargoConsagracao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel44.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel44.setText("Descrição");

        dscConsagracao.setColumns(20);
        dscConsagracao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscConsagracao.setRows(5);
        jScrollPane10.setViewportView(dscConsagracao);

        btnConcluirConsagracao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirConsagracao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirConsagracao.setText("Concluir");
        btnConcluirConsagracao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirConsagracaoMouseClicked(evt);
            }
        });

        btnCancelarConsagracao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarConsagracao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarConsagracao.setText("Cancelar");
        btnCancelarConsagracao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarConsagracaoMouseClicked(evt);
            }
        });

        btnLimparConsagracao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparConsagracao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparConsagracao.setText("Limpar");
        btnLimparConsagracao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparConsagracaoMouseClicked(evt);
            }
        });

        boxMembroConsagracao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel115.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel115.setText("Nome Membro:");

        nomeMembroConsagracao.setEditable(false);
        nomeMembroConsagracao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroConsagracao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroConsagracao.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroConsagracao.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroConsagracao.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroConsagracao.setMinimumSize(new java.awt.Dimension(256, 180));

        javax.swing.GroupLayout cadastroConsagracaoLayout = new javax.swing.GroupLayout(cadastroConsagracao);
        cadastroConsagracao.setLayout(cadastroConsagracaoLayout);
        cadastroConsagracaoLayout.setHorizontalGroup(
            cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroConsagracaoLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoMembroConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cadastroConsagracaoLayout.createSequentialGroup()
                        .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41)
                            .addComponent(jLabel42)
                            .addComponent(jLabel43))
                        .addGap(18, 18, 18)
                        .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dataConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boxMembroConsagracao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(boxCargoConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68)
                        .addComponent(jLabel115)
                        .addGap(18, 18, 18)
                        .addComponent(nomeMembroConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroConsagracaoLayout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroConsagracaoLayout.createSequentialGroup()
                        .addComponent(btnConcluirConsagracao)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarConsagracao)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparConsagracao)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        cadastroConsagracaoLayout.setVerticalGroup(
            cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroConsagracaoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel115)
                        .addComponent(nomeMembroConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel41)
                        .addComponent(boxMembroConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(boxCargoConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirConsagracao)
                    .addComponent(btnCancelarConsagracao)
                    .addComponent(btnLimparConsagracao))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        desk.add(cadastroConsagracao, "card15");

        perfilConsagracao.setBackground(new java.awt.Color(255, 255, 255));
        perfilConsagracao.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel46.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel46.setText("CPF Membro:");

        jLabel47.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel47.setText("Data Consagração:");

        jLabel48.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel48.setText("Cargo:");

        jLabel49.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel49.setText("Descrição:");

        dscConsagracaoPerfil.setEditable(false);
        dscConsagracaoPerfil.setColumns(20);
        dscConsagracaoPerfil.setRows(5);
        jScrollPane11.setViewportView(dscConsagracaoPerfil);

        btnAlterarConsagracaoPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAlterarConsagracaoPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/editar.png"))); // NOI18N
        btnAlterarConsagracaoPerfil.setText("Alterar");
        btnAlterarConsagracaoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAlterarConsagracaoPerfilMouseClicked(evt);
            }
        });

        btnSairConsagracaoPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSairConsagracaoPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/iconVoltar.png"))); // NOI18N
        btnSairConsagracaoPerfil.setText("Sair");
        btnSairConsagracaoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSairConsagracaoPerfilMouseClicked(evt);
            }
        });

        boxMembroPerfilConsagracao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxMembroPerfilConsagracao.setEnabled(false);

        boxCargosPerfilConsagracao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxCargosPerfilConsagracao.setEnabled(false);

        jLabel116.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel116.setText("Nome Membro:");

        nomeMembroConsagracaoPerfil.setEditable(false);
        nomeMembroConsagracaoPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dataConsagracaoPerfil.setEditable(false);
        dataConsagracaoPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroConsagracaoPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroConsagracaoPerfil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroConsagracaoPerfil.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroConsagracaoPerfil.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroConsagracaoPerfil.setMinimumSize(new java.awt.Dimension(256, 180));

        javax.swing.GroupLayout perfilConsagracaoLayout = new javax.swing.GroupLayout(perfilConsagracao);
        perfilConsagracao.setLayout(perfilConsagracaoLayout);
        perfilConsagracaoLayout.setHorizontalGroup(
            perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilConsagracaoLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoMembroConsagracaoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(perfilConsagracaoLayout.createSequentialGroup()
                        .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47)
                            .addComponent(jLabel48))
                        .addGap(90, 90, 90)
                        .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(boxMembroPerfilConsagracao, 0, 160, Short.MAX_VALUE)
                            .addComponent(boxCargosPerfilConsagracao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataConsagracaoPerfil))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel116)
                        .addGap(18, 18, 18)
                        .addComponent(nomeMembroConsagracaoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(perfilConsagracaoLayout.createSequentialGroup()
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(perfilConsagracaoLayout.createSequentialGroup()
                        .addComponent(btnAlterarConsagracaoPerfil)
                        .addGap(18, 18, 18)
                        .addComponent(btnSairConsagracaoPerfil)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        perfilConsagracaoLayout.setVerticalGroup(
            perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilConsagracaoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroConsagracaoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel116)
                        .addComponent(nomeMembroConsagracaoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(boxMembroPerfilConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel46)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(dataConsagracaoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(boxCargosPerfilConsagracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(perfilConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAlterarConsagracaoPerfil)
                    .addComponent(btnSairConsagracaoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        desk.add(perfilConsagracao, "card15");

        alterarConsagracao.setBackground(new java.awt.Color(255, 255, 255));
        alterarConsagracao.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel62.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel62.setText("CPF Membro:");

        try {
            dataConsagracaoAlterar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataConsagracaoAlterar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel63.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel63.setText("Data Consagração:");

        jLabel64.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel64.setText("Cargo:");

        boxCargosConsagracaoAlterar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel77.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel77.setText("Descrição");

        dscConsagracaoAlterar.setColumns(20);
        dscConsagracaoAlterar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscConsagracaoAlterar.setRows(5);
        jScrollPane14.setViewportView(dscConsagracaoAlterar);

        btnConcluirConsagracaoAlterar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirConsagracaoAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirConsagracaoAlterar.setText("Concluir");
        btnConcluirConsagracaoAlterar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirConsagracaoAlterarMouseClicked(evt);
            }
        });

        btnCancelarConsagracaoAlterar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarConsagracaoAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarConsagracaoAlterar.setText("Cancelar");
        btnCancelarConsagracaoAlterar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarConsagracaoAlterarMouseClicked(evt);
            }
        });

        btnLimparConsagracaoAlterar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparConsagracaoAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparConsagracaoAlterar.setText("Limpar");
        btnLimparConsagracaoAlterar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparConsagracaoAlterarMouseClicked(evt);
            }
        });

        boxMembroConsagracaoAlterar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxMembroConsagracaoAlterar.setEnabled(false);

        jLabel117.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel117.setText("Nome Membro:");

        nomeMembroConsagracaoAlterar.setEditable(false);
        nomeMembroConsagracaoAlterar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoMembroConsagracaoAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoMembroConsagracaoAlterar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoMembroConsagracaoAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoMembroConsagracaoAlterar.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoMembroConsagracaoAlterar.setMinimumSize(new java.awt.Dimension(256, 180));

        javax.swing.GroupLayout alterarConsagracaoLayout = new javax.swing.GroupLayout(alterarConsagracao);
        alterarConsagracao.setLayout(alterarConsagracaoLayout);
        alterarConsagracaoLayout.setHorizontalGroup(
            alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alterarConsagracaoLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoMembroConsagracaoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(alterarConsagracaoLayout.createSequentialGroup()
                        .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel63)
                            .addComponent(jLabel64))
                        .addGap(18, 18, 18)
                        .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dataConsagracaoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(boxCargosConsagracaoAlterar, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(boxMembroConsagracaoAlterar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(111, 111, 111)
                        .addComponent(jLabel117)
                        .addGap(18, 18, 18)
                        .addComponent(nomeMembroConsagracaoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alterarConsagracaoLayout.createSequentialGroup()
                        .addComponent(jLabel77)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alterarConsagracaoLayout.createSequentialGroup()
                        .addComponent(btnConcluirConsagracaoAlterar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarConsagracaoAlterar)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparConsagracaoAlterar)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        alterarConsagracaoLayout.setVerticalGroup(
            alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alterarConsagracaoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoMembroConsagracaoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarConsagracaoLayout.createSequentialGroup()
                        .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel117)
                                .addComponent(nomeMembroConsagracaoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel62))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel64))
                    .addGroup(alterarConsagracaoLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(boxMembroConsagracaoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dataConsagracaoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(boxCargosConsagracaoAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel77)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alterarConsagracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirConsagracaoAlterar)
                    .addComponent(btnCancelarConsagracaoAlterar)
                    .addComponent(btnLimparConsagracaoAlterar))
                .addContainerGap())
        );

        desk.add(alterarConsagracao, "card15");

        caixasESetores.setBackground(new java.awt.Color(255, 255, 255));
        caixasESetores.setPreferredSize(new java.awt.Dimension(900, 570));

        tableCaixas.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableCaixas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Caixa", "Setor", "Receita ", "Data Registro", "Descrição"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCaixas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCaixasMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(tableCaixas);

        jLabel22.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel22.setText("Caixas:");

        tableSetores.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableSetores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Setor", "Nome Setor", "Descrição", "Data Registro"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableSetores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSetoresMouseClicked(evt);
            }
        });
        jScrollPane18.setViewportView(tableSetores);
        if (tableSetores.getColumnModel().getColumnCount() > 0) {
            tableSetores.getColumnModel().getColumn(0).setHeaderValue("Id Caixa");
        }

        jLabel23.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel23.setText("Setores :");

        filtroSetores.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroSetores.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroSetoresKeyReleased(evt);
            }
        });

        jLabel100.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel100.setText("Filtro:");

        filtroCaixas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroCaixas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroCaixasKeyReleased(evt);
            }
        });

        jLabel101.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel101.setText("Filtro:");

        javax.swing.GroupLayout caixasESetoresLayout = new javax.swing.GroupLayout(caixasESetores);
        caixasESetores.setLayout(caixasESetoresLayout);
        caixasESetoresLayout.setHorizontalGroup(
            caixasESetoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caixasESetoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(caixasESetoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(caixasESetoresLayout.createSequentialGroup()
                        .addGroup(caixasESetoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                            .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(caixasESetoresLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel101)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filtroCaixas, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)))
                        .addContainerGap())
                    .addGroup(caixasESetoresLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel100)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filtroSetores, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))))
        );
        caixasESetoresLayout.setVerticalGroup(
            caixasESetoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, caixasESetoresLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(caixasESetoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(caixasESetoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filtroSetores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel100))
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caixasESetoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(caixasESetoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filtroCaixas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel101))
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        desk.add(caixasESetores, "card3");

        cadastroSetor.setBackground(new java.awt.Color(255, 255, 255));

        btnConcluirSetor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirSetor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirSetor.setText("Concluir");
        btnConcluirSetor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirSetorMouseClicked(evt);
            }
        });

        btnCancelarSetor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarSetor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarSetor.setText("Cancelar");
        btnCancelarSetor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarSetorMouseClicked(evt);
            }
        });

        btnLimparSetor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparSetor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparSetor.setText("Limpar");
        btnLimparSetor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparSetorMouseClicked(evt);
            }
        });

        jLabel78.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel78.setText("Nome Setor:");

        nomeSetor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel79.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel79.setText("Data :");

        fotoSetor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoSetor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoSetor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoSetor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoSetorMouseClicked(evt);
            }
        });

        jLabel80.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel80.setText("Descrição:");

        dscSetor.setColumns(20);
        dscSetor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscSetor.setRows(6);
        jScrollPane19.setViewportView(dscSetor);

        try {
            dataSetor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataSetor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout cadastroSetorLayout = new javax.swing.GroupLayout(cadastroSetor);
        cadastroSetor.setLayout(cadastroSetorLayout);
        cadastroSetorLayout.setHorizontalGroup(
            cadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroSetorLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroSetorLayout.createSequentialGroup()
                        .addComponent(btnConcluirSetor)
                        .addGap(26, 26, 26)
                        .addComponent(btnCancelarSetor)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparSetor))
                    .addComponent(fotoSetor)
                    .addGroup(cadastroSetorLayout.createSequentialGroup()
                        .addComponent(jLabel79)
                        .addGap(60, 60, 60)
                        .addComponent(dataSetor, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroSetorLayout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addGap(18, 18, 18)
                        .addComponent(nomeSetor, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroSetorLayout.createSequentialGroup()
                        .addComponent(jLabel80)
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        cadastroSetorLayout.setVerticalGroup(
            cadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroSetorLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoSetor, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(cadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(nomeSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(dataSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel80)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(cadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirSetor)
                    .addComponent(btnCancelarSetor)
                    .addComponent(btnLimparSetor))
                .addGap(41, 41, 41))
        );

        desk.add(cadastroSetor, "card21");

        cadastroCaixa.setBackground(new java.awt.Color(255, 255, 255));
        cadastroCaixa.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnLimparCaixa.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparCaixa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparCaixa.setText("Limpar");
        btnLimparCaixa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparCaixaMouseClicked(evt);
            }
        });

        jLabel81.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel81.setText("Setor:");

        jLabel82.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel82.setText("Data :");

        jLabel83.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel83.setText("Descrição:");

        btnConcluirCaixas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirCaixas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirCaixas.setText("Concluir");
        btnConcluirCaixas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirCaixasMouseClicked(evt);
            }
        });

        dscCaixa.setColumns(20);
        dscCaixa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscCaixa.setRows(6);
        jScrollPane20.setViewportView(dscCaixa);

        btnCancelarMembro2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarMembro2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarMembro2.setText("Cancelar");
        btnCancelarMembro2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarMembro2MouseClicked(evt);
            }
        });

        boxSetoresCaixa.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        try {
            dataCaixa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataCaixa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel84.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel84.setText("Receita Inicial:");

        receitaCaixa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        receitaCaixa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        javax.swing.GroupLayout cadastroCaixaLayout = new javax.swing.GroupLayout(cadastroCaixa);
        cadastroCaixa.setLayout(cadastroCaixaLayout);
        cadastroCaixaLayout.setHorizontalGroup(
            cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroCaixaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroCaixaLayout.createSequentialGroup()
                        .addComponent(btnConcluirCaixas)
                        .addGap(26, 26, 26)
                        .addComponent(btnCancelarMembro2)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparCaixa))
                    .addGroup(cadastroCaixaLayout.createSequentialGroup()
                        .addComponent(jLabel83)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroCaixaLayout.createSequentialGroup()
                        .addGroup(cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel81)
                            .addComponent(jLabel82)
                            .addComponent(jLabel84))
                        .addGap(33, 33, 33)
                        .addGroup(cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dataCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boxSetoresCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(receitaCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        cadastroCaixaLayout.setVerticalGroup(
            cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroCaixaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxSetoresCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(dataCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(receitaCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroCaixaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel83))
                    .addGroup(cadastroCaixaLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51)
                .addGroup(cadastroCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirCaixas)
                    .addComponent(btnCancelarMembro2)
                    .addComponent(btnLimparCaixa))
                .addGap(50, 50, 50))
        );

        desk.add(cadastroCaixa, "card22");

        perfilCaixa.setBackground(new java.awt.Color(255, 255, 255));
        perfilCaixa.setPreferredSize(new java.awt.Dimension(900, 570));

        tableCaixaDespesas.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableCaixaDespesas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Setor", "Descrição", "Data Registro", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane27.setViewportView(tableCaixaDespesas);
        if (tableCaixaDespesas.getColumnModel().getColumnCount() > 0) {
            tableCaixaDespesas.getColumnModel().getColumn(1).setHeaderValue("Descrição");
        }

        jLabel105.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel105.setText("Despesas:");

        filtroCaixaDespesa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroCaixaDespesa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroCaixaDespesaKeyReleased(evt);
            }
        });

        jLabel108.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel108.setText("Filtro:");

        tableCaixaOfertas.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableCaixaOfertas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Setor", "Descrição", "Data Registro", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane29.setViewportView(tableCaixaOfertas);
        if (tableCaixaOfertas.getColumnModel().getColumnCount() > 0) {
            tableCaixaOfertas.getColumnModel().getColumn(1).setHeaderValue("Descrição");
        }

        jLabel109.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel109.setText("Ofertas :");

        filtroCaixaOferta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroCaixaOferta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroCaixaOfertaKeyReleased(evt);
            }
        });

        jLabel110.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel110.setText("Filtro:");

        javax.swing.GroupLayout perfilCaixaLayout = new javax.swing.GroupLayout(perfilCaixa);
        perfilCaixa.setLayout(perfilCaixaLayout);
        perfilCaixaLayout.setHorizontalGroup(
            perfilCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilCaixaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(perfilCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(perfilCaixaLayout.createSequentialGroup()
                        .addGroup(perfilCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane29, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
                            .addGroup(perfilCaixaLayout.createSequentialGroup()
                                .addComponent(jLabel109)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel110)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(filtroCaixaOferta, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, perfilCaixaLayout.createSequentialGroup()
                        .addGroup(perfilCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(perfilCaixaLayout.createSequentialGroup()
                                .addComponent(jLabel105)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel108)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(filtroCaixaDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28))
                            .addComponent(jScrollPane27, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())))
        );
        perfilCaixaLayout.setVerticalGroup(
            perfilCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, perfilCaixaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(perfilCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel109)
                    .addComponent(filtroCaixaOferta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel110))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane29, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(perfilCaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel105)
                    .addComponent(filtroCaixaDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel108))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );

        desk.add(perfilCaixa, "card3");

        despesas.setBackground(new java.awt.Color(255, 255, 255));
        despesas.setPreferredSize(new java.awt.Dimension(900, 570));

        tableDespesas.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableDespesas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Setor", "Descrição", "Data Registro", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDespesas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDespesasMouseClicked(evt);
            }
        });
        jScrollPane22.setViewportView(tableDespesas);

        jLabel86.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel86.setText("Despesas :");

        filtroDespesas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroDespesas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroDespesasKeyReleased(evt);
            }
        });

        jLabel102.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel102.setText("Filtro:");

        javax.swing.GroupLayout despesasLayout = new javax.swing.GroupLayout(despesas);
        despesas.setLayout(despesasLayout);
        despesasLayout.setHorizontalGroup(
            despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(despesasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(despesasLayout.createSequentialGroup()
                        .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(despesasLayout.createSequentialGroup()
                        .addComponent(jLabel86)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel102)
                        .addGap(0, 0, 0)
                        .addComponent(filtroDespesas, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65))))
        );
        despesasLayout.setVerticalGroup(
            despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, despesasLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filtroDespesas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel102))
                    .addComponent(jLabel86))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addContainerGap())
        );

        desk.add(despesas, "card3");

        cadastroDespesa.setBackground(new java.awt.Color(255, 255, 255));

        btnConcluirDepesa.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirDepesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirDepesa.setText("Concluir");
        btnConcluirDepesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirDepesaMouseClicked(evt);
            }
        });

        btnCancelarDespesa.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarDespesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarDespesa.setText("Cancelar");
        btnCancelarDespesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarDespesaMouseClicked(evt);
            }
        });

        btnLimparDepesa.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparDepesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparDepesa.setText("Limpar");
        btnLimparDepesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparDepesaMouseClicked(evt);
            }
        });

        jLabel85.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel85.setText("Setor:");

        jLabel87.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel87.setText("Data :");

        jLabel88.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel88.setText("Descrição:");

        dscDespesa.setColumns(20);
        dscDespesa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscDespesa.setRows(6);
        jScrollPane21.setViewportView(dscDespesa);

        try {
            dataDespesa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataDespesa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        boxSetorDespesa.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        valorDespesa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        valorDespesa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel89.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel89.setText("Valor:");

        javax.swing.GroupLayout cadastroDespesaLayout = new javax.swing.GroupLayout(cadastroDespesa);
        cadastroDespesa.setLayout(cadastroDespesaLayout);
        cadastroDespesaLayout.setHorizontalGroup(
            cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroDespesaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroDespesaLayout.createSequentialGroup()
                        .addComponent(btnConcluirDepesa)
                        .addGap(26, 26, 26)
                        .addComponent(btnCancelarDespesa)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparDepesa))
                    .addGroup(cadastroDespesaLayout.createSequentialGroup()
                        .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel87)
                            .addComponent(jLabel85)
                            .addComponent(jLabel88)
                            .addComponent(jLabel89))
                        .addGap(26, 26, 26)
                        .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boxSetorDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(dataDespesa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                .addComponent(valorDespesa, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        cadastroDespesaLayout.setVerticalGroup(
            cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroDespesaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(boxSetorDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel87)
                    .addComponent(dataDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel89)
                    .addComponent(valorDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel88))
                .addGap(35, 35, 35)
                .addGroup(cadastroDespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirDepesa)
                    .addComponent(btnCancelarDespesa)
                    .addComponent(btnLimparDepesa))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        desk.add(cadastroDespesa, "card21");

        ofertasEDizimos.setBackground(new java.awt.Color(255, 255, 255));
        ofertasEDizimos.setPreferredSize(new java.awt.Dimension(900, 570));

        tableOfertas.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableOfertas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Setor", "Descrição", "Data Registro", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableOfertas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableOfertasMouseClicked(evt);
            }
        });
        jScrollPane23.setViewportView(tableOfertas);

        jLabel90.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel90.setText("Ofertas :");

        tableDizimos.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableDizimos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Membro", "Data Registro", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDizimos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDizimosMouseClicked(evt);
            }
        });
        jScrollPane26.setViewportView(tableDizimos);

        jLabel99.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel99.setText("Dizimos:");

        filtroDizimos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroDizimos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroDizimosKeyReleased(evt);
            }
        });

        jLabel103.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel103.setText("Filtro:");

        filtroOfertas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroOfertas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroOfertasKeyReleased(evt);
            }
        });

        jLabel104.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel104.setText("Filtro:");

        javax.swing.GroupLayout ofertasEDizimosLayout = new javax.swing.GroupLayout(ofertasEDizimos);
        ofertasEDizimos.setLayout(ofertasEDizimosLayout);
        ofertasEDizimosLayout.setHorizontalGroup(
            ofertasEDizimosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ofertasEDizimosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ofertasEDizimosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ofertasEDizimosLayout.createSequentialGroup()
                        .addGroup(ofertasEDizimosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                            .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                            .addGroup(ofertasEDizimosLayout.createSequentialGroup()
                                .addComponent(jLabel99)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel103)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(filtroDizimos, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)))
                        .addContainerGap())
                    .addGroup(ofertasEDizimosLayout.createSequentialGroup()
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel104)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filtroOfertas, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48))))
        );
        ofertasEDizimosLayout.setVerticalGroup(
            ofertasEDizimosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ofertasEDizimosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(ofertasEDizimosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(filtroOfertas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel104))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ofertasEDizimosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filtroDizimos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ofertasEDizimosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel99)
                        .addComponent(jLabel103)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addContainerGap())
        );

        desk.add(ofertasEDizimos, "card3");

        cadastroOferta.setBackground(new java.awt.Color(255, 255, 255));

        btnConcluirOferta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirOferta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirOferta.setText("Concluir");
        btnConcluirOferta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirOfertaMouseClicked(evt);
            }
        });

        btnCancelarOferta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarOferta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarOferta.setText("Cancelar");
        btnCancelarOferta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarOfertaMouseClicked(evt);
            }
        });

        btnLimparOferta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparOferta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparOferta.setText("Limpar");
        btnLimparOferta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparOfertaMouseClicked(evt);
            }
        });

        jLabel91.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel91.setText("Setor:");

        jLabel92.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel92.setText("Data :");

        jLabel93.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel93.setText("Descrição:");

        dscOferta.setColumns(20);
        dscOferta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscOferta.setRows(6);
        jScrollPane24.setViewportView(dscOferta);

        try {
            dataOferta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataOferta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        boxSetorOferta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        valorOferta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        valorOferta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel94.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel94.setText("Valor:");

        javax.swing.GroupLayout cadastroOfertaLayout = new javax.swing.GroupLayout(cadastroOferta);
        cadastroOferta.setLayout(cadastroOfertaLayout);
        cadastroOfertaLayout.setHorizontalGroup(
            cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroOfertaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroOfertaLayout.createSequentialGroup()
                        .addComponent(btnConcluirOferta)
                        .addGap(26, 26, 26)
                        .addComponent(btnCancelarOferta)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparOferta))
                    .addGroup(cadastroOfertaLayout.createSequentialGroup()
                        .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel92)
                            .addComponent(jLabel91)
                            .addComponent(jLabel93)
                            .addComponent(jLabel94))
                        .addGap(26, 26, 26)
                        .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boxSetorOferta, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(dataOferta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                .addComponent(valorOferta, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        cadastroOfertaLayout.setVerticalGroup(
            cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroOfertaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91)
                    .addComponent(boxSetorOferta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataOferta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel92))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel94)
                    .addComponent(valorOferta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel93))
                .addGap(35, 35, 35)
                .addGroup(cadastroOfertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirOferta)
                    .addComponent(btnCancelarOferta)
                    .addComponent(btnLimparOferta))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        desk.add(cadastroOferta, "card21");

        cadastroDizimo.setBackground(new java.awt.Color(255, 255, 255));
        cadastroDizimo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cadastroDizimoMouseMoved(evt);
            }
        });

        btnConcluirDizimo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirDizimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirDizimo.setText("Concluir");
        btnConcluirDizimo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirDizimoMouseClicked(evt);
            }
        });

        btnCancelarDizimo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarDizimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarDizimo.setText("Cancelar");
        btnCancelarDizimo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarDizimoMouseClicked(evt);
            }
        });

        btnLimparDizimo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparDizimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparDizimo.setText("Limpar");
        btnLimparDizimo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparDizimoMouseClicked(evt);
            }
        });

        jLabel95.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel95.setText("CPF:");

        jLabel96.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel96.setText("Data :");

        jLabel97.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel97.setText("Descrição:");

        dscDizimo.setColumns(20);
        dscDizimo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dscDizimo.setRows(6);
        jScrollPane25.setViewportView(dscDizimo);

        try {
            dataDizimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataDizimo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        boxMembroDizimo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        valorDizimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        valorDizimo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel98.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel98.setText("Valor:");

        jLabel118.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel118.setText("Nome Membro:");

        nomeMembroDizimo.setEditable(false);
        nomeMembroDizimo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout cadastroDizimoLayout = new javax.swing.GroupLayout(cadastroDizimo);
        cadastroDizimo.setLayout(cadastroDizimoLayout);
        cadastroDizimoLayout.setHorizontalGroup(
            cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroDizimoLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroDizimoLayout.createSequentialGroup()
                        .addComponent(btnConcluirDizimo)
                        .addGap(26, 26, 26)
                        .addComponent(btnCancelarDizimo)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparDizimo))
                    .addGroup(cadastroDizimoLayout.createSequentialGroup()
                        .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel96)
                            .addComponent(jLabel95)
                            .addComponent(jLabel97)
                            .addComponent(jLabel98))
                        .addGap(26, 26, 26)
                        .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(cadastroDizimoLayout.createSequentialGroup()
                                .addComponent(boxMembroDizimo, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel118)
                                .addGap(18, 18, 18)
                                .addComponent(nomeMembroDizimo, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(dataDizimo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                .addComponent(valorDizimo, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        cadastroDizimoLayout.setVerticalGroup(
            cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroDizimoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel118)
                        .addComponent(nomeMembroDizimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel95)
                        .addComponent(boxMembroDizimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataDizimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel96))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel98)
                    .addComponent(valorDizimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel97))
                .addGap(35, 35, 35)
                .addGroup(cadastroDizimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluirDizimo)
                    .addComponent(btnCancelarDizimo)
                    .addComponent(btnLimparDizimo))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        desk.add(cadastroDizimo, "card21");

        usuarios.setBackground(new java.awt.Color(255, 255, 255));
        usuarios.setPreferredSize(new java.awt.Dimension(900, 570));

        tableUsuarios.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        tableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Login", "Senha", "CPF", "Nome", "Modo Usuario", "Data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUsuariosMouseClicked(evt);
            }
        });
        jScrollPane28.setViewportView(tableUsuarios);

        jLabel134.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel134.setText("Contas:");

        filtroUsuarios.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        filtroUsuarios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroUsuariosKeyReleased(evt);
            }
        });

        jLabel135.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel135.setText("Filtro:");

        javax.swing.GroupLayout usuariosLayout = new javax.swing.GroupLayout(usuarios);
        usuarios.setLayout(usuariosLayout);
        usuariosLayout.setHorizontalGroup(
            usuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(usuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(usuariosLayout.createSequentialGroup()
                        .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(usuariosLayout.createSequentialGroup()
                        .addComponent(jLabel134)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel135)
                        .addGap(0, 0, 0)
                        .addComponent(filtroUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65))))
        );
        usuariosLayout.setVerticalGroup(
            usuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, usuariosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(usuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(usuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filtroUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel135))
                    .addComponent(jLabel134))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addContainerGap())
        );

        desk.add(usuarios, "card3");

        cadastroUsuario.setBackground(new java.awt.Color(255, 255, 255));
        cadastroUsuario.setToolTipText("");
        cadastroUsuario.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cadastroUsuario.setPreferredSize(new java.awt.Dimension(900, 570));
        cadastroUsuario.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cadastroUsuarioMouseMoved(evt);
            }
        });

        btnConcluirMembro1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirMembro1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirMembro1.setText("Concluir");
        btnConcluirMembro1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirMembro1MouseClicked(evt);
            }
        });

        btnCancelarMembro1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarMembro1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarMembro1.setText("Cancelar");
        btnCancelarMembro1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarMembro1MouseClicked(evt);
            }
        });

        btnLimparMembro1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparMembro1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparMembro1.setText("Limpar");
        btnLimparMembro1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparMembro1MouseClicked(evt);
            }
        });

        jLabel145.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel145.setText("Login:");
        jLabel145.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel145.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        loginUsuarioConta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel146.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel146.setText("Senha:");
        jLabel146.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel146.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        senhaUsuarioConta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        boxMembroUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel147.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel147.setText("CPF Membro:");
        jLabel147.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel147.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel148.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel148.setText("Nome:");
        jLabel148.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel148.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nomeUsuario.setEditable(false);
        nomeUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nomeUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nomeUsuarioMouseEntered(evt);
            }
        });

        boxModoUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel149.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel149.setText("Modo do Usuario:");
        jLabel149.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel149.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        fotoUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        fotoUsuario.setMaximumSize(new java.awt.Dimension(256, 180));
        fotoUsuario.setMinimumSize(new java.awt.Dimension(256, 180));

        jLabel150.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel150.setText("Data:");
        jLabel150.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel150.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        try {
            dataConta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataConta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout cadastroUsuarioLayout = new javax.swing.GroupLayout(cadastroUsuario);
        cadastroUsuario.setLayout(cadastroUsuarioLayout);
        cadastroUsuarioLayout.setHorizontalGroup(
            cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroUsuarioLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fotoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cadastroUsuarioLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cadastroUsuarioLayout.createSequentialGroup()
                                .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel149)
                                    .addComponent(jLabel147)
                                    .addComponent(jLabel148))
                                .addGap(31, 31, 31)
                                .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nomeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(boxMembroUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(boxModoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(71, 71, 71)
                                .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(cadastroUsuarioLayout.createSequentialGroup()
                                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel146)
                                            .addComponent(jLabel145))
                                        .addGap(31, 31, 31)
                                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(loginUsuarioConta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                            .addComponent(senhaUsuarioConta, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dataConta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel150)))
                            .addGroup(cadastroUsuarioLayout.createSequentialGroup()
                                .addComponent(btnConcluirMembro1)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelarMembro1)
                                .addGap(29, 29, 29)
                                .addComponent(btnLimparMembro1)))))
                .addContainerGap(174, Short.MAX_VALUE))
        );
        cadastroUsuarioLayout.setVerticalGroup(
            cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroUsuarioLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroUsuarioLayout.createSequentialGroup()
                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(boxMembroUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel147))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel148)
                            .addComponent(nomeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cadastroUsuarioLayout.createSequentialGroup()
                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(loginUsuarioConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel145))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(senhaUsuarioConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel146))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroUsuarioLayout.createSequentialGroup()
                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(boxModoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel149))
                            .addComponent(dataConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(86, 86, 86)
                        .addGroup(cadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancelarMembro1)
                            .addComponent(btnConcluirMembro1)
                            .addComponent(btnLimparMembro1)))
                    .addComponent(jLabel150))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        desk.add(cadastroUsuario, "card6");

        perfilUsuario.setBackground(new java.awt.Color(255, 255, 255));
        perfilUsuario.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel119.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel119.setText("Nome:");
        jLabel119.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel119.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel120.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel120.setText("RG:");
        jLabel120.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel120.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel121.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel121.setText("CPF:");
        jLabel121.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel121.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel122.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel122.setText("Telefone:");
        jLabel122.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel122.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel123.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel123.setText("Endereço:");
        jLabel123.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel123.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel124.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel124.setText("Naturalidade");
        jLabel124.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel124.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel125.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel125.setText("Numero Dependentes:");
        jLabel125.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel125.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel126.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel126.setText("Data Batismo:");
        jLabel126.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel126.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel127.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel127.setText("Data Nascimento:");
        jLabel127.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel127.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel128.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel128.setText("Data Registro:");
        jLabel128.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel128.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nomeUsuarioPerfil.setEditable(false);
        nomeUsuarioPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        rgUsuario.setEditable(false);
        try {
            rgUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        rgUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        cpfUsuario.setEditable(false);
        try {
            cpfUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cpfUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        telefoneUsuario.setEditable(false);
        try {
            telefoneUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telefoneUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        enderecoUsuario.setEditable(false);
        enderecoUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        naturalidadeUsuario.setEditable(false);
        naturalidadeUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoUsuarioPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoUsuarioPerfil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnAlterarMembro1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAlterarMembro1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/editar.png"))); // NOI18N
        btnAlterarMembro1.setText("Alterar");
        btnAlterarMembro1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAlterarMembro1MouseClicked(evt);
            }
        });

        btnSairPerfilUsuario.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSairPerfilUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/iconVoltar.png"))); // NOI18N
        btnSairPerfilUsuario.setText("Sair");
        btnSairPerfilUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSairPerfilUsuarioMouseClicked(evt);
            }
        });

        jLabel129.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel129.setText("Cargo:");
        jLabel129.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel129.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        boxModoUsuarioPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxModoUsuarioPerfil.setEnabled(false);

        emailUsuario.setEditable(false);
        emailUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        labe223.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labe223.setText("Email:");
        labe223.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        labe223.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel130.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel130.setText("Estado Civil:");
        jLabel130.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel130.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        boxEstadoCivilUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxEstadoCivilUsuario.setEnabled(false);

        jLabel131.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel131.setText("Login:");
        jLabel131.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel131.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        loginUsuarioPerfil.setEditable(false);
        loginUsuarioPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel132.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel132.setText("Senha:");
        jLabel132.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel132.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        senhaUsuarioPerfil.setEditable(false);
        senhaUsuarioPerfil.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel133.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel133.setText("Modo Usuario:");
        jLabel133.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel133.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        boxUsuarios.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxUsuarios.setEnabled(false);

        dataBatismoUsuario.setEditable(false);
        dataBatismoUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dataNascimentoUsuario.setEditable(false);
        dataNascimentoUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dataRegistroUsuario.setEditable(false);
        dataRegistroUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dependentesUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));

        javax.swing.GroupLayout perfilUsuarioLayout = new javax.swing.GroupLayout(perfilUsuario);
        perfilUsuario.setLayout(perfilUsuarioLayout);
        perfilUsuarioLayout.setHorizontalGroup(
            perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(perfilUsuarioLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(perfilUsuarioLayout.createSequentialGroup()
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, perfilUsuarioLayout.createSequentialGroup()
                                .addComponent(btnAlterarMembro1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                                .addComponent(btnSairPerfilUsuario))
                            .addGroup(perfilUsuarioLayout.createSequentialGroup()
                                .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel119)
                                    .addComponent(jLabel121)
                                    .addComponent(jLabel122)
                                    .addComponent(jLabel120))
                                .addGap(27, 27, 27)
                                .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nomeUsuarioPerfil)
                                    .addComponent(rgUsuario)
                                    .addComponent(cpfUsuario)
                                    .addComponent(telefoneUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                            .addGroup(perfilUsuarioLayout.createSequentialGroup()
                                .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel123)
                                    .addComponent(jLabel129)
                                    .addComponent(labe223))
                                .addGap(21, 21, 21)
                                .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(enderecoUsuario)
                                    .addComponent(emailUsuario)
                                    .addComponent(boxUsuarios, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(perfilUsuarioLayout.createSequentialGroup()
                                .addComponent(jLabel133)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(boxModoUsuarioPerfil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(82, 82, 82)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(perfilUsuarioLayout.createSequentialGroup()
                                .addComponent(jLabel131)
                                .addGap(150, 150, 150)
                                .addComponent(loginUsuarioPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                            .addGroup(perfilUsuarioLayout.createSequentialGroup()
                                .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(perfilUsuarioLayout.createSequentialGroup()
                                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel124)
                                            .addComponent(jLabel125)
                                            .addComponent(jLabel126)
                                            .addComponent(jLabel127)
                                            .addComponent(jLabel128)
                                            .addComponent(jLabel130))
                                        .addGap(35, 35, 35)
                                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(boxEstadoCivilUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(naturalidadeUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                                            .addComponent(dataBatismoUsuario)
                                            .addComponent(dataNascimentoUsuario)
                                            .addComponent(dataRegistroUsuario)
                                            .addComponent(dependentesUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(perfilUsuarioLayout.createSequentialGroup()
                                        .addComponent(jLabel132)
                                        .addGap(145, 145, 145)
                                        .addComponent(senhaUsuarioPerfil)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(109, Short.MAX_VALUE))
                    .addGroup(perfilUsuarioLayout.createSequentialGroup()
                        .addComponent(fotoUsuarioPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        perfilUsuarioLayout.setVerticalGroup(
            perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, perfilUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fotoUsuarioPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(perfilUsuarioLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel124)
                            .addComponent(naturalidadeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel125)
                            .addComponent(dependentesUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel126)
                            .addComponent(dataBatismoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel127)
                                .addComponent(dataNascimentoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(perfilUsuarioLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel128)
                                    .addComponent(dataRegistroUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boxEstadoCivilUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel130))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel131)
                            .addComponent(loginUsuarioPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel132)
                            .addComponent(senhaUsuarioPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(perfilUsuarioLayout.createSequentialGroup()
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nomeUsuarioPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel119, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rgUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel120))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpfUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel121))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(telefoneUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel122))
                        .addGap(11, 11, 11)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(enderecoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel123))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labe223))
                        .addGap(8, 8, 8)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel129)
                            .addComponent(boxUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel133)
                            .addComponent(boxModoUsuarioPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(perfilUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAlterarMembro1)
                            .addComponent(btnSairPerfilUsuario))))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        desk.add(perfilUsuario, "card6");

        alterarUsuario.setBackground(new java.awt.Color(255, 255, 255));
        alterarUsuario.setToolTipText("");
        alterarUsuario.setPreferredSize(new java.awt.Dimension(900, 570));

        jLabel136.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel136.setText("Nome:");
        jLabel136.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel136.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel137.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel137.setText("RG:");
        jLabel137.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel137.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel138.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel138.setText("CPF:");
        jLabel138.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel138.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel139.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel139.setText("Telefone:");
        jLabel139.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel139.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel140.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel140.setText("Endereço:");
        jLabel140.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel140.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel141.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel141.setText("Naturalidade");
        jLabel141.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel141.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel142.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel142.setText("Numero Dependentes:");
        jLabel142.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel142.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel143.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel143.setText("Data Batismo:");
        jLabel143.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel143.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel144.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel144.setText("Data Nascimento:");
        jLabel144.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel144.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel151.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel151.setText("Data Registro:");
        jLabel151.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel151.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nomeAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            rgAlterarUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        rgAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        cpfAlterarUsuario.setEditable(false);
        try {
            cpfAlterarUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cpfAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            telefoneAlterarUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telefoneAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        enderecoAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        naturalidadeAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataBatismoAlterarUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataBatismoAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataNascimentoAlterarUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataNascimentoAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        try {
            dataRegistroAlterarUsuario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataRegistroAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fotoAlterarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/choosePhoto.png"))); // NOI18N
        fotoAlterarUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fotoAlterarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fotoAlterarUsuarioMouseClicked(evt);
            }
        });

        btnConcluirAlterarUsuario.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConcluirAlterarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/check.png"))); // NOI18N
        btnConcluirAlterarUsuario.setText("Concluir");
        btnConcluirAlterarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirAlterarUsuarioMouseClicked(evt);
            }
        });

        btnCancelarAlterarUsuario.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancelarAlterarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excluir.png"))); // NOI18N
        btnCancelarAlterarUsuario.setText("Cancelar");
        btnCancelarAlterarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarAlterarUsuarioMouseClicked(evt);
            }
        });

        btnLimparAlterarUsuario.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLimparAlterarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/limpar.png"))); // NOI18N
        btnLimparAlterarUsuario.setText("Limpar");
        btnLimparAlterarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimparAlterarUsuarioMouseClicked(evt);
            }
        });

        jLabel152.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel152.setText("Email:");
        jLabel152.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel152.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        emailAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel153.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel153.setText("Estado Civil:");
        jLabel153.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel153.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        estadoCivilAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel154.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel154.setText("Login:");
        jLabel154.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel154.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        loginAlterarUsuario.setEditable(false);
        loginAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        loginAlterarUsuario.setEnabled(false);

        jLabel155.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel155.setText("Nova Senha:");
        jLabel155.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel155.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        novaSenhaAlterarUsuario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        boxModoUsuarioAlterarUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxModoUsuarioAlterarUsuario.setEnabled(false);

        jLabel157.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel157.setText("Modo Usuario:");
        jLabel157.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel157.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        dependentesAlterarUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));

        javax.swing.GroupLayout alterarUsuarioLayout = new javax.swing.GroupLayout(alterarUsuario);
        alterarUsuario.setLayout(alterarUsuarioLayout);
        alterarUsuarioLayout.setHorizontalGroup(
            alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alterarUsuarioLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarUsuarioLayout.createSequentialGroup()
                        .addComponent(btnConcluirAlterarUsuario)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarAlterarUsuario)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimparAlterarUsuario))
                    .addGroup(alterarUsuarioLayout.createSequentialGroup()
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(alterarUsuarioLayout.createSequentialGroup()
                                .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel136)
                                    .addComponent(jLabel138)
                                    .addComponent(jLabel139)
                                    .addComponent(jLabel137))
                                .addGap(27, 27, 27)
                                .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nomeAlterarUsuario)
                                    .addComponent(rgAlterarUsuario)
                                    .addComponent(cpfAlterarUsuario)
                                    .addComponent(telefoneAlterarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                            .addComponent(fotoAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(alterarUsuarioLayout.createSequentialGroup()
                                .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel140)
                                    .addComponent(jLabel152)
                                    .addComponent(jLabel154))
                                .addGap(21, 21, 21)
                                .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(loginAlterarUsuario)
                                    .addComponent(enderecoAlterarUsuario)
                                    .addComponent(emailAlterarUsuario)))
                            .addGroup(alterarUsuarioLayout.createSequentialGroup()
                                .addComponent(jLabel157)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(boxModoUsuarioAlterarUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(99, 99, 99)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel141)
                                .addComponent(jLabel142)
                                .addComponent(jLabel143)
                                .addComponent(jLabel144)
                                .addComponent(jLabel151)
                                .addComponent(jLabel153))
                            .addGroup(alterarUsuarioLayout.createSequentialGroup()
                                .addComponent(jLabel155)
                                .addGap(71, 71, 71)))
                        .addGap(35, 35, 35)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(estadoCivilAlterarUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(novaSenhaAlterarUsuario, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(alterarUsuarioLayout.createSequentialGroup()
                                .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(naturalidadeAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(dataRegistroAlterarUsuario, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(dataBatismoAlterarUsuario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                        .addComponent(dataNascimentoAlterarUsuario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                                    .addComponent(dependentesAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(92, 92, 92))
        );
        alterarUsuarioLayout.setVerticalGroup(
            alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alterarUsuarioLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(fotoAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alterarUsuarioLayout.createSequentialGroup()
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel141, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(naturalidadeAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel142)
                            .addComponent(dependentesAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataBatismoAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel143))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataNascimentoAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel144))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dataRegistroAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel151))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel153)
                            .addComponent(estadoCivilAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel155)
                            .addComponent(novaSenhaAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(alterarUsuarioLayout.createSequentialGroup()
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nomeAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rgAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel137))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpfAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel138))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(telefoneAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel139))
                        .addGap(10, 10, 10)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(enderecoAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel140))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel152)
                            .addComponent(emailAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel154)
                            .addComponent(loginAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel157)
                            .addComponent(boxModoUsuarioAlterarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(24, 24, 24)
                .addGroup(alterarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarAlterarUsuario)
                    .addComponent(btnConcluirAlterarUsuario)
                    .addComponent(btnLimparAlterarUsuario))
                .addContainerGap())
        );

        desk.add(alterarUsuario, "card6");

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setText("Membros :\n\nMenu Membros - Cadastrar Membro:\n\n\tAo clicar em menu membros irá abrir o painel de controle mostrando todos os membros cadastrados.\n\tAo clicar em Cadastrar Membro irá abrir o painel de Cadastro de Membros.\n\nDisciplinas - Cadastrar Disciplina:\n\n\tAo clicar em Disciplinas ira abrir o painel de controle mostrando todas disciplinas registradas.\n\tAo clicar em Cadastrar Disciplina irá abrir o painel de Cadastro de Disciplina, só é possível se existir algum membro já cadastrado no sistema!!!!\n\nDesligamentos - Cadastrar Desligamento:\n\n\tAo clicar em Desligamentos ira abrir o painel de controle mostrando todos desligamentos registrados.\n\tAo clicar em Cadastrar Desligamento irá abrir o painel de Cadastro de Desligamento.só é possível se existir algum membro já cadastrado no sistema!!!!\n\nConsagrações - Cadastrar Consagração:\n\n\tAo clicar em Consgrações ira abrir o painel de controle mostrando todas Consagrações registradas.\n\tAo clicar em Cadastrar Consagração irá abrir o painel de Cadastro de Consagração.só é possível se existir algum membro já cadastrado no sistema!!!!\n\nFinancas :\n\nCaixas e Setores - Cadastrar Setor\n\t      - Cadastrar  Caixa\n\n\tAo clicar em Caixas e Setores, irá abrir o painel de controle mostrando todos os Caixas e Setores cadastrados.\n\tAo clicar em Cadastrar Setores, irá abrir o painel de cadastro de setores.\n\tAo clicar em Cadastrar Caixa, irá abrir o Painel de cadastro de caixa. Só é possível se existir algum setor já cadastrado no sistema!!!!\n\nOfertas e Dizimos  - Cadastrar Oferta\n\t        - Cadastrar Dizimo\n\n\tAo clicar em Ofertas e Dizimos, irá abrir o painel de controle mostrando todos os Ofertas e Dizimos cadastrados.\n\tAo clicar em Cadastrar Oferta, irá abrir o painel de Cadastro de Ofertas.Só é possível se existir algum caixa registrado!!!!\n\tAo clicar em Cadastrar Dizimo, irá abrir o painel de Cadastro de Dizimos(Depositados sempre no caixa principal chamado \"Igreja\").\nContas - Sua conta\t\n\t\n\tAo clicar em Sua Conta, irá abrir o painel de controle da conta do usuário permitindo alterações.\n");
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.setDisabledTextColor(new java.awt.Color(51, 102, 255));
        jScrollPane30.setViewportView(jTextArea2);

        javax.swing.GroupLayout ajudaLayout = new javax.swing.GroupLayout(ajuda);
        ajuda.setLayout(ajudaLayout);
        ajudaLayout.setHorizontalGroup(
            ajudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 909, Short.MAX_VALUE)
        );
        ajudaLayout.setVerticalGroup(
            ajudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
        );

        desk.add(ajuda, "card33");

        logPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel162.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel162.setText("Acessos:");

        tableAcessos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Usuario", "Login", "Logout"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableAcessos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAcessosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableAcessos);

        jLabel163.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel163.setText("Filtro:");

        filtroTableAcessos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        filtroTableAcessos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroTableAcessosKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout logPanelLayout = new javax.swing.GroupLayout(logPanel);
        logPanel.setLayout(logPanelLayout);
        logPanelLayout.setHorizontalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(logPanelLayout.createSequentialGroup()
                        .addComponent(jLabel162)
                        .addGap(394, 394, 394)
                        .addComponent(jLabel163)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filtroTableAcessos, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 107, Short.MAX_VALUE)))
                .addContainerGap())
        );
        logPanelLayout.setVerticalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel162)
                    .addComponent(jLabel163)
                    .addComponent(filtroTableAcessos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addContainerGap())
        );

        desk.add(logPanel, "card33");

        logAlteracoes.setBackground(new java.awt.Color(255, 255, 255));

        jLabel164.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel164.setText("Alterações:");

        tableAlteracoes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Alteração", "ID Acesso", "Alteração", "Usuario", "Data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane31.setViewportView(tableAlteracoes);

        jLabel165.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel165.setText("Filtro:");

        filtroTableAlteracoes.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        filtroTableAlteracoes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroTableAlteracoesKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout logAlteracoesLayout = new javax.swing.GroupLayout(logAlteracoes);
        logAlteracoes.setLayout(logAlteracoesLayout);
        logAlteracoesLayout.setHorizontalGroup(
            logAlteracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logAlteracoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logAlteracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane31)
                    .addGroup(logAlteracoesLayout.createSequentialGroup()
                        .addComponent(jLabel164)
                        .addGap(394, 394, 394)
                        .addComponent(jLabel165)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filtroTableAlteracoes, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 83, Short.MAX_VALUE)))
                .addContainerGap())
        );
        logAlteracoesLayout.setVerticalGroup(
            logAlteracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logAlteracoesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(logAlteracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel164)
                    .addComponent(jLabel165)
                    .addComponent(filtroTableAlteracoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane31, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addContainerGap())
        );

        desk.add(logAlteracoes, "card33");

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(headerDesk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(desk, javax.swing.GroupLayout.DEFAULT_SIZE, 909, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerDesk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desk, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(mainLayout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void membroPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_membroPanelMouseEntered
        membroPanel.setBackground(corFundoPanelAtivado);
        membroLabel.setForeground(java.awt.Color.white);
        menuMembrosPop.show(main, membroPanel.getX()+245, membroPanel.getY());
        if(menuFinancasPop.isVisible()){
            menuFinancasPop.setVisible(false);
        }
    }//GEN-LAST:event_membroPanelMouseEntered

    private void membroPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_membroPanelMouseExited
        membroPanel.setBackground(corFundoPanel);
        membroLabel.setForeground(java.awt.Color.black);
        // TODO add your handling code here:
    }//GEN-LAST:event_membroPanelMouseExited

    private void financasPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_financasPanelMouseEntered
        financasPanel.setBackground(corFundoPanelAtivado);
        agendaLabel.setForeground(java.awt.Color.white);
        if(menuMembrosPop.isVisible() || menuRelatoriosPop.isVisible()){
            menuMembrosPop.setVisible(false);
            menuRelatoriosPop.setVisible(false);
        }
        menuFinancasPop.show(main, financasPanel.getX() + 245, financasPanel.getY());
        // TODO add your handling code here:
    }//GEN-LAST:event_financasPanelMouseEntered

    private void financasPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_financasPanelMouseExited
        financasPanel.setBackground(corFundoPanel);
        agendaLabel.setForeground(java.awt.Color.black);
        // TODO add your handling code here:
    }//GEN-LAST:event_financasPanelMouseExited

    private void relatoriosPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_relatoriosPanelMouseEntered
        relatoriosPanel.setBackground(corFundoPanelAtivado);
        docsLabel.setForeground(java.awt.Color.white);
        if(menuMembrosPop.isVisible() || menuFinancasPop.isVisible()){
            menuMembrosPop.setVisible(false);
            menuFinancasPop.setVisible(false);
        }
        menuRelatoriosPop.show(main, relatoriosPanel.getX() + 245, relatoriosPanel.getY());
        // TODO add your handling code here:
    }//GEN-LAST:event_relatoriosPanelMouseEntered

    private void relatoriosPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_relatoriosPanelMouseExited
         relatoriosPanel.setBackground(corFundoPanel);
         docsLabel.setForeground(java.awt.Color.black);
        // TODO add your handling code here:
    }//GEN-LAST:event_relatoriosPanelMouseExited

    private void docsPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_docsPanelMouseEntered
        docsPanel.setBackground(corFundoPanelAtivado);
        confLabel.setForeground(java.awt.Color.white);
        menuDocsPop.show(main,docsPanel.getX() +245, docsPanel.getY());
        if(menuContasPop.isVisible() || menuRelatoriosPop.isVisible() ){
            menuContasPop.setVisible(false);
            menuRelatoriosPop.setVisible(false);
        }
    }//GEN-LAST:event_docsPanelMouseEntered

    private void docsPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_docsPanelMouseExited
        docsPanel.setBackground(corFundoPanel);
        confLabel.setForeground(java.awt.Color.black);
    }//GEN-LAST:event_docsPanelMouseExited

    private void contaPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contaPanelMouseEntered
        contaPanel.setBackground(corFundoPanelAtivado);
        contaLabel.setForeground(java.awt.Color.white);
        menuContasPop.show(main, contaPanel.getX() + 245, contaPanel.getY());
    }//GEN-LAST:event_contaPanelMouseEntered

    private void contaPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contaPanelMouseExited
        contaPanel.setBackground(corFundoPanel);
        contaLabel.setForeground(java.awt.Color.black);
        // TODO add your handling code here:
    }//GEN-LAST:event_contaPanelMouseExited

    private void ajudaPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ajudaPanelMouseEntered
        ajudaPanel.setBackground(corFundoPanelAtivado);
        ajudaLabel.setForeground(java.awt.Color.white);
        if(menuContasPop.isVisible()){
            menuContasPop.setVisible(false);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_ajudaPanelMouseEntered

    private void ajudaPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ajudaPanelMouseExited
        ajudaPanel.setBackground(corFundoPanel);
        ajudaLabel.setForeground(java.awt.Color.black);

        // TODO add your handling code here:
    }//GEN-LAST:event_ajudaPanelMouseExited

    private void sairPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sairPanelMouseEntered
        sairPanel.setBackground(corFundoPanelAtivado);
        sairLabel.setForeground(java.awt.Color.white);
        // TODO add your handling code here:
    }//GEN-LAST:event_sairPanelMouseEntered

    private void sairPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sairPanelMouseExited
        sairPanel.setBackground(corFundoPanel);
        sairLabel.setForeground(java.awt.Color.black);
        // TODO add your handling code here:
    }//GEN-LAST:event_sairPanelMouseExited

    private void btnCancelarMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMembroMouseClicked
        Utilitarios.atualizarPainel(desk,membros);
        Utilitarios.fotoDefault(fotoMembro);
        Utilitarios.limparField(cadastroMembros);
        logoHeaderDesk.setText("CMS - Membros");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarMembroMouseClicked

    private void fotoMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoMembroMouseClicked
         caminhoFoto = Utilitarios.buscarFoto(cadastroMembros,fotoMembro);
        
        
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_fotoMembroMouseClicked

    private void tableMembrosDesligadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMembrosDesligadosMouseClicked
        if(evt.getClickCount() == 2){
            if(tableMembrosDesligados.getSelectedRow()!= -1){
                String cpfTableMembro = tableMembrosDesligados.getValueAt(tableMembrosDesligados.getSelectedRow(),0).toString();
                atualizarCamposMembroPerfil(Membro.perfilMembro(cpfTableMembro));                
                
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tableMembrosDesligadosMouseClicked

    private void btnConcluirMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirMembroMouseClicked
        if(caminhoFoto!=null){
            if(Utilitarios.validarCampos(cadastroMembros) && Utilitarios.validarEmail(emailMembro.getText())){
                Membro membro = new Membro();
                membro.setNumDependentes(Integer.parseInt(dependentesMembro.getSelectedItem().toString()));
                membro.setNome(nomeMembro.getText());
                membro.setRG(rgMembro.getText());
                membro.setCPF(cpfMembro.getText());
                membro.setTelefone(telefoneMembro.getText());
                membro.setEndereco(enderecoMembro.getText());
                String naturalidade = naturalidadeEstadoMembro.getSelectedItem().toString() + "-" + naturalidadeMunicipioMembro.getSelectedItem().toString();
                membro.setNaturalidade(naturalidade);
                try {
                    sqlDate = new java.sql.Date(padraoData.parse(dataBatismoMembro.getText()).getTime());
                    membro.setDataBatismo(sqlDate);
                    sqlDate = new java.sql.Date(padraoData.parse(dataNascimentoMembro.getText()).getTime());
                    membro.setDataNascimento(sqlDate);
                    sqlDate = new java.sql.Date(padraoData.parse(dataRegistroMembro.getText()).getTime());
                    membro.setDataRegistro(sqlDate);
                    sqlDate = null;
                } catch (ParseException ex) {
                    Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
                }
                membro.setEmail(emailMembro.getText());
                EstadoCivil estadoCivil = (EstadoCivil) estadoCivilMembro.getSelectedItem();
                membro.setEstadoCivil(estadoCivil.getId());
                membro.setFoto(caminhoFoto);
                Acesso_Alteracao alteracao = new Acesso_Alteracao();
                alteracao.setAcesso_id(this.idAcesso);
                alteracao.setAlteracao_id(1);
                if(Membro.cadastrarMembro(membro)){
                    Utilitarios.limparField(cadastroMembros);
                    atualizarTabelas();
                    Utilitarios.atualizarPainel(desk, membros);
                    Utilitarios.fotoDefault(fotoMembro);
                    atualizarComboBox();
                    Acesso_Alteracao.cadastrarAcesso(alteracao);
                }else{
                    Utilitarios.limparField(cadastroMembros);
                }
            }
        
        }else{
            JOptionPane.showMessageDialog(null,"Escolha uma foto");
        }
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirMembroMouseClicked

    @SuppressWarnings("empty-statement")
    private void btnLimparMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparMembroMouseClicked
        Utilitarios.limparField(cadastroMembros);
        Utilitarios.fotoDefault(fotoMembro);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparMembroMouseClicked

    private void btnDisciplinarMembroPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDisciplinarMembroPerfilMouseClicked
        Utilitarios.atualizarPainel(desk, cadastroDisciplina);
        Membro membro = Membro.perfilMembro(cpfMembroPerfil.getText());
        Utilitarios.limparField(perfilMembro);
        boxMembroDisciplina.getModel().setSelectedItem(membro);
        logoHeaderDesk.setText("CMS - Registrar Disciplina");
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDisciplinarMembroPerfilMouseClicked

    private void btnExcluirMembroPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcluirMembroPerfilMouseClicked
        Utilitarios.atualizarPainel(desk, cadastroDesligamento);
        Membro membro = Membro.perfilMembro(cpfMembroPerfil.getText());
        Utilitarios.limparField(perfilMembro);
        boxMembroDesligamento.getModel().setSelectedItem(membro);
        logoHeaderDesk.setText("CMS - Registrar Desligamento");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExcluirMembroPerfilMouseClicked

    private void btnSairMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairMembroMouseClicked
        Utilitarios.atualizarPainel(desk,membros);
        Utilitarios.limparField(perfilMembro);
        logoHeaderDesk.setText("CMS - Membros");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSairMembroMouseClicked

    private void btnCancelarDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarDisciplinaMouseClicked
        Utilitarios.limparField(cadastroDisciplina);
        Utilitarios.atualizarPainel(desk,disciplinas);
        logoHeaderDesk.setText("CMS - Disciplinas");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarDisciplinaMouseClicked

    private void btnLimparDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparDisciplinaMouseClicked
        Utilitarios.limparField(cadastroDisciplina);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparDisciplinaMouseClicked

    private void btnConcluirDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirDisciplinaMouseClicked
        if(Utilitarios.validarCampos(cadastroDisciplina)){
            Disciplina disciplina = new Disciplina();
            Membro membro = (Membro) boxMembroDisciplina.getSelectedItem();
            disciplina.setCpf(membro.getCPF());
            disciplina.setDsc(dscDisciplina.getText());
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataInicio.getText()).getTime());
                disciplina.setDataInicio(sqlDate);
                sqlDate = new java.sql.Date(padraoData.parse(dataFim.getText()).getTime());
                disciplina.setDataFim(sqlDate);
                sqlDate = null;
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(5);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            Disciplina.cadastrarDisciplina(disciplina);
            Utilitarios.limparField(cadastroDisciplina);
            Utilitarios.atualizarPainel(desk,disciplinas);
            atualizarTabelas();
            logoHeaderDesk.setText("CMS - Disciplinas");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirDisciplinaMouseClicked

    private void btnConcluirDesligamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirDesligamentoMouseClicked
        if(Utilitarios.validarCampos(cadastroDesligamento)){
            Desligamento desligamento = new Desligamento();
            Membro membro = (Membro) boxMembroDesligamento.getSelectedItem();
            desligamento.setCpf(membro.getCPF());
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataDesligamento.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            desligamento.setData(sqlDate);
            sqlDate = null;
            desligamento.setDsc(dscDesligamento.getText());
            Desligamento.cadastrarDesligamento(desligamento);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(9);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            Utilitarios.atualizarPainel(desk,desligamentos);
            Utilitarios.limparField(cadastroDesligamento);
            atualizarTabelas();
            atualizarComboBox();
            logoHeaderDesk.setText("CMS - Desligamentos");
        }
        else{
            JOptionPane.showMessageDialog(null,"Preencha Todos os Campos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirDesligamentoMouseClicked

    private void btnCancelarDesligamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarDesligamentoMouseClicked
        Utilitarios.atualizarPainel(desk,desligamentos);
        Utilitarios.limparField(cadastroDesligamento);
        logoHeaderDesk.setText("CMS - Desligamentos");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarDesligamentoMouseClicked

    private void btnLimparDesligamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparDesligamentoMouseClicked
        Utilitarios.limparField(cadastroDesligamento);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparDesligamentoMouseClicked

    private void btnConcluirConsagracaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirConsagracaoMouseClicked
        if(Utilitarios.validarCampos(cadastroConsagracao)){
            Consagracao consagracao = new Consagracao();
            Cargos cargo = (Cargos) boxCargoConsagracao.getSelectedItem();
            Membro membro = (Membro) boxMembroConsagracao.getSelectedItem();
            consagracao.setCpf(membro.getCPF());
            consagracao.setCargos_idCargo(cargo.getId());
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataConsagracao.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            consagracao.setData(sqlDate);
            sqlDate = null;
            consagracao.setDsc(dscConsagracao.getText());
            Consagracao.cadastrarConsagracao(consagracao);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(7);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            Utilitarios.atualizarPainel(desk,consagracoes);
            Utilitarios.limparField(cadastroConsagracao);
            atualizarTabelas();
            logoHeaderDesk.setText("CMS - Consagrações");
            Utilitarios.atualizarPainel(desk,consagracoes);
            Utilitarios.limparField(cadastroConsagracao);
        }
        else{
            JOptionPane.showMessageDialog(null,"Preencha Todos os Campos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirConsagracaoMouseClicked

    private void btnCancelarConsagracaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarConsagracaoMouseClicked
        Utilitarios.atualizarPainel(desk,consagracoes);
        Utilitarios.limparField(cadastroConsagracao);
        logoHeaderDesk.setText("CMS - Consagrações");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarConsagracaoMouseClicked

    private void btnLimparConsagracaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparConsagracaoMouseClicked
         Utilitarios.limparField(cadastroConsagracao);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparConsagracaoMouseClicked

    private void fotoAlterarMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoAlterarMembroMouseClicked
        caminhoFoto = Utilitarios.buscarFoto(docsPanel, fotoAlterarMembro);
        // TODO add your handling code here:
    }//GEN-LAST:event_fotoAlterarMembroMouseClicked

    private void btnConcluirAlterarMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirAlterarMembroMouseClicked
        if(caminhoFoto!=null){
            if(Utilitarios.validarCampos(alterarMembro) && Utilitarios.validarEmail(emailAlterarMembro.getText())){
                Membro membro = new Membro();
                membro.setNome(nomeAlterarMembro.getText());
                membro.setRG(rgAlterarMembro.getText());
                membro.setCPF(cpfAlterarMembro.getText());
                membro.setTelefone(telefoneAlterarMembro.getText());
                membro.setEndereco(enderecoAlterarMembro.getText());
                String naturalidade = naturalidadeEstadoAlterarMembro.getSelectedItem().toString() + "-" + naturalidadeMunicipioAlterarMembro.getSelectedItem().toString();
                membro.setNaturalidade(naturalidade);
                try {
                    sqlDate = new java.sql.Date(padraoData.parse(dataRegistroAlterarMembro.getText()).getTime());
                    membro.setDataRegistro(sqlDate);
                    sqlDate = new java.sql.Date(padraoData.parse(dataBatismoAlterarMembro.getText()).getTime());                    
                    membro.setDataBatismo(sqlDate);
                    sqlDate = new java.sql.Date(padraoData.parse(dataNascimentoAlterarMembro.getText()).getTime());
                    membro.setDataNascimento(sqlDate);
                    sqlDate = null;
                } catch (ParseException ex) {
                    Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
                }
                membro.setEmail(emailAlterarMembro.getText());
                membro.setNumDependentes(Integer.parseInt(dependentesAlterarMembro.getSelectedItem().toString()));
                EstadoCivil estadoCivil = (EstadoCivil) estadoCivilAlterarMembro.getSelectedItem();
                membro.setEstadoCivil(estadoCivil.getId());
                membro.setFoto(caminhoFoto);
                Acesso_Alteracao alteracao = new Acesso_Alteracao();
                alteracao.setAcesso_id(this.idAcesso);
                alteracao.setAlteracao_id(2);
                Acesso_Alteracao.cadastrarAcesso(alteracao);
                Membro.alterarMembro(membro);
                Utilitarios.limparField(alterarMembro);
                atualizarTabelas();
                atualizarComboBox();
                Utilitarios.atualizarPainel(desk, membros);
                Utilitarios.fotoDefault(fotoAlterarMembro);
                caminhoFoto = null;
                logoHeaderDesk.setText("CMS - Membros");
            }
            else{
                JOptionPane.showMessageDialog(null,"Preencha todos os campos!");
            }
        }else{
            JOptionPane.showMessageDialog(null,"Escolha uma foto");
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirAlterarMembroMouseClicked

    private void btnCancelarAlterarMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarAlterarMembroMouseClicked
        Utilitarios.atualizarPainel(desk, membros);
        Utilitarios.limparField(alterarMembro);
        logoHeaderDesk.setText("CMS - Membros");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarAlterarMembroMouseClicked

    private void btnLimparAlterarMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparAlterarMembroMouseClicked
        Utilitarios.limparField(alterarMembro);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparAlterarMembroMouseClicked

    private void btnAlterarMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlterarMembroMouseClicked
        atualizarCamposAlterarMembro();
        logoHeaderDesk.setText("CMS - Alterar Membro");
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarMembroMouseClicked

    private void tableDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDisciplinaMouseClicked
        if(evt.getClickCount() == 2){
            if(tableDisciplina.getSelectedRow()!= -1){
                String cpfTableDisciplina = tableDisciplina.getValueAt(tableDisciplina.getSelectedRow(),0).toString();
                atualizarCamposDisciplinaPerfil(Disciplina.perfilDisciplina(cpfTableDisciplina));                
                
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tableDisciplinaMouseClicked

    private void btnAlterarDisciplinaPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlterarDisciplinaPerfilMouseClicked
        logoHeaderDesk.setText("CMS - Alterar Disciplina");
        atualizarCamposAlterarDisciplina();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarDisciplinaPerfilMouseClicked

    private void btnConcluirAlterarDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirAlterarDisciplinaMouseClicked
        Membro membro = (Membro) boxMembroAlterarDisciplina.getSelectedItem();
        
        Disciplina disciplina = new Disciplina();
        disciplina.setCpf(membro.getCPF());
        disciplina.setDsc(dscAlterarDisciplina.getText());
        try {
            sqlDate = new java.sql.Date(padraoData.parse(dataAlterarDisciplinaInicio.getText()).getTime());
            disciplina.setDataInicio(sqlDate);
            sqlDate = new java.sql.Date(padraoData.parse(dataAlterarDisciplinaFim.getText()).getTime());
            disciplina.setDataFim(sqlDate);
            sqlDate = null;
        } catch (ParseException ex) {
            Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
        }
        Acesso_Alteracao alteracao = new Acesso_Alteracao();
        alteracao.setAcesso_id(this.idAcesso);
        alteracao.setAlteracao_id(6);
        Acesso_Alteracao.cadastrarAcesso(alteracao);
        Disciplina.atualizarDisciplina(disciplina);
        Utilitarios.atualizarPainel(desk, disciplinas);
        Utilitarios.limparField(alterarDisciplina);
        atualizarTabelas();
        logoHeaderDesk.setText("CMS - Disciplinas");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirAlterarDisciplinaMouseClicked

    private void btnCancelarAlterarDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarAlterarDisciplinaMouseClicked
        Utilitarios.atualizarPainel(desk, disciplinas);
        logoHeaderDesk.setText("CMS - Disciplinas");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarAlterarDisciplinaMouseClicked

    private void btnLimparAlterarDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparAlterarDisciplinaMouseClicked
        Utilitarios.limparField(alterarDisciplina);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparAlterarDisciplinaMouseClicked

    private void btnSairDisciplinaPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairDisciplinaPerfilMouseClicked
        Utilitarios.atualizarPainel(desk,disciplinas);
        logoHeaderDesk.setText("CMS - Disciplinas");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSairDisciplinaPerfilMouseClicked

    private void btnConcluirAlterarDesligamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirAlterarDesligamentoMouseClicked
        if(Utilitarios.validarCampos(alterarDesligamento)){
            Desligamento desligamento = new Desligamento();
            Membro membro = (Membro) boxMembroAlterarDesligamento.getSelectedItem();
            desligamento.setCpf(membro.getCPF());
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataAlterarDesligamento.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            desligamento.setData(sqlDate);
            sqlDate = null;
            desligamento.setDsc(dscAlterarDesligamento.getText());
            Desligamento.atualizarDesligamento(desligamento);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(10);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            atualizarTabelas();
            Utilitarios.limparField(alterarDesligamento);
            Utilitarios.atualizarPainel(desk, desligamentos);
            logoHeaderDesk.setText("CMS - Desligamentos");
        }
        else{
            JOptionPane.showMessageDialog(null,"Preencha Todos os Campos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirAlterarDesligamentoMouseClicked

    private void btnCancelarAlterarDesligamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarAlterarDesligamentoMouseClicked
        Utilitarios.atualizarPainel(desk,desligamentos);
        logoHeaderDesk.setText("CMS - Desligamentos");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarAlterarDesligamentoMouseClicked

    private void btnLimparAlterarDesligamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparAlterarDesligamentoMouseClicked
        Utilitarios.limparField(alterarMembro);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparAlterarDesligamentoMouseClicked

    private void tableDesligamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDesligamentoMouseClicked
        if(evt.getClickCount() == 2){
            if(tableDesligamento.getSelectedRow()!= -1){
                String idDesligamento = tableDesligamento.getValueAt(tableDesligamento.getSelectedRow(),0).toString();
                atualizarCamposDesligamentoPerfil(Desligamento.perfilDesligamento(idDesligamento));                
                
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tableDesligamentoMouseClicked

    private void btnSairDesligamentoPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairDesligamentoPerfilMouseClicked
        Utilitarios.atualizarPainel(desk,desligamentos);
        logoHeaderDesk.setText("CMS - Desligamentos");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSairDesligamentoPerfilMouseClicked

    private void btnAlterarDesligamentoPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlterarDesligamentoPerfilMouseClicked
        atualizarCamposDesligamentoAlterar();
        logoHeaderDesk.setText("CMS - Alterar Desligamento");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarDesligamentoPerfilMouseClicked

    private void tableConsagracaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableConsagracaoMouseClicked
        if(evt.getClickCount() == 2){
        if(tableConsagracao.getSelectedRow() != -1){
            String cpfTableConsagracao = tableConsagracao.getValueAt(tableConsagracao.getSelectedRow(),0).toString();
            atualizarCamposConsagracaoPerfil(Consagracao.perfilConsagracao(cpfTableConsagracao));
        
        
        }}
        // TODO add your handling code here:
    }//GEN-LAST:event_tableConsagracaoMouseClicked

    private void btnConcluirConsagracaoAlterarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirConsagracaoAlterarMouseClicked
        if(Utilitarios.validarCampos(alterarConsagracao)){
            //Alteração do dado Consagração a partir do CPF
            Consagracao consagracao = new Consagracao();
            Cargos cargo = (Cargos) boxCargosConsagracaoAlterar.getSelectedItem();
            Membro membro = (Membro) boxMembroConsagracaoAlterar.getSelectedItem();
            consagracao.setCargos_idCargo(cargo.getId());
            consagracao.setCpf(membro.getCPF());
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataConsagracaoAlterar.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            consagracao.setData(sqlDate);
            sqlDate = null;
            consagracao.setDsc(dscConsagracaoAlterar.getText());
            Consagracao.atualizarConsagracao(consagracao);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(8);
            Acesso_Alteracao.cadastrarAcesso(alteracao);

            logoHeaderDesk.setText("CMS - Consagrações");
            //Limpeza e Atualização
            atualizarTabelas();
            Utilitarios.limparField(alterarConsagracao);
            Utilitarios.atualizarPainel(desk,consagracoes);
        }
        else{
            JOptionPane.showMessageDialog(null,"Preencha Todos os Campos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirConsagracaoAlterarMouseClicked

    private void btnCancelarConsagracaoAlterarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarConsagracaoAlterarMouseClicked
        Utilitarios.atualizarPainel(desk, consagracoes);
        Utilitarios.limparField(alterarConsagracao);
        logoHeaderDesk.setText("CMS - Membros");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarConsagracaoAlterarMouseClicked

    private void btnLimparConsagracaoAlterarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparConsagracaoAlterarMouseClicked
        Utilitarios.limparField(alterarConsagracao);
                
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparConsagracaoAlterarMouseClicked

    private void btnAlterarConsagracaoPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlterarConsagracaoPerfilMouseClicked
        atualizarCamposConsagracaoAlterar();
        logoHeaderDesk.setText("CMS - Alterar Consagração");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarConsagracaoPerfilMouseClicked

    private void btnSairConsagracaoPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairConsagracaoPerfilMouseClicked
        Utilitarios.atualizarPainel(desk, consagracoes);
        logoHeaderDesk.setText("CMS - Consagrações");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSairConsagracaoPerfilMouseClicked

    private void tableMembrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMembrosMouseClicked
        if(evt.getClickCount() == 2){
            if(tableMembros.getSelectedRow()!= -1){
                String cpfTableMembro = tableMembros.getValueAt(tableMembros.getSelectedRow(),0).toString();
                atualizarCamposMembroPerfil(Membro.perfilMembro(cpfTableMembro));                
                
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tableMembrosMouseClicked

    private void tableMembrosDisciplinadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMembrosDisciplinadosMouseClicked
      
        if(evt.getClickCount() == 2){
            if(tableMembrosDisciplinados.getSelectedRow()!= -1){
                String cpfTableMembro = tableMembrosDisciplinados.getValueAt(tableMembrosDisciplinados.getSelectedRow(),0).toString();
                atualizarCamposMembroPerfil(Membro.perfilMembro(cpfTableMembro));                
                
            }
        }
    }//GEN-LAST:event_tableMembrosDisciplinadosMouseClicked

    private void tableMembrosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMembrosMouseExited

        // TODO add your handling code here:
    }//GEN-LAST:event_tableMembrosMouseExited

    private void logoPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoPanelMouseEntered
         if(menuMembrosPop.isVisible() || menuFinancasPop.isVisible()){
            menuMembrosPop.setVisible(false);
            menuFinancasPop.setVisible(false);   
         }
    }//GEN-LAST:event_logoPanelMouseEntered

    private void deskMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deskMouseEntered
        if(menuMembrosPop.isVisible() || menuFinancasPop.isVisible() || menuContasPop.isVisible() || menuRelatoriosPop.isVisible() ){
            menuMembrosPop.setVisible(false);
            menuFinancasPop.setVisible(false);
            menuContasPop.setVisible(false);
            menuRelatoriosPop.setVisible(false);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_deskMouseEntered

    private void filtroMembrosAtivosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroMembrosAtivosKeyReleased
        filtroTable(filtroMembrosAtivos.getText(),tableMembros);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroMembrosAtivosKeyReleased

    private void filtroMembrosDisciplinadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroMembrosDisciplinadosKeyReleased
        filtroTable(filtroMembrosDisciplinados.getText(),tableMembrosDisciplinados);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroMembrosDisciplinadosKeyReleased

    private void filtroMembrosDesligadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroMembrosDesligadosKeyReleased
        filtroTable(filtroMembrosDesligados.getText(),tableMembrosDesligados);
// TODO add your handling code here:
    }//GEN-LAST:event_filtroMembrosDesligadosKeyReleased

    private void filtroCpfDisciplinaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroCpfDisciplinaKeyReleased
        filtroTable(filtroCpfDisciplina.getText(),tableDisciplina);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroCpfDisciplinaKeyReleased

    private void filtroCpfDesligamentosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroCpfDesligamentosKeyReleased
        filtroTable(filtroCpfDesligamentos.getText(),tableDesligamento);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroCpfDesligamentosKeyReleased

    private void filtroCpfConsagracoesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroCpfConsagracoesKeyReleased
        filtroTable(filtroCpfConsagracoes.getText(),tableConsagracao);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroCpfConsagracoesKeyReleased

    private void logoPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoPanelMouseClicked
        Utilitarios.atualizarPainel(desk, home);
        logoHeaderDesk.setText("CMS - Inicio");
        // TODO add your handling code here:
    }//GEN-LAST:event_logoPanelMouseClicked

    private void cadastrarDespesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarDespesaActionPerformed
        Utilitarios.atualizarPainel(desk, cadastroDespesa);
        logoHeaderDesk.setText("CMS - Cadastro de Despesa");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarDespesaActionPerformed

    private void tableCaixasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCaixasMouseClicked
       
        if(evt.getClickCount() == 2){
             
        if(tableCaixas.getSelectedRow() != -1){
            
            
            int idCaixa =  Integer.parseInt(tableCaixas.getValueAt(tableCaixas.getSelectedRow(),0).toString());
            Caixas caixa = Caixas.buscarCaixa(idCaixa);
            atualizarCamposCaixaPerfil(caixa);
            logoHeaderDesk.setText("CMS - Caixa " + Setores.buscarSetor(caixa.getSetores_idSetores()));
        
        
        }}
        // TODO add your handling code here:
    }//GEN-LAST:event_tableCaixasMouseClicked

    private void tableSetoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSetoresMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableSetoresMouseClicked

    private void btnConcluirSetorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirSetorMouseClicked
        if(caminhoFoto!=null){
            if(Utilitarios.validarCampos(cadastroSetor)){
                Setores setor = new Setores();
                setor.setFoto(caminhoFoto);
                try {
                    sqlDate = new java.sql.Date(padraoData.parse(dataSetor.getText()).getTime());
                } catch (ParseException ex) {
                    Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
                }
                setor.setData(sqlDate);
                sqlDate = null;
                setor.setNome(nomeSetor.getText());
                setor.setDsc(dscSetor.getText());
                Setores.cadastrarSetor(setor);
                Acesso_Alteracao alteracao = new Acesso_Alteracao();
                alteracao.setAcesso_id(this.idAcesso);
                alteracao.setAlteracao_id(14);
                Acesso_Alteracao.cadastrarAcesso(alteracao);
                atualizarTabelas();
                atualizarComboBox();
                caminhoFoto = null;
                Utilitarios.limparField(cadastroSetor);
                Utilitarios.fotoDefault(fotoSetor);
                Utilitarios.atualizarPainel(desk, caixasESetores);
            }
            else{
                JOptionPane.showMessageDialog(null,"Preencha Todos os Campos");
            }
        }else{
            JOptionPane.showMessageDialog(null,"Escolha uma Foto :'(");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirSetorMouseClicked

    private void btnLimparSetorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparSetorMouseClicked
        Utilitarios.limparField(cadastroSetor);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparSetorMouseClicked

    private void fotoSetorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoSetorMouseClicked
        caminhoFoto = Utilitarios.buscarFoto(cadastroSetor, fotoSetor);
        // TODO add your handling code here:
    }//GEN-LAST:event_fotoSetorMouseClicked

    private void btnLimparCaixaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparCaixaMouseClicked
        Utilitarios.limparField(cadastroCaixa);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparCaixaMouseClicked

    private void btnConcluirCaixasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirCaixasMouseClicked
        if(Utilitarios.validarCampos(cadastroCaixa)){
            Caixas caixa = new Caixas();
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataCaixa.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            caixa.setData(sqlDate);
            sqlDate = null;
            caixa.setDsc(dscCaixa.getText());
            Setores setor = (Setores) boxSetoresCaixa.getSelectedItem();
            caixa.setSetores_idSetores(setor.getId());
            String receita = receitaCaixa.getText().replaceAll("\\.","");
            receita = receita.replace(",", ".");
            caixa.setReceita(Double.parseDouble(receita));
            Caixas.cadastrarCaixa(caixa);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(11);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            Utilitarios.limparField(cadastroCaixa);
            Utilitarios.atualizarPainel(desk, caixasESetores);
            atualizarTabelas();
            atualizarComboBox();
            logoHeaderDesk.setText("CMS - Caixas e Setores");
        }
        else{
            JOptionPane.showMessageDialog(null,"Preencha Todos os Campos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirCaixasMouseClicked

    private void menuCaixasSetoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCaixasSetoresMouseClicked
        Utilitarios.atualizarPainel(desk, caixasESetores);
        logoHeaderDesk.setText("CMS - Caixas E Setores");
        menuFinancasPop.setVisible(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_menuCaixasSetoresMouseClicked

    private void cadastrarCaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarCaixaActionPerformed
        Utilitarios.atualizarPainel(desk,cadastroCaixa);
        logoHeaderDesk.setText("CMS - Cadastro de Caixa");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarCaixaActionPerformed

    private void cadastrarSetorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarSetorActionPerformed
        Utilitarios.atualizarPainel(desk,cadastroSetor);
        logoHeaderDesk.setText("CMS - Cadastro de Setor");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarSetorActionPerformed

    private void btnCancelarSetorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarSetorMouseClicked
        Utilitarios.atualizarPainel(desk,caixasESetores);
        logoHeaderDesk.setText("CMS - Caixas e Setores");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarSetorMouseClicked

    private void btnCancelarMembro2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMembro2MouseClicked
        Utilitarios.limparField(caixasESetores);
        Utilitarios.atualizarPainel(desk,caixasESetores);
        logoHeaderDesk.setText("CMS - Caixas e Setores");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarMembro2MouseClicked

    private void tableDespesasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDespesasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableDespesasMouseClicked

    private void btnConcluirDepesaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirDepesaMouseClicked
        if(Utilitarios.validarCampos(cadastroDespesa)){
            Despesa despesa = new Despesa();
            Setores setor = (Setores) boxSetorDespesa.getSelectedItem();
            despesa.setSetores_idSetores(setor.getId());
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataDespesa.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            despesa.setData(sqlDate);
            sqlDate = null;
            despesa.setDsc(dscDespesa.getText());
            String receita = valorDespesa.getText().replaceAll("\\.","");
            receita = receita.replace(",", ".");
            despesa.setValor(Double.parseDouble(receita));
            Despesa.cadastrarDespesa(despesa);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(12);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            atualizarTabelas();
            atualizarComboBox();
            Utilitarios.limparField(cadastroDespesa);
            Utilitarios.atualizarPainel(desk, despesas);
            logoHeaderDesk.setText("CMS - Despesas");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirDepesaMouseClicked

    private void btnCancelarDespesaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarDespesaMouseClicked
        Utilitarios.limparField(cadastroDespesa);
        Utilitarios.atualizarPainel(desk, despesas);
        logoHeaderDesk.setText("CMS - Despesas");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarDespesaMouseClicked

    private void btnLimparDepesaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparDepesaMouseClicked
        Utilitarios.limparField(cadastroDespesa);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparDepesaMouseClicked

    private void menuDespesasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDespesasMouseClicked
        Utilitarios.atualizarPainel(desk, despesas);
        menuFinancasPop.setVisible(false);
        logoHeaderDesk.setText(("CMS - Despesas"));
        
        // TODO add your handling code here:
    }//GEN-LAST:event_menuDespesasMouseClicked

    private void cadastrarOfertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarOfertaActionPerformed
        Utilitarios.atualizarPainel(desk, cadastroOferta);
        logoHeaderDesk.setText("CMS - Cadastro de Oferta");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarOfertaActionPerformed

    private void menuOfertasEDizimosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuOfertasEDizimosMouseClicked
        Utilitarios.atualizarPainel(desk, ofertasEDizimos);
        menuFinancasPop.setVisible(false);
        logoHeaderDesk.setText("CMS - Ofertas e Dizímos");
        // TODO add your handling code here:
    }//GEN-LAST:event_menuOfertasEDizimosMouseClicked

    private void tableOfertasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOfertasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableOfertasMouseClicked

    private void btnConcluirOfertaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirOfertaMouseClicked
        if(Utilitarios.validarCampos(cadastroOferta)){
            Oferta oferta = new Oferta();
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataOferta.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            oferta.setData(sqlDate);
            sqlDate = null;
            oferta.setDsc(dscOferta.getText());
            String receita = valorOferta.getText().replaceAll("\\.","");
            receita = receita.replace(",", ".");
            oferta.setValor(Double.parseDouble(receita));
            Setores setor = (Setores) boxSetorOferta.getSelectedItem();
            oferta.setSetores_idSetores(setor.getId());
            Oferta.cadastrarOferta(oferta);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(13);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            Utilitarios.limparField(cadastroOferta);
            Utilitarios.atualizarPainel(desk, ofertasEDizimos);
            atualizarTabelas();
            atualizarComboBox();
            logoHeaderDesk.setText("CMS - Ofertas e Dizimos");
        }
        else{
            JOptionPane.showMessageDialog(null,"Preencha Todos os Campos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirOfertaMouseClicked

    private void btnCancelarOfertaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarOfertaMouseClicked
        Utilitarios.limparField(cadastroOferta);
        Utilitarios.atualizarPainel(desk, ofertasEDizimos);
        logoHeaderDesk.setText("CMS - Ofertas e Dizimos ");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarOfertaMouseClicked

    private void btnLimparOfertaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparOfertaMouseClicked
        Utilitarios.limparField(cadastroOferta);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparOfertaMouseClicked

    private void cadastrarDizimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarDizimoActionPerformed
        Utilitarios.atualizarPainel(desk, cadastroDizimo);
        logoHeaderDesk.setText("CMS - Cadastro de Dizimo");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarDizimoActionPerformed

    private void btnConcluirDizimoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirDizimoMouseClicked
        if(Utilitarios.validarCampos(cadastroDizimo)){
            Dizimos dizimo  =  new Dizimos();
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataDizimo.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            dizimo.setData(sqlDate);
            sqlDate = null;
            String receita = valorDizimo.getText().replaceAll("\\.","");
            receita = receita.replace(",", ".");
            dizimo.setValor(Double.parseDouble(receita));
            Membro membro = (Membro) boxMembroDizimo.getSelectedItem();
            dizimo.setPessoa_cpfPessoa(membro.getCPF());
            Dizimos.cadastrarDizimo(dizimo);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(15);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            Utilitarios.limparField(cadastroDizimo);
            Utilitarios.atualizarPainel(desk, ofertasEDizimos);
            atualizarTabelas();
            atualizarComboBox();
            logoHeaderDesk.setText("CMS - Ofertas e Dizimos");
        }
        else{
            JOptionPane.showMessageDialog(null,"Preencha Todos os Campos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirDizimoMouseClicked

    private void btnCancelarDizimoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarDizimoMouseClicked
        Utilitarios.limparField(cadastroDizimo);
        Utilitarios.atualizarPainel(desk,ofertasEDizimos);
        logoHeaderDesk.setText("CMS - Ofertas e Dizimos");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarDizimoMouseClicked

    private void btnLimparDizimoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparDizimoMouseClicked
        Utilitarios.limparField(cadastroDizimo);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparDizimoMouseClicked

    private void tableDizimosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDizimosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableDizimosMouseClicked

    private void filtroSetoresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroSetoresKeyReleased
        filtroTable(filtroSetores.getText(),tableSetores);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroSetoresKeyReleased

    private void filtroCaixasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroCaixasKeyReleased
        filtroTable(filtroCaixas.getText(),tableCaixas);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroCaixasKeyReleased

    private void filtroDespesasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroDespesasKeyReleased
        filtroTable(filtroDespesas.getText(),tableDespesas);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroDespesasKeyReleased

    private void filtroDizimosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroDizimosKeyReleased
        filtroTable(filtroDizimos.getText(),tableDizimos);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroDizimosKeyReleased

    private void filtroOfertasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroOfertasKeyReleased
        filtroTable(filtroOfertas.getText(),tableOfertas);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroOfertasKeyReleased

    private void filtroCaixaDespesaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroCaixaDespesaKeyReleased
        filtroTable(filtroCaixaDespesa.getText(),tableCaixaDespesas);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroCaixaDespesaKeyReleased

    private void filtroCaixaOfertaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroCaixaOfertaKeyReleased
        filtroTable(filtroCaixaOferta.getText(),tableCaixaOfertas);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroCaixaOfertaKeyReleased

    private void menuConsagracaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuConsagracaoMouseClicked
        Utilitarios.atualizarPainel(desk, consagracoes);
        menuMembrosPop.setVisible(false);
        logoHeaderDesk.setText("CMS - Consagrações");
        // TODO add your handling code here:
    }//GEN-LAST:event_menuConsagracaoMouseClicked

    private void cadastrarConsagracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarConsagracaoActionPerformed
        Utilitarios.atualizarPainel(desk, cadastroConsagracao);
        logoHeaderDesk.setText("CMS - Registrar Consagração");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarConsagracaoActionPerformed

    private void menuDesligamentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDesligamentosMouseClicked
        Utilitarios.atualizarPainel(desk,desligamentos);
        menuMembrosPop.setVisible(false);
        logoHeaderDesk.setText("CMS - Desligamentos");

        // TODO add your handling code here:
    }//GEN-LAST:event_menuDesligamentosMouseClicked

    private void cadastrarDesligamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarDesligamentoActionPerformed
        Utilitarios.atualizarPainel(desk,cadastroDesligamento);
        logoHeaderDesk.setText("CMS - Registrar Desligamento");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarDesligamentoActionPerformed

    private void menuDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDisciplinaMouseClicked
        Utilitarios.atualizarPainel(desk,disciplinas);
        menuMembrosPop.setVisible(false);
        logoHeaderDesk.setText("CMS - Disciplinas");
        // TODO add your handling code here:
    }//GEN-LAST:event_menuDisciplinaMouseClicked

    private void cadastrarDisciplinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarDisciplinaActionPerformed
        Utilitarios.atualizarPainel(desk,cadastroDisciplina);
        logoHeaderDesk.setText("CMS - Registrar Disciplina");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarDisciplinaActionPerformed

    private void menuMembrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuMembrosMouseClicked
        Utilitarios.atualizarPainel(desk,membros);
        logoHeaderDesk.setText("CMS - Membros");
        menuMembrosPop.setVisible(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_menuMembrosMouseClicked

    private void cadastrarMembroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarMembroActionPerformed
        Utilitarios.atualizarPainel(desk,cadastroMembros);
        logoHeaderDesk.setText("CMS - Cadastro de Membro");

        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarMembroActionPerformed

    private void btnExcluirMembroPerfil1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcluirMembroPerfil1MouseClicked
        Utilitarios.atualizarPainel(desk,cadastroConsagracao);
        Membro membro = Membro.perfilMembro(cpfMembroPerfil.getText());
        boxMembroConsagracao.getModel().setSelectedItem(membro);
        Utilitarios.limparField(perfilMembro);
        logoHeaderDesk.setText("CMS - Registrar Consagração");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExcluirMembroPerfil1MouseClicked

    private void cadastroDisciplinaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cadastroDisciplinaMouseMoved
        if(boxMembroDisciplina.getSelectedItem()!=null){
            Membro membro = (Membro) boxMembroDisciplina.getSelectedItem();
            nomeMembroDisciplina.setText(membro.getNome());
            fotoMembroDisciplina.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(), fotoMembroDisciplina));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroDisciplinaMouseMoved

    private void cadastroDesligamentoMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cadastroDesligamentoMouseMoved
       if(boxMembroDesligamento.getSelectedItem()!=null){
            Membro membro = (Membro) boxMembroDesligamento.getSelectedItem();
            nomeMembroDesligamento.setText(membro.getNome());
            fotoMembroDesligamento.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(),fotoMembroDesligamento));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroDesligamentoMouseMoved

    private void cadastroConsagracaoMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cadastroConsagracaoMouseMoved
        if(boxMembroConsagracao.getSelectedItem()!=null){
            Membro membro = (Membro) boxMembroConsagracao.getSelectedItem();
            nomeMembroConsagracao.setText(membro.getNome());
            fotoMembroConsagracao.setIcon(Utilitarios.redimensionarFoto(membro.getFoto(),fotoMembroConsagracao));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroConsagracaoMouseMoved

    private void cadastroDizimoMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cadastroDizimoMouseMoved
        if(boxMembroDizimo.getSelectedItem()!=null){
            Membro membro = (Membro) boxMembroDizimo.getSelectedItem();
            nomeMembroDizimo.setText(membro.getNome());
        }
            
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroDizimoMouseMoved

    private void sairPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sairPanelMouseClicked
        this.dispose();
        Acesso.atualizarAcesso(acesso);
        acesso = null;
        
        userLogin = new login();
        userLogin.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_sairPanelMouseClicked

    private void contaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contaPanelMouseClicked

        // TODO add your handling code here:
    }//GEN-LAST:event_contaPanelMouseClicked

    private void btnAlterarMembro1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlterarMembro1MouseClicked
        atualizarCamposAlterarUsuario();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarMembro1MouseClicked

    private void btnSairPerfilUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairPerfilUsuarioMouseClicked
        Utilitarios.atualizarPainel(desk, usuarios);
        logoHeaderDesk.setText("CMS - Usuarios");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSairPerfilUsuarioMouseClicked

    private void cadastrarContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarContaActionPerformed
        Utilitarios.atualizarPainel(desk, cadastroUsuario);
        logoHeaderDesk.setText("CMS - Cadastro de Usuario");
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarContaActionPerformed

    private void menuContasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuContasMouseClicked
        menuContasPop.setVisible(false);
        Utilitarios.atualizarPainel(desk,usuarios);
        logoHeaderDesk.setText("CMS - Usuarios");
        // TODO add your handling code here:
    }//GEN-LAST:event_menuContasMouseClicked

    private void btnConcluirMembro1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirMembro1MouseClicked
        if(Utilitarios.validarCampos(cadastroUsuario)){
            Usuario usuario = new Usuario();
            Membro membro = (Membro) boxMembroUsuario.getSelectedItem();
            usuario.setCpf(membro.getCPF());
            ModoUsuario modo = (ModoUsuario) boxModoUsuario.getSelectedItem();
            usuario.setModoUsuario(modo.getId());
            try {
                sqlDate = new java.sql.Date(padraoData.parse(dataConta.getText()).getTime());
            } catch (ParseException ex) {
                Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            usuario.setData(sqlDate);
            sqlDate = null;
            usuario.setLogin(loginUsuarioConta.getText());
            usuario.setPassword(senhaUsuarioConta.getText());
            Usuario.cadastrarUsuario(usuario);
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(this.idAcesso);
            alteracao.setAlteracao_id(3);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
            atualizarTabelas();
            atualizarComboBox();
            logoHeaderDesk.setText("CMS - Usuarios");
            Utilitarios.atualizarPainel(desk, usuarios);
            Utilitarios.limparField(cadastroUsuario);
            Utilitarios.fotoDefault(fotoUsuario);
       
        }else{
            JOptionPane.showMessageDialog(null,"Preencha todos Os Campos");
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirMembro1MouseClicked

    private void btnCancelarMembro1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMembro1MouseClicked
        Utilitarios.limparField(cadastroUsuario);
        Utilitarios.atualizarPainel(desk, usuarios);
        Utilitarios.fotoDefault(fotoUsuario);
        logoHeaderDesk.setText("CMS - Usuarios");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarMembro1MouseClicked

    private void btnLimparMembro1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparMembro1MouseClicked
        Utilitarios.limparField(cadastroUsuario);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparMembro1MouseClicked

    private void tableUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableUsuariosMouseClicked
        if(evt.getClickCount() == 2){
        if(tableUsuarios.getSelectedRow() != -1){
            String loginUsuario =  tableUsuarios.getValueAt(tableUsuarios.getSelectedRow(),0).toString();
            atualizarCamposPerfilUsuario(Usuario.perfilUsuario(loginUsuario));
        }}
        // TODO add your handling code here:
    }//GEN-LAST:event_tableUsuariosMouseClicked

    private void filtroUsuariosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroUsuariosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroUsuariosKeyReleased

    private void menuSuaContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSuaContaActionPerformed
        menuContasPop.setVisible(false);
        Utilitarios.atualizarPainel(desk, perfilUsuario);
        logoHeaderDesk.setText("CMS - Perfil Usuario");
        
        atualizarCamposPerfilUsuario(Usuario.perfilUsuario(this.user));
    
        // TODO add your handling code here:
    }//GEN-LAST:event_menuSuaContaActionPerformed

    private void cadastroUsuarioMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cadastroUsuarioMouseMoved
        if(boxMembroUsuario.getSelectedItem()!=null){
            Membro membro = (Membro) boxMembroUsuario.getSelectedItem();
            atualizarCamposCadastroUsuario(membro);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroUsuarioMouseMoved

    private void fotoAlterarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoAlterarUsuarioMouseClicked
        caminhoFoto = Utilitarios.buscarFoto(alterarUsuario, fotoAlterarUsuario);
        // TODO add your handling code here:
    }//GEN-LAST:event_fotoAlterarUsuarioMouseClicked

    private void btnConcluirAlterarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirAlterarUsuarioMouseClicked
        if(caminhoFoto!=null){
            if(Utilitarios.validarCampos(alterarMembro)){
                Membro membro = new Membro();
                membro.setNome(nomeAlterarUsuario.getText());
                membro.setRG(rgAlterarUsuario.getText());
                membro.setCPF(cpfAlterarUsuario.getText());
                membro.setTelefone(telefoneAlterarUsuario.getText());
                membro.setEndereco(enderecoAlterarUsuario.getText());
                membro.setNaturalidade(naturalidadeAlterarUsuario.getText());
                
                try {
                    membro.setDataBatismo((Date) padraoData.parse(dataBatismoAlterarUsuario.getText()));
                    membro.setDataNascimento((java.sql.Date)padraoData.parse(dataNascimentoAlterarUsuario.getText()));
                    membro.setDataRegistro((Date) padraoData.parse(dataRegistroAlterarUsuario.getText()));
                } catch (ParseException ex) {
                    Logger.getLogger(mainSecretaria.class.getName()).log(Level.SEVERE, null, ex);
                }
                membro.setEmail(emailAlterarUsuario.getText());
                membro.setNumDependentes(Integer.parseInt(dependentesAlterarUsuario.getSelectedItem().toString()));
                EstadoCivil estadoCivil = (EstadoCivil) estadoCivilAlterarUsuario.getSelectedItem();
                membro.setEstadoCivil(estadoCivil.getId());
                membro.setFoto(caminhoFoto);
                Membro.alterarMembro(membro);
                Acesso_Alteracao alteracao = new Acesso_Alteracao();
                alteracao.setAcesso_id(this.idAcesso);
                alteracao.setAlteracao_id(4);
                Acesso_Alteracao.cadastrarAcesso(alteracao);
                
                Usuario usuario = new Usuario();
                usuario.setPassword(novaSenhaAlterarUsuario.getText());
                usuario.setLogin(loginAlterarUsuario.getText());
                ModoUsuario modo = (ModoUsuario) boxModoUsuarioAlterarUsuario.getSelectedItem();
                usuario.setModoUsuario(modo.getId());
                Usuario.alterarUsuario(usuario);
                
                
                Utilitarios.limparField(alterarUsuario);
                atualizarTabelas();
                atualizarComboBox();
                Utilitarios.atualizarPainel(desk, usuarios);
                Utilitarios.fotoDefault(fotoAlterarUsuario);
                caminhoFoto = null;
                logoHeaderDesk.setText("CMS - Usuarios");
            }
        
        
        
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConcluirAlterarUsuarioMouseClicked

    private void btnCancelarAlterarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarAlterarUsuarioMouseClicked
        Utilitarios.atualizarPainel(desk,usuarios);
        logoHeaderDesk.setText("CMS - Usuarios");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarAlterarUsuarioMouseClicked

    private void btnLimparAlterarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparAlterarUsuarioMouseClicked
        Utilitarios.limparField(alterarUsuario);
       // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparAlterarUsuarioMouseClicked

    private void nomeUsuarioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nomeUsuarioMouseEntered
        if(boxMembroUsuario.getSelectedItem()!=null){
            Membro membro = (Membro) boxMembroUsuario.getSelectedItem();
            atualizarCamposCadastroUsuario(membro);
        }
    }//GEN-LAST:event_nomeUsuarioMouseEntered

    private void ajudaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ajudaPanelMouseClicked
        Utilitarios.atualizarPainel(desk,ajuda);
        logoHeaderDesk.setText("CMS - Ajuda");
        // TODO add your handling code here:
    }//GEN-LAST:event_ajudaPanelMouseClicked

    private void relatorioDesligamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioDesligamentosActionPerformed
        mainRelatorio main = new mainRelatorio("Relatorio Desligamentos");
        main.setVisible(true);
        main.relatorio = "relatorioDesligamentos";
        main.idAcesso = this.idAcesso;
        // TODO add your handling code here:
    }//GEN-LAST:event_relatorioDesligamentosActionPerformed

    private void relatorioCaixasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioCaixasActionPerformed
        Relatorios.relatorioSemParametro("relatorioCaixas");
        Acesso_Alteracao alteracao = new Acesso_Alteracao();
        alteracao.setAcesso_id(idAcesso);
        alteracao.setAlteracao_id(20);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_relatorioCaixasActionPerformed

    private void relatorioConsagracoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioConsagracoesActionPerformed
        mainRelatorio main = new mainRelatorio("Relatorio Consagracoes");
        main.setVisible(true);
        main.relatorio = "relatorioConsagracoes";
        main.idAcesso = this.idAcesso;
        // TODO add your handling code here:
    }//GEN-LAST:event_relatorioConsagracoesActionPerformed

    private void relatorioDespesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioDespesasActionPerformed
        mainRelatorio main = new mainRelatorio("Relatorio de Despesas");
        main.setVisible(true);
        main.relatorio = "relatorioDespesas";
        main.idAcesso = this.idAcesso;
        // TODO add your handling code here:
    }//GEN-LAST:event_relatorioDespesasActionPerformed

    private void relatorioMembrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioMembrosActionPerformed
        mainRelatorio main = new mainRelatorio("Relatorio de Membros");
        main.setVisible(true);
        main.relatorio = "relatorioMembros";
        main.idAcesso = this.idAcesso;
        // TODO add your handling code here:
    }//GEN-LAST:event_relatorioMembrosActionPerformed

    private void relatorioDizimosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioDizimosActionPerformed
        mainRelatorio main = new mainRelatorio("Relatorio de Dizimos");
        main.setVisible(true);
        main.relatorio = "relatorioDizimos";
        main.idAcesso = this.idAcesso;
        // TODO add your handling code here:
    }//GEN-LAST:event_relatorioDizimosActionPerformed

    private void relatorioOfertasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioOfertasActionPerformed
        mainRelatorio main = new mainRelatorio("Relatorio de Ofertas");
        main.setVisible(true);
        main.relatorio = "relatorioOfertas";
        main.idAcesso = this.idAcesso;
        // TODO add your handling code here:
    }//GEN-LAST:event_relatorioOfertasActionPerformed

    private void relatorioUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioUsuariosActionPerformed
        Relatorios.relatorioSemParametro("relatorioUsuarios");
        Acesso_Alteracao alteracao = new Acesso_Alteracao();
        alteracao.setAcesso_id(idAcesso);
        alteracao.setAlteracao_id(24);
    }//GEN-LAST:event_relatorioUsuariosActionPerformed

    private void carteiraMembroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carteiraMembroActionPerformed
        mainDocs main = new mainDocs();
        main.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_carteiraMembroActionPerformed

    private void fotoMembroDisciplinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoMembroDisciplinaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_fotoMembroDisciplinaMouseClicked

    private void fotoMembroDisciplinaPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoMembroDisciplinaPerfilMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_fotoMembroDisciplinaPerfilMouseClicked

    private void fotoMembroDisciplinaAlterarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoMembroDisciplinaAlterarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_fotoMembroDisciplinaAlterarMouseClicked

    private void logAcessosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logAcessosActionPerformed
        Utilitarios.atualizarPainel(desk, logPanel);
        logoHeaderDesk.setText("CMS - Log de Acessos");
        // TODO add your handling code here:
    }//GEN-LAST:event_logAcessosActionPerformed

    private void filtroTableAcessosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroTableAcessosKeyReleased
        filtroTable(filtroTableAcessos.getText(), tableAcessos);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroTableAcessosKeyReleased

    private void tableAcessosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAcessosMouseClicked
        if(evt.getClickCount() == 2){
            if(tableAcessos.getSelectedRow() != -1){
                int idAcesso =  (int) tableAcessos.getValueAt(tableAcessos.getSelectedRow(),0);
                atualizarCamposPerfilAcesso(idAcesso);
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tableAcessosMouseClicked

    private void filtroTableAlteracoesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroTableAlteracoesKeyReleased
        filtroTable(filtroTableAlteracoes.getText(),tableAlteracoes);
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroTableAlteracoesKeyReleased

    private void relatorioDisciplinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioDisciplinasActionPerformed
        mainRelatorio main = new mainRelatorio("Relatorio Disciplinas");
        main.setVisible(true);
        main.relatorio = "relatorioDisciplinas";
        main.idAcesso = this.idAcesso;
        // TODO add your handling code here:
    }//GEN-LAST:event_relatorioDisciplinasActionPerformed

    private void naturalidadeEstadoMembroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_naturalidadeEstadoMembroMouseExited
        
        // TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeEstadoMembroMouseExited

    private void naturalidadeEstadoMembroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_naturalidadeEstadoMembroMousePressed
        Estado estado = (Estado) naturalidadeEstadoMembro.getSelectedItem(); 
        atualizarMunicipios(estado);
        // TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeEstadoMembroMousePressed

    private void naturalidadeEstadoMembroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_naturalidadeEstadoMembroFocusGained
        Estado estado = (Estado) naturalidadeEstadoMembro.getSelectedItem(); 
        atualizarMunicipios(estado);
// TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeEstadoMembroFocusGained

    private void naturalidadeEstadoMembroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_naturalidadeEstadoMembroFocusLost
        Estado estado = (Estado) naturalidadeEstadoMembro.getSelectedItem(); 
        atualizarMunicipios(estado);

        // TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeEstadoMembroFocusLost

    private void naturalidadeEstadoAlterarMembroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_naturalidadeEstadoAlterarMembroFocusGained
        Estado estado = (Estado) naturalidadeEstadoAlterarMembro.getSelectedItem(); 
        atualizarMunicipios(estado);        
// TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeEstadoAlterarMembroFocusGained

    private void naturalidadeEstadoAlterarMembroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_naturalidadeEstadoAlterarMembroFocusLost
        Estado estado = (Estado) naturalidadeEstadoAlterarMembro.getSelectedItem(); 
        atualizarMunicipios(estado);        
        // TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeEstadoAlterarMembroFocusLost

    private void naturalidadeEstadoAlterarMembroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_naturalidadeEstadoAlterarMembroMouseExited
        Estado estado = (Estado) naturalidadeEstadoAlterarMembro.getSelectedItem(); 
        atualizarMunicipios(estado);        
        // TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeEstadoAlterarMembroMouseExited

    private void naturalidadeEstadoAlterarMembroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_naturalidadeEstadoAlterarMembroMousePressed
        Estado estado = (Estado) naturalidadeEstadoAlterarMembro.getSelectedItem(); 
        atualizarMunicipios(estado);        
        // TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeEstadoAlterarMembroMousePressed

    private void naturalidadeMunicipioAlterarMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_naturalidadeMunicipioAlterarMembroMouseClicked
        Estado estado = (Estado) naturalidadeEstadoAlterarMembro.getSelectedItem(); 
        atualizarMunicipios(estado);        
          // TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeMunicipioAlterarMembroMouseClicked

    private void naturalidadeMunicipioMembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_naturalidadeMunicipioMembroMouseClicked
        Estado estado = (Estado) naturalidadeEstadoMembro.getSelectedItem(); 
        atualizarMunicipios(estado);        
        // TODO add your handling code here:
    }//GEN-LAST:event_naturalidadeMunicipioMembroMouseClicked

    private void btnSairDesligamentoPerfil1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairDesligamentoPerfil1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSairDesligamentoPerfil1MouseClicked

    private void btnSairDesligamentoPerfil1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairDesligamentoPerfil1ActionPerformed
        Desligamento.religarMembro(boxMembroPerfilDesligamento.getSelectedItem().toString());
        Utilitarios.atualizarPainel(desk,desligamentos);
        logoHeaderDesk.setText("CMS - Desligamentos");
        this.atualizarTabelas();
        this.atualizarComboBox();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSairDesligamentoPerfil1ActionPerformed

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
            java.util.logging.Logger.getLogger(mainSecretaria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainSecretaria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainSecretaria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainSecretaria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new mainSecretaria().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel agendaLabel;
    private javax.swing.JPanel ajuda;
    private javax.swing.JLabel ajudaLabel;
    private javax.swing.JPanel ajudaPanel;
    private javax.swing.JPanel alterarConsagracao;
    private javax.swing.JPanel alterarDesligamento;
    private javax.swing.JPanel alterarDisciplina;
    private javax.swing.JPanel alterarMembro;
    private javax.swing.JPanel alterarUsuario;
    private javax.swing.JComboBox<Object> boxCargoConsagracao;
    private javax.swing.JComboBox<Object> boxCargosConsagracaoAlterar;
    private javax.swing.JComboBox<Object> boxCargosPerfilConsagracao;
    private javax.swing.JComboBox<Object> boxEstadoCivilUsuario;
    private javax.swing.JComboBox<Object> boxMembroAlterarDesligamento;
    private javax.swing.JComboBox<Object> boxMembroAlterarDisciplina;
    private javax.swing.JComboBox<Object> boxMembroConsagracao;
    private javax.swing.JComboBox<Object> boxMembroConsagracaoAlterar;
    private javax.swing.JComboBox<Object> boxMembroDesligamento;
    private javax.swing.JComboBox<Object> boxMembroDisciplina;
    private javax.swing.JComboBox<Object> boxMembroDizimo;
    private javax.swing.JComboBox<Object> boxMembroPerfilConsagracao;
    private javax.swing.JComboBox<Object> boxMembroPerfilDesligamento;
    private javax.swing.JComboBox<Object> boxMembroPerfilDisciplina;
    private javax.swing.JComboBox<Object> boxMembroUsuario;
    private javax.swing.JComboBox<Object> boxModoUsuario;
    private javax.swing.JComboBox<Object> boxModoUsuarioAlterarUsuario;
    private javax.swing.JComboBox<Object> boxModoUsuarioPerfil;
    private javax.swing.JComboBox<Object> boxSetorDespesa;
    private javax.swing.JComboBox<Object> boxSetorOferta;
    private javax.swing.JComboBox<Object> boxSetoresCaixa;
    private javax.swing.JComboBox<Object> boxUsuarios;
    private javax.swing.JToggleButton btnAlterarConsagracaoPerfil;
    private javax.swing.JButton btnAlterarDesligamentoPerfil;
    private javax.swing.JToggleButton btnAlterarDisciplinaPerfil;
    private javax.swing.JButton btnAlterarMembro;
    private javax.swing.JButton btnAlterarMembro1;
    private javax.swing.JButton btnCancelarAlterarDesligamento;
    private javax.swing.JToggleButton btnCancelarAlterarDisciplina;
    private javax.swing.JButton btnCancelarAlterarMembro;
    private javax.swing.JButton btnCancelarAlterarUsuario;
    private javax.swing.JToggleButton btnCancelarConsagracao;
    private javax.swing.JToggleButton btnCancelarConsagracaoAlterar;
    private javax.swing.JButton btnCancelarDesligamento;
    private javax.swing.JButton btnCancelarDespesa;
    private javax.swing.JToggleButton btnCancelarDisciplina;
    private javax.swing.JButton btnCancelarDizimo;
    private javax.swing.JButton btnCancelarMembro;
    private javax.swing.JButton btnCancelarMembro1;
    private javax.swing.JButton btnCancelarMembro2;
    private javax.swing.JButton btnCancelarOferta;
    private javax.swing.JButton btnCancelarSetor;
    private javax.swing.JButton btnConcluirAlterarDesligamento;
    private javax.swing.JToggleButton btnConcluirAlterarDisciplina;
    private javax.swing.JButton btnConcluirAlterarMembro;
    private javax.swing.JButton btnConcluirAlterarUsuario;
    private javax.swing.JButton btnConcluirCaixas;
    private javax.swing.JToggleButton btnConcluirConsagracao;
    private javax.swing.JToggleButton btnConcluirConsagracaoAlterar;
    private javax.swing.JButton btnConcluirDepesa;
    private javax.swing.JButton btnConcluirDesligamento;
    private javax.swing.JToggleButton btnConcluirDisciplina;
    private javax.swing.JButton btnConcluirDizimo;
    private javax.swing.JButton btnConcluirMembro;
    private javax.swing.JButton btnConcluirMembro1;
    private javax.swing.JButton btnConcluirOferta;
    private javax.swing.JButton btnConcluirSetor;
    private javax.swing.JButton btnDisciplinarMembroPerfil;
    private javax.swing.JButton btnExcluirMembroPerfil;
    private javax.swing.JButton btnExcluirMembroPerfil1;
    private javax.swing.JButton btnLimparAlterarDesligamento;
    private javax.swing.JToggleButton btnLimparAlterarDisciplina;
    private javax.swing.JButton btnLimparCaixa;
    private javax.swing.JToggleButton btnLimparConsagracao;
    private javax.swing.JToggleButton btnLimparConsagracaoAlterar;
    private javax.swing.JButton btnLimparDepesa;
    private javax.swing.JButton btnLimparDesligamento;
    private javax.swing.JToggleButton btnLimparDisciplina;
    private javax.swing.JButton btnLimparDizimo;
    private javax.swing.JButton btnLimparMembro;
    private javax.swing.JButton btnLimparMembro1;
    private javax.swing.JButton btnLimparOferta;
    private javax.swing.JButton btnLimparSetor;
    private javax.swing.JToggleButton btnSairConsagracaoPerfil;
    private javax.swing.JButton btnSairDesligamentoPerfil;
    private javax.swing.JButton btnSairDesligamentoPerfil1;
    private javax.swing.JToggleButton btnSairDisciplinaPerfil;
    private javax.swing.JButton btnSairMembro;
    private javax.swing.JButton btnSairPerfilUsuario;
    private javax.swing.JMenuItem cadastrarCaixa;
    private javax.swing.JMenuItem cadastrarConsagracao;
    private javax.swing.JMenuItem cadastrarConta;
    private javax.swing.JMenuItem cadastrarDesligamento;
    private javax.swing.JMenuItem cadastrarDespesa;
    private javax.swing.JMenuItem cadastrarDisciplina;
    private javax.swing.JMenuItem cadastrarDizimo;
    private javax.swing.JMenuItem cadastrarMembro;
    private javax.swing.JMenuItem cadastrarOferta;
    private javax.swing.JMenuItem cadastrarSetor;
    private javax.swing.JPanel cadastroCaixa;
    private javax.swing.JPanel cadastroConsagracao;
    private javax.swing.JPanel cadastroDesligamento;
    private javax.swing.JPanel cadastroDespesa;
    private javax.swing.JPanel cadastroDisciplina;
    private javax.swing.JPanel cadastroDizimo;
    private javax.swing.JPanel cadastroMembros;
    private javax.swing.JPanel cadastroOferta;
    private javax.swing.JPanel cadastroSetor;
    private javax.swing.JPanel cadastroUsuario;
    private javax.swing.JPanel caixasESetores;
    private javax.swing.JComboBox<Object> cargosMembroPerfil;
    private javax.swing.JMenuItem carteiraMembro;
    private javax.swing.JLabel confLabel;
    private javax.swing.JPanel consagracoes;
    private javax.swing.JLabel contaLabel;
    private javax.swing.JPanel contaPanel;
    private javax.swing.JFormattedTextField cpfAlterarMembro;
    private javax.swing.JFormattedTextField cpfAlterarUsuario;
    private javax.swing.JFormattedTextField cpfMembro;
    private javax.swing.JFormattedTextField cpfMembroPerfil;
    private javax.swing.JFormattedTextField cpfUsuario;
    private javax.swing.JFormattedTextField dataAlterarDesligamento;
    private javax.swing.JFormattedTextField dataAlterarDisciplinaFim;
    private javax.swing.JFormattedTextField dataAlterarDisciplinaInicio;
    private javax.swing.JFormattedTextField dataBatismoAlterarMembro;
    private javax.swing.JFormattedTextField dataBatismoAlterarUsuario;
    private javax.swing.JFormattedTextField dataBatismoMembro;
    private javax.swing.JFormattedTextField dataBatismoMembroPerfil;
    private javax.swing.JTextField dataBatismoUsuario;
    private javax.swing.JFormattedTextField dataCaixa;
    private javax.swing.JFormattedTextField dataConsagracao;
    private javax.swing.JFormattedTextField dataConsagracaoAlterar;
    private javax.swing.JTextField dataConsagracaoPerfil;
    private javax.swing.JFormattedTextField dataConta;
    private javax.swing.JFormattedTextField dataDesligamento;
    private javax.swing.JFormattedTextField dataDespesa;
    private javax.swing.JTextField dataDisciplinaPerfilFim;
    private javax.swing.JTextField dataDisciplinaPerfilInicio;
    private javax.swing.JTextField dataDisciplinaPerfilRegistro;
    private javax.swing.JFormattedTextField dataDizimo;
    private javax.swing.JFormattedTextField dataFim;
    private javax.swing.JFormattedTextField dataInicio;
    private javax.swing.JFormattedTextField dataNascimentoAlterarMembro;
    private javax.swing.JFormattedTextField dataNascimentoAlterarUsuario;
    private javax.swing.JFormattedTextField dataNascimentoMembro;
    private javax.swing.JFormattedTextField dataNascimentoMembroPerfil;
    private javax.swing.JTextField dataNascimentoUsuario;
    private javax.swing.JFormattedTextField dataOferta;
    private javax.swing.JFormattedTextField dataPerfilDesligamento;
    private javax.swing.JFormattedTextField dataRegistroAlterarMembro;
    private javax.swing.JFormattedTextField dataRegistroAlterarUsuario;
    private javax.swing.JFormattedTextField dataRegistroMembro;
    private javax.swing.JFormattedTextField dataRegistroMembroPerfil;
    private javax.swing.JTextField dataRegistroUsuario;
    private javax.swing.JFormattedTextField dataSetor;
    private javax.swing.JComboBox<String> dependentesAlterarMembro;
    private javax.swing.JComboBox<String> dependentesAlterarUsuario;
    private javax.swing.JComboBox<String> dependentesMembro;
    private javax.swing.JComboBox<String> dependentesMembroPerfil;
    private javax.swing.JComboBox<String> dependentesUsuario;
    private javax.swing.JPanel desk;
    private javax.swing.JPanel desligamentos;
    private javax.swing.JPanel despesas;
    private javax.swing.JPanel disciplinas;
    private javax.swing.JLabel docsLabel;
    private javax.swing.JPanel docsPanel;
    private javax.swing.JTextArea dscAlterarDesligamento;
    private javax.swing.JTextArea dscAlterarDisciplina;
    private javax.swing.JTextArea dscCaixa;
    private javax.swing.JTextArea dscConsagracao;
    private javax.swing.JTextArea dscConsagracaoAlterar;
    private javax.swing.JTextArea dscConsagracaoPerfil;
    private javax.swing.JTextArea dscDesligamento;
    private javax.swing.JTextArea dscDespesa;
    private javax.swing.JTextArea dscDisciplina;
    private javax.swing.JTextArea dscDisciplinaPerfil;
    private javax.swing.JTextArea dscDizimo;
    private javax.swing.JTextArea dscOferta;
    private javax.swing.JTextArea dscPerfilDesligamento;
    private javax.swing.JTextArea dscSetor;
    private javax.swing.JTextField emailAlterarMembro;
    private javax.swing.JTextField emailAlterarUsuario;
    private javax.swing.JTextField emailMembro;
    private javax.swing.JTextField emailMembroPerfil;
    private javax.swing.JTextField emailUsuario;
    private javax.swing.JTextField enderecoAlterarMembro;
    private javax.swing.JTextField enderecoAlterarUsuario;
    private javax.swing.JTextField enderecoMembro;
    private javax.swing.JTextField enderecoMembroPerfil;
    private javax.swing.JTextField enderecoUsuario;
    private javax.swing.JComboBox<Object> estadoCivilAlterarMembro;
    private javax.swing.JComboBox<Object> estadoCivilAlterarUsuario;
    private javax.swing.JComboBox<Object> estadoCivilMembro;
    private javax.swing.JComboBox<Object> estadoCivilMembroPerfil;
    private javax.swing.JFormattedTextField filtroCaixaDespesa;
    private javax.swing.JFormattedTextField filtroCaixaOferta;
    private javax.swing.JFormattedTextField filtroCaixas;
    private javax.swing.JFormattedTextField filtroCpfConsagracoes;
    private javax.swing.JFormattedTextField filtroCpfDesligamentos;
    private javax.swing.JFormattedTextField filtroCpfDisciplina;
    private javax.swing.JFormattedTextField filtroDespesas;
    private javax.swing.JFormattedTextField filtroDizimos;
    private javax.swing.JFormattedTextField filtroMembrosAtivos;
    private javax.swing.JFormattedTextField filtroMembrosDesligados;
    private javax.swing.JFormattedTextField filtroMembrosDisciplinados;
    private javax.swing.JFormattedTextField filtroOfertas;
    private javax.swing.JFormattedTextField filtroSetores;
    private javax.swing.JTextField filtroTableAcessos;
    private javax.swing.JTextField filtroTableAlteracoes;
    private javax.swing.JFormattedTextField filtroUsuarios;
    private javax.swing.JPanel financasPanel;
    private javax.swing.JLabel fotoAlterarMembro;
    private javax.swing.JLabel fotoAlterarUsuario;
    private javax.swing.JLabel fotoMembro;
    private javax.swing.JLabel fotoMembroConsagracao;
    private javax.swing.JLabel fotoMembroConsagracaoAlterar;
    private javax.swing.JLabel fotoMembroConsagracaoPerfil;
    private javax.swing.JLabel fotoMembroDesligamento;
    private javax.swing.JLabel fotoMembroDesligamentoAlterar;
    private javax.swing.JLabel fotoMembroDesligamentoPerfil;
    private javax.swing.JLabel fotoMembroDisciplina;
    private javax.swing.JLabel fotoMembroDisciplinaAlterar;
    private javax.swing.JLabel fotoMembroDisciplinaPerfil;
    private javax.swing.JLabel fotoMembroPerfil;
    private javax.swing.JLabel fotoSetor;
    private javax.swing.JLabel fotoUsuario;
    private javax.swing.JLabel fotoUsuarioPerfil;
    private javax.swing.JPanel headerDesk;
    private javax.swing.JPanel home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel labe222;
    private javax.swing.JLabel labe223;
    private javax.swing.JMenuItem logAcessos;
    private javax.swing.JPanel logAlteracoes;
    private javax.swing.JPanel logPanel;
    private javax.swing.JTextField loginAlterarUsuario;
    private javax.swing.JTextField loginUsuarioConta;
    private javax.swing.JTextField loginUsuarioPerfil;
    private javax.swing.JLabel logoHeaderDesk;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPanel logoPanel;
    private javax.swing.JPanel main;
    private javax.swing.JLabel membroLabel;
    private javax.swing.JPanel membroPanel;
    private javax.swing.JPanel membros;
    private javax.swing.JPanel menu;
    private javax.swing.JMenu menuCaixasSetores;
    private javax.swing.JMenu menuConsagracao;
    private javax.swing.JMenu menuContas;
    private javax.swing.JPopupMenu menuContasPop;
    private javax.swing.JMenu menuDesligamentos;
    private javax.swing.JMenu menuDespesas;
    private javax.swing.JMenu menuDisciplina;
    private javax.swing.JPopupMenu menuDocsPop;
    private javax.swing.JPopupMenu menuFinancasPop;
    private javax.swing.JMenu menuMembros;
    private javax.swing.JPopupMenu menuMembrosPop;
    private javax.swing.JMenu menuOfertasEDizimos;
    private javax.swing.JPopupMenu menuRelatoriosPop;
    private javax.swing.JMenuItem menuSuaConta;
    private javax.swing.JTextField naturalidadeAlterarUsuario;
    private javax.swing.JComboBox<Object> naturalidadeEstadoAlterarMembro;
    private javax.swing.JComboBox<Object> naturalidadeEstadoMembro;
    private javax.swing.JTextField naturalidadeMembroPerfil;
    private javax.swing.JComboBox<Object> naturalidadeMunicipioAlterarMembro;
    private javax.swing.JComboBox<Object> naturalidadeMunicipioMembro;
    private javax.swing.JTextField naturalidadeUsuario;
    private javax.swing.JTextField nomeAlterarMembro;
    private javax.swing.JTextField nomeAlterarUsuario;
    private javax.swing.JTextField nomeMembro;
    private javax.swing.JTextField nomeMembroConsagracao;
    private javax.swing.JTextField nomeMembroConsagracaoAlterar;
    private javax.swing.JTextField nomeMembroConsagracaoPerfil;
    private javax.swing.JTextField nomeMembroDesligamento;
    private javax.swing.JTextField nomeMembroDesligamentoAlterar;
    private javax.swing.JTextField nomeMembroDesligamentoPerfil;
    private javax.swing.JTextField nomeMembroDisciplina;
    private javax.swing.JTextField nomeMembroDisciplinaAlterar;
    private javax.swing.JTextField nomeMembroDisciplinaPerfil;
    private javax.swing.JTextField nomeMembroDizimo;
    private javax.swing.JTextField nomeMembroPerfil;
    private javax.swing.JTextField nomeSetor;
    private javax.swing.JTextField nomeUsuario;
    private javax.swing.JTextField nomeUsuarioPerfil;
    private javax.swing.JPasswordField novaSenhaAlterarUsuario;
    private javax.swing.JPanel ofertasEDizimos;
    private javax.swing.JPanel perfilCaixa;
    private javax.swing.JPanel perfilConsagracao;
    private javax.swing.JPanel perfilDesligamento;
    private javax.swing.JPanel perfilDisciplina;
    private javax.swing.JPanel perfilMembro;
    private javax.swing.JPanel perfilUsuario;
    private javax.swing.JFormattedTextField receitaCaixa;
    private javax.swing.JMenuItem relatorioCaixas;
    private javax.swing.JMenuItem relatorioConsagracoes;
    private javax.swing.JMenuItem relatorioDesligamentos;
    private javax.swing.JMenuItem relatorioDespesas;
    private javax.swing.JMenuItem relatorioDisciplinas;
    private javax.swing.JMenuItem relatorioDizimos;
    private javax.swing.JMenuItem relatorioMembros;
    private javax.swing.JMenuItem relatorioOfertas;
    private javax.swing.JMenuItem relatorioUsuarios;
    private javax.swing.JMenu relatoriosFinancas;
    private javax.swing.JMenu relatoriosMembros;
    private javax.swing.JPanel relatoriosPanel;
    private javax.swing.JMenu relatoriosUsuarios;
    private javax.swing.JFormattedTextField rgAlterarMembro;
    private javax.swing.JFormattedTextField rgAlterarUsuario;
    private javax.swing.JFormattedTextField rgMembro;
    private javax.swing.JFormattedTextField rgMembroPerfil;
    private javax.swing.JFormattedTextField rgUsuario;
    private javax.swing.JLabel sairLabel;
    private javax.swing.JPanel sairPanel;
    private javax.swing.JPasswordField senhaUsuarioConta;
    private javax.swing.JPasswordField senhaUsuarioPerfil;
    private javax.swing.JTable tableAcessos;
    private javax.swing.JTable tableAlteracoes;
    private javax.swing.JTable tableCaixaDespesas;
    private javax.swing.JTable tableCaixaOfertas;
    private javax.swing.JTable tableCaixas;
    private javax.swing.JTable tableConsagracao;
    private javax.swing.JTable tableDesligamento;
    private javax.swing.JTable tableDespesas;
    private javax.swing.JTable tableDisciplina;
    private javax.swing.JTable tableDizimos;
    private javax.swing.JTable tableMembros;
    private javax.swing.JTable tableMembrosDesligados;
    private javax.swing.JTable tableMembrosDisciplinados;
    private javax.swing.JTable tableOfertas;
    private javax.swing.JTable tableSetores;
    private javax.swing.JTable tableUsuarios;
    private javax.swing.JFormattedTextField telefoneAlterarMembro;
    private javax.swing.JFormattedTextField telefoneAlterarUsuario;
    private javax.swing.JFormattedTextField telefoneMembro;
    private javax.swing.JFormattedTextField telefoneMembroPerfil;
    private javax.swing.JFormattedTextField telefoneUsuario;
    private javax.swing.JPanel usuarios;
    private javax.swing.JFormattedTextField valorDespesa;
    private javax.swing.JFormattedTextField valorDizimo;
    private javax.swing.JFormattedTextField valorOferta;
    // End of variables declaration//GEN-END:variables
}
