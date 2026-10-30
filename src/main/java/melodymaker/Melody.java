package melodymaker;

import java.util.*;

public class Melody {

    private Deque<Note> queue = new LinkedList<>();
    private double duration = 0;

    public Melody(Deque<Note> notes) {
        while (!notes.isEmpty()) {
            queue.addLast(notes.getFirst());
            duration += notes.getFirst().getDuration();
            notes.pop();
        }
    }
    public double getTotalDuration() {
        return duration;
    }

    @Override
    public String toString() {
        Deque<Note> temp = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        while (!queue.isEmpty()) {
            Note note = queue.pollFirst();
            sb.append(note.toString()).append("\n");
            temp.offerLast(note);
        }

        while (!temp.isEmpty()) {
            queue.offerLast(temp.pollFirst()); // restore original order
        }

        return sb.toString();
    }

    public void changeTempo(double tempo) {
        Deque<Note> temp = new LinkedList<>();

        while (!queue.isEmpty()) {
            Note note = queue.pollFirst();
            note.setDuration(note.getDuration() * tempo);
            temp.offerLast(note);
        }

        duration *= tempo;

        while (!temp.isEmpty()) {
            queue.offerLast(temp.pollFirst()); // restore original order
        }
    }

    public void reverse() {
        queue = queue.reversed();
    }

    public void append(Melody other) {
        Deque<Note> otherSong = other.getNotes();

        while (!otherSong.isEmpty()) {
            queue.addLast(otherSong.getFirst());
            duration += otherSong.getFirst().getDuration();
            otherSong.pop();
        }

    }

    public void play() {
        Deque<Note> temp = new LinkedList<>();

        while (!queue.isEmpty()) {
            Note note = queue.pollFirst();
            note.play();
            temp.offerLast(note);
        }

        while (!temp.isEmpty()) {
            queue.offerLast(temp.pollFirst()); // restore original order
        }
    }

    public Deque<Note> getNotes() {
        return queue;
    }
}
