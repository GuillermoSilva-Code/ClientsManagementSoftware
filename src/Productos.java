import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import net.proteanit.sql.DbUtils;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class Productos extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField text0Sucursal;
	private JButton btnAgregar;
	private JButton btnEditar;
	private JButton btnEliminar;
	private JButton btnAtrás;
	private JTextField textSKU;
	private JTextField textDes;
	private JTextField textCant;
	private JTextField textPrecio;
	private JLabel lblID;
	private JTextField textID;
	private JButton btnLimpiar;
	private JMenuBar menuBar;
	private JMenu mnIndice;
	private JMenuItem mntmMenu;
	private JMenuItem mntmAgregar;
	private JMenuItem mntmEditar;
	private JMenu mnAyuda;
	private JMenuItem mntmContratistas;
	private JLabel lblId_proyecto;
	private JTextField textID_proyecto;
	private JLabel lblTexto;
	private JTextField textOrden;
	private JLabel lblOrden;
	private JTextField text0Orden;
	private JLabel lblBuscar;
	private JSeparator separatorSubBuscar;
	private JSeparator separatorSubMat;
	private JTextField textMateriales;
	private JTextField textPrecioTotal;
	private JTextField textPrecioIva;
	private JSeparator separatorInv;
	private JTextField textTotalMateriales;
	private JSeparator separatorInv2;
	private JTextField text0Cliente;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Productos frame = new Productos();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void refresh() {
		buscar();
	}
	public void buscar() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
			String text = text0Sucursal.getText();
			if(text!= null && !text.isEmpty()) {
				//si no está vacio
				//System.out.println("nada vacío");
				String sql = "SELECT productos.ID, SKU, Descripcion AS 'Descripción', Cantidad AS 'Cant', Precio AS 'Precio U. sin IVA', "
						+ "(SELECT sucursales.Nombre FROM sucursales WHERE sucursales.ID = proyectos.id_sucursal) AS 'Scurusal', Orden, id_proyecto AS 'ID Proyecto' FROM productos INNER JOIN "
						+ "proyectos ON productos.id_proyecto = proyectos.ID WHERE proyectos.id_cliente = (SELECT clientes.ID FROM clientes WHERE clientes.Nombre "
						+ "LIKE ?) AND proyectos.id_sucursal = (SELECT sucursales.ID FROM sucursales WHERE sucursales.Nombre LIKE ? AND "
						+ "sucursales.id_cliente = (SELECT clientes.ID FROM clientes WHERE clientes.Nombre LIKE ?)) AND productos.Orden LIKE ?";

				PreparedStatement pst = con.prepareStatement(sql);
				pst.setString(1, "%" + text0Cliente.getText() + "%");
				pst.setString(2, "%" + text0Sucursal.getText() + "%");
				pst.setString(3, "%" + text0Cliente.getText() + "%");
				pst.setString(4, "%" + text0Orden.getText() + "%");
				
				ResultSet rs = pst.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rs));
			}else {
				String textCliente = text0Cliente.getText();
				if(textCliente!= null && !textCliente.isEmpty()) {
					//si sucursal está vacío, pero clientes no
					//System.out.println("sucursal vacío");
					String sql = "SELECT productos.ID, SKU, Descripcion AS 'Descripción', Cantidad AS 'Cant', Precio AS 'Precio U. sin IVA', sucursales.Nombre AS 'Sucursal', "
							+ "Orden, id_proyecto FROM productos INNER JOIN proyectos on proyectos.ID = productos.id_proyecto INNER JOIN sucursales ON sucursales.ID = proyectos.id_sucursal "
							+ "WHERE proyectos.id_cliente = (SELECT clientes.ID FROM clientes WHERE clientes.Nombre LIKE ?);";

					PreparedStatement pst = con.prepareStatement(sql);
					pst.setString(1, "%" + text0Cliente.getText() + "%");
					
					ResultSet rs = pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
				}else {
					//si está vacio
					//System.out.println("está vacío");
					String sql = "SELECT productos.ID, SKU, Descripcion AS 'Descripción', Cantidad AS 'Cant', Precio AS 'Precio U. sin IVA', sucursales.Nombre AS 'Sucursal', "
							+ "Orden, id_proyecto FROM productos INNER JOIN proyectos on proyectos.ID = productos.id_proyecto INNER JOIN sucursales ON sucursales.ID = proyectos.id_sucursal;";

					PreparedStatement pst = con.prepareStatement(sql);
					
					ResultSet rs = pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
				}

			}
			resizeColumnWidth(table);
			limpiar();
			textID_proyecto.setText("");
			textOrden.setText("");
			calcularMat();calcularCant();calcularTotal();calcularIva();
		}
		catch(Exception e2){
			JOptionPane.showMessageDialog(null, e2);
		}
	}
	public void limpiar() {
		textID.setText("");
		textSKU.setText("");
		textDes.setText("");
		textCant.setText("");
		textPrecio.setText("");
		//textSuc.setText("");
		//textOrden.setText("");
	}
	
	public void resizeColumnWidth(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 15; // Min width
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        if(width > 300)
	            width=300;
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}
	
	public void calcularCant() {
        int sum = 0;
        for(int i =0;i<table.getRowCount();i++){
            sum = sum +Integer.parseInt(table.getValueAt(i, 3).toString());
            //i es la iteración de las filas y 3 es el número de la columna
        }
        textTotalMateriales.setText(Integer.toString(sum));
	}
	public void calcularMat() {
        int sum = 0;
        for(int i =0;i<table.getRowCount();i++){
            sum = i + 1;
        }
        textMateriales.setText(Integer.toString(sum));
	}
	
	public void calcularTotal() {
		int sum = 0;
		try{
		       for(int i =0;i<table.getRowCount();i++){
		            sum = sum +(Integer.parseInt(table.getValueAt(i, 4).toString().replaceAll("[^0-9]", "")))*(Integer.parseInt(table.getValueAt(i, 3).toString()));
		        }
		        String sumText = Integer.toString(sum);
		        
				String decimal = sumText.substring(0,sumText.length()-2)+"."+sumText.substring(sumText.length()-2);
				double total = Double.parseDouble(decimal);
				DecimalFormat formatter = new DecimalFormat("#,###.00");
				
				textPrecioTotal.setText("$" + formatter.format(total));
		}
		catch(Exception E){
			textPrecioTotal.setText("$-");
		}
	}
	
	public void calcularIva() {
		int sum = 0;
		try {
	        for(int i =0;i<table.getRowCount();i++){
	            sum = sum +Integer.parseInt((table.getValueAt(i, 4).toString().replaceAll("[^0-9]", "")))*(Integer.parseInt(table.getValueAt(i, 3).toString()));
	        }
	        String sumText = Integer.toString(sum);
	        
			String decimal = sumText.substring(0,sumText.length()-2)+"."+sumText.substring(sumText.length()-2);
			double total = Double.parseDouble(decimal);
			DecimalFormat formatter = new DecimalFormat("#,###.00");
			total = ((21 * total)/100)+total;
			textPrecioIva.setText("$" + formatter.format(total));
		}
		catch(Exception E) {
			textPrecioIva.setText("$-");
		}
	}

	public Productos() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Productos.class.getResource("/iconos/glpi.png")));
		setTitle("Materiales");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1280, 514);
		
		menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.activeCaptionBorder);
		setJMenuBar(menuBar);
		
		mnIndice = new JMenu("\u00CDndice");
		mnIndice.setIcon(new ImageIcon(Productos.class.getResource("/iconos/menu24.png")));
		mnIndice.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnIndice);
		
		mntmMenu = new JMenuItem("Men\u00FA Principal");
		mntmMenu.setIcon(new ImageIcon(Productos.class.getResource("/iconos/casita24.png")));
		mntmMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final MainFrame main1 = new MainFrame();
				main1.setVisible(true);
				main1.setExtendedState(JFrame.MAXIMIZED_BOTH);
				//new MainFrame().setVisible(true);
				dispose();
			}
		});
		mnIndice.add(mntmMenu);
		
		mntmAgregar = new JMenuItem("Agregar Proyecto");
		mntmAgregar.setIcon(new ImageIcon(Productos.class.getResource("/iconos/addclient24.png")));
		mntmAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Agregar().setVisible(true);
				//setVisible(false);
			}
		});
		
		mntmContratistas = new JMenuItem("Contratistas");
		mntmContratistas.setIcon(new ImageIcon(Productos.class.getResource("/iconos/contratista24.png")));
		mntmContratistas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Contratistas().setVisible(true);
				dispose();
			}
		});
		mnIndice.add(mntmContratistas);
		mnIndice.add(mntmAgregar);
		
		mntmEditar = new JMenuItem("Editar Proyecto");
		mntmEditar.setIcon(new ImageIcon(Productos.class.getResource("/iconos/editclient24.png")));
		mntmEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Editar().setVisible(true);
				//setVisible(false);
			}
		});
		mnIndice.add(mntmEditar);
		mnAyuda = new JMenu("Ayuda");
		mnAyuda.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Para buscar los materiales ingrese el nombre de la <sucursal> y presione la tecla Enter o el botón Buscar.\n "
						+ "En caso de ser necesario, el sistema le permite ingresar el Número de Orden como segundo parámetro.\n "
						+ "También puede buscar materiales por su nombre o SKU, ingresando los mismos en el texto 'Sucursal',\n"
						+ "uno a la vez (no puede ingresar nombre y SKU en la misma búsqueda).", "Ayuda", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnAyuda);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable();
		table.setFont(new Font("Arial", Font.PLAIN, 12));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				textID.setText(table.getValueAt(i, 0).toString());
				textSKU.setText(table.getValueAt(i, 1).toString());
				textDes.setText(table.getValueAt(i, 2).toString());
				textCant.setText(table.getValueAt(i, 3).toString());
				textPrecio.setText(table.getValueAt(i, 4).toString());
				textID_proyecto.setText(table.getValueAt(i, 7).toString());
				textOrden.setText(table.getValueAt(i, 6).toString());

			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
			},
			new String[] {
				"SKU", "Descripci\u00F3n", "Cantidad", "Precio", "Sucursal", "Orden"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(110);
		table.getColumnModel().getColumn(0).setMinWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(20);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(2).setMinWidth(20);
		table.getColumnModel().getColumn(3).setPreferredWidth(110);
		table.getColumnModel().getColumn(4).setPreferredWidth(110);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.setRowHeight(32);
		
		JLabel lblProd = new JLabel("Sucursal:");
		lblProd.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		
		text0Sucursal = new JTextField();
		text0Sucursal.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					buscar();
				}
			}
		});
		text0Sucursal.setColumns(10);
		
		text0Orden = new JTextField();
		text0Orden.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					buscar();
				}
			}
		});
		text0Orden.setColumns(10);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});
		btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));
		
		btnAgregar = new JButton("Agregar Producto");
		btnAgregar.setForeground(Color.BLACK);
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					String sql = "insert into productos (SKU, Descripcion, Cantidad, Precio, id_proyecto, Orden) values (?,?,?,?,?,?)";
					PreparedStatement pst = con.prepareStatement(sql);
					
					//Verifico si el precio tiene o no coma. Si no tiene, se le agrega.
					String precio = textPrecio.getText();
					Boolean coma = false;
					for (int i = 0; i < precio.length(); i++){
					    char c = precio.charAt(i);  
					    if(c==',') {
					    	coma = true;
					    	break;
					    }
					}
					if (coma==false) {
						precio = precio + ",00";
					}
					
					pst.setString(1, "" + textSKU.getText() + "");
					pst.setString(2, "" + textDes.getText() + "");
					pst.setInt(3, Integer.parseInt(textCant.getText()));
					pst.setString(4, "" + precio + "");
					pst.setInt(5, Integer.parseInt(textID_proyecto.getText()));
					pst.setString(6, "" + textOrden.getText() + "");
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Guardado Correctamente");
					con.close();
					refresh();
					limpiar();
					
					}
					catch(Exception e2) {
						JOptionPane.showMessageDialog(null, e2);
					}
			}
		});
		btnAgregar.setFont(new Font("Arial", Font.BOLD, 12));
		
		btnEditar = new JButton("Editar Producto");
		btnEditar.setForeground(Color.BLACK);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textID.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "Primero seleccione un producto en la tabla");
				}
				else {
					Productos frame = new Productos();
					Object[] options = {"Sí", "Cancelar"};
					int response = JOptionPane.showOptionDialog(frame, "¿Confirmar edición?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					    null, options, options[0]);
					if(response == JOptionPane.YES_OPTION)
					{
						try {
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
							
							String sql = "update productos set SKU=?, Descripcion=?, Cantidad=?, Precio=?, "
									+ "id_proyecto=?, Orden=?  where productos.ID = ?";
							PreparedStatement pst = con.prepareStatement(sql);
							
							String precio = textPrecio.getText();
							Boolean coma = false;
							for (int i = 0; i < precio.length(); i++){
							    char c = precio.charAt(i);  
							    if(c==',') {
							    	coma = true;
							    	break;
							    }
							}
							if (coma==false) {
								precio = precio + ",00";
							}
							
							pst.setString(1, "" + textSKU.getText() + "");
							pst.setString(2, "" + textDes.getText() + "");
							pst.setInt(3, Integer.parseInt(textCant.getText()));
							pst.setString(4, "" + precio + "");
							pst.setInt(5, Integer.parseInt(textID_proyecto.getText()));
							pst.setString(6, "" + textOrden.getText() + "");
							pst.setInt(7, Integer.parseInt(textID.getText()));
							pst.executeUpdate();
							
							JOptionPane.showMessageDialog(null, "Registros actualizados correctamente");
							refresh();
							}
							catch(Exception e2) {
								JOptionPane.showMessageDialog(null, "Primero ingrese los valores");
							}
					}
				}
		
			}
		});
		btnEditar.setFont(new Font("Arial", Font.BOLD, 12));
		
		btnEliminar = new JButton("Eliminar Producto");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textID.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "Primero seleccione un producto en la tabla");
				}else {
					Productos frame = new Productos();
					Object[] options = {"Eliminar", "Cancelar"};
					int response = JOptionPane.showOptionDialog(frame, "¿Realmente desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
					
					if(response == JOptionPane.YES_OPTION)
					{
						try {
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
							
							String sql = "DELETE FROM productos WHERE productos.ID = ?";
							PreparedStatement pst = con.prepareStatement(sql);
							pst.setString(1, textID.getText());
							pst.executeUpdate();
							JOptionPane.showMessageDialog(null, "Eliminado de manera exitosa", "!", JOptionPane.INFORMATION_MESSAGE);
							refresh(); limpiar();
						}				
						catch(Exception e2){
							JOptionPane.showMessageDialog(null, e2);
						}
					}
				}
			}
		});
		btnEliminar.setForeground(Color.RED);
		btnEliminar.setFont(new Font("Arial", Font.BOLD, 12));
		
		btnAtrás = new JButton("Volver");
		btnAtrás.setForeground(Color.BLACK);
		btnAtrás.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final MainFrame main1 = new MainFrame();
				main1.setVisible(true);
				main1.setExtendedState(JFrame.MAXIMIZED_BOTH);
				//new MainFrame().setVisible(true);
				dispose();
			}
		});
		btnAtrás.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblSKU = new JLabel("SKU");
		lblSKU.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblDescripcion = new JLabel("Descripci\u00F3n");
		lblDescripcion.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblCantidad = new JLabel("Cantidad");
		lblCantidad.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblPrecio = new JLabel("Precio");
		lblPrecio.setFont(new Font("Arial", Font.BOLD, 12));
		
		textSKU = new JTextField();
		textSKU.setColumns(10);
		
		textDes = new JTextField();
		textDes.setColumns(10);
		
		textCant = new JTextField();
		textCant.setColumns(10);
		
		textPrecio = new JTextField();
		textPrecio.setColumns(10);
		
		lblID = new JLabel("ID");
		lblID.setFont(new Font("Arial", Font.BOLD, 12));
		
		textID = new JTextField();
		textID.setEditable(false);
		textID.setColumns(10);
		
		JButton btnMostrar = new JButton("Mostrar Todo");
		btnMostrar.setForeground(Color.BLACK);
		btnMostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					String sql = "SELECT productos.ID, SKU, Descripcion AS 'Descripción', Cantidad AS 'Cant', Precio AS 'Precio U. sin IVA', sucursales.Nombre AS 'Sucursal', Orden, id_proyecto "
							+ "FROM productos INNER JOIN proyectos on proyectos.ID = productos.id_proyecto INNER JOIN sucursales ON sucursales.ID = proyectos.id_sucursal;";
					PreparedStatement pst = con.prepareStatement(sql);
					ResultSet rs = pst.executeQuery();
					
					table.setModel(DbUtils.resultSetToTableModel(rs));
					resizeColumnWidth(table);
					text0Sucursal.setText("");
					text0Orden.setText("");
					textMateriales.setText("");
					textPrecioTotal.setText("");
					textPrecioIva.setText("");
					calcularMat();calcularCant();calcularTotal();calcularIva();
				}
				catch(Exception e2){
					JOptionPane.showMessageDialog(null, e2);
				
				}
			}
		});
		btnMostrar.setFont(new Font("Arial", Font.BOLD, 12));
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setForeground(Color.BLACK);
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textID.setText("");
				textSKU.setText("");
				textDes.setText("");
				textCant.setText("");
				textPrecio.setText("");
				textID_proyecto.setText("");
				textOrden.setText("");
			}
		});
		btnLimpiar.setFont(new Font("Arial", Font.BOLD, 12));
		
		lblId_proyecto = new JLabel("ID Proyecto");
		lblId_proyecto.setFont(new Font("Arial", Font.BOLD, 12));
		
		textID_proyecto = new JTextField();
		textID_proyecto.setColumns(10);
		
		lblTexto = new JLabel("Materiales");
		lblTexto.setForeground(Color.BLUE);
		lblTexto.setFont(new Font("Arial", Font.BOLD, 18));
		
		textOrden = new JTextField();
		textOrden.setColumns(10);
		
		lblOrden = new JLabel("N\u00B0 de orden");
		lblOrden.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblNewLabel = new JLabel("Orden:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		
		JSeparator separatorBuscar = new JSeparator();
		separatorBuscar.setBackground(Color.LIGHT_GRAY);
		
		lblBuscar = new JLabel("Buscar");
		lblBuscar.setForeground(Color.BLUE);
		lblBuscar.setFont(new Font("Arial", Font.BOLD, 18));
		
		separatorSubBuscar = new JSeparator();
		
		separatorSubMat = new JSeparator();
		
		textMateriales = new JTextField();
		textMateriales.setEditable(false);
		textMateriales.setColumns(10);
		
		textPrecioTotal = new JTextField();
		textPrecioTotal.setEditable(false);
		textPrecioTotal.setColumns(10);
		
		textPrecioIva = new JTextField();
		textPrecioIva.setEditable(false);
		textPrecioIva.setColumns(10);
		
		separatorInv = new JSeparator();
		separatorInv.setForeground(UIManager.getColor("Button.background"));
		separatorInv.setBackground(UIManager.getColor("Button.background"));
		separatorInv.setOrientation(SwingConstants.VERTICAL);
		
		JLabel lblInfo1 = new JLabel("Materiales");
		lblInfo1.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblInfo3 = new JLabel("Total sin IVA");
		lblInfo3.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblInfo4 = new JLabel("Total + IVA");
		lblInfo4.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblinfo2 = new JLabel("Total materiales");
		lblinfo2.setFont(new Font("Arial", Font.BOLD, 12));
		
		textTotalMateriales = new JTextField();
		textTotalMateriales.setEditable(false);
		textTotalMateriales.setColumns(10);
		
		separatorInv2 = new JSeparator();
		separatorInv2.setForeground(UIManager.getColor("Button.background"));
		separatorInv2.setBackground(UIManager.getColor("Button.background"));
		separatorInv2.setOrientation(SwingConstants.VERTICAL);
		
		JLabel lblCliente = new JLabel("Cliente:");
		lblCliente.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		
		text0Cliente = new JTextField();
		text0Cliente.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					buscar();
				}
			}
		});
		text0Cliente.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(135)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBuscar, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(1)
							.addComponent(separatorSubBuscar, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)))
					.addGap(384)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(separatorSubMat, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTexto, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(lblCliente, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(text0Cliente, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(74)
									.addComponent(text0Sucursal, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblProd, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(11)
									.addComponent(lblNewLabel))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblID, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
									.addGap(38)
									.addComponent(textID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblSKU, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblDescripcion, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
									.addGap(7)
									.addComponent(textDes, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblCantidad, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
									.addGap(7)
									.addComponent(textCant, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(84)
									.addComponent(textPrecio, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblId_proyecto, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblOrden, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
									.addGap(7)
									.addComponent(textOrden, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnAgregar, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
									.addGap(9)
									.addComponent(btnEditar, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(150)
									.addComponent(btnLimpiar, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)))
							.addGap(8)
							.addComponent(separatorInv, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(201)
							.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(lblPrecio, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(btnEliminar, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(94)
							.addComponent(textID_proyecto, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(94)
							.addComponent(textSKU, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(84)
							.addComponent(text0Orden, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
						.addComponent(separatorBuscar, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(1)
							.addComponent(lblInfo1, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(textMateriales, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(lblinfo2, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(textTotalMateriales, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(61)
									.addComponent(btnAtrás, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)))
							.addGap(10)
							.addComponent(separatorInv2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
									.addGap(65)
									.addComponent(lblInfo3, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(textPrecioTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(20)
									.addComponent(lblInfo4, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnMostrar, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
									.addGap(39)))
							.addGap(10)
							.addComponent(textPrecioIva, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(16))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(4)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBuscar, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(12)
							.addComponent(separatorSubBuscar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(13)
							.addComponent(separatorSubMat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblTexto, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
					.addGap(7)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(3)
									.addComponent(lblCliente))
								.addComponent(text0Cliente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(11)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(text0Sucursal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblProd))
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(2)
									.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
									.addGap(21)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(3)
											.addComponent(lblID))
										.addComponent(textID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(18)
									.addComponent(lblSKU)
									.addGap(18)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(3)
											.addComponent(lblDescripcion))
										.addComponent(textDes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(15)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(3)
											.addComponent(lblCantidad))
										.addComponent(textCant, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(15)
									.addComponent(textPrecio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(15)
									.addComponent(lblId_proyecto)
									.addGap(21)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(3)
											.addComponent(lblOrden))
										.addComponent(textOrden, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(13)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(btnAgregar)
										.addComponent(btnEditar))
									.addGap(11)
									.addComponent(btnLimpiar))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(7)
									.addComponent(separatorInv, GroupLayout.PREFERRED_SIZE, 335, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnBuscar)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(181)
									.addComponent(lblPrecio))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(314)
									.addComponent(btnEliminar))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(212)
									.addComponent(textID_proyecto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(72)
									.addComponent(textSKU, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(text0Orden, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(27)
									.addComponent(separatorBuscar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
							.addGap(3)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(26)
									.addComponent(lblInfo1))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(22)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(textMateriales, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(4)
											.addComponent(lblinfo2))
										.addComponent(textTotalMateriales, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(14)
									.addComponent(btnAtrás))
								.addComponent(separatorInv2, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(22)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(4)
											.addComponent(lblInfo3))
										.addComponent(textPrecioTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(4)
											.addComponent(lblInfo4)))
									.addGap(14)
									.addComponent(btnMostrar))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(22)
									.addComponent(textPrecioIva, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
