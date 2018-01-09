import java.io 

.DataInputStream;
import java.io 

.File;
import java.io 

.FileOutputStream;
import java.io 

.IOException;
import java.net 

.ServerSocket;
import java.net 

.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.omg.CORBA.portable.InputStream;

public class myserver {
	public static void main(String[] args) throws IOException{
		
		ServerSocket server = new ServerSocket(8884); 
		System.out.println("Server is ready!");
		while(true){
		Socket connsocket = server.accept();  
		new Handler(connsocket).start();
		}
	}
	
}

class Handler extends Thread{
	private Socket client;
	int command;
	int result;
	String[] arraymsg;
	String filename;
	int filelength;
	public Handler(Socket socket){
		this.client=socket;
	}
	
	public void run(){
		String ip = client.getInetAddress().getHostAddress();  
	    System.out.println(ip + "...conncected");  
	    try{
	    DataInputStream input = new DataInputStream(client.getInputStream()); 
	    byte[] header=new byte[108];
	    input.read(header,0,108);
	    byte[] cmd=new byte[4];
	    byte[] length=new byte[4];
		byte[] data=new byte[100];
		System.arraycopy(header,0,cmd,0,4);
		System.arraycopy(header,4,length,0,4);
		System.arraycopy(header,8,data,0,100);
		command=byteArrayToInt(cmd);
		filelength=byteArrayToInt(length);
		filename=String.valueOf(getChars(data));
		//System.out.println(byteArrayToInt(cmd));
		//command=1;
		//filename="18408253338 123456";
		System.out.println("recieving size "+byteArrayToInt(length)+"byte");
		//System.out.println(getChars(data));
		switch(command){
		case 1:
			 arraymsg=filename.split(" ");
			 result=insert(arraymsg[0],arraymsg[2],arraymsg[1],arraymsg[3]);
			 System.out.println(result);
		case 2:
			arraymsg=filename.split(" ");
			result=search(arraymsg[0],arraymsg[1]);
		case 3:
			byte[] buf=new byte[filelength];
			input.read(buf,0,filelength);
			File file = new File("E:/"+filename);
			FileOutputStream fop = new FileOutputStream(file);
			if (!file.exists()) {
			    file.createNewFile();
			   }
			fop.write(buf,0,filelength);
			fop.flush();
			fop.close();
			System.out.println("done!");
			this.client.close();
			break;
		}
	    }catch(IOException e){e.printStackTrace();}
	    System.out.println("Client exit!");
	}
	public static byte[] intToByteArray(int a) {  
	    return new byte[] {  
	        (byte) ((a >> 24) & 0xFF),  
	        (byte) ((a >> 16) & 0xFF),     
	        (byte) ((a >> 8) & 0xFF),     
	        (byte) (a & 0xFF)  
	    };
	}
	public static int byteArrayToInt(byte[] b) {  
	    return   b[3] & 0xFF |  
	            (b[2] & 0xFF) << 8 |  
	            (b[1] & 0xFF) << 16 |  
	            (b[0] & 0xFF) << 24;  
	}  
	
	public static byte[] getBytes(char[] chars) {
		   Charset cs = Charset.forName ("UTF-8");
		   CharBuffer cb = CharBuffer.allocate (chars.length);
		   cb.put (chars);
		                 cb.flip ();
		   ByteBuffer bb = cs.encode (cb);
		  
		   return bb.array();

		 }
	
	public static  char[] getChars (byte[] bytes) {
		  int length; 
		  for(length=0;length<bytes.length;length++){
			  if(bytes[length]==0)
				  break;
		  }
		  byte[] mybyte=new byte[length];
		  System.arraycopy(bytes, 0, mybyte, 0, length);
	      Charset cs = Charset.forName ("UTF-8");
	      ByteBuffer bb = ByteBuffer.allocate (mybyte.length);
	      bb.put (mybyte);
	                 bb.flip ();
	       CharBuffer cb = cs.decode (bb);
	  
	   return cb.array();
	}
	 private static Connection getConn() {
	        String driver ="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	        String url = "jdbc:sqlserver://localhost:1433;databasename=Health APP";
	        String username = "sa";
	        String password = "1";
	        Connection conn = null;
	        try {
	            Class.forName(driver); //classLoader,加载对应驱动
	            conn = (Connection) DriverManager.getConnection(url, username, password);
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return conn;
	    }
	    private static int insert(String id,String pwd,String name,String age){
	        Connection conn = getConn();
	        int result=0;
	        int i = 0;
	        String sql = "insert into UserInf(UserID,UserPassword,UserName,UserAge) values(?,?,?,?)";
	        PreparedStatement pstmt;
	        try {
	            pstmt = (PreparedStatement) conn.prepareStatement(sql);
	            pstmt.setString(1,id);
	            pstmt.setString(2,pwd);
	            pstmt.setString(3, name);
	            pstmt.setString(4, age);
	            i=pstmt.executeUpdate();
	            if(i==1)
	             result=1;
	            pstmt.close();
	            conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    private static int search(String id,String pwd) {
	    	int result=0;
	        Connection conn = getConn();
	        String sql = "select * from UserInf where UserID="+id;
	        PreparedStatement pstmt;
	        try {
	            pstmt = (PreparedStatement)conn.prepareStatement(sql);
	            ResultSet rs = pstmt.executeQuery();
	            rs.next();
	            String pwd1=rs.getString(2);
	        System.out.println("id："+rs.getString(1));
	        System.out.println("pwd："+pwd1);
	      
	        if(pwd.equals(pwd1))
	        	result=1;              
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
}