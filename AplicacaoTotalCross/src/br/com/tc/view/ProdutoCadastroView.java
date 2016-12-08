package br.com.tc.view;

import java.sql.SQLException;

import br.com.tc.dao.ProdutoDAO;
import br.com.tc.model.Produto;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.Bar;
import totalcross.ui.Button;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.Window;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;

public class ProdutoCadastroView extends Window {
	
	private static final int SALVAR_BTN_ID 		= 100;
	private static final int CANCELAR_BTN_ID 	= 101;
	private final ProdutoDAO dao;
	private Edit editNome;
	private Edit editValor;
	private Edit editId;
	
	public ProdutoCadastroView(ProdutoDAO dao) {
		this.dao = dao;
		this.appObj = new Produto();
	}
	
	public ProdutoCadastroView (ProdutoDAO dao, Produto produto){
		this.dao = dao;
		this.appObj = produto;
	}
	
	protected void postPopup(){
		super.postPopup();
		initUI();
	}
	
	public void initUI(){
		super.initUI();
		
		Button salvarButton = new Button("Salvar");
		Button cancelarButton = new Button("Cancelar");
		editId = new Edit();
		editNome = new Edit();
		editValor = new Edit();
//		editValor.setDecimalPlaces(2);
//		editValor.setMode(Edit.CURRENCY,true);
		
		salvarButton.appId = SALVAR_BTN_ID;
		cancelarButton.appId = CANCELAR_BTN_ID;
		editId.setEditable(false);
		
		Font f = Font.getFont(true,Font.NORMAL_SIZE+2);
		Bar h1 = new Bar("Manter Produto");
		h1.setFont(f);
		h1.setBackForeColors(0x0A246A,Color.WHITE);
		add(h1, LEFT,0,FILL,PREFERRED);
		
		h1.add(cancelarButton, RIGHT - 2, TOP + 2, PREFERRED+2, FILL);
		h1.add(salvarButton, BEFORE - 2, SAME, SAME, FILL);
		add(new Label("Nome"), LEFT, AFTER + 5);
		add(editNome, SAME, AFTER + 2);		
		add(new Label("Valor"), LEFT, AFTER + 5);
		add(editValor,SAME,AFTER+2,FILL,PREFERRED);
		
		preencherValores();
	}

	private void preencherValores() {
		editId.setText(Convert.toString(((Produto) appObj).getId()));
		editNome.setText(((Produto) appObj).getNome());
		editValor.setText(((Produto) appObj).getValor()!= null ? ((Produto) appObj).getValor().toString() : "0" );
	}
	
	public void onEvent(Event event){
		switch (event.type) {
			case ControlEvent.PRESSED:{
				switch (((Control) event.target).appId) {
					case SALVAR_BTN_ID:{
						if(edicaoFoiPreenchidaCorretamente()){
							Produto produto = (Produto) appObj;
							try {
								if(produto.getId()==0)
									dao.insert(produto);
								else
									dao.update(produto);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							new MessageBox("Confirmação", "Produto salvo com sucesso !").popup();
							
							unpop();
						}
						break;
					}
					case CANCELAR_BTN_ID:{
						unpop();
						break;
					}
				}
			}						
		}
	}
	
	public boolean edicaoFoiPreenchidaCorretamente(){
		if(!(this.editNome.getLength() > 0)){
			new MessageBox("Erro", "Nome inválido").popup();
			return false;
		}
		
		((Produto) appObj).setNome(this.editNome.getText());
		try {
			((Produto) appObj).setValor(Convert.toDouble(this.editValor.getText()));
		} catch (InvalidNumberException e) {
			e.printStackTrace();
		}
		return true;
	}

	
}
