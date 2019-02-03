import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.table.AbstractTableModel;

public class CreateCalendar extends AbstractTableModel {
    private String[] week = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    private int[] numDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private String[][] calendar = new String[7][7];

    public CreateCalendar() {
        for (int i = 0; i < week.length; ++i) {
            calendar[0][i] = week[i];
        }
        for (int i = 1; i < 7; ++i){
            for (int j = 0; j < 7; ++j){
                calendar[i][j] = " ";
            }
        }
    }
  public void setMonth(int year, int month) {
    for (int i = 1; i < 7; ++i) {
   		for (int j = 0; j < 7; ++j) {
	        calendar[i][j] = " ";
      }
    }
  	Calendar newCal = new GregorianCalendar();
  	newCal.set(year, month, 1);
  	int offset = newCal.get(GregorianCalendar.DAY_OF_WEEK) - 1;
  	offset = offset + 7;
  	int num = daysInMonth(year, month);
    for (int i = 0; i < num; ++i) {
  		calendar[offset / 7][offset % 7] = Integer.toString(i + 1);
  		++offset;
    }
  }


  public int getColumnCount() {
    return 7;
  }

  public int getRowCount() {
    return 7;
  }
  public void setValueAt(Object value, int row, int column) {
    calendar[row][column] = (String) value;
  }

  public Object getValueAt(int row, int column) {
    return calendar[row][column];
  }



  public boolean isLeapYear(int year) {
    if (year % 4 == 0) {
      return true;
    } else {
      return false;
    }
  }

  public int daysInMonth(int year, int month) {
    int day = numDays[month];
    if (month == 1 && isLeapYear(year)) {
      ++day;
    }
    return day;
  }
}