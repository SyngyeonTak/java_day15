package day1105.db;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EmpApp2 extends JFrame{
	JButton bt_connect, bt_load;
	JTextArea area;
	JScrollPane scroll;
	
	//JDBC 기술은 DBMS 종류에 상관없이 중립적인 코드를 작성할 수 있도록 지원한다..
	//해당 DB기종마다 알맞는 Driver를 사용해야 한다.
	String url = "jdbc:mysql://localhost/db1105";//jdbc:기종://로컬호스트
	String user = "root";
	String password = "1234";
	
	Connection con;//접속 후, 그 정보를 가진 객체, 따라서 접속을 끊으려면 이 객체가 필요하다.
	PreparedStatement pstmt;//쿼리문 수행 객체, 인터페이스이므로 new로 생성하는 것이 아니라
											//접속객체인 Connection 객체로 부터 인스턴스를 얻어올 수 있다.
											//why? 접속이 성공되어야 쿼리문을 수행할 수 있으므로 접속객체에 의존적인 것이 당연하다!!
	ResultSet rs;//select 쿼리문 수행결과에 의해 표가 반환되는데, 이때 이 표를 담는 객체
	
	public EmpApp2() {
		bt_connect = new JButton("Connect");
		bt_load = new JButton("Load");
		
		area = new JTextArea();
		scroll = new JScrollPane(area);

		//스타일 적용
		area.setPreferredSize(new Dimension(780, 500));
		
		setLayout(new FlowLayout());
		
		add(bt_connect);
		add(bt_load);
		add(scroll);
		
		//버튼과 리스너 연결
		bt_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		bt_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load();//select로 emp 가져오기
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					if(rs !=null) rs.close(); 
					if(pstmt !=null) pstmt.close(); 
					if(con !=null) con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				System.exit(0);//프로세스 종료
			}
		});
		
		setSize(800, 600);
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE); DB를 닫지 않고, 프로세스만 종료해버리니깐...
	}
	
	public void connect() {
		//오라클 접속시도하기!!(1.드라이버 로드 2.접속)
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			area.append("드라이버 로드 성공\n");
			
			//접속시도
			con = DriverManager.getConnection(url, user, password);
			if(con == null) {
				area.append("접속실패\n");
			}else {
				area.append("접속성공\n");				
			}
		} catch (ClassNotFoundException e) {
			area.append("드라이버 로드 실패\n");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		//select 문을 실행해본다.
		String sql = "select * from emp";
		try {
			pstmt = con.prepareStatement(sql);
			//pstmt.executeUpdate();//DML(insert, update, delete)의 경우만
			rs = pstmt.executeQuery();//select 문의 경우엔 executeQuery() 이용해야 한다.
													//select문을 수행시키면 메모리에 테이블이 올라온다
													//이 테이블을 ResultSet 객체로 받아와야 한다.
			//rs에는 표가 들어잇다!! 따라서 원하는 레코드로 커서를 옮기자!!
			
			area.append("EMPNO\tENAME\tJOB\tMGR\tHIREDATE\t\tSAL\tCOMM\tDEPTNO\t\n");
			while (rs.next()) {//데이터가 존재하면, 한칸 전진 후 true반환
				int empno = rs.getInt("empno");
				String ename = rs.getString("ename");
				String job = rs.getString("job");
				String mgr = rs.getString("mgr");
				String hiredate = rs.getString("hiredate");
				int sal = rs.getInt("sal");
				int comm = rs.getInt("comm");
				int deptno = rs.getInt("deptno");
				area.append(empno + "\t" + ename + "\t" + job + "\t" + mgr + "\t" + hiredate + "\t" + sal + "\t" + comm
						+ "\t" + deptno+"\n");
			}
		} catch (SQLException e) {
			area.append("쿼리문 수행 실패");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new EmpApp2();
	}
}







