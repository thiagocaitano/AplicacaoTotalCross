package br.com.tc.dao;

import java.sql.SQLException;

import br.com.tc.model.Produto;
import totalcross.sql.Connection;
import totalcross.sql.DriverManager;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;
import totalcross.sys.Convert;
import totalcross.sys.Settings;


public class ProdutoDAO {
	
	private Connection con = null;
//private PreparedStatement pSmt = null;
//private PreparedStatement pSmtInsert = null;
//private PreparedStatement  pSmtUpdate = null;
//private PreparedStatement  pSmtDelete   = null;
//private PreparedStatement  pSmtSelect   = null;
//private PreparedStatement  pSmtId   = null;
	
	public ProdutoDAO() throws SQLException{
	
		con = DriverManager.getConnection("jdbc:sqlite:" + Convert.appendPath(Settings.appPath, "crud.db"));

		PreparedStatement pSmt = con.prepareStatement("create table if not exists produto(id int primary key, nome varchar, valor decimal)");
		pSmt.executeUpdate();
	}

	public void insert(Produto p) throws SQLException {	
		PreparedStatement pSmtInsert = con.prepareStatement("insert into produto values(?, ?, ?)");
		pSmtInsert.clearParameters();
		p.setId(proximoId());
		pSmtInsert.setInt(1, p.getId());
		pSmtInsert.setString(2, p.getNome());
		pSmtInsert.setDouble(3, p.getValor());
		
		pSmtInsert.executeUpdate();
	}

	public void update(Produto p) throws SQLException {
		PreparedStatement pSmtUpdate = con.prepareStatement("UPDATE produto SET nome = ?,valor=? WHERE id = ?");
		pSmtUpdate.clearParameters();
		pSmtUpdate.setString(1, p.getNome());
		pSmtUpdate.setDouble(2, p.getValor());
		pSmtUpdate.setInt(3, p.getId());
		
		pSmtUpdate.executeUpdate();
	}

	public void delete(Produto p) throws SQLException {
		PreparedStatement pSmtDelete = con.prepareStatement("DELETE FROM produto WHERE id = ?");
		pSmtDelete.clearParameters();
		pSmtDelete.setInt(1, p.getId());
		
		pSmtDelete.executeUpdate();		
	}

	public String[][] getAll() throws SQLException {
		
		PreparedStatement pSmtSelect = con.prepareStatement("SELECT * FROM produto");
		ResultSet rs =  pSmtSelect.executeQuery();
		
	    int amount = 0;
		while(rs.next()){
			amount += 1;
		}
		
		String[][] retorno  = new String[amount][3];		
		
		rs = pSmtSelect.executeQuery();	
		for (int i = 0; i < amount; i++) {
			rs.next();
			for (int j = 0; j < 3; j++) {
				retorno[i][j] = rs.getString(j+1);		
			}
		}		
		rs.close();				
		return retorno;
	}
	
	public int proximoId() throws SQLException{		
		int retorno = 1;
		
		PreparedStatement pSmtId = con.prepareStatement("SELECT max(id) as  vId from produto");
		ResultSet rs = pSmtId.executeQuery();
		while(rs.next()){
			retorno = rs.getInt("vId") + 1;
		}
		rs.close();
		return retorno;
	}

}