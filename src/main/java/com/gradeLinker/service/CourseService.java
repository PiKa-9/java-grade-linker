package com.gradeLinker.service;

import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.Student;
import com.gradeLinker.dto.storage.CourseDTOMapper;
import com.gradeLinker.repository.CourseRepository;
import com.gradeLinker.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    private final CourseDTOMapper courseDTOMapper;
    private final CourseRepository courseRepo;
    private final UsersRepository usersRepo;

    @Autowired
    public CourseService(CourseDTOMapper courseDTOMapper, CourseRepository courseRepo, UsersRepository usersRepo) {
        this.courseDTOMapper = courseDTOMapper;
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
}
