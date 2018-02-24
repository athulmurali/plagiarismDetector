package spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@SpringBootApplication
@RestController
public class SpringBootdemoApplication {


    //private String passcode = "abc";
    private static final String URL = "jdbc:mysql://localhost:3306/login";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        SpringApplication.run(SpringBootdemoApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(method = RequestMethod.POST,value = "/passcode")
    public ResponseEntity<String> helloAgain(@RequestBody String passcode123){

	    String password = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String sql = "SELECT passcode FROM credentials";
            results = statement.executeQuery(sql);

            while(results.next()) {
                password = results.getString("passcode");
                System.out.println("Matched");
            }

            if(password.equals(passcode123))
                return ResponseEntity.status(HttpStatus.OK).build();

        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }
}


//    @RequestMapping(method = RequestMethod.POST, value = "/passcode")
//    public ResponseEntity<String> helloAgain(@RequestBody String passcode123) {
//
//        System.out.println(passcode123);
//
//        if (passcode123.equals(passcode)) {
//            System.out.println("Success again");
//            return ResponseEntity.status(HttpStatus.OK).build();
//
//        }
//        System.out.println("Not Success!");
//        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
//    }


//    @RequestMapping(method = RequestMethod.POST,value = "/files")
//    public ResponseEntity<String> helloAgain2(@RequestBody File myfile){
//
//            System.out.print("Success again" + myfile);
//            return ResponseEntity.status(HttpStatus.CREATED).build();
//    }


