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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class NuevoCliente extends JFrame {

	private JPanel contentPane;
	private JTextField textCliente;
	private JTextField textContacto;
	private JTextField textEmail;
	private JTextField textID;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NuevoCliente frame = new NuevoCliente();
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
			String sql = "SELECT ID, Nombre, Contacto, Email FROM clientes";
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
		textCliente.setText("");
		textContacto.setText("");
		textEmail.setText("");
	}
	
	public NuevoCliente() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(NuevoCliente.class.getResource("/iconos/glpi.png")));
		setTitle("Clientes");
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
		
		JMenuItem mntmSucursales = new JMenuItem("Sucursales");
		mntmSucursales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NuevaSucursal().setVisible(true);
				dispose();
			}
		});
		mntmSucursales.setIcon(new ImageIcon(NuevoCliente.class.getResource("/iconos/casita16.png")));
		mnIndice.add(mntmSucursales);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		menuBar.setBackground(SystemColor.activeCaption);
	    menuBar.setOpaque(true);
	    
		
		JLabel lblCli = new JLabel("Cliente");
		lblCli.setBounds(10, 83, 46, 16);
		lblCli.setFont(new Font("Arial", Font.BOLD, 13));
		
		textCliente = new JTextField();
		textCliente.setBounds(75, 80, 163, 22);
		textCliente.setFont(new Font("Arial", Font.PLAIN, 13));
		textCliente.setColumns(10);
		
		JLabel lblCon = new JLabel("Contacto");
		lblCon.setBounds(10, 118, 60, 16);
		lblCon.setFont(new Font("Arial", Font.BOLD, 13));
		
		textContacto = new JTextField();
		textContacto.setBounds(75, 115, 163, 22);
		textContacto.setFont(new Font("Arial", Font.PLAIN, 13));
		textContacto.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(10, 153, 46, 16);
		lblEmail.setFont(new Font("Arial", Font.BOLD, 13));
		
		textEmail = new JTextField();
		textEmail.setBounds(75, 148, 163, 22);
		textEmail.setFont(new Font("Arial", Font.PLAIN, 13));
		textEmail.setColumns(10);
		
		JLabel lblID = new JLabel("ID");
		lblID.setBounds(10, 48, 21, 16);
		lblID.setFont(new Font("Arial", Font.BOLD, 13));
		
		textID = new JTextField();
		textID.setBounds(75, 45, 79, 22);
		textID.setFont(new Font("Arial", Font.PLAIN, 13));
		textID.setEditable(false);
		textID.setColumns(10);
		
		JButton btnNewButton = new JButton("Agregar");
		btnNewButton.setBounds(10, 192, 105, 25);
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					String sql = "insert into clientes (Nombre, Contacto, Email) values (?,?,?)";
					PreparedStatement pst = con.prepareStatement(sql);
		
					pst.setString(1,"" + textCliente.getText() + "");
					pst.setString(2,"" + textContacto.getText() + "");
					pst.setString(3,"" + textEmail.getText() + "");
					
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
		btnNewButton_1.setBounds(133, 192, 105, 25);
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
							
							String sql = "update clientes set Nombre=?, Contacto=?, Email=? where clientes.ID = ?";
							PreparedStatement pst = con.prepareStatement(sql);
							
							pst.setString(1, "" + textCliente.getText() + "");
							pst.setString(2, "" + textContacto.getText() + "");
							pst.setString(3, "" + textEmail.getText() + "");
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
		btnVolver.setBounds(132, 228, 105, 25);
		btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Agregar().setVisible(true);
				dispose();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(259, 41, 428, 219);
		
		table = new JTable();
		table.setFont(new Font("Arial", Font.PLAIN, 13));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				textID.setText(table.getValueAt(i, 0).toString());
				textCliente.setText(table.getValueAt(i, 1).toString());
				textContacto.setText(table.getValueAt(i, 2).toString());
				textEmail.setText(table.getValueAt(i, 3).toString());
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
				"ID", "Cliente", "Contacto", "Email"
			}
		));
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.setRowHeight(32);
		scrollPane.setViewportView(table);
		
		JButton btnNewButton_2 = new JButton("Limpiar");
		btnNewButton_2.setBounds(10, 228, 105, 25);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiar();
			}
		});
		btnNewButton_2.setFont(new Font("Arial", Font.BOLD, 13));
		
		JLabel lblNewLabel = new JLabel("CLIENTES");
		lblNewLabel.setBounds(53, 11, 80, 14);
		lblNewLabel.setForeground(new Color(0, 153, 0));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		
		JSeparator separator = new JSeparator();
		separator.setBounds(53, 23, 69, 2);
		contentPane.setLayout(null);
		contentPane.add(lblNewLabel);
		contentPane.add(separator);
		contentPane.add(lblCon);
		contentPane.add(lblEmail);
		contentPane.add(textEmail);
		contentPane.add(btnNewButton);
		contentPane.add(btnNewButton_1);
		contentPane.add(btnNewButton_2);
		contentPane.add(btnVolver);
		contentPane.add(textContacto);
		contentPane.add(lblCli);
		contentPane.add(lblID);
		contentPane.add(textID);
		contentPane.add(textCliente);
		contentPane.add(scrollPane);
		refresh();
	}
}
