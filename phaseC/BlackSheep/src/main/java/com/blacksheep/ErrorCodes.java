package com.blacksheep;

/** This Class contains all the error codes
 *
 */
public class ErrorCodes {
    // Success
    public static final int SUCCESS                   =   0;
    //Common codes
    public static final int CONNECTION_FAILURE       =    -1000;
    public static final int USERID_INVALID           =    -500;


    // Form validation
    // User account creation error codes
    public static final int PERCENTAGE_INVALID       =   -6;
    public static final int EMAIL_TAKEN              =   -5;
    public static final int EMAIL_INVALID            =   -4;
    public static final int PASSWORD_MISMATCH        =   -3;
    public static final int PASSWORD_TOO_SHORT       =   -2;
    public static final int ROLE_INVALID             =   -1;

    public static final int DB_UPDATE_FAILURE       = -700;
    //Hiding public constructor with private constructor
    private ErrorCodes(){ }
}
