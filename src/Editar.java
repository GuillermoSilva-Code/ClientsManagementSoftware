import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Editar extends JFrame {

	private JPanel contentPane;
	private JTextField text1;
	private JTextField text2;
	private JTextField text3;
	private JTextField text4;
	private JTextField text0;
	private JTextField text5;
	private JTextField text6;
	private JTextField textFecha;
	private JComboBox<String> comboBoxClientes;
	private JComboBox<String> comboBoxSucursales;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editar frame = new Editar();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void fillComboBox() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
			
			String sql = "select * from clientes";
			//String sql2 = "Select * from sucursales";
			PreparedStatement pst = con.prepareStatement(sql);
			//PreparedStatement pst2 = con.prepareStatement(sql2);
			
			ResultSet rs=pst.executeQuery(sql);
			while(rs.next()) {
				comboBoxClientes.addItem(rs.getString("Nombre"));
			}
			
			//ResultSet rs2=pst2.executeQuery(sql2);
			//while(rs2.next()) {
			//	comboBoxSucursales.addItem(rs2.getString("Nombre"));
			//}
			
			con.close();
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
	}
	public void fillComboBox2() {
		if(comboBoxClientes.getSelectedIndex()!= -1) {
			String clienteX = (String)comboBoxClientes.getSelectedItem();
			comboBoxSucursales.removeAllItems();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
				
				String sql = "SELECT * from sucursales WHERE id_cliente = (SELECT clientes.ID from clientes WHERE clientes.Nombre LIKE ? )";
				PreparedStatement pst = con.prepareStatement(sql);
				
				pst.setString(1, "%" + clienteX.toString() + "%");
				
				ResultSet rs=pst.executeQuery();
				while(rs.next()) {
					comboBoxSucursales.addItem(rs.getString("Nombre"));
				}
				
				con.close();
				
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
		}

	}


	public Editar() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Editar.class.getResource("/iconos/glpi.png")));
		setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		setTitle("Editar Proyecto");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 745, 379);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnIndice = new JMenu("\u00CDndice");
		mnIndice.setIcon(new ImageIcon(Editar.class.getResource("/iconos/menu24.png")));
		mnIndice.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnIndice);
		
		JMenuItem mntmAgregar = new JMenuItem("Agregar Proyecto");
		mntmAgregar.setIcon(new ImageIcon(Editar.class.getResource("/iconos/addclient24.png")));
		mntmAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Agregar().setVisible(true);
				dispose();
			}
		});
		mnIndice.add(mntmAgregar);
		
		JMenu mnAyuda = new JMenu("Ayuda");
		mnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnAyuda);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnOk = new JButton("");
		btnOk.setIcon(new ImageIcon(Editar.class.getResource("/iconos/lupa32.png")));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					String sql = "select * from proyectos where proyectos.ID = ?";
					PreparedStatement pst = con.prepareStatement(sql);
					pst.setInt(1, Integer.parseInt(text0.getText()));
					ResultSet rs=pst.executeQuery();
					
					while(rs.next()) {
						text0.setText(rs.getString("ID"));
						text3.setText(rs.getString("Contacto"));
						textFecha.setText(rs.getString("Fecha_de_venta"));
						text4.setText(rs.getString("Instalacion"));
						text5.setText(rs.getString("Tecnologias"));
						text6.setText(rs.getString("Instalador"));
					}
					String sql1 = "SELECT * FROM clientes WHERE ID = (SELECT proyectos.id_cliente FROM proyectos WHERE proyectos.ID = ?);";
					PreparedStatement pst1 = con.prepareStatement(sql1);
					pst1.setInt(1, Integer.parseInt(text0.getText()));
					ResultSet rs1=pst1.executeQuery();
					
					while(rs1.next()) {
						text1.setText(rs1.getString("Nombre"));
					}
					String sql2 = "SELECT * FROM sucursales WHERE ID = (SELECT proyectos.id_sucursal FROM proyectos WHERE proyectos.ID = ?);";
					PreparedStatement pst2 = con.prepareStatement(sql2);
					pst2.setInt(1, Integer.parseInt(text0.getText()));
					ResultSet rs2=pst2.executeQuery();
					
					while(rs2.next()) {
						text2.setText(rs2.getString("Nombre"));
					}
					pst.close();
					}
				
					catch(Exception e2) {
						JOptionPane.showMessageDialog(null, "Primero ingrese los valores");
					}
				comboBoxSucursales.removeAllItems();
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					String sql = "SELECT * from sucursales WHERE id_cliente = (SELECT clientes.ID from clientes WHERE clientes.Nombre LIKE ? )";
					PreparedStatement pst = con.prepareStatement(sql);
					
					pst.setString(1, "%" + text1.getText() + "%");
					
					ResultSet rs=pst.executeQuery();
					while(rs.next()) {
						comboBoxSucursales.addItem(rs.getString("Nombre"));
					}
					String sql3 = "SELECT Nombre FROM sucursales WHERE ID = (SELECT proyectos.id_sucursal FROM proyectos WHERE proyectos.ID = ?);";
					PreparedStatement pst3 = con.prepareStatement(sql3);
					pst3.setInt(1, Integer.parseInt(text0.getText()));
					ResultSet rs3=pst3.executeQuery();
					
					while(rs3.next()) {
						text2.setText(rs3.getString("Nombre"));
						
					}
					con.close();
					
					}
					catch(Exception e2) {
						JOptionPane.showMessageDialog(null, e2);
					}
				comboBoxClientes.setSelectedIndex(-1);
				comboBoxSucursales.setSelectedIndex(-1);
			}
		});
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblNota = new JLabel("Importante: Introduzca el ID del Proyecto que desea editar y presione <Enter>");
		lblNota.setForeground(Color.RED);
		lblNota.setFont(new Font("Tahoma", Font.ITALIC, 13));
		
		JLabel lblID = new JLabel("ID");
		lblID.setFont(new Font("Arial", Font.BOLD, 13));
		
		text0 = new JTextField();
		text0.setFont(new Font("Arial", Font.PLAIN, 14));
		text0.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
						
						String sql = "select * from proyectos where proyectos.ID = ?";
						PreparedStatement pst = con.prepareStatement(sql);
						pst.setInt(1, Integer.parseInt(text0.getText()));
						ResultSet rs=pst.executeQuery();
						
						while(rs.next()) {
							text0.setText(rs.getString("ID"));
							text3.setText(rs.getString("Contacto"));
							textFecha.setText(rs.getString("Fecha_de_venta"));
							text4.setText(rs.getString("Instalacion"));
							text5.setText(rs.getString("Tecnologias"));
							text6.setText(rs.getString("Instalador"));
						}
						String sql1 = "SELECT * FROM clientes WHERE ID = (SELECT proyectos.id_cliente FROM proyectos WHERE proyectos.ID = ?);";
						PreparedStatement pst1 = con.prepareStatement(sql1);
						pst1.setInt(1, Integer.parseInt(text0.getText()));
						ResultSet rs1=pst1.executeQuery();
						
						while(rs1.next()) {
							text1.setText(rs1.getString("Nombre"));
						}
						String sql2 = "SELECT * FROM sucursales WHERE ID = (SELECT proyectos.id_sucursal FROM proyectos WHERE proyectos.ID = ?);";
						PreparedStatement pst2 = con.prepareStatement(sql2);
						pst2.setInt(1, Integer.parseInt(text0.getText()));
						ResultSet rs2=pst2.executeQuery();
						
						while(rs2.next()) {
							text2.setText(rs2.getString("Nombre"));
						}
						pst.close();
						}
					
						catch(Exception e2) {
							JOptionPane.showMessageDialog(null, "Primero ingrese los valores");
						}
					comboBoxSucursales.removeAllItems();
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
						
						String sql = "SELECT * from sucursales WHERE id_cliente = (SELECT clientes.ID from clientes WHERE clientes.Nombre LIKE ? )";
						PreparedStatement pst = con.prepareStatement(sql);
						
						pst.setString(1, "%" + text1.getText() + "%");
						
						ResultSet rs=pst.executeQuery();
						while(rs.next()) {
							comboBoxSucursales.addItem(rs.getString("Nombre"));
						}
						String sql3 = "SELECT Nombre FROM sucursales WHERE ID = (SELECT proyectos.id_sucursal FROM proyectos WHERE proyectos.ID = ?);";
						PreparedStatement pst3 = con.prepareStatement(sql3);
						pst3.setInt(1, Integer.parseInt(text0.getText()));
						ResultSet rs3=pst3.executeQuery();
						
						while(rs3.next()) {
							text2.setText(rs3.getString("Nombre"));
							
						}
						con.close();
						
						}
						catch(Exception e2) {
							JOptionPane.showMessageDialog(null, e2);
						}
					comboBoxClientes.setSelectedIndex(-1);
					comboBoxSucursales.setSelectedIndex(-1);

				}
			}
		});
		text0.setColumns(10);
		
		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setFont(new Font("Arial", Font.BOLD, 13));
		
		text1 = new JTextField();
		text1.setEditable(false);
		text1.setFont(new Font("Arial", Font.PLAIN, 14));
		text1.setColumns(10);
		
		JLabel lblSucursal = new JLabel("Sucursal");
		lblSucursal.setFont(new Font("Arial", Font.BOLD, 13));
		
		text2 = new JTextField();
		text2.setEditable(false);
		text2.setFont(new Font("Arial", Font.PLAIN, 14));
		text2.setColumns(10);
		
		JLabel lblInstalacion = new JLabel("Instalaci\u00F3n");
		lblInstalacion.setFont(new Font("Arial", Font.BOLD, 13));
		
		JButton btnVolver = new JButton("Cerrar");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new MainFrame().setVisible(true);
				dispose();
			}
		});
		btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
		
		text3 = new JTextField();
		text3.setFont(new Font("Arial", Font.PLAIN, 14));
		text3.setColumns(10);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Editar frame = new Editar();
				int response = JOptionPane.showConfirmDialog(frame,"Confirmar", "Editar", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION)
				{
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
						String sql = "update proyectos set id_cliente=(SELECT clientes.ID FROM clientes WHERE clientes.Nombre LIKE ?), "
								+ "id_sucursal=(SELECT sucursales.ID FROM sucursales WHERE sucursales.Nombre LIKE ? AND sucursales.id_cliente = "
								+ "(Select clientes.ID from clientes where clientes.Nombre LIKE ?)), Contacto=?, Fecha_de_venta=?, "
								+ "Instalacion=?, Tecnologias=?, Instalador=? where ID=?";
						PreparedStatement pst = con.prepareStatement(sql);
						
						pst.setString(1, text1.getText());
						pst.setString(2, text2.getText());
						pst.setString(3, text1.getText());
						pst.setString(4, text3.getText());
						pst.setString(5, textFecha.getText());
						pst.setString(6, text4.getText());
						pst.setString(7, text5.getText());
						pst.setString(8, text6.getText());
						pst.setInt(9, Integer.parseInt(text0.getText()));
						
						pst.executeUpdate();
						
						JOptionPane.showMessageDialog(null, "Registros actualizados correctamente");
						con.close();
						}
						catch(Exception e2) {
							JOptionPane.showMessageDialog(null, e2);
						}
					text0.setText("");
					text1.setText("");
					text2.setText("");
					text3.setText("");
					text4.setText("");
					text5.setText("");
					text6.setText("");
					textFecha.setText("");
				}

			}
		});
		btnAceptar.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblTecnologia = new JLabel("Tecnolog\u00EDas");
		lblTecnologia.setFont(new Font("Arial", Font.BOLD, 13));
		
		text4 = new JTextField();
		text4.setFont(new Font("Arial", Font.PLAIN, 14));
		text4.setColumns(10);
		
		JLabel lblInstalador = new JLabel("Instalador/Venta");
		lblInstalador.setFont(new Font("Arial", Font.BOLD, 13));
		
		text5 = new JTextField();
		text5.setFont(new Font("Arial", Font.PLAIN, 14));
		text5.setColumns(10);
		
		text6 = new JTextField();
		text6.setFont(new Font("Arial", Font.PLAIN, 14));
		text6.setColumns(10);
		
		JLabel lblContacto = new JLabel("Contacto");
		lblContacto.setFont(new Font("Arial", Font.BOLD, 13));
		
		textFecha = new JTextField();
		textFecha.setFont(new Font("Arial", Font.PLAIN, 14));
		textFecha.setColumns(10);
		
		JLabel lblFecha = new JLabel("Fecha de venta");
		lblFecha.setFont(new Font("Arial", Font.BOLD, 13));
		
		comboBoxClientes = new JComboBox<String>();
		comboBoxClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillComboBox2();
				if(comboBoxClientes.getSelectedIndex()!= -1) {
					text1.setText(comboBoxClientes.getSelectedItem().toString());
				}
				
			}
		});
		
		comboBoxSucursales = new JComboBox<String>();
		comboBoxSucursales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBoxSucursales.getSelectedIndex()!= -1) {
					text2.setText(comboBoxSucursales.getSelectedItem().toString());
				}
			}
		});
		fillComboBox();
		comboBoxClientes.setSelectedIndex(-1);
		comboBoxSucursales.setSelectedIndex(-1);
		text1.setText("");
		text2.setText("");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addComponent(lblNota, GroupLayout.PREFERRED_SIZE, 463, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(221)
					.addComponent(btnAceptar, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
					.addGap(47)
					.addComponent(btnVolver, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
					.addGap(224))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(lblInstalador))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblTecnologia, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(lblInstalacion, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(text4, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
						.addComponent(text5, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
						.addComponent(text6, GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE))
					.addGap(5))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblSucursal, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCliente, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(lblID, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)))
					.addGap(50)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(text0, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(text1, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
									.addGap(9)
									.addComponent(comboBoxClientes, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
									.addGap(74)
									.addComponent(lblContacto, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(text3, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(text2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(comboBoxSucursales, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
									.addGap(32)
									.addComponent(lblFecha)
									.addGap(10)
									.addComponent(textFecha, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)))
							.addGap(5))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(6)
					.addComponent(lblNota, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(4)
							.addComponent(lblID))
						.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(text0, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addGap(17)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(text1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblCliente))
						.addComponent(comboBoxClientes, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblContacto))
						.addComponent(text3, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(lblFecha))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(17)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(text2, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(3)
									.addComponent(lblSucursal))
								.addComponent(comboBoxSucursales, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFecha, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(lblInstalacion))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(17)
							.addComponent(text4, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(lblTecnologia))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(17)
							.addComponent(text5, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addGap(17)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(lblInstalador))
						.addComponent(text6, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAceptar, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
		);
		contentPane.setLayout(gl_contentPane);
		comboBoxSucursales.removeAllItems();
	}
}
