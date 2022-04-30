//ID:319024303

import java.io.Console;
import java.io.File;

public class Scouter implements Runnable {
    private int id;
    private SynchronizedQueue<File> directoryQueue;
    private SynchronizedQueue<String> auditingQueue;
    private File root;
    private boolean isAudit;

    /**
     * Constructor for Scouter and also Register a Scouter as a producer for the directoryQueue and logs file if needed.
     *
     * @param id             thread id
     * @param directoryQueue Synchronized Directory queue.
     * @param root           root file directory
     * @param auditingQueue  Synchronized logs queue
     * @param isAudit        logs flag
     */
    public Scouter(int id,
                   SynchronizedQueue<File> directoryQueue,
                   File root,
                   SynchronizedQueue<String> auditingQueue,
                   boolean isAudit) {
        this.directoryQueue = directoryQueue;
        this.auditingQueue = auditingQueue;
        this.id = id;
        this.root = root;
        this.isAudit = isAudit;
        //Checks if logs flag is True
        if (this.isAudit) {
            //Register Scout as producer for the logs queue
            this.auditingQueue.registerProducer();
        }
        //Register the Scout as a producer for the directory queue
        this.directoryQueue.registerProducer();
    }

    /**
     * Inserting into directoryQueue all directories under the root recursively.
     * If the isAudit flag is True also inserting logs into the auditingQueue.
     * when the function is finish unregistering the Scout from the queues(no longer producer).
     */
    @Override
    public void run() {
        //Calling the Recursive function
        recDirectoryFiles(this.root);
        //Unregistering the scout from queue
        this.directoryQueue.unregisterProducer();
        //Handling log queue
        if (this.isAudit) {
            this.auditingQueue.unregisterProducer();
        }
    }

    /**
     * Helper recursive function for the run method.
     *
     * @param root - root file directory
     */
    private void recDirectoryFiles(File root) {
        //Checks if current root is a directory.
        if (root.isDirectory()) {
            //Inserting current root to directory queues
            this.directoryQueue.enqueue(root);
            if (this.isAudit) {
                this.auditingQueue.enqueue("Scouter on thread id " + this.id + ": directory named " + root.toString() + "was scouted");
            }
            File filesList[] = root.listFiles();
            //For each file in the root if it's a directory calling recursive.
            for (File file : filesList) {
                if (file.isDirectory()) {
                    recDirectoryFiles(file);
                }
            }
        }
    }
}

