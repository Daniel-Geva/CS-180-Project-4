package main;
/**
 *
 * Class that contains the detail of a teacher
 *
 * @author Sean Lee
 * @see User
 */
public class Teacher extends User {
    private final UserPermission userPermission;
    public Teacher(int ID, String name, String username, String password) {
        super(ID, name, username, password);
        userPermission = UserPermission.ADMIN;
    }

    /**
     * Returns the permission of the teacher
     * @return Permission of teacher
     */
    public Enum<UserPermission> getUserPermission() {
        return userPermission;
    }


    /**
     * Returns the teacher in String form
     * @return Teacher in String format
     */
    public String toString() {
        return super.toString() + String.format(", User Permission: %s", userPermission);
    }
}
