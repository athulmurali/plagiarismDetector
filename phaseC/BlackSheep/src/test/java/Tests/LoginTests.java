//package Tests;
//
//import com.blacksheep.CreateAST;
//import com.blacksheep.ParserFacade;
//import com.blacksheep.controller.LoginController;
//import org.junit.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.io.File;
//import java.io.IOException;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * This class tests login and createAST
// */
//public class LoginTests {
//
////    /**
////     * Test for the print function of the AST
////     */
////    @Test
////    public void test1() {
////
////        ParserFacade parserFacade = new ParserFacade();
////        CreateAST astPrinter = new CreateAST();
////
////        try {
////            assertEquals("defsum(a,b):     a+b e+c",astPrinter.print(parserFacade.parse(new File("/Users/prashanthavanagi/IdeaProjects/BlackSheep/src/main/java/com/blacksheep/controller/simple.py"))));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////    }
//
//
//    /**
//     * Test if the springboot function is running
//     */
//    @Test
//    public void test2() {
//
//        LoginController l = new LoginController();
//
//        assertEquals("hello", l.hello() );
//
//    }
//
//    /**
//     * Tests if the passcode match is working
//     */
//    @Test
//    public void test3() {
//
//        LoginController l = new LoginController();
//
//        assertEquals(ResponseEntity.status(HttpStatus.OK).build(), l.validateLoginString("passcode"));
//
//    }
//
//    /**
//     * Tests if passcode match returns false on incorrect password
//     */
//    @Test
//    public void test4() {
//
//        LoginController l = new LoginController();
//
//        assertEquals(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build(), l.validateLoginString("abc123"));
//
//    }
//
//
//
//
//
//
//}
