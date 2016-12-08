package br.com.tc.view;

import java.sql.SQLException;

import br.com.tc.dao.ProdutoDAO;
import br.com.tc.model.Produto;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.Bar;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Grid;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;

public class ProdutoView extends Container {
	
		private static final int INSERE_BTN_ID = 100;
		private static final int ATUALIZA_BTN_ID = 101;
		private static final int DELETE_BTN_ID = 102;
		private static ProdutoDAO dao = null; 
		private Grid grid;
		
		public ProdutoView() throws SQLException{
			dao = new ProdutoDAO();
		}
		public void initUI(){
			
			Button btNovo = new Button("Novo");
			Button btAlterar = new Button("Alterar");
			Button btExcluir = new Button("Excluir");
			
			btNovo.appId = INSERE_BTN_ID;
			btAlterar.appId = ATUALIZA_BTN_ID;
			btExcluir.appId = DELETE_BTN_ID;

			Font f = Font.getFont(true,Font.NORMAL_SIZE+2);
			Bar h1 = new Bar("Produtos");
			h1.canSelectTitle = true;
			h1.setFont(f);
			h1.setBackForeColors(0x0A246A,Color.WHITE);
			add(h1, LEFT,0,FILL,PREFERRED);
			
			h1.add(btExcluir, RIGHT - 2, TOP + 2, PREFERRED+5 , FILL );
			h1.add(btAlterar, BEFORE - 2, SAME, PREFERRED+5 , FILL );
			h1.add(btNovo, BEFORE - 2, SAME, PREFERRED+5 , FILL );
			
			grid = new Grid(new String[]{"Id", "Nome","Valor"}, new int[]{-10, -30,-30}, new int[]{RIGHT, LEFT,LEFT}, false);
			
			add(grid, LEFT, AFTER + 2, FILL - 2, FILL - 2);
			try {
				grid.setItems(dao.getAll());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public void onEvent(Event event){
			switch (event.type) {
			case ControlEvent.PRESSED:
				switch (((Control) event.target).appId) {
					case INSERE_BTN_ID:
						new ProdutoCadastroView(dao /*EditMode.INSERT*/).popupNonBlocking();				
						break;
					case ATUALIZA_BTN_ID:{
						if(selecionouUmaPessoa()){
							new ProdutoCadastroView(dao, pegaPessoaSelecionada() /*EditMode.UPDATE*/).popupNonBlocking();
						}
						break;
					}
					case DELETE_BTN_ID:{
						if(selecionouUmaPessoa()){
							try {
								dao.delete(pegaPessoaSelecionada());
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								grid.setItems(dao.getAll());
							} catch (SQLException e) {
								e.printStackTrace();
							}
							grid.setSelectedIndex(-1);
						}
						break;
					}
				}			
				break;
			case ControlEvent.WINDOW_CLOSED:{
				if(event.target instanceof ProdutoCadastroView){
					try {
						grid.setItems(dao.getAll());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					grid.setSelectedIndex(-1);
				}
			}
			break;
			}		
		}
		
		private boolean selecionouUmaPessoa(){
			boolean selecionouUmaPessoa = grid.getSelectedIndex() >= 0;
			
			if(!selecionouUmaPessoa){
				new MessageBox("Atenção", "Nenhuma pessoa foi selecionada").popup();
			}		
			return selecionouUmaPessoa;
		}
		
		private Produto pegaPessoaSelecionada(){
			Produto p = new Produto();
			String[] colunas = grid.getSelectedItem();
			
			try{

				p.setId(Convert.toInt(colunas[0]));
				p.setNome(colunas[1]);
				p.setValor(Convert.toDouble(colunas[2]));

			}catch(InvalidNumberException e){
				e.printStackTrace();
			}
			
			return p;
		}
	
}