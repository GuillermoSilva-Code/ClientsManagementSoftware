import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import net.proteanit.sql.DbUtils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Contratistas extends JFrame {

	private JPanel contentPane;
	private JTextField textBuscar;
	private JTable table;
	private JTextField textNombre;
	private JTextField textContacto;
	private JTextField textTelefono;
	private JTextField textID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Contratistas frame = new Contratistas();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
	public void refresh() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
			String sql = "SELECT ID, Nombre, Contacto, Teléfono FROM contratistas";
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			resizeColumnWidth(table);
		}
		catch(Exception e2){
			JOptionPane.showMessageDialog(null, e2);
		}	
	}
	public void limpiar() {
		textID.setText("");
		textNombre.setText("");
		textContacto.setText("");
		textTelefono.setText("");
	}

	public Contratistas() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Contratistas.class.getResource("/iconos/glpi.png")));
		setTitle("Contratistas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 902, 426);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnIndice = new JMenu("\u00CDndice");
		mnIndice.setIcon(new ImageIcon(Contratistas.class.getResource("/iconos/menu24.png")));
		mnIndice.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnIndice);
		
		JMenuItem mntmMenu = new JMenuItem("Men\u00FA Principal");
		mntmMenu.setIcon(new ImageIcon(Contratistas.class.getResource("/iconos/casita24.png")));
		mntmMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainFrame().setVisible(true);
				dispose();
			}
		});
		mnIndice.add(mntmMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Materiales");
		mntmNewMenuItem.setIcon(new ImageIcon(Contratistas.class.getResource("/iconos/materiales24.png")));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Productos().setVisible(true);
				dispose();
			}
		});
		mnIndice.add(mntmNewMenuItem);
		
		JMenuItem mntmAgregar = new JMenuItem("Agregar Proyecto");
		mntmAgregar.setIcon(new ImageIcon(Contratistas.class.getResource("/iconos/addclient24.png")));
		mntmAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Agregar().setVisible(true);
				//setVisible(false);
			}
		});
		mnIndice.add(mntmAgregar);
		
		JMenuItem mntmEditar = new JMenuItem("Editar Proyecto");
		mntmEditar.setIcon(new ImageIcon(Contratistas.class.getResource("/iconos/editclient24.png")));
		mntmEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Editar().setVisible(true);
				//setVisible(false);
			}
		});
		mnIndice.add(mntmEditar);
		
		JMenu mnAyuda = new JMenu("Ayuda");
		mnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnAyuda);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblBuscar = new JLabel("Buscar");
		lblBuscar.setFont(new Font("Arial", Font.BOLD, 13));
		
		textBuscar = new JTextField();
		textBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
		textBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
						
						
						String sql = "SELECT ID, Nombre, Contacto, Teléfono FROM contratistas WHERE Nombre LIKE ? or Contacto LIKE ? or Teléfono LIKE ?";
						PreparedStatement pst = con.prepareStatement(sql);
						
						pst.setString(1, "%" + textBuscar.getText() + "%");
						pst.setString(2, "%" + textBuscar.getText() + "%");
						pst.setString(3, "%" + textBuscar.getText() + "%");
						
						ResultSet rs = pst.executeQuery();
						
						table.setModel(DbUtils.resultSetToTableModel(rs));
						resizeColumnWidth(table);
						limpiar();
					}
					catch(Exception e2){
						JOptionPane.showMessageDialog(null, e2);
					}
				}
			}
		});
		textBuscar.setColumns(10);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					
					String sql = "SELECT ID, Nombre, Contacto, Teléfono FROM contratistas WHERE Nombre LIKE ? or Contacto LIKE ? or Teléfono LIKE ?";
					PreparedStatement pst = con.prepareStatement(sql);
					
					pst.setString(1, "%" + textBuscar.getText() + "%");
					pst.setString(2, "%" + textBuscar.getText() + "%");
					pst.setString(3, "%" + textBuscar.getText() + "%");
					
					ResultSet rs = pst.executeQuery();
					
					table.setModel(DbUtils.resultSetToTableModel(rs));
					resizeColumnWidth(table);
					limpiar();
				}
				catch(Exception e2){
					JOptionPane.showMessageDialog(null, e2);
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable();
		table.setFont(new Font("Arial", Font.PLAIN, 13));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				textID.setText(table.getValueAt(i, 0).toString());
				textNombre.setText(table.getValueAt(i, 1).toString());
				textContacto.setText(table.getValueAt(i, 2).toString());
				textTelefono.setText(table.getValueAt(i, 3).toString());
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"ID", "Nombre", "Contacto", "Tel\u00E9fono"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.setRowHeight(32);
		scrollPane.setViewportView(table);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainFrame().setVisible(true);
				dispose();
			}
		});
		btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
		
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					String sql = "insert into contratistas (Nombre, Contacto, Teléfono) values (?,?,?)";
					PreparedStatement pst = con.prepareStatement(sql);
		
					pst.setString(1,"" + textNombre.getText() + "");
					pst.setString(2,"" + textContacto.getText() + "");
					pst.setString(3,"" + textTelefono.getText() + "");
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Guardado Correctamente");
					con.close();
					refresh();
					limpiar();
					
					}
					catch(Exception e2) {
						JOptionPane.showMessageDialog(null, "Primero ingrese los valores");
					}
			}
		});
		btnAgregar.setFont(new Font("Arial", Font.BOLD, 13));
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textID.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "Primero seleccione un contratista en la tabla");
				}else {
					Productos frame = new Productos();
					int response = JOptionPane.showConfirmDialog(frame,"¿Realmente desea eliminar al contratista?", "Confirmar", JOptionPane.YES_NO_OPTION);
					
					if(response == JOptionPane.YES_OPTION)
					{
						try {
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
							
							String sql = "DELETE FROM contratistas WHERE ID = ?";
							PreparedStatement pst = con.prepareStatement(sql);
							pst.setString(1, textID.getText());
							pst.executeUpdate();
							JOptionPane.showMessageDialog(null, "Eliminado de manera exitosa");
							refresh();
							limpiar();
						}				
						catch(Exception e2){
							JOptionPane.showMessageDialog(null, e2);
						}
					}
				}
			}
		});
		btnEliminar.setForeground(Color.RED);
		btnEliminar.setFont(new Font("Arial", Font.BOLD, 13));
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textID.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "Primero seleccione un producto en la tabla");
				}
				else {
					Productos frame = new Productos();
					int response = JOptionPane.showConfirmDialog(frame,"Confirmar edición", "Confirmar", JOptionPane.YES_NO_OPTION);
					
					if(response == JOptionPane.YES_OPTION)
					{
						try {
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
							
							String sql = "update contratistas set Nombre=?, Contacto=?, Teléfono=? where contratistas.ID = ?";
							PreparedStatement pst = con.prepareStatement(sql);
							
							pst.setString(1, "" + textNombre.getText() + "");
							pst.setString(2, "" + textContacto.getText() + "");
							pst.setString(3, "" + textTelefono.getText() + "");
							pst.setInt(4, Integer.parseInt(textID.getText()));
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
		btnEditar.setFont(new Font("Arial", Font.BOLD, 13));
		
		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textID.setText("");
				textNombre.setText("");
				textContacto.setText("");
				textTelefono.setText("");
			}
		});
		btnLimpiar.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblCon = new JLabel("Contacto");
		lblCon.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblTel = new JLabel("Tel\u00E9fono");
		lblTel.setFont(new Font("Arial", Font.BOLD, 13));
		
		textNombre = new JTextField();
		textNombre.setFont(new Font("Arial", Font.PLAIN, 14));
		textNombre.setColumns(10);
		
		textContacto = new JTextField();
		textContacto.setFont(new Font("Arial", Font.PLAIN, 14));
		textContacto.setColumns(10);
		
		textTelefono = new JTextField();
		textTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
		textTelefono.setColumns(10);
		
		JLabel lblID = new JLabel("ID");
		lblID.setFont(new Font("Arial", Font.BOLD, 13));
		
		textID = new JTextField();
		textID.setFont(new Font("Arial", Font.PLAIN, 14));
		textID.setEditable(false);
		textID.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Introduzca el nombre del contratista que desee buscar");
		lblNewLabel.setForeground(new Color(0, 128, 0));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 13));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(lblBuscar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGap(14)
							.addComponent(textBuscar, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(lblCon, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(lblTel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnAgregar, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
							.addGap(15)
							.addComponent(btnEditar, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnEliminar, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
							.addGap(15)
							.addComponent(btnLimpiar, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNombre)
								.addComponent(lblID))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(textID, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
								.addComponent(textTelefono, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(textContacto, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(textNombre, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))))
					.addGap(15)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
					.addGap(5))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addContainerGap(555, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(437)
					.addComponent(btnVolver, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
					.addGap(177))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addGap(9)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(4)
											.addComponent(lblBuscar, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
											.addComponent(btnNewButton)
											.addComponent(textBuscar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(22)
											.addComponent(lblID))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(18)
											.addComponent(textID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addGap(3)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(21)
											.addComponent(lblNombre))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(18)
											.addComponent(textNombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addGap(18)
									.addComponent(lblCon)
									.addGap(22)
									.addComponent(lblTel)
									.addGap(36)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(btnAgregar)
										.addComponent(btnEditar))
									.addGap(12)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(btnEliminar)
										.addComponent(btnLimpiar))
									.addGap(12))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(1)
									.addComponent(scrollPane, 0, 0, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)))
							.addComponent(btnVolver))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(145)
							.addComponent(textContacto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(15)
							.addComponent(textTelefono, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		refresh();
	}
}
