package api.models;

public class LinkForm {
    String name;
    String password;
    String targetURL;

    public LinkForm(String name, String password, String targetURL) {
        this.name = name;
        this.password = password;
        this.targetURL = targetURL;
    }

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

    public String getTargetURL() {
        return targetURL;
    }

    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }
}
