import java.awt.BorderLayout;

import java.sql.*;

import net.proteanit.sql.DbUtils;
import javax.swing.table.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class Agregar extends JFrame {

	private JPanel contentPane;
	private JTextField text3;
	private JTextField text4;
	private JTextField text5;
	private JTextField text6;
	private JTextField textFecha;
	private JComboBox<String> comboBoxClientes;
	private JComboBox<String> comboBoxSucursales;
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Agregar frame = new Agregar();
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
			PreparedStatement pst = con.prepareStatement(sql);
			
			ResultSet rs=pst.executeQuery(sql);
			while(rs.next()) {
				comboBoxClientes.addItem(rs.getString("Nombre"));
			}
			con.close();
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
	}
	public void fillComboBox2() {
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

	/**
	 * Create the frame.
	 */
	public Agregar() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Agregar.class.getResource("/iconos/glpi.png")));
		setTitle("Proyecto Nuevo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 378);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnIndice = new JMenu("\u00CDndice");
		mnIndice.setIcon(new ImageIcon(Agregar.class.getResource("/iconos/menu24.png")));
		mnIndice.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnIndice);
		
		JMenuItem mntmEditar = new JMenuItem("Editar Proyecto");
		mntmEditar.setIcon(new ImageIcon(Agregar.class.getResource("/iconos/editclient24.png")));
		mntmEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Editar().setVisible(true);
				dispose();
			}
		});
		mnIndice.add(mntmEditar);
		
		JMenu mnAyuda = new JMenu("Ayuda");
		mnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnAyuda);
		contentPane = new JPanel();
		contentPane.setForeground(Color.RED);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		JButton btnVolver = new JButton("Cerrar");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new MainFrame().setVisible(true);
				dispose();
			}
		});
		btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblSucursal = new JLabel("Sucursal");
		lblSucursal.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblInstalacion = new JLabel("Instalaci\u00F3n");
		lblInstalacion.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblTec = new JLabel("Tecnolog\u00EDas");
		lblTec.setFont(new Font("Arial", Font.BOLD, 13));
		
		text3 = new JTextField();
		text3.setFont(new Font("Arial", Font.PLAIN, 14));
		text3.setColumns(10);
		
		text4 = new JTextField();
		text4.setFont(new Font("Arial", Font.PLAIN, 14));
		text4.setColumns(10);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillComboBox();
				try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
				
				String sql = "insert into proyectos (id_cliente, id_sucursal, Contacto, Fecha_de_venta, Instalacion, Tecnologias, Instalador) values "
						+ "((Select clientes.ID from clientes where clientes.Nombre LIKE ?),"
						+ "(Select sucursales.ID from sucursales where sucursales.Nombre LIKE ? AND sucursales.id_cliente = (Select clientes.ID from clientes where clientes.Nombre LIKE ?)),?,?,?,?,?)";
				PreparedStatement pst = con.prepareStatement(sql);
				
				String Cliente = (String)comboBoxClientes.getSelectedItem();
				String Sucursal = (String)comboBoxSucursales.getSelectedItem();
				pst.setString(1,"%" + Cliente + "%");
				pst.setString(2,"%" + Sucursal + "%");
				pst.setString(3,"%" + Cliente + "%");
				pst.setString(4,"" + text3.getText() + "");
				pst.setString(5,"" + textFecha.getText() + "");
				pst.setString(6,"" + text4.getText() + "");
				pst.setString(7,"" + text5.getText() + "");
				pst.setString(8,"" + text6.getText() + "");
				
				pst.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Guardado Correctamente");
				con.close();
				text3.setText("");
				text4.setText("");
				text5.setText("");
				text6.setText("");
				textFecha.setText("");
				
				}
				catch(Exception e2) {
					JOptionPane.showMessageDialog(null, "Primero ingrese los valores");
					
				}
				//new Agregar1().setVisible(true);
				//dispose();
			
				
			}
		});
		btnAceptar.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblTexto = new JLabel("A\u00F1ada los datos del proyecto y luego pulse \"Aceptar\"");
		lblTexto.setForeground(new Color(0, 128, 0));
		lblTexto.setFont(new Font("Arial", Font.ITALIC, 14));
		
		JLabel lblInstalador = new JLabel("Instalador/Venta");
		lblInstalador.setFont(new Font("Arial", Font.BOLD, 13));
		
		text5 = new JTextField();
		text5.setFont(new Font("Arial", Font.PLAIN, 14));
		text5.setColumns(10);
		
		text6 = new JTextField();
		text6.setFont(new Font("Arial", Font.PLAIN, 14));
		text6.setToolTipText("Indicar nombre de la instaladora/contratista o si es una venta");
		text6.setColumns(10);
		
		JLabel lblContacto = new JLabel("Contacto");
		lblContacto.setFont(new Font("Arial", Font.BOLD, 13));
		
		textFecha = new JTextField();
		textFecha.setFont(new Font("Arial", Font.PLAIN, 14));
		textFecha.setColumns(10);
		
		JLabel lblFecha = new JLabel("Fecha de venta");
		lblFecha.setFont(new Font("Arial", Font.BOLD, 13));
		
		comboBoxClientes = new JComboBox<String>();
		comboBoxClientes.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBoxClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillComboBox2();
			}
		});
		
		comboBoxSucursales = new JComboBox<String>();
		comboBoxSucursales.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JButton btnCli = new JButton("");
		btnCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NuevoCliente().setVisible(true);
				dispose();
			}
		});
		btnCli.setIcon(new ImageIcon(Agregar.class.getResource("/iconos/a\u00F1adir24+.png")));
		
		JButton btnSuc = new JButton("");
		btnSuc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NuevaSucursal().setVisible(true);
				dispose();
			}
		});
		btnSuc.setIcon(new ImageIcon(Agregar.class.getResource("/iconos/a\u00F1adir24+.png")));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(17)
					.addComponent(lblCliente, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addGap(57)
					.addComponent(comboBoxClientes, 0, 169, Short.MAX_VALUE)
					.addGap(10)
					.addComponent(btnCli, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(69)
					.addComponent(lblContacto, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(text3, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
					.addGap(26))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addComponent(lblSucursal, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
					.addGap(48)
					.addComponent(comboBoxSucursales, 0, 169, Short.MAX_VALUE)
					.addGap(10)
					.addComponent(btnSuc, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(32)
					.addComponent(lblFecha)
					.addGap(5)
					.addComponent(textFecha, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
					.addGap(26))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addComponent(lblInstalacion, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(39)
					.addComponent(text4, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
					.addGap(26))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addComponent(lblTec, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(25)
					.addComponent(text5, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
					.addGap(26))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addComponent(lblInstalador, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
					.addComponent(text6, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
					.addGap(26))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(191)
					.addComponent(btnAceptar, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
					.addGap(36)
					.addComponent(btnVolver, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
					.addGap(194))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTexto)
					.addContainerGap(329, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTexto)
					.addGap(19)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(5)
							.addComponent(lblCliente))
						.addComponent(comboBoxClientes, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCli, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(5)
							.addComponent(lblContacto))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(1)
							.addComponent(text3, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(15)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(5)
									.addComponent(lblSucursal))
								.addComponent(comboBoxSucursales, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSuc, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(1)
									.addComponent(textFecha, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(lblFecha)))
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(4)
							.addComponent(lblInstalacion))
						.addComponent(text4, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(4)
							.addComponent(lblTec))
						.addComponent(text5, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(4)
							.addComponent(lblInstalador))
						.addComponent(text6, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAceptar, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
		);
		contentPane.setLayout(gl_contentPane);
		fillComboBox();
	}
}

//SELECT * FROM `clientes` WHERE Nombre LIKE '%cámara%' or ID LIKE '%cámara%'or Productos LIKE '%cámara%' or Descripcion LIKE '%cámara%';