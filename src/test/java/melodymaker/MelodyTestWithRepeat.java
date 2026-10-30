package melodymaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.Scanner;
import static melodymaker.MelodyMaker.read;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MelodyTestWithRepeat {
    private Melody melody;
    private Deque<Note> originalSong;
    private Deque<Note> newSong;
    
    public MelodyTestWithRepeat() {
       
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        File file = new File("hotcrossbuns.txt");
        Scanner input = new Scanner(file);
        originalSong = read(input);
        melody = new Melody(originalSong);
        newSong = melody.getNotes();
    }
    
    @Test
    public void testMelody()
    {
        // Test number of notes.
        assertEquals(10, newSong.size());
        
        // Test that the first note is flagged as the first note.
        assertTrue(newSong.peek().isFirstNote());
        
        // Test that the duration is set correctly.
        assertEquals(8, melody.getTotalDuration());
        
        // Test that the melody has been constructed with copies of the original notes.
        // Remove the first three notes from the original song (loaded from file).
        originalSong.removeFirst();
        originalSong.removeFirst();
        originalSong.removeFirst();
        
        // Melody should not have changed.
        assertEquals(10, newSong.size());
    }

    /**
     * Test of toString method, of class Melody.
     */
    @Test
    public void testToString() throws FileNotFoundException {
        String fileText = loadFile("hotcrossbuns.txt");
        String melodyText = melody.toString();
        assertEquals(fileText, melodyText);
    }
    
    private String loadFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner input = new Scanner(file);
        String fileText = "";
        
        while(input.hasNext())
        {
            fileText += input.nextLine() + "\n";
	}
        
        return fileText;
    }

    /**
     * Test of changeTempo method, of class Melody.
     */
    @Test
    public void testChangeTempo() {
        melody.changeTempo(2);
        assertEquals(16, melody.getTotalDuration());
    }

    /**
     * Test of reverse method, of class Melody.
     */
    @Test
    public void testReverse() {
        melody.reverse();
        newSong = melody.getNotes();
        
        // Test number of notes.
        assertEquals(10, newSong.size());
        
        // Test that the first note is flagged as the first note.
        assertTrue(newSong.peek().isFirstNote());
        
        // Test that the last note is not flagged as the first note.
        assertFalse(newSong.getLast().isFirstNote());
        
        // Test that the duration is set correctly.
        assertEquals(8, melody.getTotalDuration());
        
        // Reverse our comparison notes.
        originalSong = originalSong.reversed();
        originalSong.peekFirst().setFirstNote(true);
        originalSong.peekLast().setFirstNote(false);
        
        // Check that all notes match between our notes and melody's notes.
        Note originalNote;
        Note newNote;
        
        while (!newSong.isEmpty())
        {
            originalNote = originalSong.removeFirst();
            newNote = newSong.removeFirst();
            assertTrue(originalNote.equals(newNote));
        }
    }

    /**
     * Test of append method, of class Melody.
     */
    @Test
    public void testAppend() throws FileNotFoundException {
        File file = new File("epona.txt");
        Scanner input = new Scanner(file);
        Deque<Note> eponaNotes = read(input);
        Melody eponaMelody = new Melody(eponaNotes);
        melody.append(eponaMelody);
        newSong = melody.getNotes();
        
        // Test that our own notes have not been altered.
        assertTrue(eponaNotes.peek().isFirstNote());
        eponaNotes.peek().setFirstNote(false);
        originalSong.addAll(eponaNotes);
        
        // Test number of notes.
        assertEquals(18, newSong.size());
        
        // Test that the duration is set correctly.
        assertEquals(Math.round(14.6), Math.round(melody.getTotalDuration()));
        
        // Test that the first note is flagged as the first note.
        assertTrue(newSong.removeFirst().isFirstNote());
        originalSong.removeFirst();
        
        // Check that all notes match between our notes and melody's notes.
        Note originalNote;
        Note newNote;
        
        while (!newSong.isEmpty())
        {
            originalNote = originalSong.removeFirst();
            newNote = newSong.removeFirst();
            originalNote.equals(newNote);
            assertTrue(originalNote.equals(newNote));
        }
        
    }    
}
