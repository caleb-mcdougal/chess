package dataAccess;

public interface UserDAO {
    public String getUsername();

    public int inputUsername();
    public String getPassword();

    public int inputPassword();
    public String getEmail();
    public int inputEmail();
    public int clear();

}
