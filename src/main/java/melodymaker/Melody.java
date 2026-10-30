package melodymaker;

import java.util.*;

public class Melody {

    private Deque<Note> queue = new LinkedList<>();

    public Melody(Deque<Note> notes) {
        Deque<Note> temp = new LinkedList<>();

        while (!notes.isEmpty()) {
            Note note = notes.pollFirst();
            queue.offerLast(note);
            temp.offerLast(note);
        }

        // put them back so the parameter's deque is unchanged
        while (!temp.isEmpty()) {
            notes.offerLast(temp.pollFirst());
        }
    }

    public double getTotalDuration() {
        Deque<Note> temp = new LinkedList<>();
        double total = 0;
        boolean inSection = false; // checks for repeat section
        double sectionSum = 0;

        while (!queue.isEmpty()) {
            Note n = queue.pollFirst();
            total += n.getDuration();
            if (n.isRepeat()) {
                if (!inSection) {
                    // the first true note begins the repeat section
                    inSection = true;
                    sectionSum = n.getDuration();
                } else {
                    // second true not ends the repeat section
                    sectionSum += n.getDuration();
                    total += sectionSum;      // replay the whole span
                    inSection = false;
                    sectionSum = 0; // reset the repeat section variables in case there is another one
                }
            } else if (inSection) {
                sectionSum += n.getDuration();
            }
            temp.offerLast(n);
        }

        return total;
    }

    @Override
    public String toString() {
        Deque<Note> temp = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        while (!queue.isEmpty()) {
            Note note = queue.pollFirst();
            sb.append(note.toString()).append("\n"); // added a newline for better formatting
            temp.offerLast(note);
        }

        while (!temp.isEmpty()) {
            queue.offerLast(temp.pollFirst()); // restore original order
        }

        return sb.toString();
    }

    public void changeTempo(double tempo) {
        if (tempo <= 0) {
            throw new IllegalArgumentException("tempo must be greater than zero");
        }

        Deque<Note> temp = new LinkedList<>();

        while (!queue.isEmpty()) {
            Note note = queue.pollFirst();
            note.setDuration(note.getDuration() * tempo); // this seems to work fine
            temp.offerLast(note);
        }

        while (!temp.isEmpty()) {
            queue.offerLast(temp.pollFirst()); // restore original order
        }
    }

    public void reverse() {
        Deque<Note> stack = new LinkedList<>();

        while (!queue.isEmpty()) {
            stack.push(queue.pollFirst());
        }

        while (!stack.isEmpty()) {
            queue.offerLast(stack.pop());
        }

        if (!queue.isEmpty()) {
            queue.peekFirst().setFirstNote(true);
            assert queue.peekLast() != null;
            queue.peekLast().setFirstNote(false);
        }
    }

    public void append(Melody other) {
        Deque<Note> temp = new LinkedList<>();

        while (!other.queue.isEmpty()) {
            Note note = other.queue.pollFirst();
            Note copy = new Note(note);
            queue.offerLast(copy);
            temp.offerLast(note);
        }

        while (!temp.isEmpty()) {
            other.queue.offerLast(temp.pollFirst()); // restore original order
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
        Deque<Note> copy = new LinkedList<>();
        Deque<Note> temp = new LinkedList<>();

        while (!queue.isEmpty()) {
            Note note = queue.pollFirst();
            copy.offerLast(note);
            temp.offerLast(note);
        }

        while (!temp.isEmpty()) {
            queue.offerLast(temp.pollFirst());
        }

        return copy;
    }
}
