/*
 * 스윙 컴포넌트 중, 이차원 구조의 데이터(표)를 표현하기에 최적화된  JTable을 사용해보자
 * 
 * */

package day1105.db;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TableApp extends JFrame{
	JTable table;
	String[] column = {"번호", "이름", "연락처", "주소", "성별"};
	String[][] data = {
			{"1", "Batman", "011", "고담시", "남"},	
			{"2", "Superman", "017", "크립톤", "남"},	
			{"3", "wonder", "019", "아마존", "여"}
				
	};
	JScrollPane scroll;
	public TableApp() {
		table = new JTable(data, column);//row3개 col 7개
		setLayout(new FlowLayout());
		scroll = new JScrollPane(table);
		add(scroll);//Center
		
		//마우스 이벤트 부여
		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int col = table.getSelectedColumn();//유저가 선택한 컬럼 index(호수)
				int row = table.getSelectedRow();//유저가 선택한 컬럼 index(층수)
				String value = (String)table.getValueAt(row, col);
				System.out.println(value);
			}
		});
		
		setVisible(true);
		setSize(500, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new TableApp();
	}
}
