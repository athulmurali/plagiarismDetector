package plagiarism.detector;

/**
 * IAuth - Interface Authentication
 * This interface contains methods for all authentication related functions<br><br>
 * Purpose : Any change in the configuration of the system ( operations) should
 * be possible, only if the user is authorized to do so.
 *
 * <br><br>
 *     This interface is designed in a way that the actual passwords can never be extract.
 *     Only the hashes of the password should be obtainable when the need arises.
 *
 */

public interface IAuth {
    /**
     *
     * authenticates the user with the given password
     * @return true if the given password, matches the current password
     * else false
     *
     * @see <a href="https://stackoverflow.com/questions/8881291/why-is-char-preferred-over-string-for-passwords">
     *     Why return type of char[] over String?</a>

     */
     boolean authenticate(char[] givenPassword);

    /** changePassword - replaces the current password with the new password.
     * Once replaced, the new password becomes the current password and all authentication
     * methods will refer to it.
     *

     * @return
     * 0 if the password has been changed successfully
     *<br>
     * -1 if there are any issues with current password authentication
     *<br>
     * -2 if there are any issues with new password validation
     */
     int changePassword(char[] newPassword);


}
