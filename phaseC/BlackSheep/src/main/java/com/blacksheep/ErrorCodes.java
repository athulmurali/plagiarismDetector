package com.blacksheep;

/** This Class contains all the error codes
 *
 */
public class ErrorCodes {
    // Success
    public static int SUCCESS                   =   0;
    //Common codes
    public static int CONNECTION_FAILURE       =    -1000;
    public static int USERID_INVALID           =    -500;


    // Form validation
    // User account creation error codes
    public static int PERCENTAGE_INVALID       =   -6;
    public static int EMAIL_TAKEN              =   -5;
    public static int EMAIL_INVALID            =   -4;
    public static int PASSWORD_MISMATCH        =   -3;
    public static int PASSWORD_TOO_SHORT       =   -2;
    public static int ROLE_INVALID             =   -1;


    //Hiding public constructor with private constructor
    private ErrorCodes(){ }

}
