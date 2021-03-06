package kz.dataBase.study.classes;

import kz.dataBase.study.interfaces.StudentsProviders;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

       StudentsProviders sp = new DatabaseStudentsProviders();
        System.out.println("findById="+sp.findById(1L));
        System.out.println("findAll="+sp.findAll(0, 10));
        System.out.println("countAll="+sp.countAll());
        System.out.println("findByAge="+sp.findByAge(22, 0, 10));
        System.out.println("countByAge="+sp.countByAge(22));
        Student s = new Student("test", "test", 19);
        sp.create(s);
        s.setAge(25);
        sp.update(s);
       sp.delete(s);
        System.out.println(s);
    }
}
