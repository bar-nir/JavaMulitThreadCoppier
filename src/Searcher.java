//ID:319024303

import java.io.File;

public class Searcher implements Runnable {
    private int id;
    private SynchronizedQueue<File> directoryQueue;
    private SynchronizedQueue<String> auditingQueue;
    private SynchronizedQueue<File> resultsQueue;
    private String prefix;
    private boolean isAudit;

    /**
     * Constructor for Searcher and also Register a Searcher as a producer for the resultsQueue and logs file if needed.
     *
     * @param id             Thread id.
     * @param prefix         Prefix for filtering files.
     * @param directoryQueue Directory queue.
     * @param resultsQueue   Result queue for filtered the filtered files.
     * @param auditingQueue  logs queue.
     * @param isAudit        logs flag.
     */
    public Searcher(int id,
                    String prefix,
                    SynchronizedQueue<File> directoryQueue,
                    SynchronizedQueue<File> resultsQueue,
                    SynchronizedQueue<String> auditingQueue,
                    boolean isAudit) {
        this.id = id;
        this.prefix = prefix;
        this.directoryQueue = directoryQueue;
        this.resultsQueue = resultsQueue;
        this.auditingQueue = auditingQueue;
        this.isAudit = isAudit;
//        this.directoryQueue.registerProducer();
        this.resultsQueue.registerProducer();
        if (this.isAudit) {
            this.auditingQueue.registerProducer();
        }

    }

    /**
     * Dequeue items from the directoryQueue until there are no items and no producer registered on the directory queue.
     * For each dequeue directory (item) inserting all files that start with Prefix into the result queue.
     */
    @Override
    public void run() {
        String name;
        //Dequeue first item.
        File item = this.directoryQueue.dequeue();
        //until there is Producer that are register on the directory queue and queue size is not 0 keep popping item from queue.
        // @see dequeue method in SynchronizedQueue.java.
        while (item != null) {
            //List of files under the current directory.
            File filesList[] = item.listFiles();
            for (File file : filesList) {
                //For each file checks if it is a file or directory.
                if (!file.isDirectory()) {
                    name = file.getName();
                    //Checks if the file start with prefix.
                    if (name.startsWith(this.prefix)) {
                        //enqueue into the result file
                        this.resultsQueue.enqueue(file);
                        //Handling log queue
                        if (this.isAudit) {
                            this.auditingQueue.enqueue("Searcher on thread id " + this.id + ": file named " + file.toString() + "was found");
                        }
                    }
                }
            }
            //dequeue the next directory.
            item = this.directoryQueue.dequeue();
        }
        //Unregister the thread from Result queue.
        this.resultsQueue.unregisterProducer();
        //Unregister the thread from logs queue.
        if (this.isAudit) {
            this.auditingQueue.unregisterProducer();
        }
    }
}
