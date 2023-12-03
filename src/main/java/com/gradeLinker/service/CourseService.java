package com.gradeLinker.service;

import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.Student;
import com.gradeLinker.domain.user.User;
import com.gradeLinker.dto.storage.CourseDTOMapper;
import com.gradeLinker.repository.CourseRepository;
import com.gradeLinker.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseService {
    private final Object lock = new Object();
    private final IdGenerator<String> idGenerator;

    private final CourseDTOMapper courseDTOMapper;
    private final GradeFactory gradeFactory;
    private final CourseRepository courseRepo;
    private final UsersRepository usersRepo;

    @Autowired
    public CourseService(IdGenerator<String> idGenerator, CourseDTOMapper courseDTOMapper, GradeFactory gradeFactory, CourseRepository courseRepo, UsersRepository usersRepo) {
        this.idGenerator = idGenerator;
        this.courseDTOMapper = courseDTOMapper;
        this.gradeFactory = gradeFactory;
        this.courseRepo = courseRepo;
        this.usersRepo = usersRepo;
    }

    public Course getCourseById(String courseId) {
        return courseDTOMapper.fromDTO(courseRepo.getById(courseId));
    }
    public List<Course> getCoursesByIds(Set<String> courseIds) {
        List<Course> courses = new ArrayList<>();
        for (String courseId: courseIds) {
            courses.add(this.getCourseById(courseId));
        }

        return courses;
    }
    public List<Student> getStudentsSortedByFullName(Course course) {
        List<Student> students = new ArrayList<>();

        List<CourseParticipant> participants = new ArrayList<>(course.getParticipants().values().stream().toList());
        Collections.sort(participants);

        CourseGrades courseGrades = course.getCourseGrades();
        for (CourseParticipant participant: participants) {
            if (courseGrades.getStudentUsernames().contains(participant.getUsername())) {
                students.add(new Student(
                        participant.getUsername(),
                        participant.getFullName(),
                        participant.getRoles(),
                        courseGrades.getGradesByUsername(participant.getUsername())
                ));
            }
        }
        return students;
    }

    public Student getStudentByUsername(Course course, String studentUsername) {
        CourseParticipant participant = course.getParticipantByUsername(studentUsername);

        return new Student(
                participant.getUsername(),
                participant.getFullName(),
                participant.getRoles(),
                course.getCourseGrades().getGradesByUsername(studentUsername)
        );
    }

    private String generateUniqueCourseId() {
        synchronized (lock) {
            String id = "";
            do {
                id = idGenerator.generateId();
            }
            while (courseRepo.existsById(id));

            return id;
        }
    }
    public String createCourse(String title, CourseParticipant participant) {
        // object-lock here, so different users couldn't generate the same id
        String id = this.generateUniqueCourseId();

        Course course = new Course(
                id,
                title,
                Map.of(
                        participant.getUsername(),
                        participant
                ),
                gradeFactory.createCourseGrades(new ArrayList<>())
        );
        this.saveCourse(course);

        return id;
    }
    public void saveCourse(Course course) {
        courseRepo.save(course.getId(), courseDTOMapper.toDTO(course));
    }
}
