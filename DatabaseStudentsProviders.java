package kz.dataBase.study.classes;
import kz.dataBase.study.interfaces.SetHandler;
import kz.dataBase.study.interfaces.StudentsProviders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseStudentsProviders implements StudentsProviders {

	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:postgresql://localhost:5432/test","postgres","123");
	}

	private static SetHandler<Student> ONE_STUDENT_HANDLER = new SetHandler<Student>() {
		@Override
		public Student handle(ResultSet rs) throws SQLException {
			if (rs.next()) {
				return new Student(rs.getLong("id"), rs.getString("name"), rs.getString("surname"), rs.getInt("age"));
			} else {
				return null;
			}
		}
	};
	private static SetHandler<List<Student>> LIST_STUDENTS_HANDLER = new SetHandler<List<Student>>() {
		@Override
		public List<Student> handle(ResultSet rs) throws SQLException {
			List<Student> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Student(rs.getLong("id"), rs.getString("name"), rs.getString("surname"), rs.getInt("age")));
			}
			return list;
		}
	};
	private static SetHandler<Long> COUNT_HANDLER = new SetHandler<Long>() {
		@Override
		public Long handle(ResultSet rs) throws SQLException {
			if (rs.next()) {
				return rs.getLong(1);
			} else {
				return 0L;
			}
		}
	};

	@Override
	public List<Student> findAll(int offset, int limit) throws SQLException {
		try (Connection c = getConnection()) {
			return JDBCUtils.select(c, "select * from student limit ? offset ?", LIST_STUDENTS_HANDLER, limit, offset);

		}
	}

	@Override
	public Student findById(Long id) throws SQLException {
		try (Connection c = getConnection()) {
			return JDBCUtils.select(c, "select * from student where id = ?", ONE_STUDENT_HANDLER, id);
		}
	}
	@Override
	public long countAll() throws SQLException {
		try (Connection c = getConnection()) {
			return JDBCUtils.select(c, "select count(*) from student", COUNT_HANDLER);
		}
	}

	@Override
	public List<Student> findByAge(int age, int offset, int limit) throws SQLException {
		try (Connection c = getConnection()) {
			return JDBCUtils.select(c, "select * from student where age=? limit ? offset ?", LIST_STUDENTS_HANDLER, age, limit, offset);
			}
	}

	@Override
	public long countByAge(int age) throws SQLException {
		try (Connection c = getConnection()) {
			return JDBCUtils.select(c, "select count(*) from student where age=?", COUNT_HANDLER, age);

		}
	}

	@Override
	public void create(Student student)throws SQLException {
		try (Connection c = getConnection()) {
			Student insertedStudent = JDBCUtils.insert(c, "insert into student values(nextval('student_seq'),?,?,?)", ONE_STUDENT_HANDLER, 
					student.getName(), student.getSurname(), student.getAge());
			student.setId(insertedStudent.getId());

		}
	}

	@Override
	public void update(Student student) {
		try (Connection c = getConnection()) {
			int result = JDBCUtils.executeUpdate(c, "update student set name=?, surname=?, age=? where id=?",
					student.getName(), student.getSurname(), student.getAge(), student.getId());
			if (result == 0) {
				throw new SQLException("dsd.Student not found by id="+student.getId());
			}
		} catch (SQLException e) {
			System.out.println(("Can't execute SQL query: " + e.getMessage()));
		}
	}

	@Override
	public void delete(Student student) throws SQLException {
		deleteById(student.getId());
	}

	@Override
	public void deleteById(final Long id) throws SQLException {
		try (Connection c = getConnection()) {
			int result = JDBCUtils.executeUpdate(c, "delete from student where id=?", id);
			if (result == 0) {
				throw new SQLException("dsd.Student not found by id="+id);
			}

		}
		/*execute(new Callback<Void>(){
			@Override
			public Void execute(Connection c) throws SQLException {
				int result = dsd.JDBCUtils.executeUpdate(c, "delete from student where id=?", id);
				if (result == 0) {
					throw new ProviderRetrieveException("dsd.Student not found by id="+id);
				}
				return null;
			}
		});*/
	}
	
	protected <T> T execute(Callback<T> callback) throws SQLException {
		try (Connection c = getConnection()) {
			return callback.execute(c);
		}
	}
	
	private interface Callback<T> {
		T execute(Connection c) throws SQLException;
	}
}
