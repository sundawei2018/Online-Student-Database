package com.dawei;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private StudentDBUtil studentDBUtil;
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public StudentControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			studentDBUtil = new StudentDBUtil(dataSource);
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//
			String theCommand = request.getParameter("command");
			
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			switch(theCommand) {
			
			case "LIST":
				listStudents(request, response);
				break;
			
			case "ADD":
				addStudent(request,response);
				break;
			
			case "UPDATE":
				//System.out.println("yes");
				updateStudent(request,response);
				break;
				
			case "LOAD":
				loadStudent(request, response);
				break;
				
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudents(request, response);
			}
			
		} catch (Exception e) {
			
			throw new ServletException(e);
		}
		
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String studentId = request.getParameter("studentId");
		studentDBUtil.deleteStudent(studentId);
		listStudents(request,response);
		
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentID"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		Student theStudent = new Student(id, firstName,lastName,email);
		studentDBUtil.updateStudent(theStudent);
		listStudents(request,response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String theStudentId = request.getParameter("studentId");
		Student theStudent = studentDBUtil.getStudent(theStudentId);
		request.setAttribute("THE_STUDENT", theStudent);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student theStudent = new Student(firstName,lastName,email);
		studentDBUtil.addStudent(theStudent);
		listStudents(request,response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
			// get students from db util
			List<Student> students = studentDBUtil.getStudents();
			// add students to the request
			request.setAttribute("STUDENT_LIST", students);					
			// send to JSP page (view)
			RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
			dispatcher.forward(request, response);
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
