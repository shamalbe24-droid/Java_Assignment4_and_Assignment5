import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

public class MediaPlayerQueueEngine {

    static class Song {
        private final String title;
        private final String artist;

        Song(String title, String artist) {
            this.title = title;
            this.artist = artist;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Song)) return false;
            Song other = (Song) object;
            return title.equalsIgnoreCase(other.title)
                    && artist.equalsIgnoreCase(other.artist);
        }

        @Override
        public int hashCode() {
            return (title.toLowerCase() + "\u0000" + artist.toLowerCase()).hashCode();
        }

        @Override
        public String toString() {
            return title + " - " + artist;
        }
    }

    private final LinkedList<Song> playlist = new LinkedList<>();
    private final LinkedList<Song> recentHistory = new LinkedList<>();
    private final int historyLimit;

    public MediaPlayerQueueEngine(int historyLimit) {
        if (historyLimit <= 0) {
            throw new IllegalArgumentException("History limit must be positive.");
        }
        this.historyLimit = historyLimit;
    }

    public void addSong(Song song) {
        playlist.addLast(song);
        System.out.println("Added: " + song);
    }

    public void insertSong(int index, Song song) {
        if (index < 0 || index > playlist.size()) {
            System.out.println("Invalid position: " + index);
            return;
        }
        playlist.add(index, song);
        System.out.println("Inserted at position " + index + ": " + song);
    }

    public void addPrioritySong(Song song) {
        playlist.addFirst(song);
        System.out.println("Priority song added: " + song);
    }

    public void removeDuplicates() {
        Set<Song> seen = new HashSet<>();
        ListIterator<Song> iterator = playlist.listIterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            if (!seen.add(song)) {
                iterator.remove();
            }
        }
        System.out.println("Duplicate songs removed.");
    }

    public Song playNext() {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty.");
            return null;
        }
        Song song = playlist.removeFirst();
        recentHistory.addFirst(song);
        while (recentHistory.size() > historyLimit) {
            recentHistory.removeLast();
        }
        System.out.println("Now playing: " + song);
        return song;
    }

    public void showHistory() {
        System.out.println("\nRecent history (newest first):");
        if (recentHistory.isEmpty()) {
            System.out.println("No songs played yet.");
        } else {
            for (Song song : recentHistory) {
                System.out.println(song);
            }
        }
    }

    public void traverseBothDirections() {
        ListIterator<Song> iterator = playlist.listIterator();

        System.out.println("\nForward traversal:");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("Backward traversal:");
        while (iterator.hasPrevious()) {
            System.out.println(iterator.previous());
        }
    }

    public void displayPlaylist() {
        System.out.println("\nCurrent playlist:");
        if (playlist.isEmpty()) {
            System.out.println("(empty)");
        } else {
            for (Song song : playlist) {
                System.out.println(song);
            }
        }
    }

    public static void main(String[] args) {
        MediaPlayerQueueEngine player = new MediaPlayerQueueEngine(5);

        player.addSong(new Song("Blinding Lights", "The Weeknd"));
        player.addSong(new Song("Levitating", "Dua Lipa"));
        player.addSong(new Song("Perfect", "Ed Sheeran"));
        player.addSong(new Song("Levitating", "Dua Lipa")); // duplicate

        player.insertSong(1, new Song("Yellow", "Coldplay"));
        player.addPrioritySong(new Song("Anti-Hero", "Taylor Swift"));

        player.displayPlaylist();
        player.removeDuplicates();
        player.displayPlaylist();
        player.traverseBothDirections();

        player.playNext();
        player.playNext();
        player.showHistory();
        player.displayPlaylist();
    }
}
