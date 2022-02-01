import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import net.proteanit.sql.DbUtils;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSeparator;
import java.awt.SystemColor;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class NuevaSucursal extends JFrame {

	private JPanel contentPane;
	private JTextField textSucursal;
	private JTextField textContacto;
	private JTextField textID;
	private JTable table;
	private JComboBox<String> comboBoxClientes;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NuevaSucursal frame = new NuevaSucursal();
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
			String sql = "SELECT sucursales.ID, sucursales.Nombre, sucursales.Contacto, clientes.Nombre AS 'Cliente' FROM sucursales, clientes "
					+ "WHERE sucursales.id_cliente = clientes.ID order by clientes.Nombre ASC";
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			resizeColumnWidth(table);
			comboBoxClientes.setSelectedIndex(-1);
		}
		catch(Exception e2){
			JOptionPane.showMessageDialog(null, e2);
		}	
	}
	public void limpiar() {
		textID.setText("");
		textSucursal.setText("");
		textContacto.setText("");
		comboBoxClientes.setSelectedIndex(-1);
	}
	
	public NuevaSucursal() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(NuevoCliente.class.getResource("/iconos/glpi.png")));
		setTitle("Sucursales");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 725, 361);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(Color.BLUE);
		setJMenuBar(menuBar);
		
		JMenu mnIndice = new JMenu("\u00CDndice");
		mnIndice.setForeground(new Color(0, 0, 0));
		mnIndice.setIcon(new ImageIcon(NuevoCliente.class.getResource("/iconos/menu24.png")));
		mnIndice.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnIndice);
		
		JMenuItem mntmSucursales = new JMenuItem("Clientes");
		mntmSucursales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NuevoCliente().setVisible(true);
				dispose();
			}
		});
		mntmSucursales.setIcon(new ImageIcon(NuevoCliente.class.getResource("/iconos/addclient16.png")));
		mnIndice.add(mntmSucursales);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		menuBar.setBackground(SystemColor.activeCaption);
	    menuBar.setOpaque(true);
	    
		
		JLabel lblSuc = new JLabel("Sucursal");
		lblSuc.setFont(new Font("Arial", Font.BOLD, 13));
		
		textSucursal = new JTextField();
		textSucursal.setFont(new Font("Arial", Font.PLAIN, 13));
		textSucursal.setColumns(10);
		
		JLabel lblCon = new JLabel("Contacto");
		lblCon.setFont(new Font("Arial", Font.BOLD, 13));
		
		textContacto = new JTextField();
		textContacto.setFont(new Font("Arial", Font.PLAIN, 13));
		textContacto.setColumns(10);
		
		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblID = new JLabel("ID");
		lblID.setFont(new Font("Arial", Font.BOLD, 13));
		
		textID = new JTextField();
		textID.setFont(new Font("Arial", Font.PLAIN, 13));
		textID.setEditable(false);
		textID.setColumns(10);
		
		JButton btnNewButton = new JButton("Agregar");
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					String sql = "insert into sucursales (Nombre, Contacto, id_cliente) values (?,?,(SELECT clientes.ID FROM clientes WHERE clientes.Nombre LIKE ?))";
					PreparedStatement pst = con.prepareStatement(sql);
					
					String nombreCliente = (String)comboBoxClientes.getSelectedItem();
					pst.setString(1,"" + textSucursal.getText() + "");
					pst.setString(2,"" + textContacto.getText() + "");
					pst.setString(3,"" + nombreCliente + "");
					
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
		
		JButton btnNewButton_1 = new JButton("Editar");
		btnNewButton_1.setFont(new Font("Arial", Font.BOLD, 13));
		btnNewButton_1.addActionListener(new ActionListener() {
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
							
							String sql = "update sucursales set Nombre=?, Contacto=?, id_cliente=(SELECT clientes.ID FROM "
									+ "clientes WHERE clientes.Nombre LIKE ?) where sucursales.ID = ?;";
							PreparedStatement pst = con.prepareStatement(sql);
							
							String nombreCliente = (String)comboBoxClientes.getSelectedItem();
							pst.setString(1, "" + textSucursal.getText() + "");
							pst.setString(2, "" + textContacto.getText() + "");
							pst.setString(3, "" + nombreCliente + "");
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
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Agregar().setVisible(true);
				dispose();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable();
		table.setFont(new Font("Arial", Font.PLAIN, 13));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				textID.setText(table.getValueAt(i, 0).toString());
				textSucursal.setText(table.getValueAt(i, 1).toString());
				textContacto.setText(table.getValueAt(i, 2).toString());
				String cliente = table.getValueAt(i, 3).toString();
				//cliente = nombre cliente en tabla
				for(int j = 0; j<comboBoxClientes.getItemCount();j++) {
					String clienteComp = (String)comboBoxClientes.getItemAt(j);
					//recorre los 4
					if(cliente.equals(clienteComp) == true) {
						comboBoxClientes.setSelectedIndex(j);
					}
				}
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
			},
			new String[] {
				"ID", "Sucursal", "Contacto", "Cliente"
			}
		));
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.setRowHeight(32);
		scrollPane.setViewportView(table);
		
		JButton btnNewButton_2 = new JButton("Limpiar");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiar();
			}
		});
		btnNewButton_2.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblNewLabel = new JLabel("Sucursales");
		lblNewLabel.setForeground(new Color(0, 153, 0));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		
		JSeparator separator = new JSeparator();
		
		comboBoxClientes = new JComboBox<String>();
		comboBoxClientes.setFont(new Font("Arial", Font.PLAIN, 13));
		refresh();
		fillComboBox();
		comboBoxClientes.setSelectedIndex(-1);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(48)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblID, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
							.addGap(45)
							.addComponent(textID, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblSuc, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(textSucursal, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblCon, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(textContacto, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblCliente, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGap(20)
							.addComponent(comboBoxClientes, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addGap(17)
							.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addGap(17)
							.addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
					.addGap(17))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(12)
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(4)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(3)
									.addComponent(lblID))
								.addComponent(textID, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
							.addGap(13)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(3)
									.addComponent(lblSuc))
								.addComponent(textSucursal, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
							.addGap(13)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(3)
									.addComponent(lblCon))
								.addComponent(textContacto, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
							.addGap(13)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(3)
									.addComponent(lblCliente))
								.addComponent(comboBoxClientes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(48)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNewButton)
								.addComponent(btnNewButton_1))
							.addGap(15)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNewButton_2)
								.addComponent(btnVolver)))
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE)))
		);
		contentPane.setLayout(gl_contentPane);
	}
}