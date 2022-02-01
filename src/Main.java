import java.text.DecimalFormat;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String text = "";
		if(text != null && !text.isEmpty()) {
			System.out.println("no vacio");
		}else {
			System.out.println("vacio");
		}
		String s = "USD 507";
		Boolean coma = false;
		for (int i = 0; i < s.length(); i++){
		    char c = s.charAt(i);  
		    if(c==',') {
		    	coma = true;
		    }
		}
		if (coma==false) {
			
			System.out.println(s + ",00");
		}else {
			System.out.println(s);
		}
		
		String number = "1000500000.574";
		double amount = Double.parseDouble(number);
		DecimalFormat formatter = new DecimalFormat("#,###.00");

		System.out.println(formatter.format(amount));
		
		String numt = "12,34,5";
		for(int j=0;j<numt.length();j++) {
			char a = numt.charAt(j);
			if(a != ',') {
				continue;
			}else {
				System.out.println("retroceda");
				break;
			}
			
		}
		
		for (int i = 0; i < 10; i++) {
			  if (i != 4) {
			    continue;
			  }
			  System.out.println(i);
			}
		
	
	
		
	}

}
