package kz.dataBase.study.interfaces;

import kz.dataBase.study.classes.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentsProviders {
    List<Student> findAll(int offset, int limit)throws SQLException;
    long countAll()throws SQLException;
    Student findById(Long id)throws SQLException;
    List<Student>findByAge(int age, int offset, int limit)throws SQLException;
    long countByAge(int age)throws SQLException;
    void create(Student student)throws SQLException;
    void update(Student student)throws SQLException;
    void delete(Student student)throws SQLException;
    void deleteById(Long id)throws SQLException;
}
