public class Option {
    public Course C;
    public Room R;

    public Option(Course course, Room room){
        C = course;
        R = room;
    }

    public String toString(){
        return "Course: " + C.enrolledStudents + " room: " + R.capacity;
    }
}
