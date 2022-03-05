package insecure.deserialization;

import java.io.Serializable;

public class Note implements Serializable{
    private Integer note_id;
    private String note_title;
    private String note_body;

    // Constructor
    public Note(Integer id, String title, String body){
        note_id = id;
        note_title = title;
        note_body = body;
    }

    public void print_note(){
        System.out.println(note_title + "\n" + note_body + "\n");
    }
}