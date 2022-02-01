import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.*;

import java.sql.*;
import java.sql.DriverManager;
import net.proteanit.sql.DbUtils;

import javax.swing.table.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JEditorPane;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textBuscar;
	private JLabel daxalogo;
	private JButton btnAgregarCliente;
	private JButton btnEditarCliente;
	private JLabel add;
	private JLabel add1;
	private JLabel edit;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnBuscar;
	private JButton btnMostar;
	private JButton btnVerProductos;
	private JMenuBar menuBar;
	private JMenu mnIndice;
	private JMenu mnAyuda;
	private JMenuItem mntmAgregar;
	private JMenuItem mntmEditar;
	private JMenuItem mntmMateriales;
	private JButton btnVerContratistas;
	private JMenuItem mntmNewMenuItem;
	private JSeparator separator;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Start ventana maximizada
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public void refresh() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
			
			String sql = "select ID, Cliente, Sucursal, Contacto, Fecha_de_venta AS 'Fecha de venta ', Instalacion AS 'Instalación', Tecnologias AS 'Tecnologías Usadas', Instalador AS 'Instalador/Venta' from proyectos";
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			
			table.setModel(DbUtils.resultSetToTableModel(rs));
			resizeColumnWidth(table);
		}
		catch(Exception e2){
			JOptionPane.showMessageDialog(null, e2);
		}	
	}
	public void buscar() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
			
			String sql = "SELECT proyectos.ID, clientes.Nombre AS 'Cliente', sucursales.Nombre AS 'Sucursal', proyectos.Contacto, "
					+ "Fecha_de_venta AS 'Fecha de venta ', Instalacion AS 'Instalación', Tecnologias AS 'Tecnologías Usadas', "
					+ "Instalador AS 'Instalador/Venta' FROM proyectos INNER JOIN clientes ON proyectos.id_cliente = clientes.ID INNER JOIN "
					+ "sucursales ON proyectos.id_sucursal = sucursales.ID WHERE clientes.Nombre LIKE ? OR sucursales.Nombre "
					+ "LIKE ? OR proyectos.ID LIKE ? OR Instalacion LIKE ? OR Instalador LIKE ? "
					+ "OR Tecnologias LIKE ? OR Fecha_de_venta LIKE ? ORDER BY ID ASC";
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setString(1, "%" + textBuscar.getText() + "%");
			pst.setString(2, "%" + textBuscar.getText() + "%");
			pst.setString(3, "%" + textBuscar.getText() + "%");
			pst.setString(4, "%" + textBuscar.getText() + "%");
			pst.setString(5, "%" + textBuscar.getText() + "%");
			pst.setString(6, "%" + textBuscar.getText() + "%");
			pst.setString(7, "%" + textBuscar.getText() + "%");
			ResultSet rs = pst.executeQuery();
			
			table.setModel(DbUtils.resultSetToTableModel(rs));
			resizeColumnWidth(table);
		}				
		catch(Exception e2){
			JOptionPane.showMessageDialog(null, e2);
		}
	}
	
	public void resizeColumnWidth(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 10; // width minima
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        if(width > 340)
	            width=340;
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}
	
	
	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/iconos/glpi.png")));
		setForeground(Color.WHITE);
		setBackground(Color.WHITE);
		setTitle("Informe de Ventas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1080, 480);
		
		final Agregar agr1 = new Agregar();
		final Productos prod1 = new Productos();
		final Editar edit1 = new Editar();
		final Contratistas con1 = new Contratistas();
		menuBar = new JMenuBar();
		
		setJMenuBar(menuBar);
		
		mnIndice = new JMenu("\u00CDndice");
		mnIndice.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/menu24.png")));
		mnIndice.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnIndice);
		
		
		mntmMateriales = new JMenuItem("Materiales");
		mntmMateriales.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/materiales24.png")));
		mntmMateriales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prod1.setVisible(true);
				prod1.setExtendedState(JFrame.MAXIMIZED_BOTH);
				dispose();
			}
		});
		mnIndice.add(mntmMateriales);
		
		mntmAgregar = new JMenuItem("Agregar Proyecto");
		mntmAgregar.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/addclient24.png")));
		mntmAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agr1.setVisible(true);
				
			}
		});
		
		mntmNewMenuItem = new JMenuItem("Contratistas");
		mntmNewMenuItem.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/contratista24.png")));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				con1.setVisible(true);
				dispose();
			}
		});
		mnIndice.add(mntmNewMenuItem);
		mnIndice.add(mntmAgregar);
		
		mntmEditar = new JMenuItem("Editar Proyecto");
		mntmEditar.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/editclient24.png")));
		mntmEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edit1.setVisible(true);
				//setVisible(false);
			}
		});
		mnIndice.add(mntmEditar);
		
		mnAyuda = new JMenu("Ayuda");
		mnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnAyuda);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		
		JLabel buscar = new JLabel("Buscar");
		buscar.setHorizontalAlignment(SwingConstants.CENTER);
		buscar.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		
		textBuscar = new JTextField();
		textBuscar.setFont(new Font("Arial", Font.PLAIN, 13));
		textBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				 if (e.getKeyCode()==KeyEvent.VK_ENTER){
					 buscar();
				 }
			}
		});
		textBuscar.setColumns(10);
		
		daxalogo = new JLabel("");
		daxalogo.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/glpi.png")));
		
		btnAgregarCliente = new JButton("Agregar Proyecto");
		btnAgregarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agr1.setVisible(true);
				//setVisible(false);
			}
		});
		btnAgregarCliente.setFont(new Font("Arial", Font.BOLD, 13));
		
		btnEditarCliente = new JButton("Editar Proyecto");
		btnEditarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edit1.setVisible(true);
				//setVisible(false);
			}
		});
		btnEditarCliente.setFont(new Font("Arial", Font.BOLD, 13));
		
		//insertar y reescalar imágenes 
		add1 = new JLabel("");
		add1.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/addpng.png")));
		add1.setBounds(141, 106, 28, 28);
		contentPane.add(add1);
		
		ImageIcon ii = new ImageIcon(getClass().getResource("/iconos/addpng.png"));
		Image image = (ii).getImage().getScaledInstance(add1.getWidth(), add1.getHeight(), Image.SCALE_SMOOTH);
		ii = new ImageIcon(image);
		add1.setIcon(ii);
		
		edit = new JLabel("");
		edit.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/editpng.png")));
		edit.setBounds(141, 151, 28, 28);
		contentPane.add(edit);
		
		ImageIcon iii = new ImageIcon(getClass().getResource("/iconos/editpng.png"));
		Image imagei = (iii).getImage().getScaledInstance(edit.getWidth(), edit.getHeight(), Image.SCALE_SMOOTH);
		iii = new ImageIcon(imagei);
		edit.setIcon(iii);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame frame = new MainFrame();
				Object[] options = {"Sí", "No"};
				int response = JOptionPane.showOptionDialog(frame, "¿Realmente desea salir?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null,options, options[0]); 
				
				if(response == JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}
			}
		});
		btnSalir.setFont(new Font("Arial", Font.BOLD, 13));
		
		scrollPane = new JScrollPane();
		
		table = new JTable();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setViewportView(table);
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"Cliente", "Contacto", "Sucursal", "Fecha de venta", "Tipo de Instalaci\u00F3n", "Tecnolog\u00EDas Usadas", "Instalador/Venta"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(115);
		table.getColumnModel().getColumn(2).setPreferredWidth(115);
		table.getColumnModel().getColumn(2).setMinWidth(20);
		table.getColumnModel().getColumn(4).setPreferredWidth(115);
		table.getColumnModel().getColumn(5).setPreferredWidth(115);
		table.getColumnModel().getColumn(6).setPreferredWidth(115);
		table.setRowHeight(35);
		
		btnBuscar = new JButton("");
		btnBuscar.setIcon(new ImageIcon(MainFrame.class.getResource("/iconos/lupa32.png")));
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscar();
				
			}
		});
		btnBuscar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMostar = new JButton("Mostrar todo");
		btnMostar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("JDBC:MYSQL://localhost/clientes","root","");
					
					String sql = "select ID,(SELECT clientes.Nombre FROM clientes where clientes.ID = proyectos.id_cliente) AS 'Cliente', \r\n"
							+ "(SELECT sucursales.Nombre FROM sucursales where sucursales.ID = proyectos.id_sucursal) AS 'Sucursal', Contacto, \r\n"
							+ "Fecha_de_venta AS 'Fecha de venta ', Instalacion AS 'Instalación', Tecnologias AS 'Tecnologías Usadas', \r\n"
							+ "Instalador AS 'Instalador/Venta' from proyectos;";
					PreparedStatement pst = con.prepareStatement(sql);
					ResultSet rs = pst.executeQuery();
					
					table.setModel(DbUtils.resultSetToTableModel(rs));
					resizeColumnWidth(table);
				}
				catch(Exception e2){
					JOptionPane.showMessageDialog(null, e2);
				
				}
			}
		});
		
		btnMostar.setFont(new Font("Arial", Font.BOLD, 13));
		
		btnVerProductos = new JButton("Ver materiales");
		btnVerProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prod1.setVisible(true);
				prod1.setExtendedState(JFrame.MAXIMIZED_BOTH);
				dispose();
				
			}
		});
		btnVerProductos.setForeground(Color.RED);
		btnVerProductos.setFont(new Font("Arial", Font.BOLD, 13));
		
		btnVerContratistas = new JButton("Ver contratistas");
		btnVerContratistas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				con1.setVisible(true);
				dispose();
			}
		});
		btnVerContratistas.setForeground(Color.BLACK);
		btnVerContratistas.setFont(new Font("Arial", Font.BOLD, 13));
		
		separator = new JSeparator();
		separator.setForeground(SystemColor.activeCaption);
		separator.setBackground(SystemColor.activeCaption);
		separator.setOrientation(SwingConstants.VERTICAL);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBackground(SystemColor.inactiveCaption);
		editorPane.setEditable(false);
		editorPane.setFont(new Font("Arial", Font.BOLD, 15));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(8)
					.addComponent(daxalogo, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnAgregarCliente, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnEditarCliente, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(add1)
						.addComponent(edit))
					.addGap(145)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
					.addComponent(buscar, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(textBuscar, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(37))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1032, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnSalir, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
							.addGap(247)
							.addComponent(btnMostar, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(btnVerProductos, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(btnVerContratistas, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
							.addGap(98))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(editorPane, GroupLayout.DEFAULT_SIZE, 1032, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(daxalogo)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(20)
									.addComponent(buscar, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(18)
									.addComponent(textBuscar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(16)
									.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(5)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(add1)
										.addComponent(btnAgregarCliente, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
									.addGap(23)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(btnEditarCliente, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
										.addComponent(edit)))))
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(editorPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnMostar)
							.addComponent(btnSalir, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnVerProductos)
						.addComponent(btnVerContratistas))
					.addGap(14))
		);
		contentPane.setLayout(gl_contentPane);
		buscar();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				int j = table.getSelectedColumn();
				editorPane.setText(table.getValueAt(i, j).toString());
			
			}
		});
	}
}