/**Class that contains the potential next assignment*/
public class Next {
    public Course C;
    public Room R;

    public Next(Course course, Room room){
        C = course;
        R = room;
    }

    public String toString(){
        return "Course: " + C.enrolledStudents + " room: " + R.capacity;
    }
}
