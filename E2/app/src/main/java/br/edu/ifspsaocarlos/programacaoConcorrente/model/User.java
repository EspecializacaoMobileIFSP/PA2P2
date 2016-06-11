package br.edu.ifspsaocarlos.programacaoConcorrente.model;

/**
 * @author maiko.trindade
 * @since 09/06/2016
 */
public class User {

    private String name;
    private String username;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
