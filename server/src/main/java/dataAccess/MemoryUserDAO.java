package dataAccess;

public class MemoryUserDAO implements UserDAO{
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public int inputUsername() {
        return 0;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public int inputPassword() {
        return 0;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public int inputEmail() {
        return 0;
    }

    @Override
    public int clear() {
        return 200;
    }
}
