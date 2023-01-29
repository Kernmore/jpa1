package jpa1;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="SimpleClients")
public class SimpleClient {
    @Id
    @GeneratedValue //(strategy = GenerationType.AUTO)
    @Column(name="myid")
    private long id;

    @Column(nullable = false)
    private String name;

    private int age;

    public SimpleClient() {}

    public SimpleClient(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "SimpleClient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
