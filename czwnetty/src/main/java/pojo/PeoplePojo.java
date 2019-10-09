package pojo;

import java.io.Serializable;

public class PeoplePojo implements Serializable {
    public static final long serialVersionUID = 1L;
    private String name;

    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "PeoplePojo{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
