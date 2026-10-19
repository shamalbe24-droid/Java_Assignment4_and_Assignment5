import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourseRegistrationPortal {

    static class Course {
        private final String code;
        private final String title;
        private final int capacity;
        private final HashSet<String> enrolledStudentIds = new HashSet<>();
        private final ArrayList<String> prerequisiteCodes;
        private final LinkedList<String> waitlist = new LinkedList<>();

        Course(String code, String title, int capacity, List<String> prerequisites) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive.");
            }
            this.code = code;
            this.title = title;
            this.capacity = capacity;
            this.prerequisiteCodes = new ArrayList<>(prerequisites);
        }

        public String getCode() { return code; }
        public String getTitle() { return title; }
        public int getCapacity() { return capacity; }
        public Set<String> getEnrolledStudentIds() {
            return new HashSet<>(enrolledStudentIds);
        }
        public List<String> getPrerequisiteCodes() {
            return new ArrayList<>(prerequisiteCodes);
        }
        public List<String> getWaitlist() { return new ArrayList<>(waitlist); }

        private boolean hasSeat() {
            return enrolledStudentIds.size() < capacity;
        }
    }

    private final HashMap<String, Course> courses = new HashMap<>();
    private final HashMap<String, HashSet<String>> completedCourses = new HashMap<>();

    public void addCourse(String code, String title, int capacity, List<String> prerequisites) {
        if (courses.containsKey(code)) {
            System.out.println("Course already exists: " + code);
            return;
        }
        courses.put(code, new Course(code, title, capacity, prerequisites));
        System.out.println("Added course " + code + ": " + title);
    }

    public void markCourseCompleted(String studentId, String courseCode) {
        if (!courses.containsKey(courseCode)) {
            System.out.println("Unknown course: " + courseCode);
            return;
        }
        completedCourses.computeIfAbsent(studentId, key -> new HashSet<>()).add(courseCode);
    }

    public void registerStudent(String studentId, String courseCode) {
        Course course = courses.get(courseCode);
        if (course == null) {
            System.out.println("Registration failed: course not found.");
            return;
        }
        if (course.enrolledStudentIds.contains(studentId)) {
            System.out.println(studentId + " is already enrolled in " + courseCode + ".");
            return;
        }
        if (course.waitlist.contains(studentId)) {
            System.out.println(studentId + " is already waitlisted for " + courseCode + ".");
            return;
        }

        Set<String> completed = completedCourses.getOrDefault(studentId, new HashSet<>());
        ArrayList<String> missing = new ArrayList<>();
        for (String prerequisite : course.prerequisiteCodes) {
            if (!completed.contains(prerequisite)) {
                missing.add(prerequisite);
            }
        }
        if (!missing.isEmpty()) {
            System.out.println("Registration denied for " + studentId
                    + "; missing prerequisites: " + missing);
            return;
        }

        if (course.hasSeat()) {
            course.enrolledStudentIds.add(studentId);
            System.out.println(studentId + " registered for " + courseCode + ".");
        } else {
            course.waitlist.addLast(studentId);
            System.out.println(courseCode + " is full. " + studentId + " added to waitlist.");
        }
    }

    public void dropCourse(String studentId, String courseCode) {
        Course course = courses.get(courseCode);
        if (course == null) {
            System.out.println("Course not found: " + courseCode);
            return;
        }
        if (course.waitlist.remove(studentId)) {
            System.out.println(studentId + " removed from the waitlist.");
            return;
        }
        if (!course.enrolledStudentIds.remove(studentId)) {
            System.out.println(studentId + " is not enrolled in " + courseCode + ".");
            return;
        }

        System.out.println(studentId + " dropped " + courseCode + ".");
        promoteWaitlistedStudent(course);
    }

    private void promoteWaitlistedStudent(Course course) {
        while (course.hasSeat() && !course.waitlist.isEmpty()) {
            String nextStudent = course.waitlist.removeFirst();
            Set<String> completed = completedCourses.getOrDefault(
                    nextStudent, new HashSet<>());
            if (completed.containsAll(course.prerequisiteCodes)
                    && !course.enrolledStudentIds.contains(nextStudent)) {
                course.enrolledStudentIds.add(nextStudent);
                System.out.println("Waitlist promotion: " + nextStudent
                        + " enrolled in " + course.code + ".");
            } else {
                System.out.println("Skipped " + nextStudent
                        + " because prerequisites are not met.");
            }
        }
    }

    public void displayCourse(String courseCode) {
        Course course = courses.get(courseCode);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        System.out.println("\nCourse: " + course.code + " - " + course.title);
        System.out.println("Capacity: " + course.capacity);
        System.out.println("Prerequisites: " + course.prerequisiteCodes);
        System.out.println("Enrolled students: " + course.enrolledStudentIds);
        System.out.println("Waitlist (FIFO): " + course.waitlist);
    }

    public static void main(String[] args) {
        CourseRegistrationPortal portal = new CourseRegistrationPortal();

        portal.addCourse("CS101", "Programming Fundamentals", 3, new ArrayList<String>());
        portal.addCourse("CS201", "Data Structures", 1, Arrays.asList("CS101"));

        portal.markCourseCompleted("S101", "CS101");
        portal.markCourseCompleted("S102", "CS101");
        portal.markCourseCompleted("S103", "CS101");

        portal.registerStudent("S101", "CS201");
        portal.registerStudent("S102", "CS201");
        portal.registerStudent("S103", "CS201");
        portal.displayCourse("CS201");

        System.out.println("\nS101 drops CS201:");
        portal.dropCourse("S101", "CS201");
        portal.displayCourse("CS201");
    }
}
