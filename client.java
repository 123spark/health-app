mport java.io 

.BufferedReader;  
import java.io 

.InputStreamReader;

class client_cmd{
	 String opd;
	 String msg;
	 String result;
	client_cmd(String opd,String msg){
		this.opd=opd;
		this.msg=msg;
	}
	String getOpd(){
		return this.opd;
	}
	String getMsg(){
		return this .msg;
	}
	void setResult(String result){
		this.result=result;
	}
	String getResult(){
		return this.result;
	}
	
}  
public class client {  
        public static void main(String[] args){  
                try{  
                	client_cmd cli=new client_cmd("3","user.txt");
                	   String argv1=cli.getOpd();
                	   String argv2=cli.getMsg();
                	   String argv0="e:/client.py 

";
                       
                	   String cmd1="c:/Python27/python.exe";
                	   String cmd=cmd1+" "+argv0+" "+argv1+" "+argv2;
                        System.out.println("start");  
                        Process pr = Runtime.getRuntime().exec(cmd); 
                        BufferedReader in = new BufferedReader(new  InputStreamReader(pr.getInputStream()));  
                        String line;  
                       while ((line = in.readLine()) != null) {
                    	   if(line.equals("1")||line.equals("0"))
                    		cli.setResult(line);
                            System.out.println(line);  
                        }  
                        in.close();  
                        pr.waitFor();
                        System.out.print("the result is:");
                        System.out.println(cli.getResult());
                        System.out.println("end");  
                } catch (Exception e){  
                            e.printStackTrace();  
                        }  
                }  
}  



