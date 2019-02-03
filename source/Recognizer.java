import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Recognizer {


	//the Array strings of gestures 
	private final String[] delete1 = {"Ls", "DOWNS", "Rs", "UPS", "Ls", "L_Ds"};
	private final String[] back1 = {"L_Ds", "R_Ds"};
	private final String[] select1 = {"L_Us", "R_Us", "R_Ds", "L_Ds"};
	private final String[] select2 = {"Ls", "UPS", "Rs", "DOWNS", "Ls"};
	private final String[] next1 = {"R_Ds", "L_Ds"};
	private final String[] red = {"UPS", "Rs", "DOWNS", "Ls", "DOWNS", "Rs"};
	private final String[] yellow =  {"R_Ds", "R_Us"};
	private final String[] textenbale = {"DOWNS", "Rs","UPS", "Ls"};
	private final String[] colorPanel = {"R_Us","R_Ds", "R_Us", "R_Ds", "R_Us"};
	private final String[] colorPanel2 = {"R_Us","R_Ds", "R_Us", "R_Ds", "R_Us"};
	private final String[] grabLeft = {"Ls"};
	private final String[] grabRight = {"Rs"};

	//Get patterns 
	private String getPattern(String[] vector) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("^");
        buffer.append(".{0,2}+");
        for (int i = 0; i < vector.length; i++) {
            switch (vector[i]) {
                case "RIGHT": buffer.append("E+"); break;
                case "UP": buffer.append("N+"); break;
                case "LEFT": buffer.append("W+"); break;
                case "DOWN": buffer.append("S+"); break;
                case "R_U": buffer.append("B+"); break;
                case "L_U": buffer.append("A+"); break;
                case "L_D": buffer.append("D+"); break;
                case "R_D": buffer.append("C+"); break;
                case "Rs": buffer.append("[CEB]+"); break;
                case "UPS": buffer.append("[NBA]+"); break;
                case "Ls": buffer.append("[AWD]+"); break;
                case "DOWNS": buffer.append("[DSC]+"); break;
                case "R_Ds": buffer.append("[ESC]+"); break;
                case "L_Ds": buffer.append("[WDS]+"); break;
                case "R_Us": buffer.append("[NBE]+"); break;
                case "L_Us": buffer.append("[WAN]+"); break;
                default:
                    break;
            }
        }
        
        buffer.append(".{0,2}+");
        buffer.append("$");
        return buffer.toString();
    }

    //match gesture ==> pointlist
	public String getGesture(List<point> pointList) {
		String pString = buildGesture(pointList);
		if(getNext(pString)) {
			System.out.println("NEXT here ");
			return "Next";
		} else if(getBack(pString)) {
			System.out.println("BACK here ");
			return "Back";
		} else if(getDelete(pString)) {
			System.out.println("DELETE here ");
			return "Delete";
		} else if(getSelect(pString)) {
			System.out.println("SELECT here ");
			return "Select";
		} else if (getRed(pString)) {
			return "Red";
		} else if (getYellow(pString)) {
			return "Yellow";
		} else if (getTextEnable(pString)) {
			return "Text";
		} else if (getColorPanel(pString)) {
			return "color";
		} else if (getGrabLeft(pString)) {
			System.out.println("Back here ");
			Matcher m = Pattern.compile("W").matcher(pString);
			int count = 0;
			while (m.find()) {
				count++;
			}
			if (count > 70) {
				return "Back";
			}
		} else if (getGrabRight(pString)) {
			Matcher m = Pattern.compile("E").matcher(pString);
			int count = 0;
			while (m.find()) {
				count++;
			}
			if (count > 70) {
				return "Next";
			}
		}
		return "None";
	}


	//$1 Gesture=> Rectangle 
	private boolean getRed(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(red))};
		System.out.println("Red"+ pString);
		for (int i = 0; i < n.length; i++) {
			 if (n[i].matcher(pString).find()) {
			 	return true;
			 }
		}
		return false;
	}
	//$1 Gesture => Oval
	private boolean getYellow(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(yellow))};
		System.out.println("yellow"+ pString);
		for (int i = 0; i < n.length; i++) {
			 if (n[i].matcher(pString).find()) {
			 	return true;
			 }
		}
		return false;
	}
	//$1 Gesture => Text
	private boolean getTextEnable(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(textenbale))};
		System.out.println("textenbale"+ pString);
		for (int i = 0; i < n.length; i++) {
			 if (n[i].matcher(pString).find()) {
			 	return true;
			 }
		}
		return false;
	}
	//$1 Gesture => Text
	private boolean getColorPanel(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(colorPanel))};
		System.out.println("colorPanel"+ pString);
		for (int i = 0; i < n.length; i++) {
			 if (n[i].matcher(pString).find()) {
			 	return true;
			 }
		}
		return false;
	}

	//$1 Gesture => Delete
	private boolean getDelete(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(delete1))};
		for (int i = 0; i < n.length; i++) {
			 if (n[i].matcher(pString).find()) {
			 	return true;
			 }
		}
		return false;
	}
	

	//$1 Gesture => Select
	private boolean getSelect(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(select1))};
		Pattern[] n2 = {Pattern.compile(getPattern(select2))};
		System.out.println("select"+ pString);
		for (int i = 0; i < n.length; i++) {
			 if (n[i].matcher(pString).find()) {
			 	return true;
			 }
		}
		for (int i = 0; i < n2.length; i++) {
			if (n2[i].matcher(pString).find()) {
				return true;
			}
		}

		return false;
	}
	

	//$1 Gesture => Next
	private boolean getNext(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(next1))};
		System.out.println("next"+ pString);
		for (int i = 0; i < n.length; i++) {
			 if (n[i].matcher(pString).find()) {
			 	return true;
			 }
		}
		return false;
	}

	private boolean getGrabRight(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(grabRight))};
		System.out.println("grabRight" + pString);
		for (int i = 0; i < n.length; i++) {
			if (n[i].matcher(pString).find()) {
				return true;
			}
		}
		return false;
	}

	private  boolean getGrabLeft(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(grabLeft))};
		System.out.println("grabLeft" + pString);
		for (int i = 0; i < n.length; i++) {
			if (n[i].matcher(pString).find()) {
				return true;
			}
 		}
 		return false;
	}
	//$1 Gesture ==> Back 
	private boolean getBack(String pString) {
		Pattern[] n = {Pattern.compile(getPattern(back1))};
		System.out.println("back"+ pString);
		for (int i = 0; i < n.length; i++) {
			 if (n[i].matcher(pString).find()) {
			 	return true;
			 }
		}
		return false;
	}
	
	//buildGesture
	private String buildGesture(List<point> pointList) {
		String pString = "";
		for(int i = 1; i < pointList.size(); i++) {
			pString = pString + pointList.get(i).identifyGesture(pointList.get(i-1));
		}
		System.out.println(pString);
		
		return pString;
	}
	

}